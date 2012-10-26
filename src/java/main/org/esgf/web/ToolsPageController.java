package org.esgf.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/tools")
public class ToolsPageController {

    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) {
     
        Map<String,Object> model = new HashMap<String,Object>();

        //System.out.println("tools page");
    
        return new ModelAndView("tools/tools",model);
    }
}

