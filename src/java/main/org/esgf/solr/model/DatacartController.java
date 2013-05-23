package org.esgf.solr.model;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.esgf.datacart.FileDownloadTemplateController;
import org.esgf.datacart.XmlFormatter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/solrfileproxy3")
public class DatacartController {

    private static String MAXIMUM_LIMIT = "9999";
    
    public static void main(String [] args) {
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        mockRequest.addParameter("dataset_id", "ana4MIPs.NASA-GMAO.MERRA.atmos.mon.v20121221|esgdata1.nccs.nasa.gov");
        
        DatacartController dc = new DatacartController();
        
        dc.getDoc(mockRequest);
        
        
    }
    
    
    
    @RequestMapping(method=RequestMethod.POST, value="/datacart")
    public @ResponseBody String getDoc(HttpServletRequest request) {
        
        HttpSession session = request.getSession();
        
        System.out.println("\n\n\nIn original datacart controller\n\n");
        
        Enumeration e = session.getAttributeNames();
        while( e.hasMoreElements() ) {
            String key = (String) e.nextElement();
            System.out.println("\n\n\n\nIn original datacart controller key..." + key);
        }
        

        System.out.println("Here1");
        
        if(Utils.debugMode)
            System.out.println("\n\n\nIn new datacart controller\n\n\n");
        
        String response = "";
        
        //grab the input parameters (in lieu of idStr)
        String dataset_id = request.getParameter("dataset_id");
        
        String initialLimit = request.getParameter("initialLimit");
        if(initialLimit == null) {
            initialLimit = "10";
        }
        
        String isInitialQuery = request.getParameter("isInitialQuery");
        if(isInitialQuery == null) {
            isInitialQuery = "false";
        }
        
        String limit = "";
        String offset = "0";
        if(isInitialQuery.equals("false")) {
            limit = MAXIMUM_LIMIT;
            offset = initialLimit;
        } else {
            limit = initialLimit;
        }
        
        
        String isShowAll = request.getParameter("isShowAll");
        if(isShowAll == null) {
            isShowAll = "true";
        }

        System.out.println("Here2");
        
        
        String query = "*";
        if(!isShowAll.equals("true")) {
            
            String constraints = request.getParameter("constraints");
          
            String [] constraint = constraints.split(";");
            for(int i=0;i<constraint.length;i++) {
                String constraintStr = constraint[i];
                if(constraintStr.contains("query=")) {
                    String [] parts = constraintStr.split("=");//
                    query = parts[parts.length-1];
                }
            }
            //add the text query here
            
        }
        
        if(Utils.debugMode) {
            System.out.println("isInitialQuery: " + isInitialQuery);
            System.out.println("offset: " + offset);
            System.out.println("limit: " + limit);
            System.out.println("query: " + query);
        }
        
        
        //query solr for the files
        Solr solr = new Solr();
        
        solr.addConstraint("query", query);
        //solr.addConstraint("distrib", "false");
        solr.addConstraint("limit", limit);
        solr.addConstraint("offset", offset);
        solr.addConstraint("type", "File");
        solr.addConstraint("dataset_id",dataset_id);
        
        //if(Utils.debugMode)
            System.out.println("\nsolr query->" + solr.getQueryString() + "\n\n");
        
        
        solr.executeQuery();
        
        System.out.println("after execution");
        
        SolrResponse solrResponse = solr.getSolrResponse();
        
        
        DatacartDoc datacartDoc = new DatacartDoc(solrResponse);

        datacartDoc.setDatasetId(dataset_id);

        //get response and send it back in json form
        response = datacartDoc.toJSON();

        System.out.println("Response:\n" + response);
        
        return response;
    }
    
    
}

