package org.esgf.uiproperties;

import java.io.FileInputStream;
import java.util.Properties;

public class Utils {
    public static void removeAllProperties(String propertiesFile) {
        String [] facetTokens = null;
        
        Properties properties = new Properties();
        try {
            
            properties.load(new FileInputStream(propertiesFile));
        
            for(Object key : properties.keySet()) {

                properties.remove(key);
            }
            
        } catch(Exception e) {
        }
         
    }
}
