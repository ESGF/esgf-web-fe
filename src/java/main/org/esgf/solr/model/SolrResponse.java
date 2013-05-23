package org.esgf.solr.model;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SolrResponse {

    private String response;
    private List<SolrRecord> solrRecords;
    private String count;
    
    public SolrResponse() {
        this.setResponse(null);
        this.solrRecords = new ArrayList<SolrRecord>();
        this.setCount(null);
    }
    
    public SolrResponse(String response) {
        this.solrRecords = new ArrayList<SolrRecord>();
        this.setCount(null);
        
        this.setResponse(response);
        
        this.fromXML(response);
    }
    
    public static void main(String [] args) {
        Solr solr = new Solr();
        
        solr.addConstraint("query", "*");
        solr.addConstraint("distrib", "false");
        solr.addConstraint("limit", "8");
        solr.addConstraint("type", "File");
        solr.addConstraint("dataset_id","ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1|esg2-sdnl1.ccs.ornl.gov");
        //solr.addConstraint("project", "CMIP5");
        
        solr.executeQuery();
        
        SolrResponse solrResponse = solr.getSolrResponse();
        
        List<SolrRecord> solrRecords = solrResponse.getSolrRecords();
        
        
        
        DataCartFile datacartFile = new DataCartFile(solrRecords.get(0));
        
        
        
    }

    
   
    
    
    public List<String> getDatasetIds(String core) {
        List<String> datasetIds = new ArrayList<String>();
        
        if(core.equals("File")) {
            for(int i=0;i<this.solrRecords.size();i++) {
                SolrRecord solrRecord = this.solrRecords.get(i);
                String dataset_id = "";
                
                //first find the id of the solr record
                for(int j=0;j<solrRecord.getStrNodes().size();j++) {
                    Node facetNode = solrRecord.getStrNodes().get(j);
                    
                    if(facetNode.getAttributes().item(0).getNodeValue().equals("dataset_id")) {
                        dataset_id = facetNode.getTextContent(); 
                    }
                }
                
                for(int j=0;j<solrRecord.getArrNodes().size();j++) {
                    Node facetNode = solrRecord.getArrNodes().get(j);
                    if(facetNode.getAttributes().item(0).getNodeValue().equals("url")) {
                        String content = facetNode.getTextContent(); 
                        if(content.contains("SRM")){
                            datasetIds.add(dataset_id);
                        }
                    }
                    
                }
            }
        } else {
            for(int i=0;i<this.solrRecords.size();i++) {
                //System.out.println(this.solrRecords.get(i).getStrField("id"));
            }
            //System.out.println("here");
        }
        /*
        else {
            
            for(int i=0;i<this.solrRecords.size();i++) {
                SolrRecord solrRecord = this.solrRecords.get(i);
                String id = "";
                
                //first find the id of the solr record
                for(int j=0;j<solrRecord.getStrNodes().size();j++) {
                    Node facetNode = solrRecord.getStrNodes().get(j);
                    
                    if(facetNode.getAttributes().item(0).getNodeValue().equals("id")) {
                        id = facetNode.getTextContent(); 
                    }
                }
                
                for(int j=0;j<solrRecord.getArrNodes().size();j++) {
                    Node facetNode = solrRecord.getArrNodes().get(j);
                    if(facetNode.getAttributes().item(0).getNodeValue().equals("access")) {
                        String content = facetNode.getTextContent(); 
                        //System.out.println(content);
                        if(content.contains("SRM")){
                            needsSRM.add(id);
                        }
                    }
                    
                }
            }
            
            
        }
        */
        
        return datasetIds;
    }
    
    
 
    //gets dataset ids from solr that have srm access
    public List<String> needsSRM() {
        List<String> needsSRM = new ArrayList<String>();
        
        System.out.println("Number of solr records: " + this.solrRecords.size());
        for(int i=0;i<this.solrRecords.size();i++) {
            boolean isSRM = false;
            List<String> access = this.solrRecords.get(i).getArrField("access");
            for(int j=0;j<access.size();j++) {
                if(access.get(j).equals("SRM")) {
                    isSRM = true;
                }
            }
            if(isSRM) {
                needsSRM.add(this.solrRecords.get(i).getStrField("id"));
            }
        }
        
        return needsSRM;
    }
    
    //gets file ids
    public List<String> needsSRM(String core) {
        List<String> needsSRM = new ArrayList<String>();
        
        if(core.equals("File")) {
            for(int i=0;i<this.solrRecords.size();i++) {
                SolrRecord solrRecord = this.solrRecords.get(i);
                String id = "";
                
                //first find the id of the solr record
                for(int j=0;j<solrRecord.getStrNodes().size();j++) {
                    Node facetNode = solrRecord.getStrNodes().get(j);
                    
                    if(facetNode.getAttributes().item(0).getNodeValue().equals("id")) {
                        id = facetNode.getTextContent(); 
                    }
                }
                
                
                for(int j=0;j<solrRecord.getArrNodes().size();j++) {
                    Node facetNode = solrRecord.getArrNodes().get(j);
                    if(facetNode.getAttributes().item(0).getNodeValue().equals("url")) {
                        String content = facetNode.getTextContent(); 
                        if(content.contains("SRM")){
                            needsSRM.add(id);
                        }
                    }
                    
                }
            }
        } else {
            
            for(int i=0;i<this.solrRecords.size();i++) {
                SolrRecord solrRecord = this.solrRecords.get(i);
                String id = "";
                
                //first find the id of the solr record
                for(int j=0;j<solrRecord.getStrNodes().size();j++) {
                    Node facetNode = solrRecord.getStrNodes().get(j);
                    
                    if(facetNode.getAttributes().item(0).getNodeValue().equals("id")) {
                        id = facetNode.getTextContent(); 
                    }
                }
                
                for(int j=0;j<solrRecord.getArrNodes().size();j++) {
                    Node facetNode = solrRecord.getArrNodes().get(j);
                    if(facetNode.getAttributes().item(0).getNodeValue().equals("access")) {
                        String content = facetNode.getTextContent(); 
                        if(content.contains("SRM")){
                            needsSRM.add(id);
                        }
                    }
                    
                }
            }
            
            
        }
        
        return needsSRM;
    }
    
    public void fromXML(String xml) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            
            doc.getDocumentElement().normalize();
            
            org.w3c.dom.Element docElement = doc.getDocumentElement();

            this.readHelper(docElement);
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /** Description of readHelper
     * 
     * @param docElement
     */
    public void readHelper(org.w3c.dom.Element docElement) {
        
        if(docElement.getNodeName().equals("response")) {
            
            NodeList fileNodeList = docElement.getChildNodes();
            for(int i=0;i<fileNodeList.getLength();i++) {
                Node topLevelFileNode = fileNodeList.item(i);
                if (topLevelFileNode.getNodeType() == Node.ELEMENT_NODE) {
                    
                    //only <result> matters for now...
                    if(topLevelFileNode.getNodeName().equals("result")){
                        
                        for(int j=0;j<topLevelFileNode.getAttributes().getLength();j++) {
                            String attr = topLevelFileNode.getAttributes().item(j).getNodeName();
                            if(attr.equals("numFound")) {
                                this.count = topLevelFileNode.getAttributes().item(j).getNodeValue();
                            }
                        }
                        
                        org.w3c.dom.Element resultElement = (org.w3c.dom.Element) topLevelFileNode;
                        for(int j=0;j<resultElement.getChildNodes().getLength();j++) {
                            Node docNode = resultElement.getChildNodes().item(j);
                            //doc level
                            if (docNode.getNodeType() == Node.ELEMENT_NODE) {
                                SolrRecord record = new SolrRecord();
                                List<Node> arrNodes = new ArrayList<Node>();
                                List<Node> strNodes = new ArrayList<Node>();
                                List<Node> miscNodes = new ArrayList<Node>();
                                NodeList docFacets = docNode.getChildNodes();
                                for(int k=0;k<docFacets.getLength();k++) {
                                    Node facetNode = docFacets.item(k);
                                    if (facetNode.getNodeType() == Node.ELEMENT_NODE) {
                                        if(facetNode.getNodeName().equals("arr")) {// && facetNode.getAttributes().getNamedItem("name").equals("access")) {
                                            arrNodes.add(facetNode);
                                        } else if(facetNode.getNodeName().equals("str")) {
                                            strNodes.add(facetNode);
                                        } else {
                                            miscNodes.add(facetNode);
                                        }
                                    }
                                }
                                record.setArrNodes(arrNodes);
                                record.setStrNodes(strNodes);
                                record.setMiscNodes(miscNodes);
                                
                                this.solrRecords.add(record);
                            }
                            
                        }
                    }
                    
                }
            }
        }
            
    }

    /**
     * @return the response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }
    

    public List<SolrRecord> getSolrRecords() {
        return solrRecords;
    }

    public void setSolrRecords(List<SolrRecord> solrRecords) {
        this.solrRecords = solrRecords;
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

    
}
