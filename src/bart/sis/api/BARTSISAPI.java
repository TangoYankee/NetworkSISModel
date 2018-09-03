/*
Create an object oriented model of the BART Subway System
Display the Model in a JFrame
Objects are nodes, edges, and a graph
Hashtables are used to store multiple edges and graphs
The hastables recieve their data from two SQL databases
The graph object is used to create and visualize the graph
 */
package bart.sis.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.sql.SQLException;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class BARTSISAPI {

    public static void main(String[] args) 
            throws ClassNotFoundException, SQLException, InterruptedException, FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        
        //API Key
        String key = "";
        //dimensions
        int width = 700;
        int height = 700;
        //Station Data
        String sStation = "stn";
        String cStation = "stns";
        StationConstructor stations = new StationConstructor(key, sStation, cStation);
        stations.CommitStationData();
        stations.NormalizeStationCoord(width, height);
        Hashtable<String, StationObject> stationTable = stations.getStationTable();
        //Route Data
        String sRoute = "route";
        String cRouteInfo = "routeinfo";
        String eExtend = "&route=all";
        RouteConstructor routes = new RouteConstructor(key, sRoute, cRouteInfo, eExtend);
        routes.CommitRouteData();
        Hashtable<String, RouteObject> routeTable = routes.getRouteTable();

        //Create a graph from the Hashtables
        GraphData newGraph = new GraphData(stationTable, routeTable);
        //Create a new infection
        InfectionData ebola = new InfectionData(1, "ebola", 0.5, 0.05);
        //Run an SIS simulation
        newGraph.runSIS(ebola, 100);
    }
    
}
