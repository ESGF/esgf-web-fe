package org.esgf.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.log4j.Logger;
import org.esgf.solr.proxy.SolrProxyController;

@Controller
@RequestMapping("/metadataproxy")
public class MetadataExtractorController {

    private String solrURL="http://localhost:8983/solr/";
    private final static Logger LOG = Logger.getLogger(SolrProxyController.class);
    
    //hard coded for testing - remove when finished
    private final static String METADATA_FILE_LOCATION = System.getProperty("java.io.tmpdir");//"C:\\Users\\8xo\\esgProjects\\esgsearch12-20\\esgf-web-fe\\src\\java\\main\\org\\esgf\\solr\\proxy\\";
    private final static String METADATA_FILE = "ORNL-oai_dif";//"C:\\Users\\8xo\\esgProjects\\esgsearch12-20\\esgf-web-fe\\src\\java\\main\\org\\esgf\\solr\\proxy\\";
    
    
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
        LOG.debug("doGet metadataproxy");
        return relay(request, response);
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public @ResponseBody String doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
        LOG.debug("doPost");
        return relay(request, response);
    }
    

    /*
     * This method will be changed soon - just used for testing
     * The metadata files should be accessed via some RESTful architecture
     */
    private String relay(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
        
        String queryString = request.getQueryString();        
        LOG.debug("queryString=" + queryString);
        String requestUri = request.getRequestURI();
        LOG.debug("requestUri=" + requestUri);
        
        String urlString = solrURL + "select?" + queryString + "&wt=json";
        
        String responseBody = null;
       
        
        
        String xml = "";
        
        //String metadataFileName = "ORNL-oai_dif.xml";
        
        LOG.debug("curr: " + METADATA_FILE_LOCATION);
        //File f = new File("C:\\Users\\8xo\\esgProjects\\esgsearch\\esg-search\\resources\\ORNL-oai_dif.xml");
        File f = new File(METADATA_FILE_LOCATION + METADATA_FILE + ".xml");
        
        
        LOG.debug(f.getName() + " " + f.getAbsolutePath() + " " + f.getCanonicalPath());
        //C:\Users\8xo\esgProjects
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        StringBuffer sb = new StringBuffer();
        String eachLine = br.readLine();

        while (eachLine != null) {
          sb.append(eachLine);
          sb.append("\n");
          eachLine = br.readLine();
        }
        
        xml = sb.toString();
        
        JSONObject jo = XML.toJSONObject(xml);
        //System.out.println(jo.toString());
        
        
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object

        LOG.debug("\njson\n"+jo.toString());
        
        
        //String result = 
        //responseBody = "{\"menu\": {\"id\": \"file\", \"value\": \"File\", \"popup\": { \"menuitem\": [{\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},{\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},{\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}]}}}";
        responseBody = jo.toString();    
        
        LOG.debug("Solr URL = " + urlString);
        LOG.debug("responseBody = " + responseBody);
        return responseBody;
    }
}
