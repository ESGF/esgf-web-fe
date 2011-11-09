package org.esgf.commonui;

import static org.junit.Assert.*;

import java.util.Map;

import javax.servlet.http.Cookie;

import org.esgf.adminui.CreateGroupsController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.mock.web.MockHttpServletRequest;

public class CommonUIUtilsTest {
    
    private MockHttpServletRequest request;
    
    
    @Before
    public void setUp() {
        System.out.println("In setUp()");
        request = new MockHttpServletRequest();
        
        
    }
    
    
    
    @Test
    public void testGetForm() {
        System.out.println("In testgetForm()");
        
        try {
            System.out.println("Try");
            //CreateGroupsController controller = new CreateGroupsController();
            
            request.setMethod("GET");
            
            
            Cookie [] cookies = new Cookie[1];
            Cookie cookie = new Cookie("esgf.idp.cookie", "https://pcmdi3.llnl.gov/esgcet/myopenid/jfharney");
            cookies[0] = cookie;
            request.setCookies(cookies);
            
            request.addHeader("esgf.idp.cookie", "https://pcmdi3.llnl.gov/esgcet/myopenid/jfharney");
            
            String type = Utils.getIdFromHeaderCookie(request);
            System.out.println("type: " + type);
            
            //addGroup
            
            
            
        } catch(Exception e) {
            System.out.println("Catch");
            e.printStackTrace();
        }
        
        /*
        SearchController controller = new SearchController();
        request.setMethod("GET");
        ModelAndView mav = null;
        try {
            mav = controller.handleRequest(request, null);
        } catch(Exception e) {
            fail();
        }
        assertEquals("/book/searchForm.jsp", mav.getViewName());
        */
    }
    
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
