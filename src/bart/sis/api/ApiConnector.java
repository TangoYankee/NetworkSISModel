/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bart.sis.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author miller.tim
 */
public class ApiConnector{
    private String mKey;
    private String mSection;
    private String mCmd;
    private URL mURL;
    private final String mClear = "";
    private String mExtend = mClear;
    
    private final String mBase = "http://api.bart.gov/api/%s.aspx?cmd=%s&key=%s";
    private final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private final DocumentBuilder dBuilder;
    private Document doc;
    
    public ApiConnector(String key)
            throws ParserConfigurationException{
        mKey = key;
        dBuilder = dbFactory.newDocumentBuilder();
    }
    
    public Document CallData() throws SAXException, IOException{
        doc = dBuilder.parse(mURL.openStream());
        return (doc);
        }
          
    public void setSectCmdClearExtend(String section, String cmd) throws MalformedURLException{
        mSection = section;
        mCmd = cmd;
        clearExtend();
        setURL();
    }
    
    public String getSection(){
        return this.mSection;
    }
    
    public String getCommand(){
        return this.mCmd;
    }
    
    public void setExtend(String extend) throws MalformedURLException{
        this.mExtend = extend;
        setURL();
    }
    
    public void clearExtend() throws MalformedURLException{
        this.mExtend = mClear;
        setURL();
    }
    
    public void setKey(String key) throws MalformedURLException{
        this.mKey = key;
        setURL();
    }
    
    public String getKey(){
        return this.mKey;
    }
    
    private void setURL() throws MalformedURLException{
        this.mURL = new URL(String.format(mBase + mExtend, mSection, mCmd, mKey));
    }
    
    public String getURL(){
        return this.mURL.toString();
    }
    
}
