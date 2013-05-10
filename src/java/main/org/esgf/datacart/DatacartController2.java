package org.esgf.datacart;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/datacartcontroller2")
public class DatacartController2 {

    public static void main(String [] args) {
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        
        Map<String,String> datacartMap = new HashMap<String,String>();
        datacartMap.put("dataset_id1", "dataset_id1");
        
        session.putValue("datacart", datacartMap);
        
        mockRequest.setSession(session);
        
        
        DatacartController2 dc = new DatacartController2();
        

        mockRequest.setParameter("dataset_id", "dataset_id1");
        System.out.println(dc.getDatacart(mockRequest, session));
        dc.deleteDatacart(mockRequest, session);
        System.out.println(dc.getDatacart(mockRequest, session));
        dc.updateDatacart(mockRequest, session);
        System.out.println(dc.getDatacart(mockRequest, session));
        
        System.out.println(dc.getAllDatacart(mockRequest, session));
        
        /*
        mockRequest.setParameter("dataset_id", "dataset_id1");
        
        System.out.println(dc.getDatacart(mockRequest, session));
        
        dc.deleteDatacart(mockRequest, session);

        System.out.println(dc.getDatacart(mockRequest, session));
        */
    }
    
    public DatacartController2() {
        
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/datacartAll")
    public @ResponseBody Map<String,String> getAllDatacart(HttpServletRequest request,HttpSession session) {
        Enumeration e = session.getAttributeNames();
        while( e.hasMoreElements() ) {
            String key = (String) e.nextElement();
            if(key.equals("datacart")) {
                Map<String,String> datacartMap = (Map<String,String>)session.getAttribute(key);
                return datacartMap;
                
            } 
        }
        return new HashMap<String,String>();
        
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/datacart")
    public @ResponseBody String getDatacart(HttpServletRequest request,HttpSession session) {
    
        
        String dataset_id = request.getParameter("dataset_id");
        
        
        if(dataset_id == null) {
            return "error - dataset_id is null";
        } else {
            System.out.println("GET DATACART for dataset: " + dataset_id + "");
            Enumeration e = session.getAttributeNames();
            while( e.hasMoreElements() ) {
                String key = (String) e.nextElement();
                if(key.equals("datacart")) {
                    Map<String,String> datacartMap = (Map<String,String>)session.getAttribute(key);
                    for(Object datacartKey : datacartMap.keySet()) {
                        String datacartKeyStr = (String) datacartKey;
                        //String value = datacartMap.get(datacartKeyStr);
                        System.out.println("\tkey: " + datacartKeyStr);
                        if(datacartKeyStr.equals(dataset_id)) {
                            String value = datacartMap.get(datacartKeyStr);
                            return value;
                        }
                    }
                }
            }
            return "no datcart";
        }

        
        
    }
    
    @RequestMapping(method=RequestMethod.DELETE, value="/datacart")
    public @ResponseBody void deleteDatacart(HttpServletRequest request,HttpSession session) {
        
        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id != null) {
            System.out.println("DELETING DATACART entry");
            
            Enumeration e = session.getAttributeNames();
            while( e.hasMoreElements() ) {
                String key = (String) e.nextElement();
                if(key.equals("datacart")) {
                    Map<String,String> datacartMap = (Map<String,String>)session.getAttribute(key);
                    for(Object datacartKey : datacartMap.keySet()) {
                        String datacartKeyStr = (String) datacartKey;
                        System.out.println("\tkey: " + datacartKeyStr);
                        if(datacartKeyStr.equals(dataset_id)) {
                            String value = datacartMap.get(datacartKeyStr);
                            datacartMap.remove(datacartKeyStr);
                            session.setAttribute("datacart", datacartMap);
                        }
                    }
                    //return "not found";
                }
            }
            
        }
        
        //HttpSession session = request.getSession();
        
        
        
        
    }
    
    @RequestMapping(method=RequestMethod.POST, value="/datacart")
    public @ResponseBody void updateDatacart(HttpServletRequest request,HttpSession session) {
    
        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id != null) {
            
            Map<String,String> datacartMap = new HashMap<String,String>();
            datacartMap.put(dataset_id, dataset_id);

            System.out.println("UPDATING DATACART with entry " + dataset_id);
            
            session.setAttribute("datacart", datacartMap);
            
        }
        
        
        
      
        
        
    }
}
