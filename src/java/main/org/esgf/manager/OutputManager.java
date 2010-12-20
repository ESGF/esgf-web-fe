package org.esgf.manager;

import esg.search.query.api.SearchOutput;


/**
 * Manages post query processing procedures.
 * Includes: geospatial search (centroid filter)
 * 
 * Coming soon includes: temporal search (time slicing) 
 *                       facet (currently hardcoded in SearchController)
 * 
 * @author john.harney
 *
 */
public interface OutputManager{

    /**
     * Method to filter results based on proximity to a center point
     */
    public void processCentroidFilter() throws Exception; 
    
    /**
     * Method to revise the facet counts based on post query filters
     */
    public void facetRecount() throws Exception;
    
    /**
     * Method to filter results based on slices of time over a specified rang
     */
    public void processTimeSlicing() throws Exception;
    
    /**
     * Method returns an output object after post querying processing is finished
     */
    public SearchOutput getOutput();
    
}
