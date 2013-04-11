package org.esgf.srm.cache;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esgf.solr.model.Solr;
import org.esgf.solr.model.SolrRecord;
import org.esgf.solr.model.SolrResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class SRMEntryListController {
    public static String searchAPI = "http://localhost:8080/esg-search/search?";
    
    
    public static void main(String [] args) {

        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        SRMEntryListController fc = new SRMEntryListController();
        
        
        
        String type = "Dataset";
        String id = "ornl.ultrahighres.CESM1.t85f09.F1850p.v1|esg2-sdnl1.ccs.ornl.gov";
        
        mockRequest.addParameter("type", type);
        mockRequest.addParameter("dataset_id", id);
        
        String response = fc.doPostList(mockRequest, null);
        
       
        System.out.println("response: " + response);
        
    }
    
    /*
     * Given:
     * - a dataset id
     * Return:
     * - a list of file ids that are cached
     */
    @RequestMapping(method=RequestMethod.POST, value="/isCachedList")
    public @ResponseBody String doPostList(HttpServletRequest request,final HttpServletResponse response) {
        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            dataset_id = "";
        }
        
        
        String cachedList = "";
        cachedList += "<isCachedList>";
        
        SRMEntryList srm_entry_list = SRMEntryList4DatasetId(dataset_id);
        
        for(int i=0;i<srm_entry_list.getSrm_entry_list().size();i++) {
            SRMEntry srm_entry = srm_entry_list.getSrm_entry_list().get(i);
            if(srm_entry.getIsCached().equals("true")) {
                cachedList += "<isCached>" + srm_entry.getFile_id() + "</isCached>";
            }
        }
        
        
        cachedList += "</isCachedList>";
                
        return cachedList;
        
    }
    
    /*
     * Given:
     * - an id
     * - a type
     * Return:
     * - yes/no (yes => cached, no => not cached)
     */
    @RequestMapping(method=RequestMethod.POST, value="/isCached")
    public @ResponseBody String doPost(HttpServletRequest request,final HttpServletResponse response) {
    
        
        String type = request.getParameter("type");
        if(type == null) {
            type = "Dataset";
        }
        
        String id = request.getParameter("id");
        if(id == null) {
            id = "";
        }
        
        SRMEntryList srm_entry_list = new SRMEntryList();
        
        srm_entry_list.fromFile("srm_entry_list_" + type + ".xml");
        
        String isCached = srm_entry_list.isCached(id);
        
        System.out.println("isCached: " + isCached);
        
        
        return isCached;
        
    }
    
    
    //returns a sub srmentry list for a given dataset_id
    public static SRMEntryList SRMEntryList4DatasetId(String dataset_id) {
        
        
        SRMEntryList oldSRMEntryList = new SRMEntryList();
        SRMEntryList newSRMEntryList = new SRMEntryList();
        
        oldSRMEntryList.fromFile("srm_entry_list_" + "File" + ".xml");
        for(int i=0;i<oldSRMEntryList.getSrm_entry_list().size();i++) {
            
            SRMEntry srm_entry = oldSRMEntryList.getSrm_entry_list().get(i);
            
            //System.out.println(srm_entry.getDataset_id());
            if(srm_entry.getDataset_id().equals(dataset_id)) {
                newSRMEntryList.addSRMEntry(srm_entry);
            }
        }
        
        return newSRMEntryList;
        
    }
}
