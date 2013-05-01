package org.esgf.filetransformer;

import java.io.File;

public class SRMFileTransformer extends FileTransformer {

    public static String THREDDS_DATAROOT = "/thredds/fileServer/esg_dataroot/";
    

    public SRMFileTransformer(String fileTransformerName) {
        super(fileTransformerName);
    }
    
    public SRMFileTransformer(String fileTransformerName,String file_id) {
        super(fileTransformerName,file_id);
    }
    
    
    public String getHttp() {
        
        String tempFile = this.getFileId().replace("srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://", "file:///");
        
        File f = new File(tempFile);
        String fileName = f.getName();
        
        String outputFile = "gsiftp://esg.ccs.ornl.gov:2811//lustre/esgfs/SRM/" + fileName;
        
        
        String http = outputFile.replace("gsiftp", "http");
        
        http = http.replace(":2811","");
        
        http = http.replace("//lustre/esgfs/", THREDDS_DATAROOT);
        
        
        return http;
    }

    public String getGridFTP() {

        String tempFile = this.getFileId().replace("srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://", "file:///");
        
        File f = new File(tempFile);
        String fileName = f.getName();
        
        String outputFile = "gsiftp://esg.ccs.ornl.gov:2811//lustre/esgfs/SRM/" + fileName;
        
        return outputFile;
    }

    public String getSRM() {
        return this.getFileId();
    }

    @Override
    public String getFileName() {
        // TODO Auto-generated method stub
        return null;
    }

}
