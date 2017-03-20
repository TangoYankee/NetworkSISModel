/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bart.sis.api;

import java.util.Hashtable;
import java.util.Set;

public class CoordinateNormalizer {
    private final Hashtable<String, StationObject> mStationTable;
    private final Set<String> stationKeyChain;
    private Double mMaxLatValue = Double.MIN_VALUE;
    private String mMaxLatValueKey = null;
    private Double mMaxLngValue = Double.MIN_VALUE;
    private String mMaxLngValueKey = null;
    private Double mMinLatValue = Double.MAX_VALUE;
    private String mMinLatValueKey = null;
    private Double mMinLngValue = Double.MAX_VALUE;
    private String mMinLngValueKey = null;
    
    public CoordinateNormalizer( Hashtable<String, StationObject> stationTable){
        mStationTable = stationTable;
        stationKeyChain = mStationTable.keySet();

    }
    
    public void findMinMax(){
        for(String key: stationKeyChain){
            Double latValue = Math.abs(mStationTable.get(key).getLat());
            checkMaxLatValue(latValue, key);
            checkMinLatValue(latValue, key);
            Double lngValue = Math.abs(mStationTable.get(key).getLng());
            checkMaxLngValue(lngValue, key);
            checkMinLngValue(lngValue, key);
            
        }
        /*
        System.out.printf("MaxLatKey: %s; Value: %f\n", mMaxLatValueKey, mMaxLatValue);
        System.out.printf("MinLatKey: %s; Value: %f\n", mMinLatValueKey, mMinLatValue);
        System.out.printf("MaxLngKey: %s; Value: %f\n", mMaxLngValueKey, mMaxLngValue);
        System.out.printf("MinLngKey: %s; Value: %f\n", mMinLngValueKey, mMinLngValue);
        */
    }
    
    public void normalize(int width, int height){
        mMinLngValue = mStationTable.get(mMaxLngValueKey).getLng();
        mMaxLngValue = mStationTable.get(mMinLngValueKey).getLng();
        Double heightMultiplier = Math.floor((height-50)/(mMaxLatValue-mMinLatValue));
        Double widthMultiplier = Math.floor((width-50)/(mMaxLngValue-mMinLngValue));
        
        
        
        for (String key: stationKeyChain){
            Double yCoord = mStationTable.get(key).getLat();
            Double xCoord = mStationTable.get(key).getLng();
            yCoord = height-(heightMultiplier*(yCoord-mMinLatValue)+(height*.01));
            xCoord = widthMultiplier*(xCoord-mMinLngValue)+(width*.01);
            mStationTable.get(key).setYCoord(yCoord);
            mStationTable.get(key).setXCoord(xCoord);
            /*
            System.out.printf("Station: %s, Lat: %3f, Lng: %3f, X: %f, Y: %f\n",
                    mStationTable.get(key).getAbbr(),
                    mStationTable.get(key).getLat(),
                    mStationTable.get(key).getLng(),
                    mStationTable.get(key).getXCoord(),
                    mStationTable.get(key).getYCoord());
            */
        }
    
    }
    
    private void checkMaxLatValue(Double challenger, String key){
        if(challenger > mMaxLatValue){
            this.mMaxLatValue = challenger;
            this.mMaxLatValueKey = key;
        }    
    }
    
    private void checkMinLatValue(Double challenger, String key){
        if(challenger < mMinLatValue){
            this.mMinLatValue = challenger;        
            this.mMinLatValueKey = key;  
        }
    }
    
    private void checkMaxLngValue(Double challenger, String key){
        if(challenger > mMaxLngValue){
            this.mMaxLngValue = challenger;        
            this.mMaxLngValueKey = key;
        }
    }

    private void checkMinLngValue(Double challenger, String key){
        if(challenger < mMinLngValue){
            this.mMinLngValue = challenger;        
            this.mMinLngValueKey = key;
        }
    }
    
    public Hashtable getStationTable(){
        return (mStationTable);
    }
    
}
