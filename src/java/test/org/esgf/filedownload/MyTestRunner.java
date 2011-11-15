package org.esgf.filedownload;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class MyTestRunner {
	public static void main(String[] args) {

	    System.out.println("Running test for DoxElementTest");
        Result result = JUnitCore.runClasses(DocElementTest.class);
        
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Total Tests: " + result.getRunCount());
        System.out.println("Failure count: " + result.getFailureCount());
        
	    /*
	    System.out.println("Running test for FileElementTest");
        Result result = JUnitCore.runClasses(FileElementTest.class);
        
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Total Tests: " + result.getRunCount());
        System.out.println("Failure count: " + result.getFailureCount());
        
	    
	    
        System.out.println("Running test for MIMESElementTest");
        Result result = JUnitCore.runClasses(MIMESElementTest.class);
        
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Total Tests: " + result.getRunCount());
        System.out.println("Failure count: " + result.getFailureCount());
        
        

        System.out.println("Running test for URLSElementTest");
        result = JUnitCore.runClasses(URLSElementTest.class);
        
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Total Tests: " + result.getRunCount());
        System.out.println("Failure count: " + result.getFailureCount());
	       */
	}
}

/*
Result result = JUnitCore.runClasses(MyClassTest.class);
for (Failure failure : result.getFailures()) {
    System.out.println(failure.toString());
}

System.out.println("Running test for MyClass1");
result = JUnitCore.runClasses(MyClass1Test.class);
for (Failure failure : result.getFailures()) {
    System.out.println(failure.toString());
}
System.out.println("Total Tests: " + result.getRunCount());
System.out.println("Failure count: " + result.getFailureCount());
*/