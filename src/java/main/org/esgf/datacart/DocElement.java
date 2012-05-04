package org.esgf.datacart;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DocElement implements DataCartElement {

    private final static String testInitializationFile = "C:\\Users\\8xo\\esgf-web-fe\\docelement.xml";
    private final static String testInitializationFileFile = "C:\\Users\\8xo\\esgf-web-fe\\fileelement.xml";
    

    /** Description */
    private String datasetId;
    
    /** Description */
    private int count;
    
    /** Description */
    private String hasHttp;
    
    /** Description */
    private String hasOpenDap;

    /** Description */
    private String hasGridFTP;
    
    /** Description */
    private String hasSRM;
    
    /** Description */
    private List<FileElement> fileElements;
    
    /** Description of DocElement() constructor
     * 
     */
    public DocElement() {
        
    }
    
    
    
    /** Description of toElement() 
     * 
     */
    @Override
    public Element toElement() {
        
        Element docEl = new Element("doc");
        
        if(this.datasetId != null) {
            Element datasetIdEl = new Element("datasetId");
            datasetIdEl.addContent(this.datasetId);
            docEl.addContent(datasetIdEl);
        }
        
        Element countEl = new Element("count");
        countEl.addContent((new Integer(this.count)).toString());
        docEl.addContent(countEl);

        if(this.hasHttp != null) {
            Element hasHttpEl = new Element("hasHttp");
            hasHttpEl.addContent(this.hasHttp);
            docEl.addContent(hasHttpEl);
        }
        
        if(this.hasOpenDap != null) {
            Element hasOpenDapEl = new Element("hasOpenDap");
            hasOpenDapEl.addContent(this.hasOpenDap);
            docEl.addContent(hasOpenDapEl);
        }
        
        if(this.hasGridFTP != null) {
            Element hasGridFTPEl = new Element("hasGridFTP");
            hasGridFTPEl.addContent(this.hasGridFTP);
            docEl.addContent(hasGridFTPEl);
        }
        
        if(this.hasSRM != null) {
            Element hasGridFTPEl = new Element("hasSRM");
            hasGridFTPEl.addContent(this.hasGridFTP);
            docEl.addContent(hasGridFTPEl);
        }
        
        if(this.fileElements != null) {
            Element filesEl = new Element("files");
            
            for(int i=0;i<this.fileElements.size();i++) {
                FileElement fe = this.fileElements.get(i);
                filesEl.addContent(fe.toElement());
            }
            //filesEl.addContent(this.)
            docEl.addContent(filesEl);
        }
        
        return docEl;
    }













    /** Descriptionn of toXML()
     * 
     */
    @Override
    public String toXML() {
        String xml = "";
        
        Element docEl = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(docEl);
        
        return xml;
    }




    /** Description of toJSONObject() 
     * 
     */
    @Override
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













    /** Description of toJSON() 
     * 
     */
    @Override
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
     */
    @Override
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





    public void fromXML(String xmlStr) {
        try {
            DocumentBuilderFactory dbf =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlStr));

            Document doc = db.parse(is);
            
            doc.getDocumentElement().normalize();
            
            org.w3c.dom.Element docElement = doc.getDocumentElement();
            
            this.readHelper(docElement);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    /** Description of fromFile() 
     * 
     */
    @Override
    public void fromFile(String file) {
        
        this.datasetId = null;
        this.hasHttp = null;
        this.hasOpenDap = null;
        this.hasGridFTP = null;
        this.hasSRM = null;
        this.fileElements = null;
        
        File fXmlFile = new File(file);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            
            org.w3c.dom.Element docElement = doc.getDocumentElement();
            
            this.readHelper(docElement);
            
            
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    /** Description of readHelper()
     * 
     * @param docElement
     */
    public void readHelper(org.w3c.dom.Element docElement) {
        if(docElement.getNodeName().equals("doc")) {
            NodeList docNodeList = docElement.getChildNodes();//doc.getDocumentElement().getChildNodes();
            
            for(int i=0;i<docNodeList.getLength();i++) {
                Node topLevelDocNode = docNodeList.item(i);
                if (topLevelDocNode.getNodeType() == Node.ELEMENT_NODE) {
                
                    org.w3c.dom.Element topLevelElement = (org.w3c.dom.Element) topLevelDocNode;
                    if(topLevelElement.getTagName().equals("datasetId")) {
                        this.datasetId = topLevelElement.getTextContent();
                    } else if(topLevelElement.getTagName().equals("count")) {
                        this.count = Integer.parseInt(topLevelElement.getTextContent());
                    } else if(topLevelElement.getTagName().equals("hasGridFTP")) {
                        this.hasGridFTP = topLevelElement.getTextContent();
                    } else if(topLevelElement.getTagName().equals("hasHttp")) {
                        this.hasHttp = topLevelElement.getTextContent();
                    } else if(topLevelElement.getTagName().equals("hasOpenDap")) {
                        this.hasOpenDap = topLevelElement.getTextContent();
                    } else if(topLevelElement.getTagName().equals("hasSRM")) {
                        this.hasSRM = topLevelElement.getTextContent();
                    } else if(topLevelElement.getTagName().equals("files")) {
                        
                        NodeList fileNodeList = topLevelElement.getChildNodes();//doc.getDocumentElement().getChildNodes();
                        
                        List<FileElement> fileList = new ArrayList<FileElement>();
                        for(int j=0;j<fileNodeList.getLength();j++) {
                            
                            Node topLevelFileNode = fileNodeList.item(j);
                            if (topLevelFileNode.getNodeType() == Node.ELEMENT_NODE) {
                                org.w3c.dom.Element topLevelFileElement = (org.w3c.dom.Element) topLevelFileNode;
                                FileElement fe = new FileElement();
                                fe.readHelper(topLevelFileElement);
                                fileList.add(fe);
                            }
                            
                            this.setFileElements(fileList);  
                        }
                        
                        
                        
                    } 
                
                }
            }
        
        }
    }
    
    
    
    public void addFile(FileElement fileElement) {
        if(fileElement != null) {
            this.fileElements.add(fileElement);
        }
    }
    

    public void removeFile(FileElement fileElement) {
        if(fileElement != null) {
            this.fileElements.remove(fileElement);
        }
    }
    
    
    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getHasHttp() {
        return hasHttp;
    }

    public void setHasHttp(String hasHttp) {
        this.hasHttp = hasHttp;
    }

    public String getHasOpenDap() {
        return hasOpenDap;
    }

    public void setHasOpenDap(String hasOpenDap) {
        this.hasOpenDap = hasOpenDap;
    }

    public String getHasGridFTP() {
        return hasGridFTP;
    }

    public void setHasGridFTP(String hasGridFTP) {
        this.hasGridFTP = hasGridFTP;
    }

    public String getHasSRM() {
        return hasSRM;
    }

    public void setHasSRM(String hasSRM) {
        this.hasSRM = hasSRM;
    }

    public List<FileElement> getFileElements() {
        return fileElements;
    }

    public void setFileElements(List<FileElement> fileElements) {
        this.fileElements = fileElements;
    }




    public static void main(String [] args) {
        
        
        DocElement u = new DocElement();
        
        u.fromFile(testInitializationFile);
        
        System.out.println(new XmlFormatter().format(u.toXML()));
        
        FileElement fileElement = new FileElement();
        fileElement.fromFile(testInitializationFileFile);
        
        u.addFile(fileElement);
        
        
        u.toFile(testInitializationFile);
        
        //u = new URLSElement2();
        
        u.fromFile(testInitializationFile);

        System.out.println(new XmlFormatter().format(u.toXML()));
        /**/
        
        /* */
        
    }









    
}
