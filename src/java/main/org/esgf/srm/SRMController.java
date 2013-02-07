package org.esgf.srm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value="/srmview")
public class SRMController {

    @SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView doPost(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        
        System.out.println("In SRM Controller");
        
        String datasetId = request.getParameter("datasetId");
        String type = request.getParameter("type");
        String s_url = request.getParameter("s_url");
        
        Map<String,Object> model = new HashMap<String,Object>();

        model.put("datasetId", datasetId);
        model.put("type", type);
        model.put("s_url", s_url);
        
        /*
        String datasetId = request.getParameter("datasetId");
        String type = request.getParameter("type");
        
        
        Map<String,Object> model = new HashMap<String,Object>();

        model.put("datasetId", datasetId);
        model.put("type", type);
        model.put("peerStr", peerStr);
        model.put("technoteStr", technoteStr);
        model.put("fqParamStr", fqParamStr);
        */
        return new ModelAndView("srmview", model);
        
    }
    
}
