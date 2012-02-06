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
package org.esgf.globusonline;

import java.net.URI;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/goformview1")
public class GOFormView1Controller {


    private final static Logger LOG = Logger.getLogger(GOFormView1Controller.class);

    private final static String GOFORMVIEW_MODEL = "GoFormView_model";
    private final static String GOFORMVIEW_DATASET_NAME = "GoFormView_Dataset_Name";
    private final static String GOFORMVIEW_FILE_URLS = "GoFormView_File_Urls";
    private final static String GOFORMVIEW_FILE_NAMES = "GoFormView_File_Names";
    private final static String GOFORMVIEW_ERROR = "GoFormView_Error";
    private final static String GOFORMVIEW_ERROR_MSG = "GoFormView_ErrorMsg";
    private final static String GOFORMVIEW_MYPROXY_SERVER = "GoFormView_Myproxy_Server";

    public GOFormView1Controller() {
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView doPost(final HttpServletRequest request) {
        
        //grab the dataset name, file names and urls from the query string
        String dataset_name = request.getParameter("id");
        String [] file_names = request.getParameterValues("child_id");
        String [] file_urls = request.getParameterValues("child_url");
        
        Map<String,Object> model = new HashMap<String,Object>();

        String myproxyServerStr = null;

        try
        {
	    //get the openid here from the cookie
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
            URI myproxyServerURI = Utils.resolveMyProxyViaOpenID(openId);
            LOG.debug("Got MyProxy URI: " + myproxyServerURI);

            String mHost = myproxyServerURI.toString();
            String mPort = "7512";
            if (myproxyServerURI.getHost() != null)
            {
                mHost = myproxyServerURI.getHost();
            }
            if (myproxyServerURI.getPort() != -1)
            {
                mPort = new Integer(myproxyServerURI.getPort()).toString();
            }
            myproxyServerStr = mHost + ":" + mPort;
            LOG.debug("Using MyProxy Server: " + myproxyServerStr);

	    if (request.getParameter(GOFORMVIEW_MODEL)!=null) {
		//it should never come here...
	    }
	    else {
		//place the dataset name, file names and urls into the model
		model.put(GOFORMVIEW_MYPROXY_SERVER, myproxyServerStr);
		model.put(GOFORMVIEW_FILE_URLS, file_urls);
		model.put(GOFORMVIEW_FILE_NAMES, file_names);
		model.put(GOFORMVIEW_DATASET_NAME, dataset_name);
	    }
        }
        catch(Exception e)
        {
	    String errorMsg = "Failed to resolve OpenID: " + e;
            LOG.error("Failed to resolve OpenID: " + e);
            model.put(GOFORMVIEW_ERROR, "error");
            model.put(GOFORMVIEW_ERROR_MSG, errorMsg + "<br><br>Please make sure that you're" +
		      " logged in as a valid user before trying to download data!<br><br>");
        }
        return new ModelAndView("goformview1", model);
    }
}

