package org.esgf.web;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/live")

public class LiveSearchController {

    private final static Logger LOG = Logger.getLogger(LiveSearchController.class);
    
}
