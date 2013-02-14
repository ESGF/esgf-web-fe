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
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.esgf.datacart.FileDownloadTemplateController;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// this is a turl -> gsiftp://esg2-sdnl1.ccs.ornl.gov//lustre/esgfs/SRM/shared/V.0.0-505553807/t341f02.FAMIPr.cam2.h0.1978-09.nc

@Controller
public class SRMProxyController {

    private static boolean debugFlag = true;
    
    private static String srmAPIURL = "http://localhost:8080/esg-srm/service/srmrequest?";
    
    public static void main(String [] args) {
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        //String [] id = {"anl.cssef.homme.v1|esg.anl.gov","obs4MIPs.CNES.AVISO.mon.v1|esg-datanode.jpl.nasa.gov"};
        //ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1.t341f02.FAMIPr.cam2.h0.1978-09.nc|esg2-sdnl1.ccs.ornl.gov
        //srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-09.nc
        
        String [] file_ids = {"ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1.t341f02.FAMIPr.cam2.h0.1978-09.nc|esg2-sdnl1.ccs.ornl.gov"};
        
        String [] file_urls = {"srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-09.nc"};
        
        mockRequest.addParameter("file_ids[]", file_ids);
        mockRequest.addParameter("file_urls[]", file_urls);
        
        
        SRMProxyController fc = new SRMProxyController();
        
        fc.doPost(mockRequest, null);
        
        
    }
    
    @RequestMapping(method=RequestMethod.POST, value="/srmproxy")
    //public ModelAndView addEmployee(@RequestBody String body) {
    public void doPost(HttpServletRequest request,final HttpServletResponse response) {
        
        System.out.println("In ESGF-WEB-FE SRMProxyController. HTTP POST: doPost");
     
        //grab the ids from the query string
        String [] ids = request.getParameterValues("file_ids[]");
        
        //grab the urls from the query string
        String [] urls = request.getParameterValues("file_urls[]");
        
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
        
        if(debugFlag) {
            for(int i=0;i<urls.length;i++) {
                System.out.println("url: " + i + " " + urls[i]);
            }
        }
        
        
        
        String responseStr = queryESGSRM(urls);
        
        
        
    }
    
    private static String queryESGSRM(String [] urls) {
        
        String response = null;
        String responseBody = null;
        
        // create an http client
        HttpClient client = new HttpClient();

        //attact the dataset id to the query string
        PostMethod method = new PostMethod(srmAPIURL);
        
        String queryString = "";
        
        //add the urls
        for(int i=0;i<urls.length;i++) {
            if(i == 0 && urls.length == 1) {
                queryString += "url=";
                
                queryString += encode(urls[i]);
                
            } 
            else if(i == 0 && urls.length != 1) {
                queryString += "url=";
                
                queryString += encode(urls[i]) + "&";
            }
            else if(i == urls.length - 1) {
                queryString += "url=";
                
                queryString += encode(urls[i]);
            }
        }
        
        //add the ids
        /*
        for(int i=0;i<ids.length;i++) {
            if(i == 0 && ids.length == 1) {
                queryString += "file_id=";
                
                queryString += encode(ids[i]);
                
            } 
            else if(i == 0 && ids.length != 1) {
                queryString += "file_id=";
                
                queryString += encode(ids[i]) + "&";
            }
            else if(i == ids.length - 1) {
                queryString += "file_id=";
                
                queryString += encode(ids[i]);
            }
        }
        */
        queryString += "&length=" + urls.length;
        
        
        System.out.println("\n\n\n\n\nTHE QUERY STRING");
        System.out.println("\t" + queryString);
        System.out.println("\n\n\n");
        
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
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }

        
        
        return null;
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



/*
private static String querySRMService(String compressedUrl) {
    
    String response = null;
    
    
    
    String responseBody = null;
    
    // create an http client
    HttpClient client = new HttpClient();

    
    //attact the dataset id to the query string
    PostMethod method = new PostMethod(srmAPIURL);
    
    System.out.println("In querySRMService for : " + compressedUrl);
    
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
*/

