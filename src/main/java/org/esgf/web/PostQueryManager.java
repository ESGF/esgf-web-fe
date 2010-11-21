package org.esgf.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.spatial.geometry.DistanceUnits;
import org.apache.lucene.spatial.geometry.FloatLatLng;
import org.apache.lucene.spatial.geometry.LatLng;
import org.apache.lucene.spatial.geometry.shape.LLRect;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import esg.search.core.Record;
import esg.search.query.api.FacetProfile;
import esg.search.query.api.SearchOutput;
import esg.search.query.api.SearchService;
import esg.search.query.impl.solr.SearchInputImpl;


import org.apache.log4j.*;

public class PostQueryManager{

	private SearchOutput newOutput;
	
	//old parameters
	private SearchService searchService;
	private FacetProfile facetProfile;
	private HttpServletRequest request;
	private SearchInputImpl input;
	private SearchOutput output;
	

    private final static Logger LOG = Logger.getLogger(PostQueryManager.class);
	
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
		this.newOutput = output;
		
	}
	
	public void processCentroidFilter() throws Exception
	{
		
		LOG.debug("RADIUS search");
		
		int originalLimit = input.getLimit();
		int originalOffset = input.getOffset();
		
		
		//first expunge ALL old records in output
		//to be replaced by new records retrieved
		List<Record> removedRecords = new ArrayList<Record>();
		//LOG.debug("\t\tRECORDS SIZE " + output.getResults().size());
		for(int i=0;i<output.getResults().size();i++)
		{
			Record oldRecord = output.getResults().get(i);
			removedRecords.add(oldRecord);
		}
		
		for(int i=0;i<removedRecords.size();i++)
		{
			output.removeResult(removedRecords.get(i));
		}
		//end expunge
		
		
		//second get ALL possible results
		input.setLimit(output.getCounts());
		input.setOffset(0);
		
		SearchOutput totalOutput = searchService.search(input, true, true);
		
		int offset = input.getOffset();
		
		int recordIndex = 0;
		
		List<Record> returnedRecords = new ArrayList<Record>();
		
		int addedRecords = 0;
		
		
		LatLng center = getCenter();
		double radius = getRadius();
		
		//while(!limitReached)
		for(int i=0;i<totalOutput.getResults().size();i++)
		{
			//extract next record
			Record record = totalOutput.getResults().get(i);
			
			//do the check for here
			//if()
			if(isInRange(record,center.getLat(),center.getLng(),radius))
			{
				//LOG.debug("\tRecord: " + i + " In geo range");
				//LOG.debug("\tChecking if it is in range..." + offset + " TO " + (offset+originalLimit));
				//LOG.debug("\taddedRecords:  " + addedRecords);
				
				if(addedRecords >= originalOffset && addedRecords < (originalOffset+originalLimit))
				{
					//LOG.debug("\tADDING");
					returnedRecords.add(record);
				}
				addedRecords++;
			}
			
		}
		

		//LOG.debug("\tUPDATING COUNTS to " + addedRecords);
		
		output.setCounts(addedRecords);
		
		
		//add new records
		for(int i=0;i<returnedRecords.size();i++)
		{
			Record record = returnedRecords.get(i);
			output.addResult(record);
		}
		
		
		
		//reset the limit
		input.setLimit(originalLimit);
		
		//reste the offset
		input.setOffset(originalOffset);
		
		
		
	}
	
	public SearchOutput getOutput()
	{
		return this.output;
	}
	
	
	
	
	
	
	
	
	/* Helper methods for the centroid filter 
	 * 
	 * May refactor these by putting them into static methods in some common utils type class 
	 * 
	 */
	
	private double getRadius()
	{
		double radius = 0;
		
		//find the center of the search using the lucene - right now manually find
		double centerLat = 0;
		double centerLong = 0;
		
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
		
		LatLng center = boundedRect.getMidpoint();
		
		centerLat = center.getLat();
		centerLong = center.getLng();
		
		LatLng sePoint = new FloatLatLng(southDegreeValue,eastDegreeValue);
		LatLng midSPoint = sePoint.calculateMidpoint(swPoint);
		
		radius = midSPoint.arcDistance(center, DistanceUnits.KILOMETERS);
		
		
		return radius;
	}
	
	private LatLng getCenter()
	{
		LatLng center = null;
		
		//find the center of the search using the lucene - right now manually find
		double centerLat = 0;
		double centerLong = 0;
		
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
	
	
	
	
	private static SearchOutput filterByRadius(final HttpServletRequest request,SearchOutput output)
	{
		//find the center of the search using the lucene - right now manually find
		double centerLat = 0;
		double centerLong = 0;
		
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
		
		LatLng center = boundedRect.getMidpoint();
		
		centerLat = center.getLat();
		centerLong = center.getLng();
		
		LatLng sePoint = new FloatLatLng(southDegreeValue,eastDegreeValue);
		LatLng midSPoint = sePoint.calculateMidpoint(swPoint);
		
		double radius = midSPoint.arcDistance(center, DistanceUnits.KILOMETERS);
		
		List<Record> deletedRecords = new ArrayList<Record>();
		
		
		for(int i=0;i<output.getResults().size();i++)
		{
			Record record = output.getResults().get(i);
			
			
			//check if it is within radius
			if(!isInRange(record,centerLat,centerLong,radius))
			{
				deletedRecords.add(record);
			}
		}
		
		for(int i=0;i<deletedRecords.size();i++)
		{
			Record record = deletedRecords.get(i);
			output.removeResult(record);
		}
		
		
		return output;
	}
	
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
