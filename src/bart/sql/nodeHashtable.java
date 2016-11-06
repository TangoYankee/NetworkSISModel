/*
Create and manage a hashtable for the nodes
 */
package bart.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;


public class nodeHashtable {
    private final Hashtable<String, nodeData> nodeTable = new Hashtable<>();
    private final SQLDatabase mSQLdatabase;
    
    //Main function
    public nodeHashtable(SQLDatabase sqldatabase) 
            throws SQLException{
        mSQLdatabase = sqldatabase;
        PopulateNodeDataTable();
    }
    
    //Write data to the hashtable from the sqldatabase
    private void PopulateNodeDataTable() 
            throws SQLException{
        try (
                ResultSet nodes = mSQLdatabase.getNodeData("stationLocation")) {
                while(nodes.next()){
                    String station = nodes.getString("station");
                    int id = nodes.getInt("stationId");
                    int lat = nodes.getInt("latitude");
                    int lng = nodes.getInt("longitude");
                    nodeTable.put(station,new nodeData(id, station, lat, lng));
            }
            nodes.close();
        }
    }

    //Function to access Hashtable
    public Hashtable getNodeTable(){
        return(nodeTable);
    }
    
}
