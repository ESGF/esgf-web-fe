package org.esgf.srmcache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.esgf.solr.model.Solr;
import org.esgf.solr.model.SolrRecord;
import org.esgf.solr.model.SolrResponse;
import org.esgf.srm.SRMControls;

public class PostgresSRMCacheStore extends SRMCacheStore {

    public static String create_table_SQL = "create table srm_entries(file_id varchar(128),dataset_id varchar(128),isCached varchar(32),timeStamp varchar(128), primary key (file_id,dataset_id));";
    
    public static String drop_table_SQL = "drop table srm_entries;";
    
    
    public static String table_name = "srm_entries";
    
    private Connection connection;
    
    public PostgresSRMCacheStore() {
        setName("Postgres");
        
        //establish connection
        this.connection = null;
        try {
            //System.out.println("connection");
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/" + SRMControls.db_name, SRMControls.valid_user,
                    SRMControls.valid_password);
            
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
            
        }
        
    }
    
    public SRMEntry getSRMEntryForFile_id(String dataset_id,String file_id) {
        
        String sql = "select * from srm_entries where dataset_id=" + dataset_id + " AND file_id=" + file_id + ";";
        
        Statement st = null;
        int update = 0;
        
        SRMEntry srm_entry = null;
                
        ResultSet rs;
        try {
            rs = st.executeQuery(sql);
            
            while(rs.next()) {
                String file_idVal = (String)rs.getObject("file_id");
                String dataset_idVal = (String)rs.getObject("dataset_id");
                String timeStampVal = (String)rs.getObject("timeStamp");
                String isCachedVal = (String)rs.getObject("isCached");
                srm_entry = new SRMEntry(file_idVal,dataset_idVal,isCachedVal,timeStampVal);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return srm_entry;
    }

    public List<SRMEntry> getSRMEntriesForDataset_id(String dataset_id) {
        
        String sql = "select * from srm_entries where dataset_id=" + dataset_id + ";";
        
        Statement st = null;
        int update = 0;
        
        SRMEntry srm_entry = null;
        
        List<SRMEntry> srm_entries = new ArrayList<SRMEntry>();
        
        ResultSet rs;
        System.out.println(sql);
        try {
            rs = st.executeQuery(sql);
            
            while(rs.next()) {
                String file_idVal = (String)rs.getObject("file_id");
                String dataset_idVal = (String)rs.getObject("dataset_id");
                String timeStampVal = (String)rs.getObject("timeStamp");
                String isCachedVal = (String)rs.getObject("isCached");
                srm_entry = new SRMEntry(file_idVal,dataset_idVal,isCachedVal,timeStampVal);
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
        
        String updateCommand = "insert into " + SRMControls.table_name + " (file_id,dataset_id,isCached,timeStamp) values (" +
                                "'" + srm_entry.getFile_id() + "'," +
                                "'" + srm_entry.getDataset_id() + "'," +
                                "'" + srm_entry.getIsCached() + "'," +
                                "'" + srm_entry.getTimeStamp() + "');";
                
        //System.out.println("Update Command: " + updateCommand);
        if (this.connection != null) {
            try {
                st = connection.createStatement();
                int update = st.executeUpdate(updateCommand);
                st.close();
            } catch(SQLException e) {
                return -1;
            }
            
        }  else {
            return -1;
        }
        
        
        return 0;
    }

    public int updateSRMEntry(SRMEntry srm_entry) {
        
        Statement st = null;
        ResultSet rs = null;
        
        String updateCommand = "update " + SRMControls.table_name + 
                               " set " + 
                               "file_id='" + srm_entry.getFile_id() + "'," +
                               "dataset_id='" + srm_entry.getDataset_id() + "'," +
                               "isCached='" + srm_entry.getIsCached() + "'," +
                               "timeStamp='" + srm_entry.getTimeStamp() + ";";
              

        //System.out.println("Update Command: " + updateCommand);
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
        
        
        System.out.println("InitializeCacheStore");
        
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
                    
                    //System.out.println("Querying-> " + dataset_ids.get(i));
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
                            //System.out.println("Dataset: " + dataset_ids.get(i) + " " + file_id);
                            String timeStamp = Long.toString(System.currentTimeMillis());
                            String isCached = "N/A";
                            SRMEntry srm_entry = new SRMEntry(file_id,dataset_id,isCached,timeStamp);
                            //this.srm_entries.add(srm_entry);
                            
                            
                            this.addSRMEntry(srm_entry);
                            
                            
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
        
        System.out.println("RemoveCacheStore");
        
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
                        System.out.println("Table: " + tableName + " already exists!");
                        exists = true;
                    } else { 
                        System.out.println("Table: " + tableName + " does not appear to exist.");
                        exists = false;
                    }

                }
            }

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        return exists;
    }

    @Override
    public void createCacheStore() {
        // TODO Auto-generated method stub
        
    }

}
