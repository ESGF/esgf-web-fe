package org.esgf.srmcache;

import javax.servlet.http.HttpServletRequest;

import org.esgf.srm.cache.SRMCache;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class SRMCacheStoreController {

    private long BESTMAN_EXPIRATION = (24*60*60*1000);
    
    private static String DB_TYPE = "postgres";
    
    private SRMCacheStore srm_cache;
    
    public static void main(String [] args) {
        
        SRMCacheStoreController srm_cache_controller = new SRMCacheStoreController();
        
        /*
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        String dataset_id = "a";
        mockRequest.addParameter("dataset_id", dataset_id);
        
        String file_id = "b";
        mockRequest.addParameter("file_id", file_id);
        
        String openid = "openid1";
        mockRequest.addParameter("openid", openid);

        srm_cache_controller.addSRMEntry(mockRequest);
        
        mockRequest.setParameter("file_id", "c");
        
        srm_cache_controller.addSRMEntry(mockRequest);
        
        //mockRequest.setParameter("file_id", "c");
        
        //String srm_entry = srm_cache_controller.getSRMEntry(mockRequest);
        //System.out.println(srm_entry);
         
         */
    }
    
    public SRMCacheStoreController() {

        SRMCacheStoreFactory srmCacheStore = new SRMCacheStoreFactory();
        
        srm_cache = srmCacheStore.makeSRMCacheStore(DB_TYPE); 
        
        
        /*
        srm_cache.createCacheStore();
        */
    }
    
    
    //input srmentry stuff (dataset_id and file_id)
    @RequestMapping(method=RequestMethod.POST, value="/addSRMEntry")
    public @ResponseBody String addSRMEntry(HttpServletRequest request) {
    
        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            dataset_id = "dataset_id";
        }
        String file_id = request.getParameter("file_id");
        if(file_id == null) {
            file_id = "file_id";
        }
        
        String openid = request.getParameter("openid");
        if(openid == null) {
            openid = "openid";
        }
        
        //srm_cache.createCacheStore();
        
        
        
        String isCached = "N/A";
        String timeStamp = Long.toString(System.currentTimeMillis());
        
        SRMEntry srm_entry = new SRMEntry(file_id,dataset_id,isCached,timeStamp,openid);

        if(srm_cache.addSRMEntry(srm_entry) == 0) {
            return "success";
        } else {
            return "failure";
        }
        
        
    }
    
    //input dataset_id and file_id
    @RequestMapping(method=RequestMethod.GET, value="/getSRMEntry")
    public @ResponseBody String getSRMEntry(HttpServletRequest request) {

        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            dataset_id = "dataset_id";
        }
        String file_id = request.getParameter("file_id");
        if(file_id == null) {
            file_id = "file_id";
        }
        
        SRMEntry srm_entry = srm_cache.getSRMEntryForFile_id(dataset_id, file_id);
        if(srm_entry == null) {
            return "failure";
        } else {
            return srm_entry.toXML();
        }
        
        
    }

    //input srmentry stuff (dataset_id and file_id)
    @RequestMapping(method=RequestMethod.PUT, value="/updateSRMEntry")
    public @ResponseBody String updateSRMEntry(HttpServletRequest request) {

        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            dataset_id = "dataset_id";
        }
        String file_id = request.getParameter("file_id");
        if(file_id == null) {
            file_id = "file_id";
        }
        
        
        return "success";
        
    }
    

    //input srmentry stuff (dataset_id and file_id)
    @RequestMapping(method=RequestMethod.DELETE, value="/deleteSRMEntry")
    public @ResponseBody String deleteSRMEntry(HttpServletRequest request) {

        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            dataset_id = "dataset_id";
        }
        String file_id = request.getParameter("file_id");
        if(file_id == null) {
            file_id = "file_id";
        }
        
        
        
        return "success";
    
    }
    
    
    
}
