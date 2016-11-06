/*
Object for nodes
 */
package bart.sql;

public class nodeData {
    private final int mID;
    private final String mName;
    private String mState;
    private int mDaysInfected;
    private int mLatitude;
    private int mLongitude;

    public nodeData(int x, String name, int latitude, int longitude){
        mID = x;
        mName = name;
        mState = "Healthy";
        mDaysInfected = 0;
        mLatitude = latitude;
        mLongitude = longitude;
        
    }
    
    public int getID(){
        return mID;
    }
    
    public String getName(){
        return (mName);
    }
    
    public void infectNode(){
        this.mState = "Infected";
        this.mDaysInfected ++;
    }
    
    public void healNode(){
        this.mState = "Healthy";
        this.mDaysInfected = 0;
    }
    
    public void changeState(){
        if ("Healthy".equals(mState))
                infectNode();
        else
                healNode();
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
    
    public int getLatitude(){
        return(mLatitude);
    }
    
    public void setLatitude(int latitude){
        this.mLatitude=latitude; 
    }
    
    public int getLongitude(){
        return(mLongitude);
    }
    
    public void setLongitude(int longitude){
        this.mLongitude=longitude; 
    }
        
}

