/*
Create an object oriented model of the BART Subway System
Display the Model in a JFrame
Objects are nodes, edges, and a graph
Hashtables are used to store multiple edges and graphs
The hastables recieve their data from two SQL databases
The graph object is used to create and visualize the graph
 */
package bart.sql;

import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.sql.SQLException;

public class BARTSQL {

    public static void main(String[] args) 
            throws ClassNotFoundException, SQLException, InterruptedException, FileNotFoundException {
        //Create a SQL connection
        String JDBCDriver = "com.mysql.jdbc.Driver";
        String dbURL = "jdbc:mysql://localhost:3306/subways?";
        String username = "root";
        String password = "B10@D4t4St0r3";

        //Object of a new database
        SQLDatabase sqldatabase = new SQLDatabase(JDBCDriver, dbURL, username, password);
        
        //Manage the database
//        String file = "C:/Users/miller.tim/Documents/Urban Resilience/StationsLocations.csv";
//        String tableName = "stationLocation";
//        sqldatabase.dropTable(tableName);
//        sqldatabase.createTableNode(tableName);
//        sqldatabase.fillTableNode(tableName, file);
        
        //Create hashtables of node and edge data
        nodeHashtable nodeTable = new nodeHashtable(sqldatabase);
        edgeHashtable edgeTable = new edgeHashtable(sqldatabase);
        Hashtable<String, nodeData> nodeHashes = nodeTable.getNodeTable();
        Hashtable<String, edgeData> edgeHashes = edgeTable.getEdgeTable(); 

        //Create a graph from the Hashtables
        graphData newGraph = new graphData(nodeHashes, edgeHashes, sqldatabase);
        //Create a new infection
        infectionData ebola = new infectionData(1, "ebola", 0.5, 0.05);
        //Run an SIS simulation
        newGraph.runSIS(ebola, 100);
    }
    
}
