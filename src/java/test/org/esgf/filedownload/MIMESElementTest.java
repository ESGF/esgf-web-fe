package org.esgf.filedownload;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;

public class MIMESElementTest {
    
    private final static Logger LOG = Logger.getLogger(MIMESElementTest.class);
    
    private final static String testInitializationFile = "src\\java\\test\\org\\esgf\\filedownload\\files\\MIMESElement\\MIMESInitializationElement.xml";
    private final static String testAddRemoveFile = "src\\java\\test\\org\\esgf\\filedownload\\files\\MIMESElement\\MIMESAddElement.xml";
    
    
    
    
    @Before
    public void setUp() throws JDOMException, IOException {
            
        
    }
    
    @Test
    public void testInitialization() throws JDOMException, IOException, DocumentException {
        System.out.println("Test initializaton");

        MIMESElement me = new MIMESElement();
        
        String MIMESfileName = testInitializationFile; 
        
        //compare sizes...initial size should be 1 
        int initialSize = 1;
        assertEquals(initialSize,me.getMIMES().size());
        
        
        //get XML string from the file
        String fileXML = getXMLStringFromFile(MIMESfileName);
        
        //get XML string from the Element
        String mimeXML = getXMLStringFromMIMESElement(me);
        
        //compare
        assertEquals(fileXML,mimeXML);
       
    }
    
    @Test
    public void testAddRemove() throws JDOMException, IOException, DocumentException {
        System.out.println("Test addRemove");

        MIMESElement me = new MIMESElement();
        
        String MIMESAddFileName = this.testAddRemoveFile; 
        
        String MIMESRemoveFileName = this.testInitializationFile;
        
        //testing the add
        me.addMIME("mime2");
    
        //get XML string from the file
        String fileXML = getXMLStringFromFile(MIMESAddFileName);
        
        //get XML string from the Element
        String mimeXML = getXMLStringFromMIMESElement(me);
        
        //compare
        assertEquals(fileXML,mimeXML);
        int size = me.getMIMES().size();
        assertEquals(size,me.getMIMES().size());
        
        //testing the remove
        me.removeMIME("mime2");

        //get XML string from the file
        fileXML = getXMLStringFromFile(MIMESRemoveFileName);

        //get XML string from the Element
        mimeXML = getXMLStringFromMIMESElement(me);

        //compare
        assertEquals(fileXML,mimeXML);
    }
    
    @Test
    public void testAddNull() {
        System.out.println("Test addNull");

        MIMESElement me = new MIMESElement();
        
        String MIMESFileName = this.testInitializationFile;
        
        String mimeString = null;
        
        me.addMIME(mimeString);
        
        //get XML string from the file
        String fileXML = getXMLStringFromFile(MIMESFileName);

        //get XML string from the Element
        String mimeXML = getXMLStringFromMIMESElement(me);

        //compare
        assertEquals(fileXML,mimeXML);
        
    }
    
    @Test
    public void testRemoveNull() {
        System.out.println("Test removeNull");

        MIMESElement me = new MIMESElement();
        
        String MIMESFileName = this.testInitializationFile;
        
        String mimeString = null;
        
        me.removeMIME(mimeString);
        
        //get XML string from the file
        String fileXML = getXMLStringFromFile(MIMESFileName);

        //get XML string from the Element
        String mimeXML = getXMLStringFromMIMESElement(me);

        //compare
        assertEquals(fileXML,mimeXML);
    }
    
    
    
    @Test
    public void testToElement() {
        System.out.println("Test removeTestToElement");

        MIMESElement me = new MIMESElement();
        
        me.addMIME("mime2");
        
        String MIMESFileName = this.testAddRemoveFile;
        
        //get XML string from the Element
        Element meElement = me.toElement();
        String meElementXML = getXMLStringFromElement(meElement);

        //get XML string from the file
        Element fromFileElement = getElementFromFile(MIMESFileName);
        String fromFileElementXML = getXMLStringFromElement(fromFileElement);
        
        //compare
        assertEquals(meElementXML,fromFileElementXML);
        
    }
    
    @After
    public void tearDown() {
        LOG.debug("Tearing down MIMESElementTest");
    }
    
    
    
    private Element getElementFromFile(String MIMESfileName) {
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
    
    private String getXMLStringFromFile(String MIMESfileName) {
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
    
    private String getXMLStringFromElement(Element e) {
        
        XMLOutputter mimeOutputter = new XMLOutputter();
        String xml = mimeOutputter.outputString(e);
        
        return xml;
    }
    
    private String getXMLStringFromMIMESElement(MIMESElement me) {
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

