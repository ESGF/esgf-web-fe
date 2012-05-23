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

public class MIMESElementTest {
    
    String DIR_PREFIX = "src/java/test/org/esgf/datacart/xml/MIMESElement/";//URLSElementTestData-WrongElementNames.xml";
    String FILENAME_WRONG_ELEMENT = DIR_PREFIX + "MIMESElementTestData-WrongElementNames.xml";
    String FILENAME_LIST1 = DIR_PREFIX + "MIMESElementTestData-List1.xml";
    String FILENAME_LIST2 = DIR_PREFIX + "MIMESElementTestData-List2.xml";
    String FILENAME_POST = DIR_PREFIX + "MIMESElementTestData-Post.xml";
    String FILENAME_MIXED = DIR_PREFIX + "MIMESElementTestData-Mixed.xml";
   
    
    
    //test runner
    public static void main(String [] args) {
        
        
        System.out.println("Running test for MIMESElementTest");
        Result result = JUnitCore.runClasses(MIMESElementTest.class);
        
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Total Tests: " + result.getRunCount());
        System.out.println("Failure count: " + result.getFailureCount());
         
    }
    

    @Test
    /**
     * Test1 - file doesn't exist
     * should yield a MIMESElement with an empty mimes list
     */
    public void test1() {

        String fileName = "a.txt";
        
        MIMESElement mimesElement = new MIMESElement();
        mimesElement.fromFile(fileName);
        
        assertEquals(mimesElement.getMimes().size(),0);
    }
    
    @Test
    /**
     * Test2 - file exists but the DOM is different
     * should produce an empty mimes list
     */
    public void test2() {
        String fileName = FILENAME_WRONG_ELEMENT;
        
        MIMESElement mimesElement = new MIMESElement();
        mimesElement.fromFile(fileName);
        //System.out.println("size: " + mimesElement.getMimes().size());
        assertEquals(mimesElement.getMimes().size(),0);
        
    }
    
    @Test
    /**
     * Test3 - add a mime to an empty mimes element
     */
    public void test3() {
        
        MIMESElement mimesElement = new MIMESElement();
        mimesElement.addMime("mime1");
        assertEquals(mimesElement.getMimes().size(),1);
        
    }
    
    @Test
    /**
     * Test4 - file exists with correct DOM with 1 mime
     * 
     */
    public void test4() {
        String fileName = FILENAME_LIST1;
        
        MIMESElement mimesElement = new MIMESElement();
        mimesElement.fromFile(fileName);
        
        assertEquals(mimesElement.getMimes().size(),1);
        assertEquals(mimesElement.getMimes().get(0),"mime1");
        
    }
    
    @Test
    /**
     * Test 5 - file exists with mixed DOM
     */
    public void test5() {
        String fileName = FILENAME_MIXED;

        MIMESElement mimesElement = new MIMESElement();
        mimesElement.fromFile(fileName);
        assertEquals(mimesElement.getMimes().size(),2);
        assertEquals(mimesElement.getMimes().get(1),"mime2");
        
    }
    
    
    @Test
    /**
     * Test 6 - file exists with correct DOM with 2 mimes
     */
    public void test6() {
        String fileName = FILENAME_LIST2;
        
        MIMESElement mimesElement = new MIMESElement();
        mimesElement.fromFile(fileName);
        
        assertEquals(mimesElement.getMimes().size(),2);
        assertEquals(mimesElement.getMimes().get(1),"mime2");
        
    }
    
    
    @Test
    /**
     * Test 7 - add a mime
     */
    public void test7() {
        String fileName = FILENAME_LIST2;
        
        MIMESElement mimesElement = new MIMESElement();
        mimesElement.fromFile(fileName);
        mimesElement.addMime("mime3");
        assertEquals(mimesElement.getMimes().size(),3);
    }
    
    @Test
    /**
     * Test 8 - add a null mime
     */
    public void test8() {

        String fileName = FILENAME_LIST2;
        
        MIMESElement mimesElement = new MIMESElement();
        mimesElement.fromFile(fileName);
        mimesElement.addMime("mime3");

        mimesElement.addMime(null);
        assertEquals(mimesElement.getMimes().size(),3);
        
    }
    
    @Test
    /**
     * Test 9 - remove a null mime
     */
    public void test9() {

        String fileName = FILENAME_LIST2;
        
        MIMESElement mimesElement = new MIMESElement();
        mimesElement.fromFile(fileName);
        mimesElement.addMime("mime3");

        mimesElement.removeMime(null);
        assertEquals(mimesElement.getMimes().size(),3);
    }
    
    @Test
    /**
     * Test10 - remove a mime that doesn't exist
     */
    public void test10() {

        String fileName = FILENAME_LIST2;
        
        MIMESElement mimesElement = new MIMESElement();
        mimesElement.fromFile(fileName);
        mimesElement.addMime("mime3");
        
        mimesElement.removeMime("mime4");
        assertEquals(mimesElement.getMimes().size(),3);
    }
    
    @Test
    /**
     * Test11 - remove a mime that does exist
     */
    public void test11() {

        String fileName = FILENAME_LIST2;
        
        MIMESElement mimesElement = new MIMESElement();
        mimesElement.fromFile(fileName);
        mimesElement.addMime("mime3");

        mimesElement.removeMime("mime2");
        assertEquals(mimesElement.getMimes().size(),2);
        assertEquals(mimesElement.getMimes().get(1),"mime3");
    }
    
    @Test
    /**
     * Test12 - write to a file and extract object from that file
     */
    public void test12() {


        String fileName = FILENAME_LIST2;
        
        MIMESElement mimesElement = new MIMESElement();
        mimesElement.fromFile(fileName);
        mimesElement.addMime("mime3");
        
        fileName = FILENAME_POST;
        
        mimesElement.toFile(fileName);
        
        MIMESElement mimesElement2 = new MIMESElement();
        mimesElement2.fromFile(fileName);
        
        assertEquals(mimesElement.getMimes().size(),mimesElement2.getMimes().size());
        assertEquals(mimesElement.getMimes().get(0),mimesElement2.getMimes().get(0));
    }
    
    @Test
    /**
     * Test 13 - fromXML/readHelper, read from null
     */
    public void test13() {

        org.w3c.dom.Element docElement = null;

        MIMESElement mimesElement3 = new MIMESElement();
        mimesElement3.readHelper(docElement);
        
        assertEquals(mimesElement3.getMimes().size(),0);
    }
    
    @Test
    /**
     * Test 14 - readHelper for non-conformant DOM
     */
    public void test14() {
        org.w3c.dom.Element docElement = null;
        
        MIMESElement mimesElement3 = new MIMESElement();
        String xml = "<a>a</a>";
        docElement = domHelper(xml);
        mimesElement3.readHelper(docElement);
        
        assertEquals(mimesElement3.getMimes().size(),0);
        
    }
    
    @Test
    /**
     * Test 15 - readHelper for conformant DOM
     */
    public void test15() {
        org.w3c.dom.Element docElement = null;
        
        MIMESElement mimesElement3 = new MIMESElement();
        String xml = "<mimes><mime>mime1</mime></mimes>";
        docElement = domHelper(xml);
        mimesElement3.readHelper(docElement);
        
        assertEquals(mimesElement3.getMimes().size(),1);
    }
    
    @Test
    /**
     * Test 16 - readHelper with mixed DOM
     */
    public void test16() {
        org.w3c.dom.Element docElement = null;

        MIMESElement mimesElement3 = new MIMESElement();
        String xml = "<mimes><mime>mime1</mime><ab>a</ab></mimes>";
        docElement = domHelper(xml);
        mimesElement3.readHelper(docElement);
        
        assertEquals(mimesElement3.getMimes().size(),1);
    }
    
    @Test
    /**
     * Test 17 - ensure reread of file resets the mimes
     * 
     */
    public void test17() {

        String fileName = FILENAME_LIST1;
        
        MIMESElement mimesElement = new MIMESElement();
        mimesElement.fromFile(fileName);
        
        fileName = FILENAME_LIST2;
        
        mimesElement.fromFile(fileName);
        
        assertEquals(mimesElement.getMimes().size(),2);
        
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
