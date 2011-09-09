package org.esgf.service;

import java.util.List;

import org.xml.sax.InputSource;


public interface NodeService {
    public List<NodeStatus> getLiveNodeList(InputSource isource);

}
