/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bart.sis.api;

/**
 *
 * @author miller.tim
 */
public class RouteObject {
    //private final String mEdgeID;
    private final String mAbbr;
    private final String mRouteID;
    private final String mHexColor;
    private final String mOrig;
    private final String mDest;
    
    public RouteObject(String routeID, String hexColor, String orig, String dest, String abbr){
        //mEdgeID = edgeID;
        mRouteID = routeID;
        mHexColor = hexColor;
        mOrig = orig;
        mDest = dest;  
        //mAbbr = String.format("%s:%s-%s", mRouteID, mOrig, mDest);
        mAbbr = abbr;
    }
    

    
    public String getAbbr(){
        return(mAbbr);
    }
    
    public String getRouteID(){
        return (mRouteID);
    }
    
    public String getHexColor(){
        return(mHexColor);
    }

    public String getOrig(){
        return(mOrig);
    }
    
    public String getDest(){
        return(mDest);
    }
    
}
