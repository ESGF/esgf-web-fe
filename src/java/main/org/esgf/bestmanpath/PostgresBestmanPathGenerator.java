package org.esgf.bestmanpath;

import org.esgf.srmcache.SRMCacheStore;
import org.esgf.srmcache.SRMCacheStoreController;
import org.esgf.srmcache.SRMCacheStoreFactory;
import org.esgf.srmcache.SRMEntry;

public class PostgresBestmanPathGenerator extends BestmanPathGenerator {

    public PostgresBestmanPathGenerator(String dataset_id,String file_id) {
        super();
        this.name = "PostgresBestman";
        this.file_id = file_id;
        this.dataset_id = dataset_id;
    }
    
    @Override
    public String getBestmanPath() {
        
        SRMCacheStoreFactory srmCacheStore = new SRMCacheStoreFactory();
        
        SRMCacheStore srm_cache = srmCacheStore.makeSRMCacheStore(SRMCacheStoreController.DB_TYPE); 
        
        System.out.println("\t\tGETTING ENTRY FOR D: " + dataset_id + " AND F: " + file_id);
        SRMEntry srm_entry = srm_cache.getSRMEntryForFile_id(dataset_id, file_id);
        
        String bestmanNum = srm_entry.getBestmannumber();
        
        return bestmanNum;
    }

}
