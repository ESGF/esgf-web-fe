package org.esgf.datacart;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class DataCartSolrHandlerTest {
  //test runner
    public static void main(String [] args) {
        
        
        System.out.println("Running test for DataCartSolrHandlerTest");
        Result result = JUnitCore.runClasses(DataCartSolrHandlerTest.class);
        
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Total Tests: " + result.getRunCount());
        System.out.println("Failure count: " + result.getFailureCount());
         
    }
    
    /*
     * Test Cases to be conducted (grouped by method name)
     * 
     * DataCartSolrHandler constructor/preassembleQueryString
     * 
     * queryIndex()/querySolrForFiles()
     * 
     * solrResponseToJSON()
     * 
     * getFileElements()
     * 
     * getDocElement2()
     * 
     */
    

    @Test
    public void test1() {
        assertEquals(0,0);
    }
    
}
