package org.esgf.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.spatial.geometry.DistanceUnits;
import org.apache.lucene.spatial.geometry.FloatLatLng;
import org.apache.lucene.spatial.geometry.LatLng;
import org.apache.lucene.spatial.geometry.shape.LLRect;
import org.springframework.util.StringUtils;

import esg.search.core.Record;
import esg.search.query.api.Facet;
import esg.search.query.api.FacetProfile;
import esg.search.query.api.SearchOutput;
import esg.search.query.api.SearchService;
import esg.search.query.impl.solr.SearchInputImpl;


import org.apache.log4j.*;

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
public class PostQueryManager{

	
	private SearchService searchService;
	private FacetProfile facetProfile;
	private HttpServletRequest request;
	private SearchInputImpl input;
	private SearchOutput output;
	

    private final static Logger LOG = Logger.getLogger(PostQueryManager.class);
	
    
    /**
     * Manages how the query will be constructed AFTERcalling solr
     * Includes: geospatial search (centroid filter)
     * 
     * Coming soon includes: temporal search
     *                       facet (currently hardcoded in SearchController)
     * 
     * @author john.harney
     *
     */
	public PostQueryManager(SearchService searchService,
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
		
	}
	
	/**
     * Method to filter results based on proximity to a center point
     * 
     * throws Exception: if the operation did not complete successfully.
     */
	public void processCentroidFilter() throws Exception
	{
		
		LOG.debug("Centroid search filtering");
		
		int originalLimit = input.getLimit();
		int originalOffset = input.getOffset();
		
		
		//1 - expunge ALL old records in output (to be replaced by new records retrieved)
		List<Record> removedRecords = new ArrayList<Record>();
		for(int i=0;i<output.getResults().size();i++)
		{
			Record oldRecord = output.getResults().get(i);
			removedRecords.add(oldRecord);
		}
		
		for(int i=0;i<removedRecords.size();i++)
		{
			output.removeResult(removedRecords.get(i));
		}
		
		
		//2 - get ALL possible results
		input.setLimit(output.getCounts());
		input.setOffset(0);
		
		SearchOutput totalOutput = searchService.search(input, true, true);
		
		
		List<Record> returnedRecords = new ArrayList<Record>();
		
		int addedRecords = 0;
		
		LatLng center = getCenter();
		double radius = getRadius();
	
		//3 - find all records that match the centroid criteria
		for(int i=0;i<totalOutput.getResults().size();i++)
		{
			//extract next record
			Record record = totalOutput.getResults().get(i);
			
			//do the range check here
			if(isInRange(record,center.getLat(),center.getLng(),radius))
			{
				if(addedRecords >= originalOffset && addedRecords < (originalOffset+originalLimit))
				{
					returnedRecords.add(record);
				}
				addedRecords++;
			}
			
		}
		
		output.setCounts(addedRecords);
		
		
		//4 - Add only these records to the output
		for(int i=0;i<returnedRecords.size();i++)
		{
			Record record = returnedRecords.get(i);
			output.addResult(record);
		}
		
		//5 - Update the facet counts as well
		/*  This code causes the site to crash -- NEEDS FIXING IMMEDIATELY
		for (final String parName : facetProfile.getTopLevelFacets().keySet()) {
		    LOG.debug("Top LEvel: " + parName);
		    //loop over all subfacets in each facet
		    for(int i=0;i<totalOutput.getFacets().get(parName).getSubFacets().size();i++)
		    {
		        Facet subFacet = totalOutput.getFacets().get(parName).getSubFacets().get(i);
		        
		        int newCount = totalOutput.getFacets().get(parName).getSubFacets().get(i).getCounts();
		        LOG.debug("NewCount for " + parName + " " + newCount);
		        
		        //subFacet.setCounts(newCount);
		    }
        }
		*/
		
		
		//reset the limit
		input.setLimit(originalLimit);
		
		//reste the offset
		input.setOffset(originalOffset);
		
		
		
	}
	
	/**
     * Method to filter results based on slices of time over a specified rang
     * 
     * Note: Method still under development
     */
    public void processTimeSlicing() throws Exception
    {
        
    }
	
	/**
     * Method returns an output object after post querying processing is finished
     * Note: this method is somewhat redundant as the object reference is in SearchController already
     * May need to make a cloneable searchoutput object so that we are safe here
     */
	public SearchOutput getOutput()
	{
		return this.output;
	}
	
	
	
	/* 
	 * The following are helper methods for the centroid filter 
	 * 
	 * May refactor these by putting them into static methods in some common utils type class 
	 * 
	 */
	
	/**
     * Method to find a radius of an circle given only its inscribed boundingbox coordinates
     * 
     * @return double
     */
	private double getRadius()
	{
		double radius = 0;
		
		double eastDegreeValue = 0;
		double westDegreeValue = 0; 
		double southDegreeValue = 0; 
		double northDegreeValue = 0; 
			
		
		//obtain the extreme points here
		if(request.getParameterValues("east_degrees")!=null &&
		   request.getParameterValues("west_degrees")!=null &&
		   request.getParameterValues("north_degrees")!=null &&
		   request.getParameterValues("south_degrees")!=null)
		{
			String [] parValues = request.getParameterValues("east_degrees");
			
			for (final String parValue : parValues) {
				if (StringUtils.hasText(parValue)) {
					eastDegreeValue = Double.parseDouble(parValue);
				}
			}
			
			parValues = request.getParameterValues("west_degrees");
			
			for (final String parValue : parValues) {
				if (StringUtils.hasText(parValue)) {
					westDegreeValue = Double.parseDouble(parValue);
				}
			}	
			
			parValues = request.getParameterValues("north_degrees");
			
			for (final String parValue : parValues) {
				if (StringUtils.hasText(parValue)) {
					northDegreeValue = Double.parseDouble(parValue);
				}
			}
			
			parValues = request.getParameterValues("south_degrees");
			
			for (final String parValue : parValues) {
				if (StringUtils.hasText(parValue)) {
					southDegreeValue = Double.parseDouble(parValue);
				}
			}
			
			
		}
		
		//use lucene to find the radius in km
		LatLng nePoint = new FloatLatLng(northDegreeValue,eastDegreeValue);
		LatLng swPoint = new FloatLatLng(southDegreeValue,westDegreeValue);
		
		LLRect boundedRect = new LLRect(swPoint,nePoint);
		
		LatLng center = boundedRect.getMidpoint();
		
		LatLng sePoint = new FloatLatLng(southDegreeValue,eastDegreeValue);
		LatLng midSPoint = sePoint.calculateMidpoint(swPoint);
		
		radius = midSPoint.arcDistance(center, DistanceUnits.KILOMETERS);
		
		
		return radius;
	}
	
	/** 
	 * Method to retrieve the center of the centroid based search
	 * 
	 * @return LatLng
	 */
	private LatLng getCenter()
	{
		LatLng center = null;
		
		//find the center of the search using the lucene
		double eastDegreeValue = 0;
		double westDegreeValue = 0; 
		double southDegreeValue = 0; 
		double northDegreeValue = 0; 
			
		if(request.getParameterValues("east_degrees")!=null &&
		   request.getParameterValues("west_degrees")!=null &&
		   request.getParameterValues("north_degrees")!=null &&
		   request.getParameterValues("south_degrees")!=null)
		{
			String [] parValues = request.getParameterValues("east_degrees");
			
			for (final String parValue : parValues) {
				if (StringUtils.hasText(parValue)) {
					eastDegreeValue = Double.parseDouble(parValue);
				}
			}
			
			parValues = request.getParameterValues("west_degrees");
			
			for (final String parValue : parValues) {
				if (StringUtils.hasText(parValue)) {
					westDegreeValue = Double.parseDouble(parValue);
				}
			}	
			
			parValues = request.getParameterValues("north_degrees");
			
			for (final String parValue : parValues) {
				if (StringUtils.hasText(parValue)) {
					northDegreeValue = Double.parseDouble(parValue);
				}
			}
			
			parValues = request.getParameterValues("south_degrees");
			
			for (final String parValue : parValues) {
				if (StringUtils.hasText(parValue)) {
					southDegreeValue = Double.parseDouble(parValue);
				}
			}
		}
		
		
		LatLng nePoint = new FloatLatLng(northDegreeValue,eastDegreeValue);
		LatLng swPoint = new FloatLatLng(southDegreeValue,westDegreeValue);
		
		LLRect boundedRect = new LLRect(swPoint,nePoint);
		
		center = boundedRect.getMidpoint();
		
		return center;
	}
	
	/** Method to check if the lat and long point are within the specified radius
	 * Note: this method may be moved to a Utils class
	 * 
	 * @param record Record to be checked
	 * @param double Latitude of center
	 * @param double Longitute of center
	 * @param double Radius specified
	 * 
     * @return LatLng
     */
	private static boolean isInRange(Record record,double centerLat,double centerLong,double radius)
	{
		boolean isInRange = true;
		
		Map<String, List<String>> fields = record.getFields();
		double record_wd = Double.parseDouble(fields.get("west_degrees").get(0));
		double record_ed = Double.parseDouble(fields.get("east_degrees").get(0));
		double record_nd = Double.parseDouble(fields.get("north_degrees").get(0));
		double record_sd = Double.parseDouble(fields.get("south_degrees").get(0));
		
		//use lucene methods here to determine if it is within range
		//note: this is an ALL INCLUSIVE search so all degrees must be within the range
		//find the distances between center and the record's extreme geospatial info
		LatLng center = new FloatLatLng(centerLat,centerLong);
		
		LatLng pointSW = new FloatLatLng(record_sd,record_wd);
		double pointSWDist = center.arcDistance(pointSW, DistanceUnits.KILOMETERS);
		LatLng pointNE = new FloatLatLng(record_nd,record_ed);
		double pointNEDist = center.arcDistance(pointNE, DistanceUnits.KILOMETERS);
		LatLng pointNW = new FloatLatLng(record_nd,record_wd);
		double pointNWDist = center.arcDistance(pointNW, DistanceUnits.KILOMETERS);
		LatLng pointSE = new FloatLatLng(record_sd,record_ed);
		double pointSEDist = center.arcDistance(pointSE, DistanceUnits.KILOMETERS);
		
		
		//Note: Test for all extreme points (may be superfluous...revisit)
		if(pointSWDist > radius || 
		   pointSEDist > radius ||
		   pointNWDist > radius ||
		   pointNEDist > radius)
		{
			isInRange = false;
		}
		
		
		return isInRange; 
	}
	
}
