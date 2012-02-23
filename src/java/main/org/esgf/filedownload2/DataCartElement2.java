package org.esgf.filedownload2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public interface DataCartElement2 {

    public Element toElement();
    

    /** Description of toXML()
     * 
     * @return
     */
    public String toXML();

    /** Description of toJSONObject()
     * 
     * @return
     */
    public JSONObject toJSONObject();
    
    
    public String toJSON();
    
    
    
    /** Description of toFile()
     * 
     * @param file Filename of the output
     */
    public void toFile(String file);
    
    
    public void fromFile(String file);
    
}
