package org.esgf.commonui;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class MyTestRunner {
	public static void main(String[] args) {

        System.out.println("Running test for CommonUIUtilsTest");
        Result result = JUnitCore.runClasses(CommonUIUtilsTest.class);
        
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Total Tests: " + result.getRunCount());
        System.out.println("Failure count: " + result.getFailureCount());
        
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
	}
}

