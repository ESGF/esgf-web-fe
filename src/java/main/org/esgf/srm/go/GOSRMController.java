package org.esgf.srm.go;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esgf.datacart.XmlFormatter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/gosrmview")
public class GOSRMController {
    
    @SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView doGet(final HttpServletRequest request, HttpServletResponse response)  {
    
        Map<String,Object> model = new HashMap<String,Object>();

        
        
        
        String go_hash_id = request.getParameter("go_hash_id");
        if(go_hash_id == null) {
            go_hash_id = "aaa";
        }
        
        
        String fileName = "GOSRMObjList.xml";
        GOSRMObjList go_srm_obj_list = new GOSRMObjList(fileName);
        
        //from hash id, get the pamaters for go srm view
        GOSRMObj go_srm_obj = go_srm_obj_list.getGOSRMObj(go_hash_id);//new GOSRMObj(go_hash_id);
        
        
        model.put("go_hash_id", go_hash_id);
        model.put("go_child_url", go_srm_obj.getChild_url());
        model.put("go_child_id", go_srm_obj.getChild_id());
        model.put("go_type", go_srm_obj.getType());
        model.put("go_credential", go_srm_obj.getCredential());
        model.put("go_id", go_srm_obj.getId());

        System.out.println(new XmlFormatter().format(go_srm_obj.toXML()));
        
        return new ModelAndView("gosrmview", model);
        
    }
    
    
    private void scratch(final HttpServletRequest request) {
        
        System.out.println("In scratch");
        
        GOSRMObjList go_srm_list = new GOSRMObjList();
        
        GOSRMObj g1 = new GOSRMObj("g1");
        GOSRMObj g2 = new GOSRMObj("g2");
        
        go_srm_list.addGOSRMObj(g1);
        go_srm_list.addGOSRMObj(g2);
        
        //System.out.println(new XmlFormatter().format(go_srm_list.toXML()));
        
        //go_srm_list.toFile("GOSRMObjList.xml");
        
        go_srm_list.fromFile("GOSRMObjList.xml");
        
        System.out.println(new XmlFormatter().format(go_srm_list.toXML()));
        
        
    }
    
    public static void main(String [] args) {
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        final MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        mockRequest.addParameter("go_hash_id", "g1");
        
        GOSRMController controller = new GOSRMController();
        controller.doGet(mockRequest, mockResponse);
    }
    
}
