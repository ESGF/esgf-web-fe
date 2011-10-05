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
 * @author Feiyi Wang (fwang2@ornl.gov)
 *
 */
package org.esgf.globusonline;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.globusonline.*;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/goformview4")
public class GOFormView4Controller {


    private final static Logger LOG = Logger.getLogger(GOFormView4Controller.class);

    private final static String GOFORMVIEW_MODEL = "GoFormView_model";
    private final static String GOFORMVIEW_DATASET_NAME = "GoFormView_Dataset_Name";
    private final static String GOFORMVIEW_USER_CERTIFICATE = "GoFormView_UserCertificate";
    private final static String GOFORMVIEW_FILE_URLS = "GoFormView_File_Urls";
    private final static String GOFORMVIEW_FILE_NAMES = "GoFormView_File_Names";
    private final static String GOFORMVIEW_ERROR = "GoFormView_Error";
    private final static String GOFORMVIEW_GO_USERNAME = "GoFormView_GOUsername";

    // should be configurable?
    private final static String CA_CERTIFICATE_FILE = "/etc/grid-security/certificates/97552d04.0";
    

    public GOFormView4Controller() {
        System.out.println("GO FORM VIEW 4");
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView doPost(final HttpServletRequest request)
    {
        /* get model params here */
        String dataset_name = request.getParameter("id");
        String userCertificate = request.getParameter("usercertificate");
        String [] file_names = request.getParameterValues("child_id");
        String [] file_urls = request.getParameterValues("child_url");
        String goUserName = request.getParameter("gousername");
        String target = request.getParameter("target");
        String endpointInfo = request.getParameter("endpointinfo");

        //if this request comes via go form view 3, then obtain the request parameters for myproxy username
        String srcMyproxyUserName = request.getParameter("srcmyproxyuser");
        String srcMyproxyUserPass = request.getParameter("srcmyproxypass");

        String destMyproxyUserName = null;
        String destMyproxyUserPass = null;

        if ((request.getParameter("myproxyUserName") != null) && (request.getParameter("myproxyUserPass") != null))
        {
            destMyproxyUserName = request.getParameter("myproxyUserName");
            destMyproxyUserPass= request.getParameter("myproxyUserPass");
        }
        LOG.debug("GOFORMView4Controller got Certificate " + userCertificate);
        LOG.debug("GOFORMView4Controller got Target " + target);
        LOG.debug("GOFORMView4Controller got endpointInfo " + endpointInfo);
        LOG.debug("GOFORMView4Controller got Src Myproxy User " + srcMyproxyUserName);
        LOG.debug("GOFORMView4Controller got Src Myproxy Pass ******");
        LOG.debug("GOFORMView4Controller got Dest Myproxy User " + destMyproxyUserName);
        LOG.debug("GOFORMView4Controller got Dest Myproxy Pass ******");

        Map<String,Object> model = new HashMap<String,Object>();

        JGOTransfer transfer = new JGOTransfer(goUserName, userCertificate, userCertificate, CA_CERTIFICATE_FILE);
        transfer.setVerbose(true); // FIXME: For debugging
        try
        {
            transfer.initialize();

            // first activate the source endpoint (WE NEED TO DETERMINE WHAT THIS IS FIRST)
            LOG.debug("Activating source endpoint esg#anl");
            transfer.activateEndpoint("esg#anl", srcMyproxyUserName, srcMyproxyUserPass);

            // second, activate the target endpoint
            String[] endpointPieces = endpointInfo.split(":");
            LOG.debug("Activating destination endpoint " + endpointPieces[0]);
            transfer.activateEndpoint(endpointPieces[0], destMyproxyUserName, destMyproxyUserPass);

            // transform all URLs here


            // kick off the transfer here!

            if (request.getParameter(GOFORMVIEW_MODEL)!=null) {
                System.out.println("NOT NULL");
            }
            else
            {
                String error = isErrorInGORequest();
                //model.put(GOFORMVIEW_ERROR, error);
            }
        }
        catch(Exception e)
        {
            
            String error = e.toString();
            model.put(GOFORMVIEW_ERROR, error);
            LOG.error("Failed to initialize Globus Online: " + e);
        }
        return new ModelAndView("goformview4", model);
    }
    
    
    /*
     * This method determines if there is an error in processing request
     * If there is an error, it simply returns a string "error", which points it to the appropriate view on go_form_view4.jsp
     * otherwise it returns "non-error"
     * NOTE: I know this probably should return a boolean, but maybe a string message should accompany why or why not there is an error
     * That is a GO design decision
     * 
     */
    private String isErrorInGORequest() {
        //change me
        
        return "error";
    }
    
}

