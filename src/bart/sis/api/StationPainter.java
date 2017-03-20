/*
Color the nodes based off its state(Healthy/Infected)
 */
package bart.sis.api;

import java.awt.Color;
import java.awt.Paint;
import java.util.Hashtable;
import org.apache.commons.collections15.Transformer;

class StationPainter implements Transformer<String, Paint> {
    private final Hashtable<String, StationObject> mNodeTable;

    public StationPainter(Hashtable<String, StationObject> nodeTable) {
        mNodeTable = nodeTable;
    }
    
    //Get information for each vertex from the hashtable
    @Override
    public Paint transform(String n){
        String state = mNodeTable.get(n).getState();
        //System.out.println("n: " +n);
        Color foundState;
        switch(state){
            case "Healthy": foundState = Color.white;
                break;
            case "Infected": foundState = Color.gray;
                break;
            default: foundState = Color.black;
                break;
        }
        return(foundState);
    }
}
