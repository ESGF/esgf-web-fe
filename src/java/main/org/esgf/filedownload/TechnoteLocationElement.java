package org.esgf.filedownload;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class TechnoteLocationElement  implements DataCartElement{
    
    private List<String> techNoteLocations;
    
    public TechnoteLocationElement() {
        techNoteLocations = new ArrayList<String>();
        techNoteLocations.add("techNoteLocations | t | a");
    }
    
    
    public void setTechNoteLocations(List<String> techNoteLocations) {
        this.techNoteLocations = techNoteLocations;
    }

    public List<String> getTechNoteLocations() {
        return techNoteLocations;
    }
    
    
    public boolean removeTechnoteLocation(String techNoteLocation) {
        boolean removed = false;
        for(int i=0;i<techNoteLocations.size();i++) {
            if(techNoteLocations.get(i).equals(techNoteLocation)) {
                techNoteLocations.remove(i);
                removed = true;
            }
        }
        return removed;
    }
    
    public void addTechnoteLocation(String techNoteLocation) {
        techNoteLocations.add(techNoteLocation);
    }
    

    @Override
    public String toXML() {
        String xml = "";
        
        Element techNoteLocationsElement = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(techNoteLocationsElement);
        
        return xml;
    }


    @Override
    public Element toElement() {
        Element techNoteLocationsEl = new Element("techNoteLocations");
        
        for(int i=0;i<techNoteLocations.size();i++) {
            Element techNoteLocationEl = new Element("techNoteLocation");
            techNoteLocationEl.addContent(techNoteLocations.get(i));
            techNoteLocationsEl.addContent(techNoteLocationEl);
        }
        
        return techNoteLocationsEl;
    }
    
    

}
