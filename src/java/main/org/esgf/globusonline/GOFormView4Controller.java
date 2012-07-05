/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * ÒLicensorÓ) hereby grants to any person (the ÒLicenseeÓ) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organizationÕs name.  If the Software is protected by a proprietary
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
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.Ó
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

import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.globusonline.*;

import esg.common.util.ESGFProperties;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/goformview4")
public class GOFormView4Controller {


    private final static Logger LOG = Logger.getLogger(GOFormView4Controller.class);

    private final static String GOFORMVIEW_MODEL = "GoFormView_model";
    private final static String GOFORMVIEW_DATASET_NAME = "GoFormView_Dataset_Name";
    private final static String GOFORMVIEW_USER_CERTIFICATE = "GoFormView_UserCertificate";
    private final static String GOFORMVIEW_FILE_URLS = "GoFormView_File_Urls";
    private final static String GOFORMVIEW_FILE_NAMES = "GoFormView_File_Names";
    private final static String GOFORMVIEW_ERROR = "GoFormView_Error";
    private final static String GOFORMVIEW_ERROR_MSG = "GoFormView_ErrorMsg";
    private final static String GOFORMVIEW_TRANSFER_INFO1 = "GoFormView_TransferInfo1";
    private final static String GOFORMVIEW_TRANSFER_INFO2 = "GoFormView_TransferInfo2";
    private final static String GOFORMVIEW_TRANSFER_INFO3 = "GoFormView_TransferInfo3";
    private final static String GOFORMVIEW_TRANSFER_INFO4 = "GoFormView_TransferInfo4";
    private final static String GOFORMVIEW_GO_USERNAME = "GoFormView_GOUsername";

    // should be configurable?
    private final static String CA_CERTIFICATE_FILE = "/etc/grid-security/certificates/97552d04.0";
    

    public GOFormView4Controller()
    {
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView doPost(final HttpServletRequest request)
    {
        /* get model params here */
        String dataset_name = request.getParameter("id");
        String userCertificate = request.getParameter("usercertificate");
        String goUserName = request.getParameter("gousername");
        String target = request.getParameter("target");
        String [] file_names = request.getParameterValues("child_id");
        String [] file_urls = request.getParameterValues("child_url");
        String[] endpointInfos = request.getParameterValues("endpointinfos");
        String endpoint = request.getParameter("endpointdropdown");
        String createdSrcEndpoint = null, createdDestEndpoint = null;

        //if this request comes via go form view 3, then obtain the request parameters for myproxy username
        String srcMyproxyUserName = request.getParameter("srcmyproxyuser");
        String srcMyproxyUserPass = request.getParameter("srcmyproxypass");
        String myProxyServerStr = request.getParameter("srcmyproxyserver");

        StringBuffer errorStatus = new StringBuffer("Steps leading up to the error are shown below:<br><br>");

        LOG.debug("GOFORMView4Controller got Certificate " + userCertificate);
        LOG.debug("GOFORMView4Controller got Target " + target);
        LOG.debug("GOFORMView4Controller got selected endpoint " + endpoint);
        LOG.debug("GOFORMView4Controller got Src Myproxy User " + srcMyproxyUserName);
        LOG.debug("GOFORMView4Controller got Src Myproxy Pass ******");
        LOG.debug("GOFORMView4Controller got Src Myproxy Server " + myProxyServerStr);

        System.out.println("GOFORMView4Controller got Certificate " + userCertificate);
        System.out.println("GOFORMView4Controller got Target " + target);
        System.out.println("GOFORMView4Controller got selected endpoint " + endpoint);
        System.out.println("GOFORMView4Controller got Src Myproxy User " + srcMyproxyUserName);
        System.out.println("GOFORMView4Controller got Src Myproxy Pass ******");
        System.out.println("GOFORMView4Controller got Src Myproxy Server " + myProxyServerStr);

        Map<String,Object> model = new HashMap<String,Object>();

        JGOTransfer transfer = new JGOTransfer(goUserName, userCertificate, userCertificate, CA_CERTIFICATE_FILE);
        transfer.setVerbose(true);
        try
        {
            String newURL = null, goEP = null;
            String[] pieces = null;
            Vector<String> fileList = null;
            HashMap<String, Vector<String>> sourceMap = new HashMap<String, Vector<String>>();

            transfer.initialize();

            // find the endpointInfo line that matches the endpoint the user selected
            String endpointInfo = Utils.getEndpointInfoFromEndpointStr(endpoint, endpointInfos);
            System.out.println("User selected endpoint that has the info: " + endpointInfo);
            LOG.debug("User selected endpoint that has the info: " + endpointInfo);

            boolean isGlobusConnect = endpointInfo.endsWith("true");

            // FIXME: Cache from previous time we called this?
            // or reconstruct from the other format of them that we have?
            Vector<EndpointInfo> goEndpointInfos = transfer.listEndpoints();

            // first pass, find all sources
            // we create a mapping of GO endpoints to Filelists
            for(String curURL : file_urls)
            {
                pieces = curURL.split("//");
                if ((pieces != null) && (pieces.length > 1))
                {
                    goEP = Utils.lookupGOEPBasedOnGridFTPURL(pieces[1], goEndpointInfos, true);
                    if (goEP == null)
                    {
                        goEP = Utils.lookupGOEPBasedOnGridFTPURL(pieces[1], goEndpointInfos, false);
                    }

                    if (!sourceMap.containsKey(goEP))
                    {
                        LOG.debug("Mapped GridFTP Server " + pieces[1] + " to GO EP " + goEP);
                        System.out.println("Mapped GridFTP Server " + pieces[1] + " to GO EP " + goEP);
                        sourceMap.put(goEP, new Vector<String>());
                    }

                    fileList = sourceMap.get(goEP);
                    newURL = "//" + pieces[2];

                    LOG.debug("Transformed " + curURL + " into " + newURL);
                    System.out.println("Transformed " + curURL + " into " + newURL);
                    fileList.add(newURL);
                }
                else
                {
                    LOG.debug("Failed to split URL on //: " + curURL);
                    System.out.println("Failed to split URL on //: " + curURL);
                }
            }

            // For now we always just grab the first endpoint since we
            // can only handle a single source endpoint (per transfer)
            // ... break up into multiple transfers later when we
            // support transfers of multiple data sets at once
            Map.Entry<String, Vector<String>> entry = sourceMap.entrySet().iterator().next();
            String goSourceEndpoint = entry.getKey();

            System.out.println("Got GO Source EP: " + goSourceEndpoint);
            if (goSourceEndpoint != null)
            {
                fileList = entry.getValue();
            }
            else
            {
                // Create source endpoint matching first known Endpoint info
                EndpointInfo info = goEndpointInfos.get(0);
                String srcEndpointInfo = info.getEPName() + "^^" + info.getHosts() +
                    "^^" + myProxyServerStr + "^^" + info.isGlobusConnect();
                goSourceEndpoint = Utils.createGlobusOnlineEndpointFromEndpointInfo(
                    transfer, goUserName, srcEndpointInfo);
                createdSrcEndpoint = goSourceEndpoint;
            }

            LOG.debug("Using GO Source EP: " + goSourceEndpoint);
            System.out.println("Using GO Source EP: " + goSourceEndpoint);

            errorStatus.append("Source endpoint resolved as \"");
            errorStatus.append(goSourceEndpoint);
            errorStatus.append("\".<br>");

            // first activate the source endpoint
            LOG.debug("Activating source endpoint " + goSourceEndpoint);
            System.out.println("Activating source endpoint " + goSourceEndpoint);
            errorStatus.append("Attempting to activate Source Endpoint " + goSourceEndpoint + " ...<br>");
            try
            {
                // first try the activation as-is, with the 'original' myproxy info
                transfer.activateEndpoint(goSourceEndpoint, srcMyproxyUserName, srcMyproxyUserPass);
            }
            catch(Exception e)
            {
                // if that failed, manually override myproxy server to match user's
                // Create source endpoint matching first known Endpoint info
                LOG.debug("[*] Attempting newly created EP with MyProxy Server " + myProxyServerStr);
                System.out.println("[*] Attempting newly created EP with MyProxy Server " + myProxyServerStr);
                EndpointInfo info = goEndpointInfos.get(0);
                String srcEndpointInfo = info.getEPName() + "^^" + info.getHosts() +
                    "^^" + myProxyServerStr + "^^" + info.isGlobusConnect();
                goSourceEndpoint = Utils.createGlobusOnlineEndpointFromEndpointInfo(
                    transfer, goUserName, srcEndpointInfo);
                createdSrcEndpoint = goSourceEndpoint;

                LOG.debug("Activating source endpoint " + goSourceEndpoint);
                System.out.println("Activating source endpoint " + goSourceEndpoint);
                transfer.activateEndpoint(goSourceEndpoint, srcMyproxyUserName, srcMyproxyUserPass);
            }
            errorStatus.append("Source Endpoint activated properly!<br>");

            String[] endpointPieces = endpointInfo.split("\\^\\^");
            String destEPName = endpointPieces[0];

            // second, activate the target endpoint (special case GlobusConnect)
            if (isGlobusConnect == true)
            {
                LOG.debug("Detected Globus Connect target endpoint: " + destEPName);
                System.out.println("Detected Globus Connect target endpoint: " + destEPName);
                errorStatus.append("Detected Globus Connect target endpoint.<br>");
                errorStatus.append("Attempting to activate Destination Endpoint " + destEPName + " ...<br>");
                transfer.activateEndpoint(destEPName);
                errorStatus.append("Destination Endpoint activated properly!<br>");
            }
            else
            {
                // find if an existing endpoint exists in this user's namespace
                EndpointInfo destInfo = Utils.getLocalEndpointBasedOnGlobalName(
                    goUserName, destEPName, goEndpointInfos);
                if (destInfo == null)
                {
                    destEPName = Utils.createGlobusOnlineEndpointFromEndpointInfo(
                        transfer, goUserName, endpointInfo);
                    createdDestEndpoint = destEPName;
                }
                else
                {
                    // if so, use it as the dest since it's presumably already configured
                    destEPName = destInfo.getEPName();
                }

                LOG.debug("Activating destination endpoint " + destEPName);
                System.out.println("Activating destination endpoint " + destEPName);
                errorStatus.append("Attempting to activate Destination Endpoint " + destEPName + " ...<br>");
                transfer.activateEndpoint(destEPName, srcMyproxyUserName, srcMyproxyUserPass);
                errorStatus.append("Destination Endpoint activated properly!<br>");
            }

            // kick off the transfer here!
            errorStatus.append("Attempting to start Globus Online Transfer ...<br>");
            String taskID = transfer.transfer(goSourceEndpoint, destEPName, fileList, target);
            if (taskID != null)
            {
                errorStatus.append("Globus Online Transfer got TaskID " + taskID + ".<br>");
                String transferInfo1 = "The transfer has been accepted and a task has been " +
                    "created and queued for execution.";
                String transferInfo2 = "Globus Online TaskID: " + taskID;
                LOG.debug("Started Globus Online transfer with TaskID: " + taskID);

                if (request.getParameter(GOFORMVIEW_MODEL)!=null) {
                }
                else
                {
                    model.put(GOFORMVIEW_TRANSFER_INFO1, transferInfo1);
                    model.put(GOFORMVIEW_TRANSFER_INFO2, transferInfo2);
                }
            }
            else
            {
                String error = errorStatus.toString() + "<br><b>Main Error:</b><br><br>Transfer failed";
                model.put(GOFORMVIEW_ERROR, "error");
                model.put(GOFORMVIEW_ERROR_MSG, error);
                LOG.error("Failed to initiate Globus Online transfer.");
            }
        }
        catch(Exception e)
        {
            String error = errorStatus.toString() + "<br><b>Main Error:</b><br><br>" + e.toString();
            model.put(GOFORMVIEW_ERROR, "error");
            model.put(GOFORMVIEW_ERROR_MSG, error);
            LOG.error("Failed to initialize Globus Online: " + e);

            System.out.println("Trying to teardown created source endpoints ...");
            if (createdSrcEndpoint != null)
            {
                try { transfer.removeEndpoint(createdSrcEndpoint); } catch(Exception e1) {}
            }
            if (createdDestEndpoint != null)
            {
                try { transfer.removeEndpoint(createdDestEndpoint); } catch(Exception e2) {}
            }
            System.out.println("Attempted endpoint removal complete");
        }
        return new ModelAndView("goformview4", model);
    }
}

