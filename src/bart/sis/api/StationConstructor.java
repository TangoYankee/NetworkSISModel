/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bart.sis.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class StationConstructor {
    private int mWidth;
    private int mHeight;
    private final String mSection;
    private final String mCmd;
    private final String mKey;
    
    private final ApiConnector mApiConnection;
    private final Document mStationData;
    
    private Hashtable<String, StationObject> mStationTable = new Hashtable<>();
    private Set<String> stationKeyChain;
    
    public StationConstructor(String key, String sStation, String cStation) throws ParserConfigurationException, MalformedURLException, SAXException, IOException{
        mKey = key;
        mSection = sStation;
        mCmd = cStation;
        mApiConnection = new ApiConnector(mKey);
        mApiConnection.setSectCmdClearExtend(mSection, mCmd);
        mStationData = mApiConnection.CallData();
        
    }
    
    public void CommitStationData(){

        String station = "station";        
        String name = "name";
        String abbr = "abbr";
        String lat = "gtfs_latitude";
        String lng = "gtfs_longitude";
        for (int i=0; i < mStationData.getElementsByTagName(station).getLength(); i++){
            String thisName= mStationData.getElementsByTagName(name).item(i).getTextContent();
            //System.out.printf("%s: %s\n", name, thisName);
            String thisAbbr = mStationData.getElementsByTagName(abbr).item(i).getTextContent();
            //System.out.printf("%s: %s\n", abbr, thisAbbr);
            //String thisLat = mStationData.getElementsByTagName(lat).item(i).getTextContent();
            Double thisLat = Double.parseDouble(mStationData.getElementsByTagName(lat).item(i).getTextContent());
            //System.out.printf("%s: %f\n", lat, thisLat);
            Double thisLng = Double.parseDouble(mStationData.getElementsByTagName(lng).item(i).getTextContent());
            //System.out.printf("%s: %f\n", lng, thisLng);
            mStationTable.put(thisAbbr, new StationObject(thisName, thisAbbr, thisLat, thisLng));
        }
        
    
    }
    
    public void NormalizeStationCoord(int width, int height){
        mWidth = width;
        mHeight= height;
        CoordinateNormalizer normal = new CoordinateNormalizer(mStationTable);
        normal.findMinMax();
        normal.normalize(mWidth, mHeight);
        mStationTable = normal.getStationTable();
    }
    
    
    
    
    public Hashtable getStationTable(){
        return (mStationTable);
    }
    
}
