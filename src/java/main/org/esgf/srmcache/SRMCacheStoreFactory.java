package org.esgf.srmcache;

public class SRMCacheStoreFactory {

    public SRMCacheStore makeSRMCacheStore(String type) {
        
        if(type.equalsIgnoreCase("RAM")) {
            
            return new RamSRMCacheStore();
            
        } else if(type.equalsIgnoreCase("XML")){
            
            return new XmlSRMCacheStore();
            
        } else {
            
            return new PostgresSRMCacheStore();
        
        }
        
    }
    
    
    
}
