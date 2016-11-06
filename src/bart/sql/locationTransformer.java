/*
Pull the location of nodes from the Hashtable. This data is used to place the
nodes in the visualization
 */
package bart.sql;

import java.awt.geom.Point2D;
import java.util.Hashtable;
import org.apache.commons.collections15.Transformer;

class locationTransformer implements Transformer<String, Point2D>{
    private final Hashtable<String, nodeData> mNodeTable;
    
    public locationTransformer(Hashtable<String, nodeData> nodeTable){
        mNodeTable = nodeTable;
    }
    
    @Override
    public Point2D transform(String n){

        double lat = mNodeTable.get(n).getLatitude();
        double lng = mNodeTable.get(n).getLongitude();
        
        return(new Point2D.Double(lat, lng));
    }
}

