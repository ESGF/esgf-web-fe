package org.esgf.srmcache;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.esgf.propertiesreader.PropertiesReader;
import org.esgf.propertiesreader.PropertiesReaderFactory;
import org.esgf.solr.model.Solr;
import org.esgf.solr.model.SolrRecord;
import org.esgf.solr.model.SolrResponse;
import org.esgf.srm.SRMControls;

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
    
    public PostgresSRMCacheStore() {
        
        PropertiesReaderFactory factory = new PropertiesReaderFactory();
        PropertiesReader srm_props = factory.makePropertiesReader("SRM");

        table_name = srm_props.getValue("srm_table_name");
        /*
        create_table_SQL = "create table " + table_name + 
                "(file_id varchar(128),dataset_id varchar(128),isCached varchar(32),timeStamp varchar(128), expiration varchar(128),openid varchar(256), primary key (file_id,dataset_id,openid));";
        */
        
        create_table_SQL = "create table " + table_name + 
                "(file_id varchar(128),dataset_id varchar(128),timeStamp varchar(128), expiration varchar(128),primary key (file_id,dataset_id));";
        
        
        drop_table_SQL = "drop table " + table_name + ";";

        
        setName("Postgres");
        
        //establish connection to the database
        this.connection = null;
        //System.out.println("Trying connection");
        try {
            
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/" + srm_props.getValue("srm_db_name"), 
                    srm_props.getValue("srm_valid_user"),
                    srm_props.getValue("srm_valid_password"));
            
            /*
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/" + SRMControls.db_name, SRMControls.valid_user,
                    SRMControls.valid_password);
            */
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
               System.out.println("Table already there"); 
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
            
        }
        
    }
    
    public SRMEntry getSRMEntryForFile_id(String dataset_id,String file_id) {
        
        String sql = "select * from srm_entries where dataset_id='" + dataset_id + "' AND file_id='" + file_id + "';";
        
        Statement st = null;
        int update = 0;
        
        SRMEntry srm_entry = null;
                
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()) {
                String file_idVal = (String)rs.getObject("file_id");
                String dataset_idVal = (String)rs.getObject("dataset_id");
                String timeStampVal = (String)rs.getObject("timeStamp");
                String expirationVal = (String)rs.getObject("expiration");
                //String isCachedVal = (String)rs.getObject("isCached");
                //String openidVal = (String)rs.getObject("openid");
                //srm_entry = new SRMEntry(file_idVal,dataset_idVal,isCachedVal,timeStampVal,expirationVal,openidVal);
                srm_entry = new SRMEntry(file_idVal,dataset_idVal,timeStampVal,expirationVal);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return srm_entry;
    }

    public List<SRMEntry> getSRMEntriesForDataset_id(String dataset_id) {
        
        String sql = "select * from srm_entries where dataset_id='" + dataset_id + "';";
        
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
                //srm_entry = new SRMEntry(file_idVal,dataset_idVal,isCachedVal,timeStampVal,expirationVal,openidVal);
                srm_entry = new SRMEntry(file_idVal,dataset_idVal,timeStampVal,expirationVal);
                srm_entries.add(srm_entry);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return srm_entries;
    }

    public int addSRMEntry(SRMEntry srm_entry) {
        
        Statement st = null;
        ResultSet rs = null;
        
        PropertiesReaderFactory factory = new PropertiesReaderFactory();
        PropertiesReader srm_props = factory.makePropertiesReader("SRM");

        
        String updateCommand = "insert into " + table_name + 
                " (file_id,dataset_id,timeStamp,expiration) values (" +
                "'" + srm_entry.getFile_id() + "'," +
                "'" + srm_entry.getDataset_id() + "'," +
                //"'" + srm_entry.getIsCached() + "'," +
                "'" + srm_entry.getTimeStamp() + "'," +
                "'" + srm_entry.getExpiration() + "'" +
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
    
    public int updateAllSRMEntriesForDatasetId(String dataset_id) {
        Statement st = null;
        ResultSet rs = null;
        
        //String isCached = "N/A";
        String timeStamp = Long.toString(System.currentTimeMillis());
        long expirationLong = Long.parseLong(timeStamp) + SRMControls.expiration;
        //String openid = "openid";
        
        String updateCommand = "update " + table_name + 
                               " set " + 
                               //"file_id='" + srm_entry.getFile_id() + "'," +
                               //"dataset_id='" + srm_entry.getDataset_id() + "'," +
                               //"isCached='" + isCached + "'," +
                               "timeStamp='" + timeStamp + "'," +
                               "expiration='" + Long.toString(expirationLong) + "'" +
                               //"openid='" + openid + "' 
                               " where " +
                               "dataset_id='" + dataset_id +
                               "';";
              

        
        int update = -1;
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
        
        //System.out.println("Update command: " + updateCommand + " " + update);
        
        return update;
    }

    public int updateSRMEntry(SRMEntry srm_entry) {
        
        Statement st = null;
        ResultSet rs = null;
        
        
        String updateCommand = "update " + table_name + 
                               " set " + 
                               //"isCached='" + srm_entry.getIsCached() + "'," +
                               "timeStamp='" + srm_entry.getTimeStamp() + "'," +
                               "expiration='" + srm_entry.getExpiration() + "'" +
                               //"openid='" + srm_entry.getOpenid() + "'" +
                               	" where " +
                               "file_id='" + srm_entry.getFile_id() + "' and " +
                               "dataset_id='" + srm_entry.getDataset_id() +
                               "';";
              

        
        System.out.println("Update command: " + updateCommand);
        
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

                if(SQLTableExists(connection, table_name)) {
                    update = st.executeUpdate(drop_table_SQL);
                }
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

                            //SRMEntry srm_entry = new SRMEntry(file_id,dataset_id,isCached,timeStamp,timeStamp,"openid");
                            SRMEntry srm_entry = new SRMEntry(file_id,dataset_id,timeStamp,timeStamp);
                            
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
                e.printStackTrace();
                
            }
            
        }
    }

    @Override
    public void disconnect() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    
    public static boolean SQLTableExists(Connection connection, String tableName) {
        boolean exists = false;

        try {
            Statement stmt = connection.createStatement();
            String sqlText = "SELECT tables.table_name FROM information_schema.tables WHERE table_name = '" + tableName + "'";    
            ResultSet rs = stmt.executeQuery(sqlText);

            if (rs != null) {
                while (rs.next()) {
                    if (rs.getString(1).equalsIgnoreCase(tableName)) {
                        //System.out.println("Table: " + tableName + " already exists!");
                        exists = true;
                    } else { 
                        //System.out.println("Table: " + tableName + " does not appear to exist.");
                        exists = false;
                    }

                }
            }

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
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
               
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<SRMEntry> getSRMEntriesForOpenid(String openid) {
        String sql = "select * from srm_entries where openid='" + openid + "';";
        
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
                //srm_entry = new SRMEntry(file_idVal,dataset_idVal,isCachedVal,timeStampVal,expirationVal,openidVal);
                srm_entry = new SRMEntry(file_idVal,dataset_idVal,timeStampVal,expirationVal);
                srm_entries.add(srm_entry);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return srm_entries;
    }

}
