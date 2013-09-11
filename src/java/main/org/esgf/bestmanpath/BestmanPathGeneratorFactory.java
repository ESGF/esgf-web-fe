package org.esgf.bestmanpath;

import org.esgf.srmcache.SRMCacheStore;

public class BestmanPathGeneratorFactory {

    public BestmanPathGenerator makeBestmanPathGenerator(String bestmanPathGeneratorName,SRMCacheStore srm_cache,String dataset_id,String file_id) {
        
        if(bestmanPathGeneratorName.equalsIgnoreCase("Random")) {
            return new RandomBestmanPathGenerator();
        } else {
            return new PostgresBestmanPathGenerator(srm_cache,dataset_id,file_id);
        }
        
    }
}
