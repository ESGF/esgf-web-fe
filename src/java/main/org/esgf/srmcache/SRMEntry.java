package org.esgf.srmcache;

public class SRMEntry {

    private String file_id;
    private String dataset_id;
    private String isCached;
    private String timeStamp;
    
    public SRMEntry(String file_id,String dataset_id,String isCached,String timeStamp) {
        this.file_id = file_id;
        this.dataset_id = dataset_id;
        this.isCached = isCached;
        this.timeStamp = timeStamp;
    }
    
    public SRMEntry(String file_id,String dataset_id,String isCached) {
        this.file_id = file_id;
        this.dataset_id = dataset_id;
        this.isCached = isCached;
        
        Long timeStampLong = System.currentTimeMillis();
        this.timeStamp = timeStampLong.toString();
        
    }
    
    
    /**
     * @return the file_id
     */
    public String getFile_id() {
        return file_id;
    }
    /**
     * @param file_id the file_id to set
     */
    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }
    /**
     * @return the isCached
     */
    public String getIsCached() {
        return isCached;
    }
    /**
     * @param isCached the isCached to set
     */
    public void setIsCached(String isCached) {
        this.isCached = isCached;
    }
    /**
     * @return the timeStamp
     */
    public String getTimeStamp() {
        return timeStamp;
    }
    /**
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    /**
     * @return the dataset_id
     */
    public String getDataset_id() {
        return dataset_id;
    }
    /**
     * @param dataset_id the dataset_id to set
     */
    public void setDataset_id(String dataset_id) {
        this.dataset_id = dataset_id;
    }
    
}
