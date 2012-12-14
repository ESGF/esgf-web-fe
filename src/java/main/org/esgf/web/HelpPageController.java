package org.esgf.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("unused")
@Controller
@RequestMapping(value="/help")
public class HelpPageController {

    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) {
     
        Map<String,Object> model = new HashMap<String,Object>();

        //System.out.println("help page");
    
        return new ModelAndView("help/help",model);
    }
}
