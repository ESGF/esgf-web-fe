package org.esgf.globusonline;

import javax.servlet.http.HttpServletRequest;

import org.esgf.metadata.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/gosources")
public class GOSourcesController {

    /**
     * Function doGet
     * 
     * Entry point into the FileDownloadController
     * 
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String doGet(HttpServletRequest request) throws JSONException {
    
        System.out.println("\t\t\tIn GOSourcesController");
        
        
        return "hello";
    }
    
}
