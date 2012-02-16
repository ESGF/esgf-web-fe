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
    
    private String tracking_id;
    private String checksum;
    private String checksum_type;
    
    private String hasGrid;
    private String hasHttp;
    private String hasOpenDap;
    
    private ServicesElement servicesElement;
    private URLSElement urlsElement;
    private MIMESElement mimesElement;
    private TechnotesElement technotesElement;
    
    private String hasTechnote;
    
    public FileElement() {

        this.initialize();
        
    }
    
    private void initialize() {
        this.fileId = new String("fileId");
        this.title = new String("title");
        this.size = new String("size");
        
        this.tracking_id = new String("tracking_id");
        this.checksum = new String("checksum");
        this.checksum_type = new String("checksum_type");
        
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

                this.initialize();
                
                
                Iterator iter = docJSON.sortedKeys();
                
                boolean technote = false;

                this.tracking_id = "unknown";
                this.checksum = "unknown";
                this.checksum_type = "unknown";
                
                while(iter.hasNext()) {
                    String key = (String)iter.next();

                    String value = docJSON.getString(key);
                    //System.out.println("key: " + key + " value: " + value);
                    //set the fileid
                    if(key.equals("id")) {
                        //System.out.println("id value: " + value);
                        this.fileId = value;
                        if(this.fileId.equals("cmip5.output1.NCC.NorESM1-M.historicalExt.6hr.atmos.6hrLev.r2i1p1.v20111102.hus_6hrLev_NorESM1-M_historicalExt_r2i1p1_2006010100-2006063018.nc|bmbf-ipcc-ar5.dkrz.de")){
                            //System.out.println("file");
                        }
                    } 
                    //set the file size element
                    else if(key.equals("size")) {
                        this.size = value;
                    }
                    //set the file title element
                    else if(key.equals("title")) {
                        this.title = value;
                    }
                    //set the file title element
                    else if(key.equals("checksum")) {

                        JSONArray checksumsJSON = (JSONArray)docJSON.getJSONArray(key);
                        this.checksum = (String)checksumsJSON.get(0);
                        //System.out.println("                   checksum: " + this.checksum);
                        
                    }
                    //set the file title element
                    else if(key.equals("tracking_id")) {
                        JSONArray trackingIdJSON = (JSONArray)docJSON.getJSONArray(key);
                        this.tracking_id = (String)trackingIdJSON.get(0);
                        //System.out.println("                   TrackingId: " + this.tracking_id);
                    }
                    //set the file title element
                    else if(key.equals("checksum_type")) {
                        JSONArray checksum_typeJSON = (JSONArray)docJSON.getJSONArray(key);
                        
                        this.checksum_type = (String)checksum_typeJSON.get(0);
                        //System.out.println("Checksum Type: " + this.checksum_type);
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
                            if(service.equals("OPENDAP")) {
                                this.hasOpenDap = "true";
                                //System.out.print("\tService is an OPendap");
                            } else if(service.equals("HTTPServer")) {
                                //System.out.print("\tService is an HTTP");
                                this.hasHttp = "true";
                            } else if(service.equals("GridFTP")) {
                                this.hasGrid = "true";
                                //System.out.print("\tService is an GRIDFTP");
                            }
                        }

                        
                        this.setUrlsElement(urlsElement);
                        this.setMimesElement(mimesElement);
                        this.setServicesElement(servicesElement);

                        
                        
                        //come back to this
                        //this.hasGrid = "true";
                        //this.hasHttp = "true";
                        //this.hasOpenDap = "true";

                    }
                    else if(key.equals("xlink")) {
                        technote = true;
                        
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
    
    public String getTrackingId() {
        return tracking_id;
    }

    public void setTrackingId(String tracking_id) {
        this.tracking_id = tracking_id;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
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
        
        Element checksumEl = new Element("checksum");
        checksumEl.addContent(this.checksum);
        fileEl.addContent(checksumEl);
        
        Element checksum_typeEl = new Element("checksum_type");
        checksum_typeEl.addContent(this.checksum_type);
        fileEl.addContent(checksum_typeEl);
        
        Element tracking_idEl = new Element("tracking_id");
        tracking_idEl.addContent(this.tracking_id);
        fileEl.addContent(tracking_idEl);
        
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
