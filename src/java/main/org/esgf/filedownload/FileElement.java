package org.esgf.filedownload;

import java.util.ArrayList;
import java.util.List;

import org.esgf.metadata.JSONArray;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class FileElement {
    
    private String fileId;
    private String title;
    private String size;
    private String hasGrid;
    private String hasHttp;
    
    private ServicesElement servicesElement;
    private URLSElement urlsElement;
    private MIMESElement mimesElement;
    
    public FileElement() {

        this.fileId = new String("");
        this.title = new String("");
        this.size = new String("");
        this.hasGrid = new String("false");
        this.hasHttp = new String("false");
        
        this.mimesElement = new MIMESElement();
        this.urlsElement = new URLSElement();
        this.servicesElement = new ServicesElement();
        
    }
    
    
    public String getFileId() {
        return fileId;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getHasGrid() {
        return hasGrid;
    }
    public void setHasGrid(String hasGrid) {
        this.hasGrid = hasGrid;
    }
    public String getHasHttp() {
        return hasHttp;
    }
    public void setHasHttp(String hasHttp) {
        this.hasHttp = hasHttp;
    }
    public ServicesElement getServicesElement() {
        return servicesElement;
    }
    public void setServicesElement(ServicesElement servicesElement) {
        this.servicesElement = servicesElement;
    }
    public URLSElement getUrlsElement() {
        return urlsElement;
    }
    public void setUrlsElement(URLSElement urlsElement) {
        this.urlsElement = urlsElement;
    }
    public MIMESElement getMimesElement() {
        return mimesElement;
    }
    public void setMimesElement(MIMESElement mimesElement) {
        this.mimesElement = mimesElement;
    }
    
    public void addService(String service) {
        this.servicesElement.addService(service);
    }
    
    public boolean removeService(String service) {
        return this.servicesElement.removeService(service);
    }
    
    
    public void addMIME(String mime) {
        this.mimesElement.addMIME(mime);
    }
    
    public boolean removeMIME(String mime) {
        return this.mimesElement.removeMIME(mime);
    }
    
    
    public void addURL(String url) {
        this.urlsElement.addURL(url);
    }
    
    public boolean removeURL(String url) {
        return this.urlsElement.removeURL(url);
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
        
        FileElement fe = new FileElement();
        
        ServicesElement se = new ServicesElement();
        String service = "service1";
        se.addService(service);
        service = "service2";
        se.addService(service);
        fe.setServicesElement(se);
        
        URLSElement ue = new URLSElement();
        String url = "url1";
        ue.addURL(url);
        url = "url2";
        ue.addURL(url);
        fe.setUrlsElement(ue);

        MIMESElement me = new MIMESElement();
        String mime = "mime1";
        me.addMIME(mime);
        mime = "mime2";
        me.addMIME(mime);
        fe.setMimesElement(me);
        
        String fileId = "fileId1";
        fe.setFileId(fileId);
        
        String title = "title1";
        fe.setTitle(title);
        
        String size = "size1";
        fe.setSize(size);
        
        String hasGrid = "true";
        fe.setHasGrid(hasGrid);
        
        String hasHttp = "true";
        fe.setHasHttp(hasHttp);
        
        System.out.println(fe);

        System.out.println(fe.toXML());
        
        fe.removeMIME("mime1");
        
        System.out.println(fe.toXML());
    }
    
    
}
