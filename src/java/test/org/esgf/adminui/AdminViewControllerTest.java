package org.esgf.adminui;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.mock.web.MockHttpServletRequest;

public class AdminViewControllerTest {
    
    private MockHttpServletRequest request;
    private final static Logger LOG = Logger.getLogger(AdminViewControllerTest.class);
    
    @Before
    public void setUp() {
        LOG.debug("Setting up AdminViewControllerTest");
    }
    
    @Test
    public void testdoGet() {
        LOG.debug("Test doGet()");
    }
    
    
    @After
    public void tearDown() {
        LOG.debug("Tearing down AdminViewControllerTest");
    }
    
}
