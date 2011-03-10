package org.esgf.accounts;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esgf.security.OpenidCookieFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import esg.node.security.ESGFDataAccessException;
import esg.node.security.GroupRoleDAO;
import esg.node.security.UserInfo;
import esg.node.security.UserInfoCredentialedDAO;

@Controller
@RequestMapping("/createAccount")
public class CreateAccountController {
    
    private final static String FORM_VIEW = "accounts/createAccountForm";
    private final static String SUCCESS_VIEW = "accounts/createAccountSuccess";
    private final static String CONFIRM_VIEW = "accounts/confirmAccount";
    private final static String MODEL_NAME = "user";
        
    private static final int PASSWORD_MIN_LENGHT = 6;
    public final static String PAR_URL = "url";
    public final static String PAR_DATE = "date";
    
    private final static String GROUP_NAME = "CMIP5 Research";
    private final static String ROLE_NAME = "User";
    
    private final static Log LOG = LogFactory.getLog(CreateAccountController.class);
    
    /**
     * Secured database access class.
     */
    private UserInfoCredentialedDAO userInfoDAO = null;
    private GroupRoleDAO groupRoleDAO = null;
    
    private AccountsNotifier notifier;
    
    @Autowired
    public CreateAccountController(final @Qualifier("dbProperties") Properties props) {
        try {
            this.userInfoDAO = new UserInfoCredentialedDAO("rootAdmin", props.getProperty("security.admin.passwd"), props);
            this.groupRoleDAO = new GroupRoleDAO(props);
            // FIXME
            groupRoleDAO.addGroup(GROUP_NAME);
            groupRoleDAO.addRole(ROLE_NAME);
        } catch(Exception e) {
            LOG.warn(e.getMessage());
        }
        
        notifier = new AccountsEmailNotifier(props);
    }
    
    /**
     * Method used to instantiate a valid {@link CreateAccountBean} object.
     * This method is invoked before the GET/POST request handler and before HTTP parameters binding.
     * @param request
     * @return
     */
    @ModelAttribute(MODEL_NAME)
    public CreateAccountBean formBackingObject(final HttpServletRequest request) {
        
        // create empty user
        final CreateAccountBean user = new CreateAccountBean( userInfoDAO.getNewUserInfo() );
        LOG.info("Created empty UserInfo object");
        return user;
        
    }

    @RequestMapping(method=RequestMethod.GET)
    protected ModelAndView doGet(final HttpServletRequest request, final @ModelAttribute(MODEL_NAME) CreateAccountBean user) throws Exception {
                
        /* FIXME */
        user.setUserName("testman");
        user.setFirstName("Tester");
        user.setMiddleName("Middle");
        user.setLastName("Smith");
        user.setCity("Los Angeles");
        user.setOrganization("JPL");
        user.setCountry("U.S.");
        user.setEmail("joe.tester@test.com");
        user.setState("California");

        final Map<String,Object> model = new HashMap<String,Object>();
        model.put(MODEL_NAME, user);
        return new ModelAndView(FORM_VIEW, model);

    }
    
    @RequestMapping(method=RequestMethod.POST)
    protected ModelAndView doPost(final HttpServletRequest request, final @ModelAttribute(MODEL_NAME) CreateAccountBean user, final BindingResult errors) throws Exception {
        
        try {
            
            // validate user input
            validate(user, errors);
            
            if (errors.hasErrors()) {
                return new ModelAndView(FORM_VIEW).addObject(MODEL_NAME, user);
                
            } else {
                
                // persist user to the database
                boolean success =  userInfoDAO.addUserInfo(user.getUser());
                
                if (!success) {
                    errors.reject("error.invalid", new Object[] {}, "Database ingestion error");
                    return new ModelAndView(FORM_VIEW).addObject(MODEL_NAME, user);
                    
                } else {
                    
                    // set password
                    userInfoDAO.setPassword(user.getUser(), user.getPassword1());
                    // generate verification token
                    final String verificationToken = userInfoDAO.genVerificationToken(user.getOpenid());
                    // set permissions - FIXME
                    userInfoDAO.addPermission(user.getUser(), GROUP_NAME, ROLE_NAME);
                    
                    // notify user
                    notifier.accountCreated(getServerUrl(request), user.getUser(), verificationToken);
                                    
                    // use POST-REDIRECT-GET pattern with additional model "?openid_identifier=...&remember_openid=..." to set openid cookie
                    String confirmUrl = CONFIRM_VIEW + "?token="+user.getUserName(); // FIXME
                    return new ModelAndView(new RedirectView(SUCCESS_VIEW)).addObject(OpenidCookieFilter.PARAMETER_OPENID, user.getOpenid())
                                                                           .addObject(OpenidCookieFilter.PARAMETER_REMEMBERME, "on")
                                                                           .addObject(PAR_URL, confirmUrl );
                    
                }
                
            }
        
        // capture database runtime exception and transform into servlet exceptions for proper handling
        } catch(ESGFDataAccessException e) {
            throw new ServletException(e);
        }
           
    }
    
    private final String getServerUrl(final HttpServletRequest request) {
        return request.getScheme()
               + "://"
               + request.getServerName()
               + (StringUtils.hasText(request.getServerPort()+"") ? ":"+request.getServerPort() : "")
               + request.getContextPath();
    }
    
    /**
     * Method to validate user input
     * @param user
     * @param errors
     */
    private final void validate(final CreateAccountBean user, final BindingResult errors) {
        
        // validate first name
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "error.required", "'First Name' is required");
        if (StringValidationUtils.hasInvalidCharacters(user.getFirstName())) errors.rejectValue("firstName", "error.invalid", new Object[] {}, getInvalidCharactersErrorMessage("First Name"));

        // validate middle name
        if (StringValidationUtils.hasInvalidCharacters(user.getMiddleName())) errors.rejectValue("middleName", "error.invalid", new Object[] {}, getInvalidCharactersErrorMessage("Middle Name"));

        // validate last name
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "error.required", "'Last Name' is required");
        if (StringValidationUtils.hasInvalidCharacters(user.getLastName())) errors.rejectValue("lastName", "error.invalid", new Object[] {}, getInvalidCharactersErrorMessage("Last Name"));

        // validate email
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.required", "'Email' is required");
        if (StringValidationUtils.hasInvalidCharacters(user.getEmail())) errors.rejectValue("email", "error.invalid", new Object[] {}, getInvalidCharactersErrorMessage("Email"));
        if (!StringValidationUtils.isEmail(user.getEmail())) errors.rejectValue("email", "error.invalid", new Object[] {}, "'Email' is not well formed");

        // verify username
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "error.required", "Username is required");
        if (StringValidationUtils.isNotAlphanumeric(user.getUserName())) errors.rejectValue("userName", "error.invalid", new Object[] {}, "'User Name' contains invalid characters");
        final UserInfo _user = userInfoDAO.getUserById(user.getUserName());
        if (_user!=null && _user.isValid()) errors.rejectValue("userName", "error.notunique", new Object[] {}, "'User Name' already taken");
        
        // verify password
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password1", "error.required", "'Password' is required");
        if (user.getPassword1().length()<PASSWORD_MIN_LENGHT) errors.rejectValue("password1", "error.invalid", new Object[] {}, "'Password' lenght must be at least "+PASSWORD_MIN_LENGHT+" characters");
        if (!StringValidationUtils.hasOneUpperCaseLetter(user.getPassword1())) errors.rejectValue("password1", "error.invalid", new Object[] {}, "'Password' must contain at least one upper case letter");
        if (!StringValidationUtils.hasOneLowerCaseLetter(user.getPassword1())) errors.rejectValue("password1", "error.invalid", new Object[] {}, "'Password' must contain at least one lower case letter");
        if (!StringValidationUtils.hasOneNumber(user.getPassword1())) errors.rejectValue("password1", "error.invalid", new Object[] {}, "'Password' must contain at least one number");
        if (!user.getPassword1().equals(user.getPassword2())) errors.rejectValue("password2", "error.invalid", new Object[] {}, "'Password' and 'Confirm Password' do not match");
        
        // validate organization
        if (StringValidationUtils.hasInvalidCharacters(user.getOrganization())) errors.rejectValue("organization", "error.invalid", new Object[] {}, getInvalidCharactersErrorMessage("Organization"));

        // validate city
        if (StringValidationUtils.hasInvalidCharacters(user.getCity())) errors.rejectValue("city", "error.invalid", new Object[] {}, getInvalidCharactersErrorMessage("City"));

        // validate state
        if (StringValidationUtils.hasInvalidCharacters(user.getState())) errors.rejectValue("state", "error.invalid", new Object[] {}, getInvalidCharactersErrorMessage("State"));

        // validate country
        if (StringValidationUtils.hasInvalidCharacters(user.getCountry())) errors.rejectValue("country", "error.invalid", new Object[] {}, getInvalidCharactersErrorMessage("Country"));

    }
    
    private final String getInvalidCharactersErrorMessage(final String field) {
        return "'" + field + "' cannot contain any of the characters: > < # $ & ! / \\";
    }

}
