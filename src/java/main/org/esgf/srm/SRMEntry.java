package org.esgf.srm;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.esgf.datacart.XmlFormatter;
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
    
    public static void main(String [] args) {
        String fileName = "srmentry.xml";
        
        SRMEntry srmentry = new SRMEntry("file_id1","isCached1","timeStamp1");

        srmentry.setFile_id("file_id2");
        
        srmentry.toFile(fileName);
        
        SRMEntry srmentry2 = new SRMEntry();
        
        srmentry2.fromFile(fileName);
        
        System.out.println(srmentry2.toXML());
        
    }
    
}
