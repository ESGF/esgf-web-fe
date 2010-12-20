package org.esgf.manager;

import esg.search.query.api.SearchInput;

/**
 * Manages how the query will be constructed before calling solr
 * Includes: geospatial search (centroid filter)
 * 
 * Coming soon includes: temporal search
 *                       facet (currently hardcoded in SearchController)
 * 
 * @author john.harney
 *
 */
public interface InputManager {

    
    /**
     * Method to input the facet profile
     * 
     * Note: currently under development
     */
    public void inputFacetProfile();
    
    /**
     * Method to input the geospatial constraints
     */
    public void inputGeospatialConstraints();
    
    /**
     * Method to input the temporal constraints
     */
    public void inputTemporalConstraints();
    
    
    /**
     * Method returns the input to the SearchController
     */
    public SearchInput getInput();
    
    
    
}
