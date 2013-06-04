package org.esgf.filetransformer;

import java.io.File;

import org.esgf.bestmanpath.BestmanPathGenerator;
import org.esgf.bestmanpath.BestmanPathGeneratorFactory;
import org.esgf.srmcache.SRMCacheStore;

public class GeneralFileTransformer extends FileTransformer  {

    public GeneralFileTransformer(String fileTransformerName,SRMCacheStore srm_cache) {
        super(fileTransformerName,srm_cache);
    }
    
    public GeneralFileTransformer(String fileTransformerName,SRMCacheStore srm_cache,String file_id) {
        super(fileTransformerName,srm_cache,file_id);
    }
    
    public GeneralFileTransformer(String fileTransformerName,SRMCacheStore srm_cache,String file_url,String dataset_id,String file_id) {
        super(fileTransformerName,srm_cache,file_url,dataset_id,file_id);
    }
    
    @Override
    public String getHttp() {

        String http = SRMFileTransformationUtils.gridftp2http(this.getGridFTP());
        
        return http;
    }

    @Override
    public String getGridFTP() {

        
        String outputFile = "";
        
        String tempFile = this.getFileUrl().replace("srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://", "file:///");
        
        File f = new File(tempFile);
        String fileName = f.getName();
        
        
        BestmanPathGeneratorFactory bestmanPathGeneratorFactory = new BestmanPathGeneratorFactory();
        BestmanPathGenerator bestmanNumGenerator = 
                bestmanPathGeneratorFactory.makeBestmanPathGenerator("Postgres",srm_cache,dataset_id,file_id);
        
        
        /*
        BestmanPathGenerator bestmanNumGenerator = 
                bestmanPathGeneratorFactory.makeBestmanPathGenerator(SRMFileTransformationUtils.BESTMAN_PATH_GENERATOR_TYPE,dataset_id,file_id);
        */
        String bestmannum = bestmanNumGenerator.getBestmanPath(srm_cache);
        
        
        
        outputFile = "gsiftp://esg.ccs.ornl.gov:2811//lustre/esgfs/shared/" + bestmannum + "/" + fileName;
        
        return outputFile;
    }

    @Override
    public String getSRM() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFileName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SRMCacheStore getSRMCacheStore() {
        // TODO Auto-generated method stub
        return null;
    }

}
