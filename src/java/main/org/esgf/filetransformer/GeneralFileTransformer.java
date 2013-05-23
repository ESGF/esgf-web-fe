package org.esgf.filetransformer;

import java.io.File;

import org.esgf.bestmanpath.BestmanPathGenerator;
import org.esgf.bestmanpath.BestmanPathGeneratorFactory;

public class GeneralFileTransformer extends FileTransformer  {

    public GeneralFileTransformer(String fileTransformerName) {
        super(fileTransformerName);
    }
    
    public GeneralFileTransformer(String fileTransformerName,String file_id) {
        super(fileTransformerName,file_id);
    }
    
    public GeneralFileTransformer(String fileTransformerName,String file_url,String dataset_id,String file_id) {
        super(fileTransformerName,file_url,dataset_id,file_id);
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
        //tempFile = transformServerName(tempFile);
        
        File f = new File(tempFile);
        String fileName = f.getName();
        
        /*
        String file_id = "";
        String dataset_id = "";
        */
        
        BestmanPathGeneratorFactory bestmanPathGeneratorFactory = new BestmanPathGeneratorFactory();
        BestmanPathGenerator bestmanNumGenerator = 
                bestmanPathGeneratorFactory.makeBestmanPathGenerator("Postgres",dataset_id,file_id);
        
        /*
        BestmanPathGenerator bestmanNumGenerator = 
                bestmanPathGeneratorFactory.makeBestmanPathGenerator(SRMFileTransformationUtils.BESTMAN_PATH_GENERATOR_TYPE,dataset_id,file_id);
        */
        String bestmannum = bestmanNumGenerator.getBestmanPath();
        
        
        
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

}
