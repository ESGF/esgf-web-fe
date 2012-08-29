package org.esgf.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.esgf.metadata.JSONException;
import org.esgf.uiproperties.UIProperties;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/uiproperties")
public class UIPropertiesController {

  //private final static String UI_PROPERTIES_FILE = "/Users/8xo/software/tomcat/apache-tomcat-6.0.35/bin/esgf.ui.properties";
  private final static String UI_PROPERTIES_FILE = "/Users/8xo/esg/config/esgf.ui.properties";
    
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String doGet(HttpServletRequest request) {
        
        System.out.println("In do get");
        
        Properties properties = new Properties();
        String propertiesFile = UI_PROPERTIES_FILE;
        
        boolean createDefault = false;
        
        UIProperties uiproperties = new UIProperties();
        
        try {
            
            properties.load(new FileInputStream(propertiesFile));
            
       
            
            for(Object key : properties.keySet()) {
                String keyStr = (String) key;
                String value = properties.getProperty(keyStr);
                System.out.println(keyStr + " " + value);
                
                if(keyStr.equals("enableGlobusOnline")) {
                    uiproperties.setEnableGlobusOnline(value);
                } else if(keyStr.equals("lasrestrictions")) {
                    System.out.println(value.split(";").length);
                    String [] valueArr = value.split(";");
                    List<String> lasRestrictionList = new ArrayList<String>();
                    for(int i=0;i<valueArr.length;i++) {
                        lasRestrictionList.add(valueArr[i]);
                    }
                    uiproperties.setLasExclusions(lasRestrictionList);
                } else if(keyStr.equals("datacartMax")) {
                    uiproperties.setDatacartMax(value);
                } else if(keyStr.equals("defaultFileCounter")) {
                    uiproperties.setDefaultFileCounter(value);
                } else if(keyStr.equals("defaultDatasetCounter")) {
                    uiproperties.setDefaultDatasetCounter(value);
                }
            }
        } catch(FileNotFoundException f) {
            
            System.out.println("Using default ui properties list");
            
            
        } catch(Exception e) {
        
            e.printStackTrace();
        }
        
        String json = uiproperties.toJSON();
        
        return json;
    }
    
    public static void main(String [] args) {
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        UIPropertiesController ui = new UIPropertiesController();
        
        
        System.out.println(ui.doGet(mockRequest));
        
    }
    
}
