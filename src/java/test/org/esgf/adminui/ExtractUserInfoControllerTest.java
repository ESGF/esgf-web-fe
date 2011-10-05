package org.esgf.adminui;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.mock.web.MockHttpServletRequest;

public class ExtractUserInfoControllerTest {
    
    private MockHttpServletRequest request;
    private final static Logger LOG = Logger.getLogger(ExtractUserInfoControllerTest.class);
    
    @Before
    public void setUp() {
        LOG.debug("Setting up ExtractUserInfoControllerTest");
    }
    
    @Test
    public void testdoGet() {
        LOG.debug("Test doGet()");
    }

    @Test
    public void testdoPost() {
        LOG.debug("Test doPost()");
    }

    @Test
    public void testprocessGetUserInfoType() {
        LOG.debug("Test processGetUserInfoType()");
    }
    
    
    @After
    public void tearDown() {
        LOG.debug("Tearing down ExtractUserInfoControllerTest");
    }
    
}
