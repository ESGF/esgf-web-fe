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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esgf.manager.InputManager;
import org.esgf.manager.InputManagerImpl;
import org.esgf.manager.OutputManager;
import org.esgf.manager.OutputManagerImpl;
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

    private final static String ManageUsers_MISC = "ManageUsers_misc";
    private final static String ManageUsers_INPUT = "ManageUsers_input";
    private final static String ManageUsers_MODEL = "ManageUsers_model";

    private final static Logger LOG = Logger.getLogger(ManageUsersController.class);

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
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView doGet(final HttpServletRequest request,
            final @ModelAttribute(ManageUsers_MISC) String ManageUsersMisc,
            final @ModelAttribute(ManageUsers_INPUT) String ManageUsersInput) {
        LOG.debug("In do get");
        
        Map<String,Object> model = new HashMap<String,Object>();

        
        if (request.getParameter(ManageUsers_MODEL)!=null) {
            LOG.debug("Not null");
            // retrieve model from session
            model = (Map<String,Object>)request.getSession().getAttribute(ManageUsers_MODEL);

        } else {
            LOG.debug("null");
            LOG.debug("ManageUsers Input: " + ManageUsersInput);
            LOG.debug("ManageUsers Misc: " + ManageUsersMisc);
            

            // populate model
            model.put(ManageUsers_MISC, ManageUsersMisc);
            model.put(ManageUsers_INPUT, ManageUsersInput);
            

            request.getSession().setAttribute(ManageUsers_MODEL, model);
            
        }
        return new ModelAndView("usermanagement", model);
    }
    
    
    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ModelAttribute(ManageUsers_MISC)
    public String formManageUsersMiscObject(final HttpServletRequest request) throws Exception {
        LOG.debug("formManageUsersMiscObject");
        
        
        
        return "ManageUsers_MISC here";
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

