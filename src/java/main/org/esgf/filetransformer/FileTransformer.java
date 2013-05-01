package org.esgf.filetransformer;

public abstract class FileTransformer {

    private String name;
    private String file_id;
    
    public FileTransformer(String name) {
        this.name = name;
    }
    
    public FileTransformer(String name,String file_id) {
        this.name = name;
        this.file_id = file_id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFileId() {
        return file_id;
    }
    public void setFileId(String file_id) {
        this.file_id = file_id;
    }
    
    abstract public String getHttp();
    abstract public String getGridFTP();
    abstract public String getSRM();
    abstract public String getFileName();
    
    
    public static void main(String [] args) {
        FileTransformerFactory factory = new FileTransformerFactory();
        
        String file_id = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-10.nc";
        
        FileTransformer filetrans = factory.makeFileTransformer("SRM",file_id);
        
        System.out.println(filetrans.getHttp());
        
    }
}
