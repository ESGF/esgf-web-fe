package org.esgf.filetransformer;

public abstract class FileTransformer {

    private String name;
    private String file_url;
    protected String dataset_id;
    protected String file_id;
    
    public FileTransformer(String name) {
        this.name = name;
    }
    
    public FileTransformer(String name,String file_url) {
        this.name = name;
        this.file_url = file_url;
    }

    public FileTransformer(String name,String file_url,String dataset_id,String file_id) {
        this.name = name;
        this.file_url = file_url;
        this.dataset_id = dataset_id;
        this.file_id = file_id;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFileUrl() {
        return file_url;
    }
    public void setFileUrl(String file_url) {
        this.file_url = file_url;
    }
    
    abstract public String getHttp();
    abstract public String getGridFTP();
    abstract public String getSRM();
    abstract public String getFileName();
    
    
    public static void main(String [] args) {
        FileTransformerFactory factory = new FileTransformerFactory();
        
        String file_url = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-10.nc";
        
        FileTransformer filetrans = factory.makeFileTransformer("SRM",file_url);
        
        System.out.println(filetrans.getHttp());
        
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
}
