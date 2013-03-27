/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * "Licensor") hereby grants to any person (the "Licensee") obtaining a copy
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
 * @author Neill Miller (neillm@mcs.anl.gov), Feiyi Wang (fwang2@ornl.gov),
 *	   Eric Blau (blau@mcs.anl.gov)
 *
 */
package org.esgf.globusonline;

import java.net.URI;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.*;
import javax.servlet.ServletException;


import org.openid4java.discovery.yadis.YadisException;

import esg.common.util.ESGFProperties;

import esg.node.security.UserInfo;
import esg.node.security.UserInfoCredentialedDAO;
import esg.node.security.UserInfoDAO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import org.globusonline.nexus.*;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import org.globusonline.nexus.exception.NexusClientException;
import org.globusonline.nexus.exception.ValueErrorException;
import org.json.JSONException;
import org.json.JSONObject;

@Controller
@RequestMapping(value="/goauthview1")
public class GOauthView1Controller {


    private final static Logger LOG = Logger.getLogger(GOFormView1Controller.class);
    private final static String GLOBUSONLINE_PROPERTIES_FILE = "/esg/config/globusonline.properties";
    private final static String GOFORMVIEW_ERROR = "GoFormView_Error";
    private final static String GOFORMVIEW_ERROR_MSG = "GoFormView_ErrorMsg";

    public GOauthView1Controller() {
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView doPost(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        //grab the dataset name, file names and urls from the query string
        String dataset_name = request.getParameter("id");
        String [] file_names = request.getParameterValues("child_id");
        String [] file_urls = request.getParameterValues("child_url");
	String esg_user="";
	String esg_password="";
	//String e;
	//grab the credential string
	String credential = request.getParameter("credential");
	System.out.println("Starting GlobusOnline workflow");
	//System.out.println("\n\n\n\t\tGO Credential " + credential + "\n\n\n");

		StringBuffer currentURL = request.getRequestURL();
		String currentURI = request.getRequestURI();
	        //System.out.println("current URL is: " + currentURL);
	        //System.out.println("current URI is: " + currentURI);
		//System.out.println("index is: " + currentURL.lastIndexOf(currentURI));
		String BaseURL = currentURL.substring(0, currentURL.lastIndexOf(currentURI));
		//System.out.println("BaseURL string is: " + BaseURL );

//Create a session if it doesn't already exist, so we can save state.
  	HttpSession session = request.getSession(true);
	if (session.isNew() == false) {
	  session.invalidate();
	  session = request.getSession(true);
	}

	        //System.out.println("Auth1, session id is:" + session.getId());

		session.setAttribute("fileUrls", file_urls);
		session.setAttribute("fileNames", file_names);
		session.setAttribute("datasetName", dataset_name);
		session.setAttribute("baseurl", BaseURL);
		if (!(credential == null)){
		  session.setAttribute("usercertificatefile", credential);
		}

        Cookie [] cookies = request.getCookies();
            String openId = "";

            for(int i = 0; i < cookies.length; i++)
            {
                if (cookies[i].getName().equals("esgf.idp.cookie"))
                {
                    openId = cookies[i].getValue();
                }
            }

            LOG.debug("Got User OpenID: " + openId); 
        	Map<String,Object> model = new HashMap<String,Object>();
		// Create the client
		Properties GOProperties = getGOProperties();
		String PortalID = (String) GOProperties.getProperty("GOesgfPortalID", "bogususer");
		String PortalPass = (String) GOProperties.getProperty("GOesgfPortalPassword", "boguspassword");
		
		String loginUri = "";
		try{
                GoauthClient cli = new GoauthClient("nexus.api.globusonline.org", "globusonline.org",PortalID, PortalPass);
                cli.setIgnoreCertErrors(true);

		// Redirect the user agent to the globusonline log in page
		loginUri = cli.getLoginUrl(response.encodeURL(BaseURL + "/esgf-web-fe/goauthview2"));

		} catch (NexusClientException e) {
		  System.out.println("ERROR:  GOesgfPortalID and/or GOesgfPortalPassword wrong or not set.");
            //            e.printStackTrace();
            	  model.put(GOFORMVIEW_ERROR, "error");
String error_msg = "GlobusOnline Configuration file not found. Please create /esg/config/globusonline.properties and populate it with GOesgfPortalID and GOesgfPortalPassword";
            	  model.put(GOFORMVIEW_ERROR_MSG, error_msg);
	    	  return new ModelAndView("goauthview3", model);
		}
        String myproxyServerStr = null;

        return new ModelAndView("redirect:" + loginUri, model);
    }

private Properties getGOProperties()
  {
 	Properties properties = new Properties();

        String propertiesFile = GLOBUSONLINE_PROPERTIES_FILE;

        try {
            properties.load(new FileInputStream(propertiesFile));

        } catch(FileNotFoundException fe) {

	    String error_msg = "GlobusOnline Configuration file not found. Please create /esg/config/globusonline.properties and populate it with GOesgfPortalID and GOesgfPortalPassword";

            System.out.println("---------------------------------------------------------------------");
	    System.out.println(error_msg);
            System.out.println("---------------------------------------------------------------------");

        } catch(Exception e) {
            e.printStackTrace();
        }
  return properties;
  }
}
