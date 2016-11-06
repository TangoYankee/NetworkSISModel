/*
Object for infections
 */
package bart.sql;

/**
 *
 * @author miller.tim
 */
public class infectionData {
    private final int mID;
    private String mName;
    private double mInfectRate;
    private double mHealRate;
    
    public infectionData(int ID, String name, double infectRate, double healRate){
        mID = ID;
        mName = name;
        mInfectRate = infectRate;
        mHealRate = healRate;
    }
    
    public int getID(){
        return(this.mID);
    }
    
    public String getName(){
        return(mName);
    }
    
    public void setName(String name){
        this.mName = name;
    }
    
    public double getInfectionRate(){
        return(mInfectRate);
    }
    
    public void set(double infectRate){
        this.mInfectRate = infectRate;
    }
    
    public double getHealRate(){
        return(mHealRate);
    }
    
    public void setHealRate(double healRate){
        this.mHealRate = healRate;
    }

}
