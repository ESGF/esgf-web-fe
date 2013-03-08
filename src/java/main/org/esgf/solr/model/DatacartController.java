package org.esgf.solr.model;

import javax.servlet.http.HttpServletRequest;

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

    
    public static void main(String [] args) {
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        mockRequest.addParameter("dataset_id", "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1|esg2-sdnl1.ccs.ornl.gov");
        
        DatacartController dc = new DatacartController();
        
        dc.getDoc(mockRequest);
        
        
    }
    
    
    
    @RequestMapping(method=RequestMethod.POST, value="/datacart")
    public @ResponseBody String getDoc(HttpServletRequest request) {
        
        System.out.println("In get doc");
        
        String response = "";
        
        //grab the input parameters (in lieu of idStr)
        String dataset_id = request.getParameter("dataset_id");
        
        
        
        //query solr for the files
        Solr solr = new Solr();
        
        solr.addConstraint("query", "*");
        solr.addConstraint("distrib", "false");
        solr.addConstraint("limit", "8");
        solr.addConstraint("type", "File");
        solr.addConstraint("dataset_id",dataset_id);
        
        solr.executeQuery();
        
        SolrResponse solrResponse = solr.getSolrResponse();
        
        DatacartDoc datacartDoc = new DatacartDoc(solrResponse);

        System.out.println( new XmlFormatter().format(datacartDoc.toXML()));
        System.out.println( datacartDoc.toJSON());
        //get response and send it back in json form
        
        
/*
        String idStr = request.getParameter("idStr");
        
        String peerStr = request.getParameter("peerStr");
        //String [] peers = peerStr.split(";");

        //NOTE: ID and PEERS SHOULD BE THE SAME LENGTH!!!!
        
        //get the fq string and convert to an array of peers
        String fqStr = request.getParameter("fqStr");
        String [] fq = fqStr.split(";");
        
        
        String technotes = request.getParameter("technotesStr");
        //System.out.println("Technotes: " + technotes);
        
        
        //get the search constraints togggle parameter
        String showAllStr = request.getParameter("showAllStr");
        
        //get the flag denoting whether or not this is an initial Query
        String initialQuery = request.getParameter("initialQuery");
        

        //get the fileCounter
        String fileCounter = request.getParameter("fileCounter");
        
        
        
        datasetId = idStr;
*/       
        return response;
        //return null;
    }
    
    
}




/*
System.out.println("--------");
System.out.println("\tpeerStr\t" + peerStr);
System.out.println("\tfqStr\t" + fqStr);
System.out.println("\ttechnotesStr\t" + technotes);
System.out.println("\tshowAllStr\t" + showAllStr);
System.out.println("\tinitialQuery\t" + initialQuery);
System.out.println("\tfileCount\t " + fileCounter + "\n");
*/