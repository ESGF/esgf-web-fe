package org.esgfLegacy.datacart;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
import org.xml.sax.SAXException;

public class TechnoteElement implements DataCartElement {

    private String name;
    private String location;


    private final static String testInitializationFile = "C:\\Users\\8xo\\esgf-web-fe\\technoteelement.xml";
    
    
    public TechnoteElement() {
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toXML() {
        String xml = "";
        
        Element technoteElement = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(technoteElement);
        
        return xml;
    }

    @Override
    public Element toElement() {
        Element technoteEl = new Element("technote");

        Element technoteNameEl = new Element("name");
        if(this.name != null) {
            technoteNameEl.addContent(this.name);
        }
        
        Element technoteLocationEl = new Element("location");
        if(this.location != null) {
            technoteLocationEl.addContent(this.location);
        }
        
        technoteEl.addContent(technoteNameEl);
        technoteEl.addContent(technoteLocationEl);
        
        return technoteEl;
    }

    @Override
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

    @Override
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

    @Override
    /**Description of fromFile()
     * 
     * @param file
     */
    public void fromFile(String file) {
        
        //overwrite whatever was in the data structure
        //this.urls = null;
        
        
        File fXmlFile = new File(file);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            
            doc.getDocumentElement().normalize();
            
            
            if(doc.getDocumentElement().getNodeName().equals("technote")) {
                
                NodeList nodes = doc.getDocumentElement().getChildNodes();
                
                
                for(int i=0;i<nodes.getLength();i++) {
                    Node node = nodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {

                        org.w3c.dom.Element eElement = (org.w3c.dom.Element) node;
                        if(eElement.getTagName().equals("name")) {
                            this.name = eElement.getTextContent();
                        } else if(eElement.getTagName().equals("location")) {
                            this.location = eElement.getTextContent();
                        }
                        
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
        
    }
    
    
    public static void main(String [] args) {
        TechnoteElement u = new TechnoteElement();
        
        
        
        u.fromFile(testInitializationFile);
        
        System.out.println(new XmlFormatter().format(u.toXML()));
        
        //u.toFile(testInitializationFile);
        
        //u = new URLSElement2();
        
        //u.fromFile(testInitializationFile);

        //System.out.println(u.toXML());
        
        
    }
    
    
    
}
