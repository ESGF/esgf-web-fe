package org.esgf.searchConfig;

import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class SearchConfiguration {


    private String enableGlobusOnline;
    
    public SearchConfiguration() {
        
        //default for enable globus online is false
        this.setEnableGlobusOnline("false");
    }

    public String isEnableGlobusOnline() {
        return enableGlobusOnline;
    }

    public void setEnableGlobusOnline(String enableGlobusOnline) {
        this.enableGlobusOnline = enableGlobusOnline;
    }

    public String getEnableGlobusOnline() {
        return enableGlobusOnline;
    }

    public Element toElement() {
     
        Element paramsEl = new Element("params");
        Element enableGlobusOnlineEl = new Element("enableGlobusOnline");
        enableGlobusOnlineEl.addContent(this.enableGlobusOnline);
        paramsEl.addContent(enableGlobusOnlineEl);
        return paramsEl;
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
