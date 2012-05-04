package org.esgf.datacart;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class DataCartDocsTest {
  
    //test runner
    public static void main(String [] args) {
        
        
        System.out.println("Running test for DataCartElementTest");
        Result result = JUnitCore.runClasses(DataCartElementTest.class);
        
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Total Tests: " + result.getRunCount());
        System.out.println("Failure count: " + result.getFailureCount());
         
    }
    
    
    /*
     * Test Cases to be conducted (grouped by method name)
     * 
     * addDocElement()
     * 
     * removeDocElement()
     * 
     * toElement()
     * 
     * fromXML(String xmlStr)
     * 
     * fromFile(String file)
     * 
     * readHelper(org.w3c.dom.Element docElement)
     * 
     * 
     * 
     * readHelper()
     * 
     */
    
    
    
    @Test
    public void testInitialization() {
        int a = 0;
        
        assertEquals(a,0);
        
    }
    
    
}
