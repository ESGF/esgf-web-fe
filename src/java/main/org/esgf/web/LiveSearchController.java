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
package org.esgf.web;

/**
 *
 * @author Feiyi Wang (fwang2@ornl.gov)
 *
 */
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/live")

public class LiveSearchController {


    private final static String MODEL = "model";
    private final static String SEARCH = "search";
    private final static String DATACART = "datacart";
    private final static String URL_FACET_VALUES = "url_facet_values";
    private final static String SEARCH_CONSTRAINTS = "search_constraints";
    
    private final static Logger LOG = Logger.getLogger(LiveSearchController.class);

    public static void main(String [] args) {
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        String openId = "openid1";

        mockRequest.addParameter("openid", openId);

        mockRequest.addParameter("model", "CAM5");
        mockRequest.addParameter("project", "CMIP5");
        
        //mockRequest.addParameter("search", "true");
        //mockRequest.addParameter("datacart", "true");
        
        LiveSearchController l = new LiveSearchController();
        
        l.index(mockRequest, null);
        
        
    }
    
    
    @RequestMapping(method=RequestMethod.GET)
    //public String index(HttpServletRequest request, HttpServletResponse response) {
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Enter index");
        
        
        
        Map<String,Object> model = getModel(request);


        
        //return "live-search";

        return new ModelAndView("live-search", model);
    }
    
    private Map<String,Object> getModel(HttpServletRequest request) {

        Map<String,Object> model = new HashMap<String,Object>();
        
        
        model.put(MODEL, "modelllll");

        
        
        Enumeration<String> paramEnum = request.getParameterNames();
        
        List<String> list_constraints = new ArrayList<String>();
        
        String search = null;
        String datacart = null;
        
        while(paramEnum.hasMoreElements()) { 
            String content = (String) paramEnum.nextElement();
            if(content.equals("search")) {
                search = request.getParameter(content);
            } else if(content.equals("datacart")) {
                datacart = request.getParameter(content);
            } else if(content.equals("model")) {
                list_constraints.add("model=" + request.getParameter(content));
            } else if(content.equals("project")) {
                list_constraints.add("project=" + request.getParameter(content));
            } else if(content.equals("id")) {
                list_constraints.add("id=" + request.getParameter(content));
            }
            System.out.println(content+"-->"); 
            System.out.println(request.getParameter(content));
        }
        
        if(search == null) {
            search = "false";
        } 
        if(datacart == null) {
            datacart = "false";
        }

        String [] url_facets = new String[list_constraints.size()];
        String [] search_constraints_values = new String[list_constraints.size()];
        
        System.out.println("---URL Search Constraints---");
        for(int i=0;i<search_constraints_values.length;i++) {
            search_constraints_values[i] = list_constraints.get(i);
            url_facets[i] = list_constraints.get(i).split("=")[0];
            System.out.println("fac: " + url_facets[i] + " sc: " + search_constraints_values[i]);
        }
        System.out.println("---END URL Search Constraints---");
        
        
        model.put(SEARCH, search);
        model.put(DATACART, datacart);
        model.put(SEARCH_CONSTRAINTS, search_constraints_values);
        model.put(URL_FACET_VALUES,url_facets);
        
        
        
        return model;
    }
    
    
}
