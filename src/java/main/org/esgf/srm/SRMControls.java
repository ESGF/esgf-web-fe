package org.esgf.srm;

public class SRMControls {

    public static String searchAPI = "http://esg.ccs.ornl.gov/esg-search/search?";
    public static boolean environmentSwitch = false;
    
    
    
    public static String srmAPIURL = "http://esg.ccs.ornl.gov:8080/esgf-srm/service/srmrequest?";
    

    //public static String db_name = "esgcet";
    public static String db_name = "first_db";
    //public static String table_name = "esgf_security.srm_entries";
    public static String table_name = "srm_entries";
    public static String valid_user = "first_user";
    //public static String valid_user = "dbsuper";
    public static String valid_password = "mattryan12";
    //public static String valid_password = "esg4ever";
    
    
    public static long expiration = 5000000;
    
}
