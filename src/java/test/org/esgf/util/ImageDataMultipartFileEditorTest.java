package org.esgf.util;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImageDataMultipartFileEditorTest {
    
    private final static Logger LOG = Logger.getLogger(ImageDataMultipartFileEditorTest.class);
   
    @Before
    public void setUp() {
        LOG.debug("Setting up ImageDataMultipartFileEditorTest");
    }
    
    @Test
    public void testsetValue() {
        LOG.debug("Test setValue()");
    }
    
    @Test
    public void testgetAsText() {
        LOG.debug("Test getAsText()");
    }
    
    
    @After
    public void tearDown() {
        LOG.debug("Tearing down ImageDataMultipartFileEditorTest");
    }
}
