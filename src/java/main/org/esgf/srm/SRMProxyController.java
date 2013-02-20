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
import org.esgf.datacart.DocElement;
import org.esgf.datacart.FileDownloadTemplateController;
import org.esgf.datacart.URLSElement;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// this is a turl -> gsiftp://esg2-sdnl1.ccs.ornl.gov//lustre/esgfs/SRM/shared/V.0.0-505553807/t341f02.FAMIPr.cam2.h0.1978-09.nc

@Controller
public class SRMProxyController {

    private static final String DEFAULT_TYPE = "Dataset";

    private static boolean debugFlag = true;
    
    private static boolean printIDsFlag = false; 
    
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
     
        String [] file_ids = null;
        String [] file_urls = null;
        
        String type = request.getParameter("type");
        if(type == null) {
            type = "Dataset";
        }
        
        if(type.equals("Dataset")) {
            
            DocElement doc = querySolr(request);
            
            //System.out.println("Count: " + doc.getCount());

            file_ids = getFileIds(doc,"ids");
            file_urls = getFileIds(doc,"urls");
            
            
        
        } else {
        
            String file_id = request.getParameter("file_id");
            String file_url = request.getParameter("file_url");
            
            file_ids = new String [1];
            file_urls = new String [1];
            
            file_ids[0] = file_id;
            file_urls[0] = file_url;
            
        }
        
        
        
        /*
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
        
        */
        
        String responseStr = queryESGSRM(file_urls);
        
        
        
    }
    
    public static String [] getFileIds(DocElement doc, String which) {
        String [] file_ids = new String[doc.getFileElements().size()];
        String [] file_urls = new String[doc.getFileElements().size()];
        
        for(int i=0;i<doc.getFileElements().size();i++) {
            
            file_ids[i] = doc.getFileElements().get(i).getFileId();
            
            URLSElement urlsElement = doc.getFileElements().get(i).getURLSElement();
            for(int j=0;j<urlsElement.getUrls().size();j++) {
                if(urlsElement.getUrls().get(j).contains("srm://")) { 
                    file_urls[i] = urlsElement.getUrls().get(j);
                    if(printIDsFlag) {
                       System.out.println("\t" + file_urls[i]);
                    } 
                    
                }
            }
        }
        
        if(which.equals("ids")) {
            return file_ids;
        } else {
            return file_urls;
        }
        
    }
    
    
    public static DocElement querySolr(HttpServletRequest request) {
      //query solr first
        String filtered = request.getParameter("filtered");
        filtered = "false";
    
        
        String idStr = request.getParameter("dataset_id");
        if(idStr == null) {
            idStr = "null";
        }
        
       
        String peerStr = request.getParameter("peerStr");

        if(peerStr == null) {
            peerStr = "null";
        }
        
        //get the fq string and convert to an array of peers
        String fqStr = request.getParameter("fqParamStr");
        
        if(fqStr == null) {
            fqStr = "null";
        }
        String [] fq = fqStr.split(";");
        

        //get the search constraints togggle parameter
        String showAllStr = "true";
        if(showAllStr == null) {
            showAllStr = "null";
        }
        
        
        //get the flag denoting whether or not this is an initial Query
        String initialQuery = request.getParameter("initialQuery");
        if(initialQuery == null) {
            initialQuery = "null";
        }

        //get the fileCounter
        String fileCounter = request.getParameter("fileCounter");
        if(fileCounter == null) {
            fileCounter = "null";
        }
        
        System.out.println("--------");
        System.out.println("\tidStr\t" + idStr);
        System.out.println("\tpeerStr\t" + peerStr);
        System.out.println("\tfqStr\t" + fqStr);
        //System.out.println("\ttechnotesStr\t" + technotes);
        System.out.println("\tshowAllStr\t" + showAllStr);
        System.out.println("\tinitialQuery\t" + initialQuery);
        System.out.println("\tfileCount\t " + fileCounter + "\n");
        
        
        DocElement doc = FileDownloadTemplateController.getDocElement(idStr,peerStr,initialQuery,fq,showAllStr,fileCounter);
        
        return doc;
    }
    
    private static String queryESGSRM(String [] file_urls) {
        
        System.out.println("\nIn queryESGSRM\n");
        
        String response = null;
        String responseBody = null;
        
        // create an http client
        HttpClient client = new HttpClient();

        //attact the dataset id to the query string
        PostMethod method = new PostMethod(SRMUtils.srmAPIURL);
        
        String queryString = "";
        String unencodedQueryString = "";
        //add the urls
        for(int i=0;i<file_urls.length;i++) {
            System.out.println("\t adding id: " + i);
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

        System.out.println("\tQuerystring-> " + queryString);
        System.out.println("\n\n\n");
        System.out.println("\tUnencodedQuerystring-> " + unencodedQueryString);
        System.out.println("\n\n\n");
        
        
        /*
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
    */
        
        
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




