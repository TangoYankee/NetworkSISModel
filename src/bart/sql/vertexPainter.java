/*
Color the nodes based off its state(Healthy/Infected)
 */
package bart.sql;

import java.awt.Color;
import java.awt.Paint;
import java.util.Hashtable;
import org.apache.commons.collections15.Transformer;

class vertexPainter implements Transformer<String, Paint> {
    private final Hashtable<String, nodeData> mNodeTable;

    public vertexPainter(Hashtable<String, nodeData> nodeTable) {
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
