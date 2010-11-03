package org.esgf.web;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import esg.search.query.api.FacetProfile;
import esg.search.query.api.SearchOutput;
import esg.search.query.api.SearchService;
import esg.search.query.impl.solr.SearchInputImpl;


import esg.search.core.Record;
import java.util.List;

import org.apache.lucene.spatial.geometry.DistanceUnits;
import org.apache.lucene.spatial.geometry.LatLng;
import org.apache.lucene.spatial.geometry.FloatLatLng;
import org.apache.lucene.spatial.geometry.shape.LLRect;
import org.apache.lucene.spatial.tier.DistanceUtils;

@Controller
@RequestMapping(value="/search")

public class SearchController {
	
	private final SearchService searchService;
	private final FacetProfile facetProfile;
	
	private final static String SEARCH_INPUT = "search_input";
	private final static String SEARCH_OUTPUT = "search_output";
	private final static String FACET_PROFILE = "facet_profile";
	private final static String ERROR_MESSAGE = "error_message";
	private final static String SEARCH_MODEL = "search_model";
	
	
	private final static Log LOG = LogFactory.getLog(SearchController.class);
	
	/**
	 * List of invalid text characters - 
	 * anything that is not within square brackets.
	 */
	private static Pattern pattern = 
		Pattern.compile(".*[^a-zA-Z0-9_\\-\\.\\@\\'\\:\\;\\,\\s/()].*");
	
	public SearchController(final SearchService searchService, 
			final FacetProfile facetProfile) {
		this.searchService = searchService;
		this.facetProfile = facetProfile;
	}
	
	/**
	 * Method used to execute non-standard biding of search constraints.
	 * This method is invoked before the GET/POST request handler and 
	 * before HTTP parameters binding.
	 * 
	 * @param request
	 * @return
	 */
	@ModelAttribute(SEARCH_INPUT)
	public SearchInputImpl formBackingObject(final HttpServletRequest request) {
		
		// instantiate command object
		final SearchInputImpl input = new SearchInputImpl();
		
		//process geospatial search 
		processBoundingBoxSearch(request,input);
		
		// security note: loop ONLY over parameters in facet profile
		for (final String parName : facetProfile.getTopLevelFacets().keySet()) {
			final String[] parValues = request.getParameterValues(parName);
			if (parValues!=null) {
				for (final String parValue : parValues) {
					if (StringUtils.hasText(parValue)) {
						input.addConstraint(parName, parValue);
						if (LOG.isTraceEnabled()) 
							LOG.trace("formBackingObject: set constraint name=" +
									parName+" value="+parValue);

					}
				}
			}
			
		}
		
		
		
		return input;
		
	}
	
	public void processBoundingBoxSearch(HttpServletRequest request,SearchInputImpl input)
	{
		
		
		//refactor this
		if(request.getParameterValues("west_degrees")!=null)
		{
			String [] parValues = request.getParameterValues("west_degrees");
			for (final String parValue : parValues) {
				if (StringUtils.hasText(parValue)) {
					String geoConstraint = "[ " + parValue + " TO * ]";
					input.addGeospatialRangeConstraint("west_degrees",geoConstraint);
				}
			}
		}
		
		
		
		if(request.getParameterValues("east_degrees")!=null)
		{
			//System.out.println(request.getParameterValues("west_degrees").toString() + "\n");
			String [] parValues = request.getParameterValues("east_degrees");
			for (final String parValue : parValues) {
				if (StringUtils.hasText(parValue)) {
					String geoConstraint = "[ * TO " + parValue + "]";
					input.addGeospatialRangeConstraint("east_degrees",geoConstraint);
					
				}
			}
		}
		
		
		
		if(request.getParameterValues("south_degrees")!=null)
		{
			//System.out.println(request.getParameterValues("west_degrees").toString() + "\n");
			String [] parValues = request.getParameterValues("south_degrees");
			for (final String parValue : parValues) {
				if (StringUtils.hasText(parValue)) {
					String geoConstraint = "[ " + parValue + " TO * ]";
					input.addGeospatialRangeConstraint("south_degrees",geoConstraint);
					
				}
			}
		}
		
		
		if(request.getParameterValues("north_degrees")!=null)
		{
			//System.out.println(request.getParameterValues("west_degrees").toString() + "\n");
			String [] parValues = request.getParameterValues("north_degrees");
			for (final String parValue : parValues) {
				if (StringUtils.hasText(parValue)) {
					String geoConstraint = "[ * TO " + parValue + "]";
					input.addGeospatialRangeConstraint("north_degrees",geoConstraint);
				}
			}
		}
	}
	
	
	
	/**
	 * Method invoked in response to a GET request:
	 * -) if invoked directly, a new set of facets is retrieved (but no results)
	 * -) if invoked in response to a POST-REDIRECT,
	 * @param request
	 * @param input
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView doGet(
			final HttpServletRequest request,
			final @ModelAttribute(SEARCH_INPUT) SearchInputImpl input, 
			final BindingResult result) throws Exception {
		
		Map<String,Object> model = new HashMap<String,Object>();
		
		if (request.getParameter(SEARCH_MODEL)!=null) {
			
			// retrieve model from session
			model = (Map<String,Object>)request.getSession().getAttribute(SEARCH_MODEL);
			
		} else {
			
			// set retrieval of all facets in profile
			input.setFacets(new ArrayList<String>(facetProfile.getTopLevelFacets().keySet()));
	
			// execute query for facets
			final SearchOutput output = searchService.search(input, false, true);
			if (LOG.isTraceEnabled())  
				LOG.trace("doGetPost: facets="+output.getFacets());
			
			// populate model
			model.put(SEARCH_INPUT, input);
			model.put(SEARCH_OUTPUT, output);
			model.put(FACET_PROFILE, facetProfile);
			
			// save model in session
			request.getSession().setAttribute(SEARCH_MODEL, model);
			
		}
		
		return new ModelAndView("search", model);
	}
	
	/**
	 * Method invoked in response to a POST request: both results and facets are retrieved.
	 * @param request
	 * @param input
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method=RequestMethod.POST)
	@SuppressWarnings("unchecked")
	protected ModelAndView doPost(final HttpServletRequest request, 
			final @ModelAttribute(SEARCH_INPUT) SearchInputImpl input, 
			final BindingResult result) throws Exception {

		
		
		// invalid user input
		if (isNotValid(input.getText())) {					
			
			// re-use previous model (output and profile)
			final Map<String,Object> model = (Map<String,Object>)request.getSession().getAttribute(SEARCH_MODEL);
			// override search input
			model.put(SEARCH_INPUT, input);
			// add error
			model.put(ERROR_MESSAGE, "Error: invalid characters found in search text");
						
		// valid user input
		} else {
			
			
			
			// set retrieval of all facets in profile
			input.setFacets(new ArrayList<String>(facetProfile.getTopLevelFacets().keySet()));
	
			// execute query for results, facets
			final SearchOutput output = searchService.search(input, true, true);
			if (LOG.isTraceEnabled()) 
			{
				LOG.trace("doPost: results="+output);
			}
			
			String [] parValues = request.getParameterValues("whichGeo");
			for (final String parValue : parValues) 
			{
				//if radius is selected then further filtering is needed
				if(parValue.equals("Radius"))
				{
					SearchOutput filteredRadiusRecords = filterByRadius(request,output);
					output = filteredRadiusRecords;
					System.out.println("\n\n\nRUNNING RADIUS\n\n\n");
				}
				else
				{
					System.out.println("\n\n\nRUNNING BOUNDING BOX\n\n\n");
				}
			}
			
			
			
			
			// store new model in session
			final Map<String,Object> model = new HashMap<String,Object>();
			model.put(SEARCH_INPUT, input);
			model.put(SEARCH_OUTPUT, output);
			model.put(FACET_PROFILE, facetProfile);
			request.getSession().setAttribute(SEARCH_MODEL, model);
		
		}
		
		
		
		// use POST-REDIRECT-GET pattern with additional parameter "?search_model"
		final String url = request.getRequestURL().toString();
		return new ModelAndView(new RedirectView(url)).addObject(SEARCH_MODEL,"true");
	}

	
	public SearchOutput filterByRadius(final HttpServletRequest request,SearchOutput output)
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
			
			System.out.println("SD: " + southDegreeValue);
			System.out.println("ND: " + northDegreeValue);
			System.out.println("ED: " + eastDegreeValue);
			System.out.println("WD: " + westDegreeValue);
			
		}
		
		
		LatLng nePoint = new FloatLatLng(northDegreeValue,eastDegreeValue);
		LatLng swPoint = new FloatLatLng(southDegreeValue,westDegreeValue);
		
		//LatLng center = nePoint.calculateMidpoint(swPoint);
		
		LLRect boundedRect = new LLRect(swPoint,nePoint);
		
		LatLng center = boundedRect.getMidpoint();
		
		centerLat = center.getLat();
		centerLong = center.getLng();
		
		
		//find the radius
		//double radius =Math.abs(centerLat - eastDegreeValue);
		
		LatLng sePoint = new FloatLatLng(southDegreeValue,eastDegreeValue);
		LatLng midSPoint = sePoint.calculateMidpoint(swPoint);
		
		double radius = midSPoint.arcDistance(center, DistanceUnits.KILOMETERS);
		
		
		
		
		//SearchOutput filteredOutput = output;
		
		List<Record> deletedRecords = new ArrayList<Record>();
		
		
		for(int i=0;i<output.getResults().size();i++)
		{
			Record record = output.getResults().get(i);
			
			System.out.println("\nRecord: " + i);
			
			//check if it is within radius
			if(!isInRange(record,centerLat,centerLong,radius))
			//if(i % 2 == 1)
			{
				System.out.println("REMOVING RECORD: " + record.getId());
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
	
	
	
	
	public static boolean isNotValid(final String text) {
		final Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}

}