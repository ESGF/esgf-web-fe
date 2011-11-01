package org.esgf.filedownload;

import java.util.ArrayList;
import java.util.List;

import org.esgf.metadata.JSONArray;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class ServicesElement {
    
    private List<String> services;
    
    public ServicesElement() {
        services = new ArrayList<String>();
        services.add("service");
    }

    public void setService(List<String> services) {
        this.services = services;
    }

    public List<String> getServices() {
        return services;
    }
    

    public boolean removeService(String service) {
        boolean removed = false;
        for(int i=0;i<services.size();i++) {
            if(services.get(i).equals(service)) {
                services.remove(i);
                removed = true;
            }
        }
        return removed;
    }
    
    public void addService(String service) {
        services.add(service);
    }
    
    
    public String toXML() {
        String xml = "";
        
        Element servicesElement = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(servicesElement);
        
        return xml;
    }
    
    public Element toElement() {
        Element servicesEl = new Element("services");
        
        for(int i=0;i<services.size();i++) {
            Element serviceEl = new Element("service");
            serviceEl.addContent(services.get(i));
            servicesEl.addContent(serviceEl);
        }
        
        return servicesEl;
    }
    
    public String toString() {
        String str = "services\n";
        
        for(int i=0;i<services.size();i++) {
            str += "\tservice: " + i + " " + services.get(i) + "\n";
        }
        
        return str;
    }
    
    
    public static void main(String [] args) {
        ServicesElement se = new ServicesElement();
        String service = "service1";
        se.addService(service);
        service = "service2";
        se.addService(service);
        System.out.println(se.getServices().size());
        System.out.println(se);
        System.out.println(se.toXML());
        se.removeService("service2");
        System.out.println(se.toXML());
        
    }
    
}
