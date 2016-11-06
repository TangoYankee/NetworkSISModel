/*
Manage the CSV files for the SQL database
 */
package bart.sql;

import java.io.FileNotFoundException;
import com.opencsv.CSVReader;
import java.io.FileReader;
        
        
public class importCSV {
    private final String mFilename;
    CSVReader mReader = null;
    
    public importCSV(String filename) 
            throws FileNotFoundException{
        mFilename = filename;
        mReader = new CSVReader(new FileReader(mFilename));
    }
    
    public CSVReader getFileReader(){
        return(mReader);
    }
       
}
