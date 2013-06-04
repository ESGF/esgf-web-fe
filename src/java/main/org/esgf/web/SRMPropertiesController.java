package org.esgf.web;

import org.esgf.propertiesreader.SRMProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/srmpropertiesproxy")
public class SRMPropertiesController {

    public SRMPropertiesController() {

        SRMProperties srm_properties = new SRMProperties();
        System.out.println("\n\n\n\n\nINITIALIZING SRM PROPERTIES\n\n\n\n\n");
        
    }
}
