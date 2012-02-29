package org.esgf.filedownload2;

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

import org.esgf.filedownload.XmlFormatter;
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

public class DataCartDocs2 implements DataCartElement2 {
    
    private final static String testInitializationFile = "C:\\Users\\8xo\\esgf-web-fe\\docselement.xml";


    private List<DocElement2> docElements;
    
    public DataCartDocs2() {
        
    }
    

    public List<DocElement2> getDocElements() {
        return docElements;
    }

    public void setDocElements(List<DocElement2> docElements) {
        this.docElements = docElements;
    }
    
    public void addDocElement2(DocElement2 docElement) {
        if(this.docElements == null) {
            docElements = new ArrayList<DocElement2>();
        }
        
        if(docElement != null) {
            docElements.add(docElement);
        }
        
    }
    
    public void removeDocElement2(DocElement2 docElement) {
        if(this.docElements == null) {
            docElements = new ArrayList<DocElement2>();
        }
        
        if(docElement != null) {
            docElements.remove(docElement);
        }
        
    }
    
    
    /** Description of toElement() 
     * 
     */
    @Override
    public Element toElement() {
        
        Element docsEl = new Element("docs");
        
        if(this.docElements != null) {
            for(int i=0;i<this.docElements.size();i++) {
                docsEl.addContent(this.docElements.get(i).toElement());
            }
        }
        
        return docsEl;
    }


    /** Descriptionn of toXML()
     * 
     */
    @Override
    public String toXML() {
        String xml = "";
        
        Element docsEl = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(docsEl);
        
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
        
        this.docElements = null;
        
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
        if(docElement.getNodeName().equals("docs")) {
            NodeList docNodeList = docElement.getChildNodes();//doc.getDocumentElement().getChildNodes();
            
            List<DocElement2> docElements = new ArrayList<DocElement2>();
            
            for(int i=0;i<docNodeList.getLength();i++) {
                Node topLevelDocNode = docNodeList.item(i);
                if (topLevelDocNode.getNodeType() == Node.ELEMENT_NODE) {
                
                    org.w3c.dom.Element topLevelElement = (org.w3c.dom.Element) topLevelDocNode;
                    
                    if(topLevelElement.getTagName().equals("doc")) {
                        System.out.println("\t" + topLevelElement.getTagName());
                        DocElement2 de = new DocElement2();
                        de.readHelper(topLevelElement);
                        docElements.add(de);
                    }
                    
                }
            }
            this.setDocElements(docElements);
        }
    }
    
    
    public static void main(String [] args) {
        DataCartDocs2 d = new DataCartDocs2();
        
        d.fromFile(testInitializationFile);
        
        DocElement2 de = d.getDocElements().get(1);

        System.out.println(de.getFileElements().size());
        System.out.println(de.getDatasetId());

        d.addDocElement2(de);
        d.addDocElement2(de);
        
        System.out.println(d.getDocElements().size());
        
    }
    
}
