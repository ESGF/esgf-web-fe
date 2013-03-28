package org.esgf.srm;

import java.util.Vector;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GOSRMObj {

    private String go_hash_id;
    

    private String [] child_id;
    private String [] child_url;
    private String type;
    private String id;
    private String credential;
    
    public GOSRMObj() {
        
        this.go_hash_id = "aaa";
        
        Vector<String> child_url_vec = new Vector<String>();
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0005-09-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0005-10-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0005-11-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0005-12-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-01-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-02-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-03-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-04-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-05-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-06-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-07-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-08-latlon.nc");
        this.child_url = new String [child_url_vec.size()];
        for(int i=0;i<child_url_vec.size();i++) {
            this.child_url[i] = child_url_vec.get(i);
        }
       
        Vector<String> child_id_vec = new Vector<String>();
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0005-09-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0005-10-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0005-11-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0005-12-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-01-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-02-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-03-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-04-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-05-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-06-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-07-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-08-latlon.nc|esg.anl.gov");
        this.child_id = new String [child_id_vec.size()];
        for(int i=0;i<child_id_vec.size();i++) {
            this.child_id[i] = child_id_vec.get(i);
        }
        
        
        this.type = "create";
        this.id = "anl.cssef.homme.v1|esg.anl.gov";
        this.credential = "undefined";
        
        
    }
    
    public GOSRMObj(String go_hash_id) {
        
        this.go_hash_id = go_hash_id;
        
        Vector<String> child_url_vec = new Vector<String>();
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0005-09-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0005-10-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0005-11-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0005-12-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-01-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-02-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-03-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-04-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-05-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-06-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-07-latlon.nc");
        child_url_vec.add("gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-08-latlon.nc");
        this.child_url = new String [child_url_vec.size()];
        for(int i=0;i<child_url_vec.size();i++) {
            this.child_url[i] = child_url_vec.get(i);
        }
       
        Vector<String> child_id_vec = new Vector<String>();
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0005-09-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0005-10-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0005-11-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0005-12-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-01-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-02-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-03-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-04-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-05-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-06-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-07-latlon.nc|esg.anl.gov");
        child_id_vec.add("anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-08-latlon.nc|esg.anl.gov");
        this.child_id = new String [child_id_vec.size()];
        for(int i=0;i<child_id_vec.size();i++) {
            this.child_id[i] = child_id_vec.get(i);
        }
        
        
        this.type = "create";
        this.id = "anl.cssef.homme.v1|esg.anl.gov";
        this.credential = "undefined";
        
    }
    
    
    /** Description of toElement()
     * 
     * @return serialized XML element equivalent of the class
     */
    public Element toElement() {

        Element goSRMObjEl = new Element("go_srm_obj");
        
        if(this.go_hash_id != null) {
            Element go_hash_idEl = new Element("go_hash_id");
            go_hash_idEl.addContent(this.go_hash_id);
            goSRMObjEl.addContent(go_hash_idEl);
        }
        if(this.type != null) {
            Element go_typeEl = new Element("go_type");
            go_typeEl.addContent(this.type);
            goSRMObjEl.addContent(go_typeEl);
        }
        if(this.credential != null) {
            Element go_credentialEl = new Element("go_credential");
            go_credentialEl.addContent(this.credential);
            goSRMObjEl.addContent(go_credentialEl);
        }
        if(this.id != null) {
            Element go_idEl = new Element("go_id");
            go_idEl.addContent(this.id);
            goSRMObjEl.addContent(go_idEl);
        }
        if(this.child_id != null) {
            Element go_child_idsEl = new Element("go_child_ids");
            for(int i=0;i<this.child_id.length;i++) {
                Element go_child_idEl = new Element("go_child_id");
                go_child_idEl.addContent(this.child_id[i]);
                go_child_idsEl.addContent(go_child_idEl);
            }
            goSRMObjEl.addContent(go_child_idsEl);
        }
        if(this.child_url != null) {
            Element go_child_urlsEl = new Element("go_child_urls");
            for(int i=0;i<this.child_url.length;i++) {
                Element go_child_urlEl = new Element("go_child_url");
                go_child_urlEl.addContent(this.child_url[i]);
                go_child_urlsEl.addContent(go_child_urlEl);
            }
            goSRMObjEl.addContent(go_child_urlsEl);
        }
        return goSRMObjEl;
    }
    
    /** Description of toXML()
     * 
     * @return
     */
    public String toXML() {
        String xml = "";
        
        Element fileEl = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(fileEl);
        
        return xml;
    }

    /**
     * @return the child_id
     */
    public String [] getChild_id() {
        return child_id;
    }

    /**
     * @param child_id the child_id to set
     */
    public void setChild_id(String [] child_id) {
        this.child_id = child_id;
    }

    /**
     * @return the child_url
     */
    public String [] getChild_url() {
        return child_url;
    }

    /**
     * @param child_url the child_url to set
     */
    public void setChild_url(String [] child_url) {
        this.child_url = child_url;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the credential
     */
    public String getCredential() {
        return credential;
    }

    /**
     * @param credential the credential to set
     */
    public void setCredential(String credential) {
        this.credential = credential;
    }
    
    public String getGo_hash_id() {
        return go_hash_id;
    }

    public void setGo_hash_id(String go_hash_id) {
        this.go_hash_id = go_hash_id;
    }
    
    public String toString() {
        String str = "";
        str += "GO Object: " + this.go_hash_id + "\n";
        str += "Id: " + this.id + " Type: " + this.type + "\n";
        str += "Credential: " + this.credential + "\n";
        str += "Child ID: \n";
        for(int i=0;i<this.child_id.length;i++) {
            str += "\t" + this.child_id[i] + "\n";
        }
        for(int i=0;i<this.child_url.length;i++) {
            str += "\t" + this.child_url[i] + "\n";
        }
        /*
        this.go_hash_id
        this.id
        this.credential
        this.type
        
        this.child_id
        this.child_url
        */
        return str;
    }
    
}


/*
child_url=gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0005-09-latlon.nc
child_url=gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0005-10-latlon.nc
child_url=gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0005-11-latlon.nc
child_url=gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0005-12-latlon.nc
child_url=gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-01-latlon.nc
child_url=gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-02-latlon.nc
child_url=gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-03-latlon.nc
child_url=gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-04-latlon.nc
child_url=gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-05-latlon.nc
child_url=gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-06-latlon.nc
child_url=gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-07-latlon.nc
child_url=gsiftp://esg.anl.gov:2811//CSSEF/fcn-ne120b.cam2.h0.0006-08-latlon.nc

child_id=anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0005-09-latlon.nc|esg.anl.gov
child_id=anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0005-10-latlon.nc|esg.anl.gov
child_id=anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0005-11-latlon.nc|esg.anl.gov
child_id=anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0005-12-latlon.nc|esg.anl.gov
child_id=anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-01-latlon.nc|esg.anl.gov
child_id=anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-02-latlon.nc|esg.anl.gov
child_id=anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-03-latlon.nc|esg.anl.gov
child_id=anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-04-latlon.nc|esg.anl.gov
child_id=anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-05-latlon.nc|esg.anl.gov
child_id=anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-06-latlon.nc|esg.anl.gov
child_id=anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-07-latlon.nc|esg.anl.gov
child_id=anl.cssef.homme.v1.fcn-ne120b.cam2.h0.0006-08-latlon.nc|esg.anl.gov
*/
