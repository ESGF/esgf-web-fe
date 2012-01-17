package org.esgf.filedownload;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class TechnotesElement implements DataCartElement{

    private List<TechnoteElement> technotes;
    
    public TechnotesElement() {
        technotes = new ArrayList<TechnoteElement>();
        technotes.add(new TechnoteElement());
        technotes.add(new TechnoteElement());
    }
    public List<TechnoteElement> getTechnotes() {
        return technotes;
    }

    public void setTechnotes(List<TechnoteElement> technotes) {
        this.technotes = technotes;
    }
    
    public void addTechnoteElement(TechnoteElement te) {
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
    
    
    public boolean removeTechnoteElement(TechnoteElement technote) {
        // TODO Auto-generated method stub
        return false;
    }

    
}
