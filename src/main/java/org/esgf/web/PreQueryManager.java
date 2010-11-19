package org.esgf.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import esg.search.query.api.FacetProfile;
import esg.search.query.api.SearchOutput;
import esg.search.query.api.SearchService;
import esg.search.query.impl.solr.SearchInputImpl;

public class PreQueryManager {

	private SearchOutput newOutput;
	
	//old parameters
	private SearchService searchService;
	private FacetProfile facetProfile;
	private HttpServletRequest request;
	private SearchInputImpl input;
	private SearchOutput output;
	
	
	 private final static Logger LOG = Logger.getLogger(PreQueryManager.class);
		
	public PreQueryManager(SearchService searchService,
								  FacetProfile facetProfile,
								  HttpServletRequest request,
								  SearchInputImpl input,
								  SearchOutput output) throws Exception
	{
			
		this.searchService = searchService;
		this.facetProfile = facetProfile;
		this.request = request;
		this.input = input;
		this.output = output;
		this.newOutput = output;
			
	}
	
	
	
	
	
	
}
