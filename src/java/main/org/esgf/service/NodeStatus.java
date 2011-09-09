package org.esgf.service;

public class NodeStatus {
    private String nodeIp;
    private String nodeName;

    public NodeStatus(String name, String ip) {
        this.nodeName = name;
        this.nodeIp = ip;

    }

    public String getNodeIp() {
        return nodeIp;
    }
    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }
    public String getNodeName() {
        return nodeName;
    }
    public void setNodeNamet(String nodeName) {
        this.nodeName = nodeName;
    }

}
