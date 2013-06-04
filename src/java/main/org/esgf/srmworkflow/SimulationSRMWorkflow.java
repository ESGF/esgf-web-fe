package org.esgf.srmworkflow;

import org.esgf.filetransformer.SRMFileTransformationUtils;
import org.esgf.srm.SRMResponse;
import org.esgf.srmcache.SRMCacheStore;

public class SimulationSRMWorkflow extends SRMWorkflow {

    public SimulationSRMWorkflow(String type) {
        super(type);
    }

    public SRMResponse runWorkFlow(String [] srm_files,SRMCacheStore srm_cache) {
        
        String [] outputFiles = SRMFileTransformationUtils.simulateSRM(srm_files,srm_cache);
        
        SRMResponse srm_response = new SRMResponse();
        srm_response.setMessage("SUCCESS");
        srm_response.setResponse_urls(outputFiles);
        
        return srm_response;
        
    }

}
