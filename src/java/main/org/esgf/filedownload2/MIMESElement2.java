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

public class MIMESElement2 {

    /** Description */
    private List<String> mimes;
    

    private final static String testInitializationFile = "C:\\Users\\8xo\\esgf-web-fe\\mimeselement.xml";
    
    /**
     * 
     */
    public MIMESElement2() {
        
    }

    /**
     * 
     * @param urls
     */
    public void setMimes(List<String> mimes) {
        if(mimes != null) {
            this.mimes = mimes;
        }
    }

    /**
     * 
     * @return
     */
    public List<String> getMimes() {
        return mimes;
    }
    
    /** Description of addMime()
     * 
     * @param mime
     */
    public void addMime(String mime) {
        if(this.mimes == null) {
            this.mimes = new ArrayList<String>();
        }
        if(mime != null) {
            this.mimes.add(mime);
        }
    }
    
    /** Description of removeMime() 
     * 
     * @param mime
     */
    public void removeMime(String mime) {
        if(mime != null) {
            this.mimes.remove(mime);
        }
    }
    
    /** Description of toElement()
     * 
     * @return serialized XML element equivalent of the class
     */
    public Element toElement() {
        Element mimesEl = new Element("mimes");
        
        if(this.mimes != null) {
            for(int i=0;i<mimes.size();i++) {
                Element mimeEl = new Element("mime");
                mimeEl.addContent(mimes.get(i));
                mimesEl.addContent(mimeEl);
            }
        }
        
        return mimesEl;
    }
    

    /** Description of toXML()
     * 
     * @return
     */
    public String toXML() {
        String xml = "";
        
        Element mimesEl = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(mimesEl);
        
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
        
        //overwrite whatever was in the data structure
        this.mimes = null;
        
        
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
        this.mimes = null;
        
        
        if(docElement.getNodeName().equals("mimes")) {
          //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            //NodeList nList = doc.getElementsByTagName("service");
     
            NodeList nList = docElement.getChildNodes();
            
            
            for (int temp = 0; temp < nList.getLength(); temp++) {
     
                
               Node nNode = nList.item(temp);
               if (nNode.getNodeType() == Node.ELEMENT_NODE) {
     
                  org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
     
                  //System.out.println(eElement.getTextContent());
                  String mime = eElement.getTextContent();
                  if(this.mimes == null) {
                      this.mimes = new ArrayList<String>();
                  }
                  this.mimes.add(mime);
                  
               }
            }
            
        }
    }
    
    public static void main(String [] args) {
        MIMESElement2 u = new MIMESElement2();
        
        u.fromFile(testInitializationFile);
        
        System.out.println(new XmlFormatter().format(u.toXML()));
        
        u.toFile(testInitializationFile);
        
        //u = new URLSElement2();
        
        u.fromFile(testInitializationFile);

        System.out.println(u.toXML());
        
        
    }
    
    
}
