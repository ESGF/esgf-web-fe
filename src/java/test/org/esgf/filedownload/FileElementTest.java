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

public class FileElementTest {
    
    private final static Logger LOG = Logger.getLogger(FileElementTest.class);

    private final static String testInitializationFile = "src\\java\\test\\org\\esgf\\filedownload\\files\\FileElement\\FileElement.xml";
    private final static String testAddRemoveFile = "src\\java\\test\\org\\esgf\\filedownload\\files\\FileElement\\FileElement2.xml";
    
    
    @Before
    public void setUp() throws JDOMException, IOException {
            
    }
    
    @Test
    public void testInitialization() throws JDOMException, IOException, DocumentException {
        System.out.println("Test initializaton");

        FileElement fe = new FileElement();
        
        String FilefileName = testInitializationFile;
        
        //get XML string from the file
        String fileXML = Utils.getXMLStringFromFile(FilefileName);
        
        //get XML string from the Element
        String mimeXML = Utils.getXMLStringFromMIMESElement(fe);

        //compare
        assertEquals(fileXML,mimeXML);
    }
    
    @Test
    public void testAddRemove() throws JDOMException, IOException, DocumentException {
        System.out.println("Test addRemove");

        FileElement fe = new FileElement();
        String FileElementAddFileName = this.testAddRemoveFile; 
        String FileElementRemoveFileName = this.testInitializationFile;
        
        fe.addMIME("mime2");
        fe.addService("service2");
        fe.addURL("url2");

        //get XML string from the file
        String fileXML = Utils.getXMLStringFromFile(FileElementAddFileName);
        
        //get XML string from the Element
        String mimeXML = Utils.getXMLStringFromMIMESElement(fe);

        //compare
        assertEquals(fileXML,mimeXML);
        

        fe.removeMIME("mime2");
        fe.removeService("service2");
        fe.removeURL("url2");
        
        //get XML string from the file
        fileXML = Utils.getXMLStringFromFile(FileElementRemoveFileName);
        
        //get XML string from the Element
        mimeXML = Utils.getXMLStringFromMIMESElement(fe);

        //compare
        assertEquals(fileXML,mimeXML);
        
    }
    
    @Test
    public void testNull() {
        System.out.println("Test null");

        FileElement fe = new FileElement();
        
        String mime2 = null;
        String url2 = null;
        String service2 = null;
        
        fe.addMIME(mime2);
        
        String FilefileName = testInitializationFile;
        
        //get XML string from the file
        String fileXML = Utils.getXMLStringFromFile(FilefileName);

        //get XML string from the Element
        String mimeXML = Utils.getXMLStringFromMIMESElement(fe);
        
        assertEquals(fileXML,mimeXML);
        
        fe.removeMIME(mime2);

        //get XML string from the Element
        mimeXML = Utils.getXMLStringFromMIMESElement(fe);

        assertEquals(fileXML,mimeXML);
        
        fe.addURL(url2);
        
        //get XML string from the Element
        String urlXML = Utils.getXMLStringFromMIMESElement(fe);

        assertEquals(fileXML,urlXML);
        
        fe.removeURL(url2);
        
        urlXML = Utils.getXMLStringFromMIMESElement(fe);
        
        assertEquals(fileXML,urlXML);
        
        
        //get XML string from the Element
        String serviceXML = Utils.getXMLStringFromMIMESElement(fe);

        assertEquals(fileXML,serviceXML);
        
        fe.removeURL(service2);
        
        serviceXML = Utils.getXMLStringFromMIMESElement(fe);
        
        assertEquals(fileXML,serviceXML);
        
        
        
    }
    

    
    @Test
    public void testToElement() {
        System.out.println("Test TestToElement");

        FileElement fe = new FileElement();
        
        fe.addMIME("mime2");
        fe.addService("service2");
        fe.addURL("url2");
        
        String addedInfoFileName = testAddRemoveFile;
        
        //get XML string from the Element
        Element feElement = fe.toElement();
        String feElementXML = Utils.getXMLStringFromElement(feElement);

        //get XML string from the file
        Element fromFileElement = Utils.getElementFromFile(addedInfoFileName);
        String fromFileElementXML = Utils.getXMLStringFromElement(fromFileElement);
        
        //compare
        assertEquals(feElementXML,fromFileElementXML);
        
    }
    
    
    
    @After
    public void tearDown() {
        LOG.debug("Tearing down MIMESElementTest");
    }
    
    
    
    
}

