package org.esgf.legacydatacart;

import java.util.ArrayList;
import java.util.List;

import org.esgf.metadata.JSONArray;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class LegacyDocElement implements LegacyDataCartElement {
    
    private String datasetId;
    private int count;
    
    private String hasHttp;
    private String hasOpenDap;
    private String hasGridFTP;
    private List<LegacyFileElement> fileElements;


    private LegacyTechnotesElement technotesElement;
    
    public LegacyDocElement() {
        this.datasetId = new String("");
        this.fileElements = new ArrayList<LegacyFileElement>();
        LegacyFileElement blankElement = new LegacyFileElement();
        this.fileElements.add(blankElement);
        this.fileElements.add(blankElement);
        this.technotesElement = new LegacyTechnotesElement();
        this.setCount(0);
        this.setHasGridFTP("0");
        this.setHasHttp("0");
        this.setHasOpenDap("0");
        
        
    }
    

    public LegacyTechnotesElement getTechnotesElement() {
        return technotesElement;
    }


    public void setTechnotesElement(LegacyTechnotesElement technotesElement) {
        this.technotesElement = technotesElement;
    }
    
    public String getDatasetId() {
        return datasetId;
    }


    public void setDatasetId(String datasetId) {
        if(datasetId != null)
            this.datasetId = datasetId;
    }


    public List<LegacyFileElement> getFileElements() {
        return fileElements;
    }


    public void setFileElements(List<LegacyFileElement> fileElements) {
        if(fileElements != null) {
            List<LegacyFileElement> newFileElements = new ArrayList<LegacyFileElement>();
            LegacyFileElement blankElement = new LegacyFileElement();
            newFileElements.add(blankElement);
            newFileElements.add(blankElement);
            newFileElements.addAll(fileElements);
            this.count = fileElements.size();
            this.fileElements = newFileElements;
        }
    }
    
    public String getHasHttp() {
        return hasHttp;
    }


    public void setHasHttp(String hasHttp) {
        if(hasHttp != null)
            this.hasHttp = hasHttp;
    }


    public String getHasOpenDap() {
        return hasOpenDap;
    }


    public void setHasOpenDap(String hasOpenDap) {
        if(hasOpenDap != null)
            this.hasOpenDap = hasOpenDap;
    }


    public String getHasGridFTP() {
        return hasGridFTP;
    }


    public void setHasGridFTP(String hasGridFTP) {
        if(hasGridFTP != null)
            this.hasGridFTP = hasGridFTP;
    }


    public void addFileElement(LegacyFileElement fileElement) {
        if(fileElement != null) {
            this.fileElements.add(fileElement);
        }
    }
    
    public void removeFileElement(String fileId) {
        if(fileId != null) {
            for(int i=0;i<this.fileElements.size();i++) {
                LegacyFileElement fe = this.fileElements.get(i);
                if(fe.getFileId().equals(fileId)) {
                    this.count--;
                    this.fileElements.remove(i);
                }
            }
        }
    }

    public void addMIMEElement(String fileId,String mime) {
        if(fileId != null && mime != null) {
            for(int i=0;i<this.fileElements.size();i++) {
                LegacyFileElement fe = this.fileElements.get(i);
                if(fe.getFileId().equals(fileId)) {
                    fe.addMIME(mime);
                }
            }
        }
        
    }
    
    public void removeMIMEElement(String fileId,String mime) {
        if(fileId != null && mime != null) {
            for(int i=0;i<this.fileElements.size();i++) {
                LegacyFileElement fe = this.fileElements.get(i);
                if(fe.getFileId().equals(fileId)) {
                    fe.removeMIME(mime);
                }
            }
        }
    }
    
    public void addServiceElement(String fileId,String service) {
        if(fileId != null && service != null) {
            for(int i=0;i<this.fileElements.size();i++) {
                LegacyFileElement fe = this.fileElements.get(i);
                if(fe.getFileId().equals(fileId)) {
                    fe.addService(service);
                }
            }
        }
    }
    
    public void removeServiceElement(String fileId,String service) {
        if(fileId != null && service != null) {
            for(int i=0;i<this.fileElements.size();i++) {
                LegacyFileElement fe = this.fileElements.get(i);
                if(fe.getFileId().equals(fileId)) {
                    fe.removeService(service);
                }
            }
        }
    }
    

    public void addTechnote(LegacyTechnoteElement te) {
        if(te != null) {
            this.technotesElement.addTechnoteElement(te);
        }
    }
    
    public void removeTechnote(LegacyTechnoteElement te) {
        if(te != null) {
            this.technotesElement.removeTechnoteElement(te);
        }
    }
    
    public void addURLElement(String fileId, String url) {
        if(fileId != null && url != null) {
            for(int i=0;i<this.fileElements.size();i++) {
                LegacyFileElement fe = this.fileElements.get(i);
                if(fe.getFileId().equals(fileId)) {
                    fe.addURL(url);
                }
            }
        }
    }
    
    public void removeURLElement(String fileId,String url) {
        if(fileId != null && url != null) {
            for(int i=0;i<this.fileElements.size();i++) {
                LegacyFileElement fe = this.fileElements.get(i);
                if(fe.getFileId().equals(fileId)) {
                    fe.removeURL(url);
                }
            }
        }
    }
    


    public String toString() {
        String str = "doc\n";
        
        str += "\tdatasetId: " + this.datasetId + "\n";
        
        for(int i=0;i<fileElements.size();i++) {
            str += "\tFILE: " + i + " \n\t-----\n" + fileElements.get(i).toString() + "\n";
        }
        
        return str;
    }
    
    public Element toElement() {
        Element docEl = new Element("doc");

        Element datasetidEl = new Element("datasetId");
        datasetidEl.addContent(this.datasetId);
        docEl.addContent(datasetidEl);
        
        Element countEl = new Element("count");
        countEl.addContent(Integer.toString(count));
        docEl.addContent(countEl);

        for(int i=0;i<this.fileElements.size();i++) {
            LegacyFileElement fe = this.fileElements.get(i);
            Element fileEl = this.fileElements.get(i).toElement();
            if(fe.getHasHttp().equals("true")) {
                this.hasHttp = new String("1");
            } 
            if(fe.getHasOpenDap().equals("true")) {
                this.hasOpenDap = new String("1");
            } 
            if(fe.getHasGrid().equals("true")) {
                this.hasGridFTP = new String("1");
            }

            docEl.addContent(fileEl);
        }

        Element hasHttpEl = new Element("hasHttp");
        hasHttpEl.addContent(this.hasHttp);
        docEl.addContent(hasHttpEl);

        Element hasOpenDapEl = new Element("hasOpenDap");
        hasOpenDapEl.addContent(this.hasOpenDap);
        docEl.addContent(hasOpenDapEl);

        Element hasGridFTPEl = new Element("hasGridFTP");
        hasGridFTPEl.addContent(this.hasGridFTP);
        docEl.addContent(hasGridFTPEl);
        
        Element technotesEl = new Element("technotes");
        technotesEl.addContent(this.technotesElement.toElement());
        docEl.addContent(technotesEl);
        
        return docEl;
    }
    
   
    
    
    public String toXML() {
        String xml = "";

        Element docElement = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(docElement);
        
        return xml;
    }
    
    public static void main(String [] args) {
        
       
        
        
    }


    public void setCount(int count) {
        this.count = count;
    }


    public int getCount() {
        return count;
    }
    

}
