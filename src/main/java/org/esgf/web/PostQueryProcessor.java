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

public class PostQueryProcessor {

	private SearchOutput newOutput;
	
	private SearchService searchService;
	private FacetProfile facetProfile;
	private HttpServletRequest request;
	private SearchInputImpl input;
	private SearchOutput output;
	

    private final static Logger LOG = Logger.getLogger(PostQueryProcessor.class);
	
	public PostQueryProcessor(SearchService searchService,
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
	
	public void processCentroidFilter() throws Exception
	{
		
		//first we must check if the whichGeoFlag is true
		String [] parValues = request.getParameterValues("whichGeo");
		for (final String parValue : parValues) 
		{
			//if radius is selected then further filtering is needed
			if(parValue.equals("Radius"))
			{
				SearchOutput filteredRadiusRecords = PostQueryProcessor.filterByRadius(request,output);
				newOutput = filteredRadiusRecords;
				LOG.debug("RADIUS search");
			}
			else
			{
				LOG.debug("BOUNDINGBOX search");
			}
		}
		
		/*
		 * BEGIN POSTQUERY PROCESSING 
		 */
		
		// get the counts
		int oldCounts = output.getCounts();
		int oldOffset = output.getOffset();
		
		LOG.debug("\tOLDCOUNTS: " + oldCounts);
		LOG.debug("\tOLDOFFSET: " + oldOffset);
		

		// POST QUERY PROCESSING 
		
		
		//Step 1 - get ALL records (for the counts)
		
		//set the limit to be the max
		input.setLimit(oldCounts);
		LOG.debug("\tSetting limit to " + oldCounts);
		
		
		//set the offset to 0
		input.setOffset(0);
		LOG.debug("\tSetting offset to " + 0);
		
		
		
		SearchOutput duplicateOutput = searchService.search(input, true, true);
		
		parValues = new String[parValues.length];
		parValues = request.getParameterValues("whichGeo");
		for (final String parValue : parValues) 
		{
			//if radius is selected then further filtering is needed
			if(parValue.equals("Radius"))
			{
				SearchOutput filteredRadiusRecords = PostQueryProcessor.filterByRadius(request,duplicateOutput);
				duplicateOutput = filteredRadiusRecords;
				LOG.debug("RADIUS search");
			}
			else
			{
				LOG.debug("BOUNDINGBOX search");
			}
		}
		
		
		int totalCounts = duplicateOutput.getResults().size();
		LOG.debug("\tTOTAL COUNTS AFTER FILTER: " + totalCounts);
		output.setCounts(totalCounts);
		
		
		
		//reset the limit here
		input.setLimit(10);
		LOG.debug("\tResetting limit to " + 10);
		
		//reset the offset to oldOffset
		input.setOffset(oldOffset);
		LOG.debug("\tResetting offset to " + oldOffset);
		
		
		//end step 1
		
		
		//step 2 - need to adjust for pagination
		
		/*
		//get the remaining results if there are less than 10
		int limitNum = input.getLimit();
		LOG.debug("\tSTARTING the logic for the remaining results...");
		LOG.debug("\tLimitnum: : " + limitNum);
		int numRecordsRetrieved = output.getResults().size();
		LOG.debug("\tNumber of records: " + numRecordsRetrieved);
		
		int diffNumRecordsLimit = limitNum - numRecordsRetrieved;
		
		LOG.debug("\t\tLooping until there is a valid record");
		
		LOG.debug("\t\tInitially Setting the offset to numRecordsRetrieved");
		output.setOffset(numRecordsRetrieved);
		
		
		//reset the offset to (oldOffset + numRecords in the loop) 
		input.setOffset(oldOffset);
		
		
		
		// Get the real counts here
		

		//LOG.debug("\tOUTPUT RECORDS SIZE: " + output.getResults().size());

		//output.setCounts(output.getResults().size());
		*/
		
	}
	
	public SearchOutput getOutput()
	{
		return this.output;
	}
	
	
	public static SearchOutput filterByRadius(final HttpServletRequest request,SearchOutput output)
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
	
	public static boolean isInRange(Record record,double centerLat,double centerLong,double radius)
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
