package org.esgf.solr.model;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

public class SolrRecord {

    private List<Node> arrNodes;
    private List<Node> strNodes;
    private List<Node> miscNodes;
    
    public SolrRecord() {
        this.arrNodes = new ArrayList<Node>();
        this.strNodes = new ArrayList<Node>();
        this.setMiscNodes(new ArrayList<Node>());
    }


    /*
    public String getId() {
        String id = "";
        
        for(int i=0;i<this.strNodes.size();i++) {
            Node node = this.strNodes.get(i);
            System.out.println("node name: " + this.strNodes.get(i).getNodeName());
        }
        
        return id;
    }
    */
    
    /**
     * @return the arrNodes
     */
    public List<Node> getArrNodes() {
        return arrNodes;
    }

    /**
     * @param arrNodes the arrNodes to set
     */
    public void setArrNodes(List<Node> arrNodes) {
        this.arrNodes = arrNodes;
    }

    /**
     * @return the strNodes
     */
    public List<Node> getStrNodes() {
        return strNodes;
    }

    /**
     * @param strNodes the strNodes to set
     */
    public void setStrNodes(List<Node> strNodes) {
        this.strNodes = strNodes;
    }

    /**
     * @return the miscNodes
     */
    public List<Node> getMiscNodes() {
        return miscNodes;
    }

    /**
     * @param miscNodes the miscNodes to set
     */
    public void setMiscNodes(List<Node> miscNodes) {
        this.miscNodes = miscNodes;
    }
    
    
}
