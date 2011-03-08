/**
 * @author - fwang2@ornl.gov
 */

package org.esgf.solr.proxy;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

@Controller
@RequestMapping("/solrproxy")
public class SolrProxyController {

    private String solrURL="http://localhost:8983/solr/";
    private final static Logger LOG = Logger.getLogger(SolrProxyController.class);

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String doGet(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("doGet");
        return relay(request, response);
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public @ResponseBody String doPost(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("doPost");
        return relay(request, response);
    }
    

    private String relay(HttpServletRequest request, HttpServletResponse response) {
        
        String queryString = request.getQueryString();        
        LOG.debug("queryString=" + queryString);
        String requestUri = request.getRequestURI();
        LOG.debug("requestUri=" + requestUri);
        
        String urlString = solrURL + "select?" + queryString + "&wt=json";
        
        String responseBody = null;
        
        // create an http client
        HttpClient client = new HttpClient();
        
        // create a method instance
        GetMethod method = new GetMethod(urlString);
        
        // custom retry
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
        
        LOG.debug("Solr URL = " + urlString);
        LOG.debug("responseBody = " + responseBody);
        return responseBody;
    }
}
