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
		
		
		String thisLine = "";
		
		// instantiate command object
		final SearchInputImpl input = new SearchInputImpl();
		
		
		
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
	protected String doPost(final HttpServletRequest request, 
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
				LOG.trace("\nadding west_degrees Constraint\n");
			}
			
			// store new model in session
			final Map<String,Object> model = new HashMap<String,Object>();
			model.put(SEARCH_INPUT, input);
			model.put(SEARCH_OUTPUT, output);
			model.put(FACET_PROFILE, facetProfile);
			
			request.getSession().setAttribute(SEARCH_MODEL, model);
		
		}
		
		
		
		// use POST-REDIRECT-GET pattern with additional parameter "?search_model"
		//final String url = request.getRequestURL().toString();
		//return new ModelAndView(new RedirectView(url)).addObject(SEARCH_MODEL,"true");
		return "redirect:/search?search_model=true";
	}

	
	public static boolean isNotValid(final String text) {
		final Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}

}