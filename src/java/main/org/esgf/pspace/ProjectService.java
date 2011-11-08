package org.esgf.pspace;

import org.apache.solr.client.solrj.SolrResponse;
import org.springframework.web.client.RestTemplate;


public class ProjectService {

    private String baseSolr = "http://localhost:8983/solr/select?";

    public SolrResponse retrieveFacets(String facet) {
        SolrResponse sr = new RestTemplate().getForObject(
                baseSolr + "facet.field={facet}", SolrResponse.class, facet);
        
        assert sr != null;
        
        return null;
    }
    
    public static void main(String[] argv) {
        ProjectService ps = new ProjectService();
        ps.retrieveFacets("project");
    }
}
