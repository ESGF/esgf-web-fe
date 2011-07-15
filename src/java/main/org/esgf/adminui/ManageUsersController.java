/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * “Licensor”) hereby grants to any person (the “Licensee”) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization’s name.  If the Software is protected by a proprietary
 * trademark owned by Licensor or the Department of Energy, then derivative
 * works of the Software may not be distributed using the trademark without
 * the prior written approval of the trademark owner.
 *
 * 2. Neither the names of Licensor nor the Department of Energy may be used
 * to endorse or promote products derived from this Software without their
 * specific prior written permission.
 *
 * 3. The Software, with or without modification, must include the following
 * acknowledgment:
 *
 *    "This product includes software produced by UT-Battelle, LLC under
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.”
 *
 * 4. Licensee is authorized to commercialize its derivative works of the
 * Software.  All derivative works of the Software must include paragraphs 1,
 * 2, and 3 above, and the DISCLAIMER below.
 *
 *
 * DISCLAIMER
 *
 * UT-Battelle, LLC AND THE GOVERNMENT MAKE NO REPRESENTATIONS AND DISCLAIM
 * ALL WARRANTIES, BOTH EXPRESSED AND IMPLIED.  THERE ARE NO EXPRESS OR
 * IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE,
 * OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY PATENT, COPYRIGHT,
 * TRADEMARK, OR OTHER PROPRIETARY RIGHTS, OR THAT THE SOFTWARE WILL
 * ACCOMPLISH THE INTENDED RESULTS OR THAT THE SOFTWARE OR ITS USE WILL NOT
 * RESULT IN INJURY OR DAMAGE.  The user assumes responsibility for all
 * liabilities, penalties, fines, claims, causes of action, and costs and
 * expenses, caused by, resulting from or arising out of, in whole or in part
 * the use, storage or disposal of the SOFTWARE.
 *
 *
 ******************************************************************************/


/**
 * On request mapping:
 *
 *     url rewrite filter will take over first, then we do regular Spring mapping.
 *     RedirectView is discouraged here as it will mess up the current rewrite
 *     rule, use "redirect:" prefix instead, and it is regarded as a better alternative
 *     anyway.
 *
 * For any redirect trouble, please refers to ROOT/urlrewrite.xml
 *
 * @author John Harney (harneyjf@ornl.gov)
 * @author Feiyi Wang (fwang2@ornl.gov)
 *
 */
package org.esgf.adminui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esgf.manager.InputManager;
import org.esgf.manager.InputManagerImpl;
import org.esgf.manager.OutputManager;
import org.esgf.manager.OutputManagerImpl;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import esg.search.query.api.FacetProfile;
import esg.search.query.api.SearchOutput;
import esg.search.query.api.SearchService;
import esg.search.query.impl.solr.SearchInputImpl;

@Controller
@RequestMapping(value="/usermanagement")

public class ManageUsersController {

    private final static String ManageUsers_INPUT = "ManageUsers_input";
    private final static String ManageUsers_USER = "ManageUsers_user";
    private final static String ManageUsers_MODEL = "ManageUsers_model";

    private final static Logger LOG = Logger.getLogger(ManageUsersController.class);
    private final static String USERS_FILE = "users.store";

    /**
     * List of invalid text characters -
     * anything that is not within square brackets.
     */
    private static Pattern pattern =
        Pattern.compile(".*[^a-zA-Z0-9_\\-\\.\\@\\'\\:\\;\\,\\s/()].*");

    public ManageUsersController() {
        LOG.debug("IN ManageUsersController Constructor");
    }

    /**
     * Method invoked in response to a GET request:
     * -) if invoked directly, a new set of facets is retrieved (but no results)
     * -) if invoked in response to a POST-REDIRECT,
     * @param request
     * @param input
     * @param result
     * @return
     * @throws IOException 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView doGet(final HttpServletRequest request,
            final @ModelAttribute(ManageUsers_INPUT) String ManageUsersInput) throws IOException {
        LOG.debug("In do get");
        
        Map<String,Object> model = new HashMap<String,Object>();

        
        if (request.getParameter(ManageUsers_MODEL)!=null) {
            LOG.debug("Not null");
            // retrieve model from session
            model = (Map<String,Object>)request.getSession().getAttribute(ManageUsers_MODEL);

        } else {
            LOG.debug("null");
            LOG.debug("ManageUsers Input: " + ManageUsersInput);
            

            
             
            User [] users = getUsersFromXML();
            
            // populate model
            model.put(ManageUsers_INPUT, ManageUsersInput);
            model.put(ManageUsers_USER, users);
            

            request.getSession().setAttribute(ManageUsers_MODEL, model);
            
        }
        return new ModelAndView("usermanagement", model);
    }
    
    private User [] getUsersHardCoded() {
        
        
        Group group1 = new Group("CDIAC","Standard","Valid");
        Group group2 = new Group("C-LAMP","Standard","Valid");
        
        Group [] user1_groups = {group1, group2}; 
        Group [] user2_groups = {group2}; 
       
        
        User user1 = new User("user1_lastName","user1_firstName","user1_userName","user1_emailAddress",
                "user1_status","user1_organization","user1_city","user1_state","user1_country","user1_openId","user1_DN",user1_groups);
        User user2 = new User("user2_lastName","user2_firstName","user2_userName","user2_emailAddress",
                "user2_status","user2_organization","user2_city","user2_state","user2_country","user2_openId","user2_DN",user2_groups);
        
        User [] users = {user1,user2};
        
        return users;
    }
    
    private User [] getUsersFromXML() throws IOException {
        
        LOG.debug("In getUsers()");
        
        /* this logic is deprecated and only used for testing - it utilizes the xml in users.store */
        final File file = new ClassPathResource(USERS_FILE).getFile();

        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        User [] userArray = new User[1];
        
        try{

            Document document = (Document) builder.build(file);
            LOG.debug("Building document");
            
            Element rootNode = document.getRootElement();
            LOG.debug("root name: " + rootNode.getName());
            
            List users = (List)rootNode.getChildren();
            LOG.debug(users.size());
            userArray =  new User[users.size()];
            
            for(int i=0;i<users.size();i++)
            {
                
                Element userEl = (Element) users.get(i);
                LOG.debug("USER: " + i);
                List attributes = (List)userEl.getChildren();
                
                String firstName = "";
                String lastName = "";
                String userName = "";
                String emailAddress = "";
                String status = "";
                String organization = "";
                String city = "";
                String state = "";
                String country = "";
                String openId = "";
                String DN = "";
                String groups = "";
                
                for(int j=0;j<attributes.size();j++)
                {
                    Element attEl = (Element) attributes.get(j);
                    if(attEl.getName().equals("firstName")) {
                        firstName = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("lastName")) {
                        lastName = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("userName")) {
                        userName = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("emailAddress")) {
                        emailAddress = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("status")) {
                        status = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("organization")) {
                        organization = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("city")) {
                        city = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("state")) {
                        state = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("country")) {
                        country = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("openId")) {
                        openId = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("DN")) {
                        DN = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("groups")) {
                        groups = attEl.getTextNormalize();
                        
                    }
                }
                
                //create new User object
                //generic groups for now
                Group grp = new Group("CDIAC","Standard","Valid");
                Group [] grps = {grp};
                User user = new User(firstName,lastName,userName,emailAddress,status,organization,city,state,country,openId,DN,grps);
                
                userArray[i] = user;
                
            }
            
        }catch(Exception e) {
            LOG.debug("File not found");
        }
        
        return userArray;
    }
    
    
    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ModelAttribute(ManageUsers_INPUT)
    public String formManageUsersInputObject(final HttpServletRequest request) throws Exception {

        LOG.debug("formManageUsersInputObject called");


        return "ManageUsers_Table here";

    }

}

