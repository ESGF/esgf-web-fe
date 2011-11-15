package org.esgf.filedownload;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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

public class DocElementTest {
    
    private final static Logger LOG = Logger.getLogger(DocElementTest.class);

    private final static String testInitializationFile = "src\\java\\test\\org\\esgf\\filedownload\\files\\DocElement\\DocElement.xml";
    private final static String testAddRemoveFile = "src\\java\\test\\org\\esgf\\filedownload\\files\\DocElement\\DocElement2.xml";
    private final static String testAddRemoveFile2 = "src\\java\\test\\org\\esgf\\filedownload\\files\\DocElement\\DocElement3.xml";
    
    
    @Before
    public void setUp() throws JDOMException, IOException {
            
    }
    
    @Test
    public void testInitialization() throws JDOMException, IOException, DocumentException {
        System.out.println("Test initializaton");

        DocElement de = new DocElement();
        
        String docfileName = testInitializationFile;

        //get XML string from the file
        String fileXML = Utils.getXMLStringFromFile(docfileName);
        
        //get XML string from the Element
        String mimeXML = Utils.getXMLStringFromMIMESElement(de);

        
        //compare
        assertEquals(fileXML,mimeXML);
        
    }
    
    @Test
    public void testAddRemove() throws JDOMException, IOException, DocumentException {
        System.out.println("Test addRemove");
        

        /* adding a blank element */
        DocElement de = new DocElement();

        String docFileName = testInitializationFile;
        String docAddRemoveFileName = testAddRemoveFile;
        String docAddRemoveFileName2 = testAddRemoveFile2;
        
        FileElement fe = new FileElement();
        
        List<FileElement> fileElements = new ArrayList<FileElement>();
        fileElements.add(fe);
        
        de.setFileElements(fileElements);
        

        //get XML string from the file
        String fileXML = Utils.getXMLStringFromFile(docAddRemoveFileName);
        
        //get XML string from the Element
        String docXML = Utils.getXMLStringFromMIMESElement(de);

        //compare the xml
        assertEquals(fileXML,docXML);
                
        //count should now be 1 (DESPITE THERE BEING 3 ELEMENTS)
        assertEquals(1,de.getCount());
        
        
        /* end adding a blank element */
        
        
        /* edit the third element */

        de = new DocElement();
       
        fe = new FileElement();
        
        fe.setFileId("fileId3");
        
        de.addFileElement(fe);
        
        
        de.addMIMEElement("fileId3", "mime2");
        de.addURLElement("fileId3", "url2");
        de.addServiceElement("fileId3", "service2");
        
        //get XML string from the Element
        docXML = Utils.getXMLStringFromMIMESElement(de);
        
        //get XML string from the file
        fileXML = Utils.getXMLStringFromFile(docAddRemoveFileName2);
      
        System.out.println(docXML);
        
        System.out.println(fileXML);
        
        //compare the xml
        assertEquals(fileXML,docXML);
        
        
        /* end editting the third element */
        
        
    }
    
    @Test
    public void testNull() {
        
    }
    

    
    @Test
    public void testToElement() {
        
       
    }
    
    
    
    @After
    public void tearDown() {
        LOG.debug("Tearing down MIMESElementTest");
    }
    
    
    
    
}

