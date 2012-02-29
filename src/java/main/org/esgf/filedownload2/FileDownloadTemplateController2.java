package org.esgf.filedownload2;

import javax.servlet.http.HttpServletRequest;

import org.esgf.filedownload.FileDownloadTemplateController;
import org.esgf.filedownload.XmlFormatter;
import org.esgf.metadata.JSONException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/solrfileproxy2")
public class FileDownloadTemplateController2 {

    public static void main(String [] args) {
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        String peerStr = "esg-datanode.jpl.nasa.gov";
        String showAll = "false";
        String fqStr = ",offset=0,replica=false";

        String [] id = {"obs4MIPs.NASA-JPL.AIRS.mon.v1|esg-datanode.jpl.nasa.gov"};
        
        mockRequest.addParameter("peerStr", peerStr);        
        mockRequest.addParameter("showAll", showAll);
        mockRequest.addParameter("fqStr", fqStr);
      
        
        
        mockRequest.addParameter("id", id);
        
        //System.out.println(mockRequest.ge);
        
        FileDownloadTemplateController2 fc = new FileDownloadTemplateController2();
        
//        fc.doGet(mockRequest);
        
        
        
    }
    
    
    
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String doGet(HttpServletRequest request) {

        System.out.println("In doGet");

        String peerStr = request.getParameter("peerStr");
        System.out.println("Peer Str: " + peerStr);
        
        
        
        
        String technoteStr = request.getParameter("technotes");
        String showAllStr = request.getParameter("showAll");
        if(showAllStr == null) {
            System.out.println("SHOW IS NULL");
        } else {
            System.out.println("SHOW IS NOT NULL");
            System.out.println("Show All Str: " + showAllStr);
        }
        
        String [] id = request.getParameterValues("id");
        
        System.out.println(id.length);
        
        /*
        String fqStr = request.getParameter("fqStr");
        System.out.println("fqStr: " + fqStr);
        String [] fq= new String[1];
        fq[0] = fqStr;//fqStr.split(",");
        
        
        //String [] id = request.getParameterValues("id");
        String idStr = request.getParameter("idStr");
        
        System.out.println("idStr: " + idStr);
        
        String id [] = idStr.split(";");
        
        System.out.println(id.length);
        
        
        
        
        //need to parse fqStr and create an array [] fq
        
        String initialQuery = request.getParameter("initialQuery");
        
        DataCartSolrHandler handler = new DataCartSolrHandler(peerStr,showAllStr,fq);

        String datasetId = id[0];
        
        DocElement2 doc = handler.getDocElement2(datasetId);

        System.out.println(new XmlFormatter().format(doc.toXML()));
        */

        System.out.println("End doGet");
        return "<a></a>";
    }
    

    //@RequestMapping(method=RequestMethod.GET)
    /*
    public @ResponseBody String getDocElement2(HttpServletRequest request) {

        String peerStr = request.getParameter("peerStr");
        
        
        String technoteStr = request.getParameter("technotes");
        String showAllStr = request.getParameter("showAll");
        
        String fqStr = request.getParameter("fqStr");
        String [] fq = fqStr.split(",");
        

        String [] id = request.getParameterValues("id");
        System.out.println(id.length);
        
        
        //need to parse fqStr and create an array [] fq
        
        String initialQuery = request.getParameter("initialQuery");
        
        DataCartSolrHandler handler = new DataCartSolrHandler(peerStr,showAllStr,fq);
        
        DataCartDocs2 dcds2 = new DataCartDocs2();
        for(int i=0;i<id.length;i++) {
            DocElement2 doc = handler.getDocElement2(id[i]);
            
            System.out.println(doc.getDatasetId());
        }
        
        return null;
    }
    */
    
    
}
