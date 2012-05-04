package org.esgf.legacydatacart;

import org.springframework.mock.web.MockHttpServletRequest;

public class Scenarios {

    /* Scenario 1
    - one from nasa, one from local (with technote)
    ["BEGIN DATACART CONTENTS"]
    log4javascript.js:156["ID: obs4MIPs.NASA-JPL.AIRS.mon,obs4MIPs.NASA-JPL.AIRS.mon.v1:esg-datanode.jpl.nasa.gov"]
    log4javascript.js:156["PEER: esg-datanode.jpl.nasa.gov;undefined"]
    log4javascript.js:156["SHOWALL: true"]
    log4javascript.js:156["FQ: ,offset=0,replica=false,project=obs4MIPs"]
    log4javascript.js:156["END DATACART CONTENTS"]
    
    */
    public static void runScenario1(){
        
        
        
        
        
        /*
        
        mockRequest.addParameter("id[]", id);
        mockRequest.addParameter("peer", peer);
        mockRequest.addParameter("showAll", showAll);
        mockRequest.addParameter("fq", fq);
        */
        
        

        

        /* SCenario 2
        - one from nasa, one from local (with technote), showAll is false

        ["BEGIN DATACART CONTENTS"]
        log4javascript.js:156["ID: obs4MIPs.NASA-JPL.AIRS.mon,obs4MIPs.NASA-JPL.AIRS.mon.v1:esg-datanode.jpl.nasa.gov"]
        log4javascript.js:156["PEER: esg-datanode.jpl.nasa.gov;undefined"]
        log4javascript.js:156["SHOWALL: false"]
        log4javascript.js:156["FQ: ,offset=0,replica=false,project=obs4MIPs"]
        log4javascript.js:156["END DATACART CONTENTS"]
        
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        String [] id = {"obs4MIPs.NASA-JPL.AIRS.mon.v1:esg-datanode.jpl.nasa.gov"};
        String peer = "undefined";
        String showAll = "false";
        String [] fq = {"variable=hus"};
        
        mockRequest.addParameter("id[]", id);
        mockRequest.addParameter("peer", peer);
        mockRequest.addParameter("showAll", showAll);
        mockRequest.addParameter("fq", fq);
        */
        

        /* Scenario 3
        - one from nasa, one from local (with technote), showAll is false, variable is hus

        ["BEGIN DATACART CONTENTS"]
        log4javascript.js:156["ID: obs4MIPs.NASA-JPL.AIRS.mon,obs4MIPs.NASA-JPL.AIRS.mon.v1:esg-datanode.jpl.nasa.gov"]
        log4javascript.js:156["PEER: esg-datanode.jpl.nasa.gov;undefined"]
        log4javascript.js:156["SHOWALL: false"]
        log4javascript.js:156["FQ: ,offset=0,replica=false,variable=hus"]
        log4javascript.js:156["END DATACART CONTENTS"]
        
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        String [] id = {"obs4MIPs.NASA-JPL.AIRS.mon.v1:esg-datanode.jpl.nasa.gov"};
        String peer = "undefined";
        String showAll = "false";
        String [] fq = {"variable=hus"};
        
        mockRequest.addParameter("id[]", id);
        mockRequest.addParameter("peer", peer);
        mockRequest.addParameter("showAll", showAll);
        mockRequest.addParameter("fq", fq);
        */
        
        

        /* Scenario 4
        - one from nasa, one from local (with technote), showAll is true, project is obs4MIPs, NO DATACART CONTENTS SELECTED

        ["BEGIN DATACART CONTENTS"]
        log4javascript.js:156["ID: "]
        log4javascript.js:156["PEER: "]
        log4javascript.js:156["SHOWALL: true"]
        log4javascript.js:156["FQ: ,offset=0,replica=false,query=*,project=obs4MIPs"]
        log4javascript.js:156["END DATACART CONTENTS"]
        
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        String [] id = {"obs4MIPs.NASA-JPL.AIRS.mon.v1:esg-datanode.jpl.nasa.gov"};
        String peer = "undefined";
        String showAll = "false";
        String [] fq = {"variable=hus"};
        
        mockRequest.addParameter("id[]", id);
        mockRequest.addParameter("peer", peer);
        mockRequest.addParameter("showAll", showAll);
        mockRequest.addParameter("fq", fq);
        */
        
    }
    
    
}
