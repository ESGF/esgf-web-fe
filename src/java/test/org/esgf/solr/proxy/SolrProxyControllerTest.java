package org.esgf.solr.proxy;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.mock.web.MockHttpServletRequest;

public class SolrProxyControllerTest {
    
    private MockHttpServletRequest request;
    private final static Logger LOG = Logger.getLogger(SolrProxyControllerTest.class);
    
    @Before
    public void setUp() {
        LOG.debug("Setting up SolrProxyControllerTest");
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
    public void testRelay() {
        LOG.debug("Test relay()");
        
    }
    
    @After
    public void tearDown() {
        LOG.debug("Tearing down SolrProxyControllerTest");
    }
    
}
