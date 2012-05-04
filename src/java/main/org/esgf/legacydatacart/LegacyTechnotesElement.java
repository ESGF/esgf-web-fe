package org.esgf.legacydatacart;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class LegacyTechnotesElement implements LegacyDataCartElement{

    private List<LegacyTechnoteElement> technotes;
    
    public LegacyTechnotesElement() {
        technotes = new ArrayList<LegacyTechnoteElement>();
        technotes.add(new LegacyTechnoteElement());
        technotes.add(new LegacyTechnoteElement());
    }
    public List<LegacyTechnoteElement> getTechnotes() {
        return technotes;
    }

    public void setTechnotes(List<LegacyTechnoteElement> technotes) {
        this.technotes = technotes;
    }
    
    public void addTechnoteElement(LegacyTechnoteElement te) {
        this.technotes.add(te);
    }
    
    @Override
    public String toXML() {
        String xml = "";
        
        Element technotesElement = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(technotesElement);
        
        return xml;
    }
    
    @Override
    public Element toElement() {

        Element technoteEl = new Element("technotes");
        
        for(int i=0;i<technotes.size();i++) {
            technoteEl.addContent(technotes.get(i).toElement());
        }
        
        return technoteEl;
    }
    
    public String toString() {
        String str = "Technotes\n";
        
        for(int i=0;i<technotes.size();i++) {
            str += "\tTechnote: " + i + " " + technotes.get(i).getName() + " " + technotes.get(i).getLocation() + "\n";
        }
        
        return str;
    }
    
    
    public boolean removeTechnoteElement(LegacyTechnoteElement technote) {
        // TODO Auto-generated method stub
        return false;
    }

    
}
