/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bart.sis.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author miller.tim
 */
public class RouteConstructor {
    private final String mKey;
    private final String mSection;
    private final String mCmd;
    private final String mExtend;
    
    private final ApiConnector mApiConnection;
    private final Document mRouteData;
    
    private final Hashtable<String, RouteObject> mRouteTable = new Hashtable<>();
    
    public RouteConstructor(String key, String section, String cmd, String extend) throws ParserConfigurationException, MalformedURLException, SAXException, IOException{
        mKey = key;
        mSection = section;
        mCmd = cmd;
        mExtend = extend;
        mApiConnection = new ApiConnector(mKey);
        mApiConnection.setSectCmdClearExtend(mSection, mCmd);
        mApiConnection.setExtend(mExtend);
        mRouteData = mApiConnection.CallData();
        System.out.println(mApiConnection.getURL());
    
    }
    
    public void CommitRouteData(){
        String route = "route";
        String number = "number";
        String routeID = "routeID";
        String hexColor = "hexcolor";
        String config = "config";
     
        
        System.out.printf("Number of Routes: %d\n", mRouteData.getElementsByTagName(route).getLength());
        int numRoutes = mRouteData.getElementsByTagName(route).getLength();
        String[] foundRoutes = new String[numRoutes];
        for(int i = 0; i < numRoutes; i++){
            String thisHexColor = mRouteData.getElementsByTagName(hexColor).item(i).getTextContent();
            boolean routeFound = Arrays.asList(foundRoutes).contains(thisHexColor);
            System.out.printf("ArrayContains Color: %s\n", routeFound);
            if(!routeFound){
                foundRoutes[i] = thisHexColor;
                String thisRouteID = mRouteData.getElementsByTagName(routeID).item(i).getTextContent();
                String thisNumber = mRouteData.getElementsByTagName(number).item(i).getTextContent();
                
                NodeList stationNodeList = mRouteData.getElementsByTagName(config).item(i).getChildNodes();
                for(int x = 0; x < (stationNodeList.getLength()-1); x++){
                    int y = x+1;
                    String origStation = stationNodeList.item(x).getTextContent();
                    String destStation = stationNodeList.item(y).getTextContent();
                    String thisAbbr = String.format("%s:%s-%s", thisNumber, origStation, destStation);
                    mRouteTable.put(thisAbbr, new RouteObject(thisRouteID, thisHexColor, origStation, destStation, thisAbbr));
                    
                    System.out.printf("Route Color: %s, Abbreviation: %s\n", thisHexColor, thisAbbr);

                }
            }
        }
    }
    
    public Hashtable getRouteTable(){
        return(mRouteTable);
    }
    
}
