package org.esgf.uiproperties;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
public class FacetPropertiesController {

    
    private final static String FACET_PROPERTIES_FILE = "/esg/config/facets.properties";
    private final static String FACET_PRACTICE_FILE = "/esg/config/facets_prac.properties";
    
    
    private final static String FACET_SHORT_NAMES = "Facet_Short_Names";
    private final static String FACET_LONG_NAMES = "Facet_Long_Names";
    private final static String FACET_DESCRIPTIONS = "Facet_Descriptions";
    
    
public static void main(String [] args) {
        
        String propertiesFile = FACET_PROPERTIES_FILE;
        
        String [] short_names = {"project111111",
                "institute",
                "model",
                "source_id",
                "experiment_family1",
                "time_frequency",
                "product",
                "realm",
                "variable",
                "variable_long_name",
                "cmor_table",
                "cf_standard_name",
                "ensemble"};

        String [] long_names = {"Project",
                "Institute",
                "Model",
                "Instrument",
                "Experiment Family",
                "Time Frequency",
                "Product",
                "Realm",
                "Variable",
                "Variable Long Name",
                "CMIP Table",
                "CF Standard Name",
                "Ensemble"};

        String [] descriptions = {"optional_project_description",
                "optional_institute_description",
                "optional_model_description",
                "optional_instrument_description",
                "optional_experiment_family_description",
                "optional_time_frequency_description",
                "optional_product_description",
                "optional_realm_description",
                "optional_variable_description",
                "optional_variable_long_name_description",
                "optional_cmor_table_description",
                "optional_cf_standard_name_description",
                "optional_ensemble_description"};

 
    }

    @RequestMapping(method=RequestMethod.GET,value="/facetproperties")
    public ModelAndView getUIProperties() {
        
        
        if(!(new File(FACET_PROPERTIES_FILE)).exists()) {
            createPropertiesFile();
        }
        
        Map<String,Object> model = new HashMap<String,Object>();
        
        String [] facets = getFacetInfo();

        String [] facet_short_names = new String [facets.length];
        String [] facet_long_names = new String [facets.length];
        String [] facet_descriptions = new String [facets.length];
        
        for(int i=0;i<facets.length;i++) {
            String [] facetTokens = facets[i].split(":");
            
            String shortName = facetTokens[0];
            facet_short_names[i] = shortName;
            
            String longName = facetTokens[1];
            facet_long_names[i] = longName;
            
            String description = facetTokens[2];
            facet_descriptions[i] = description;
        }

        model.put(FACET_SHORT_NAMES, facet_short_names);
        model.put(FACET_LONG_NAMES, facet_long_names);
        model.put(FACET_DESCRIPTIONS, facet_descriptions);
        
        return new ModelAndView("uiproperties/facetproperties/facetproperties",model);
        
    }
    
    
    public static void createPropertiesFile() {
      //need to create the file with those properties
        try{
            // Create file 
            FileWriter fstream = new FileWriter(FACET_PROPERTIES_FILE);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("project=0:Project:optional_project_description\n");
            out.write("institute=1:Institute:optional_institute_description\n");
            out.write("model=2:Model:optional_model_description\n");
            out.write("source_id=3:Instrument:optional_instrument_description\n");
            out.write("experiment_family=4:Experiment Family:optional_experiment_family_description\n");
            out.write("time_frequency=5:Time Frequency:optional_time_frequency_description\n");
            out.write("product=6:Product:optional_product_description\n");
            out.write("realm=7:Realm:optional_realm_description\n");
            out.write("variable=8:Variable:optional_variable_description\n");
            out.write("variable_long_name=9:Variable Long Name:optional_variable_long_name_description\n");
            out.write("cmor_table=10:CMIP Table:optional_cmor_table_description\n");
            out.write("cf_standard_name=11:CF Standard Name:optional_cf_standard_name_description\n");
            out.write("ensemble=12:Ensemble:optional_ensemble_description\n");
                  
            //Close the output stream
            out.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    @RequestMapping(method=RequestMethod.POST, value="/facetpropertie/update")
    public @ResponseBody void doPost(HttpServletRequest request) throws IOException, ParserConfigurationException, JSONException {
        
        String [] short_names = request.getParameterValues("short_names[]");
        String [] long_names = request.getParameterValues("long_names[]");
        String [] descriptions = request.getParameterValues("descriptions[]");
        
        Utils.removeAllProperties(FACET_PROPERTIES_FILE);
        
        updateESGFUIPropertiesFile(short_names,long_names,descriptions);

    }
    
    
    
    public static void updateESGFUIPropertiesFile(String [] short_names,
                                           String [] long_names,
                                           String [] descriptions) {
        
        try {
            
          //need to create the file with those properties
            try{
                FileWriter fstream = new FileWriter(FACET_PROPERTIES_FILE);
                BufferedWriter out = new BufferedWriter(fstream);
                
                for(int i=0;i<short_names.length;i++) {
                    String str = short_names[i] + "=" + i + ":" + long_names[i] + ":" + descriptions[i] + "\n";
                    out.write(str);
                }  
                //Close the output stream
                out.close();
            }catch (Exception e){//Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
           
        } catch (Exception e) {
            e.printStackTrace();
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
            //facetTokens = getDefaultFacets();
            
        } catch(Exception e) {
        
            e.printStackTrace();
        }
        
        if(createDefault) {
            System.out.println("Using default facet list");
            //facetTokens = getDefaultFacets();
        }
        
        return facetTokens;
    }
    
    
}
