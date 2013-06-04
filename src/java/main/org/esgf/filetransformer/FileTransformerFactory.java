package org.esgf.filetransformer;

import org.esgf.srmcache.SRMCacheStore;


public class FileTransformerFactory {

    public FileTransformer makeFileTransformer(String fileTransformerName,SRMCacheStore srm_cache,String file_id) {
            
        if(fileTransformerName.equalsIgnoreCase("HTTP")) {
            return new HTTPFileTransformer(fileTransformerName,srm_cache,file_id);
        } else if(fileTransformerName.equalsIgnoreCase("GridFTP")){
            return new GridFTPFileTransformer(fileTransformerName,srm_cache,file_id);
        } else if(fileTransformerName.equalsIgnoreCase("SRM")){
            return new SRMFileTransformer(fileTransformerName,srm_cache,file_id);
        } else {
            return null;
        }
        
            
    }
    
    public FileTransformer makeFileTransformer(String fileTransformerName,SRMCacheStore srm_cache,String file_url,String dataset_id,String file_id) {
        
        return new GeneralFileTransformer(fileTransformerName,srm_cache,file_url,dataset_id,file_id);
        
    }
}
