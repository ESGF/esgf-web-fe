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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esgf.commonui.GroupOperationsInterface;
import org.esgf.commonui.GroupOperationsESGFDBImpl;
import org.esgf.commonui.GroupOperationsXMLImpl;
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
@RequestMapping(value="/creategroups")

public class CreateGroupsController {

    private final static String CreateGroups_MISC = "CreateGroups_misc";
    private final static String CreateGroups_INPUT = "CreateGroups_input";
    private final static String CreateGroups_MODEL = "CreateGroups_model";
    private final static String CreateGroups_GROUP = "CreateGroups_group";

    private final static Logger LOG = Logger.getLogger(CreateGroupsController.class);

    private GroupOperationsInterface goi;
    private UserOperationsInterface uoi;
    
    
    public CreateGroupsController() throws FileNotFoundException, IOException {
        LOG.debug("IN CreateGroupsController Constructor");
        if(Utils.environmentSwitch) {
            goi = new GroupOperationsESGFDBImpl();
            uoi = new UserOperationsESGFDBImpl();
        }
        else {
            goi = new GroupOperationsXMLImpl();
            uoi = new UserOperationsXMLImpl();
        }
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
            final @ModelAttribute(CreateGroups_MISC) String CreateGroupsMisc,
            final @ModelAttribute(CreateGroups_INPUT) String CreateGroupsInput) throws IOException {
        LOG.debug("------CreateGroupsController doGet------");
        
        
        Map<String,Object> model = getModel(request,CreateGroupsInput);
        
        
        LOG.debug("------End CreateGroupsController doGet------");
        return new ModelAndView("creategroups", model);
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
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView doPost(final HttpServletRequest request,
            final @ModelAttribute(CreateGroups_MISC) String CreateGroupsMisc,
            final @ModelAttribute(CreateGroups_INPUT) String CreateGroupsInput) throws IOException {
        LOG.debug("------CreateGroupsController doPost------");
        
        
      //get the userId from the cookie
        String userId = Utils.getIdFromHeaderCookie(request);
        
        //get the type of operation from the request parameter (add, edit, delete)
        String type = Utils.getTypeFromQueryString(request);
        
        String groupName = request.getParameter("groupName");

        
        LOG.debug("Type->" + type);
        LOG.debug("GroupName|->" + groupName);
        
        //from the type perform the appropriate operation
        if(type.equalsIgnoreCase("add")) {
            addGroup(request);
        }
        else if(type.equalsIgnoreCase("edit")){
            editGroup(request);
        }
        else if(type.equalsIgnoreCase("delete")) {
            
            //delete the rootAdmin user first
            
            
            deleteGroup(request);
        } else if(type.equalsIgnoreCase("editUsersInGroup")) {
            
            
            Enumeration<String> paramEnum = request.getParameterNames();
            
            
            List<User> users = uoi.getAllUsers();
            List<User> checkedUsers = new ArrayList<User>();
            
            //first find the groups that were added
            while(paramEnum.hasMoreElements()) { 
                String postContent = (String) paramEnum.nextElement();
                
                String userName = request.getParameter(postContent);

                if(!postContent.equals("groupName") && !postContent.equals("type")) {

                    //System.out.println("Adding User->" + userName + " from group " + groupName);
                    checkedUsers.add(uoi.getUserObjectFromUserName(userName));
                    uoi.addUserToGroup(userName, groupName);
                }
            }
            
            //next find the users that were excluded from the check list and delete them
            //i.e. delete whatever user is leftover
            for(int i=0;i<users.size();i++) {
                User user = users.get(i);
                boolean canDelete = true;
                for(int j=0;j<checkedUsers.size();j++) {
                    User checkedUser = checkedUsers.get(j);
                    if(user == null) {
                        System.out.println("\t\t[->user is null");
                    }
                    if(checkedUser != null) {
                        if(user.getUserName().equalsIgnoreCase(checkedUser.getUserName())) {
                            canDelete = false;
                        }
                    }
                    
                }
                if(canDelete) {
                    System.out.println("[->Deleting User->" + user.getUserName() + " from group " + groupName);
                
                    //delete the user from the group
                    uoi.deleteUserFromGroup(user.getUserName(), groupName);
                    
                }
                
                
            }
            
            
        }
       
        Map<String,Object> model = getModel(request,CreateGroupsInput);
        
        
        LOG.debug("------End CreateGroupsController doPost------");
        
        return new ModelAndView("creategroups", model);
    }
    
    
    private void editGroup(final HttpServletRequest request) throws IOException {
        LOG.debug("------ManageUsersController editUser------");
        
        Utils.queryStringInfo(request);
        
        
        //groupId = goi.getGroupIdFromGroupName(request.getParameter("groupName"));
        
        String groupName = request.getParameter("groupName");
        if(groupName == null || groupName.equals("")) {
            groupName = "N/A";
        }
        String groupDescription = request.getParameter("groupDescription");
        if(groupDescription == null || groupDescription.equals("")) {
            groupDescription = "N/A";
        }
        
        //String groupId = goi.getGroupIdFromGroupName(groupName);//request.getParameter("id");
        /* NEED TO FIX THIS */
        /*
        Group group = goi.getGroupObjectFromGroupName(groupName);
        String groupId = group.getid();
        
        LOG.debug("gId: " + groupId + " groupName: " + groupName + " groupDescription: " + groupDescription);
        goi.editGroup(groupId, groupName, groupDescription);
        */
        
        LOG.debug("------End CreateGroupsController editUser------");
    }
    
    
    
    private void deleteGroup(final HttpServletRequest request) throws IOException {
        LOG.debug("------CreateGroupsController deleteUser------");
        
        //Utils.queryStringInfo(request);
        String groupName = request.getParameter("groupName");
        
        String groupId = goi.getGroupIdFromGroupName(groupName);
        
        //delete rootAdmin user from group first
        uoi.deleteUserFromGroup("rootAdmin", groupName);
        
        LOG.debug("Deleteing->" + groupId);
        goi.deleteGroup(groupName);

        LOG.debug("------End CreateGroupsController deleteUser------");

    }
    
    
    
    private void addGroup(final HttpServletRequest request) throws IOException {
        LOG.debug("------CreateGroupsController addUser------");
        
        Utils.queryStringInfo(request);
        
        String groupName = request.getParameter("groupName");
        if(groupName == null || groupName.equals("")) {
            groupName = "N/A";
        }
        String groupDescription = request.getParameter("groupDescription");
        if(groupDescription == null || groupDescription.equals("")) {
            groupDescription = "N/A";
        }

        goi.addGroup(groupName, groupDescription);
        

        //must add the root user to the group
        uoi.addUserToGroup("rootAdmin", groupName);
        
        
        
        LOG.debug("------CreateGroupsController addUser------");
        
    }
    
    
    
    
    /* Helper function for extracting the model */
    @SuppressWarnings("unchecked")
    private Map<String,Object> getModel(final HttpServletRequest request,
                                       final @ModelAttribute(CreateGroups_INPUT)  String ManageUsersInput) throws IOException {
        LOG.debug("------CreateGroupsController getModel------");
        Map<String,Object> model = new HashMap<String,Object>();
        
        if (request.getParameter(CreateGroups_MODEL)!=null) {
            // retrieve model from session
            model = (Map<String,Object>)request.getSession().getAttribute(CreateGroups_MODEL);

        } else {
            
            List<Group> groups = goi.getAllGroups();
            //convert to user array so jsp doesn't complain
            Group [] groupArray = groups.toArray(new Group[groups.size()]);
            /*
            List<User> users = uoi.getAllUsers();
            User [] userArray = users.toArray(new User[users.size()]);
            */
            
            System.out.println("groupArr|->" + groupArray.length);
            // populate model
            model.put(CreateGroups_INPUT, ManageUsersInput);
            //LOG.debug("About to plug in USERS");
            model.put(CreateGroups_GROUP, groupArray);
            //LOG.debug("After plug in USERS");
            request.getSession().setAttribute(CreateGroups_MODEL, model);
            
        }

        LOG.debug("------End CreateGroupsController getModel------");
        return model;
    }
    
    
    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ModelAttribute(CreateGroups_MISC)
    public String formCreateGroupsMiscObject(final HttpServletRequest request) throws Exception {
        LOG.debug("formCreateGroupsMiscObject");
        return "CreateGroups_MISC here";
    }
    
    
    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ModelAttribute(CreateGroups_INPUT)
    public String formCreateGroupsInputObject(final HttpServletRequest request) throws Exception {
        LOG.debug("formCreateGroupsInputObject called");
        return "CreateGroups_Table here";

    }

}

