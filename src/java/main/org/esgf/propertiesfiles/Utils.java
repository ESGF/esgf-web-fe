package org.esgf.propertiesfiles;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

public class Utils {
    
    private static String HELP_PROPERTIES_FILE = "/esg/config/help.properties";
    private static String FACET_PROPERTIES_FILE = "/esg/config/facets.properties";
    
    public static void removeAllProperties(String propertiesFile) {
        String [] facetTokens = null;
        
        Properties properties = new Properties();
        try {
            
            properties.load(new FileInputStream(propertiesFile));
        
            for(Object key : properties.keySet()) {

                properties.remove(key);
            }
            
        } catch(Exception e) {
            
            System.out.println("Problem removing all properties");
            e.printStackTrace();
            
        }
         
    }
    
    public static void createFacetPropertiesFile() {
        
        try{
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
        } catch(Exception e) {
            System.err.println("Error in creating properties file: " + e.getMessage());
        }
        //Create file with defaults
        
        
    }
    
    public static void createHelpPropertiesFile() {
        try{
            // Create file with defaults
            FileWriter fstream = new FileWriter(HELP_PROPERTIES_FILE);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("user_faq=0;ESGF User FAQ;http://www.esgf.org/wiki/ESGF_User_FAQ;User FAQ Question;Commonly asked questions from the ESGF user community.\n");
            out.write("user_registration=1;User registration at ESGF;http://www.esgf.org/wiki/ESGF_Data_Download;User registration at ESGF;A quick tutorial on how to register as a user and join scientific working groups.\n");
            out.write("data_download=2;Data download from ESGF;http://www.esgf.org/wiki/ESGF_Data_Download;Downloading data from ESGF;A descirption of how to extract data files from the ESGF portal.\n");
            out.write("web_interface=3;ESGF Web Interface User Guide;http://www.esgf.org/wiki/fe-user-guide;ESGF web portal question;A complete guide to using the ESGF web portal user interface.\n");
            out.write("web_search=4;ESGF Search User Guide;http://www.esgf.org/wiki/ESGF_Web_Search_User_Guide;ESGF Search Question;A short user guide to discovering data within ESGF.\n");
            out.write("CMIP5_question=5;CMIP5 questions;http://cmip-pcmdi.llnl.gov/cmip5;CMIP5 Question;Post scientific questions related to CMIP5.\n");
            //Close the output stream
            out.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error in creating help properties file: " + e.getMessage());
        }

    }
    
}
