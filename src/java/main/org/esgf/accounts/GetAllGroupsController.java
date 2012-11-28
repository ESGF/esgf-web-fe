package org.esgf.accounts;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Enumeration;
import java.util.List;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.esgf.commonui.GroupOperationsESGFDBImpl;
import org.esgf.commonui.GroupOperationsInterface;
import org.esgf.commonui.GroupOperationsXMLImpl;
import org.esgf.commonui.UserOperationsESGFDBImpl;
import org.esgf.commonui.UserOperationsInterface;
import org.esgf.commonui.UserOperationsXMLImpl;
import org.esgf.commonui.Utils;
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;

import org.jdom.JDOMException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import esg.common.util.ESGFProperties;

import esg.node.security.UserInfo;
import esg.node.security.UserInfoCredentialedDAO;
import esg.node.security.GroupRoleDAO;

/**
 * Implementation of this controller will return a list of all the groups on this.node
 * The controller searches node records, and then returns a list of groups
 * @author Matthew Harris 
 */
@Controller
@RequestMapping("/showallgroupsproxy")
public class GetAllGroupsController {
   
    private final static Logger LOG = Logger.getLogger(GetAllGroupsController.class);
    
    private final static boolean debugFlag = true;

    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
    private GroupRoleDAO myGroupRoleDAO;
        
    public GetAllGroupsController() throws FileNotFoundException, IOException {
        if(Utils.environmentSwitch) {
            // try to set up myUserInfoDAO here.
            ESGFProperties myESGFProperties = new ESGFProperties();
            this.passwd = myESGFProperties.getAdminPassword();        
            this.myUserInfoDAO = new UserInfoCredentialedDAO(root,passwd,myESGFProperties);
            this.myGroupRoleDAO = new GroupRoleDAO(myESGFProperties);
        }
        
        LOG.debug("IN GetAllGroupsController Constructor");
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
    public @ResponseBody String doGet(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("GetAllGroupsController doGet");

        return "";        
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
        System.out.println("ADF I'm in");
        String query = (String)request.getParameter("query");
        String userName = "";
        boolean pass = true;
        String errormessage = "";
        String groups = "";
        JSONObject jsonObj = null;
        
        try {
            jsonObj = new JSONObject(query);
            userName = jsonObj.getString("userName");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            errormessage = "error in parsing the json text string :" + query;
            pass = false;
        }
        
        List<String[]> results = myGroupRoleDAO.getGroupEntriesNotFor(myUserInfoDAO.getUserById(userName).getOpenid());

        for(String[] string : results){
          groups = groups + Arrays.toString(string);
        }
        groups = groups + "[0, 1, 2, 3, 4]";

        LOG.debug("GetAllGroupsController -->" + userName);

        String xmlOutput = "<EditOutput>";
        if(pass){
          //Returning list of all groups
          xmlOutput += "<status>success</status>";
          xmlOutput += "<comment>" + groups + "</comment>";
          xmlOutput += "</EditOutput>";
         
        } else {
          //warning warning error warning warning
          xmlOutput += "<status>fail</status>";
          xmlOutput += "<comment>" + errormessage + "</comment>";
          xmlOutput += "</EditOutput>";
        }
        JSONObject jo = XML.toJSONObject(xmlOutput);
        String jsonContent = jo.toString();        
        return jsonContent;
    }
}



