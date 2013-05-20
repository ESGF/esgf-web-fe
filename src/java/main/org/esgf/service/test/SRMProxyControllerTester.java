package org.esgf.service.test;

import org.esgf.srm.SRMProxyController;
import org.esgf.srm.utils.SRMUtils;
import org.springframework.mock.web.MockHttpServletRequest;

public class SRMProxyControllerTester {

    private static String DATASET_NODE = "|esg2-sdnl1.ccs.ornl.gov"; 
    private static String BESTMAN_SURL = "";
    
    public static void main(String [] args) {
        
        
        test1();
        
        
        
        
    }
    
    // test 2 - scenario for dataset and using the method and wget
    private static void test3() {
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        String dataset_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1" + DATASET_NODE;
        String file_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1.t341f02.FAMIPr.cam2.h0.1978-12.nc" + DATASET_NODE;
        
        mockRequest.addParameter("dataset_id", dataset_id);
        mockRequest.addParameter("constraints", SRMUtils.INPUT_CONSTRAINTS);
        mockRequest.addParameter("file_id", file_id);
        mockRequest.addParameter("file_url", "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-10.nc");
        mockRequest.addParameter("open_id", SRMUtils.INPUT_OPEN_ID);
        mockRequest.addParameter("type", "Dataset");
        mockRequest.addParameter("scriptType", "WGET");
        
        SRMProxyController fc = new SRMProxyController();
        
        String response = fc.doPost(mockRequest, null);
    }
    
    // test 2 - scenario for file and using the method and guc
    private static void test2() {
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        String dataset_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1" + DATASET_NODE;
        String file_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1.t341f02.FAMIPr.cam2.h0.1978-12.nc" + DATASET_NODE;
        
        mockRequest.addParameter("dataset_id", dataset_id);
        mockRequest.addParameter("constraints", SRMUtils.INPUT_CONSTRAINTS);
        mockRequest.addParameter("file_id", file_id);
        mockRequest.addParameter("file_url", "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-10.nc");
        mockRequest.addParameter("open_id", SRMUtils.INPUT_OPEN_ID);
        mockRequest.addParameter("type", "File");
        mockRequest.addParameter("scriptType", "GUC");
        
        SRMProxyController fc = new SRMProxyController();
        
        String response = fc.doPost(mockRequest, null);
        
    }
    
    // test 1 - scenario for file and using the method and wget
    private static void test1() {
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        String dataset_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1" + DATASET_NODE;
        String file_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1.t341f02.FAMIPr.cam2.h0.1978-12.nc" + DATASET_NODE;
        
        mockRequest.addParameter("dataset_id", dataset_id);
        mockRequest.addParameter("constraints", SRMUtils.INPUT_CONSTRAINTS);
        mockRequest.addParameter("file_id", file_id);
        mockRequest.addParameter("file_url", "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-10.nc");
        mockRequest.addParameter("open_id", SRMUtils.INPUT_OPEN_ID);
        mockRequest.addParameter("type", "File");
        mockRequest.addParameter("scriptType", "WGET");
        
        SRMProxyController fc = new SRMProxyController();
        
        String response = fc.doPost(mockRequest, null);
        
    }
    
    
}
