package org.esgf.srmcache;

import java.util.List;

public class Driver {
    
    public static void main(String [] args) {
        
        SRMCacheStoreFactory srmCacheStore = new SRMCacheStoreFactory();
        
        String type = "postgres";
        
        SRMCacheStore srm_cache = srmCacheStore.makeSRMCacheStore(type); 
        
        System.out.println(srm_cache.getName());
        
        SRMEntry srm_entry = new SRMEntry("a","b","c","d","e");
        
        srm_cache.initializeCacheStore();
        
        //List<SRMEntry> entries = srm_cache.getSRMEntriesForDataset_id("ornl.ultrahighres.CESM1.t85f09.B1850_50yrs.seaIce.v1%7Cesg2-sdnl1.ccs.ornl.gov");
        List<SRMEntry> entries = srm_cache.getSRMEntriesForDataset_id("ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1|esg2-sdnl1.ccs.ornl.gov");
        //System.out.println("size: " + entries.size());
        
    }
}
