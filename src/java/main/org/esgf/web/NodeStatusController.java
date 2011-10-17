package org.esgf.web;

import java.util.List;

import org.esgf.service.NodeService;
import org.esgf.service.NodeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NodeStatusController {

    @Autowired
    @Qualifier("nodeStatus")
    private NodeService nodeService;
    
    @RequestMapping(value="peer/list", method=RequestMethod.GET,
            headers={"Accept=text/xml, application/json"})
    public @ResponseBody List<NodeStatus> getActiveNodes() {
        System.out.println("gettingActiveNodes");
        return nodeService.getLiveNodeList();
        
    }
}
