package org.esgf.srm.cache;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.esgf.datacart.XmlFormatter;
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.esgf.solr.model.Solr;
import org.esgf.solr.model.SolrResponse;
import org.esgf.srm.SRMControls;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SRMEntryList {

    private List<SRMEntry> srm_entry_list;
    
    private static int limit = 80;
    
    public static void main(String [] args) {
        
        /*
        SRMEntry srm_entry = new SRMEntry();
        srm_entry.setFile_id("file_id21");
        srm_entry.setDataset_id("dataset_id1");
        srm_entry.setTimeStamp("timeStamp1");
        srm_entryDB.addEntry(srm_entry);
        */
         
        SRMEntryList srm_entry_list = new SRMEntryList();
        
        srm_entry_list.fromDB();
        
        System.out.println(new XmlFormatter().format(srm_entry_list.toXML()));
       
    }
    
    public SRMEntry getSRMEntryForFileId(String file_id,String dataset_id) {
        
        SRMEntry srm_entry = new SRMEntry();
        
        for(int i=0;i<srm_entry_list.size();i++) {
            if(srm_entry_list.get(i).getFile_id().equals(file_id)) {
                if(srm_entry_list.get(i).getDataset_id().equals(dataset_id)) {
                    srm_entry = srm_entry_list.get(i);
                    
                }
            }
        }
        
        return srm_entry;
        
    }
    
    public SRMEntry getSRMEntryForFileId(String file_id) {
        
        SRMEntry srm_entry = new SRMEntry();
        
        for(int i=0;i<srm_entry_list.size();i++) {
            if(srm_entry_list.get(i).getFile_id().equals(file_id)) {
                srm_entry = srm_entry_list.get(i);
            }
        }
        
        return srm_entry;
        
    }
    
    public List<SRMEntry> getSRMEntriesForDatasetId(String dataset_id) {
        
        List<SRMEntry> srm_entries = new ArrayList<SRMEntry>();
        
        for(int i=0;i<srm_entry_list.size();i++) {
            if(srm_entry_list.get(i).getDataset_id().equals(dataset_id)) {
                srm_entries.add(srm_entry_list.get(i));
            }
        }
        
        return srm_entries;
        
    }
    
    public void fromDB() {
        
        Connection con = null;
        try {
            System.out.println("connection");
            con = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/" + SRMControls.db_name, SRMControls.valid_user,
                    SRMControls.valid_password);
            
            Statement st = con.createStatement();
            
            String selectCommand = "select * from " + SRMControls.table_name + ";";
            
            ResultSet rs = st.executeQuery(selectCommand);
            
            if(rs != null) {
                while(rs.next()) {
                    SRMEntry srm_entry = new SRMEntry();
                    String file_idVal = (String)rs.getObject("file_id");
                    String dataset_idVal = (String)rs.getObject("dataset_id");
                    String timeStampVal = (String)rs.getObject("timeStamp");
                    
                   
                    srm_entry.setFile_id(file_idVal);
                    srm_entry.setDataset_id(dataset_idVal);
                    srm_entry.setTimeStamp(timeStampVal);
                    this.srm_entry_list.add(srm_entry);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
        }
        
    }
    
    public void toDB() {
        
    }
    
    
    public SRMEntryList() {
        this.setSrm_entry_list(new ArrayList<SRMEntry>());
    }
    
    public SRMEntryList(SRMEntryList sList) {
        this.setSrm_entry_list(srm_entry_list);
    }
    
    /*
    //note that this is a complete initialization!
    //it will wipe out the contents of any persistent srm entry list
    public void fromSolr(String core) {
        
        SRMEntryList srm_entry_list = new SRMEntryList();
        
        Solr solr = new Solr();
        limit = 80;
        solr.addConstraint("query", "*");
        solr.addConstraint("distrib", "false");
        solr.addConstraint("limit", Integer.toString(limit));
        
        solr.addConstraint("type", core);

        List<String> srms = new ArrayList<String>();
        List<String> dataset_ids = new ArrayList<String>();
        
        
        boolean iterate = true;
        int counter = 0;
        
        
        while(iterate) {
            String offset = Integer.toString(counter*limit);
            solr.addConstraint("offset", Integer.toString(counter*limit));
            solr.executeQuery();
            
            SolrResponse solrResponse = solr.getSolrResponse();
            
            String countStr = solrResponse.getCount();
            int count = Integer.parseInt(countStr);

            List<String> srmDatasets = solrResponse.needsSRM(core);
            srms.addAll(srmDatasets);
            
            List<String> dataset_ids_sublist = solrResponse.getDatasetIds(core);
            dataset_ids.addAll(dataset_ids_sublist);
            solr.removeConstraint("offset");
            
            if(count < (counter+1)*limit) {
                iterate = false;
            }
            counter++;
            
            
        }
        
        
        for(int i=0;i<srms.size();i++) {
            
            SRMEntry srm_entry = new SRMEntry(srms.get(i),"false","N/A",dataset_ids.get(i));
            srm_entry_list.addSRMEntry(srm_entry);
        }
        
        //System.out.println(dataset_ids);
        
        
        System.out.println("/esg/config/srm_entry_list_" + core + ".xml");
        srm_entry_list.toFile("/esg/config/srm_entry_list_" + core + ".xml");
        
        this.setSrm_entry_list(srm_entry_list.getSrm_entry_list());
        
    }
    */
    
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
    
    
    public void fromFile(String file) {
        
        
        File fXmlFile = new File(file);
        
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            
            org.w3c.dom.Element docElement = doc.getDocumentElement();
            
            this.readHelper(docElement);
        } catch(Exception e) {
            e.printStackTrace();
        }
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
    
    
    public void readHelper(org.w3c.dom.Element docElement) {
        
        List<SRMEntry> srm_entry_list = new ArrayList<SRMEntry>();
        if(docElement.getNodeName().equals("srm_entry_list")) {
            NodeList fileNodeList = docElement.getChildNodes();
            for(int i=0;i<fileNodeList.getLength();i++) {
                Node topLevelFileNode = fileNodeList.item(i);
                if (topLevelFileNode.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element topLevelElement = (org.w3c.dom.Element) topLevelFileNode;
                    SRMEntry srm_entry = new SRMEntry();
                    srm_entry.fromElement(topLevelElement);
                    srm_entry_list.add(srm_entry);
                }
            }
            
           
        }
     
        this.setSrm_entry_list(srm_entry_list);
    }

    
    public Element toElement() {

        Element srm_entry_listEl = new Element("srm_entry_list");
        
        if(this.srm_entry_list != null) {
            for(int i=0;i<srm_entry_list.size();i++) {
                Element srm_entryEl = srm_entry_list.get(i).toElement();
                srm_entry_listEl.addContent(srm_entryEl);
            }
        }

        
        return srm_entry_listEl;
    }
    
    public String toXML() {
        String xml = "";
        
        Element fileEl = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(fileEl);
        
        return xml;
    }
    
    public void toFile(String file) {
        
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(new XmlFormatter().format(this.toXML()));
            out.close();
        } 
        catch (IOException e) { 
            e.printStackTrace();
            System.out.println("Exception ");

        }
        
    }
    
    public void changeCached(String id,String cacheValue) {
        
        System.out.println("\n\nCHANGE CACHED\n\n");
        for(int i=0;i<this.srm_entry_list.size();i++) {
            
            //System.out.println("\tid: " + this.srm_entry_list.get(i).getFile_id() + "\n\tid: " + id + "\n");
            if(this.srm_entry_list.get(i).getFile_id().equals(id)) {
                //System.out.println("Match\n");
                this.srm_entry_list.get(i).setIsCached(cacheValue);
                
            }
            
        }
        /*
        List<SRMEntry> srm_entries = new ArrayList<SRMEntry>();
        for(int i=0;i<this.srm_entry_list.size();i++) {
            
            
            
            if(this.srm_entry_list.get(i).getFile_id().equals(id)) {
                System.out.println("DO I ever get here?");
                this.srm_entry_list.get(i).setIsCached(cacheValue);
                srm_entries.add(this.srm_entry_list.get(i));
                
                System.out.println(this.srm_entry_list.get(i));
            }
        }
        this.setSrm_entry_list(srm_entries);
        */
    }
    
    public String isCached(String file_id) {
        String isCached = "false";
        for(int i=0;i<this.srm_entry_list.size();i++) {
            //if(file_id.equals("ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1.t341f02.FAMIPr.cam2.h0.1978-09.nc|esg2-sdnl1.ccs.ornl.gov"))
            //    System.out.println("list: " + this.srm_entry_list.get(i).getFile_id() + " " + file_id);
            if(file_id.equals(this.srm_entry_list.get(i).getFile_id())) {
                isCached = this.srm_entry_list.get(i).getIsCached();
            }
        }
        return isCached;
    }
    
    public void removeSRMEntry(String file_id) {
        for(int i=0;i<this.srm_entry_list.size();i++) {
            if(file_id.equals(this.srm_entry_list.get(i).getFile_id())) {
                this.srm_entry_list.remove(i);
            }
        }
    }
    
    
    public void addSRMEntry(SRMEntry srm_entry) {
        boolean isDuplicate = false;
        String srm_entry_id = srm_entry.getFile_id();
        for(int i=0;i<this.srm_entry_list.size();i++) {
            if(srm_entry_list.get(i).getFile_id().equals(srm_entry_id)) {
                isDuplicate = true;
            }
        }
        if(!isDuplicate) 
            this.srm_entry_list.add(srm_entry);
    }

    /**
     * @return the srm_entry_list
     */
    public List<SRMEntry> getSrm_entry_list() {
        return srm_entry_list;
    }

    /**
     * @param srm_entry_list the srm_entry_list to set
     */
    public void setSrm_entry_list(List<SRMEntry> srm_entry_list) {
        this.srm_entry_list = srm_entry_list;
    }
    
    
    
}
