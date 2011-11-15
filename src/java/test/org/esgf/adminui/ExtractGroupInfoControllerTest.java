package org.esgf.adminui;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.mock.web.MockHttpServletRequest;

public class ExtractGroupInfoControllerTest {
    
    private MockHttpServletRequest request;
    private final static Logger LOG = Logger.getLogger(ExtractGroupInfoControllerTest.class);
    
    @Before
    public void setUp() {
        LOG.debug("Setting up ExtractGroupInfoControllerTest");
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
    public void testprocessGetAllUsersInGroupType() {
        LOG.debug("Test processGetAllUsersInGroupType()");
    }

    @Test
    public void testprocessGetGroupInfoType() {
        LOG.debug("Test processGetGroupInfoType()");
    }

    @Test
    public void testprocessgetGroupsForUserType() {
        LOG.debug("Test processgetGroupsForUserType()");
    }

    @Test
    public void testprocessEditType() {
        LOG.debug("Test processEditType()");
    }
    
    @Test
    public void testqueryStringInfo() {
        LOG.debug("Test queryStringInfo()");
    }
    
    
    @After
    public void tearDown() {
        LOG.debug("Tearing down ExtractGroupInfoControllerTest");
        
    }
    
}
