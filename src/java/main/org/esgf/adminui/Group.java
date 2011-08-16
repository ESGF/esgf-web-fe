package org.esgf.adminui;

import java.io.Serializable;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class Group implements Serializable{

    private String id;
    private String name;
    private String description;
    
    public Group() {
        this.id = "group_id";
        this.name = "group_name";
        this.description = "group_description";
    }
    
    public Group(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    
    public void setid(String id) {
        this.id = id;
    }
    public String getid() {
        if(this.id != null)
            return this.id;
        else
            return "N/A";
    }
    public void setname(String name) {
        this.name = name;
    }
    public String getname() {
        if(this.name != null)
            return this.name;
        else
            return "N/A";
    }
    public void setdescription(String description) {
        this.description = description;
    }
    public String getdescription() {
        if(this.description != null)
            return this.description;
        else
            return "N/A";
    }
    
    public String toString() {
        String str = "";
        
        str += "Group Information" + System.getProperty("line.separator");
        str += "\tGroupId: " + this.id + System.getProperty("line.separator");
        str += "\tGroupName: " + this.name + System.getProperty("line.separator");
        str += "\tGroupDescription: " + this.description + System.getProperty("line.separator");
        
        
        return str;
    }
    
    public String toXml() {
        String xmlContent = "";
        
        Element groupElement = new Element("group");
        
        Element groupIdEl = new Element("groupid");
        if(this.id != null) {
            groupIdEl.addContent(this.id);
        }
        else {
            groupIdEl.addContent("N/A");
        }
        
        
        Element groupNameEl = new Element("groupname");
        if(this.name != null) {
            groupNameEl.addContent(this.name);
        }
        else {
            groupNameEl.addContent("N/A");
        }
        
        
        
        Element groupDescriptionEl = new Element("groupdescription");
        if(this.description != null) {
            groupDescriptionEl.addContent(this.description);
        }
        else {
            groupDescriptionEl.addContent("N/A");
        }
        
        
        groupElement.addContent(groupIdEl);
        groupElement.addContent(groupNameEl);
        groupElement.addContent(groupDescriptionEl);

        XMLOutputter outputter = new XMLOutputter();
        xmlContent = outputter.outputString(groupElement);
        
        
        return xmlContent;
    }
    
    
}
