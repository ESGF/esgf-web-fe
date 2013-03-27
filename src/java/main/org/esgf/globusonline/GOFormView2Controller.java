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
 * organization's name.  If the Software is protected by a proprietary
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
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy."
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
 * @author Neill Miller (neillm@mcs.anl.gov), Feiyi Wang (fwang2@ornl.gov)
 *
 */
package org.esgf.globusonline;

import java.net.URI;

import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import org.globusonline.*;
import org.globus.myproxy.MyProxyException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/goformview2")
public class GOFormView2Controller {


    private final static Logger LOG = Logger.getLogger(GOFormView2Controller.class);

    private final static String GOFORMVIEW_MODEL = "GoFormView_model";
    private final static String GOFORMVIEW_DATASET_NAME = "GoFormView_Dataset_Name";
    private final static String GOFORMVIEW_USER_CERTIFICATE = "GoFormView_UserCertificate";
    private final static String GOFORMVIEW_GO_USERNAME = "GoFormView_GOUsername";
    private final static String GOFORMVIEW_FILE_URLS = "GoFormView_File_Urls";
    private final static String GOFORMVIEW_FILE_NAMES = "GoFormView_File_Names";
    private final static String GOFORMVIEW_ENDPOINTS = "GoFormView_Endpoints";
    private final static String GOFORMVIEW_ENDPOINTINFOS = "GoFormView_EndpointInfos";
    private final static String GOFORMVIEW_ERROR = "GoFormView_Error";
    private final static String GOFORMVIEW_ERROR_MSG = "GoFormView_ErrorMsg";
    private final static String GOFORMVIEW_MYPROXY_SERVER = "GoFormView_Myproxy_Server";
    private final static String GOFORMVIEW_SRC_MYPROXY_USER = "GoFormView_SrcMyproxyUser";
    private final static String GOFORMVIEW_SRC_MYPROXY_PASS = "GoFormView_SrcMyproxyPass";

    // should be configurable?
    private final static String CA_CERTIFICATE_FILE = "/etc/grid-security/certificates/97552d04.0";
    
    public GOFormView2Controller() {
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView doPost(final HttpServletRequest request)
    {
        Map<String,Object> model = new HashMap<String,Object>();

        /* Get the params from the form request */
        String dataset_name = request.getParameter("id");
        String [] file_names = request.getParameterValues("child_id");
        String [] file_urls = request.getParameterValues("child_url");
        String goUserName = request.getParameter("goUserName"); 
        String myProxyServerStr = request.getParameter(GOFORMVIEW_MYPROXY_SERVER);
	String myProxyUserName = request.getParameter(GOFORMVIEW_SRC_MYPROXY_USER);
        String myProxyUserPass = request.getParameter("myProxyUserPass");
        //String goEmail = request.getParameter("goEmail");

        LOG.debug("GOFormView2Controller: dataset_name = " + dataset_name);
        LOG.debug("GOFormView2Controller: file_names = " + file_names);
        LOG.debug("GOFormView2Controller: file_urls = " + file_urls);

        LOG.debug("goUserName: " + goUserName + " " + "myProxyUserName: " + myProxyUserName +
                           " " + "myProxyUserPass: " + "*****" + " myProxyServerStr: " + myProxyServerStr);
        System.out.println("goUserName: " + goUserName + " " + "myProxyUserName: " + myProxyUserName +
                           " " + "myProxyUserPass: " + "*****" + " myProxyServerStr: " + myProxyServerStr);

        StringBuffer errorStatus = new StringBuffer("Steps leading up to the error are shown below:<br><br>");
        errorStatus.append("Globus Online Username entered: ");
        errorStatus.append(goUserName);
        errorStatus.append("<br>MyProxy Username entered: ");
        errorStatus.append(myProxyUserName);
        errorStatus.append("<br>");

        try
        {
            LOG.debug("Initializing Globus Online Transfer object");

            JGOTransfer transfer = new JGOTransfer(
                goUserName, myProxyServerStr, myProxyUserName,
                myProxyUserPass, CA_CERTIFICATE_FILE);

            transfer.setVerbose(true);
            transfer.initialize();

            LOG.debug("Globus Online Transfer object Initialize complete");

            errorStatus.append("Globus Online Transfer object Initialize complete<br>");

            String userCertificateFile = transfer.getUserCertificateFile();
            LOG.debug("Retrieved user credential file: " + userCertificateFile);

            LOG.debug("About to retrieve available endpoints");
            Vector<EndpointInfo> endpoints = transfer.listEndpoints();
            LOG.debug("We pulled down " + endpoints.size() + " endpoints");
            errorStatus.append("Endpoints retrieved<br>");

            // Make sure if any of the user's local endpoints are
            // globus connect endpoints, they are listed first
            endpoints = Utils.bringGCEndpointsToTop(goUserName, endpoints);

            if (request.getParameter(GOFORMVIEW_MODEL)!=null) {

            }
            else
            {
                LOG.debug("Placing all info in the model");
                model.put(GOFORMVIEW_FILE_URLS, file_urls);
                model.put(GOFORMVIEW_FILE_NAMES, file_names);
                model.put(GOFORMVIEW_DATASET_NAME, dataset_name);
                model.put(GOFORMVIEW_USER_CERTIFICATE, userCertificateFile);
                model.put(GOFORMVIEW_GO_USERNAME, goUserName);
                model.put(GOFORMVIEW_SRC_MYPROXY_USER, myProxyUserName);
                model.put(GOFORMVIEW_SRC_MYPROXY_PASS, myProxyUserPass);
                model.put(GOFORMVIEW_MYPROXY_SERVER, myProxyServerStr);

                String[] endPointNames = getDestinationEndpointNames(endpoints);
                String[] endPointInfos = constructEndpointInfos(endpoints);

                LOG.debug("Retrieved an array of " + endPointNames.length + " Endpoint names");
                LOG.debug("Retrieved an array of " + endPointInfos.length + " EndpointInfo strings");

                model.put(GOFORMVIEW_ENDPOINTS, endPointNames);
                model.put(GOFORMVIEW_ENDPOINTINFOS, endPointInfos);

                LOG.debug("All info placed in the model!");
            }
        }
        catch(MyProxyException me)
        {
            String error = me.toString();
            if (error.contains("invalid password"))
            {
                error = "We could not start your download because of the following error:<br><br>The password you provided for your ESGF account with username \"" + myProxyUserName + "\" is incorrect.<br><br>Please use your browser back button to go to the previous page, and try the request again.";
            }
            else
            {
                error = errorStatus.toString() + "<br><b>Main Error:</b><br><br>" + me.toString();
            }
            model.put(GOFORMVIEW_ERROR, "error");
            model.put(GOFORMVIEW_ERROR_MSG, error);
            LOG.error("Failed to initialize Globus Online: " + me);
        }
        catch(JGOTransferException jgte)
        {
            String error = jgte.toString();
            if (error.contains("Endpoint List failure: ClientError.AuthenticationFailed(400 Authentication Failed)"))
            {
                error = "Your Globus Online Account \"" + goUserName + "\" is not linked to the ESGF Portal account.<br><br>Please follow steps in <a href=\"http://www.esgf.org/wiki/ESGF_GO_AccountSetup\">http://www.esgf.org/wiki/ESGF_GO_AccountSetup</a> to link accounts.";
            }
            model.put(GOFORMVIEW_ERROR, "error");
            model.put(GOFORMVIEW_ERROR_MSG, error);
            LOG.error("Failed to initialize Globus Online: " + jgte);
        }
        catch(Exception e)
        {
            String error = errorStatus.toString() + "<br><b>Main Error:</b><br><br>" + e.toString();
            model.put(GOFORMVIEW_ERROR, "error");
            model.put(GOFORMVIEW_ERROR_MSG, error);
            LOG.error("Failed to initialize Globus Online: " + e);
            e.printStackTrace();
        }
        return new ModelAndView("goformview2", model);
    }

    private String[] getDestinationEndpointNames(Vector<EndpointInfo> endpoints)
    {
        int numEndpoints = endpoints.size();
        String [] endPointNames = new String[numEndpoints];

        for(int i = 0; i < numEndpoints; i++)
        {
            endPointNames[i] = endpoints.get(i).getEPName();
        }
        return endPointNames;
    }

    private String[] constructEndpointInfos(Vector<EndpointInfo> endpoints)
    {
        int numEndpoints = endpoints.size();
        String [] endPointNames = new String[numEndpoints];

        for(int i = 0; i < numEndpoints; i++)
        {
            // we encode this in a string as follows
            // EPNAME^^HOSTS^^MYPROXYSERVER^^ISGLOBUSCONNECT
            endPointNames[i] = endpoints.get(i).getEPName() + "^^" + endpoints.get(i).getHosts() +
                "^^" + endpoints.get(i).getMyproxyServer() + "^^" + endpoints.get(i).isGlobusConnect();
        }
        return endPointNames;
    }
}

