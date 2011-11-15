package org.esgf.filedownload;

import java.io.Reader;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class Utils {
    
    public static Element getElementFromFile(String MIMESfileName) {
        SAXBuilder parser = new SAXBuilder();
        Element rootElement = null;
        try {
            Document doc = parser.build(MIMESfileName);
            rootElement = doc.getRootElement();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return rootElement;
    }
    
    
    public static String getXMLStringFromFile(String MIMESfileName) {
        SAXBuilder parser = new SAXBuilder();
        String fileXML = null;
        try {
            Document doc = parser.build(MIMESfileName);
            XMLOutputter fileOutputter = new XMLOutputter();
            fileXML = fileOutputter.outputString(doc.getRootElement());
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        
        return fileXML;
    }
    
    public static String getXMLStringFromElement(Element e) {
        
        XMLOutputter mimeOutputter = new XMLOutputter();
        String xml = mimeOutputter.outputString(e);
        
        return xml;
    }
    
    public static String getXMLStringFromMIMESElement(DataCartElement me) {
        SAXBuilder builder = new SAXBuilder();
        Reader in = new StringReader(me.toXML());
        String mimeXML = null;
        try {
            Document mimeDoc = builder.build(in);
            
            XMLOutputter mimeOutputter = new XMLOutputter();
            mimeXML = mimeOutputter.outputString(mimeDoc.getRootElement());
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return mimeXML;
    }
}
