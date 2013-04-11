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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.esgf.datacart.XmlFormatter;
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.esgf.srm.SRMControls;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SRMEntry {

    private String file_id;
    private String isCached;
    private String timeStamp;
    private String dataset_id;
    
    public String getDataset_id() {
        return dataset_id;
    }

    public void setDataset_id(String dataset_id) {
        this.dataset_id = dataset_id;
    }
    
    public static void main(String [] args) {
        
        SRMEntry entry = new SRMEntry();
        
        SRMEntry srmentry = new SRMEntry("file_id121","isCached1","timeStamp22221","dataset_id1");

        srmentry.toDB();
        
        entry.fromDB("file_id21", "dataset_id331");
        
        System.out.println(entry.toXML());
        
        
    }

    public SRMEntry() {
        this.setFile_id("");
        this.setIsCached("");
        this.setTimeStamp("");
        this.dataset_id = null;
    }

    public SRMEntry(String file_id, String isCached, String timeStamp) {
        this.setFile_id(file_id);
        this.setIsCached(isCached);
        this.setTimeStamp(timeStamp);
        this.dataset_id = null;
    }

    public SRMEntry(String file_id, String isCached, String timeStamp,String dataset_id) {
        this.setFile_id(file_id);
        this.setIsCached(isCached);
        this.setTimeStamp(timeStamp);
        this.dataset_id = dataset_id;
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
        
        if(this.dataset_id != null) {
            Element dataset_idEl = new Element("dataset_id");
            dataset_idEl.addContent(this.dataset_id);
            srm_entryEl.addContent(dataset_idEl);
        } else {
            Element dataset_idEl = new Element("dataset_id");
            dataset_idEl.addContent("N/A");
            srm_entryEl.addContent(dataset_idEl);
        }
        
        return srm_entryEl;
    }
    
    //commit to DB
    public void toDB() {

        Connection con = null;
        try {
            con = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/" + SRMControls.db_name, SRMControls.valid_user,
                    SRMControls.valid_password);
            
            Statement st = null;
            ResultSet rs = null;
            
            String updateCommand = "update " + SRMControls.table_name + 
                                   " set " + 
                                   "timeStamp='" + this.getTimeStamp() + "' " +
                                   " where " + 
                                   "file_id='" + this.getFile_id() + "' and " +
                                   "dataset_id='" + this.getDataset_id() + "';";
            
            String insertCommand = "insert into " + SRMControls.table_name + " (file_id,dataset_id,timeStamp) values (" +
                    "'" + this.file_id + "'," +
                    "'" + this.dataset_id + "'," +
                    "'" + this.timeStamp + "');";

            
            try {


                st = con.createStatement();
                int update = st.executeUpdate(updateCommand);
                System.out.println("Update: " + update);
                //if the update is not successful (0) then the tuple didn't exist - add it here
                if(update == 0) {
                    System.out.println("Executing: " + insertCommand);
                    st.executeUpdate(insertCommand);
                    
                } 
                
                st.close();
            } catch(SQLException e) {
                e.printStackTrace();
                
            }

            con.close();
            
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
            
        }
    }
    
    //get from DB
    public void fromDB(String file_id,String dataset_id) {
        
        Connection con = null;
        try {
            con = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/" + SRMControls.db_name, SRMControls.valid_user,
                    SRMControls.valid_password);
            
            Statement st = con.createStatement();
            
            String selectCommand = "select * from " + SRMControls.table_name + " " +
                                   "where file_id='" + file_id + "' and " +
                                         "dataset_id='" + dataset_id + "';";
            
            
            ResultSet rs = st.executeQuery(selectCommand);
            
            if(rs != null) {
                while(rs.next()) {
                    SRMEntry srm_entry = new SRMEntry();
                    String file_idVal = (String)rs.getObject("file_id");
                    String dataset_idVal = (String)rs.getObject("dataset_id");
                    String timeStampVal = (String)rs.getObject("timeStamp");
                    
                    this.dataset_id = dataset_idVal;
                    this.file_id = file_idVal;
                    this.timeStamp = timeStampVal;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
        }
        
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

    public void fromElement(org.w3c.dom.Element docElement) {
        this.readHelper(docElement);
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
    
        if(docElement.getNodeName().equals("srm_entry")) {
            NodeList fileNodeList = docElement.getChildNodes();
            
            for(int i=0;i<fileNodeList.getLength();i++) {
                Node topLevelFileNode = fileNodeList.item(i);
                if (topLevelFileNode.getNodeType() == Node.ELEMENT_NODE) {
                    
                    org.w3c.dom.Element topLevelElement = (org.w3c.dom.Element) topLevelFileNode;
                    if(topLevelElement.getTagName().equals("file_id")) {
                        String file_id = topLevelElement.getTextContent();
                        this.file_id = file_id;
                    }
                    if(topLevelElement.getTagName().equals("isCached")) {
                        String isCached = topLevelElement.getTextContent();
                        this.isCached = isCached;
                    }
                    if(topLevelElement.getTagName().equals("timeStamp")) {
                        String timeStamp = topLevelElement.getTextContent();
                        this.timeStamp = timeStamp;
                    }
                    if(topLevelElement.getTagName().equals("dataset_id")) {
                        String dataset_id = topLevelElement.getTextContent();
                        this.dataset_id = dataset_id;
                    }
                    
                }
            }
        }
        
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
    
    
    
}
