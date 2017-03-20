/*
Pull the location of nodes from the Hashtable. This data is used to place the
nodes in the visualization
 */
package bart.sis.api;

import java.awt.geom.Point2D;
import java.util.Hashtable;
import org.apache.commons.collections15.Transformer;

class LocationTransformer implements Transformer<String, Point2D>{
    private final Hashtable<String, StationObject> mStationTable;
    
    public LocationTransformer(Hashtable<String, StationObject> stationTable){
        mStationTable = stationTable;
    }
    
    @Override
    public Point2D transform(String n){
        double lat = mStationTable.get(n).getYCoord();
        double lng = mStationTable.get(n).getXCoord();
        
        return(new Point2D.Double(lng, lat));
    }
}

