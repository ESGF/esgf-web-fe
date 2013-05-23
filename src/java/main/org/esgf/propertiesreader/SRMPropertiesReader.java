package org.esgf.propertiesreader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.esgf.srm.utils.SRMUtils;

public class SRMPropertiesReader extends PropertiesReader {
    
    
    //private Properties prop = new Properties();
    
    public static void main(String [] args) {
        //SRMPropertiesReader srm_props = new SRMPropertiesReader("SRM");
        PropertiesReaderFactory factory = new PropertiesReaderFactory();
        PropertiesReader srm_props = factory.makePropertiesReader("SRM");
        
        System.out.println(srm_props.getValue("srm_api_url"));
    }

    
    
    public SRMPropertiesReader(String name) {
        this.name = name;
        this.prop = new Properties();
        
        System.out.println("No properties file...use defaults and post to location");

        /*
        prop.setProperty("bestman_expiration", SRMPropertiesConstants.expiration);
        prop.setProperty("failure_message", SRMPropertiesConstants.failure_message);
        prop.setProperty("success_message", SRMPropertiesConstants.success_message);
        prop.setProperty("srm_api_url", SRMPropertiesConstants.srmAPIURL);
        prop.setProperty("srm_db_name", SRMPropertiesConstants.db_name);
        prop.setProperty("srm_table_name", SRMPropertiesConstants.table_name);
        prop.setProperty("srm_valid_user", SRMPropertiesConstants.valid_user);
        prop.setProperty("srm_valid_password", SRMPropertiesConstants.valid_password);
        */
        
        try {
            //load a properties file
            prop.load(new FileInputStream(SRMUtils.SRM_PROPERTIES_FILE_LOCATION));
            
        } catch(Exception e) {
            
            System.out.println("No properties file...use defaults and post to location");

            prop.setProperty("bestman_expiration", SRMPropertiesConstants.expiration);
            prop.setProperty("failure_message", SRMPropertiesConstants.failure_message);
            prop.setProperty("success_message", SRMPropertiesConstants.success_message);
            prop.setProperty("srm_api_url", SRMPropertiesConstants.srmAPIURL);
            prop.setProperty("srm_db_name", SRMPropertiesConstants.db_name);
            prop.setProperty("srm_table_name", SRMPropertiesConstants.table_name);
            prop.setProperty("srm_valid_user", SRMPropertiesConstants.valid_user);
            prop.setProperty("srm_valid_password", SRMPropertiesConstants.valid_password);
            
            
            try {
                prop.store(new FileOutputStream(SRMUtils.SRM_PROPERTIES_FILE_LOCATION), null);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            
        }
        
    }
    
    
    
    public String getValue(String key) {
        return this.prop.getProperty(key);
    }
    
    public void printProps() {
        for(Object key : this.prop.keySet()) {
            
            String keyStr = (String) key;
            String value = this.prop.getProperty(keyStr);
            System.out.println("key: " + key + " value: " + value);
            
        }
    }
    
    public Properties getProp() {
        return prop;
    }



    public void setProp(Properties prop) {
        this.prop = prop;
    }

    
    
    
}
