package org.esgf.solr.model;

import java.util.ArrayList;
import java.util.List;

import org.esgf.datacart.FileElement;
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.esgf.srm.SRMEntryList;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class DatacartDoc {

    /** Description */
    private String datasetId;
    
    /** Description */
    private String count;

    /** Description */
    private String hasSRM;
    
    private String isCached;
    
    private List<String> technotes;
    
    /** Description */
    private List<DataCartFile> datacartFiles;
    
    public List<DataCartFile> getDatacartFiles() {
        return datacartFiles;
    }

    public void setDatacartFiles(List<DataCartFile> datacartFiles) {
        this.datacartFiles = datacartFiles;
    }

    public DatacartDoc() {
        
    }
    
    public DatacartDoc(SolrResponse solrResponse) {
        
        //get the count
        this.setCount(solrResponse.getCount());
        this.datacartFiles = new ArrayList<DataCartFile>();
        this.setIsCached("true");
        for(int i=0;i<solrResponse.getSolrRecords().size();i++) {
            SolrRecord solrRecord = solrResponse.getSolrRecords().get(i);
            
            DataCartFile datacartFile = new DataCartFile(solrRecord);
            this.datacartFiles.add(datacartFile);
            if(datacartFile.getIsCached().equals("false")) {
                this.setIsCached("false");
            }
        }
        
        
    }
    
    public Element toElement() {
        
        Element docEl = new Element("doc");
        
        if(this.datasetId != null) {
            Element datasetIdEl = new Element("datasetId");
            datasetIdEl.addContent(this.datasetId);
            docEl.addContent(datasetIdEl);
        }
        
        if(this.isCached != null) {
            Element isCachedEl = new Element("isCached");
            isCachedEl.addContent(this.isCached);
            docEl.addContent(isCachedEl);
        }
        

        if(this.datacartFiles != null) {
            Element filesEl = new Element("files");
            for(int i=0;i<this.datacartFiles.size();i++) {
                Element datacartFileEl = this.datacartFiles.get(i).toElement();
                filesEl.addContent(datacartFileEl);
            }
            docEl.addContent(filesEl);
        }
        
        if(this.technotes != null) {
            Element technotesEl = new Element("technotes");
            
            docEl.addContent(technotesEl);
        }
        
        return docEl;
    }
    
    public String toXML() {
        String xml = "";
        
        Element docEl = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(docEl);
        
        return xml;
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
    
    

    /**
     * @return the datasetId
     */
    public String getDatasetId() {
        return datasetId;
    }

    /**
     * @param datasetId the datasetId to set
     */
    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    /**
     * @return the count
     */
    public String getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(String count) {
        this.count = count;
    }

    /**
     * @return the hasSRM
     */
    public String getHasSRM() {
        return hasSRM;
    }

    /**
     * @param hasSRM the hasSRM to set
     */
    public void setHasSRM(String hasSRM) {
        this.hasSRM = hasSRM;
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
     * @return the technotes
     */
    public List<String> getTechnotes() {
        return technotes;
    }

    /**
     * @param technotes the technotes to set
     */
    public void setTechnotes(List<String> technotes) {
        this.technotes = technotes;
    }
    
}
