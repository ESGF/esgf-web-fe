package org.esgf.filedownload;

import java.util.ArrayList;
import java.util.List;

import org.esgf.metadata.JSONArray;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class MIMESElement {
    
    private List<String> mimes;
    
    public MIMESElement() {
        mimes = new ArrayList<String>();
        mimes.add("mime");
    }

    public void setMIME(List<String> services) {
        this.mimes = mimes;
    }

    public List<String> getMIMES() {
        return mimes;
    }
    

    public boolean removeMIME(String mime) {
        boolean removed = false;
        for(int i=0;i<mimes.size();i++) {
            if(mimes.get(i).equals(mime)) {
                mimes.remove(i);
                removed = true;
            }
        }
        return removed;
    }
    
    public void addMIME(String mime) {
        mimes.add(mime);
    }
    
    
    public String toXML() {
        String xml = "";
        
        Element mimesElement = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(mimesElement);
        
        return xml;
    }
    
    public Element toElement() {
        Element mimesEl = new Element("mimes");
        
        for(int i=0;i<mimes.size();i++) {
            Element mimeEl = new Element("mime");
            mimeEl.addContent(mimes.get(i));
            mimesEl.addContent(mimeEl);
        }
        
        return mimesEl;
    }
    
    public String toString() {
        String str = "mimes\n";
        
        for(int i=0;i<mimes.size();i++) {
            str += "\tmime: " + i + " " + mimes.get(i) + "\n";
        }
        
        return str;
    }
    
    
    public static void main(String [] args) {
        MIMESElement me = new MIMESElement();
        String mime = "mime1";
        me.addMIME(mime);
        mime = "mime2";
        me.addMIME(mime);
        System.out.println(me.getMIMES().size());
        System.out.println(me);
        System.out.println(me.toXML());
        me.removeMIME("mime2");
        System.out.println(me.toXML());
        
    }
    
}
