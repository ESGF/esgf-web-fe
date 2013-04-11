package org.esgf.srm.cache;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/srmcache")
public class SRMCacheController {

    private long BESTMAN_EXPIRATION = (24*60*60*1000);
    
    public static void main(String [] args) {
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        
        SRMCacheController srmCache = new SRMCacheController();
        
        mockRequest.addParameter("timeStamp", "tss2");
        
        srmCache.updateCache(mockRequest);
        
        String entry = srmCache.getEntry(mockRequest);
        
        mockRequest.addParameter("file_id", "file_id111");
        mockRequest.addParameter("dataset_id", "dataset_id111");
        mockRequest.addParameter("timeStamp", "timeStamp111");
        mockRequest.addParameter("isCache", "isCache111");
        
        srmCache.addEntry(mockRequest);
        
        
    }
    
    @RequestMapping(method=RequestMethod.POST, value="/addCache")
    public @ResponseBody String addEntry(HttpServletRequest request) {
    
        String response = "success\n";
        
        SRMCache srmCache = new SRMCache();
        
        Enumeration<String> paramEnum = request.getParameterNames();
        
        while(paramEnum.hasMoreElements()) { 
            String postContent = (String) paramEnum.nextElement();
            System.out.println(postContent+"-->"); 
            System.out.println(request.getParameter(postContent));
        }
        
        String file_id = request.getParameter("file_id");
        String dataset_id = request.getParameter("dataset_id");
        String isCached = request.getParameter("isCached");
        String timeStamp = request.getParameter("timeStamp");
        
        System.out.println("file_id: " + file_id);
        System.out.println("dataset_id: " + dataset_id);
        System.out.println("isCached: " + isCached);
        System.out.println("timeStamp: " + timeStamp);
        
        if(file_id == null || dataset_id == null || timeStamp == null) {
            response = "failure\n";
        } else {
            SRMEntry new_srm_entry = new SRMEntry(file_id,isCached,timeStamp,dataset_id);
            System.out.println("Adding entry");
            srmCache.addFileEntry(new_srm_entry);
        }
        
        
        
        return response;
    }
    
    
    @RequestMapping(method=RequestMethod.GET, value="/getCache")
    public @ResponseBody String getEntry(HttpServletRequest request) {
        
        String response = "expired";
        
        SRMCache srmCache = new SRMCache();
        
        String file_id = request.getParameter("file_id");
        if(file_id == null) {
            file_id = "";
        }
        
        System.out.println("In getCache for file_id: " + file_id);
        
        SRMEntry entry = srmCache.getSRMEntryForFileId(file_id);
        
        
        Long dbTimeStamp = Long.parseLong(entry.getTimeStamp());
        Long expiredTime = dbTimeStamp.longValue() + BESTMAN_EXPIRATION;
        Long currentTimeStamp = (System.currentTimeMillis());
        
        System.out.println("EXPIRED: " + expiredTime + " CURRENT: " + currentTimeStamp);
        if(dbTimeStamp.longValue() < currentTimeStamp.longValue()) {
            response = "current";
        }
        
        return response;
    }
    
    
    @RequestMapping(method=RequestMethod.PUT, value="/updateCache")
    public @ResponseBody String updateCache(HttpServletRequest request) {
    
        String response = "success\n";
        
        SRMCache srmCache = new SRMCache();
        
        String file_id = request.getParameter("file_id");
        String timeStamp = request.getParameter("timeStamp");
        
        Long timeStampLong = System.nanoTime();
        timeStamp = timeStampLong.toString();
        
        if(file_id == null || timeStamp == null) {
            response = "failure\n";
        } else {
            srmCache.setFileTimeStamp(file_id,timeStamp);
        }
        
        return response;
    }
    
}
