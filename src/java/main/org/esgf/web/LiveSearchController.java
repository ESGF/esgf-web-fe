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
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.esgf.datacart.FileDownloadTemplateController;
import org.esgf.propertiesfiles.Utils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/live")

public class LiveSearchController {

    private final static Logger LOG = Logger.getLogger(LiveSearchController.class);

    private final static String HELP_PROPERTIES_FILE = "/esg/config/help.properties";
    private final static String FACET_PROPERTIES_FILE = "/esg/config/facets.properties";

    private final static String MODEL_NAME = "Model_Name";
    private final static String DATACART_OPEN = "Datacart_Open";
    private final static String FACET_PARAM_LIST = "Facet_Params";
    private final static String FACET_PARAM_VALUES_LIST = "Facet_Params_Values";
    
    
    public static void main(String [] args) {
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        String queryStr = "model=kkk1&project=jjj&institute=ll&search=true";
        String searchConstraints = "&offset=0&type=Dataset&project=CMIP5&institute=INM&latest=true&replica=false&distrib=false&";
        
        mockRequest.addParameter("facet_param_list", queryStr);
        mockRequest.addParameter("search_constraint_list", searchConstraints);
        
        
        LiveSearchController fc = new LiveSearchController();
        
        fc.getFacetListHelper(mockRequest);
        
        //fc.getDocElement(mockRequest);
        
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        
        //first we must check if the properties file exists...
        //if it doesn't then we must create a default one
        
        
        
        File f = new File(FACET_PROPERTIES_FILE);
        if(!f.exists()) { 
            System.out.println("Facet properties file does not exist...creating it here");
            Utils.createFacetPropertiesFile();
        }
        
        
        Map<String,Object> model = new HashMap<String,Object>();

        String datacartOpen = request.getParameter("datacart");
        if(datacartOpen == null) {
            datacartOpen = "false";
        } else if(!datacartOpen.equals("true")) {
            datacartOpen = "false";
        }
        
        String modelName = request.getParameter("model");
        if(modelName == null) {
            modelName = "null";
        } 
        

        //model.put(MODEL_NAME,modelName);
        model.put(DATACART_OPEN,datacartOpen);
        

        List<String> facetParams = getFacetParamList(request);
        
        List<String> modelFacetParams = new ArrayList<String>();
        List<String> modelFacetParamsValues = new ArrayList<String>();
        
        
        for(int i=0;i<facetParams.size();i++) {
            System.out.println("facet: " + facetParams.get(i) + " isFacet: " + isFacet(facetParams.get(i)));
            if(isFacet(facetParams.get(i))) {
                modelFacetParams.add(facetParams.get(i));
                modelFacetParamsValues.add(request.getParameter(facetParams.get(i)));
            }
        }
        
        //System.out.println("modelFacetParamsLen: " + modelFacetParams.size());
        String [] facetParamList = (String [])modelFacetParams.toArray(new String[0]);
        String [] facetParamValuesList = (String [])modelFacetParamsValues.toArray(new String[0]);

        model.put(FACET_PARAM_LIST, facetParamList);
        model.put(FACET_PARAM_VALUES_LIST, facetParamValuesList);
        
        
        
        /*
        String [] facet_params = (String []) facetParams.toArray(new String[0]);
        
        System.out.println("facet_params len: " + facet_params.length);
        */
        LOG.debug("Enter index with modelName " + modelName + " and datacart open " + datacartOpen);
        
        return new ModelAndView("live-search",model);
    }
    
    private static List<String> getFacetParamList(HttpServletRequest request) {
        
        List<String> facetParams = new ArrayList<String>();
        
        for(Object key : request.getParameterMap().keySet()) {
            String keyStr = (String)key;
            
//           System.out.println("Param: " + keyStr);
            facetParams.add(keyStr);
        }
        
        
        return facetParams;
        
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/facetList")
    public @ResponseBody List<String> getFacetList(HttpServletRequest request) {

        List<String> facetList = getFacetListHelper(request);
        
        
        return facetList;
    }
    
    private List<String> getFacetListHelper(HttpServletRequest request) {
        List<String> facetList = new ArrayList<String>();
        
        String scl = request.getParameter("search_constraint_list");
        String fpl= request.getParameter("facet_param_list");
        
        String useURLParams = request.getParameter("useURLParams");
        
        System.out.println("\t\t\tUSEURLPARAMS: " + useURLParams);
        
        String [] search_constraint_list = scl.split("&");
        String [] facet_param_list = fpl.split("&");
        
        
        String [] overwriteFacetList = (String []) overwriteDuplicate(search_constraint_list,facet_param_list,useURLParams).toArray(new String [0]);
        facetList = enhanceParamList(overwriteFacetList,facet_param_list,useURLParams);
        
        
        List<String> validFacetList = getValidFacetList(facetList);
        
        System.out.println("helper: " + validFacetList);
        
        return validFacetList;
    }
    
    private List<String> getValidFacetList(List<String> facetList) {
        List<String> validFacetList = new ArrayList<String>();
        
        System.out.println("In getValidFacetList");
        
        
        for(int i=0;i<facetList.size();i++) {
            String facetItem = facetList.get(i);
            System.out.println("Checking facet..." + facetItem.split("=")[0]);
            
            System.out.println("\t\t\tfacetItem: " + facetItem);
            
            if(isFacet(facetItem.split("=")[0]) || 
                    facetList.get(i).contains("offset=") ||
                    facetList.get(i).contains("type=") ||
                    facetList.get(i).contains("latest=") ||
                    facetList.get(i).contains("distrib=") ||
                    facetList.get(i).contains("query=") ||
                    facetList.get(i).contains("replica=")) {
                
                validFacetList.add(facetList.get(i));
            
            }
        }
        
        //return facetList;
        return validFacetList;
    }
    
    private List<String> overwriteDuplicate(String [] search_constraint_list,String [] facet_param_list,String useURLParams) {
        
        List<String> newList = new ArrayList<String>();
        
        for(int i=0;i<search_constraint_list.length;i++) {
            String searchConstraint = search_constraint_list[i];
            String facet_name = searchConstraint.split("=")[0];
            String facet_value = "";
            if(!facet_name.equals("")) {
                facet_value = searchConstraint.split("=")[1];
                //System.out.println(facet_name + "=" + facet_value);
            }
        }
        
        for(int i=0;i<search_constraint_list.length;i++) {
            String searchConstraint = search_constraint_list[i];
            String facet_name = searchConstraint.split("=")[0];
            String facet_value = "";
            if(!facet_name.equals("")) {
                facet_value = searchConstraint.split("=")[1];
            }
//            System.out.println("searchConstraint: " + searchConstraint);
            boolean facetFound = false;
            if(useURLParams.equals("true")) {
                for(int j=0;j<facet_param_list.length;j++) {
                    String param = facet_param_list[j];
                    String param_name = param.split("=")[0];
                    String param_value = param.split("=")[1];
//                    System.out.println("\tParam_name: " + param_name + " param_value: " + param_value);
                    if(searchConstraint.contains(param_name) && !facet_name.equals("")) {
                        facetFound = true;
                        newList.add(param_name + "=" + param_value);
//                        System.out.println("\t\t\tContaints");
                    }
                }
                
            }
            
            if(!facetFound  && !facet_name.equals("")) {
                newList.add(facet_name + "=" + facet_value);
            }
            
        }
        
        
        System.out.println("overwrite newList: " + newList);
        return newList;
        
    }
    
    private List<String> enhanceParamList(String [] search_constraint_list,String [] facet_param_list,String useURLParams) {
        
        List<String> newList = new ArrayList<String>();
        
        
        for(int i=0;i<search_constraint_list.length;i++) {
            String searchConstraint = search_constraint_list[i];
            String facet_name = searchConstraint.split("=")[0];
            String facet_value = "";
            if(!facet_name.equals("")) {
                facet_value = searchConstraint.split("=")[1];
                //System.out.println(facet_name + "=" + facet_value);
            }
            newList.add(searchConstraint);
        }
        

        if(useURLParams.equals("true")) {
            for(int i=0;i<facet_param_list.length;i++) {
                String param = facet_param_list[i];
                String param_name = param.split("=")[0];
                String param_value = param.split("=")[1];
//              
                boolean facetFound = false;
                for(int j=0;j<search_constraint_list.length;j++) {
                    String searchConstraint = search_constraint_list[j];
                    String facet_name = searchConstraint.split("=")[0];
                    String facet_value = "";
                    if(!facet_name.equals("")) {
                        facet_value = searchConstraint.split("=")[1];
                    }
                    //System.out.println("searchConstraint: " + searchConstraint + " param_name: " + param_name);
                    if(searchConstraint.contains(param_name) && !facet_name.equals("")) {
                        facetFound = true;
                        //newList.add(facet_name + "=" + facet_value);
                    }
                }
                if(!facetFound) {
                    //System.out.println("facet not found...adding");
                    newList.add(param_name + "=" + param_value);
                    
                }
            }
        }
        
        
        
        
        //System.out.println("enhance newList: " + newList);
        
        return newList;
    }
    
    private static boolean isFacet(String facet) {
        boolean isFacet = false;
        
        Properties properties = new Properties();
        try {
            
            properties.load(new FileInputStream(FACET_PROPERTIES_FILE));
        
            for(Object key : properties.keySet()) {

                String keyStr = (String)key;
                if(facet.equals(keyStr))
                    isFacet = true;
            }
            
        } catch(Exception e) {
            
            System.out.println("Problem removing all properties");
            e.printStackTrace();
            
        }
        
        
        return isFacet;
    }
    
    private static List<String> getCurrentFacets() {
        
        List<String> currentFacets = new ArrayList<String>();
        
        
        Properties properties = new Properties();
        try {
            
            properties.load(new FileInputStream(FACET_PROPERTIES_FILE));
        
            for(Object key : properties.keySet()) {

                String keyStr = (String)key;
                System.out.println("key: " + keyStr);
            }
            
        } catch(Exception e) {
            
            System.out.println("Problem removing all properties");
            e.printStackTrace();
            
        }
        
        return currentFacets;
        
    }
    
}
