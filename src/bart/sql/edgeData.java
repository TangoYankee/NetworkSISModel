/*
Object for edges
 */
package bart.sql;

public class edgeData {
    private final int mID;
    private String mColor;
    
    public edgeData(int x, String color){
        mID = x;
        mColor = color;
    }
    
    public int getID(){
        return(mID);
    }

    public void setColor(String color){
        this.mColor = color;
    }
    
    public String getColor(){
        return(mColor);
    }
    
}
