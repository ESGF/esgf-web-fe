package org.esgf.filedownload;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.esgf.metadata.JSONArray;
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class FileElement implements DataCartElement {
    
    private String fileId;
    private String title;
    private String size;
    private String hasGrid;
    private String hasHttp;
    private String hasOpenDap;
    
    private ServicesElement servicesElement;
    private URLSElement urlsElement;
    private MIMESElement mimesElement;
    private TechnotesElement technotesElement;
    
    private String hasTechnote;
    
    public FileElement() {

        this.fileId = new String("fileId");
        this.title = new String("title");
        this.size = new String("size");
        this.hasGrid = new String("false");
        this.hasHttp = new String("false");
        this.hasOpenDap = new String("false");
        this.hasTechnote = new String("false");
        
        this.mimesElement = new MIMESElement();
        this.urlsElement = new URLSElement();
        this.servicesElement = new ServicesElement();
        this.technotesElement = new TechnotesElement();
        
    }
    
    public FileElement(JSONObject docJSON, String string) {
        if(string.equals("solr")) {
            try {
                
                Iterator iter = docJSON.sortedKeys();
                
                boolean technote = false;
                
                while(iter.hasNext()) {
                    String key = (String)iter.next();

                    String value = docJSON.getString(key);
                    //System.out.println("key: " + key + " value: " + value);
                    //set the fileid
                    if(key.equals("id")) {
                        this.fileId = value;
                    } 
                    //set the file size element
                    else if(key.equals("size")) {
                        this.size = value;
                    }
                  //set the file title element
                    else if(key.equals("title")) {
                        this.title = value;
                    }
                    else if(key.equals("url")) {
                        
                        //set the urls, mimes, and services elements
                        URLSElement urlsElement = new URLSElement();
                        MIMESElement mimesElement = new MIMESElement();
                        ServicesElement servicesElement = new ServicesElement();
                        JSONArray urlsJSON = (JSONArray)docJSON.getJSONArray(key);
                        
                        for(int i=0;i<urlsJSON.length();i++) {
                            String urlStr = urlsJSON.get(i).toString();
                            
                            String [] urlStrTokens = urlStr.split("\\|");
                            
                            String url = urlStrTokens[0];
                            urlsElement.addURL(url);
                            
                            String mime = urlStrTokens[1];
                            mimesElement.addMIME(mime);
                            
                            String service = urlStrTokens[2];
                            servicesElement.addService(service);
                            //System.out.println("url: " + url + " " + " mime: " + mime + " service: " + service);
                            if(service.equals("OPENDAP")) {
                                this.hasOpenDap = "true";
                            } else if(service.equals("HTTPServer")) {
                                this.hasHttp = "true";
                            } else if(service.equals("GridFTP")) {
                                this.hasGrid = "true";
                            }
                        }

                        this.setUrlsElement(urlsElement);
                        this.setMimesElement(mimesElement);
                        this.setServicesElement(servicesElement);
                        
                        //come back to this
                        this.hasGrid = "true";
                        this.hasHttp = "true";
                        this.hasOpenDap = "true";

                    }
                    else if(key.equals("xlink")) {
                        technote = true;
                        
                        System.out.println("XLINK");
                        JSONArray xlinkJSON = (JSONArray)docJSON.getJSONArray(key);
                        TechnotesElement technotesElement = new TechnotesElement();
                        for(int i=0;i<xlinkJSON.length();i++) {
                            String xlinkStr = xlinkJSON.get(i).toString();
                            String [] xlinkStrTokens = xlinkStr.split("\\|");
                            String name = xlinkStrTokens[1];
                            String location = xlinkStrTokens[0];
                            
                            TechnoteElement technoteElement = new TechnoteElement();
                            technoteElement.setName(name);
                            technoteElement.setLocation(location);
                            
                            technotesElement.addTechnoteElement(technoteElement);
                        }
                        
                        this.setTechnotesElement(technotesElement);
                    }
                    
                    
                }
                
                if(!technote) {
                    this.setTechnotesElement(new TechnotesElement());
                    this.hasTechnote = "false";
                } else {
                    this.hasTechnote = "true";
                }
                
                /*
                
                JSONArray xlinkJSON = (JSONArray)docJSON.getJSONArray("xlink");
                
                if(xlinkJSON.length() > 0) {
                    this.hasTechnote = "true";
                }
                
                TechnotesElement technotesElement = new TechnotesElement();
                for(int i=0;i<xlinkJSON.length();i++) {
                    String xlinkStr = xlinkJSON.get(i).toString();
                    String [] xlinkStrTokens = xlinkStr.split("\\|");
                    String name = xlinkStrTokens[1];
                    String location = xlinkStrTokens[0];
                    
                    TechnoteElement technoteElement = new TechnoteElement();
                    technoteElement.setName(name);
                    technoteElement.setLocation(location);
                    
                    technotesElement.addTechnoteElement(technoteElement);
                }
                
                this.setTechnotesElement(technotesElement);
                */
            } catch (JSONException e) {
                System.out.println("ERROR in creating fileelement from solr");
                e.printStackTrace();
            }

        }
    }

    public String getHasTechnote() {
        return hasTechnote;
    }

    public void setHasTechnote(String hasTechnote) {
        this.hasTechnote = hasTechnote;
    }
    
    public TechnotesElement getTechnotesElement() {
        return technotesElement;
    }

    public void setTechnotesElement(TechnotesElement technotesElement) {
        this.technotesElement = technotesElement;
    }
    
    public String getFileId() {
        return fileId;
    }
    public void setFileId(String fileId) {
        if(fileId != null)
            this.fileId = fileId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        if(title != null)
            this.title = title;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        if(size != null)
            this.size = size;
    }
    public String getHasGrid() {
        return hasGrid;
    }
    public void setHasGrid(String hasGrid) {
        if(hasGrid != null)
            this.hasGrid = hasGrid;
    }
    public String getHasHttp() {
        return hasHttp;
    }
    public void setHasHttp(String hasHttp) {
        if(hasHttp != null)
            this.hasHttp = hasHttp;
    }
    public ServicesElement getServicesElement() {
        return servicesElement;
    }
    public void setServicesElement(ServicesElement servicesElement) {
        if(servicesElement != null) {
            List<String> services = servicesElement.getServices();
            for(int i=0;i<services.size();i++) {
                //System.out.println("Setting service: " + services.get(i));
                String service = services.get(i);
                if(service.contains("HTTPS")) {
                    this.hasHttp = new String("true");
                } else if(service.contains("OpenDAP")) {
                    this.setHasOpenDap(new String("true"));
                } else if(service.contains("GridFTP")) {
                    this.hasGrid = new String("true");
                }
            }
            this.servicesElement = servicesElement;
        }
        
    }
    public URLSElement getUrlsElement() {
        return urlsElement;
    }
    public void setUrlsElement(URLSElement urlsElement) {
        if(urlsElement != null)
            this.urlsElement = urlsElement;
    }
    public MIMESElement getMimesElement() {
        return mimesElement;
    }
    public void setMimesElement(MIMESElement mimesElement) {
        if(mimesElement != null)
            this.mimesElement = mimesElement;
    }
    
    public void addService(String service) {
        if(service != null) {
            if(service.contains("HTTPS")) {
                this.hasHttp = new String("true");
            } else if(service.contains("OpenDAP")) {
                this.setHasOpenDap(new String("true"));
            } else if(service.contains("GridFTP")) {
                this.hasGrid = new String("true");
            }
            this.servicesElement.addService(service);
        }
        
    }
    
    public boolean removeService(String service) {
        if(service != null)
            return this.servicesElement.removeService(service);
        else 
            return false;
    }
    
    
    public void addMIME(String mime) {
        if(mime != null)
            this.mimesElement.addMIME(mime);
    }
    
    public boolean removeMIME(String mime) {
        if(mime != null)
            return this.mimesElement.removeMIME(mime);
        else
            return false;
    }
    
    
    public void addURL(String url) {
        if(url != null)
            this.urlsElement.addURL(url);
    }
    
    public boolean removeURL(String url) {
        if(url != null)
            return this.urlsElement.removeURL(url);
        else
            return false;
    }
    
    public void addTechnote(TechnoteElement technote) {
        if(technote != null)
            this.technotesElement.addTechnoteElement(technote);
    }
    
    public boolean removeTechnote(TechnoteElement technote) {
        if(technote != null)
            return this.technotesElement.removeTechnoteElement(technote);
        else
            return false;
    }
    
    public Element toElement() {
        Element fileEl = new Element("file");
        
        Element fileidEl = new Element("fileId");
        fileidEl.addContent(this.fileId);
        fileEl.addContent(fileidEl);
        
        Element titleEl = new Element("title");
        titleEl.addContent(this.title);
        fileEl.addContent(titleEl);
        
        Element sizeEl = new Element("size");
        sizeEl.addContent(this.size);
        fileEl.addContent(sizeEl);
        
        Element hasGridEl = new Element("hasGrid");
        hasGridEl.addContent(this.hasGrid);
        fileEl.addContent(hasGridEl);
        
        Element hasHttpEl = new Element("hasHttp");
        hasHttpEl.addContent(this.hasHttp);
        fileEl.addContent(hasHttpEl);
        
        Element servicesEl = this.servicesElement.toElement();
        fileEl.addContent(servicesEl);
        
        Element urlsEl = this.urlsElement.toElement();
        fileEl.addContent(urlsEl);
        
        Element mimesEl = this.mimesElement.toElement();
        fileEl.addContent(mimesEl);

        Element technotesEl = this.technotesElement.toElement();
        fileEl.addContent(technotesEl);
        
        return fileEl;
    }
    
    public String toString() {
        String str = "file element\n";

        str += "\tfileid: " + this.fileId + "\n";
        str += "\ttitle: " + this.title + "\n";
        str += "\tsize: " + this.size + "\n";
        str += "\thasGrid: " + this.hasGrid + "\n";
        str += "\thasHttp: " + this.hasHttp + "\n";
        str += this.servicesElement.toString();
        str += this.urlsElement.toString();
        str += this.mimesElement.toString();
        str += this.technotesElement.toString();
        
        return str;
    }
    
    public String toXML() {
        String xml = "";
        
        Element servicesElement = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(servicesElement);
        
        return xml;
    }
    
    public static void main(String [] args) {
        
    }


    public void setHasOpenDap(String hasOpenDap) {
        this.hasOpenDap = hasOpenDap;
    }


    public String getHasOpenDap() {
        return hasOpenDap;
    }
    
    
}
