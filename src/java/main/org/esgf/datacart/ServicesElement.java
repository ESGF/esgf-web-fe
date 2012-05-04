package org.esgf.datacart;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import org.xml.sax.SAXException;

public class ServicesElement {

    /** Description */
    private List<String> services;
    

    private final static String testInitializationFile = "C:\\Users\\8xo\\esgf-web-fe\\serviceselement.xml";
    
    /**
     * 
     */
    public ServicesElement() {
        
    }

    /** 
     * 
     * @param urls
     */
    public void setServices(List<String> services) {
        if(services != null) {
            this.services = services;
        }
    }

    /**
     * 
     * @return
     */
    public List<String> getServices() {
        return services;
    }
    
    /**Description of addService()
     * 
     * @param service
     */
    public void addService(String service) {
        if(this.services == null) {
            this.services = new ArrayList<String>();
        }
        if(service != null) {
            this.services.add(service);
        }
    }
    
    /** Description of removeService
     * 
     * @param service
     */
    public void removeService(String service) {
        if(service != null) {
            this.services.remove(service);
        }
    }
    
    /** Description of toElement()
     * 
     * @return serialized XML element equivalent of the class
     */
    public Element toElement() {
        Element servicesEl = new Element("services");
        
        if(this.services != null) {
            for(int i=0;i<services.size();i++) {
                Element serviceEl = new Element("service");
                serviceEl.addContent(services.get(i));
                servicesEl.addContent(serviceEl);
            }
        }
        
        return servicesEl;
    }
    

    /** Description of toXML()
     * 
     * @return
     */
    public String toXML() {
        String xml = "";
        
        Element servicesEl = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(servicesEl);
        
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
    
    /** Description of toJSON()
     * 
     * @return
     */
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

        //overwrite whatever was in the data structure
        this.services = null;
        
        
        if(docElement.getNodeName().equals("services")) {
          //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            //NodeList nList = doc.getElementsByTagName("service");
     
            NodeList nList = docElement.getChildNodes();
            
            
            for (int temp = 0; temp < nList.getLength(); temp++) {
     
                
               Node nNode = nList.item(temp);
               if (nNode.getNodeType() == Node.ELEMENT_NODE) {
     
                  org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
     
                  //System.out.println(eElement.getTextContent());
                  String service = eElement.getTextContent();
                  if(this.services == null) {
                      this.services = new ArrayList<String>();
                  }
                  this.services.add(service);
                  
               }
            }
            
        }
    }
    
    public static void main(String [] args) {
        ServicesElement u = new ServicesElement();
        
        u.fromFile(testInitializationFile);
        
        System.out.println(new XmlFormatter().format(u.toXML()));
        
        u.toFile(testInitializationFile);
        
        //u = new URLSElement2();
        
        u.fromFile(testInitializationFile);

        System.out.println(u.toXML());
        
        
    }
    
    
}
