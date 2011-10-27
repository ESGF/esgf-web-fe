package org.esgf.filedownload;

import java.util.ArrayList;
import java.util.List;

import org.esgf.metadata.JSONArray;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class URLSElement {
    
    private List<String> urls;
    
    public URLSElement() {
        urls = new ArrayList<String>();
    }

    public void setURL(List<String> services) {
        this.urls = urls;
    }

    public List<String> getURLS() {
        return urls;
    }
    

    public boolean removeURL(String url) {
        boolean removed = false;
        for(int i=0;i<urls.size();i++) {
            if(urls.get(i).equals(url)) {
                urls.remove(i);
                removed = true;
            }
        }
        return removed;
    }
    
    public void addURL(String URL) {
        urls.add(URL);
    }
    
    
    public String toXML() {
        String xml = "";
        
        Element URLsElement = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(URLsElement);
        
        return xml;
    }
    
    public Element toElement() {
        Element urlsEl = new Element("urls");
        
        for(int i=0;i<urls.size();i++) {
            Element urlEl = new Element("url");
            urlEl.addContent(urls.get(i));
            urlsEl.addContent(urlEl);
        }
        
        return urlsEl;
    }
    
    public String toString() {
        String str = "urls\n";
        
        for(int i=0;i<urls.size();i++) {
            str += "\turl: " + i + " " + urls.get(i) + "\n";
        }
        
        return str;
    }
    
    
    public static void main(String [] args) {
        URLSElement me = new URLSElement();
        String URL = "URL1";
        me.addURL(URL);
        URL = "URL2";
        me.addURL(URL);
        System.out.println(me.getURLS().size());
        System.out.println(me);
        System.out.println(me.toXML());
        me.removeURL("URL2");
        System.out.println(me.toXML());
        
    }
    
}
