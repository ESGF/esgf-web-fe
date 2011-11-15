package org.esgf.adminui;

import org.apache.log4j.Logger;
import org.esgf.commonui.CommonUIUtilsTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.mock.web.MockHttpServletRequest;

public class GroupTest {
    
    private MockHttpServletRequest request;
    
    private Group group1, group2, groupIdNull, groupNameNull, groupDescriptionNull;
    
    private final static Logger LOG = Logger.getLogger(GroupTest.class);
    
    @Before
    public void setUp() {
        LOG.debug("Setting up GroupTest");
        group1 = new Group("id1", "name1", "description1");
        group2 = new Group("id2", "name2", "description2");
        groupIdNull = new Group(null,"name1", "description1");
        groupNameNull = new Group("id1",null, "description1");
        groupDescriptionNull = new Group("id1","name1", null);
    }
    
    
    
    @Test
    public void toXMLTest() {
        LOG.debug("Test toXML()");
    }

    @Test
    public void toStringTest() {
        LOG.debug("Test toString()");
    }
    
    
    @After
    public void tearDown() {
        LOG.debug("Tearing down GroupTest");
    }
    
    public static void main(String[] args) {

    }
    
}
