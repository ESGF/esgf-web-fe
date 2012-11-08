package org.esgf.uiproperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HelpPropertiesController {

    private final static String HELP_PROPERTIES_FILE = "/esg/config/help.properties";
    private final static String HELP_PRACTICE_FILE = "/esg/config/help_prac.properties";
    
    
    @RequestMapping(method=RequestMethod.GET,value="/helpproperties")
    public ModelAndView getUIProperties() {
    
        Map<String,Object> model = new HashMap<String,Object>();
        

        String [] helps = getHelpInfo();
        
        for(int i=0;i<helps.length;i++) {
            
            System.out.println("help: " + i + " " + helps[i]);
        }
        
        return new ModelAndView("uiproperties/helpproperties/helpproperties",model);
    
    }


    private static String[] getHelpInfo() {
        
        String [] helpTokens = null;
        
        Properties properties = new Properties();
        String propertiesFile = HELP_PROPERTIES_FILE;
        
        boolean createDefault = false;
        
        try {
            
            properties.load(new FileInputStream(propertiesFile));
            
            helpTokens = new String [properties.size()];
            
            for(Object key : properties.keySet()) {
                String value = (String)properties.get(key);
                
                String [] valueTokens = value.split(":");
                try {
                    int index = Integer.parseInt(valueTokens[0]);
                    
                    if(index < properties.size()) {

                        String facetInfo = (String)key + ":" + valueTokens[1] + ":" + valueTokens[2];
                        helpTokens[index] = facetInfo;
                    }
                    
                } 
                //Note: need to fix this.  This will only work if ALL facet readings are wrong
                catch(Exception e) {
                    System.out.println("COULD NOT INDEX: " + key);

                    createDefault = true;
                    
                }
                
            }
            
            
        } catch(FileNotFoundException f) {
            
            System.out.println("Using default facet list");
            //facetTokens = getDefaultFacets();
            
        } catch(Exception e) {
        
            e.printStackTrace();
        }
        
        if(createDefault) {
            System.out.println("Using default facet list");
            //facetTokens = getDefaultFacets();
        }
        
        return helpTokens;
        
        /*
        String [] facetTokens = null;
        
        Properties properties = new Properties();
        String propertiesFile = FACET_PROPERTIES_FILE;
        
        boolean createDefault = false;
        
        try {
            
            properties.load(new FileInputStream(propertiesFile));
            
            facetTokens = new String [properties.size()];
            
            for(Object key : properties.keySet()) {
                String value = (String)properties.get(key);
                
                String [] valueTokens = value.split(":");
                try {
                    int index = Integer.parseInt(valueTokens[0]);
                    
                    if(index < properties.size()) {

                        String facetInfo = (String)key + ":" + valueTokens[1] + ":" + valueTokens[2];
                        facetTokens[index] = facetInfo;
                    }
                    
                } 
                //Note: need to fix this.  This will only work if ALL facet readings are wrong
                catch(Exception e) {
                    System.out.println("COULD NOT INDEX: " + key);

                    createDefault = true;
                    
                }
                
            }
            
            List<String> fixedFacetTokens = new ArrayList<String>();
            
            //"fix" the array here (for any index collisions
            for(int i=0;i<facetTokens.length;i++) {
                if(facetTokens[i] != null) {
                    fixedFacetTokens.add(facetTokens[i]);
                }
            }
            
            facetTokens = (String [])fixedFacetTokens.toArray(new String[fixedFacetTokens.size()] );
            
        } catch(FileNotFoundException f) {
            
            System.out.println("Using default facet list");
            //facetTokens = getDefaultFacets();
            
        } catch(Exception e) {
        
            e.printStackTrace();
        }
        
        if(createDefault) {
            System.out.println("Using default facet list");
            //facetTokens = getDefaultFacets();
        }
        
        return facetTokens;
        */
        

        
    }
    
}
