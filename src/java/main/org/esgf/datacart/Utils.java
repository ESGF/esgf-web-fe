package org.esgf.datacart;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.http.HTTPException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.springframework.mock.web.MockHttpServletRequest;

public class Utils {

    private static String datacartURLbase = "http://localhost:8080/esgf-web-fe/solrfileproxy2/datacart?";
    
    
    public static void main(String [] args) {

        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        String peerStr = "dev.esg.anl.gov";
        String showAll = "true";
        String fqStr = ";offset=0;replica=false";
        String initialQuery = "true";
        String idStr = "anl.cssef.homme.v1|esg.anl.gov";
        String fileCounter = "10";
        
        mockRequest.addParameter("peerStr", peerStr);        
        mockRequest.addParameter("showAllStr", showAll);
        mockRequest.addParameter("fqStr", fqStr);
        mockRequest.addParameter("initialQuery", initialQuery);
        mockRequest.addParameter("idStr", idStr);
        mockRequest.addParameter("fileCounter", fileCounter);
        

        Utils fc = new Utils();
        
        DataCartDocs datacart = fc.getDataCart(mockRequest);
        
        System.out.println(datacart.getDocElements().get(0).getFileElements().size());
        
        
        
    }
    
    private DataCartDocs getDataCartAll(final HttpServletRequest request) {
        
        
        return null;
    }
    
    private DataCartDocs getDataCart(final HttpServletRequest request) {
        
        
        
        // create an http client
        HttpClient client = new HttpClient();

        String datacartURL = datacartURLbase;
        
        //attact the dataset id to the query string
        GetMethod method = new GetMethod(datacartURL);

        
        String peerStr = request.getParameter("peerStr");
        String showAllStr = request.getParameter("showAllStr");
        String fqStr = request.getParameter("fqStr");
        String initialQuery = request.getParameter("initialQuery");
        String idStr = request.getParameter("idStr");
        String fileCounter = request.getParameter("fileCounter");
        
        
        try {
            peerStr = URLEncoder.encode(peerStr,"UTF-8").toString();
            showAllStr = URLEncoder.encode(showAllStr,"UTF-8").toString();
            fqStr = URLEncoder.encode(fqStr,"UTF-8").toString();
            initialQuery = URLEncoder.encode(initialQuery,"UTF-8").toString();
            idStr = URLEncoder.encode(idStr,"UTF-8").toString();
            fileCounter = URLEncoder.encode(fileCounter,"UTF-8").toString();
            
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        
        System.out.println("idStr: " + idStr);

        
        
        datacartURL += "&showAllStr=" + showAllStr + 
                "&fqStr=" + fqStr + 
                "&initialQuery=" + initialQuery +
                "&idStr=" + idStr +
                "&peerStr=" + peerStr +
                "&fileCounter=" + fileCounter;
        
        System.out.println("datacartURL: " + datacartURL);
        
        method.setQueryString(datacartURL);
        
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        
        String responseBody = "";
        
        try {
            // execute the method
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                    System.out.println("Method failed: " + method.getStatusLine());

            }

            // read the response
            responseBody = method.getResponseBodyAsString();
        } catch (HTTPException e) {
            System.out.println("Fatal protocol violation");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Fatal transport error");
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        
        System.out.println("ResponseBody:\n" + responseBody + "\n\n\n");
        
        JSONObject json = null;
        

        DataCartDocs datacart = null;
        
        try {
            json = new JSONObject(responseBody);
            
            //grab the "docs" key
            String xml = XML.toString(json);
            
            System.out.println(xml);
            
            datacart = new DataCartDocs();
            datacart.fromXML(xml);
            
            
            
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return datacart;
    }
    
}
