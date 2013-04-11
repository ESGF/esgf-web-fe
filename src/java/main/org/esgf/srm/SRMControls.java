package org.esgf.srm;

public class SRMControls {

    public static String searchAPI = "http://localhost:8080/esg-search/search?";
    public static String srmAPIURL = "http://localhost:8080/esgf-srm/service/srmrequest?";
    public static boolean environmentSwitch = false;
    
    
    public static String create_table_SQL = "create table srm_entries(file_id varchar(128),dataset_id varchar(128),timeStamp varchar(128), primary key (file_id,dataset_id));";
    

    //public static String db_name = "esgcet";
    public static String db_name = "first_db";
    //public static String table_name = "esgf_security.srm_entries";
    public static String table_name = "srm_entries";
    public static String valid_user = "first_user";
    //public static String valid_user = "dbsuper";
    public static String valid_password = "mattryan12";
    //public static String valid_password = "esg4ever";
    
}
