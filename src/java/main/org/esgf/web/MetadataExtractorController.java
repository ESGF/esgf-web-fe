package org.esgf.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
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
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.esgf.solr.proxy.SolrProxyController;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import esg.search.publish.api.PublishingService;
import esg.search.utils.XmlParser;
/**
 * Implementation of metadata extraction controller responsible for extracting metadata that ARE NOT contained in the solr records.
 * The controller searches through the metdata file to find the proper record.  Currently parsing of TDS, OAI, CAS, and FGDC files are supported.
 * 
 * @author john.harney
 *
 */
@Controller
@RequestMapping("/metadataproxy")
public class MetadataExtractorController {

    private final static Logger LOG = Logger.getLogger(MetadataExtractorController.class);
    
    //hard coded for testing - remove when finished
    private static String METADATA_FILE_LOCATION = System.getProperty("java.io.tmpdir");//System.getProperty("java.io.tmpdir");
   
    /**
     * Sends a relay (indirectly) to fetch the appropriate metadata file.
     * 
     * Note: GET and POST contain the same functionality.  
     * 
     * @param  request  HttpServletRequest object containing the query string
     * @param  response  HttpServletResponse object containing the metadata in json format
     * 
     */
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
    

    /**
     * Sends a relay to fetch the appropriate metadata file.
     * 
     * @param  request  HttpServletRequest object containing the query string
     * @param  response  HttpServletResponse object containing the metadata in json format
     * 
     */
    private String relay(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, ParserConfigurationException {
        String queryString = request.getQueryString();        
        LOG.debug("queryString=" + queryString);
        
        String requestUri = request.getRequestURI();
        LOG.debug("requestUri=" + requestUri);

        
        if(METADATA_FILE_LOCATION.startsWith("/var/folders/"))
        {
            METADATA_FILE_LOCATION = "/tmp/";
        }
        
        
        String id = request.getParameter("id");
        String format = request.getParameter("metadataformat");
        String filename = request.getParameter("metadatafile");
        LOG.debug("URL: " + filename);
        
        URL f = new URL(filename);
        
        String jsonContent = "";
        
        if(format.equalsIgnoreCase("oai"))
        {
            jsonContent = processOAI(f,id);
        }
        else if(format.equalsIgnoreCase("fgdc"))
        {
            jsonContent = processFGDC(f,id);
        }
        else if(format.equalsIgnoreCase("cas"))
        {
            jsonContent = processCAS(f,id);
        }
        else //thredds
        {
            jsonContent = processTHREDDS(f,id);
        }
        
        
        return jsonContent;
    }
    
    /**
     * Helper function to fetch CAS metadata files.
     * 
     * @param  f  URL location of the cas document
     * @param  id  The string identifier of the dataset that is being searched
     * 
     */
    public String processCAS(URL f,String id) throws JSONException
    {
        String jsonContent = "";
        
        LOG.debug("IN CAS id: " + id);
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
        try{
            
            Document document = (Document) builder.build(f);
            Element rootNode = document.getRootElement();
            Element returnedEl = null;
            Namespace ns = (Namespace)rootNode.getNamespace();
            
            LOG.debug("Successful " + rootNode.getName());
            
            List records = (List)rootNode.getChildren();
            for(int i=0;i<records.size();i++)
            {
                Element recordEl = (Element) records.get(i);
                for(int j=0;j<recordEl.getChildren().size();j++)
                {
                    Element metEl = ((Element)recordEl.getChildren().get(j));
                    if(metEl.getName().equals("Filename"))
                    {
                        Element filenameEl = (Element)metEl;
                        if(filenameEl.getText().equals(id))
                        {
                            LOG.debug("FOUND RECORD for ID: " + id);
                            returnedEl = recordEl;
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
        
       
          }catch(IOException io){
             System.out.println(io.getMessage());
          }catch(JDOMException jdomex){
             System.out.println(jdomex.getMessage());
     }
          JSONObject jo = XML.toJSONObject(xmlContent);
          
          
          
          jsonContent = jo.toString();
          
          jsonContent = jsonContent.replaceAll("cas:", "");
          jsonContent = jsonContent.replaceAll(":cas", "");
          jsonContent = jsonContent.replaceAll("esg:", "");
          jsonContent = jsonContent.replaceAll(":esg", "");
          jsonContent = jsonContent.replaceAll("rdf:", "");
          jsonContent = jsonContent.replaceAll(":rdf", "");
          
          LOG.debug("json: \n" + jsonContent);
          
          
        return jsonContent;
    }
    
    /**
     * Helper function to fetch OAI metadata files.
     * 
     * @param  f  URL location of the oai document
     * @param  id  The string identifier of the dataset that is being searched
     * 
     */
    public String processOAI(URL f,String id) throws JSONException
    {
        
        SAXBuilder builder = new SAXBuilder();
 
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
 
         }catch(IOException io){
            System.out.println(io.getMessage());
         }catch(JDOMException jdomex){
            System.out.println(jdomex.getMessage());
        }
         
        JSONObject jo = XML.toJSONObject(xmlContent);
         
        String jsonContent = jo.toString();
        LOG.debug("json: \n" + jo.toString());
        
        
        return jsonContent;
    }
    
    /**
     * Helper function to fetch TDS metadata files.
     * 
     * @param  f  URL location of the tds document
     * @param  id  The string identifier of the dataset that is being searched
     * 
     */
    public String processTHREDDS(URL f,String id) throws JSONException
    {
        String jsonContent = "";
        
        LOG.debug("IN THREDDS");
        
        SAXBuilder builder = new SAXBuilder();
        
        Element returnedEl = null;
        String xmlContent = "";
        try{
            
            Document document = (Document) builder.build(f);

            Element rootNode = document.getRootElement();
            Namespace ns = (Namespace)rootNode.getNamespace();
            LOG.debug("Successful " + rootNode.getName());
            returnedEl = rootNode;
        }catch(IOException io){
            System.out.println(io.getMessage());
        }catch(JDOMException jdomex){
           System.out.println(jdomex.getMessage());
       }
        if(returnedEl == null)
        {
            LOG.debug("Found no element match");
        }
        else
        {
            XMLOutputter outputter = new XMLOutputter();
            xmlContent = outputter.outputString(returnedEl);
            
            JSONObject jo = XML.toJSONObject(xmlContent);
            LOG.debug("json: \n" + jo.toString());
    
            jsonContent = jo.toString();
        }
        
        return jsonContent;
    }
    
    /**
     * Helper function to fetch FGDC metadata files.
     * 
     * @param  f  URL location of the fgdc document
     * @param  id  The string identifier of the dataset that is being searched
     * 
     */
    public String processFGDC(URL f,String id) throws JSONException
    {
        SAXBuilder builder = new SAXBuilder();
        
        Element returnedEl = null;
        String xmlContent = "";
        try{
           Document document = (Document) builder.build(f);
           Element rootNode = document.getRootElement();
           Namespace ns = (Namespace)rootNode.getNamespace();
           
           LOG.debug("rootNode: " + rootNode.getName());
           
           //record.metadata.idinfo.citation.citeinfo.title;
           Element idinfoEl = rootNode.getChild("idinfo", ns);
           if(idinfoEl != null)
           {
           
               Element citationEl = idinfoEl.getChild("citation",ns);
               if(citationEl != null)
               {
                   Element citeinfoEl = citationEl.getChild("citeinfo", ns);
                   if(citeinfoEl != null)
                   {
                       Element titleEl = citationEl.getChild("title", ns);
                       if(titleEl != null)
                       {
                           if(titleEl.getText().equals(id))
                           {
                               returnedEl = rootNode;
                           }
                       }
                   }
               }
           }
         }catch(IOException io){
            System.out.println(io.getMessage());
         }catch(JDOMException jdomex){
            System.out.println(jdomex.getMessage());
        }
         
        JSONObject jo = XML.toJSONObject(xmlContent);
         
        String jsonContent = jo.toString();
        LOG.debug("json: \n" + jo.toString());
        return jsonContent;
    }
    
}



