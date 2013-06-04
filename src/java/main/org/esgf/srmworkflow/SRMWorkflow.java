package org.esgf.srmworkflow;

import org.esgf.srm.SRMResponse;
import org.esgf.srmcache.SRMCacheStore;

public abstract class SRMWorkflow {

    private String type;
    
    public SRMWorkflow(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public abstract SRMResponse runWorkFlow(String [] srm_files,SRMCacheStore srm_cache);
    
}
