package org.esgf.srm;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class SRMEntryListPopulationController {

    public static String searchAPI = "http://localhost:8080/esg-search/search?";
        
    @RequestMapping(method=RequestMethod.POST, value="/srmlistpopulation")
    //public ModelAndView addEmployee(@RequestBody String body) {
    public @ResponseBody String doPost(HttpServletRequest request,final HttpServletResponse response) {
        String str = "";
        
        System.out.println("In post");
    
        //first query local solr index for all records
        
        
        //from all records, filter over just those records that have an srm url
        
        
        //produce the initial srm entry list from these records
        
    
        return str;
    }
    
    public static void main(String [] args) {
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        
        SRMEntryListPopulationController fc = new SRMEntryListPopulationController();
        
        String response = fc.doPost(mockRequest, null);
        
        querySolr();
        
    }
    
    
    public static void querySolr() {
        
        String responseBody = "";
        
     // create an http client
        HttpClient client = new HttpClient();

        //attact the dataset id to the query string
        PostMethod method = new PostMethod(searchAPI);
        
        String queryString = "query=*&distrib=false";
        
        method.setQueryString(queryString);
        
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        
        try {
            // execute the method
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
            }

            // read the response
            responseBody = method.getResponseBodyAsString();
            
            System.out.println(responseBody);
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
    
        
    }
    
}
