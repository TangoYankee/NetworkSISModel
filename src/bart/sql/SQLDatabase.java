/*
Manage all of the SQL databases
 */
package bart.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.sql.ResultSet;


public class SQLDatabase {
    private final Properties properties = new Properties();
    private final String mJDBCDriver;
    private final String mDBURL;
    private final String mUsername;
    private final String mPassword;
    private final Connection conn;
    private final Statement stmt;
    
    private int mID;
    private importCSV csv;
    
    //Connect to the database
    public SQLDatabase(String JDBCDriver, String dbURL, String username, String password) 
            throws ClassNotFoundException, SQLException{
        mJDBCDriver = JDBCDriver;
        mDBURL = dbURL;
        mUsername = username;
        mPassword = password;
        mID = 1;
        
        properties.setProperty("user", mUsername);
        properties.setProperty("password", mPassword);
        properties.setProperty("useSSL", "false");
        properties.setProperty("autoReconnect", "true");
       
        Class.forName(mJDBCDriver);
        conn = DriverManager.getConnection(mDBURL, properties);
        stmt = conn.createStatement();
    }
    
    //Keep track of ids for nodes
    private void updateID(){
        this.mID++;
    }
    
    //Create a new database   
    public void createDatabase(String database)
            throws SQLException{
        String sql = "CREATE DATABASE " + database.toUpperCase();
        stmt.executeUpdate(sql);
    }
    
    //Drop a database
    public void dropDatabase(String database) 
            throws SQLException{
        String sql = "DROP DATABASE "+database.toUpperCase();
        stmt.executeUpdate(sql);
    }
    
    //Create a table with a fixed format for nodes
    public void createTableNode(String tableName) 
            throws SQLException{
        String sql = "CREATE TABLE " + tableName.toUpperCase()+
                "(stationId INTEGER not NULL, "+ 
                "station VARCHAR(255),"+
                "latitude INTEGER,"+ 
                "longitude INTEGER,"+
                "PRIMARY KEY( stationId ))";
        stmt.executeUpdate(sql);        
    }
    
    //Create a table with a fixed format for the edges
    public void createTableEdge(String tableName)
            throws SQLException{
        String sql = "Create TABLE " + tableName.toUpperCase()+
                "(id INTEGER not NULL, "+ 
                "nodeOne VARCHAR(255),"+
                "nodeTwo VARCHAR(255),"+ 
                "line VARCHAR(255),"+
                "PRIMARY KEY( id ))";
        stmt.executeUpdate(sql);     
    }
    
    //Fixed function to specifically fill node tables
    public void fillTableNode(String table, String file)
            throws SQLException, FileNotFoundException{
        csv = new importCSV(file);
        CSVReader reader= csv.getFileReader();
        for(String[] station: reader){
            String sql = "INSERT INTO "+table.toUpperCase()+" VALUES("+mID+", '"+station[0]+"', "+station[1]+", "+station[2]+")";
            stmt.executeUpdate(sql);
            updateID();
        }
    }

    //Fixed function to specifically fill edge tables
    public void fillTableEdge(String table, String file)
            throws SQLException, FileNotFoundException{
        csv = new importCSV(file);
        CSVReader reader = csv.getFileReader();
        for(String[] line: reader){
            String sql = "INSERT INTO "+table.toUpperCase()+" VALUES("+line[0]+",'"+line[1]+"','"+line[2]+"','"+line[3]+"')";
            stmt.executeUpdate(sql);
        }
    }
    //Drop a table
    public void dropTable(String tableName) 
            throws SQLException{
        String sql = "DROP TABLE "+tableName.toUpperCase();
        stmt.executeUpdate(sql);
    }    
    //Get the contents of the node and send it to the caller
    public ResultSet getNodeData(String table) 
            throws SQLException{
        String sql = "SELECT * FROM " +
                table.toUpperCase() +
                " WHERE stationId IS NOT NULL";
        ResultSet nodes = stmt.executeQuery(sql);         
        return(nodes);
    }
    //Get the contents of the edge and send it to the caller
    public ResultSet getEdgeData(String table) 
            throws SQLException{
        String sql = "SELECT lineId, nodeOne, nodeTwo, line FROM " +
                table.toUpperCase() +
                " WHERE lineId IS NOT NULL";
        ResultSet edges = stmt.executeQuery(sql);         
        return(edges);
    }
}
