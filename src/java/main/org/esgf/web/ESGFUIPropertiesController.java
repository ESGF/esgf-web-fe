package org.esgf.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.esgf.metadata.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value="/uiproperties")
public class ESGFUIPropertiesController {
    
    
    private final static String FACET_PROPERTIES_FILE = "/esg/config/facets.properties";
    private final static String FACET_PRACTICE_FILE = "/esg/config/facets_prac.properties";
    
    
    private final static String FACET_SHORT_NAMES = "Facet_Short_Names";
    private final static String FACET_LONG_NAMES = "Facet_Long_Names";
    private final static String FACET_DESCRIPTIONS = "Facet_Descriptions";
    

    @RequestMapping(method=RequestMethod.POST, value="/update")
    public @ResponseBody void doGet(HttpServletRequest request) throws IOException, ParserConfigurationException, JSONException {
    
        
        /*
        System.out.println("Printing query string");
        for(Object key : request.getParameterMap().keySet()) {
            String keyStr = (String) key;
            
            System.out.println("keyStr: " + keyStr);
        }
        */
        
        removeAllProperties();
        
        String [] short_names = request.getParameterValues("short_names[]");
        String [] long_names = request.getParameterValues("long_names[]");
        String [] descriptions = request.getParameterValues("descriptions[]");
        
        /*
        for(int i=0;i<short_names.length;i++) {
            System.out.println("Short names: " + short_names[i]);
        }
        
        
        System.out.println("In do get");
        */
        
        removeAllProperties();
        
        updateESGFUIPropertiesFile(short_names,long_names,descriptions);
        
    }
    
    public static void removeAllProperties() {
        String [] facetTokens = null;
        
        Properties properties = new Properties();
        String propertiesFile = FACET_PRACTICE_FILE;
        try {
            
            properties.load(new FileInputStream(propertiesFile));
        
            for(Object key : properties.keySet()) {

                properties.remove(key);
            }
            
        } catch(Exception e) {
        }
         
    }
    
    public static void updateESGFUIPropertiesFile(String [] short_names,
                                           String [] long_names,
                                           String [] descriptions) {
        
        String [] facetTokens = null;
        
        Properties properties = new Properties();
        String propertiesFile = FACET_PRACTICE_FILE;
        
        
        try {
            
            properties.load(new FileInputStream(propertiesFile));
            
            facetTokens = new String [properties.size()];
            
            
            for(int i=0;i<short_names.length;i++) {
                System.out.println("Processing: " + short_names[i]);
                
                boolean found = false;
                
                String key = short_names[i];
                String value = Integer.toString(i) + ":" + long_names[i] + ":" + descriptions[i];
                properties.setProperty(key, value);
                
            }
            
            OutputStream out = new FileOutputStream( new File(FACET_PRACTICE_FILE));

            properties.store(out, "");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView getUIProperties() {
        Map<String,Object> model = new HashMap<String,Object>();

        //System.out.println("help page");
        
        String [] facets = getFacetInfo();

        String [] facet_short_names = new String [facets.length];
        String [] facet_long_names = new String [facets.length];
        String [] facet_descriptions = new String [facets.length];
        
        for(int i=0;i<facets.length;i++) {
            String [] facetTokens = facets[i].split(":");
            
            String shortName = facetTokens[0];
            facet_short_names[i] = shortName;
            System.out.println("ShortName: " + shortName);
            
            String longName = facetTokens[1];
            facet_long_names[i] = longName;
            
            String description = facetTokens[2];
            facet_descriptions[i] = description;
        }

        model.put(FACET_SHORT_NAMES, facet_short_names);
        model.put(FACET_LONG_NAMES, facet_long_names);
        model.put(FACET_DESCRIPTIONS, facet_descriptions);
        

        return new ModelAndView("uiproperties/uiproperties",model);
        
    }
    
    public static void main(String [] args) {
        
        String [] facets = getFacetInfo();
        
        for(int i=0;i<facets.length;i++) {
            System.out.println("i: " + i + " facets: " + facets[i]);
        }
        
    }
    
    
    
    /**
     * New helper method for extracting facet info from the facets.properties file in /esg/config
     * 
     * @return
     */
    private static String [] getFacetInfo() {
        

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
            facetTokens = getDefaultFacets();
            
        } catch(Exception e) {
        
            e.printStackTrace();
        }
        
        if(createDefault) {
            System.out.println("Using default facet list");
            facetTokens = getDefaultFacets();
        }
        
        return facetTokens;
    }
    
    private static String [] getDefaultFacets() {
        
        String [] facets = new String[13];
        facets[0] = "project:Project:optional_project_description";
        facets[1] = "institute:Institute:optional_institute_description";
        facets[2] = "model:Model:optional_model_description";
        facets[3] = "source_id:Instrument:optional_instrument_description";
        facets[4] = "experiment_family:Experiment Family:optional_experiment_family_description";
        facets[5] = "time_frequency:Time Frequency:optional_time_frequency_description";
        facets[6] = "product:Product:optional_product_description";
        facets[7] = "realm:Realm:optional_realm_description";
        facets[8] = "variable:Variable:optional_variable_description";
        facets[9] = "variable_long_name:Variable Long Name:optional_variable_long_name_description";
        facets[10] = "cmor_table:CMIP Table:optional_cmor_table_description";
        facets[11] = "cf_standard_name:CF Standard Name:optional_cf_standard_name_description";
        facets[12] = "ensemble:Ensemble:optional_ensemble_description";
        
        
        
        return facets;
        
    }

    
    
    
}
