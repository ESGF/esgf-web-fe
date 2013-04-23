package org.esgf.srmcache;

import java.util.ArrayList;
import java.util.List;

import org.esgf.solr.model.Solr;
import org.esgf.solr.model.SolrRecord;
import org.esgf.solr.model.SolrResponse;

public class RamSRMCacheStore extends SRMCacheStore {
    
    private List<SRMEntry> srm_entries;
    
    public RamSRMCacheStore() {
        setName("RAM");
        this.srm_entries = new ArrayList<SRMEntry>();
    }
    
    public SRMEntry getSRMEntryForFile_id(String dataset_id,String file_id) {
        
        SRMEntry srm_entry = null;
        
        for(int i=0;i<srm_entries.size();i++) {
           SRMEntry retrievedEntry = srm_entries.get(i);
           if(file_id.equals(retrievedEntry.getFile_id()) && dataset_id.equals(retrievedEntry.getDataset_id())) {
               srm_entry = retrievedEntry;
           }
               
        }
        
        return srm_entry;
    }

    public List<SRMEntry> getSRMEntriesForDataset_id(String dataset_id) {
        
        List<SRMEntry> entries = new ArrayList<SRMEntry>();
        
        for(int i=0;i<srm_entries.size();i++) {
            SRMEntry srm_entry = srm_entries.get(i);
            if(dataset_id.equals(srm_entry.getDataset_id())) {
                entries.add(srm_entry);
            }
                
         }
        
        return srm_entries;
        
    }

    public int addSRMEntry(SRMEntry srm_entry) {
        
        if(this.srm_entries.add(srm_entry)) {
            return 0;
        } else {
            return -1;
        }
        
    }

    public int updateSRMEntry(SRMEntry srm_entry) {
        
        for(int i=0;i<srm_entries.size();i++) {
            
            if(srm_entry.getDataset_id().equals(srm_entries.get(i).getDataset_id()) && 
               srm_entry.getFile_id().equals(srm_entries.get(i).getDataset_id())) {
                srm_entries.set(i, srm_entry);
                return 0;
            }
                
        }
        
        return -1;
    }

    public int deleteSRMEntry(SRMEntry srm_entry) {
        if(this.srm_entries.remove(srm_entry)) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public void initializeCacheStore() {
        
        List<SRMEntry> srm_entry_list = new ArrayList<SRMEntry>();
        
        String core = "Dataset";
        
        Solr solr = new Solr();
        int limit = 80;
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
            
            
            List<String> needsSRM = solrResponse.needsSRM();
            
            dataset_ids.addAll(needsSRM);
            
            solr.removeConstraint("offset");
            
            if(count < (counter+1)*limit) {
                iterate = false;
            }
            
            counter++;
        }
        
        for(int i=0;i<dataset_ids.size();i++) {
            
            //System.out.println("Querying-> " + dataset_ids.get(i));
            core = "File";
            solr = new Solr();
            limit = 80;
            solr.addConstraint("query", "*");
            solr.addConstraint("distrib", "false");
            solr.addConstraint("limit", Integer.toString(limit));
            solr.addConstraint("type", core);
            solr.addConstraint("dataset_id", dataset_ids.get(i));
            iterate = true;
            counter = 0;
            while(iterate) {
                String offset = Integer.toString(counter*limit);
                solr.addConstraint("offset", Integer.toString(counter*limit));
                //System.out.println("QueryString-> " + solr.getQueryString());
                solr.executeQuery();
                
                SolrResponse solrResponse = solr.getSolrResponse();
                
                String countStr = solrResponse.getCount();
                int count = Integer.parseInt(countStr);
                
                for(int j=0;j<solrResponse.getSolrRecords().size();j++) {
                    SolrRecord record = solrResponse.getSolrRecords().get(j);
                    String file_id = record.getStrField("id");
                    String dataset_id = dataset_ids.get(i);
                    //System.out.println("Dataset: " + dataset_ids.get(i) + " " + file_id);
                    String timeStamp = Long.toString(System.currentTimeMillis());
                    String isCached = "N/A";
                    SRMEntry srm_entry = new SRMEntry(file_id,dataset_id,isCached,timeStamp);
                    this.srm_entries.add(srm_entry);
                }
                
                solr.removeConstraint("offset");
                
                if(count < (counter+1)*limit) {
                    iterate = false;
                }
                
                counter++;
                
            }
        }
        
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
