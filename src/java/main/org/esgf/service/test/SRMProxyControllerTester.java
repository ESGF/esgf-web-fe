package org.esgf.service.test;

import java.util.Scanner;

import org.esgf.srm.SRMProxyController;
import org.esgf.srm.utils.SRMUtils;
import org.springframework.mock.web.MockHttpServletRequest;

public class SRMProxyControllerTester {

    private static String DATASET_NODE = "|esg2-sdnl1.ccs.ornl.gov"; 
    private static String BESTMAN_SURL = "";
    
    public static String INPUT_OPEN_ID = "https://esg.ccs.ornl.gov/esgf-idp/openid/kjchrebet";
    
    public static void main(String [] args) {
        
        
        
        /*
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        
        if(answer.equalsIgnoreCase("test1")) {
            test1();
        } else if(answer.equalsIgnoreCase("test2")) {
            test2();
        } else if(answer.equalsIgnoreCase("test3")) {
            test3();
        } else if(answer.equalsIgnoreCase("test4")) {
            test4();
        } else if(answer.equalsIgnoreCase("test5")) {
            test5();
        } else if(answer.equalsIgnoreCase("test6")) {
            test6();
        } else {
            test7();
        }
        */
        
        test6();
        
        
    }
    
    // test 7 - scenario for file and using the method and wget
    private static void test7() {
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        String dataset_id = "ornl.ultrahighres.CESM1.t85f09.B1850_50yrs.atmos.v1" + DATASET_NODE;
        String file_id = "ornl.ultrahighres.CESM1.t85f09.B1850_50yrs.atmos.v1.t85f09.B1850.cam2.h0.0001-11.nc" + DATASET_NODE;
        mockRequest.addParameter("dataset_id", dataset_id);
        mockRequest.addParameter("constraints", SRMUtils.INPUT_CONSTRAINTS);
        mockRequest.addParameter("file_id", file_id);
        mockRequest.addParameter("file_url", "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli048/UHRGCS/ORNL/CESM1/t85f09.B1850_50yrs/atm/hist/t85f09.B1850.cam2.h0.0001-11.nc");
        mockRequest.addParameter("open_id", INPUT_OPEN_ID);
        mockRequest.addParameter("type", "File");
        mockRequest.addParameter("scriptType", "WGET");
        
        SRMProxyController fc = new SRMProxyController();
        
        String response = fc.doPost(mockRequest, null);
        
    }
    
    
    
    
    // test 6 - scenario for dataset and using the method and wget on node
    private static void test6() {
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        String dataset_id = "ornl.ultrahighres.CESM1.t85f09.B1850_50yrs.atmos.v1" + DATASET_NODE;
        String file_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1.t341f02.FAMIPr.cam2.h0.1978-12.nc" + DATASET_NODE;
        
        mockRequest.addParameter("dataset_id", dataset_id);
        mockRequest.addParameter("constraints", SRMUtils.INPUT_CONSTRAINTS);
        mockRequest.addParameter("file_id", file_id);
        mockRequest.addParameter("file_url", "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-10.nc");
        mockRequest.addParameter("open_id", INPUT_OPEN_ID);
        mockRequest.addParameter("type", "Dataset");
        mockRequest.addParameter("scriptType", "WGET");
        
        
        SRMProxyController fc = new SRMProxyController();
        
        String response = fc.doPost(mockRequest, null);
    }
    
    
    
    
    
    
    
 // test 5 - scenario for dataset and using the method and globus url copy on node
    private static void test5() {
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        String dataset_id = "ornl.ultrahighres.CESM1.t85f09.B1850_50yrs.atmos.v1" + DATASET_NODE;
        String file_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1.t341f02.FAMIPr.cam2.h0.1978-12.nc" + DATASET_NODE;
        
        mockRequest.addParameter("dataset_id", dataset_id);
        mockRequest.addParameter("constraints", SRMUtils.INPUT_CONSTRAINTS);
        mockRequest.addParameter("file_id", file_id);
        mockRequest.addParameter("file_url", "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-10.nc");
        mockRequest.addParameter("open_id", INPUT_OPEN_ID);
        mockRequest.addParameter("type", "Dataset");
        mockRequest.addParameter("scriptType", "GUC");
        
        SRMProxyController fc = new SRMProxyController();
        
        String response = fc.doPost(mockRequest, null);
    }
    
 // test 4 - scenario for dataset and using the method and globus url copy
    private static void test4() {
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        String dataset_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1" + DATASET_NODE;
        String file_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1.t341f02.FAMIPr.cam2.h0.1978-12.nc" + DATASET_NODE;
        
        mockRequest.addParameter("dataset_id", dataset_id);
        mockRequest.addParameter("constraints", SRMUtils.INPUT_CONSTRAINTS);
        mockRequest.addParameter("file_id", file_id);
        mockRequest.addParameter("file_url", "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-10.nc");
        mockRequest.addParameter("open_id", INPUT_OPEN_ID);
        mockRequest.addParameter("type", "Dataset");
        mockRequest.addParameter("scriptType", "GUC");
        
        SRMProxyController fc = new SRMProxyController();
        
        String response = fc.doPost(mockRequest, null);
    }
    
 // test 3 - scenario for dataset and using the method and wget
    private static void test3() {
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        String dataset_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1" + DATASET_NODE;
        String file_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1.t341f02.FAMIPr.cam2.h0.1978-12.nc" + DATASET_NODE;
        
        mockRequest.addParameter("dataset_id", dataset_id);
        mockRequest.addParameter("constraints", SRMUtils.INPUT_CONSTRAINTS);
        mockRequest.addParameter("file_id", file_id);
        mockRequest.addParameter("file_url", "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-10.nc");
        mockRequest.addParameter("open_id", INPUT_OPEN_ID);
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
        mockRequest.addParameter("open_id", INPUT_OPEN_ID);
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
        mockRequest.addParameter("open_id", INPUT_OPEN_ID);
        mockRequest.addParameter("type", "File");
        mockRequest.addParameter("scriptType", "WGET");
        
        SRMProxyController fc = new SRMProxyController();
        
        String response = fc.doPost(mockRequest, null);
        
    }
    
    
}
