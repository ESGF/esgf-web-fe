package org.esgf.propertiesreader;

import org.esgf.filetransformer.FileTransformer;
import org.esgf.filetransformer.GridFTPFileTransformer;
import org.esgf.filetransformer.HTTPFileTransformer;
import org.esgf.filetransformer.SRMFileTransformer;

public class PropertiesReaderFactory {

    public PropertiesReader makePropertiesReader(String propertiesReaderName) {
        
        if(propertiesReaderName.equalsIgnoreCase("SRM")) {
            return new SRMPropertiesReader(propertiesReaderName);
        } else {
            return null;
        }
        
            
    }
}
