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
public class HelpPropertiesController {

    private final static String HELP_PROPERTIES_FILE = "/esg/config/help.properties";
    private final static String HELP_PRACTICE_FILE = "/esg/config/help_prac.properties";

    private final static String HELP_SUBJECT_IDS = "Help_Subject_Ids";
    private final static String HELP_HEADER_NAMES = "Help_Header_Names";
    private final static String HELP_HEADER_LINKS = "Help_Header_Links";
    private final static String HELP_EMAILS = "Help_Emails";
    private final static String HELP_DESCRIPTIONS = "Help_Descriptions";
    
    
    public static void main(String [] args) {

        //subject_id=0;Header Name;Header Link;Email;Description
        /*
        String [] helps = getHelpInfo();

        String [] subject_ids = new String [helps.length];
        String [] header_names = new String [helps.length];
        String [] header_links = new String [helps.length];
        String [] emails = new String [helps.length];
        String [] descriptions = new String [helps.length];
        
        for(int i=0;i<helps.length;i++) {
            
            String [] helpTokens = helps[i].split(";");
            
            String subject_id = helpTokens[0];
            subject_ids[i] = subject_id;
            
            String header_name = helpTokens[1];
            header_names[i] = header_name;

            String header_link = helpTokens[2];
            header_links[i] = header_link;

            String email = helpTokens[3];
            emails[i] = email;

            String description = helpTokens[4];
            descriptions[i] = description;
        }    
        */
    }
    
    @RequestMapping(method=RequestMethod.GET,value="/helpproperties")
    public ModelAndView getUIProperties() {
    
        if(!(new File(HELP_PROPERTIES_FILE)).exists()) {
            System.out.println("File Doesn't exist - create one here");
            createPropertiesFile();
        }
        
        Map<String,Object> model = new HashMap<String,Object>();
        

        String [] helps = getHelpInfo();
        
        String [] subject_ids = new String [helps.length];
        String [] header_names = new String [helps.length];
        String [] header_links = new String [helps.length];
        String [] emails = new String [helps.length];
        String [] descriptions = new String [helps.length];
        
        for(int i=0;i<helps.length;i++) {
            
            String [] helpTokens = helps[i].split(";");
            
            String subject_id = helpTokens[0];
            subject_ids[i] = subject_id;
            
            String header_name = helpTokens[1];
            header_names[i] = header_name;

            String header_link = helpTokens[2];
            header_links[i] = header_link;

            String email = helpTokens[3];
            emails[i] = email;

            String description = helpTokens[4];
            descriptions[i] = description;
            
        }
        

        model.put(HELP_SUBJECT_IDS, subject_ids);
        model.put(HELP_HEADER_NAMES, header_names);
        model.put(HELP_HEADER_LINKS, header_links);
        model.put(HELP_EMAILS, emails);
        model.put(HELP_DESCRIPTIONS, descriptions);
        
        return new ModelAndView("uiproperties/helpproperties/helpproperties",model);
    
    }


    
    @RequestMapping(method=RequestMethod.POST, value="/helpproperties/update")
    public @ResponseBody void doUpdatePost(HttpServletRequest request) throws IOException, ParserConfigurationException, JSONException {
        
        String [] help_subject_ids = request.getParameterValues("help_subject_ids[]");
        String [] help_header_names = request.getParameterValues("help_header_names[]");
        String [] help_header_links = request.getParameterValues("help_header_links[]");
        String [] help_emails = request.getParameterValues("help_emails[]");
        String [] help_descriptions = request.getParameterValues("help_descriptions[]");
        
        Utils.removeAllProperties(HELP_PROPERTIES_FILE);
        
        updateESGFUIPropertiesFile(help_subject_ids,help_header_names,help_header_links,help_emails,help_descriptions);

    }
    
    
    @RequestMapping(method=RequestMethod.POST, value="/helpproperties/restore")
    public @ResponseBody void doRestorePost(HttpServletRequest request) throws IOException, ParserConfigurationException, JSONException {
        
        Utils.removeAllProperties(HELP_PROPERTIES_FILE);
        
        createPropertiesFile();
        
    }
    
    
    
    public static void createPropertiesFile() {
        
        //need to create the file with those properties
          try{
              // Create file 
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
              System.err.println("Error: " + e.getMessage());
          }
      }
    
    public static void updateESGFUIPropertiesFile(String [] help_subject_ids,
                                                  String [] help_header_names,
                                                  String [] help_header_links,
                                                  String [] help_emails,
                                                  String [] help_descriptions) {

        /*
        for(int i=0;i<help_subject_ids.length;i++) {
            System.out.println("i: " + help_subject_ids[i] + " " + help_descriptions[i]);
        }
        */
        
        try {
    
            //need to create the file with those properties
            try{
                FileWriter fstream = new FileWriter(HELP_PROPERTIES_FILE);
                BufferedWriter out = new BufferedWriter(fstream);
    
                for(int i=0;i<help_subject_ids.length;i++) {
                    String str = help_subject_ids[i] + "=" + i + ";" + help_header_names[i] + ";" + help_header_links[i] + ";" + help_emails[i] + ";" + help_descriptions[i] + "\n";
                    
                    out.write(str);
                }  
                //Close theVal output stream
                out.close();
            }catch (Exception e){//Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    
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
                
                //System.out.println("value: " + value);
                
                String [] valueTokens = value.split(";");
                //System.out.println("ValTokLen: " + valueTokens.length);
                try {
                    int index = Integer.parseInt(valueTokens[0]);
                    
                    if(index < properties.size()) {

                        String facetInfo = (String)key + ";" + valueTokens[1] + ";" + valueTokens[2] + ";" + valueTokens[3] + ";" + valueTokens[4];
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
        

        
    }
    
    
    
}
