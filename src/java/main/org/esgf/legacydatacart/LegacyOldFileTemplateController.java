/*****************************************************************************
 * Copyright � 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * �Licensor�) hereby grants to any person (the �Licensee�) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization�s name.  If the Software is protected by a proprietary
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
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.�
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
 *
 * @author John Harney (harneyjf@ornl.gov), Feiyi Wang (fwang2@ornl.gov)
 *
 * Changelog:
 *
 * The query string is encoded through encoder instead of manual string
 * The converted template will be:
 *
 * For debug only:
 *
 * The converted template:
 *
 * <response>
 *    <doc>
 *       <dataset_id> whatever </dataset_id>
 *       <file>
 *          <file_id> ... </file_id>
 *          <size> ... </size>
 *          ...
 *       </file>
 *
 *       <file> .... </file>
 *   </doc>
 * </response>
 */



package org.esgf.legacydatacart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.http.HTTPException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.esgf.metadata.JSONArray;
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



/*
 * Conversion
 */
/*
//responsebody looks like this
//  <responseHeader>
//  </responseHeader>
//  <response>
//   <doc>
//    <id>
//    <parent_id>
//    <service>
//    <title>
//    <size>
//   </doc>
//  </response>
*/
//    to (below) -->

/*
 * <file>
 *   <fileid></fileid>
 *   <title></title>
 *   <size></size>
 *   <urls>
 *     <url></url>
 *     <url></url>
 *     ...
 *     <url></url>
 *   </urls>
 *   <mimes>
 *     <mime></mime>
 *     <mime></mime>
 *     ...
 *     <mime></mime>
 *   </mimes>
 *   <services>
 *     <service></service>
 *     <service></service>
 *     ...
 *     <service></service>  
 *   </services>
 *   
 * </file>
 */



@Controller
@RequestMapping("/oldsolrfileproxy")
public class LegacyOldFileTemplateController {

    
    private static String solrURL="http://localhost:8983/solr/select";
    
    private static String searchAPIURL = "http://localhost:8081/esg-search/search?";
    
    private final static Logger LOG = Logger.getLogger(LegacyOldFileTemplateController.class);

    //for debugging
    private final static int startCharIndex = 600;
    private final static int endCharIndex = 2600;
    
    //toggle between full datasets and filtered datasets
    //private final static boolean datasetFilterFlag = true;
    
    //private final static String MAX_ROWS = "300";

    //private final static boolean useSearchAPI = true;

    
    /**
     * Main method to test the controller using Mock Objects
     * 
     * @param args
     */
    public static void main(String [] args) {
        
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        /*
        String [] id = {"obs4MIPs.NASA-JPL.AIRS.mon.v1:esg-datanode.jpl.nasa.gov"};
        String peer = "undefined";
        String showAll = "true";
        
        mockRequest.addParameter("id", id);
        mockRequest.addParameter("peer", peer);
        mockRequest.addParameter("showAll", showAll);
        
        String queryString = "format=application%2Fsolr%2Bjson&type=File&shards=localhost:8983/solr&variable=hus";
        */
        
        
        
        //JSONArray ja = getJSONArrayUsingSearchAPI(queryString,dataset_id);
        //String dc = getDataCartUsingSearchAPI(mockRequest);
        //System.out.println("dc\n" + dc);
        
        /*
        FileDownloadTemplateController fc = new FileDownloadTemplateController();
        
        
        try {
            String output = fc.doGet(mockRequest);
            System.out.println("output\n" + output);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        String queryString = "format=application%2Fsolr%2Bjson&type=File&shards=localhost:8983/solr&variable=hus";
        String dataset_id = "obs4MIPs.NASA-JPL.AIRS.mon.v1:esg-datanode.jpl.nasa.gov";
        String [] fqArr = {""};
        String peer = "localhost:8983/solr";
        
        LegacyDocElement docElement = getDocElement(queryString,dataset_id,fqArr,peer);
        

        System.out.println("docelement\n" + docElement.toXML());
    }
    
    public static String responseToJSON(List<LegacyDocElement> docElements) {
      
        String jsonContent = "";
        
        //create xml and convert to JSON
        //***UPDATE ME WITH A PROPER MARSHALLER!!!!***
        String xmlStr = "";
        for(int i=0;i<docElements.size();i++) {
            xmlStr += docElements.get(i).toXML();
        }
        JSONObject returnJSON = null;
        try {
            returnJSON = XML.toJSONObject(xmlStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        //convert the JSON object back to a string
        jsonContent = returnJSON.toString();

        System.out.println("JSONCONTENT\n" + jsonContent);
        
        return jsonContent;
    }
  
    
    
    public static LegacyDocElement getDocElement(String queryString,String dataset_id,String [] fq,String peer) {
    
        LegacyDocElement docElement = null;
        
        //start building the doc element here
      
        try {
            //create doc element
            docElement = new LegacyDocElement();
            
            //add the dataset id
            docElement.setDatasetId(dataset_id);
            
            List<LegacyFileElement> fileElements = getFileElements(queryString,dataset_id);

            //create a count element
            docElement.setCount(fileElements.size());

            //attach the file elements to the document
            for(int i=0;i<fileElements.size();i++) {
                docElement.addFileElement(fileElements.get(i));
            }
            
            String hasGridFTP = "true",hasHttp = "true",hasOpenDap = "true";
            
            docElement.setHasGridFTP(hasGridFTP);
            docElement.setHasHttp(hasHttp);
            docElement.setHasOpenDap(hasOpenDap);
            
            
        }catch(Exception e) {
            System.out.println("Problem building doc element");
        }
        
        return docElement;
    }
    
    
    
    private static List<LegacyFileElement> getFileElements(String queryString,String dataset_id) {
        
        List<LegacyFileElement> fileElements = new ArrayList<LegacyFileElement>();
        
        //raw response from solr of files matching the query for dataset_id
        String solrResponse = querySolrForFiles(queryString,dataset_id);
        
        System.out.println("responseString\n" + solrResponse);
        
        //convert to JSON array
        JSONArray files = solrResponseToJSON(solrResponse);

        for(int j=0;j<files.length();j++) {
            System.out.println("\tAdding another file");
            
            try {
                //grab the JSON object
                JSONObject docJSON = new JSONObject(files.get(j).toString());

                //create a new FileElement from the JSONObject
                LegacyFileElement fileElement = new LegacyFileElement(docJSON,"solr");
                
                System.out.println(fileElement.toXML());
                fileElements.add(fileElement);
                
            } catch(Exception e) {
                
            }
        }
        
        return fileElements;
    }
    
    private static JSONArray solrResponseToJSON(String rawString) {
        //convert extracted string into json array
        JSONObject jsonResponse = null;
        JSONArray jsonArray = null;
        try {
            jsonResponse = new JSONObject(rawString);
            jsonArray = jsonResponse.getJSONArray("docs");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            System.out.println("Problem converting Solr response to json string");
            e.printStackTrace();
        }
        return jsonArray;
    }
    
    
    
    private static String querySolrForFiles(String queryString,String dataset_id) {
        
        String marker = "\"response\":";
        
        String responseBody = null;
        
        // create an http client
        HttpClient client = new HttpClient();

        //attact the dataset id to the query string
        GetMethod method = new GetMethod(searchAPIURL);
        
        //add the dataset to the query string
        queryString += "&dataset_id=" + dataset_id;
        
        method.setQueryString(queryString);
        
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        
        try {
            // execute the method
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                    LOG.error("Method failed: " + method.getStatusLine());

            }

            // read the response
            responseBody = method.getResponseBodyAsString();
        } catch (HTTPException e) {
            LOG.error("Fatal protocol violation");
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("Fatal transport error");
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }

        //just get the important part of the response (i.e. leave off the header and the facet info)
        int start = responseBody.lastIndexOf(marker) + marker.length();
        int end = responseBody.length();
        String responseString = responseBody.substring(start,end);
        
        return responseString;
    }
    
    
    /**
     * Function doGet
     * 
     * Entry point into the FileDownloadController
     * 
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String doGet(HttpServletRequest request) throws JSONException {
        System.out.println("In doGet");
        
        long beginTime = System.nanoTime();
        String dataCartContents = getDataCartUsingSearchAPI(request);
        long endTime = System.nanoTime();
        
        System.out.println("--TIME MEASUREMENT FOR LOADING DATACART--");
        System.out.println("\tTotal Time: " + ((int)(endTime - beginTime)) + "ns");
        
        System.out.println("-----------------------------------------");
        
        return dataCartContents;
        
    }
    
    /**
     * Function getJSONArrayUsingSearchAPI
     * 
     * Issues a query to the Search API using the given queryString and dataset_id parameters and returns a JSON array of "doc" records
     * 
     * Uses a helper function called getResponseBodyUsingSearchAPI to grab the result and then prunes the header and facet information out of that result 
     * 
     * @param queryString
     * @param dataset_id
     * @return
     */
    private static JSONArray getJSONArrayUsingSearchAPI(String queryString,String dataset_id) {
    
        String marker = "\"response\":";
        
        //System.out.println("\nQUERY STRING: " + queryString + "\n");
    
        
        //get the json response for all files associated with dataset_id 
        String responseRawString = getResponseBodyUsingSearchAPI(queryString,dataset_id);
        
        //just get the important part of the response (i.e. leave off the header and the facet info)
        int start = responseRawString.lastIndexOf(marker) + marker.length();
        int end = responseRawString.length();
        String extractedString = responseRawString.substring(start,end);


        //System.out.println("Extracted String:\n" + extractedString);
        
        
        
        //convert extracted string into json array
        JSONObject jsonResponse = null;
        JSONArray jsonArrayResponseDocs = null;
        try {
            jsonResponse = new JSONObject(extractedString);
            jsonArrayResponseDocs = jsonResponse.getJSONArray("docs");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        
        return jsonArrayResponseDocs;
    
    }

    
    private static String getDataCartUsingSearchAPI(HttpServletRequest request) {
        
        System.out.println("---In getDataCartUsingSearchAPI---");

        Document document = null;
        String jsonContent = null;
        
        
        //get the ids from the servlet querystring here
        //these ids represent the dataset ids to be accessed in the datacart
        //String [] names = request.getParameterValues("id[]");
        String [] names = request.getParameterValues("id");
        
        System.out.println("id: " + names[0]);
        
        //create a new xml document with a root node named response
        document = new Document(new Element("response"));
        
        String peer = request.getParameter("peer");
        
        
        //only do anything if there is stuff in the datacart
        if(names != null) {
            String peers [] = null;
            if(peer != null) {
                peers = peer.split(";");
            } else {
                peers = new String[1];
                peers[0] = "localhost:8983/solr";
            }

            System.out.println("peer: " + peers[0]);
            
            for(int i=0;i<peers.length;i++) {
                System.out.println("\n\nName: " + names[i] + " PEER: " + peers[i] + "\n");
            }
            
            //create a new list of docElements
            List<LegacyDocElement> docElements = new ArrayList<LegacyDocElement>();
          
          
            //iterate over the list of datasets in the id request paraemter
            for(int i=0;i<names.length;i++) {
                peers[i] = "localhost";
                String queryString = preassembleQueryStringUsingSearchAPI(request,peers[i]);
                
                queryString += "&variable=hus";
                
                System.out.println("queryString: " + queryString);

                String dataset_id = names[i];
                
                System.out.println("DatasetID: " + dataset_id);
                
                //get the json representation for each dataset
                JSONArray jsonArrayResponseDocs = getJSONArrayUsingSearchAPI(queryString,dataset_id);
                
                try {
                    //create doc element
                    LegacyDocElement docElement = new LegacyDocElement();
                    
                    //add the dataset id
                    docElement.setDatasetId(dataset_id);
                    
                    //file elements of the dataset (per dataset_id)
                    List<LegacyFileElement> fileElements = new ArrayList<LegacyFileElement>();
                    
                    System.out.println("Length: " + jsonArrayResponseDocs.length());
                    
                    //add all other file elements
                    for(int j=0;j<jsonArrayResponseDocs.length();j++) {
                        System.out.println("\tAdding another file");
                        JSONObject docJSON = new JSONObject(jsonArrayResponseDocs.get(j).toString());
                        LegacyFileElement fileElement = createFileElementUsingSearchAPI(docJSON);
                        fileElements.add(fileElement);
                    }
                    
                    //create a count element
                    docElement.setCount(jsonArrayResponseDocs.length());
                    
                    //attach the file elements to the document
                    docElement.setFileElements(fileElements);
                    docElements.add(docElement);
                    
                    
                }catch(Exception e) {
                    
                }
                
            }//end iterate over datasets
            
            System.out.println("DocElements size: " + docElements.size());
            
            //create xml and convert to JSON
            //***UPDATE ME WITH A PROPER MARSHALLER!!!!***
            String xmlStr = "";
            for(int i=0;i<docElements.size();i++) {
                xmlStr += docElements.get(i).toXML();
            }
            JSONObject returnJSON = null;
            try {
                returnJSON = XML.toJSONObject(xmlStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            //convert the JSON object back to a string
            jsonContent = returnJSON.toString();

            System.out.println("JSONCONTENT\n" + jsonContent);
            
            
            
        } else {
            System.out.println("Names is Null");
        }

        return jsonContent;
        
/*
        System.exit(0);
        
        
        //only do anything if there is stuff in the datacart
        if(names != null) {
            
            String peers [] = null;
            if(peer == null) {
                peers = peer.split(";");
            } else {
                peers = new String[1];
                peers[0] = "localhost:8983/solr";
            }
            
            
            
            for(int i=0;i<peers.length;i++) {
                System.out.println("\n\nName: " + names[i] + " PEER: " + peers[i] + "\n");
            }
            
            
          //create a new list of docElements
            List<DocElement> docElements = new ArrayList<DocElement>();
            
            //iterate over the list of datasets in the id request paraemter
            for(int i=0;i<names.length;i++) {
                
                String queryString = preassembleQueryStringUsingSearchAPI(request,peers[i]);
            
                String dataset_id = names[i];
                //System.out.println("dataset_id: " + i + " " + dataset_id);
                
                //get the json representation for each dataset
                JSONArray jsonArrayResponseDocs = getJSONArrayUsingSearchAPI(queryString,dataset_id);
                
                try {
                    //create doc element
                    DocElement docElement = new DocElement();
                    
                    //add the dataset id
                    docElement.setDatasetId(dataset_id);
                    
                    //file elements of the dataset (per dataset_id)
                    List<FileElement> fileElements = new ArrayList<FileElement>();
                    
                    //insert initial file here
                    //FileElement initialFileElement = createInitialFileElement1();
                    
                    //add all other file elements
                    for(int j=0;j<jsonArrayResponseDocs.length();j++) {
                        JSONObject docJSON = new JSONObject(jsonArrayResponseDocs.get(j).toString());
                        FileElement fileElement = createFileElementUsingSearchAPI(docJSON);
                        fileElements.add(fileElement);
                    }
                    
                    //create a count element
                    docElement.setCount(jsonArrayResponseDocs.length());
                    
                    //attach the file elements to the document
                    docElement.setFileElements(fileElements);
                    docElements.add(docElement);
                    
                    
                } catch(Exception e) {
                    System.out.println("JSON ERROR");
                    e.printStackTrace();
                }
            }

            //create xml and convert to JSON
            String xmlStr = "";
            for(int i=0;i<docElements.size();i++) {
                xmlStr += docElements.get(i).toXML();
            }
            JSONObject returnJSON = null;
            try {
                returnJSON = XML.toJSONObject(xmlStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            System.out.println("xmlStr\n" + xmlStr);
            
            System.exit(0);
            
            //convert the JSON object back to a string
            jsonContent = returnJSON.toString();
        
        }
        
        // if(jsonContent != null && jsonContent.length() > 1001)
        //System.out.println("jsonContent: " + jsonContent.subSequence(0, 1000));
        
        return jsonContent;
        */
    }
    /**
     * function getDataCartUsingSearchAPI gets a list of parameters from the request object and returns 
     * the json content that will be sent to the data cart
     * 
     * The parameters are the following:
     * - id - an array of dataset ids that the user has added to the datacart
     * - showAll - a toggle parameter determining whether the files will be filtered over the current set of search constraints
     * - fq - an array representing the search constraints
     *
     * It queries the search API for matching files to specific dataset id's
     * 
     * //responsebody looks like this
     *  <responseHeader>
     *  </responseHeader>
     *  <response>
     *   <doc>
     *    <id>
     *    <parent_id>
     *    <service>
     *    <title>
     *    <size>
     *   </doc>
     *  </response>
     *    to (below) -->

     * <doc>
     *      <file>
     *          <fileid></fileid>
     *          <title></title>
     *          <size></size>
     *          <urls>
     *              <url></url>
     *              <url></url>
     *              ...
     *              <url></url>
     *          </urls>
     *          <mimes>
     *              <mime></mime>
     *              <mime></mime>
     *              ...
     *              <mime></mime>
     *          </mimes>
     *          <services>
     *              <service></service>
     *              <service></service>
     *              ...
     *              <service></service>  
     *          </services>   
     *       </file>
     *      <file>
     *      ...
     *      </file>
     * 
     * ...
     * </doc>
     * 
     * @param request
     * @return
     */
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Function createFileElementUsingSearchAPI
     * 
     * Creates a file element given a JSON object representation of a file
     * 
     * @param docJSON
     * @return
     * @throws JSONException
     */
    public static LegacyFileElement createFileElementUsingSearchAPI(JSONObject docJSON) throws JSONException {
        
        System.out.println("---In CreateFileElementUsingSearchAPI---");
        
        System.out.println(docJSON.toString());
        
        //create a new FileElement
        LegacyFileElement fileElement = new LegacyFileElement();
        
        
        //set the file id element
        //String fileId = docJSON.get("file_id").toString();
        
        String fileId = docJSON.get("id").toString();

        System.out.println("\tAdded file_id: " + fileId);
        
        fileElement.setFileId(fileId);

        
        //set the file title element
        String title = docJSON.get("title").toString();
        fileElement.setTitle(title);

        System.out.println("\tAdded title: " + title);
        
        //set the file size element
        String size = docJSON.get("size").toString();
        fileElement.setSize(size);

        System.out.println("\tAdded size: " + size);

        
        //set the urls, mimes, and services elements
        LegacyURLSElement urlsElement = new LegacyURLSElement();
        LegacyMIMESElement mimesElement = new LegacyMIMESElement();
        LegacyServicesElement servicesElement = new LegacyServicesElement();
        JSONArray urlsJSON = (JSONArray)docJSON.getJSONArray("url");
        for(int i=0;i<urlsJSON.length();i++) {
            String urlStr = urlsJSON.get(i).toString();
            
            String [] urlStrTokens = urlStr.split("\\|");
            
            String url = urlStrTokens[0];
            urlsElement.addURL(url);
            
            String mime = urlStrTokens[1];
            mimesElement.addMIME(mime);
            
            String service = urlStrTokens[2];
            servicesElement.addService(service);
            
        }
        fileElement.setUrlsElement(urlsElement);
        fileElement.setMimesElement(mimesElement);
        fileElement.setServicesElement(servicesElement);
        
        
        JSONArray xlinkJSON = (JSONArray)docJSON.getJSONArray("xlink");
        System.out.println("XLINK: " + xlinkJSON.length());
        
        LegacyTechnotesElement technotesElement = new LegacyTechnotesElement();
        
        for(int i=0;i<xlinkJSON.length();i++) {

            String xlinkStr = xlinkJSON.get(i).toString();
            
            String [] xlinkStrTokens = xlinkStr.split("\\|");
            
            String name = xlinkStrTokens[1];
            
            String location = xlinkStrTokens[0];
            
            LegacyTechnoteElement technoteElement = new LegacyTechnoteElement();
            technoteElement.setName(name);
            technoteElement.setLocation(location);
            
            technotesElement.addTechnoteElement(technoteElement);
            
        }
        
        fileElement.setTechnotesElement(technotesElement);
        
        
        System.out.println("---End In CreateFileElementUsingSearchAPI---");
        return fileElement;
    }
    
    
    /**
     * 
     * @param queryString
     * @param dataset_id
     * @return
     */
    private static String getResponseBodyUsingSearchAPI(String queryString,String dataset_id) {

        String responseBody = null;
        System.out.println("In getResponseBodyUsingSearchAPI");
        System.out.println("\tDatasetId: " + dataset_id + "\n\tqueryString: " + queryString);
        
     // create an http client
        HttpClient client = new HttpClient();

        //attact the dataset id to the query string
        GetMethod method = null;
        
        method = new GetMethod(searchAPIURL);
        
        //add distributed search to the query string
        //queryString += "&distrib=false";
        
        //add the dataset to the query string
        queryString += "&dataset_id=" + dataset_id;//replica=false";//"&dataset_id=" + "a";//dataset_id;
        
        //take this out
        queryString = "format=application%2Fsolr%2Bjson&type=File&shards=localhost:8983/solr&variable=hus&dataset_id=obs4MIPs.NASA-JPL.AIRS.mon.v1:esg-datanode.jpl.nasa.gov";
        
        System.out.println("\nResponse Body QueryString: " + queryString + "\n");

        
        method.setQueryString(queryString);
        
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        
        try {
            // execute the method
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                    LOG.error("Method failed: " + method.getStatusLine());

            }

            // read the response
            responseBody = method.getResponseBodyAsString();
        } catch (HTTPException e) {
            LOG.error("Fatal protocol violation");
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("Fatal transport error");
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }

        //System.out.println("-----RESPONSEBODY-----");
        //System.out.println(responseBody.substring(startCharIndex, endCharIndex));
        //System.out.println("-----END RESPONSEBODY-----");

        return responseBody;
    }
    
    /**
     * 
     * @param request
     * @return
     */
    private String getDataCart(HttpServletRequest request) {

       // System.out.println("in getDataCart");
        
        //org.esgf.commonui.Utils.queryStringInfo(request);
        
        
        String queryString = preassembleQueryString(request);
        
        //get the ids from the servlet querystring here
        //note: these represent the 'keys' in the localStorage['dataCart'] map
        String [] names = request.getParameterValues("id[]");
        
       
        
        String showAll = request.getParameter("showAll");
        
        
        Document document = null;
        
        String jsonContent = null;

        document = new Document(new Element("response"));
        
        if(names != null) {
            List<LegacyDocElement> docElements = new ArrayList<LegacyDocElement>();
            
            for(int i=0;i<names.length;i++) {
                String dataset_id = names[i];
                
                //get files for each data set in a jsonarray
                JSONArray jsonArrayResponseDocs = getJSONArrayForDatasetidUsingSearchAPI(queryString,dataset_id);
                
                try {
                    
                    //create doc element
                    LegacyDocElement docElement = new LegacyDocElement();
                    
                    //add the dataset id
                    docElement.setDatasetId(dataset_id);
                    
                    //System.out.println("dataset_id: " + dataset_id);
                    
                    List<LegacyFileElement> fileElements = new ArrayList<LegacyFileElement>();
                    
                    //insert initial file here
                    LegacyFileElement initialFileElement = createInitialFileElement1();
                    
                    //add all other file elements
                    for(int j=0;j<jsonArrayResponseDocs.length();j++) {
                        JSONObject docJSON = new JSONObject(jsonArrayResponseDocs.get(j).toString());
                        LegacyFileElement fileElement = createFileElementUsingSearchAPI(docJSON);
                        fileElements.add(fileElement);
                    }
                    
                    docElement.setCount(jsonArrayResponseDocs.length());
                    
                    docElement.setFileElements(fileElements);
                    
                    docElements.add(docElement);
                    
                    
                    
                } catch(Exception e) {
                    System.out.println("JSON ERROR");
                    //e.printStackTrace();
                }
            }
            
            
            String xmlStr = "";
            for(int i=0;i<docElements.size();i++) {
                xmlStr += docElements.get(i).toXML();
            }
            
            JSONObject returnJSON = null;
            try {
                returnJSON = XML.toJSONObject(xmlStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            
            jsonContent = returnJSON.toString();
        
        }
        
        return jsonContent;
    }
    
    
    
    
    
    
    /**
     * /**
     * creates json array of file entries for a given dataset
     
     * @param queryString
     * @param dataset_id
     * @return
     */
    static JSONArray getJSONArrayForDatasetidUsingSearchAPI(String queryString,String dataset_id) {
        String marker = "\"response\":";
        

        //System.out.println("In getJSONArrayForDatasetid");
        
        //get the json response for all files associated with dataset_id 
        String responseRawString = getResponseBody(queryString,dataset_id);
        
        //just get the important part of the response (i.e. leave off the header and the facet info)
        int start = responseRawString.lastIndexOf(marker) + marker.length();
        int end = responseRawString.length();
        String extractedString = responseRawString.substring(start,end);

        //System.out.println("------\nEXTRACTED STRING\n" + extractedString.substring(0, 3000) + "\n--------\n");
        
        JSONObject jsonResponse = null;
        JSONArray jsonArrayResponseDocs = null;
        
        try {
            jsonResponse = new JSONObject(extractedString);
            jsonArrayResponseDocs = jsonResponse.getJSONArray("docs");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return jsonArrayResponseDocs;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * 
     * @param docJSON
     * @return
     * @throws JSONException
     */
    public static LegacyFileElement createFileElement1(JSONObject docJSON) throws JSONException {
        
        LegacyFileElement fileElement = new LegacyFileElement();
        
        String fileId = docJSON.get("id").toString();
        fileElement.setFileId(fileId);
        
        String title = docJSON.get("title").toString();
        fileElement.setTitle(title);
        
        String size = docJSON.get("size").toString();
        fileElement.setSize(size);

        
        LegacyURLSElement urlsElement = new LegacyURLSElement();
        LegacyMIMESElement mimesElement = new LegacyMIMESElement();
        LegacyServicesElement servicesElement = new LegacyServicesElement();
        
        
        JSONArray urlsJSON = (JSONArray)docJSON.getJSONArray("url");
        for(int i=0;i<urlsJSON.length();i++) {
            String urlStr = urlsJSON.get(i).toString();
            
            String [] urlStrTokens = urlStr.split("\\|");
            
            String url = urlStrTokens[0];
            urlsElement.addURL(url);
            
            String mime = urlStrTokens[1];
            mimesElement.addMIME(mime);
            
            String service = urlStrTokens[2];
            servicesElement.addService(service);
            
        }
        
        fileElement.setUrlsElement(urlsElement);
        fileElement.setMimesElement(mimesElement);
        fileElement.setServicesElement(servicesElement);
        
        return fileElement;
    }
    
    
    
    
    
    /**
     * /**
     * creates json array of file entries for a given dataset
     *
     * @param queryString
     * @param id
     * @return
     */
    JSONArray getJSONArrayForDatasetID(String queryString,String id) {
        
        //System.out.println("In getJSONArrayForDatasetID");
        
        String marker = "\"response\":";
        String responseRawString = getResponseBody(queryString,id);
        int start = responseRawString.lastIndexOf(marker) + marker.length();
        int end = responseRawString.length();
        String extractedString = responseRawString.substring(start,end);
        
        JSONObject responseBody = null;
        JSONArray docsJSON = null;
        try {
            responseBody = new JSONObject(extractedString);
            docsJSON = responseBody.getJSONArray("docs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return docsJSON;
    }
    
    
    
    
    
    /**
     * 
     * @return
     */
    private static LegacyFileElement createInitialFileElement1() {
        
        LegacyFileElement fileElement = new LegacyFileElement();
        
        //put "dummy" values in the file element
        fileElement.setFileId("fileId");
        fileElement.setHasGrid("hasGrid");
        fileElement.setHasHttp("hasHttp");
        fileElement.setSize("size");
        fileElement.setTitle("title");
        fileElement.setMimesElement(new LegacyMIMESElement());
        fileElement.setServicesElement(new LegacyServicesElement());
        fileElement.setUrlsElement(new LegacyURLSElement());
        
        
        return fileElement;
    }
    
    
    
    
    
    
    
   

    
    
    

    /** (String id)
     * This method extracts all file records for a given dataset id and assembles them in json format
     * 
     * @param id        Dataset Id
     * @return          Solr response for all files given the dataset id
     */
    
    private static String getResponseBody(String queryString,String dataset_id)  {

        //System.out.println("In getResponseBody");
        
        String responseBody = null;

        // create an http client
        HttpClient client = new HttpClient();

        //attact the dataset id to the query string
        GetMethod method = null;
        String combinedQueryStr = "";
        
       
        combinedQueryStr += queryString + "&fq=dataset_id:" + dataset_id + "&wt=json";
        method = new GetMethod(solrURL);
        
        method.setQueryString(combinedQueryStr);
        
        
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        try {
            // execute the method
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                    LOG.error("Method failed: " + method.getStatusLine());

            }

            // read the response
            responseBody = method.getResponseBodyAsString();
        } catch (HTTPException e) {
            LOG.error("Fatal protocol violation");
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("Fatal transport error");
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        
        
        
        return responseBody;
    }
    
    
    
    
    /**
     * 
     * @param request
     * @return
     */
    private static String preassembleQueryStringUsingSearchAPI(HttpServletRequest request,String indexPeer) {
        
        String queryString = "";
        
        queryString += "format=application%2Fsolr%2Bjson";
        queryString += "&type=File";
        
        //System.out.println("INDEX PEER: " + indexPeer);
        
        if(!indexPeer.equals("undefined")) {
            queryString += "&shards=" + indexPeer + ":8983/solr";
        }
        
        if(request.getParameter("showAll").equals("false")) {
            
            //System.out.println("Showall is false");
          //get the 'fq' params from the servlet query string here
            String [] fqParams = request.getParameterValues("fq[]");
            
            //get the 'q' param from the servlet query string here
            //String qParam = request.getParameter("q");
            String fullText = "";
            if(fqParams != null) {
                //System.out.println("fqParams is not null");
              //append the 'fq' params to the query string
                for(int i=0;i<fqParams.length;i++) {
                    String fqParam = fqParams[i];
                    //System.out.println("fqParam: " + fqParam);
                    if(!fqParam.equals("") && !fqParam.equals(" ")) {
                        //System.out.println("\tfqParam not <space>");
                        if(!fqParam.contains("query")) {
                            queryString += "&" + fqParam;
                        } else {
                            //System.out.println("")
                            String [] clause = fqParam.split("=");
                            fullText += clause[1] + "%20";
                        }
                    }
                }
                if(!fullText.equals("") && !fullText.equals(" ")) {
                    //System.out.println("FULLTEXT: " + fullText);
                    queryString += "&query=" + fullText;//fullText.substring(0,fullText.length());
                
                }
            } else {
                //System.out.println("fqParams is null");
            }
        }
        
        //System.out.println("\n\nQUERYSTRING: " + queryString + "\n\n");
        
        return queryString;
    }
    
    /**
     * 
     * @param request
     * @return
     */
    private static String preassembleQueryString(HttpServletRequest request) {
        
        //System.out.println("\tin preassembleQueryString");
        
        String queryString = "";
        
        queryString += "q=*:*&json.nl=map&start=0&rows=" + 300 + "&fq=type:File";

        
        if(request.getParameter("shardType").equals("solrconfig")) {
            queryString = "qt=/distrib&" + queryString;
        } else {
            queryString = "shards=" + request.getParameter("shardsString") + "&" + queryString;
        }
        
        if(request.getParameter("showAll").equals("false")) {
          //get the 'fq' params from the servlet query string here
            String [] fqParams = request.getParameterValues("fq[]");
            
            //get the 'q' param from the servlet query string here
            //String qParam = request.getParameter("q");
            
            if(fqParams != null) {
              //append the 'fq' params to the query string
                for(int i=0;i<fqParams.length;i++) {
                    String fqParam = fqParams[i];
                    queryString += "&fq=" + fqParam;
                }
            }
            
        }
        
        
        return queryString;
    }
    

    /**
     * queryStringInfo(HttpServletRequest request)
     * Private method that prints out the contents of the request.  Used mainly for debugging.
     * 
     * @param request
     */
    @SuppressWarnings("unchecked")
    public static void queryStringInfo(HttpServletRequest request) {
        System.out.println("--------Utils Query String Info--------");
        Enumeration<String> paramEnum = request.getParameterNames();
        
        while(paramEnum.hasMoreElements()) { 
            String postContent = (String) paramEnum.nextElement();
            System.out.println(postContent+"-->"); 
            System.out.println(request.getParameter(postContent));
        }
        System.out.println("--------End Utils Query String Info--------");
    }

}

