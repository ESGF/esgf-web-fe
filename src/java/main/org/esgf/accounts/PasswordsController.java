package org.esgf.accounts;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/passwordsview")
public class PasswordsController {

    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView doPost(final HttpServletRequest request) {
        Map<String,Object> model = new HashMap<String,Object>();

        
        return new ModelAndView("passwordsview", model);
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView doGet(final HttpServletRequest request) {
        Map<String,Object> model = new HashMap<String,Object>();

        return new ModelAndView("passwordsview", model);
    }
    
}
