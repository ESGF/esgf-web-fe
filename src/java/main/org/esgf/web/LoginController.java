package org.esgf.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller

public class LoginController {

    private final static Logger LOG = Logger.getLogger(LoginController.class);
    
    @RequestMapping(method=RequestMethod.GET, value="/login")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Enter index");
        return "login";
    }
}
