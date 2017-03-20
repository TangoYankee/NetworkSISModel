/*
Color the edges based off its Line (Red, Yellow, etc...)
 */
package bart.sis.api;

import java.awt.Color;
import java.awt.Paint;
import java.util.Hashtable;
import org.apache.commons.collections15.Transformer;

class RoutePainter implements Transformer<String, Paint>{
    private final Hashtable<String, RouteObject> mRouteTable;
    
    public RoutePainter(Hashtable<String, RouteObject> routeTable){
        mRouteTable = routeTable;
    }

    @Override
    public Paint transform(String i) {
        String line = mRouteTable.get(i).getHexColor();
        //System.out.println(line);
        Color foundLine;
        switch(line){
        case "#ff0000": foundLine = Color.red;
            break;
        case "#ff9933": foundLine = Color.orange;
            break;
        case "#0099cc": foundLine = Color.blue;
            break;
        case "#ffff33": foundLine = Color.yellow;
            break;
        case "#339933": foundLine = Color.green;
            break;
        case "#d5cfa3": foundLine = Color.white;
            break;
        default: foundLine = Color.black;
            break;
        }
        return (foundLine);
      
        }
    
}