package org.esgf.srmcache;

import java.util.List;

import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.esgf.srm.SRMControls;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class SRMEntry {

    private String file_id;
    private String dataset_id;
    private String isCached;
    private String timeStamp;
    private String openid;
    private String expiration;
  

    public SRMEntry(String file_id,String dataset_id,String isCached,String timeStamp,String expiration,String openid) {
        this.file_id = file_id;
        this.dataset_id = dataset_id;
        this.isCached = isCached;
        this.timeStamp = timeStamp;
        this.expiration = expiration;
        this.openid = openid;
    }
    
    public SRMEntry(String file_id,String dataset_id,String isCached,String openid) {
        this.file_id = file_id;
        this.dataset_id = dataset_id;
        this.isCached = isCached;
        this.openid = openid;
        
        Long timeStampLong = System.currentTimeMillis();
        this.timeStamp = timeStampLong.toString();
        
        this.expiration = this.timeStamp + SRMControls.expiration;
                
    }
    
    public JSONObject toJSONObject() {
        JSONObject json = null;
        
        try {
            json = XML.toJSONObject(this.toXML());
        } catch (JSONException e) {
            System.out.println("Problem in toJSONObject");
            e.printStackTrace();
        }
        
        return json;
    }
    
    public String toJSON() {
        String json = null;
        
        try {
            json = this.toJSONObject().toString(3);
        } catch (JSONException e) {
            System.out.println("Problem in toJSON");
            e.printStackTrace();
        }
        
        return json;
    }
    
    
    
    public Element toElement() {

        Element srm_entryEl = new Element("srm_entry");
        
        if(this.file_id != null) {
            Element file_idEl = new Element("file_id");
            file_idEl.addContent(this.file_id);
            srm_entryEl.addContent(file_idEl);
        }
        
        if(this.dataset_id != null) {
            Element dataset_idEl = new Element("dataset_id");
            dataset_idEl.addContent(this.dataset_id);
            srm_entryEl.addContent(dataset_idEl);
        } else {
            Element dataset_idEl = new Element("dataset_id");
            dataset_idEl.addContent("N/A");
            srm_entryEl.addContent(dataset_idEl);
        }
        
        if(this.isCached != null) {
            Element isCachedEl = new Element("isCached");
            isCachedEl.addContent(this.isCached);
            srm_entryEl.addContent(isCachedEl);
        }

        if(this.timeStamp != null) {
            Element timeStampEl = new Element("timeStamp");
            timeStampEl.addContent(this.timeStamp);
            srm_entryEl.addContent(timeStampEl);
        }
        
        if(this.expiration != null) {
            Element expirationEl = new Element("expiration");
            expirationEl.addContent(this.expiration);
            srm_entryEl.addContent(expirationEl);
        }
        
        if(this.openid != null) {
            Element openidEl = new Element("openid");
            openidEl.addContent(this.openid);
            srm_entryEl.addContent(openidEl);
        }
        
        return srm_entryEl;
    }
    
    
    public String toXML() {
        String xml = "";
        
        Element fileEl = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(fileEl);
        
        return xml;
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

    /**
     * @return the openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid the openid to set
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    
    
    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
    
    
    
    
}
