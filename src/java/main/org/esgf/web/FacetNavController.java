package org.esgf.web;

/**
 * Author: fwang2@ornl.gov
 * 
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;




@Controller
@RequestMapping(value="/browse")

public class FacetNavController {

    private final static Logger LOG = Logger.getLogger(FacetNavController.class);
    
    @RequestMapping(method=RequestMethod.GET)
    public String index(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Enter index");
        return "browse";
    }
}
