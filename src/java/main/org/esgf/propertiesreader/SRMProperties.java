package org.esgf.propertiesreader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.esgf.srm.utils.SRMUtils;

public class SRMProperties {

    public static String srmAPIURL;
    public static String expiration;
    public static String success_message;
    public static String failure_message;
    public static String db_name;
    public static String table_name;
    public static String valid_user;
    public static String valid_password;
    /*
    private String srmAPIURL;
    private String expiration;
    private String success_message;
    private String failure_message;
    private String db_name;
    private String table_name;
    private String valid_user;
    private String valid_password;
    */
    
    /*
    public String getSrmAPIURL() {
        return srmAPIURL;
    }

    public void setSrmAPIURL(String srmAPIURL) {
        this.srmAPIURL = srmAPIURL;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getSuccess_message() {
        return success_message;
    }

    public void setSuccess_message(String success_message) {
        this.success_message = success_message;
    }

    public String getFailure_message() {
        return failure_message;
    }

    public void setFailure_message(String failure_message) {
        this.failure_message = failure_message;
    }

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getValid_user() {
        return valid_user;
    }

    public void setValid_user(String valid_user) {
        this.valid_user = valid_user;
    }

    public String getValid_password() {
        return valid_password;
    }

    public void setValid_password(String valid_password) {
        this.valid_password = valid_password;
    }
    */
    public SRMProperties() {
        
        
        this.defaults();
    }
    
    public SRMProperties(String fileName) {
        
        Properties properties = new Properties();
        FileInputStream fis = null;
        try {
            //load a properties file
            fis = new FileInputStream(fileName);
            properties.load(fis);
            
        } catch(Exception e) {
            
            System.out.println("No properties file...use defaults and post to location");

            expiration = (String) properties.get("bestman_expiration");
            srmAPIURL = (String) properties.getProperty("srm_api_url");
            success_message = (String)properties.getProperty("srm_db_name");
            failure_message = (String)properties.getProperty("srm_table_name");
            db_name = (String)properties.getProperty("srm_db_name");
            table_name = (String)properties.getProperty("srm_table_name");
            valid_user = (String)properties.getProperty("srm_valid_user");
            valid_password = (String)properties.getProperty("srm_valid_password");
            /*
            this.expiration = (String) properties.get("bestman_expiration");
            this.srmAPIURL = (String) properties.getProperty("srm_api_url");
            this.success_message = (String)properties.getProperty("srm_db_name");
            this.failure_message = (String)properties.getProperty("srm_table_name");
            this.db_name = (String)properties.getProperty("srm_db_name");
            this.table_name = (String)properties.getProperty("srm_table_name");
            this.valid_user = (String)properties.getProperty("srm_valid_user");
            this.valid_password = (String)properties.getProperty("srm_valid_password");
            */
            
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        
    }
    
    
    public void defaults() {

        srmAPIURL = SRMUtils.srmAPIURL;//"http://localhost:8080/esgf-srm/service/srmrequest?";
        expiration = SRMUtils.expiration;//"86400000";
        success_message = SRMUtils.success_message;//"success";
        failure_message = SRMUtils.failure_message;//"failure";
        db_name = SRMUtils.db_name;//"esgcet";
        table_name = SRMUtils.table_name;//"esgf_security.srm_entries";
        valid_user = SRMUtils.valid_user;//"dbsuper";
        valid_password = SRMUtils.valid_password;//"esgf4ever";
        /*
        this.srmAPIURL = SRMUtils.srmAPIURL;//"http://localhost:8080/esgf-srm/service/srmrequest?";
        this.expiration = SRMUtils.expiration;//"86400000";
        this.success_message = SRMUtils.success_message;//"success";
        this.failure_message = SRMUtils.failure_message;//"failure";
        this.db_name = SRMUtils.db_name;//"esgcet";
        this.table_name = SRMUtils.table_name;//"esgf_security.srm_entries";
        this.valid_user = SRMUtils.valid_user;//"dbsuper";
        this.valid_password = SRMUtils.valid_password;//"esgf4ever";
        */
    }
    
}
