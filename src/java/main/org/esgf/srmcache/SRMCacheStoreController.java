package org.esgf.srmcache;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.esgf.srm.SRMControls;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SRMCacheStoreController {

    private long BESTMAN_EXPIRATION = (24*60*60*1000);
    
    private static String DB_TYPE = "postgres";

    private static String FAILURE_MESSAGE = "failure";
    private static String SUCCESS_MESSAGE = "success";
    
    private SRMCacheStore srm_cache;
    
    public static void main(String [] args) {
        
        SRMCacheStoreController srm_cache_controller = new SRMCacheStoreController();
        
        
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        String dataset_id = "a";
        mockRequest.addParameter("dataset_id", dataset_id);
        
        String file_id = "b";
        mockRequest.addParameter("file_id", file_id);
        
        String openid = "openid1";
        mockRequest.addParameter("openid", openid);

        
        System.out.println(srm_cache_controller.getSRMEntry(mockRequest));
        /*
        
        mockRequest.setParameter("dataset_id", "a");
        mockRequest.setParameter("file_id", "o");
        mockRequest.setParameter("openid", "openid2");
        
        System.out.println("adding...");
        srm_cache_controller.addSRMEntry(mockRequest);

        try {
            Thread.sleep(1000);
            System.out.println("adding...");
            srm_cache_controller.updateSRMEntry(mockRequest);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

        mockRequest.setParameter("openid", "openid1");
        
        String result_xml = srm_cache_controller.getSRMEntryOpenId(mockRequest);
        
        System.out.println(result_xml);
        
        
        
        result_xml = srm_cache_controller.getSRMEntryDatasetId(mockRequest);
        
        System.out.println(result_xml);
        */
        
        /*
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
        
    }
    
    
    //input srmentry stuff (dataset_id and file_id)
    @RequestMapping(method=RequestMethod.POST, value="/addSRMEntry")
    public @ResponseBody String addSRMEntry(HttpServletRequest request) {
    
        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            return FAILURE_MESSAGE;
        }
        String file_id = request.getParameter("file_id");
        if(file_id == null) {
            return FAILURE_MESSAGE;
        }
        String openid = request.getParameter("openid");
        if(openid == null) {
            return FAILURE_MESSAGE;
        }
        
        
        //add a dummy value for isCached until we find a better use for it
        String isCached = "N/A";
        String timeStamp = Long.toString(System.currentTimeMillis());
        
        long expirationLong = Long.parseLong(timeStamp) + SRMControls.expiration;
        String expiration = Long.toString(expirationLong);
        
        SRMEntry srm_entry = new SRMEntry(file_id,dataset_id,isCached,timeStamp,expiration,openid);

        if(srm_cache.addSRMEntry(srm_entry) == 0) {
            return SUCCESS_MESSAGE;
        } else {
            return FAILURE_MESSAGE;
        }
        
    }
    
    //automatically populate the db
    @RequestMapping(method=RequestMethod.POST, value="/initializeSRMEntryList")
    public @ResponseBody String initializeCacheDBFromSolr(HttpServletRequest request) {
    
        String openid = request.getParameter("openid");
        if(openid == null) {
            return FAILURE_MESSAGE;
        }
        
        
        this.srm_cache.initializeCacheStore();
        
        
        return SUCCESS_MESSAGE;
        
        
    }
    
    //input dataset_id and file_id
    @RequestMapping(method=RequestMethod.GET, value="/getSRMEntry")
    public @ResponseBody String getSRMEntry(HttpServletRequest request) {

        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            return FAILURE_MESSAGE;
        }
        String file_id = request.getParameter("file_id");
        if(file_id == null) {
            return FAILURE_MESSAGE;
        }
        String openid = request.getParameter("openid");
        if(openid == null) {
            return FAILURE_MESSAGE;
        }
        
        SRMEntry srm_entry = srm_cache.getSRMEntryForFile_id(dataset_id, file_id);
        if(srm_entry == null) {
            return FAILURE_MESSAGE;
        } else {
            return srm_entry.toXML();
        }
        
        
    }
    
    //input dataset_id
    @RequestMapping(method=RequestMethod.GET, value="/getSRMEntryDatasetId")
    public @ResponseBody String getSRMEntryDatasetId(HttpServletRequest request) {
        
        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            return FAILURE_MESSAGE;
        }
        
        
        List<SRMEntry> srm_entries = srm_cache.getSRMEntriesForDataset_id(dataset_id);
        if(srm_entries == null) {
            return FAILURE_MESSAGE;
        } else {
            return srmEntryListXML(srm_entries);
        }
        
    }
    
    //input open_id
    @RequestMapping(method=RequestMethod.GET, value="/getSRMEntryOpenId")
    public @ResponseBody String getSRMEntryOpenId(HttpServletRequest request) {
        
        String openid = request.getParameter("openid");
        if(openid == null) {
            return FAILURE_MESSAGE;
        }
        
        List<SRMEntry> srm_entries = srm_cache.getSRMEntriesForOpenid(openid);
        if(srm_entries == null) {
            return FAILURE_MESSAGE;
        } else {
            return srmEntryListXML(srm_entries);
        }
        
    }

    //input srmentry stuff (dataset_id and file_id)
    @RequestMapping(method=RequestMethod.PUT, value="/updateSRMEntry")
    public @ResponseBody String updateSRMEntry(HttpServletRequest request) {

        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            return FAILURE_MESSAGE;
        }
        String file_id = request.getParameter("file_id");
        if(file_id == null) {
            return FAILURE_MESSAGE;
        }
        String openid = request.getParameter("openid");
        if(openid == null) {
            return FAILURE_MESSAGE;
        }
        
        
        
        SRMEntry srm_entry = srm_cache.getSRMEntryForFile_id(dataset_id, file_id);
        
        String timeStamp = Long.toString(System.currentTimeMillis());
        long expirationLong = Long.parseLong(timeStamp) + SRMControls.expiration;

        srm_entry.setTimeStamp(timeStamp);
        srm_entry.setExpiration(Long.toString(expirationLong));
        
        
        if(this.srm_cache.updateSRMEntry(srm_entry) == 1) {
            return SUCCESS_MESSAGE;
        } else {
            return FAILURE_MESSAGE;
        }
        
        
    }
    

    //input srmentry stuff (dataset_id and file_id)
    @RequestMapping(method=RequestMethod.DELETE, value="/deleteSRMEntry")
    public @ResponseBody String deleteSRMEntry(HttpServletRequest request) {

        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            return FAILURE_MESSAGE;
        }
        String file_id = request.getParameter("file_id");
        if(file_id == null) {
            return FAILURE_MESSAGE;
        }
        String openid = request.getParameter("openid");
        if(openid == null) {
            return FAILURE_MESSAGE;
        }
        
        SRMEntry srm_entry = srm_cache.getSRMEntryForFile_id(dataset_id, file_id);
        
        if(this.srm_cache.deleteSRMEntry(srm_entry) == 1) {
            return SUCCESS_MESSAGE;
        } else {
            return FAILURE_MESSAGE;
        }
        
    
    }
    
    
    public static String srmEntryListXML(List<SRMEntry> entries) {
        String xml = "";
        
        Element srmentryListingEl = new Element("srm_entry");//this.toElement();
        
        for(int i=0;i<entries.size();i++) {
            SRMEntry srm_entry = entries.get(i);
            srmentryListingEl.addContent(srm_entry.toElement());
        }
        

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(srmentryListingEl);
        
        return xml;
    }
    
}
