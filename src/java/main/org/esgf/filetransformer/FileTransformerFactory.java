package org.esgf.filetransformer;


public class FileTransformerFactory {

    public FileTransformer makeFileTransformer(String fileTransformerName,String file_id) {
            
        if(fileTransformerName.equalsIgnoreCase("HTTP")) {
            return new HTTPFileTransformer(fileTransformerName,file_id);
        } else if(fileTransformerName.equalsIgnoreCase("GridFTP")){
            return new GridFTPFileTransformer(fileTransformerName,file_id);
        } else if(fileTransformerName.equalsIgnoreCase("SRM")){
            return new SRMFileTransformer(fileTransformerName,file_id);
        } else {
            return null;
        }
        
            
    }
    
    public FileTransformer makeFileTransformer(String fileTransformerName,String file_url,String dataset_id,String file_id) {
        
        return new GeneralFileTransformer(fileTransformerName,file_url,dataset_id,file_id);
        
    }
}
