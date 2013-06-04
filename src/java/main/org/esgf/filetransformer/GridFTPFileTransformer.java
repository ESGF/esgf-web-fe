package org.esgf.filetransformer;

import org.esgf.srmcache.SRMCacheStore;

public class GridFTPFileTransformer extends FileTransformer {


    public GridFTPFileTransformer(String fileTransformerName,SRMCacheStore srm_cache) {
        super(fileTransformerName,srm_cache);
    }
    
    public GridFTPFileTransformer(String fileTransformerName,SRMCacheStore srm_cache,String file_id) {
        super(fileTransformerName,srm_cache,file_id);
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

    @Override
    public SRMCacheStore getSRMCacheStore() {
        // TODO Auto-generated method stub
        return null;
    }

}
