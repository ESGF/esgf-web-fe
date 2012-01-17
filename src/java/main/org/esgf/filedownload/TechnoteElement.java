package org.esgf.filedownload;

import java.util.List;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class TechnoteElement implements DataCartElement {

    private String name;
    private String location;

    public TechnoteElement() {
        this.name = "technoteName";
        this.location = "technoteLocation";
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toXML() {
        String xml = "";
        
        Element technoteElement = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(technoteElement);
        
        return xml;
    }

    @Override
    public Element toElement() {
        Element technoteEl = new Element("technote");
        
        Element technoteNameEl = new Element("name");
        technoteNameEl.addContent(this.name);
        
        Element technoteLocationEl = new Element("location");
        technoteLocationEl.addContent(this.location);
        
        technoteEl.addContent(technoteNameEl);
        technoteEl.addContent(technoteLocationEl);
        return technoteEl;
    }

    
    
}
