package org.esgf.web;

/**
 * 
 * Homepage controller
 * 
 * Author: fwang2@ornl.gov
 *
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    private final static Logger LOG = Logger.getLogger(HomeController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String index(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Enter home");
        return "home";
    }
}
