package org.esgf.metadata;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.http.HTTPException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.esgf.datacart.MIMESElement;
import org.esgf.datacart.ServicesElement;
import org.esgf.datacart.URLSElement;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/metadataview")
public class MetadataViewController {

    private final static String METADATAVIEW_DATASET_NAME = "MetadataView_Dataset_Name";
    private final static String RESPONSE = "Response";

    private static String searchAPIURL = "http://localhost:8081/esg-search/search?";
    
    @SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView doGet(final HttpServletRequest request) {
    
        Map<String,Object> model = new HashMap<String,Object>();

        
        
        String id = request.getParameter("id");
        if(id != null) {
            System.out.println("ID: " + id);
            model.put(METADATAVIEW_DATASET_NAME, id);
            System.out.println("Putting " + id + " into the model");
        } else {
            System.out.println("NULL");
        }
        
        String solrQueryString = "";

        
        String responseBody = null;
        
        // create an http client
        HttpClient client = new HttpClient();

        //attact the dataset id to the query string
        GetMethod method = new GetMethod(searchAPIURL);
        
        //add the dataset to the query string
        try {
            solrQueryString += "format=application%2Fsolr%2Bjson&id=" + URLEncoder.encode(id,"UTF-8").toString();
            //System.out.println("\nthis.solrQueryString->\t" + URLEncoder.encode(dataset_id,"UTF-8").toString());
            //System.out.println("\n\tthis.solrQueryString->\t" + this.solrQueryString);
            
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        method.setQueryString(solrQueryString);
        
        System.out.println("\tQUERYSTR: " + method.getQueryString());
        		
        
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        
        try {
            // execute the method
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                    //LOG.error("Method failed: " + method.getStatusLine());

            }

            // read the response
            responseBody = method.getResponseBodyAsString();
        } catch (HTTPException e) {
            //LOG.error("Fatal protocol violation");
            e.printStackTrace();
        } catch (IOException e) {
            //LOG.error("Fatal transport error");
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        

        String marker = "\"response\":";
        
        //just get the important part of the response (i.e. leave off the header and the facet info)
        int start = responseBody.lastIndexOf(marker) + marker.length();
        int end = responseBody.length();
        String responseString = responseBody.substring(start,end);
        
        
        
        
      //convert extracted string into json array
        JSONObject jsonResponse = null;
        JSONArray jsonArray = null;
        String numFound = null;
        try {
            
            jsonResponse = new JSONObject(responseString);
            
            //jsonArray = jsonResponse.getJSONArray("docs");
            //numFound = jsonResponse.getString("numFound");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            System.out.println("Problem converting Solr response to json string - getDocElement");
            //e.printStackTrace();
        }
        
        
        fromSolr(jsonResponse);
        
        
        
        
        model.put(RESPONSE, responseString);
        /*
     
       
        return responseString;
        */
        
        
        
        
        
        
        return new ModelAndView("metadataview", model);
    
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView doPost(final HttpServletRequest request) {
    
        Map<String,Object> model = new HashMap<String,Object>();

        String id = request.getParameter("id");
        if(id != null) {
            System.out.println("ID: " + id);
            model.put(METADATAVIEW_DATASET_NAME, id);
            System.out.println("Putting " + id + " into the model");
        } else {
            System.out.println("NULL");
        }
        
        return new ModelAndView("metadataview", model);
    
    }
    
    
    
    public void fromSolr(JSONObject solrResponse) {
        
        System.out.println("\n\n\tFROM SOLR\n");
        
        Iterator iter = solrResponse.sortedKeys();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            
            if(key.equals("docs")) {
                try {
                    System.out.println("KEY: " + solrResponse.getString(key));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                /*
                try{
                    String value = solrResponse.getString(key);
                    
                    //System.out.println(solrResponse.getString(key));
                    JSONObject jsonobj = new JSONObject(value);
                    
                    Iterator iter2 = jsonobj.sortedKeys();
                    
                    while(iter2.hasNext()) {
                        String key2 = (String)iter2.next();
                        
                        System.out.println("Key2: " + key2);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                
                JSONObject value = null;
                try {

                   //value = solrResponse.getString(key);

                   value = solrResponse.getJSONObject(key);
                   
                   
                   
                   //System.out.println("value: " + value);
                   Iterator iter2 = value.sortedKeys();
                   
                   while(iter2.hasNext()) {
                       String key2 = (String)iter2.next();
                       
                       System.out.println("key2: " + key2);
                   }
                   
                   
                } catch (Exception e) {
                    e.printStackTrace();
                }
                */
            }
        }

        /*
        this.tracking_id = "unknown";
        this.checksum = "unknown";
        this.checksum_type = "unknown";
        this.technoteStr = "NA";
        
        while(iter.hasNext()) {
            String key = (String)iter.next();

            String value = null;
            try {
                value = solrResponse.getString(key);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //System.out.println("key: " + key + " value: " + value);
            //set the fileid
            if(key.equals("id")) {
                
                this.fileId = value;
                
            } 
            //set the file size element
            else if(key.equals("size")) {
                
                this.size = value;
                
            }
            //set the file title element
            else if(key.equals("title")) {
                
                this.title = value;
                
            }
            //set the file title element
            else if(key.equals("checksum")) {
                
                JSONArray checksumsJSON;
                try {
                    checksumsJSON = (JSONArray)solrResponse.getJSONArray(key);
                    this.checksum = (String)checksumsJSON.get(0);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //System.out.println("                   checksum: " + this.checksum);
                
            }
            //set the file title element
            else if(key.equals("tracking_id")) {
                
                JSONArray trackingIdJSON;
                try {
                    trackingIdJSON = (JSONArray)solrResponse.getJSONArray(key);
                    this.tracking_id = (String)trackingIdJSON.get(0);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //System.out.println("                   TrackingId: " + this.tracking_id);
                
            }
            //set the file title element
            else if(key.equals("checksum_type")) {
                
                
                JSONArray checksum_typeJSON;
                try {
                    checksum_typeJSON = (JSONArray)solrResponse.getJSONArray(key);
                    this.checksum_type = (String)checksum_typeJSON.get(0);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                //System.out.println("Checksum Type: " + this.checksum_type);
                
                
            }
            else if(key.equals("url")) {
                
                
                //set the urls, mimes, and services elements
                URLSElement urlsElement = new URLSElement();
                MIMESElement mimesElement = new MIMESElement();
                ServicesElement servicesElement = new ServicesElement();
                JSONArray urlsJSON = null;
                try {
                    urlsJSON = (JSONArray)solrResponse.getJSONArray(key);
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
                for(int i=0;i<urlsJSON.length();i++) {
                    String urlStr = null;
                    try {
                        urlStr = urlsJSON.get(i).toString();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                    String [] urlStrTokens = urlStr.split("\\|");
                    
                    String url = urlStrTokens[0];
                    urlsElement.addUrl(url);
                    
                    String mime = urlStrTokens[1];
                    mimesElement.addMime(mime);
                    
                    String service = urlStrTokens[2];
                    servicesElement.addService(service);
                    if(service.equals("OPENDAP")) {
                        this.hasOpenDap = "true";
                        //System.out.print("\tService is an OPendap");
                    } else if(service.equals("HTTPServer")) {
                        //System.out.print("\tService is an HTTP");
                        this.hasHttp = "true";
                    } else if(service.equals("GridFTP")) {
                        this.hasGrid = "true";
                        //System.out.print("\tService is an GRIDFTP");
                    } else if(service.equals("SRM")) {
                        this.hasSRM = "true";
                        //System.out.print("\tService is an GRIDFTP");
                    }
                }

                if(this.hasOpenDap == null) {
                    this.hasOpenDap = "false";
                } 
                if(this.hasGrid == null) {
                    this.hasGrid = "false";
                } 
                if(this.hasHttp == null) {
                    this.hasHttp = "false";
                } 
                if(this.hasSRM == null) {
                    this.hasSRM = "false";
                }
                
                this.urlsElement = urlsElement;
                this.mimesElement = mimesElement;
                this.servicesElement = servicesElement;

                
                
            }
            else if(key.equals("xlink")) {
                
                try {
                    JSONArray xlinkJSON = (JSONArray)solrResponse.getJSONArray(key);
                    //System.out.println("\n****XLINK LENGTH****\n\n" + xlinkJSON.length());
                    
                    //just get the pdf url
                    String [] technoteTokens = ((String)xlinkJSON.get(0)).split("\\|");
                    
                    String technoteStr = technoteTokens[0];
                    
                    this.technoteStr = technoteStr;
                    
                    //System.out.println("\ttechnote: " + this.technoteStr);
                } catch(Exception e) {
                    e.printStackTrace();
                }
               
            }
            
            
        }
        */
    }
    
    
    
    
    
    
    
    
    
}
