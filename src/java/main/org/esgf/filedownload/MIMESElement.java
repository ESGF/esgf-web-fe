package org.esgf.filedownload;

import java.util.ArrayList;
import java.util.List;

import org.esgf.metadata.JSONArray;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/** Description of MyClass 
*
* @author John Harney
* @version Bedford-Stuyvesant
* 
*/
public class MIMESElement implements DataCartElement {
    
    /** listing of mime types associated with the file */
    private List<String> mimes;
    
    /**
     * DOCUMENT ME!
     */
    public MIMESElement() {
        mimes = new ArrayList<String>();
        mimes.add("mime");
    }

    /**
     * DOCUMENT ME!
     * @param mimes
     */
    public void setMIME(List<String> mimes) {
        if(mimes != null) {
            this.mimes = mimes;
        }
    }

    /**
     * DOCUMENT ME!
     * @return Returns the listing of mime types for this file
     */
    public List<String> getMIMES() {
        return mimes;
    }
    
    /**
     * DOCUMENT ME!
     * @param mime
     * @return Returns true if there was a mime type removed, false otherwise
     */
    public boolean removeMIME(String mime) {
        boolean removed = false;
        if(mime != null) {
            for(int i=0;i<mimes.size();i++) {
                if(mimes.get(i).equals(mime)) {
                    mimes.remove(i);
                    removed = true;
                }
            }
        }
        
        return removed;
    }
    
    /**
     * DOCUMENT ME!
     * @param mime
     */
    public void addMIME(String mime) {
        if(mime != null)
            mimes.add(mime);
    }
    
    /**
     * DOCUMENT ME!
     */
    public String toXML() {
        String xml = "";
        
        Element mimesElement = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(mimesElement);
        
        return xml;
    }
    
    /**
     * DOCUMENT ME!
     */
    public Element toElement() {
        Element mimesEl = new Element("mimes");
        
        for(int i=0;i<mimes.size();i++) {
            Element mimeEl = new Element("mime");
            mimeEl.addContent(mimes.get(i));
            mimesEl.addContent(mimeEl);
        }
        
        return mimesEl;
    }
    
    /**
     * DOCUMENT ME!
     */
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
