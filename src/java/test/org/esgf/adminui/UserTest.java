package org.esgf.adminui;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.mock.web.MockHttpServletRequest;

public class UserTest {
    
    private MockHttpServletRequest request;
    private final static Logger LOG = Logger.getLogger(UserTest.class);
    
    @Before
    public void setUp() {
        LOG.debug("Setting up UserTest");
    }

    @Test
    public void testtoXML() {
        LOG.debug("Test toXML");
    }
    
    @Test
    public void testtoString() {
        LOG.debug("Test toString");
    }
    
    
    @After
    public void tearDown() {
        LOG.debug("Tearing down UserTest");
    }
    
}
