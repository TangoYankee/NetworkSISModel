/*
Create and manage a hashtable for the edges
 */
package bart.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

public class edgeHashtable {
    private final Hashtable<String, edgeData> edgeTable = new Hashtable<>();
    private final SQLDatabase mSQLdatabase;
    
    //Main Function
    public edgeHashtable(SQLDatabase sqldatabase)
            throws SQLException{
        mSQLdatabase = sqldatabase;
        PopulateEdgeDataTable();
    }
    
    //Fill the Hashtable with data
    private void PopulateEdgeDataTable() 
            throws SQLException{
        try ( 
                ResultSet edges = mSQLdatabase.getEdgeData("bartlines")) {
            
                while(edges.next()){
                    int id = edges.getInt("lineId");
                    String line = edges.getString("line");
                    String EdgeID = "Edge"+Integer.toString(id);
                    edgeTable.put(EdgeID, new edgeData (id, line));
            }
        }

    }
       
    //Function to access Hashtable
    public Hashtable getEdgeTable(){
        return(edgeTable);
    }
}
