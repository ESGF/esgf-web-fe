package org.esgf.filedownload2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.esgf.filedownload.XmlFormatter;
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FileElement2 {

    /** Urls given by the results of the search api */
    private String fileId;
    
    private String title;
    private String size;
    

    private final static String testInitializationFile = "C:\\Users\\8xo\\esgf-web-fe\\fileelement.xml";
    
    /**
     * 
     */
    public FileElement2() {
        
    }

    
    
    
    /** Description of toElement()
     * 
     * @return serialized XML element equivalent of the class
     */
    public Element toElement() {
        Element fileEl = new Element("file");
        
        if(this.fileId != null) {
            Element fileIdEl = new Element("fileId");
            fileIdEl.addContent(this.fileId);
            fileEl.addContent(fileIdEl);
        }
        
        if(this.size != null) {
            Element sizeEl = new Element("size");
            sizeEl.addContent(this.size);
            fileEl.addContent(sizeEl);
        }
        
        if(this.title != null) {
            Element titleEl = new Element("title");
            titleEl.addContent(this.title);
            fileEl.addContent(titleEl);
        }
        
        return fileEl;
    }
    

    /** Description of toXML()
     * 
     * @return
     */
    public String toXML() {
        String xml = "";
        
        Element fileEl = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(fileEl);
        
        return xml;
    }

    /** Description of toJSONObject()
     * 
     * @return
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
    
    
    
    /** Description of toFile()
     * 
     * @param file Filename of the output
     */
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
    
    /**Description of fromFile()
     * 
     * @param file
     */
    public void fromFile(String file) {
        
        this.fileId = null;
        this.size = null;
        this.title = null;
        
        File fXmlFile = new File(file);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            
            
            if(doc.getDocumentElement().getNodeName().equals("file")) {
                
                NodeList fileNodeList = doc.getDocumentElement().getChildNodes();
                
                //for (int temp = 0; temp < nList.getLength(); temp++) {
                for(int i=0;i<fileNodeList.getLength();i++) {
                    Node topLevelFileNode = fileNodeList.item(i);
                    if (topLevelFileNode.getNodeType() == Node.ELEMENT_NODE) {
                        org.w3c.dom.Element topLevelElement = (org.w3c.dom.Element) topLevelFileNode;
                        if(topLevelElement.getTagName().equals("fileId")) {
                            this.fileId = topLevelElement.getTextContent();
                        } else if(topLevelElement.getTagName().equals("size")) {
                            this.size = topLevelElement.getTextContent();
                        } else if(topLevelElement.getTagName().equals("title")) {
                            this.title = topLevelElement.getTextContent();
                        }
                    }
                }
            }
            
        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        //overwrite whatever was in the data structure
        this.urls = null;
        
        
        File fXmlFile = new File(file);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            
            doc.getDocumentElement().normalize();
            
            
            if(doc.getDocumentElement().getNodeName().equals("urls")) {
              //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                NodeList nList = doc.getElementsByTagName("url");
         
                for (int temp = 0; temp < nList.getLength(); temp++) {
         
                    
                   Node nNode = nList.item(temp);
                   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
         
                      org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
         
                      //System.out.println(eElement.getTextContent());
                      String url = eElement.getTextContent();
                      if(this.urls == null) {
                          this.urls = new ArrayList<String>();
                      }
                      this.urls.add(url);
                      
                   }
                }
            }
            
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
    
    
    
    
    
    public String getFileId() {
        return fileId;
    }




    public void setFileId(String fileId) {
        this.fileId = fileId;
    }




    public String getTitle() {
        return title;
    }




    public void setTitle(String title) {
        this.title = title;
    }




    public String getSize() {
        return size;
    }




    public void setSize(String size) {
        this.size = size;
    }

    
    
    public static void main(String [] args) {
        
        
        FileElement2 u = new FileElement2();
        
        u.fromFile(testInitializationFile);
        
        System.out.println(new XmlFormatter().format(u.toXML()));
        
        u.toFile(testInitializationFile);
        
        //u = new URLSElement2();
        
        u.fromFile(testInitializationFile);

        System.out.println(u.toXML());
        /* */
        
    }
    
    
}
