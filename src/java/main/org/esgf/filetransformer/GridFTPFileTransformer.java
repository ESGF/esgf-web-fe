package org.esgf.filetransformer;

public class GridFTPFileTransformer extends FileTransformer {


    public GridFTPFileTransformer(String fileTransformerName) {
        super(fileTransformerName);
    }
    
    public GridFTPFileTransformer(String fileTransformerName,String file_id) {
        super(fileTransformerName,file_id);
    }

    @Override
    public String getHttp() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getGridFTP() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSRM() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFileName() {
        // TODO Auto-generated method stub
        return null;
    }

}
