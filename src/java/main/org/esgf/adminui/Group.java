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
        return id;
    }
    public void setname(String name) {
        this.name = name;
    }
    public String getname() {
        return name;
    }
    public void setdescription(String description) {
        this.description = description;
    }
    public String getdescription() {
        return description;
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
        groupIdEl.addContent(this.id);
        Element groupNameEl = new Element("groupname");
        groupNameEl.addContent(this.name);
        Element groupDescriptionEl = new Element("groupdescription");
        groupDescriptionEl.addContent(this.description);
        
        
        groupElement.addContent(groupIdEl);
        groupElement.addContent(groupNameEl);
        groupElement.addContent(groupDescriptionEl);

        XMLOutputter outputter = new XMLOutputter();
        xmlContent = outputter.outputString(groupElement);
        
        
        return xmlContent;
    }
    
    
}
