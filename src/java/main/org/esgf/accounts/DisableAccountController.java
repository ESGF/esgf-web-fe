package org.esgf.accounts;

import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import esg.node.security.ESGFDataAccessException;
import esg.node.security.UserInfo;
import esg.node.security.UserInfoCredentialedDAO;
import esg.node.security.UserInfoDAO;

/**
 * Controller to disable a user account.
 * Currently the controller can be invoked by anybody how knows the proper (openid, token) combination.
 * 
 * @author Luca Cinquini
 *
 */
@Controller
@RequestMapping("/disableAccount")
public class DisableAccountController {
    
    private final static Log LOG = LogFactory.getLog(DisableAccountController.class);
    
    private final static String MODEL_NAME = "user";
    private final static String GET_VIEW = "accounts/disableAccountGetView";
    private final static String POST_VIEW = "accounts/disableAccountPostView";
    
    /**
     * Secured database access class.
     */
    private UserInfoCredentialedDAO userInfoDAO = null;

    @Autowired
    public DisableAccountController(final @Qualifier("esgfProperties") Properties props) {
        
        try {
            userInfoDAO = new UserInfoCredentialedDAO("rootAdmin", props.getProperty("security.admin.passwd"), props);
        } catch(Exception e) {
            LOG.error(e.getMessage());
        }
        
    }
    
    @RequestMapping(method=RequestMethod.GET)
    protected String doGet(final HttpServletRequest request, final @ModelAttribute(MODEL_NAME) DisableAccountBean user) throws ServletException {
        return GET_VIEW;
    }
    
    @RequestMapping(method=RequestMethod.POST)
    protected String doPost(final HttpServletRequest request, final @ModelAttribute(MODEL_NAME) DisableAccountBean user) throws ServletException {
        
        LOG.warn("Disabling account for user: "+user.getOpenid());
        try {
            
            boolean success = userInfoDAO.changeStatus(user.getOpenid(), UserInfo.DISABLED, user.getToken());
            if (success) {
                user.setMessage("The account for user: "+user.getOpenid()+" has been disabled");
            } else {
                user.setMessage("Account disabling failed - please contact the site administrator.");
            }
            
        } catch(ESGFDataAccessException e) {
            LOG.equals(e.getMessage());
            user.setMessage("Account disabling failed - please contact the site administrator.");
        }
        
        return POST_VIEW;
    }

}
