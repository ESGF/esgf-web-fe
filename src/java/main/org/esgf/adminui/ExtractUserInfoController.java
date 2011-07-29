package org.esgf.adminui;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.esgf.commonui.GroupOperationsInterface;
import org.esgf.commonui.GroupOperationsXMLImpl;
import org.esgf.commonui.UserOperationsInterface;
import org.esgf.commonui.UserOperationsXMLImpl;
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.jdom.JDOMException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * Implementation of metadata extraction controller responsible for extracting metadata that ARE NOT contained in the solr records.
 * The controller searches through the metdata file to find the proper record.  Currently parsing of TDS, OAI, CAS, and FGDC files are supported.
 *
 * @author john.harney
 *
 */
@Controller
@RequestMapping("/extractuserdataproxy")
public class ExtractUserInfoController {
    private final static Logger LOG = Logger.getLogger(ExtractUserInfoController.class);
    
    
    private GroupOperationsInterface goi;
    private UserOperationsInterface uoi;
    

    private final static boolean debugFlag = true;
    
    public ExtractUserInfoController() {
        LOG.debug("IN ExtractUserInfoController Constructor");
        goi = new GroupOperationsXMLImpl();
        uoi = new UserOperationsXMLImpl();
    }
    
    
    /**
     * Note: GET and POST contain the same functionality.
     *
     * @param  request  HttpServletRequest object containing the query string
     * @param  response  HttpServletResponse object containing the metadata in json format
     * @throws JDOMException 
     *
     */
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, ParserConfigurationException, JDOMException {
        LOG.debug("ExtractUserInfoController doGet");

        String type = request.getParameter("type");
        if(debugFlag)
            LOG.debug("Type: " + type);
        
        if(type.equalsIgnoreCase("edit")) {
            return processEditType(request, response);
        }
        else {
            return null;
        }
    }
    
    /**
     * Note: GET and POST contain the same functionality.
     *
     * @param  request  HttpServletRequest object containing the query string
     * @param  response  HttpServletResponse object containing the metadata in json format
     * @throws JDOMException 
     *
     */
    @RequestMapping(method=RequestMethod.POST)
    public @ResponseBody String doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, ParserConfigurationException, JDOMException {
        LOG.debug("ExtractUserInfoController doPost");

        String type = request.getParameter("type");
        if(debugFlag)
            LOG.debug("Type: " + type);
        
        if(type.equalsIgnoreCase("edit")) {
            return processEditType(request, response);
        }
        else {
            return null;
        }
    }
    
    /**
     * @param  request  HttpServletRequest object containing the query string
     * @param  response  HttpServletResponse object containing the metadata in json format
     * @throws JDOMException 
     *
     */
    private String processEditType(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, ParserConfigurationException, JDOMException {
        String jsonContent = "jsonContent";

        String userName = request.getParameter("id");
        
        if(debugFlag)
            queryStringInfo(request);
        
        /* Search through the data store to see if the id (username or openid) is there 
         * If it is there, then make the appropriate updates,
         * otherwise, just ignore
         * 
         * As of now, the returned data is in JSON format, so there needs to be some conversion between xml/db store to key/value json pairs
         */
        try {
            //xml store version
            LOG.debug("UserName->" + userName);
            
            User user = uoi.getUserObjectFromUserName(userName);
            //String xmlOutput = getXMLTupleOutputFromEdit(userName);
            String xmlOutput = user.toXml();

            
            JSONObject jo = XML.toJSONObject(xmlOutput);

            jsonContent = jo.toString();
            
        }catch(Exception e) {
            LOG.debug("Problem with conversion to json content in processEditType");
        }
        
        if(debugFlag)
            LOG.debug("JsonContent: " + jsonContent);
        
        return jsonContent;
    }
    
    
    
    /**
     * queryStringInfo(HttpServletRequest request)
     * Private method that prints out the contents of the request.  Used mainly for debugging.
     * 
     * @param request
     */
    private void queryStringInfo(HttpServletRequest request) {
        LOG.debug("--------Query String Info--------");
        Enumeration<String> paramEnum = request.getParameterNames();
        
        while(paramEnum.hasMoreElements()) { 
            String postContent = (String) paramEnum.nextElement();
            LOG.debug(postContent+"-->"); 
            LOG.debug(request.getParameter(postContent));
        }
        LOG.debug("--------End Query String Info--------");
    }
}

