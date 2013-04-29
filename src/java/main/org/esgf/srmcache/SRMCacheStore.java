package org.esgf.srmcache;

import java.util.List;

public abstract class SRMCacheStore {

    private String name;
    
    //get
    public abstract SRMEntry getSRMEntryForFile_id(String dataset_id,String file_id);
    public abstract List<SRMEntry> getSRMEntriesForDataset_id(String dataset_id);
    public abstract List<SRMEntry> getSRMEntriesForOpenid(String openid);
    
    //post
    public abstract int addSRMEntry(SRMEntry srm_entry);
    
    //put
    public abstract int updateSRMEntry(SRMEntry srm_entry);
    
    //delete
    public abstract int deleteSRMEntry(SRMEntry srm_entry);
    
    public abstract void createCacheStore();
    
    public abstract void initializeCacheStore();
    
    public abstract void removeCacheStore();
    
    public abstract void disconnect();
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
