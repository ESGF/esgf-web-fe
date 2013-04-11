package org.esgf.srm.cache;

import java.util.ArrayList;
import java.util.List;

import org.esgf.solr.model.Solr;
import org.esgf.solr.model.SolrResponse;

public class SRMCache {
    
    private static int limit = 80;
    
    private boolean cacheOn;
    private boolean isDB;
    private SRMEntryList srm_entry_list;
    
    public SRMCache() {
        this.setCacheOn(true);
        this.isDB = true;
        this.srm_entry_list = new SRMEntryList();
        this.srm_entry_list.fromDB();
    }
    
    public SRMCache(boolean cacheOn) {
        this.setCacheOn(cacheOn);
        this.isDB = true;
        this.srm_entry_list = new SRMEntryList();
        this.srm_entry_list.fromDB();
        
    }
    
    public SRMCache(boolean cacheOn,boolean isDB) {
        this.cacheOn = cacheOn;
        this.isDB = isDB;
        this.srm_entry_list = new SRMEntryList();
        if(isDB) {

            this.srm_entry_list.fromDB();
        }
    }
    
    public static void main(String [] args) {
        
        
        /*
        SRMCache srmCache = new SRMCache();
        srmCache.getSRMEntryListForDatasetId("dataset_ide1");
        
        
        SRMEntry new_entry = new SRMEntry("a","b","c","d");
        new_entry.toDB();
        */
        
        SRMCache srmCache = new SRMCache();
        srmCache.initializeCache("File");
    }
    
    
    public void initializeCache(String core) {
        
        Solr solr = new Solr();
        limit = 80;
        solr.addConstraint("query", "*");
        solr.addConstraint("distrib", "false");
        solr.addConstraint("limit", Integer.toString(limit));
        
        solr.addConstraint("type", core);

        List<String> srms = new ArrayList<String>();
        List<String> dataset_ids = new ArrayList<String>();
        
        boolean iterate = true;
        int counter = 0;
        
        
        while(iterate) {
            String offset = Integer.toString(counter*limit);
            
            solr.addConstraint("offset", Integer.toString(counter*limit));
            solr.executeQuery();
            
            SolrResponse solrResponse = solr.getSolrResponse();
            
            String countStr = solrResponse.getCount();
            int count = Integer.parseInt(countStr);

            List<String> srmDatasets = solrResponse.needsSRM(core);
            srms.addAll(srmDatasets);
            
            List<String> dataset_ids_sublist = solrResponse.getDatasetIds(core);
            dataset_ids.addAll(dataset_ids_sublist);
            solr.removeConstraint("offset");
            
            if(count < (counter+1)*limit) {
                iterate = false;
            }
            counter++;
            
            
        }
        System.out.println("Here?");
        for(int i=0;i<srms.size();i++) {
            
            String file_id = srms.get(i);
            
            //System.out.println("File_id: " + file_id);
            String dataset_id = dataset_ids.get(i);
            String timeStamp = Long.toString(System.currentTimeMillis());
           
            //if(i < 4) {
                SRMEntry new_entry = new SRMEntry(file_id,"false",timeStamp,dataset_id);
            //    System.out.println("file: " + file_id);
                new_entry.toDB();
            //}
                
        }
        
        
        
    }
    
    
    //get listing for just a dataset
    public List<SRMEntry> getSRMEntryListForDatasetId(String dataset_id) {
        
        List<SRMEntry> entries = this.srm_entry_list.getSRMEntriesForDatasetId(dataset_id);
       
        
        return entries;
    }
    
    //get SRMEntry for just a file
    public SRMEntry getSRMEntryForFileId(String file_id) {
        
        SRMEntry entry = this.srm_entry_list.getSRMEntryForFileId(file_id);
        
        return entry;
    }
    
    
    public void setFileTimeStamp(String file_id,String timeStamp) {
        SRMEntry entry = getSRMEntryForFileId(file_id);
        if(entry != null) {
            entry.setTimeStamp(timeStamp);
            if(this.isDB) {
                entry.toDB();
            }
        }
        
    }
    
    public void setDatasetTimeStamp(String dataset_id,String timestamp) {
        List<SRMEntry> entries = getSRMEntryListForDatasetId(dataset_id);
        
        if(entries != null) {
            for(int i=0;i<entries.size();i++) {
                if(this.isDB) {
                    entries.get(i).toDB();
                }
            }
        }
        
        
    }
    
    public void addFileEntry(SRMEntry srm_entry) {
        srm_entry.toDB();
        this.srm_entry_list.addSRMEntry(srm_entry);
    }
   

    /**
     * @return the srm_entry_list
     */
    public SRMEntryList getSrm_entry_list() {
        return srm_entry_list;
    }

    /**
     * @param srm_entry_list the srm_entry_list to set
     */
    public void setSrm_entry_list(SRMEntryList srm_entry_list) {
        this.srm_entry_list = srm_entry_list;
    }

    /**
     * @return the cacheOn
     */
    public boolean isCacheOn() {
        return cacheOn;
    }

    /**
     * @param cacheOn the cacheOn to set
     */
    public void setCacheOn(boolean cacheOn) {
        this.cacheOn = cacheOn;
    }
    
    
    
    

}
