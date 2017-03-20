/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bart.sis.api;


public class StationObject {
    private final String mAbbr;
    private final String mName;
    private String mState;
    private int mDaysInfected;
    private final Double mLat;
    private final Double mLng;
    private Double mXCoord;
    private Double mYCoord;
    
    
    public StationObject(String name, String abbr, Double lat, Double lng){
        mName = name;
        mAbbr = abbr;
        mState = "Healthy";
        mDaysInfected = 0;
        mLat = lat;
        mLng = lng;
        mXCoord = (lng);
        mYCoord = (lat);
       }
        public void infectStation(){
        this.mState = "Infected";
        this.mDaysInfected ++;
    }
    
    public void healStation(){
        this.mState = "Healthy";
        this.mDaysInfected = 0;
    }
    
    public void changeState(){
        if ("Healthy".equals(mState))
                infectStation();
        else
                healStation();
    }
    
    public String getState(){
        return (mState);
    }
    
    public int getDaysInfected(){
        return(mDaysInfected);
    }
    
    public void addDayInfected(){
        this.mDaysInfected++;
    }
    
    public String getName(){
        return(mName);
    }
    
    public String getAbbr(){
        return(mAbbr);
    }
    
    public Double getLat(){
        return(mLat);
    }
    
    public Double getLng(){
        return(mLng);
    }
    
    public void setXCoord(Double xCoord){
        this.mXCoord = xCoord;
    }
    
    public Double getXCoord(){
        return(mXCoord);
    }
    
    public void setYCoord(Double yCoord){
        this.mYCoord = yCoord;
    }
    
    public Double getYCoord(){
        return(mYCoord);
    }
    
}
