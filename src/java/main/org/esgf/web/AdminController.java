package org.esgf.web;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final static Logger LOG = Logger.getLogger(AdminController.class);


    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {

        LOG.debug("--> admin index");
        return "admin";
    }
}
