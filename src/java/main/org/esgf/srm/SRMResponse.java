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

public class SRMResponse {

	private String [] response_urls;
	private String message;
	
	public SRMResponse() {
		this.setResponse_urls(null);
		this.setMessage(null);
	}
	
	public SRMResponse(String [] response_urls,String message) {
		this.setResponse_urls(response_urls);
		this.setMessage(message);
	}

	/**
	 * @return the response_urls
	 */
	public String [] getResponse_urls() {
		return response_urls;
	}

	/**
	 * @param response_urls the response_urls to set
	 */
	public void setResponse_urls(String [] response_urls) {
		this.response_urls = response_urls;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/** Description of toElement()
     * 
     * @return serialized XML element equivalent of the class
     */
    public Element toElement() {

        Element srm_responseEl = new Element("srm_response");
        
        if(this.message != null) {
            Element messageEl = new Element("message");
            messageEl.addContent(this.message);
            srm_responseEl.addContent(messageEl);
        }
        
        if(this.response_urls != null) {
        	Element response_urlsEl = new Element("response_urls");
        	
        	for(int i=0;i<this.response_urls.length;i++) {
        		Element response_urlEl = new Element("response_url");
        		String response_url = this.response_urls[i];
        		response_urlEl.addContent(response_url);
        		response_urlsEl.addContent(response_urlEl);
        	}
        	
        	srm_responseEl.addContent(response_urlsEl);
        }
        
        return srm_responseEl;
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
    
    
    /** Description of readHelper
     * 
     * @param docElement
     */
    public void readHelper(org.w3c.dom.Element docElement) {
    
    	if(docElement.getNodeName().equals("srm_response")) {
    		NodeList fileNodeList = docElement.getChildNodes();
            
    		for(int i=0;i<fileNodeList.getLength();i++) {
                Node topLevelFileNode = fileNodeList.item(i);
                if (topLevelFileNode.getNodeType() == Node.ELEMENT_NODE) {
                	
                	org.w3c.dom.Element topLevelElement = (org.w3c.dom.Element) topLevelFileNode;
                    if(topLevelElement.getTagName().equals("message")) {
                    	String message = topLevelElement.getTextContent();
                    	this.message = message;
                    }
                    if(topLevelElement.getTagName().equals("response_urls")) {
                    	NodeList response_urls_list = topLevelFileNode.getChildNodes();
                    	List<String> response_urls = new ArrayList<String>();
                    	for(int j=0;j<response_urls_list.getLength();j++) {
                    		Node response_urlNode = response_urls_list.item(j);
                    		if (response_urlNode.getNodeType() == Node.ELEMENT_NODE) {
                    			if(response_urlNode.getNodeName().equals("response_url")) {
                                    response_urls.add(response_urlNode.getTextContent());
                    			}
                    		}
                    	}
                    	this.response_urls = new String[response_urls.size()];
                    	for(int j=0;j<response_urls.size();j++) {
                    		this.response_urls[j] = response_urls.get(j);
                    	}
                    }
                	
                }
    		}
    	}
    }
	
	public String toString() {
		String str = "";
		str += "\n----SRM Response Message----\n";
		str += "Message:\n\t" + this.message + "\n";
		str += "Response_urls:\n";
		for(int i=0;i<this.response_urls.length;i++) {
			str += "\t" + this.response_urls[i] + "\n";
		}
		str += "\n----End SRM Response Message----\n";
		return str;
	}
	
}
