package org.esgf.srmcache;

import java.util.List;

public class XmlSRMCacheStore extends SRMCacheStore {

    public XmlSRMCacheStore() {
        setName("xml");
    }
    
    public SRMEntry getSRMEntryForFile_id(String dataset_id,String file_id) {
        return null;
    }

    public List<SRMEntry> getSRMEntriesForDataset_id(String dataset_id) {
        return null;
    }

    public int addSRMEntry(SRMEntry srm_entry) {
        return 0;
    }

    public int updateSRMEntry(SRMEntry srm_entry) {
        return 0;
    }

    public int deleteSRMEntry(SRMEntry srm_entry) {
        return 0;
    }

    @Override
    public void initializeCacheStore() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeCacheStore() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void disconnect() {
        // TODO Auto-generated method stub
        
    }

}
