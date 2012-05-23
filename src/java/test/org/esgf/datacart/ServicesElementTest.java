package org.esgf.datacart;


import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.DocumentException;
import org.jdom.JDOMException;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class ServicesElementTest {
    

    String DIR_PREFIX = "src/java/test/org/esgf/datacart/xml/ServicesElement/";//ServicesElementTestData-WrongElementNames.xml";
    String FILENAME_WRONG_ELEMENT = DIR_PREFIX + "ServicesElementTestData-WrongElementNames.xml";
    String FILENAME_LIST1 = DIR_PREFIX + "ServicesElementTestData-List1.xml";
    String FILENAME_LIST2 = DIR_PREFIX + "ServicesElementTestData-List2.xml";
    String FILENAME_POST = DIR_PREFIX + "ServicesElementTestData-Post.xml";
    String FILENAME_MIXED = DIR_PREFIX + "ServicesElementTestData-Mixed.xml";
    
    
    //test runner
    public static void main(String [] args) {
        
        
        System.out.println("Running test for ServicesElementTest");
        Result result = JUnitCore.runClasses(ServicesElementTest.class);
        
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Total Tests: " + result.getRunCount());
        System.out.println("Failure count: " + result.getFailureCount());
         
    }
    

    
    @Test
    /**
     * Test1 - file doesn't exist
     * should yield a ServicesElement with an empty services list
     */
    public void test1() {

        String fileName = "a.txt";
        
        ServicesElement servicesElement = new ServicesElement();
        servicesElement.fromFile(fileName);
        
        assertEquals(servicesElement.getServices().size(),0);
    }
    
    @Test
    /**
     * Test2 - file exists but the DOM is different
     * should produce an empty services list
     */
    public void test2() {
        String fileName = FILENAME_WRONG_ELEMENT;
        
        ServicesElement servicesElement = new ServicesElement();
        servicesElement.fromFile(fileName);
        //System.out.println("size: " + servicesElement.getServices().size());
        assertEquals(servicesElement.getServices().size(),0);
        
    }
    
    @Test
    /**
     * Test3 - add a service to an empty services element
     */
    public void test3() {
        
        ServicesElement servicesElement = new ServicesElement();
        servicesElement.addService("service1");
        assertEquals(servicesElement.getServices().size(),1);
        
    }
    
    @Test
    /**
     * Test4 - file exists with correct DOM with 1 service
     * 
     */
    public void test4() {
        String fileName = FILENAME_LIST1;
        
        ServicesElement servicesElement = new ServicesElement();
        servicesElement.fromFile(fileName);
        
        assertEquals(servicesElement.getServices().size(),1);
        assertEquals(servicesElement.getServices().get(0),"service1");
        
    }
    
    @Test
    /**
     * Test 5 - file exists with mixed DOM
     */
    public void test5() {
        String fileName = FILENAME_MIXED;

        ServicesElement servicesElement = new ServicesElement();
        servicesElement.fromFile(fileName);
        assertEquals(servicesElement.getServices().size(),2);
        assertEquals(servicesElement.getServices().get(1),"service2");
        
    }
    
    
    @Test
    /**
     * Test 6 - file exists with correct DOM with 2 services
     */
    public void test6() {
        String fileName = FILENAME_LIST2;
        
        ServicesElement servicesElement = new ServicesElement();
        servicesElement.fromFile(fileName);
        
        assertEquals(servicesElement.getServices().size(),2);
        assertEquals(servicesElement.getServices().get(1),"service2");
        
    }
    
    
    @Test
    /**
     * Test 7 - add a service
     */
    public void test7() {
        String fileName = FILENAME_LIST2;
        
        ServicesElement servicesElement = new ServicesElement();
        servicesElement.fromFile(fileName);
        servicesElement.addService("service3");
        assertEquals(servicesElement.getServices().size(),3);
    }
    
    @Test
    /**
     * Test 8 - add a null service
     */
    public void test8() {

        String fileName = FILENAME_LIST2;
        
        ServicesElement servicesElement = new ServicesElement();
        servicesElement.fromFile(fileName);
        servicesElement.addService("service3");

        servicesElement.addService(null);
        assertEquals(servicesElement.getServices().size(),3);
        
    }
    
    @Test
    /**
     * Test 9 - remove a null service
     */
    public void test9() {

        String fileName = FILENAME_LIST2;
        
        ServicesElement servicesElement = new ServicesElement();
        servicesElement.fromFile(fileName);
        servicesElement.addService("service3");

        servicesElement.removeService(null);
        assertEquals(servicesElement.getServices().size(),3);
    }
    
    @Test
    /**
     * Test10 - remove a service that doesn't exist
     */
    public void test10() {

        String fileName = FILENAME_LIST2;
        
        ServicesElement servicesElement = new ServicesElement();
        servicesElement.fromFile(fileName);
        servicesElement.addService("service3");
        
        servicesElement.removeService("service4");
        assertEquals(servicesElement.getServices().size(),3);
    }
    
    @Test
    /**
     * Test11 - remove a service that does exist
     */
    public void test11() {

        String fileName = FILENAME_LIST2;
        
        ServicesElement servicesElement = new ServicesElement();
        servicesElement.fromFile(fileName);
        servicesElement.addService("service3");

        servicesElement.removeService("service2");
        assertEquals(servicesElement.getServices().size(),2);
        assertEquals(servicesElement.getServices().get(1),"service3");
    }
    
    @Test
    /**
     * Test12 - write to a file and extract object from that file
     */
    public void test12() {


        String fileName = FILENAME_LIST2;
        
        ServicesElement servicesElement = new ServicesElement();
        servicesElement.fromFile(fileName);
        servicesElement.addService("service3");
        
        fileName = FILENAME_POST;
        
        servicesElement.toFile(fileName);
        
        ServicesElement servicesElement2 = new ServicesElement();
        servicesElement2.fromFile(fileName);
        
        assertEquals(servicesElement.getServices().size(),servicesElement2.getServices().size());
        assertEquals(servicesElement.getServices().get(0),servicesElement2.getServices().get(0));
    }
    
    @Test
    /**
     * Test 13 - fromXML/readHelper, read from null
     */
    public void test13() {

        org.w3c.dom.Element docElement = null;

        ServicesElement servicesElement3 = new ServicesElement();
        servicesElement3.readHelper(docElement);
        
        assertEquals(servicesElement3.getServices().size(),0);
    }
    
    @Test
    /**
     * Test 14 - readHelper for non-conformant DOM
     */
    public void test14() {
        org.w3c.dom.Element docElement = null;
        
        ServicesElement servicesElement3 = new ServicesElement();
        String xml = "<a>a</a>";
        docElement = domHelper(xml);
        servicesElement3.readHelper(docElement);
        
        assertEquals(servicesElement3.getServices().size(),0);
        
    }
    
    @Test
    /**
     * Test 15 - readHelper for conformant DOM
     */
    public void test15() {
        org.w3c.dom.Element docElement = null;
        
        ServicesElement servicesElement3 = new ServicesElement();
        String xml = "<services><service>service1</service></services>";
        docElement = domHelper(xml);
        servicesElement3.readHelper(docElement);
        
        assertEquals(servicesElement3.getServices().size(),1);
    }
    
    @Test
    /**
     * Test 16 - readHelper with mixed DOM
     */
    public void test16() {
        org.w3c.dom.Element docElement = null;

        ServicesElement servicesElement3 = new ServicesElement();
        String xml = "<services><service>service1</service><ab>a</ab></services>";
        docElement = domHelper(xml);
        servicesElement3.readHelper(docElement);
        
        assertEquals(servicesElement3.getServices().size(),1);
    }
    
    @Test
    /**
     * Test 17 - ensure reread of file resets the services
     * 
     */
    public void test17() {

        String fileName = FILENAME_LIST1;
        
        ServicesElement servicesElement = new ServicesElement();
        servicesElement.fromFile(fileName);
        
        fileName = FILENAME_LIST2;
        
        servicesElement.fromFile(fileName);
        
        assertEquals(servicesElement.getServices().size(),2);
        
    }
    
    
    private org.w3c.dom.Element domHelper(String xml) {
        org.w3c.dom.Element docElement = null;
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            Document doc = db.parse(is);
            
            docElement = doc.getDocumentElement();
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        
        return docElement;
    }
    
    


}
