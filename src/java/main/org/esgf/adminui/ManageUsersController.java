/*****************************************************************************
 * Copyright � 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * �Licensor�) hereby grants to any person (the �Licensee�) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization�s name.  If the Software is protected by a proprietary
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
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.�
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
 *
 * @author John Harney (harneyjf@ornl.gov)
 * @author Feiyi Wang (fwang2@ornl.gov)
 *
 */
package org.esgf.adminui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esgf.commonui.UserOperationsESGFDBImpl;
import org.esgf.commonui.UserOperationsInterface;
import org.esgf.commonui.UserOperationsXMLImpl;
import org.esgf.commonui.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/usermanagement")

/*
 * Insert class description here...Basically manages the user management view
 */
public class ManageUsersController {

    private final static String ManageUsers_INPUT = "ManageUsers_input";
    private final static String ManageUsers_USER = "ManageUsers_user";
    private final static String ManageUsers_MODEL = "ManageUsers_model";

    private final static Logger LOG = Logger.getLogger(ManageUsersController.class);
    
    private UserOperationsInterface uoi;
    
   
    
    public ManageUsersController() throws FileNotFoundException, IOException {
        LOG.debug("IN ManageUsersController Constructor");
        //declare a UserOperations "Object"
        //uoi = new UserOperationsESGFDBImpl();
        uoi = new UserOperationsXMLImpl();
    }

    /**
     * Method invoked in response to a GET request:
     * --fill in description later
     * @param request
     * @param input
     * @param result
     * @return
     * @throws IOException 
     * @throws Exception
     */
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView doGet(final HttpServletRequest request,
            final @ModelAttribute(ManageUsers_INPUT) String ManageUsersInput) throws IOException {

        LOG.debug("------ManageUsersController doGet------");
        
        //get the model given the httprequest and the manageusersinput model attr
        Map<String,Object> model = getModel(request,ManageUsersInput);
       
        
        LOG.debug("------End ManageUsersController doGet------");
        
        return new ModelAndView("usermanagement", model);
    }
    
    
    /**
     * Method invoked in response to a POST request:
     * --fill in description later
     * @param request
     * @param input
     * @param result
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView doPost(final HttpServletRequest request,
            final @ModelAttribute(ManageUsers_INPUT) String ManageUsersInput) throws IOException {
            
        LOG.debug("------ManageUsersController doPost------");

        //get the userId from the cookie
        String userId = Utils.getIdFromHeaderCookie(request);
        LOG.debug("UserId Retrieved: " + userId);
        
        //get the type of operation from the request parameter (add, edit, delete)
        String type = Utils.getTypeFromQueryString(request);
        
        
        LOG.debug("TYPE->" + type);
        
        //from the type perform the appropriate operation
        if(type.equalsIgnoreCase("add")) {
            addUser(request);
        }
        else if(type.equalsIgnoreCase("edit")){
            editUser(request);
        }
        else if(type.equalsIgnoreCase("delete")) {
            deleteUser(request);
        }
        //otherwise ignore and return the model
        
        //get the model given the httprequest and the manageusersinput model attr
        Map<String,Object> model = getModel(request,ManageUsersInput);
        
        LOG.debug("------End ManageUsersController doPost------");
        return new ModelAndView("usermanagement", model);
    }

    
    /* Helper function for extracting the model */
    private Map<String,Object> getModel(final HttpServletRequest request,
                                       final @ModelAttribute(ManageUsers_INPUT)  String ManageUsersInput) throws IOException {
        LOG.debug("------ManageUsersController getModel------");
        Map<String,Object> model = new HashMap<String,Object>();
        
        if (request.getParameter(ManageUsers_MODEL)!=null) {
            // retrieve model from session
            model = (Map<String,Object>)request.getSession().getAttribute(ManageUsers_MODEL);

        } else {
            
            List<User> users = uoi.getAllUsers();
            //convert to user array so jsp doesn't complain
            User [] userArray = users.toArray(new User[users.size()]);
        
            // populate model
            model.put(ManageUsers_INPUT, ManageUsersInput);
            //LOG.debug("About to plug in USERS");
            model.put(ManageUsers_USER, userArray);
            //LOG.debug("After plug in USERS");
            request.getSession().setAttribute(ManageUsers_MODEL, model);
            
        }

        LOG.debug("------End ManageUsersController getModel------");
        return model;
    }
    
    
    
    
    /*
     * Helper method to formulate the model for the usermanagement view
     */
    private Map<String,Object> formModel(final HttpServletRequest request,final @ModelAttribute(ManageUsers_INPUT) String ManageUsersInput) throws IOException{

        LOG.debug("------ManageUsersController formModel------");
        
        Map<String,Object> model = new HashMap<String,Object>();

        if (request.getParameter(ManageUsers_MODEL)!=null) {
            LOG.debug("Not null");
            // retrieve model from session if needed
            model = (Map<String,Object>)request.getSession().getAttribute(ManageUsers_MODEL);

        } else {
            LOG.debug("ManageUsers Input: " + ManageUsersInput);
            
            List<User> users = uoi.getAllUsers();
            
            // populate model
            model.put(ManageUsers_INPUT, ManageUsersInput);
            LOG.debug("About to plug in USERS");
            model.put(ManageUsers_USER, users);
            LOG.debug("After plug in USERS");
            
            
            request.getSession().setAttribute(ManageUsers_MODEL, model);
        }
        LOG.debug("------End ManageUsersController formModel------");
        return model;
    }
    
    private void editUser(final HttpServletRequest request) throws IOException {
        LOG.debug("------ManageUsersController editUser------");
        
        String userName = request.getParameter("userName");
        LOG.debug("USERNAME->" + userName);
        
        String first = request.getParameter("firstName");
        if(first == null || first.equals("")) {
            first = "N/A";
        }
        String middle = request.getParameter("middle");
        if(middle == null || middle.equals("")) {
            middle = "N/A";
        }
        String last = request.getParameter("lastName");
        if(last == null || last.equals("")) {
            last = "N/A";
        }
        String email = request.getParameter("email");
        if(email == null || email.equals("")) {
            email = "N/A";
        }
        String organization = request.getParameter("organization");
        if(organization == null || organization.equals("")) {
            organization = "N/A";
        }
        String city = request.getParameter("city");
        if(city == null || city.equals("")) {
            city = "N/A";
        }
        String state = request.getParameter("state");
        if(state == null || state.equals("")) {
            state = "N/A";
        }
        String country = request.getParameter("country");
        if(country == null || country.equals("")) {
            country = "N/A";
        }
        
        String userId = uoi.getUserIdFromUserName(userName);
        uoi.editUser(userId,first,middle,last,email,userName,organization,city,state,country);

        
        
        
        
        LOG.debug("------End ManageUsersController editUser------");
    }
    
    
    
    private void deleteUser(final HttpServletRequest request) throws IOException {
        LOG.debug("------ManageUsersController deleteUser------");
        
        String userName = request.getParameter("user");
        
        LOG.debug("\n\n\n\n\n");
        LOG.debug("userName->"+userName);
        LOG.debug("\n\n\n\n\n");
        
        String userId = uoi.getUserIdFromUserName(userName);
        uoi.deleteUser(userName);
        
        LOG.debug("------End ManageUsersController deleteUser------");
    }
    
    
    
    private void addUser(final HttpServletRequest request) throws IOException {
        LOG.debug("------ManageUsersController addUser------");
        
        String username = request.getParameter("userName");
        if(username == null || username.equals("")) {
            username = "N/A";
        }
        String first = request.getParameter("firstName");
        if(first == null || first.equals("")) {
            first = "N/A";
        }
        String middle = request.getParameter("middle");
        if(middle == null || middle.equals("")) {
            middle = "N/A";
        }
        String last = request.getParameter("lastName");
        if(last == null || last.equals("")) {
            last = "N/A";
        }
        String email = request.getParameter("email");
        if(email == null || email.equals("")) {
            email = "N/A";
        }
        String organization = request.getParameter("organization");
        if(organization == null || organization.equals("")) {
            organization = "N/A";
        }
        String city = request.getParameter("city");
        if(city == null || city.equals("")) {
            city = "N/A";
        }
        String state = request.getParameter("state");
        if(state == null || state.equals("")) {
            state = "N/A";
        }
        String country = request.getParameter("country");
        if(country == null || country.equals("")) {
            country = "N/A";
        }
        
        //using the xml store
        uoi.addUser(first,middle,last,email,username,organization,city,state,country);

        LOG.debug("------End ManageUsersController addUser------");
        
    }
    
    
    
    
    
}


