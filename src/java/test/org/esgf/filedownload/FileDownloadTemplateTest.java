package org.esgf.filedownload;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.mock.web.MockHttpServletRequest;

public class FileDownloadTemplateTest {
    
    private MockHttpServletRequest request;
    private final static Logger LOG = Logger.getLogger(FileDownloadTemplateTest.class);
    
    @Before
    public void setUp() {
        LOG.debug("Setting up FileDownloadTemplateTest");
    }
    
    @Test
    public void testdoGet() {
        LOG.debug("Test doGet()");
    }
    
    @Test
    public void testconvertTemplateFormatV2() {
        LOG.debug("Test convertTemplateFormatV2()");
    }
    
    @Test
    public void testconvertTemplateFormatV1() {
        LOG.debug("Test convertTemplateFormatV1()");
        
    }
    
    @Test
    public void testcreateInitialFileElement() {
        LOG.debug("Test createInitialFileElement()");
    }
    
    @Test
    public void testgetResponseBody() {
        LOG.debug("Test getResponseBody()");
    }
    
    @Test
    public void testcreateFileElement() {
        LOG.debug("Test createFileElement()");
    }
    
    
    @After
    public void tearDown() {
        LOG.debug("Tearing down FileDownloadTemplateTest");
    }
    
}
