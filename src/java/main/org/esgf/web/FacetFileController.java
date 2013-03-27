/*****************************************************************************
 * Copyright 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * "Licensor") hereby grants to any person (the "Licensee") obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization's name.  If the Software is protected by a proprietary
 * trademark owned by Licensor or the Department of Energy, then derivative
 * works of the Software may not be distributed using the trademark without
 * the prior written approval of the trademark owner.
 *
 * 2. Neither the names of Licensor nor the Department of Energy may be used
 * to endorse or promote products derived from this Software without their
 * specific prior written permission.
 *
 * 3. The Software, with or without modification, must include the following
 * acknowledgment:
 *
 *    "This product includes software produced by UT-Battelle, LLC under
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy."
 *
 * 4. Licensee is authorized to commercialize its derivative works of the
 * Software.  All derivative works of the Software must include paragraphs 1,
 * 2, and 3 above, and the DISCLAIMER below.
 *
 *
 * DISCLAIMER
 *
 * UT-Battelle, LLC AND THE GOVERNMENT MAKE NO REPRESENTATIONS AND DISCLAIM
 * ALL WARRANTIES, BOTH EXPRESSED AND IMPLIED.  THERE ARE NO EXPRESS OR
 * IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE,
 * OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY PATENT, COPYRIGHT,
 * TRADEMARK, OR OTHER PROPRIETARY RIGHTS, OR THAT THE SOFTWARE WILL
 * ACCOMPLISH THE INTENDED RESULTS OR THAT THE SOFTWARE OR ITS USE WILL NOT
 * RESULT IN INJURY OR DAMAGE.  The user assumes responsibility for all
 * liabilities, penalties, fines, claims, causes of action, and costs and
 * expenses, caused by, resulting from or arising out of, in whole or in part
 * the use, storage or disposal of the SOFTWARE.
 *
 *
 ******************************************************************************/

package org.esgf.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.esgf.metadata.JSONArray;
import org.esgf.metadata.JSONException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 *
 *
 * @author john.harney
 *
 */
@Controller
@RequestMapping("/facetfileproxy")
public class FacetFileController {
    
    private final static Logger LOG = Logger.getLogger(FacetFileController.class);

    //Name and location of the file (In this case, the base package location)
    private final static String FACET_FILE = "facets.properties";
    private final static String FACET_LABEL_FILE = "longnames.properties";
    
    private final static String FACET_PROPERTIES_FILE = "/esg/config/facets.properties";
    
    /**
     * This method gives a response to a request (called by esgf-web-fe/web/scripts/esgf/solr.js) for the facets defined in the file facets.properties.  
     * The logic places all facets (delimited by a ; for the time being) into a json array, where it is parsed in solr.js
     * 
     * @return String Json representation of the facet array
     * 
     * @throws IOException
     * @throws JSONException
     * @throws ParserConfigurationException
     */
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String doGet() throws IOException, ParserConfigurationException, JSONException {
      
        String [] tokens = getFacetInfo();
        
        JSONArray facet_arrJSON = new JSONArray(tokens);
  
        String jsonContent = facet_arrJSON.toString();
  
        LOG.debug("doGet facetfileproxy");
  
        return jsonContent;
    } //end doGet

    
    /**
     * Helper method for assembling a facet array string from the file facets.properties.  
     * The file exists in the root package. It will return an empty array if the file does not exist.
     * 
     * @return String Json representation of the facet array
     * 
     */
    private String [] parseFacets(File file) {
        
        try {
            String fileContents = FileUtils.readFileToString(file);
            
            String delims = ";";
            String [] tokens = fileContents.split(delims);
            
            return tokens;
        } catch(Exception e) {
            LOG.debug("Could not find file: " + file.getName());
            return new String[0];		
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

    
    public static void main(String [] args) {
        
        String [] facets = getFacetInfo();
        
        
        for(int i=0;i<facets.length;i++) {
            System.out.println("facet: " + i + " " + facets[i]);
        }
        System.out.println(facets.length);
        
    }
    
    
    
    
}
