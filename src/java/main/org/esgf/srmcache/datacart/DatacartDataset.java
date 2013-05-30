package org.esgf.srmcache.datacart;

import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.esgf.solr.model.Solr;
import org.esgf.solr.model.SolrRecord;
import org.esgf.solr.model.SolrResponse;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class DatacartDataset {

    //var datasetInfo = {'numFiles' : evt.data.doc['number_of_files'], 'peer' : evt.data.doc['index_node'] , 'xlink' : evt.data.doc['xlink'], 'access' : evt.data.doc['access']};

    /*
    ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1|esg2-sdnl1.ccs.ornl.gov
    cmip5.output1.INM.inmcm4.1pctCO2.day.atmos.day.r1i1p1.v20110323|pcmdi9.llnl.gov
    cmip5.output1.INM.inmcm4.abrupt4xCO2.mon.ocnBgchem.Omon.r1i1p1.v20110323|pcmdi9.llnl.gov
    */
    public static void main(String [] args) {
        String dataset_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1|esg2-sdnl1.ccs.ornl.gov";
        
        String [] datasetIds = {dataset_id};
        
        DatacartDataset [] dDatasets = getDatacartDatasetFromSolr(datasetIds);
    }
    
    
    private String datasetId;
    private String numFiles;
    private String peer;
    private String [] xlink;
    private String [] access;
    
    public DatacartDataset(String datasetId,String numFiles,String peer,String [] xlink,String [] access) {
        this.datasetId = datasetId;
        this.numFiles = numFiles;
        this.peer = peer;
        this.xlink = xlink;
        this.access = access;
    }
    
    public DatacartDataset() {
        this.datasetId = "undefined";
        this.numFiles = "undefined";
        this.peer = "undefined";
        this.xlink = new String[1];
        this.xlink[0] = "undefined";
        this.access = new String[1];
        this.access = new String[1];
    }
    
    public String getDatasetId() {
        return datasetId;
    }
    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }
    public String getNumFiles() {
        return numFiles;
    }
    public void setNumFiles(String numFiles) {
        this.numFiles = numFiles;
    }
    public String getPeer() {
        return peer;
    }
    public void setPeer(String peer) {
        this.peer = peer;
    }
    public String [] getXlink() {
        return xlink;
    }
    public void setXlink(String [] xlink) {
        this.xlink = xlink;
    }
    public String [] getAccess() {
        return access;
    }
    public void setAccess(String [] access) {
        this.access = access;
    }
    
    public static DatacartDataset getDatacartDatasetFromSolr(String datasetId) {

        DatacartDataset dDataset = new DatacartDataset();
        
        //query solr for the files
        Solr solr = new Solr();
        
        solr.addConstraint("query", "*");
        //solr.addConstraint("distrib", "false");
        solr.addConstraint("limit", "1");
        solr.addConstraint("offset", "0");
        solr.addConstraint("type", "Dataset");
      
        solr.addConstraint("id",datasetId);
        //System.out.println("\n\nquery->" + solr.getQueryString() + "\n\n");
        solr.executeQuery();
        
        SolrResponse solrResponse = solr.getSolrResponse();
        //System.out.println(solrResponse.getSolrRecords().size());
        
        if(solrResponse.getSolrRecords().size() > 0) {

            SolrRecord record = solrResponse.getSolrRecords().get(0);
            
            //get the id
            String id = record.getStrField("id");
            
            //get the numfiles
            String numFiles = record.getMiscField("number_of_files");
            if(numFiles == null) {
                numFiles = "undefined";
                
            }
            
            //get the peer
            String peer = record.getStrField("index_node");
            if(peer == null) {
                peer = "undefined";
            }
            
            //get the xlink
            String [] xlink = null;
            if(record.getArrField("xlink") == null) {
                xlink = new String [1];
                xlink[0] = "undefined";
            } else {
                xlink = new String [record.getArrField("xlink").size()];
                for(int j=0;j<xlink.length;j++) {
                    xlink[j] = record.getArrField("xlink").get(j);
                }
            }
              
            
            //get the access
            String [] access = null;
            if(record.getArrField("access") == null) {
                access = new String [1];
                access[0] = "undefined";
            } else {
                access = new String [record.getArrField("access").size()];
                for(int j=0;j<access.length;j++) {
                    access[j] = record.getArrField("access").get(j);
                }
            }
            
            //(String datasetId,String numFiles,String peer,String [] xlink,String [] access) {
            dDataset = new DatacartDataset(id,numFiles,peer,xlink,access);

        }
        

        return dDataset;
        
    }
    
    public static DatacartDataset [] getDatacartDatasetFromSolr(String [] datasetIds) {
        
        DatacartDataset [] dDatasets = new DatacartDataset[datasetIds.length];
        
      //query solr for the files
        Solr solr = new Solr();
        
        solr.addConstraint("query", "*");
        //solr.addConstraint("distrib", "false");
        solr.addConstraint("limit", "1");
        solr.addConstraint("offset", "0");
        solr.addConstraint("type", "Dataset");
        
        //call solr for each dataset id
        for(int i=0;i<datasetIds.length;i++) {
            String dataset_id = datasetIds[i];

            solr.addConstraint("id",dataset_id);
            //System.out.println("query->" + solr.getQueryString());
            solr.executeQuery();
            
            SolrResponse solrResponse = solr.getSolrResponse();
            //System.out.println(solrResponse.getSolrRecords().size());
            
            SolrRecord record = solrResponse.getSolrRecords().get(0);
            
            //System.out.println("Record: " + i + " id: " + record.getStrField("id"));
            
            //get the id
            String id = record.getStrField("id");
            
            //get the numfiles
            String numFiles = record.getMiscField("number_of_files");
            if(numFiles == null) {
                numFiles = "undefined";
                
            }
            
            //get the peer
            String peer = record.getStrField("index_node");
            if(peer == null) {
                peer = "undefined";
            }
            
            //get the xlink
            String [] xlink = null;
            if(record.getArrField("xlink") == null) {
                xlink = new String [1];
                xlink[0] = "undefined";
            } else {
                xlink = new String [record.getArrField("xlink").size()];
                for(int j=0;j<xlink.length;j++) {
                    xlink[j] = record.getArrField("xlink").get(j);
                }
            }
              
            
            //get the access
            String [] access = null;
            if(record.getArrField("access") == null) {
                access = new String [1];
                access[0] = "undefined";
            } else {
                access = new String [record.getArrField("access").size()];
                for(int j=0;j<access.length;j++) {
                    access[j] = record.getArrField("access").get(j);
                }
            }

            solr.removeConstraint("id");
            
            //(String datasetId,String numFiles,String peer,String [] xlink,String [] access) {
            DatacartDataset dDataset = new DatacartDataset(id,numFiles,peer,xlink,access);
              
            dDatasets[i] = dDataset;
            
        }
        
        
        
        return dDatasets;
    }
    
    /**
     * datasetId
     * numFiles
     * access
     * peer
     * xlink
     * @return
     */
    
    public Element toElement() {
        
        Element datacartdatasetEl = new Element("datacartdataset");
        
        if(this.datasetId != null) {
            Element datasetIdEl = new Element("datasetId");
            datasetIdEl.addContent(this.datasetId);
            datacartdatasetEl.addContent(datasetIdEl);
        }

        if(this.numFiles != null) {
            Element numFilesEl = new Element("numFiles");
            numFilesEl.addContent(this.numFiles);
            datacartdatasetEl.addContent(numFilesEl);
        }

        if(this.access != null) {
            Element accessEl = new Element("access");
            String accessStr = "";
            for(int i=0;i<this.access.length;i++) {
                if(i == 0) {
                    accessStr += this.access[i];
                } else {
                    accessStr += ";" + this.access[i];
                }
            }
            accessEl.addContent(accessStr);
            datacartdatasetEl.addContent(accessEl);
        }

        if(this.xlink != null) {
            Element xlinkEl = new Element("xlink");
            xlinkEl.addContent("undefined");
            datacartdatasetEl.addContent(xlinkEl);
        }

        if(this.peer != null) {
            Element peerEl = new Element("peer");
            peerEl.addContent("undefined");
            datacartdatasetEl.addContent(peerEl);
        }
        
        return datacartdatasetEl;
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
    
    
    
    
}
