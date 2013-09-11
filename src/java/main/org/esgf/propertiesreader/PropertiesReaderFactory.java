package org.esgf.propertiesreader;

public class PropertiesReaderFactory {

    public PropertiesReader makePropertiesReader(String propertiesReaderName) {
        
        if(propertiesReaderName.equalsIgnoreCase("SRM")) {
            return new SRMPropertiesReader(propertiesReaderName);
        } else {
            return null;
        }
        
            
    }
}
