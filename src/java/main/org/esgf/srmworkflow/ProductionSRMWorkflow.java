package org.esgf.srmworkflow;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.ws.http.HTTPException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.esgf.propertiesreader.PropertiesReader;
import org.esgf.propertiesreader.PropertiesReaderFactory;
import org.esgf.srm.SRMControls;
import org.esgf.srm.SRMResponse;

public class ProductionSRMWorkflow extends SRMWorkflow {

    public ProductionSRMWorkflow(String type) {
        super(type);
    }

    public SRMResponse runWorkFlow(String [] srm_files) {
            
        String [] resultingUrls = null;
        
        String responseStr = queryESGSRM(srm_files);
        
        SRMResponse srm_response = new SRMResponse();
        
        srm_response.fromXML(responseStr);
        
        //resultingUrls = srm_response.getResponse_urls();
        
        return srm_response;
        
    }
    
    
    private static String queryESGSRM(String [] file_urls) {
        
        //if(debugFlag)
            System.out.println("\nIn queryESGSRM for size: " + file_urls.length + "\n");
        
        String response = null;
        String responseBody = null;
        
        // create an http client
        HttpClient client = new HttpClient();

        //attact the dataset id to the query string
        //PostMethod method = new PostMethod(SRMControls.srmAPIURL);
        PropertiesReaderFactory factory = new PropertiesReaderFactory();
        PropertiesReader srm_props = factory.makePropertiesReader("SRM");

        PostMethod method = new PostMethod(srm_props.getValue("srm_api_url"));
        String queryString = "";
        String unencodedQueryString = "";

        //add the urls
        for(int i=0;i<file_urls.length;i++) {

            if(i == 0 && file_urls.length == 1) {
                queryString += "url=";
                unencodedQueryString += "url=";
                queryString += encode(file_urls[i]);
                
            } 
            else if(i == 0 && file_urls.length != 1) {
                queryString += "url=";
                unencodedQueryString += "url=" + (file_urls[i]) + "&";
                queryString += encode(file_urls[i]) + "&";
            }
            else if(i == file_urls.length - 1) {
                queryString += "url=";
                unencodedQueryString += "url=" + (file_urls[i]);
                queryString += encode(file_urls[i]);
            } 
            else {
                queryString += "url=";
                unencodedQueryString += "url=" + (file_urls[i]) + "&";
                queryString += encode(file_urls[i]) + "&";
            }
        }


        queryString += "&length=" + file_urls.length;
        unencodedQueryString += "&length=" + file_urls.length;

        queryString += "&file_request_type=" + "http";
        
        

        method.setQueryString(queryString);
        System.out.println("\tQuerystring-> " + method.getQueryString());
        
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        
        try {
            // execute the method
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                System.out.println("statusCode: " + statusCode);
            }

            // read the response
            responseBody = method.getResponseBodyAsString();
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
    
        
        
        return responseBody;
    }
    
    public static String encode(String queryString) {
        
        try {
            queryString = URLEncoder.encode(queryString,"UTF-8").toString();
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        return queryString;
    }

}
