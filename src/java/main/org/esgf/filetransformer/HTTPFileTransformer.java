package org.esgf.filetransformer;

//for http filename
public class HTTPFileTransformer extends FileTransformer {

    public HTTPFileTransformer(String fileTransformerName) {
        super(fileTransformerName);
    }
    
    public HTTPFileTransformer(String fileTransformerName,String file_id) {
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
