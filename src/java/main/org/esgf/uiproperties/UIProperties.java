package org.esgf.uiproperties;

import java.util.ArrayList;
import java.util.List;

import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class UIProperties {

    private String enableGlobusOnline;

    private List<String> lasExclusions;
    
    private String datacartMax;
    
    private String defaultFileCounter;
    
    private String defaultDatasetCounter;
    
    public UIProperties() {
      
        this.setEnableGlobusOnline("false");
        this.setLasExclusions(new ArrayList<String>());
        this.setDatacartMax("100");
        this.setDefaultFileCounter("10");
        this.setDefaultDatasetCounter("10");
    
    }

    public String getEnableGlobusOnline() {
        return enableGlobusOnline;
    }

    public void setEnableGlobusOnline(String enableGlobusOnline) {
        this.enableGlobusOnline = enableGlobusOnline;
    }

    public List<String> getLasExclusions() {
        return lasExclusions;
    }

    public void setLasExclusions(List<String> lasExclusions) {
        this.lasExclusions = lasExclusions;
    }
    
    public Element toElement() {
        
        Element uipropertiesEl = new Element("uiproperties");

        Element enableGlobusOnlineEl = new Element("enableGlobusOnline");
        enableGlobusOnlineEl.addContent(this.enableGlobusOnline);
        uipropertiesEl.addContent(enableGlobusOnlineEl);

        Element datacartMaxEl = new Element("datacartMax");
        datacartMaxEl.addContent(this.datacartMax);
        uipropertiesEl.addContent(datacartMaxEl);

        Element defaultFileCounterEl = new Element("defaultFileCounter");
        defaultFileCounterEl.addContent(this.defaultFileCounter);
        uipropertiesEl.addContent(defaultFileCounterEl);

        Element defaultDatasetCounterEl = new Element("defaultDatasetCounter");
        defaultDatasetCounterEl.addContent(this.defaultDatasetCounter);
        uipropertiesEl.addContent(defaultDatasetCounterEl);
        
        for(int i=0;i<this.lasExclusions.size();i++) {

            Element lasExcludeEl = new Element("lasexclude");
            lasExcludeEl.addContent(this.lasExclusions.get(i));
            uipropertiesEl.addContent(lasExcludeEl);
            
        }
        
        return uipropertiesEl;
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

    public String getDatacartMax() {
        return datacartMax;
    }

    public void setDatacartMax(String datacartMax) {
        this.datacartMax = datacartMax;
    }

    public String getDefaultFileCounter() {
        return defaultFileCounter;
    }

    public void setDefaultFileCounter(String defaultFileCounter) {
        this.defaultFileCounter = defaultFileCounter;
    }

    public String getDefaultDatasetCounter() {
        return defaultDatasetCounter;
    }

    public void setDefaultDatasetCounter(String defaultDatasetCounter) {
        this.defaultDatasetCounter = defaultDatasetCounter;
    }
    
    
    
}
