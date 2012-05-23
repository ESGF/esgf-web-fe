package org.esgf.datacart;


import static org.junit.Assert.*;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class URLSElementTest {
    String DIR_PREFIX = "src/java/test/org/esgf/datacart/xml/URLSElement/";//URLSElementTestData-WrongElementNames.xml";
    String FILENAME_WRONG_ELEMENT = DIR_PREFIX + "URLSElementTestData-WrongElementNames.xml";
    String FILENAME_LIST1 = DIR_PREFIX + "URLSElementTestData-List1.xml";
    String FILENAME_LIST2 = DIR_PREFIX + "URLSElementTestData-List2.xml";
    String FILENAME_POST = DIR_PREFIX + "URLSElementTestData-Post.xml";
    String FILENAME_MIXED = DIR_PREFIX + "URLSElementTestData-Mixed.xml";
    
    //String FILENAME_WRONG_ELEMENT = "/Users/8xo/esgProjects/5-22/esgf-web-fe/src/java/test/org/esgf/datacart/xml/URLSElement/URLSElementTestData.xml";
    
    
    //test runner
    public static void main(String [] args) {
        
        
        System.out.println("Running test for URLSElementTest");
        Result result = JUnitCore.runClasses(URLSElementTest.class);
        
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Total Tests: " + result.getRunCount());
        System.out.println("Failure count: " + result.getFailureCount());
    }
    
   
    
    @Test
    /**
     * Test1 - file doesn't exist
     * should yield a URLSElement with an empty urls list
     */
    public void test1() {

        String fileName = "a.txt";
        
        URLSElement urlsElement = new URLSElement();
        urlsElement.fromFile(fileName);
        
        assertEquals(urlsElement.getUrls().size(),0);
    }
    
    @Test
    /**
     * Test2 - file exists but the DOM is different
     * should produce an empty urls list
     */
    public void test2() {
        String fileName = FILENAME_WRONG_ELEMENT;
        
        URLSElement urlsElement = new URLSElement();
        urlsElement.fromFile(fileName);
        //System.out.println("size: " + urlsElement.getUrls().size());
        assertEquals(urlsElement.getUrls().size(),0);
        
    }
    
    @Test
    /**
     * Test3 - add a url to an empty urls element
     */
    public void test3() {
        
        URLSElement urlsElement = new URLSElement();
        urlsElement.addUrl("url1");
        assertEquals(urlsElement.getUrls().size(),1);
        
    }
    
    @Test
    /**
     * Test4 - file exists with correct DOM with 1 url
     * 
     */
    public void test4() {
        String fileName = FILENAME_LIST1;
        
        URLSElement urlsElement = new URLSElement();
        urlsElement.fromFile(fileName);
        
        assertEquals(urlsElement.getUrls().size(),1);
        assertEquals(urlsElement.getUrls().get(0),"url1");
        
    }
    
    @Test
    /**
     * Test 5 - file exists with mixed DOM
     */
    public void test5() {
        String fileName = FILENAME_MIXED;

        URLSElement urlsElement = new URLSElement();
        urlsElement.fromFile(fileName);
        assertEquals(urlsElement.getUrls().size(),2);
        assertEquals(urlsElement.getUrls().get(1),"url2");
        
    }
    
    
    @Test
    /**
     * Test 6 - file exists with correct DOM with 2 urls
     */
    public void test6() {
        String fileName = FILENAME_LIST2;
        
        URLSElement urlsElement = new URLSElement();
        urlsElement.fromFile(fileName);
        
        assertEquals(urlsElement.getUrls().size(),2);
        assertEquals(urlsElement.getUrls().get(1),"url2");
        
    }
    
    
    @Test
    /**
     * Test 7 - add a url
     */
    public void test7() {
        String fileName = FILENAME_LIST2;
        
        URLSElement urlsElement = new URLSElement();
        urlsElement.fromFile(fileName);
        urlsElement.addUrl("url3");
        assertEquals(urlsElement.getUrls().size(),3);
    }
    
    @Test
    /**
     * Test 8 - add a null url
     */
    public void test8() {

        String fileName = FILENAME_LIST2;
        
        URLSElement urlsElement = new URLSElement();
        urlsElement.fromFile(fileName);
        urlsElement.addUrl("url3");

        urlsElement.addUrl(null);
        assertEquals(urlsElement.getUrls().size(),3);
        
    }
    
    @Test
    /**
     * Test 9 - remove a null url
     */
    public void test9() {

        String fileName = FILENAME_LIST2;
        
        URLSElement urlsElement = new URLSElement();
        urlsElement.fromFile(fileName);
        urlsElement.addUrl("url3");

        urlsElement.removeUrl(null);
        assertEquals(urlsElement.getUrls().size(),3);
    }
    
    @Test
    /**
     * Test10 - remove a url that doesn't exist
     */
    public void test10() {

        String fileName = FILENAME_LIST2;
        
        URLSElement urlsElement = new URLSElement();
        urlsElement.fromFile(fileName);
        urlsElement.addUrl("url3");
        
        urlsElement.removeUrl("url4");
        assertEquals(urlsElement.getUrls().size(),3);
    }
    
    @Test
    /**
     * Test11 - remove a url that does exist
     */
    public void test11() {

        String fileName = FILENAME_LIST2;
        
        URLSElement urlsElement = new URLSElement();
        urlsElement.fromFile(fileName);
        urlsElement.addUrl("url3");

        urlsElement.removeUrl("url2");
        assertEquals(urlsElement.getUrls().size(),2);
        assertEquals(urlsElement.getUrls().get(1),"url3");
    }
    
    @Test
    /**
     * Test12 - write to a file and extract object from that file
     */
    public void test12() {


        String fileName = FILENAME_LIST2;
        
        URLSElement urlsElement = new URLSElement();
        urlsElement.fromFile(fileName);
        urlsElement.addUrl("url3");
        
        fileName = FILENAME_POST;
        
        urlsElement.toFile(fileName);
        
        URLSElement urlsElement2 = new URLSElement();
        urlsElement2.fromFile(fileName);
        
        assertEquals(urlsElement.getUrls().size(),urlsElement2.getUrls().size());
        assertEquals(urlsElement.getUrls().get(0),urlsElement2.getUrls().get(0));
    }
    
    @Test
    /**
     * Test 13 - fromXML/readHelper, read from null
     */
    public void test13() {

        org.w3c.dom.Element docElement = null;

        URLSElement urlsElement3 = new URLSElement();
        urlsElement3.readHelper(docElement);
        
        assertEquals(urlsElement3.getUrls().size(),0);
    }
    
    @Test
    /**
     * Test 14 - readHelper for non-conformant DOM
     */
    public void test14() {
        org.w3c.dom.Element docElement = null;
        
        URLSElement urlsElement3 = new URLSElement();
        String xml = "<a>a</a>";
        docElement = domHelper(xml);
        urlsElement3.readHelper(docElement);
        
        assertEquals(urlsElement3.getUrls().size(),0);
        
    }
    
    @Test
    /**
     * Test 15 - readHelper for conformant DOM
     */
    public void test15() {
        org.w3c.dom.Element docElement = null;
        
        URLSElement urlsElement3 = new URLSElement();
        String xml = "<urls><url>url1</url></urls>";
        docElement = domHelper(xml);
        urlsElement3.readHelper(docElement);
        
        assertEquals(urlsElement3.getUrls().size(),1);
    }
    
    @Test
    /**
     * Test 16 - readHelper with mixed DOM
     */
    public void test16() {
        org.w3c.dom.Element docElement = null;

        URLSElement urlsElement3 = new URLSElement();
        String xml = "<urls><url>url1</url><ab>a</ab></urls>";
        docElement = domHelper(xml);
        urlsElement3.readHelper(docElement);
        
        assertEquals(urlsElement3.getUrls().size(),1);
    }
    
    @Test
    /**
     * Test 17 - ensure reread of file resets the urls
     * 
     */
    public void test17() {

        String fileName = FILENAME_LIST1;
        
        URLSElement urlsElement = new URLSElement();
        urlsElement.fromFile(fileName);
        
        fileName = FILENAME_LIST2;
        
        urlsElement.fromFile(fileName);
        
        assertEquals(urlsElement.getUrls().size(),2);
        
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










