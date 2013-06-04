package org.esgf.srmcache;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.esgf.filetransformer.SRMFileTransformationUtils;
import org.esgf.propertiesreader.PropertiesReader;
import org.esgf.propertiesreader.PropertiesReaderFactory;
import org.esgf.propertiesreader.SRMProperties;
import org.esgf.solr.model.Solr;
import org.esgf.solr.model.SolrRecord;
import org.esgf.solr.model.SolrResponse;
import org.esgf.srm.SRMControls;
import org.esgf.srm.utils.SRMUtils;

public class PostgresSRMCacheStore extends SRMCacheStore {

    /*
    public static String create_table_SQL = 
            "create table srm_entries(file_id varchar(128),dataset_id varchar(128),isCached varchar(32),timeStamp varchar(128), expiration varchar(128),openid varchar(256), primary key (file_id,dataset_id,openid));";
    
    public static String drop_table_SQL = "drop table srm_entries;";
    
    
    public static String table_name = "srm_entries";
    */
    
    public static String create_table_SQL = "";
    public static String drop_table_SQL = "";
    public static String table_name = "";
    
    private Connection connection;
    
    public static void main(String [] args) {
        String sql = "select * from srm_entries where dataset_id='ornl.ultrahighres.CESM1.t85f09.B1850_50yrs.seaIce.v1|esg2-sdnl1.ccs.ornl.gov' AND file_id='ornl.ultrahighres.CESM1.t85f09.B1850_50yrs.seaIce.v1.t85f09.B1850.cice.h.0005-02.nc|esg2-sdnl1.ccs.ornl.gov';";
    
        Statement st = null;

        PreparedStatement pst = null;
        
        Connection connection = null;
        
        PropertiesReaderFactory factory = new PropertiesReaderFactory();
        PropertiesReader srm_props = factory.makePropertiesReader("SRM");

        System.out.println("IN main");
        
        ResultSet rs;
        try {
            
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/" + srm_props.getValue("srm_db_name"), 
                    srm_props.getValue("srm_valid_user"),
                    srm_props.getValue("srm_valid_password"));
            
            pst = connection.prepareStatement(sql);
            rs = pst.executeQuery();
            if(!rs.next()) {
                System.out.println("RS is null?????");
            }else {
                System.out.println("not null");
            }
            /*
             
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            System.out.println("first: " + rs.first());
            if(!rs.next()) {
                System.out.println("RS is null?????");
            }
            */
            
            //System.out.println("Executing sql: " + sql);
        
            sql = "select * from esgf_security.srm_entries where dataset_id='ornl.ultrahighres.CESM1.t85f09.B1850_50yrs.seaIce.v1|esg2-sdnl1.ccs.ornl.gov' AND file_id='ornl.ultrahighres.CESM1.t85f09.B1850_50yrs.seaIce.v1.t85f09.B1850.cice.h.0005-02.nc|esg2-sdnl1.ccs.ornl.gov'";
            
            
            pst = connection.prepareStatement(sql);
            rs = pst.executeQuery();
            if(!rs.next()) {
                System.out.println("RS is null?????");
            }else {
                System.out.println("not null");
            }
        
        } catch(Exception e) {
            //e.printStackTrace();
        }
    
    }
    
    public PostgresSRMCacheStore() {
        
        
        System.out.println("\nGETTING PSQL Object\n");
        //System.out.println("\n\nGETTING SRM PROPERTIES HERE\n\n");
        //System.out.println("pass->" + SRMProperties.valid_password);
        
        PropertiesReaderFactory factory = new PropertiesReaderFactory();
        PropertiesReader srm_props = factory.makePropertiesReader("SRM");
        
        if(SRMUtils.postgresCacheStoreFlag) {
            System.out.println("OPENING PROPERTIES FILE HERE");
        }
        
        table_name = srm_props.getValue("srm_table_name");
       
        create_table_SQL = "create table " + table_name + 
                "(file_id varchar(128),dataset_id varchar(128),timeStamp varchar(128), expiration varchar(128), bestmanNumber varchar(128), primary key (file_id,dataset_id));";
        
        //System.out.println("in constructor: " + create_table_SQL);
        
        drop_table_SQL = "drop table " + table_name + ";";

        setName("Postgres");
        
        //establish connection to the database
        this.connection = null;
        try {
            
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/" + srm_props.getValue("srm_db_name"), 
                    srm_props.getValue("srm_valid_user"),
                    srm_props.getValue("srm_valid_password"));
            
            //check to see if the table exists
            boolean tableExists = false;
            
            //if the table doesn't exist - create it
            if(!SQLTableExists(connection, table_name)) {
                Statement st = connection.createStatement();

                if(!SQLTableExists(connection, table_name)) {
                    int update = st.executeUpdate(create_table_SQL);
                }
                //System.out.println("Table not there"); 
                
            } else {
                if(SRMUtils.postgresCacheStoreFlag) {
                    System.out.println("\tTable already there"); 
                }
            }
            
            //this.connection.close();
            
        } catch (SQLException e) {
            //e.printStackTrace();
           
            System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
            
        }
        
    }
    
    public SRMEntry getSRMEntryForFile_id(String dataset_id,String file_id) {
        
        String sql = "select * from " + table_name +  " where dataset_id='" + dataset_id + "' AND file_id='" + file_id + "';";
        
        Statement st = null;
        int update = 0;
        
        SRMEntry srm_entry = null;
                
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
       
            //System.out.println("\tExecuting sql: " + sql);
            
            while(rs.next()) {
                //System.out.println("\trecord");
                String file_idVal = (String)rs.getObject("file_id");
                String dataset_idVal = (String)rs.getObject("dataset_id");
                String timeStampVal = (String)rs.getObject("timeStamp");
                String expirationVal = (String)rs.getObject("expiration");
                String bestmannumberVal = (String)rs.getObject("bestmannumber");
                srm_entry = new SRMEntry(file_idVal,dataset_idVal,timeStampVal,expirationVal,bestmannumberVal);
            }

            rs.close();
            st.close();
            
        } catch (SQLException e) {
            System.out.println("Error in getSRMEntryForFile_id");
            //e.printStackTrace();
        } finally {
        }
        
        return srm_entry;
    }

    public List<SRMEntry> getSRMEntriesForDataset_id(String dataset_id) {
        
        String sql = "select * from " + table_name + " where dataset_id='" + dataset_id + "';";
        
        Statement st = null;
        int update = 0;
        
        SRMEntry srm_entry = null;
        
        List<SRMEntry> srm_entries = new ArrayList<SRMEntry>();
        
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()) {
                String file_idVal = (String)rs.getObject("file_id");
                String dataset_idVal = (String)rs.getObject("dataset_id");
                String timeStampVal = (String)rs.getObject("timeStamp");
                //String isCachedVal = (String)rs.getObject("isCached");
                //String openidVal = (String)rs.getObject("openid");
                String expirationVal = (String)rs.getObject("expiration");
                String bestmannumberVal = (String)rs.getObject("bestmannumber");
                //srm_entry = new SRMEntry(file_idVal,dataset_idVal,isCachedVal,timeStampVal,expirationVal,openidVal);
                srm_entry = new SRMEntry(file_idVal,dataset_idVal,timeStampVal,expirationVal,bestmannumberVal);
                srm_entries.add(srm_entry);
            }

            rs.close();
            st.close();
            
        } catch (SQLException e) {
            System.out.println("Error in getSRMEntriesForDataset_id");
            //e.printStackTrace();
        }
        
        return srm_entries;
    }

    public int addSRMEntry(SRMEntry srm_entry) {
        
        Statement st = null;
        ResultSet rs = null;
        
        //PropertiesReaderFactory factory = new PropertiesReaderFactory();
        //PropertiesReader srm_props = factory.makePropertiesReader("SRM");

        
        String updateCommand = "insert into " + table_name + 
                " (file_id,dataset_id,timeStamp,expiration,bestmannumber) values (" +
                "'" + srm_entry.getFile_id() + "'," +
                "'" + srm_entry.getDataset_id() + "'," +
                //"'" + srm_entry.getIsCached() + "'," +
                "'" + srm_entry.getTimeStamp() + "'," +
                "'" + srm_entry.getExpiration() + "'," +
                "'" + srm_entry.getBestmannumber() + "'" +
                //"'" + srm_entry.getOpenid() +
                ");";

        //System.out.println("\t" + updateCommand);
        /*
        String updateCommand = "insert into " + SRMControls.table_name + 
                                " (file_id,dataset_id,isCached,timeStamp,expiration,openid) values (" +
                                "'" + srm_entry.getFile_id() + "'," +
                                "'" + srm_entry.getDataset_id() + "'," +
                                "'" + srm_entry.getIsCached() + "'," +
                                "'" + srm_entry.getTimeStamp() + "'," +
                                "'" + srm_entry.getExpiration() + "'," +
                                "'" + srm_entry.getOpenid() +
                                "');";
        */        
        
        if (this.connection != null) {
            try {
                st = connection.createStatement();
                int update = st.executeUpdate(updateCommand);
                st.close();
            } catch(SQLException e) {
                //e.printStackTrace();
                return -1;
            }
            
        }  else {
            return -1;
        }
        
        return 0;
    }
    
    public int updateAllSRMEntriesForDatasetId(String dataset_id,String [] file_ids,String [] response_urls) {
        Statement st = null;
        ResultSet rs = null;
        
        //String isCached = "N/A";
        String timeStamp = Long.toString(System.currentTimeMillis());
        long expirationLong = Long.parseLong(timeStamp) + SRMControls.expiration;
        //String openid = "openid";
        
        int update = -1;
        
        if(file_ids.length == response_urls.length) {
            
            for(int i=0;i<file_ids.length;i++) {
                String file_id = file_ids[i];
                
                String bestmannum = SRMFileTransformationUtils.extractBestmanNumFromUrl(response_urls[i]);
                
                String updateCommand = "update " + table_name + 
                                       " set " + 
                                       "bestmannumber='" + bestmannum + "'," +
                                       "timeStamp='" + timeStamp + "'," +
                                       "expiration='" + Long.toString(expirationLong) + "'" +
                                       " where " +
                                       "dataset_id='" + dataset_id + "' AND " +
                                       "file_id='" + file_id +
                                       "';";
                      
                if(SRMUtils.updateAllSRMEntriesForDatasetIdFlag)
                    System.out.println("Update: " + updateCommand);
                
                if (this.connection != null) {
                    try {
        
                        st = connection.createStatement();
                        update = st.executeUpdate(updateCommand);
                        st.close();
                    } catch(SQLException e) {
                        
                    }
                } else {
                    System.out.println("Not connected to the database");
                }
            }
            
        }
        
        return update;
    }
    
    

    public int updateSRMEntry(SRMEntry srm_entry) {
        
        Statement st = null;
        ResultSet rs = null;
        
        
        String updateCommand = "update " + table_name + 
                               " set " + 
                               "timeStamp='" + srm_entry.getTimeStamp() + "'," +
                               "bestmannumber='" + srm_entry.getBestmannumber() + "'," +
                               "expiration='" + srm_entry.getExpiration() + "'" +
                               	" where " +
                               "file_id='" + srm_entry.getFile_id() + "' and " +
                               "dataset_id='" + srm_entry.getDataset_id() +
                               "';";
              

        //System.out.println("Update: " + updateCommand);
        
        if (this.connection != null) {
            try {

                st = connection.createStatement();
                int update = st.executeUpdate(updateCommand);
                st.close();
            } catch(SQLException e) {
                
            }
        } else {
            System.out.println("Not connected to the database");
        }
        
        return 0;
    }

    public int deleteSRMEntry(SRMEntry srm_entry) {
        
        
        return 0;
    }

    public void initializeCacheStore() {
        
        
        
        String command = "";

        String tableName = "srm_entries";
        
        
        if (this.connection != null) {
            Statement st = null;
            int update = 0;
            
            try {
                
                st = connection.createStatement();

                //if(SQLTableExists(connection, table_name)) {
                    update = st.executeUpdate(drop_table_SQL);
                //}
                //System.out.println(create_table_SQL);
                update = st.executeUpdate(create_table_SQL);
               
                
                //fill table with solr records
                
                
                List<SRMEntry> srm_entry_list = new ArrayList<SRMEntry>();
                
                String core = "Dataset";
                
                Solr solr = new Solr();
                int limit = 80;
                solr.addConstraint("query", "*");
                solr.addConstraint("distrib", "false");
                solr.addConstraint("limit", Integer.toString(limit));
                solr.addConstraint("type", core);

                
                List<String> srms = new ArrayList<String>();
                List<String> dataset_ids = new ArrayList<String>();
                
                boolean iterate = true;
                int counter = 0;
                
                
                
                while(iterate) {
                    
                    String offset = Integer.toString(counter*limit);
                    solr.addConstraint("offset", Integer.toString(counter*limit));
                    //System.out.println("\tExecuting solr query");
                    //System.out.println("\tQueryString->" + solr.getQueryString());
                    solr.executeQuery();
                    
                    
                    SolrResponse solrResponse = solr.getSolrResponse();
                    
                    String countStr = solrResponse.getCount();
                    int count = Integer.parseInt(countStr);
                    
                    
                    List<String> needsSRM = solrResponse.needsSRM();
                    
                    dataset_ids.addAll(needsSRM);
                    
                    solr.removeConstraint("offset");
                    
                    if(count < (counter+1)*limit) {
                        iterate = false;
                    }
                    
                    counter++;
                }
                
                for(int i=0;i<dataset_ids.size();i++) {
                    
                    core = "File";
                    solr = new Solr();
                    limit = 80;
                    solr.addConstraint("query", "*");
                    solr.addConstraint("distrib", "false");
                    solr.addConstraint("limit", Integer.toString(limit));
                    solr.addConstraint("type", core);
                    solr.addConstraint("dataset_id", dataset_ids.get(i));
                    iterate = true;
                    counter = 0;
                    String timeStamp = Long.toString(System.currentTimeMillis());
                    
                    while(iterate) {
                        String offset = Integer.toString(counter*limit);
                        solr.addConstraint("offset", Integer.toString(counter*limit));
                        //System.out.println("QueryString-> " + solr.getQueryString());
                        solr.executeQuery();
                        
                        SolrResponse solrResponse = solr.getSolrResponse();
                        
                        String countStr = solrResponse.getCount();
                        int count = Integer.parseInt(countStr);
                        
                        for(int j=0;j<solrResponse.getSolrRecords().size();j++) {
                            SolrRecord record = solrResponse.getSolrRecords().get(j);
                            String file_id = record.getStrField("id");
                            String dataset_id = dataset_ids.get(i);
                            String isCached = "N/A";
                            
                            long expirationLong = Long.parseLong(timeStamp) + SRMControls.expiration;

                            String bestmannumber = "V-0.0.000000";
                            
                            //SRMEntry srm_entry = new SRMEntry(file_id,dataset_id,isCached,timeStamp,timeStamp,"openid");
                            SRMEntry srm_entry = new SRMEntry(file_id,dataset_id,timeStamp,timeStamp,bestmannumber);
                            
                            //this.srm_entries.add(srm_entry);
                            
                            
                            int add = this.addSRMEntry(srm_entry);
                            
                            //System.out.println("adding " + srm_entry.getFile_id() + " " + add);
                            
                        }
                        
                        solr.removeConstraint("offset");
                        
                        if(count < (counter+1)*limit) {
                            iterate = false;
                        }
                        
                        counter++;
                        
                    }
                }
                
                st.close();
                
                
            } catch(SQLException e) {
                System.out.println("Error in initializeCacheStore");
                //e.printStackTrace();
            }
            
        }
    }
    
 

    public void removeCacheStore() {
        
        String command = "";
        
        if (this.connection != null) {
            Statement st = null;
            int update = 0;
            
            try {
                
                st = connection.createStatement();
                if(SQLTableExists(connection, table_name)) {
                    update = st.executeUpdate(drop_table_SQL);
                }
                st.close();
                
            } catch(SQLException e) {
                //e.printStackTrace();
                System.out.println("Error In removeCacheStore");
            }
            
        }
    }

    @Override
    public void disconnect() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            System.out.println("Error In disconnect");
            //e.printStackTrace();
        }
    }
    
    
    
    public static boolean SQLTableExists(Connection connection, String tableName) {
        
        tableName = "srm_entries";
        
        boolean exists = false;

        try {
            Statement stmt = connection.createStatement();
            String sqlText = "SELECT tables.table_name FROM information_schema.tables WHERE table_name = '" + tableName + "'";    
            if(SRMUtils.tableExistsFlag) {
                
                System.out.println(sqlText);
            }
            
            ResultSet rs = stmt.executeQuery(sqlText);

            if (rs != null) {
                //System.out.println("NOT NULL");
                //System.out.println("rs next: " + rs.next());
                while (rs.next()) {
                    //System.out.println("record");
                    if (rs.getString(1).equalsIgnoreCase(tableName)) {
                        //System.out.println("Table: " + tableName + " already exists!");
                        exists = true;
                    } else { 
                        //System.out.println("Table: " + tableName + " does not appear to exist.");
                        exists = false;
                    }

                }
            } else {
                //System.out.println("NULL");
            }
            
            rs.close();

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        
        if(SRMUtils.tableExistsFlag) {
            System.out.println("Table " + tableName + ": " + exists);
        }
        
        return exists;
    }

    public void createCacheStore() {
        if (this.connection != null) {
            Statement st = null;
            int update = 0;
            
            try {
                
                st = connection.createStatement();

                if(SQLTableExists(connection, table_name)) {
                    update = st.executeUpdate(drop_table_SQL);
                }
                update = st.executeUpdate(create_table_SQL);
                st.close();
            } catch(Exception e) {
                System.out.println("Error in createCacheStore");
                //e.printStackTrace();
            }
        }
    }

    @Override
    public List<SRMEntry> getSRMEntriesForOpenid(String openid) {
        String sql = "select * from " + table_name + " where openid='" + openid + "';";
        
        Statement st = null;
        int update = 0;
        
        SRMEntry srm_entry = null;
        
        List<SRMEntry> srm_entries = new ArrayList<SRMEntry>();
        
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()) {
                String file_idVal = (String)rs.getObject("file_id");
                String dataset_idVal = (String)rs.getObject("dataset_id");
                String timeStampVal = (String)rs.getObject("timeStamp");
                String expirationVal = (String)rs.getObject("expiration");
                String bestmannumberVal = (String)rs.getObject("bestmannumber");
                srm_entry = new SRMEntry(file_idVal,dataset_idVal,timeStampVal,expirationVal,bestmannumberVal);
                srm_entries.add(srm_entry);
            }
            rs.close();
            st.close();
            
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("Error in getSRMEntriesForOpenid");
        }
        
        return srm_entries;
    }

}
