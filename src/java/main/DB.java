import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DB {

    
    private static String db_name = "esgcet";
    private static String table_name = "films";
    private static String valid_user = "dbsuper";
    private static String valid_password = "esg4ever";
    
    private Connection con;
    
    private static String create_table_SQL = "create table esgf_security.srm_entries(file_id varchar(128),dataset_id varchar(128),timeStamp varchar(128), primary key (file_id,dataset_id));";
    
    
    public static void main(String [] args) {
        DB db = new DB();
        
        String command = null;
        db.buildDB(command);
    }
    
    
    public DB() {
        
        //establish connection
        this.con = null;
        try {
            
            this.con = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/" + db_name, valid_user,
                    valid_password);
            
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
            
        }
    }
    
    private void buildDB(String command) {
    
        
        Statement st = null;
        int update = 0;
        
        if (this.con != null) {
        
            System.out.println("Creating db");
            
            try {
                
                
                st = con.createStatement();
                update = st.executeUpdate(create_table_SQL);
               
                //adds tuples if needed
                if(command != null) {
                    update = st.executeUpdate(command);
                    System.out.println("updating... " + update);
                }
                st.close();
                
            } catch(SQLException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    
}
