package org.esgf.srm.go;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.esgf.datacart.XmlFormatter;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GOSRMObjList {

    private List<GOSRMObj> go_srm_objs;
    
    public GOSRMObjList() {
        this.setGo_srm_objs(new ArrayList<GOSRMObj>());
    }
    
    public GOSRMObjList(String file) {
        this.fromFile(file);
    }
    
    public void addGOSRMObj(GOSRMObj go_srm_obj) {
        
        if(go_srm_obj != null) {
            boolean isDuplicate = false;
            for(int i=0;i<this.go_srm_objs.size();i++) {
                if(this.go_srm_objs.get(i).getGo_hash_id().equals(go_srm_obj.getGo_hash_id())) {
                    isDuplicate = true;
                }
            }
            if(!isDuplicate)
                this.go_srm_objs.add(go_srm_obj);
        }
        
    }
    
    public void deleteGOSRMObj(String hashCode) {
        for(int i=0;i<this.go_srm_objs.size();i++) {
            if(this.go_srm_objs.get(i).getGo_hash_id().equals(hashCode)) {
                this.go_srm_objs.remove(i);
            }
        }
    }
    
    public void deleteGOSRMObj(GOSRMObj go_srm_obj) {
        this.go_srm_objs.remove(go_srm_obj);
    }
    
    public GOSRMObj getGOSRMObj(String hashcode) {
        GOSRMObj go_srm_obj = null;
        for(int i=0;i<this.go_srm_objs.size();i++) {
            if(this.go_srm_objs.get(i).getGo_hash_id().equals(hashcode)) {
                go_srm_obj = this.go_srm_objs.get(i);
            }
        }
        return go_srm_obj;
    }

    /**
     * @return the go_srm_objs
     */
    public List<GOSRMObj> getGo_srm_objs() {
        return go_srm_objs;
    }

    /**
     * @param go_srm_objs the go_srm_objs to set
     */
    public void setGo_srm_objs(List<GOSRMObj> go_srm_objs) {
        this.go_srm_objs = go_srm_objs;
    }
    
    
    
    /** Description of toElement()
     * 
     * @return serialized XML element equivalent of the class
     */
    public Element toElement() {
    
        Element goSRMObjListEl = new Element("go_srm_obj_list");
        
        for(int i=0;i<this.go_srm_objs.size();i++) {
            goSRMObjListEl.addContent(this.go_srm_objs.get(i).toElement());
        }
        
        return goSRMObjListEl;
    
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

    
    /** Description of toFile()
     * 
     * @param file Filename of the output
     */
    public void toFile(String file) {
        
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(new XmlFormatter().format(this.toXML()));
            out.close();
        } 
        catch (IOException e) { 
            e.printStackTrace();
            System.out.println("Exception ");

        }
        
    }
    
    
    /**Description of fromFile()
     * 
     * @param file
     */
    public void fromFile(String file) {
        
        System.out.println("File: " + file);
        
        
        
        File fXmlFile = new File(file);
        System.out.println(fXmlFile.exists());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            
            org.w3c.dom.Element docElement = doc.getDocumentElement();
            
            this.readHelper(docElement);
            
        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    /** Description of readHelper
     * 
     * @param docElement
     */
    public void readHelper(org.w3c.dom.Element docElement) {
        
        
        if(docElement.getNodeName().equals("go_srm_obj_list")) {
            System.out.println("Did I make it here?");
            this.go_srm_objs = new ArrayList<GOSRMObj>();
            NodeList fileNodeList = docElement.getChildNodes();//doc.getDocumentElement().getChildNodes();
            
            for(int i=0;i<fileNodeList.getLength();i++) {
                Node topLevelFileNode = fileNodeList.item(i);
                if (topLevelFileNode.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element topLevelElement = (org.w3c.dom.Element) topLevelFileNode;
                    if(topLevelElement.getTagName().equals("go_srm_obj")) {
                        
                        GOSRMObj go_srm_obj = new GOSRMObj();
                        NodeList go_srm_obj_list = topLevelElement.getChildNodes();
                        for(int j=0;j<go_srm_obj_list.getLength();j++) {
                            Node go_srm_obj_node = go_srm_obj_list.item(j);
                            if (go_srm_obj_node.getNodeType() == Node.ELEMENT_NODE) {
                                
                                if(go_srm_obj_node.getNodeName().equals("go_hash_id")) {
                                    String go_hash_id = go_srm_obj_node.getTextContent();
                                    go_srm_obj.setGo_hash_id(go_hash_id);
                                }
                                if(go_srm_obj_node.getNodeName().equals("go_type")) {
                                    String go_type = go_srm_obj_node.getTextContent();
                                    go_srm_obj.setType(go_type);
                                }
                                if(go_srm_obj_node.getNodeName().equals("go_id")) {
                                    String go_id = go_srm_obj_node.getTextContent();
                                    go_srm_obj.setType(go_id);
                                }
                                if(go_srm_obj_node.getNodeName().equals("go_credential")) {
                                    String go_credential = go_srm_obj_node.getTextContent();
                                    go_srm_obj.setType(go_credential);
                                }
                                if(go_srm_obj_node.getNodeName().equals("go_child_urls")) {
                                    NodeList go_child_url_list = go_srm_obj_node.getChildNodes();
                                    List<String> go_child_urls = new ArrayList<String>();
                                    for(int k=0;k<go_child_url_list.getLength();k++) {
                                        Node go_child_url_node = go_child_url_list.item(k);
                                        if (go_child_url_node.getNodeType() == Node.ELEMENT_NODE) {
                                            go_child_urls.add(go_child_url_node.getTextContent());
                                        }
                                    }
                                    String [] go_child_urls_arr = new String[go_child_urls.size()];
                                    for(int k=0;k<go_child_urls.size();k++) {
                                        go_child_urls_arr[k] = go_child_urls.get(k);
                                    }
                                    go_srm_obj.setChild_url(go_child_urls_arr);
                                }
                                if(go_srm_obj_node.getNodeName().equals("go_child_ids")) {
                                    NodeList go_child_id_list = go_srm_obj_node.getChildNodes();
                                    List<String> go_child_ids = new ArrayList<String>();
                                    for(int k=0;k<go_child_id_list.getLength();k++) {
                                        Node go_child_id_node = go_child_id_list.item(k);
                                        if (go_child_id_node.getNodeType() == Node.ELEMENT_NODE) {
                                            go_child_ids.add(go_child_id_node.getTextContent());
                                        }
                                    }
                                    String [] go_child_ids_arr = new String[go_child_ids.size()];
                                    for(int k=0;k<go_child_ids.size();k++) {
                                        go_child_ids_arr[k] = go_child_ids.get(k);
                                    }
                                    go_srm_obj.setChild_id(go_child_ids_arr);
                                }
                            }
                        }
                        //add 
                        this.go_srm_objs.add(go_srm_obj);
                    }
                }
            }
            
            
            
        }
    }
    
}
