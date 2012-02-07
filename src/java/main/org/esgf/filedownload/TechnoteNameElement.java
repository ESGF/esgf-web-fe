package org.esgf.filedownload;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class TechnoteNameElement  implements DataCartElement{
    
    private List<String> techNoteNames;

    public TechnoteNameElement() {
        techNoteNames = new ArrayList<String>();
        techNoteNames.add("techNoteNames");
    }
    
    
    public void setTechNoteNames(List<String> techNoteNames) {
        this.techNoteNames = techNoteNames;
    }

    public List<String> getTechNoteNames() {
        return techNoteNames;
    }

    public boolean removeTechnoteName(String techNoteName) {
        boolean removed = false;
        for(int i=0;i<techNoteNames.size();i++) {
            if(techNoteNames.get(i).equals(techNoteName)) {
                techNoteNames.remove(i);
                removed = true;
            }
        }
        return removed;
    }
    
    public void addTechnoteName(String techNoteName) {
        techNoteNames.add(techNoteName);
    }

    @Override
    public String toXML() {
        String xml = "";
        
        Element techNoteNamesElement = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(techNoteNamesElement);
        
        return xml;
    }


    @Override
    public Element toElement() {
        Element techNoteNamesEl = new Element("techNoteNames");
        
        for(int i=0;i<techNoteNames.size();i++) {
            Element techNoteNameEl = new Element("techNoteName");
            techNoteNameEl.addContent(techNoteNames.get(i));
            techNoteNamesEl.addContent(techNoteNameEl);
        }
        
        return techNoteNamesEl;
    }
    
}
