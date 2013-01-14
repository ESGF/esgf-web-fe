package org.esgf.srm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class SRMProxyController {

    
    private static String srmAPIURL = "http://localhost:8080/esg-srm/service/srmrequest?";
    
    
    @RequestMapping(method=RequestMethod.POST, value="/srmproxy")
    //public ModelAndView addEmployee(@RequestBody String body) {
    public void doPost(HttpServletRequest request,final HttpServletResponse response) {
        
        System.out.println("In srm proxy post");
     
        //grab the urls from the query string
        String [] urls = request.getParameterValues("file_ids[]");//null;
        if(urls == null) {
            
            System.out.println("NULL URLS");
            
            urls = new String[2];
            urls[0] = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
                        "SFN=mss://esg2-sdnl1.ccs.ornl.gov/proj/cli049/UHRGCS/ORNL/CESM1" +
                        "/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-01.nc";
            urls[1] = 
                    "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
                        "SFN=mss://esg2-sdnl1.ccs.ornl.gov/proj/cli049/UHRGCS/ORNL" +
                        "/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-12.nc";
        } 
        
        //compress urls into one string
        String url = "";
        for(int i=0;i<urls.length;i++) {
            if(i < urls.length -1) {
                url += urls[i] + ";"; 
            } else {
                url += urls[i];
            }
        }
        
        //attach url=<compressed urls> to queryString to esg-srm service
        String responseStr = querySRMService(url);
        
        
    }
    
    private static String querySRMService(String compressedUrl) {
        
        String response = null;
        
        String responseBody = null;
        
        // create an http client
        HttpClient client = new HttpClient();

        
        //attact the dataset id to the query string
        PostMethod method = new PostMethod(srmAPIURL);
        
        //System.out.println("UrLSSSS: " + url);
        
        try {
            compressedUrl = URLEncoder.encode(compressedUrl,"UTF-8").toString();
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        //System.out.println("\n\nURL: " + url + "\n\n\n\n");
        
        method.setQueryString("url=" + compressedUrl);
        
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        
        
        try {
            // execute the method
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
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

        
        System.out.println("Response: \n" + responseBody);
        
        
        return response;
        
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/srmproxy1")
    //public ModelAndView addEmployee(@RequestBody String body) {
    public void doGet(@RequestBody String body,HttpServletRequest request,final HttpServletResponse response) {
        
        System.out.println("In srm proxy get");
      
    }
    
}
