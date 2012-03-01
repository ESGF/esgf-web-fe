package org.esgf.filedownload2;

import javax.servlet.http.HttpServletRequest;

import org.esgf.filedownload.FileDownloadTemplateController;
import org.esgf.filedownload.XmlFormatter;
import org.esgf.metadata.JSONException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/solrfileproxy2")
public class FileDownloadTemplateController2 {

    private static boolean useCurl = false;
    private final static String testInitializationFile = "C:\\Users\\8xo\\esgf-web-fe\\docselement.xml";

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
    
    
    /**
     * 
     * @param request
     * @return
     */
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String doGet(HttpServletRequest request) {

        System.out.println("In doGet");

        this.printParameters(request);
        
        String peer = request.getParameter("peer");
        
        String technotes = request.getParameter("technotes");
        
        String showAll = request.getParameter("showAll");
        
        String idStr = request.getParameter("id");
        String [] id = idStr.split(",");
        
        String fqStr = request.getParameter("fq");
        String [] fq = fqStr.split(".");
        
        DataCartSolrHandler handler = new DataCartSolrHandler(peer,showAll,fq);

        DataCartDocs2 doc = new DataCartDocs2();
        
        for(int i=0;i<id.length;i++) {
            DocElement2 d = handler.getDocElement2(id[i]);
            doc.addDocElement2(d);
            System.out.println(new XmlFormatter().format(d.toXML()));
        }
        
        //return doc.toJSON();
        return doc.toJSON();
    }
    
    /**
     * 
     * @param request
     * @return
     */
    @RequestMapping(method=RequestMethod.GET, value="/datacart")
    public @ResponseBody String getDocElement2(HttpServletRequest request) {
        System.out.println("In getDocElement2");

        this.printParameters(request);
        
        String peer = request.getParameter("peer");
        
        String technotes = request.getParameter("technotes");
        
        String showAll = request.getParameter("showAll");
        
        String [] id = null;
        if(!useCurl) {
            id = request.getParameterValues("id[]");
        } else {
            String idStr = request.getParameter("id");
            id = idStr.split(",");
        }
        
        String [] fq = null;
        if(!useCurl) {
            fq = request.getParameterValues("fq[]");
        } else {

            String fqStr = request.getParameter("fq");
            fq = fqStr.split(",");
        }
        
        DataCartSolrHandler handler = new DataCartSolrHandler(peer,showAll,fq);

        DataCartDocs2 doc = new DataCartDocs2();
        
        for(int i=0;i<id.length;i++) {
            DocElement2 d = handler.getDocElement2(id[i]);
            doc.addDocElement2(d);
            doc.toFile(testInitializationFile);
            //System.out.println(new XmlFormatter().format(d.toXML()));
        }
        
        return doc.toJSON();
        //return new XmlFormatter().format(doc.toXML());
        
    }
    

    /**
     * 
     * @param request
     */
    public void printParameters(HttpServletRequest request) {
        String peer = request.getParameter("peer");
        System.out.println("Peer Str: " + peer);
        
        String technotes = request.getParameter("technotes");
        System.out.println("technotes: " + technotes);
        
        String showAll = request.getParameter("showAll");
        System.out.println("Show all: " + showAll);
        
        String [] id = null;
        if(!useCurl) {
            id = request.getParameterValues("id[]");
            if(id != null)
                System.out.println(id.length);
            else 
                System.out.println("NULL");
        } else {
            String idStr = request.getParameter("id");
            System.out.println("idStr: " + idStr);
            id = idStr.split(",");
            for(int i=0;i<id.length;i++) {
                System.out.println("\tid: " + i + " " + id[i]);
            }
        }
        
        
        String [] fq = null;
        
        if(!useCurl) {
            fq = request.getParameterValues("fq[]");
            for(int i=0;i<fq.length;i++) {
                System.out.println("\tfq: " + fq[i]);
            }
        } else {
            String fqStr = request.getParameter("fq");
            System.out.println("fqStr: " + fqStr);
            fq = fqStr.split(",");
            for(int i=0;i<fq.length;i++) {
                System.out.println("\tfq: " + fq[i]);
            }
        }
        
    }
    
    
}
