package org.esgf.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import esg.search.utils.XmlParser;

@Controller
@RequestMapping("/metadataproxy")
public class MetadataExtractorController {

    private String solrURL="http://localhost:8983/solr/";
    private final static Logger LOG = Logger.getLogger(SolrProxyController.class);
    
    //hard coded for testing - remove when finished
    private final static String METADATA_FILE_LOCATION = System.getProperty("java.io.tmpdir");
    private final static String METADATA_FILE = "ORNL-oai_dif";
    
    
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, ParserConfigurationException {
        LOG.debug("doGet metadataproxy");
        return relay(request, response);
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public @ResponseBody String doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, ParserConfigurationException {
        LOG.debug("doPost");
        return relay(request, response);
    }
    

    /*
     * This method will be changed soon - just used for testing
     * The metadata files should be accessed via some RESTful architecture
     */
    private String relay(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, ParserConfigurationException {
        
        String queryString = request.getQueryString();        
        LOG.debug("queryString=" + queryString);
        
        
        
        String requestUri = request.getRequestURI();
        LOG.debug("requestUri=" + requestUri);
        
        String urlString = solrURL + "select?" + queryString + "&wt=json";
        
        String responseBody = null;
       
        String id = request.getParameter("id");
        
        
        
        //String metadataFileName = "ORNL-oai_dif.xml";
        
        
        //Assuming the data structure is json
        
        LOG.debug("curr: " + METADATA_FILE_LOCATION);
        //File f = new File("C:\\Users\\8xo\\esgProjects\\esgsearch\\esg-search\\resources\\ORNL-oai_dif.xml");
        File f = new File(METADATA_FILE_LOCATION + METADATA_FILE + ".xml");
        /*
        
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        StringBuffer sb = new StringBuffer();
        String eachLine = br.readLine();

        while (eachLine != null) {
          sb.append(eachLine);
          sb.append("\n");
          eachLine = br.readLine();
        }
        
        LOG.debug(sb.toString());
        */
        
        
        //C:\Users\8xo\esgProjects
        /*
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        StringBuffer sb = new StringBuffer();
        String eachLine = br.readLine();

        while (eachLine != null) {
          //sb.append(eachLine);
          //sb.append("\n");
          eachLine = br.readLine();
          //LOG.debug("eachLIne: " + eachLine);
        }
        */
        
        SAXBuilder builder = new SAXBuilder();
        //File xmlFile = new File("c:\\file.xml");
 
        Element returnedEl = null;
        String xmlContent = "";
        try{
 
           Document document = (Document) builder.build(f);
           Element rootNode = document.getRootElement();
           Namespace ns = (Namespace)rootNode.getNamespace();
           LOG.debug("Successful " + rootNode.getName());
           Element el = (Element) rootNode.getChild("ListRecords",ns);
           LOG.debug("el " + el.getName());
           
           List records = (List)el.getChildren("record", ns);
           for(int i=0;i<records.size();i++)
           {
               Element recordEl = (Element) records.get(i);
               
               Element metadataEl = (Element)recordEl.getChild("metadata",ns);
               if(metadataEl != null)
               {
                   Element difEl = (Element)metadataEl.getChild("DIF",ns);
                   if(difEl != null)
                   {
                       Element idEl = (Element)difEl.getChild("Entry_ID",ns);
                       if(idEl !=null)
                       {
                           if(idEl.getText().equals(id))
                           {
                               LOG.debug("ID: " + idEl.getText());
                           
                               returnedEl = recordEl;
                           }
                       }
                   }
               }
           }
           
           if(returnedEl == null)
           {
               LOG.debug("Found no element match");
           }
           else
           {
               XMLOutputter outputter = new XMLOutputter();
               xmlContent = outputter.outputString(returnedEl);
           }
           
           LOG.debug(records.size());
          /* for (int i=0; i< list.size(); i++)
           {
             Element node = (Element) list.get(i);
             LOG.debug("Name: " + node.getName());
           }*/
 
         }catch(IOException io){
            System.out.println(io.getMessage());
         }catch(JDOMException jdomex){
            System.out.println(jdomex.getMessage());
        }
         
        //LOG.debug("xml: \n" + xmlContent);
        
        JSONObject jo = XML.toJSONObject(xmlContent);
         
        String jsonContent = jo.toString();
        LOG.debug("json: \n" + jo.toString());
        
        
        //String result = 
        responseBody = "{\"menu\": {\"id\": \"file\", \"value\": \"File\", \"popup\": { \"menuitem\": [{\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},{\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},{\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}]}}}";
        //responseBody = jo.toString();    
        //responseBody = sb.toString();
        
        
        
        LOG.debug("Solr URL = " + urlString);
        LOG.debug("responseBody = " + responseBody); 
        LOG.debug("queryString=" + queryString);
        LOG.debug("Parameter names: " + request.getParameterNames().toString());
        
        Enumeration paramNames = request.getParameterNames();
        
        while(paramNames.hasMoreElements()) {
          String paramName = (String)paramNames.nextElement();
          String paramValue = request.getParameter(paramName);
          LOG.debug(paramName + " " + paramValue);
        }
        return jsonContent;
    }
}


/*
//File file = new File("c:\\MyXMLFile.xml");
DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
DocumentBuilder db = dbf.newDocumentBuilder();

Document doc = (Document) db.parse(f);
Element rootEl = (Element)doc.getDocumentElement();

rootEl.get
*/
//final Element rootEl = doc.getRootElement();
//final Namespace ns = rootEl.getNamespace();
//LOG.debug("Root element " + rootEl.getName());
//
//Element listRecordsEl = (Element)rootEl.getChild("ListRecords");
//List records = listRecordsEl.getChildren("record");
//
//Iterator iterator = records.iterator();
//
//while(iterator.hasNext())
//{
//    Element record = (Element)iterator.next();
//    LOG.debug("record" + record.getContentSize());
//}

  // NodeList nodeLst = doc.getElementsByTagName("record");


/*FileOutputStream out; // declare a file output object
PrintStream p; // declare a print stream object
*/

//System.out.println("Information of all employees");

//xml = sb.toString();

//LOG.debug(xml);

//JSONObject jo = XML.toJSONObject(xml);
//System.out.println(jo.toString());

//LOG.debug("\njson\n"+jo.toString());
