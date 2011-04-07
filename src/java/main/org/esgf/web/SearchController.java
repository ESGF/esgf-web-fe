/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * “Licensor”) hereby grants to any person (the “Licensee”) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization’s name.  If the Software is protected by a proprietary
 * trademark owned by Licensor or the Department of Energy, then derivative
 * works of the Software may not be distributed using the trademark without
 * the prior written approval of the trademark owner.
 *
 * 2. Neither the names of Licensor nor the Department of Energy may be used
 * to endorse or promote products derived from this Software without their
 * specific prior written permission.
 *
 * 3. The Software, with or without modification, must include the following
 * acknowledgment:
 *
 *    "This product includes software produced by UT-Battelle, LLC under
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.”
 *
 * 4. Licensee is authorized to commercialize its derivative works of the
 * Software.  All derivative works of the Software must include paragraphs 1,
 * 2, and 3 above, and the DISCLAIMER below.
 *
 *
 * DISCLAIMER
 *
 * UT-Battelle, LLC AND THE GOVERNMENT MAKE NO REPRESENTATIONS AND DISCLAIM
 * ALL WARRANTIES, BOTH EXPRESSED AND IMPLIED.  THERE ARE NO EXPRESS OR
 * IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE,
 * OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY PATENT, COPYRIGHT,
 * TRADEMARK, OR OTHER PROPRIETARY RIGHTS, OR THAT THE SOFTWARE WILL
 * ACCOMPLISH THE INTENDED RESULTS OR THAT THE SOFTWARE OR ITS USE WILL NOT
 * RESULT IN INJURY OR DAMAGE.  The user assumes responsibility for all
 * liabilities, penalties, fines, claims, causes of action, and costs and
 * expenses, caused by, resulting from or arising out of, in whole or in part
 * the use, storage or disposal of the SOFTWARE.
 *
 *
 ******************************************************************************/


/**
 * On request mapping:
 *
 *     url rewrite filter will take over first, then we do regular Spring mapping.
 *     RedirectView is discouraged here as it will mess up the current rewrite
 *     rule, use "redirect:" prefix instead, and it is regarded as a better alternative
 *     anyway.
 *
 * For any redirect trouble, please refers to ROOT/urlrewrite.xml
 *
 * @author Feiyi Wang (fwang2@ornl.gov)
 *
 */
package org.esgf.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esgf.manager.InputManager;
import org.esgf.manager.InputManagerImpl;
import org.esgf.manager.OutputManager;
import org.esgf.manager.OutputManagerImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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

    private final static Logger LOG = Logger.getLogger(SearchController.class);

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
     * @throws Exception
     */
    @ModelAttribute(SEARCH_INPUT)
    public SearchInputImpl formBackingObject(final HttpServletRequest request) throws Exception {

        LOG.debug("formBackingObject() called");


        InputManager inputManager = new InputManagerImpl(facetProfile,request);

        //input geo constraints
        inputManager.inputGeospatialConstraints();

        //input temporal constrants
        inputManager.inputTemporalConstraints();

        //input facet profile
        inputManager.inputFacetProfile();

        SearchInputImpl input = (SearchInputImpl) inputManager.getInput();


        return input;

    }

    /**
     * Return search results as partial view
     */
    @RequestMapping(value="/results", method=RequestMethod.GET)
    public String doSearchResults(
            final HttpServletRequest request,
            Model model,
            final @ModelAttribute(SEARCH_INPUT) SearchInputImpl input,
            final BindingResult result) throws Exception {

        LOG.debug("doSearchResults() called");



        // set retrieval of all facets in profile
        input.setFacets(new ArrayList<String>(facetProfile.getTopLevelFacets().keySet()));


        //create a post query processor object
        OutputManager outputManager = new OutputManagerImpl(searchService,
                                                        facetProfile,
                                                        request,
                                                        input);



        //if the whichGeo flag is switched to Radius, we must perform post-query processing
        //here the centroid filter is called
        String [] parValues = request.getParameterValues("whichGeo");

        if(parValues!=null)
        {
            if(parValues[0].equals("Radius"))
            {
                outputManager.processCentroidFilter();
            }
        }
        SearchOutput output = outputManager.getOutput();


        // populate model
        model.addAttribute(SEARCH_OUTPUT, output);
        model.addAttribute(FACET_PROFILE, facetProfile);
        model.addAttribute(SEARCH_INPUT, input);


        return "search_results";
    }

    /**
     * Return facets as partial view
     */
    @RequestMapping(value="/facets", method=RequestMethod.GET)
    public String doSearchFacets(
            final HttpServletRequest request,
            Model model,
            final @ModelAttribute(SEARCH_INPUT) SearchInputImpl input,
            final BindingResult result) throws Exception {

        LOG.debug("doSearchFacets() called");


        // set retrieval of all facets in profile
        input.setFacets(new ArrayList<String>(facetProfile.getTopLevelFacets().keySet()));


        //create a post query processor object
        OutputManager outputManager = new OutputManagerImpl(searchService,
                                                        facetProfile,
                                                        request,
                                                        input);

        //if the whichGeo flag is switched to Radius, we must perform post-query processing
        //here the centroid filter is called
        String [] parValues = request.getParameterValues("whichGeo");
        if(parValues[0].equals("Radius"))
        {
            outputManager.facetRecount();
        }


        //get the output
        SearchOutput output = outputManager.getOutput();


        // populate model
        model.addAttribute(SEARCH_OUTPUT, output);
        model.addAttribute(FACET_PROFILE, facetProfile);
        model.addAttribute(SEARCH_INPUT, input);


        // save model in session
        return "search_facets";
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

        LOG.debug("doGET() called");


        Map<String,Object> model = new HashMap<String,Object>();

        if (request.getParameter(SEARCH_MODEL)!=null) {

            // retrieve model from session
            model = (Map<String,Object>)request.getSession().getAttribute(SEARCH_MODEL);

        } else {

            // set retrieval of all facets in profile
            input.setFacets(new ArrayList<String>(facetProfile.getTopLevelFacets().keySet()));

            // execute query for facets
            final SearchOutput output = searchService.search(input, false, true);

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
     * Method invoked in response to a POST request:
     * both results and facets are retrieved.
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
        LOG.debug("doPost() called");

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
            //if (LOG.isTraceEnabled())
            //{
                LOG.debug("\tdoPost: results="+output);
            //}


            // store new model in session
            final Map<String,Object> model = new HashMap<String,Object>();
            model.put(SEARCH_INPUT, input);
            model.put(SEARCH_OUTPUT, output);
            model.put(FACET_PROFILE, facetProfile);

            request.getSession().setAttribute(SEARCH_MODEL, model);

        }

        LOG.debug("End doGET()\n");


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

