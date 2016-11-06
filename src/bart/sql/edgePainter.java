/*
Color the edges based off its Line (Red, Yellow, etc...)
 */
package bart.sql;

import java.awt.Color;
import java.awt.Paint;
import java.util.Hashtable;
import org.apache.commons.collections15.Transformer;

class edgePainter implements Transformer<String, Paint>{
    private final Hashtable<String, edgeData> mEdgeTable;
    
    public edgePainter(Hashtable<String, edgeData> edgeTable){
        mEdgeTable = edgeTable;
    }

    @Override
    public Paint transform(String i) {
        String line = mEdgeTable.get(i).getColor();
        Color foundLine;
        switch(line){
        case "Red": foundLine = Color.red;
            break;
        case "Orange": foundLine = Color.orange;
            break;
        case "Blue": foundLine = Color.blue;
            break;
        case "Yellow": foundLine = Color.yellow;
            break;
        case "Green": foundLine = Color.green;
            break;
        case "Transfer": foundLine = Color.white;
            break;
        default: foundLine = Color.black;
            break;
        }
        return (foundLine);
      
        }
    
}