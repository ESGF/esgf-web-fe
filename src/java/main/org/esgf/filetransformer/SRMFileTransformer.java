package org.esgf.filetransformer;

import java.io.File;

import org.esgf.bestmanpath.BestmanPathGenerator;
import org.esgf.bestmanpath.BestmanPathGeneratorFactory;
import org.esgf.srmcache.SRMCacheStore;

public class SRMFileTransformer extends FileTransformer {

    public static String THREDDS_DATAROOT = "/thredds/fileServer/esg_dataroot/";
    

    public SRMFileTransformer(String fileTransformerName,SRMCacheStore srm_cache) {
        super(fileTransformerName,srm_cache);
    }
    
    public SRMFileTransformer(String fileTransformerName,SRMCacheStore srm_cache,String file_url) {
        super(fileTransformerName,srm_cache,file_url);
        System.out.println("\nIn SRMFileTransformer Constructor: " + fileTransformerName + "\n\tfile url: " + file_url);
    }
    
    
    public String getHttp() {
        
        /*
        String tempFile = this.getFileUrl().replace("srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://", "file:///");
        
        File f = new File(tempFile);
        String fileName = f.getName();
        
        String outputFile = "gsiftp://esg.ccs.ornl.gov:2811//lustre/esgfs/SRM/" + fileName;
        
        
        String http = outputFile.replace("gsiftp", "http");
        
        http = http.replace(":2811","");
        
        http = http.replace("//lustre/esgfs/", THREDDS_DATAROOT);
        */
        
        String http = SRMFileTransformationUtils.gridftp2http(this.getGridFTP());
        
        return http;
    }

    public String getGridFTP() {

        String outputFile = "";
        
        String tempFile = this.getFileUrl().replace("srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://", "file:///");
        //tempFile = transformServerName(tempFile);
        
        File f = new File(tempFile);
        String fileName = f.getName();
        
        String file_id = "";
        String dataset_id = "";
        
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

    public String getSRM() {
        return this.getFileUrl();
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
