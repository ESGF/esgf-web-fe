package org.esgf.searchConfig;

import java.util.ArrayList;
import java.util.List;

import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class LASConfiguration {


    private List<String> LASRegexList;
    
    
    public LASConfiguration() {
        setLASRegexList(new ArrayList<String>());
    }

    public LASConfiguration(List<String> LASRegexList)  {
        //this.LASRegexList = new ArrayList<String>();
        //for(int i=0;i<LASRegexList.size();i++) {
        //    this.LASRegexList.set(i, LASRegexList.get(i));
        //}
        //System.out.println(LASRegexList)
        this.LASRegexList = LASRegexList;
    }

    public List<String> getLASRegexList() {
        return LASRegexList;
    }


    public void setLASRegexList(List<String> LASRegexList) {
        this.LASRegexList = LASRegexList;
    }
    
    public void addLASRegex(String regex) {
        this.LASRegexList.add(regex);
    }
    
    public Element toElement() {
        
        
        
        Element lasConfigEl = new Element("lasconfig");
        
        for(int i=0;i<LASRegexList.size();i++) {
            Element regexEl = new Element("regex");
            regexEl.addContent(this.LASRegexList.get(i));
            lasConfigEl.addContent(regexEl);
        }
        
        return lasConfigEl;
    }

    /** Descriptionn of toXML()
     * 
     */
    public String toXML() {
        String xml = "";
        
        Element docEl = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(docEl);
        
        return xml;
    }


    /** Description of toJSON() 
     * 
     */
    public String toJSON() {
        String json = null;
        
        try {
            json = this.toJSONObject().toString(3);
        } catch (JSONException e) {
            System.out.println("Problem in toJSON");
            e.printStackTrace();
        }
        
        return json;
    }



    /** Description of toJSONObject() 
     * 
     */
    public JSONObject toJSONObject() {
        JSONObject json = null;
        
        try {
            json = XML.toJSONObject(this.toXML());
        } catch (JSONException e) {
            System.out.println("Problem in toJSONObject");
            e.printStackTrace();
        }
        
        return json;
    }
    

    
    
    
}
