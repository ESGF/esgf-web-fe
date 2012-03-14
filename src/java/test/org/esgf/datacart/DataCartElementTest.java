package org.esgf.datacart;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class DataCartElementTest {
  
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
    
    
    
    
    
    
    
}
