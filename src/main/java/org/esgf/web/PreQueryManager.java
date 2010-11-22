package org.esgf.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import esg.search.query.api.FacetProfile;
import esg.search.query.api.SearchInput;
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
		
	public PreQueryManager(FacetProfile facetProfile,
								  HttpServletRequest request,
								  SearchInputImpl input) throws Exception
	{
			
		this.searchService = searchService;
		this.facetProfile = facetProfile;
		this.request = request;
		this.input = input;
		this.output = output;
		this.newOutput = output;
			
	}
	
	//come back to this
	public void inputFacetProfile()
	{
		
		
	}
	
	public void inputGeospatialConstraints()
	{
		
		
		if(request.getParameterValues("searchType")!=null)
		{
			String [] parValues = request.getParameterValues("searchType");
			System.out.println("SEARCH TYPE: " + parValues[0]);
		}
		
		
		if(request.getParameterValues("searchType")!=null)
		{
			if(request.getParameterValues("searchType")[0].equals("Encloses"))
			{
				LOG.debug("Encloses");
				if(request.getParameterValues("west_degrees")!=null &&
						   request.getParameterValues("east_degrees")!=null	&&
						   request.getParameterValues("north_degrees")!=null &&
						   request.getParameterValues("south_degrees")!=null)
				{
					String geoString = "";
					
					geoString += "west_degrees:[" + request.getParameterValues("west_degrees")[0] + " TO *] AND ";
					geoString += "east_degrees:[* TO " + request.getParameterValues("east_degrees")[0] + "] AND ";
					geoString += "north_degrees:[* TO " + request.getParameterValues("north_degrees")[0] + "] AND ";
					geoString += "south_degrees:[" + request.getParameterValues("south_degrees")[0] + " TO *]";
					
					input.addGeospatialRangeConstraint(geoString);
					
					LOG.debug("GeoStringEncloses: " + geoString);

				}
				
				
			}
			else
			{
				LOG.debug("Overlaps");
				//Logic: if ANY of the extreme points (ne, nw, se, sw) are within the boundaries
				
				String boundingboxWD = "";
				String boundingboxED = "";
				String boundingboxSD = "";
				String boundingboxND = "";
				if(request.getParameterValues("west_degrees")!=null &&
				   request.getParameterValues("east_degrees")!=null	&&
				   request.getParameterValues("north_degrees")!=null &&
				   request.getParameterValues("south_degrees")!=null)
				{
					boundingboxWD = request.getParameterValues("west_degrees")[0];
					boundingboxED = request.getParameterValues("east_degrees")[0];
					boundingboxSD = request.getParameterValues("south_degrees")[0];
					boundingboxND = request.getParameterValues("north_degrees")[0];
					
					
					String geoString = "";
					
					//case 1
					//NE point in bounding box
					geoString += "(east_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
								 "north_degrees:[" + boundingboxSD + " TO " + boundingboxND + "])";
					
					geoString += " OR ";
					
					//case 2
					//SE point in bounding box
					geoString += "(east_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
					 			 "south_degrees:[" + boundingboxSD + " TO " + boundingboxND + "])";
					
					geoString += " OR ";
					
					//case 3
					//SW point in bounding box
					geoString += "(west_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
		 			 			 "south_degrees:[" + boundingboxSD + " TO " + boundingboxND + "])";
					
					
					geoString += " OR ";
					
					//case 4
					//NW point in bounding box
					geoString += "(west_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
								 "north_degrees:[" + boundingboxSD + " TO " + boundingboxND + "])";
		
					geoString += " OR ";
					
					//case 5
					//east degree in range and n & s are above and below respectively
					geoString += "(east_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
								 "south_degrees:[ * TO " + boundingboxSD + "] AND " +
								 "north_degrees:[" + boundingboxND + " TO " + "* ])";

					geoString += " OR ";
					
					//case 6
					//west degree in range and n & s are above and below respectively
					geoString += "(west_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
								 "south_degrees:[ * TO " + boundingboxSD + "] AND " +
								 "north_degrees:[" + boundingboxND + " TO " + "* ])";

					geoString += " OR ";
					
					//case 7
					//north degree in range and n & s are above and below respectively
					geoString += "(north_degrees:[" + boundingboxSD + " TO " + boundingboxND + "] AND " +
					 			 "west_degrees:[ * TO " + boundingboxWD + "] AND " +
					 			 "east_degrees:[" + boundingboxED + " TO " + "* ])";

					geoString += " OR ";
					
					//case 8
					//south degree in range and n & s are above and below respectively
					geoString += "(south_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
					 			 "west_degrees:[ * TO " + boundingboxWD + "] AND " +
					 			 "east_degrees:[" + boundingboxED + " TO " + "* ])";

					geoString += " OR ";
					
					//case 9
					//data box > user defined bounding box				
					geoString += "(east_degrees:[" + boundingboxED + " TO " + " *] AND " +
								 "west_degrees:[ * TO " + boundingboxWD + "] AND " +
								 "south_degrees:[ * TO " + boundingboxSD + "] AND " +
								 "north_degrees:[" + boundingboxND + " TO " + "* ])";

					LOG.debug("GeoStringOverlaps: " + geoString);

					input.addGeospatialRangeConstraint(geoString);
				}
				
				
				
				
			}
		}
		
	}
	
	public void inputTemporalConstraints()
	{
		
	}
	
	
	public SearchInput getInput()
	{
		return this.input;
	}
	
	
}
