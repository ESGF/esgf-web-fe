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

        Map<String,Object> model = new HashMap<String,Object>();

        String type = request.getParameter("type");
        model.put("type", type);
        if(type.equals("Dataset")) {

            String file_url = "N/A";
            model.put("file_url", file_url);
            
            String file_id = "N/A";
            model.put("file_id", file_id);
            
            String dataset_id = request.getParameter("dataset_id");
            model.put("dataset_id", dataset_id);
            
            String filtered = request.getParameter("filtered");
            model.put("filtered", filtered);
            
            
            String peerStr = request.getParameter("peerStr");
            model.put("peerStr", peerStr);
            
            String technoteStr = request.getParameter("technoteStr");
            model.put("technoteStr", technoteStr);
            
            String fqParamStr = request.getParameter("fqParamStr");
            model.put("fqParamStr", fqParamStr);
            
            String initialQuery = request.getParameter("initialQuery");
            model.put("initialQuery", initialQuery);
            
            String fileCounter = request.getParameter("fileCounter");
            model.put("fileCounter", fileCounter);        
            
            
            
        } else {
            

            String file_url = request.getParameter("file_url");
            model.put("file_url", file_url);
            
            String file_id = request.getParameter("file_id");
            model.put("file_id", file_id);

            String dataset_id = request.getParameter("dataset_id");
            model.put("dataset_id", dataset_id);
            
            String filtered = request.getParameter("filtered");
            model.put("filtered", filtered);
            
            model.put("peerStr", "N/A");
            model.put("technoteStr", "N/A");
            model.put("fqParamStr", "N/A");
            model.put("initialQuery", "N/A");
            model.put("fileCounter", "N/A");        
            
        }
        
        return new ModelAndView("srmview", model);
        
    }
    
}
