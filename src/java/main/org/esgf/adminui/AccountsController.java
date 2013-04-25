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
 * @author Feiyi Wang (fwang2@ornl.gov)
 *
 */
package org.esgf.adminui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import org.esgf.commonui.UserOperationsESGFDBImpl;
import org.esgf.commonui.UserOperationsInterface;
import org.esgf.commonui.UserOperationsXMLImpl;
import org.esgf.commonui.Utils;

@Controller
@RequestMapping(value="/accountsview")

public class AccountsController {

    private final static String ACCOUNTS_INPUT = "accounts_input";
    private final static String ACCOUNTS_USERINFO = "accounts_userinfo";
    private final static String ACCOUNTS_GROUPINFO = "accounts_groupinfo";
    private final static String ACCOUNTS_ROLEINFO = "accounts_roleinfo";
    private final static String ACCOUNTS_MODEL = "accounts_model";
    private final static String ACCOUNTS_ERROR = "accounts_error";

    private final static Logger LOG = Logger.getLogger(AccountsController.class);
    private UserOperationsInterface uoi;
    private String openId;

    private final static boolean debugFlag = true;

    public AccountsController() throws FileNotFoundException, IOException {
        LOG.debug("IN AccountsController Constructor");
        if(Utils.environmentSwitch) {
            uoi = new UserOperationsESGFDBImpl();
        }
        else {
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
     * @throws JDOMException 
     * @throws Exception
     */
    //@SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView doGet(final HttpServletRequest request,
            final @ModelAttribute(ACCOUNTS_INPUT) String accountsInput) throws JDOMException, IOException {

        LOG.debug("In do get");

        openId = Utils.getIdFromHeaderCookie(request);
        if(debugFlag) {
            LOG.debug("UserId Retrieved: " + openId);
        }
        
        Map<String,Object> model = getModel(request,accountsInput);

        return new ModelAndView("accountsview", model);
    }
    
    /* Helper function for extracting the model */
    @SuppressWarnings("unchecked")
    private Map<String,Object> getModel(final HttpServletRequest request,
                                       final @ModelAttribute(ACCOUNTS_INPUT)  String accountsInput) throws IOException {
        LOG.debug("------AccountsController getModel------");
        Map<String,Object> model = new HashMap<String,Object>();
        
        if (request.getParameter(ACCOUNTS_MODEL)!=null) {
            // retrieve model from session
            model = (Map<String,Object>)request.getSession().getAttribute(ACCOUNTS_MODEL);

        } else {
            
            // get user info from DAO
            User userInfo = uoi.getUserObjectFromUserOpenID(openId);
            
            if(userInfo == null){
              //LOG.debug("userInfo:" + userInfo);
              String error = "true";
              model.put(ACCOUNTS_ERROR, error);
            }
            else {

              // get group info from DAO
              List<Group> groups = uoi.getGroupsFromUser(userInfo.getUserName());
            
              // Get roles for each group as concatenated strings
              List<String> roles = new ArrayList<String>();
              Map<String,Set<String>> userperms = uoi.getUserPermissionsFromOpenID(openId);
              LOG.debug("userperms = " + userperms);
            
              for (Group g : groups) {
                String roleNames = "";
                Set<String> roleSet = userperms.get(g.getname());
                
                if (roleSet != null) {
                    Iterator<String> it = userperms.get(g.getname()).iterator();
                    while (it.hasNext()) {
                        roleNames += it.next();
                        if (it.hasNext()) roleNames += ", ";
                    }
                }
                roles.add(roleNames);
              }
            
              // populate model
              model.put(ACCOUNTS_INPUT, accountsInput);
              model.put(ACCOUNTS_USERINFO, userInfo);
              Group [] groupArray = groups.toArray(new Group[groups.size()]);
              model.put(ACCOUNTS_GROUPINFO, groupArray);
              String [] roleArray = roles.toArray(new String[roles.size()]);
              model.put(ACCOUNTS_ROLEINFO, roleArray);
              request.getSession().setAttribute(ACCOUNTS_MODEL, model);
              model.put(ACCOUNTS_ERROR, "false");
            }
        }

        LOG.debug("------End AccountsController getModel------");
        return model;
    }
    
    
    /**
     * Method invoked in response to a POST request:
     * both results and facets are retrieved.
     * @param request
     * @param input
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping(method=RequestMethod.POST)
    @SuppressWarnings("unchecked")
    protected ModelAndView doPost(final HttpServletRequest request,
            final @ModelAttribute(ACCOUNTS_INPUT) String accountsInput) throws Exception {
        LOG.debug("In do post");

        //get the userId from the cookie
        String userId = Utils.getIdFromHeaderCookie(request);
        if(debugFlag) {
            LOG.debug("UserId Retrieved: " + userId);
        }
        
        if(userId.equals("https://pcmdi3.llnl.gov/esgcet/myopenid/jfharney")) {
            userId = "user1_userName";
        }
        
        //initialize the model sent to the view
        Map<String,Object> model = new HashMap<String,Object>();

        //make sure this is a "fresh" model
        if (request.getParameter(ACCOUNTS_MODEL)!=null) {
            LOG.debug("Not null");
            // retrieve model from session
            model = (Map<String,Object>)request.getSession().getAttribute(ACCOUNTS_MODEL);

        } 
        else {
            model.put(ACCOUNTS_INPUT, accountsInput);
            request.getSession().setAttribute(ACCOUNTS_MODEL, model);
        }
        return new ModelAndView("accountsview", model);
    }
    
    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ModelAttribute(ACCOUNTS_INPUT)
    public String formAccountsInputObject(final HttpServletRequest request) throws Exception {
        LOG.debug("formAccountsInputObject called");
        return "ACCOUNTS_INPUT here";
    }

}
