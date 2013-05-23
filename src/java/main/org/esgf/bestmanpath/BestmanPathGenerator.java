package org.esgf.bestmanpath;

public abstract class BestmanPathGenerator {

    protected String name;
    protected String file_id;
    protected String dataset_id;
    
    public String getDataset_id() {
        return dataset_id;
    }

    public void setDataset_id(String dataset_id) {
        this.dataset_id = dataset_id;
    }


    public String getFile_id() {
        return file_id;
    }
    
    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    abstract public String getBestmanPath();
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
