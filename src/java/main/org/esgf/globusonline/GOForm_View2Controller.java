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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/goformview2")
public class GOForm_View2Controller {


    private final static Logger LOG = Logger.getLogger(GOForm_View2Controller.class);

    private final static String GOFORMVIEW_MODEL = "GoFormView_model";
    private final static String GOFORMVIEW_DATASET_NAME = "GoFormView_Dataset_Name";
    private final static String GOFORMVIEW_FILE_URLS = "GoFormView_File_Urls";
    private final static String GOFORMVIEW_FILE_NAMES = "GoFormView_File_Names";
    private final static String GOFORMVIEW_ENDPOINTS = "GoFormView_Endpoints";
    
    
    public GOForm_View2Controller() {
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView doPost(final HttpServletRequest request) {
        

        /* Get the params from the form request */
        String dataset_name = request.getParameter("id");
        String [] file_names = request.getParameterValues("child_id");
        String [] file_urls = request.getParameterValues("child_url");
        String goUserName = request.getParameter("goUserName");
        String myProxyUserName = request.getParameter("myProxyUserName");
        String myProxyUserPass = request.getParameter("myProxyUserPass");
        String goEmail = request.getParameter("goEmail");
        
        System.out.println("goUserName: " + goUserName + " " + "myProxyUserName: " + myProxyUserName + " " + "myProxyUserPass: " + myProxyUserPass + " goEmail: " + goEmail);
        
        
        Map<String,Object> model = new HashMap<String,Object>();

        if (request.getParameter(GOFORMVIEW_MODEL)!=null) {
            
        }
        else {
            

            model.put(GOFORMVIEW_FILE_URLS, file_urls);
            model.put(GOFORMVIEW_FILE_NAMES, file_names);
            model.put(GOFORMVIEW_DATASET_NAME, dataset_name);
            
            //get the endpoints
            String [] endPoints = getDestinationEndpoints(request);
            model.put(GOFORMVIEW_ENDPOINTS, endPoints);
            
        }

        
        return new ModelAndView("goformview2", model);
    }
    
    
    /*
     * Private method obtaining the destination endpoints given some openId (obtained from the cookie)
     */
    private String [] getDestinationEndpoints(final HttpServletRequest request) {
        String [] endPoints;
        
        //get the openid here from the cookie
        Cookie [] cookies = request.getCookies();
        String openId = "";
        for(int i=0;i<cookies.length;i++) {
            if(cookies[i].getName().equals("esgf.idp.cookie")) {
                openId = cookies[i].getValue();
            }
        }
        
        //use the openid to gather endpoints
        //insert Globus Online code here
        //CHANGEME - hard coded
        endPoints = new String[3];
        for(int i=0;i<endPoints.length;i++) {
            endPoints[i] = "ep" + i;
        }
        
        return endPoints;
    }
    
    
}

