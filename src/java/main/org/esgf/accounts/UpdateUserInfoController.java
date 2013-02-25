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

/**
 * Implementation of this controller will update a user's information
 * @author Matthew Harris 
 */

@Controller
@RequestMapping("/updateuserinfoproxy")
public class UpdateUserInfoController {
   
    private final static Logger LOG = Logger.getLogger(UpdateUserInfoController.class);
    
    private final static boolean debugFlag = true;
     
    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
        
    public UpdateUserInfoController() throws FileNotFoundException, IOException {
        /*
        if(Utils.environmentSwitch) {
            // try to set up myUserInfoDAO here.
            ESGFProperties myESGFProperties = new ESGFProperties();
            this.passwd = myESGFProperties.getAdminPassword();        
            this.myUserInfoDAO = new UserInfoCredentialedDAO(root,passwd,myESGFProperties);
        }
        */
        LOG.debug("IN UpdateUserInfoController Constructor");
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
        LOG.debug("UpdateUserInfoController doGet");

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
        
        LOG.debug("ExtractUserInfoController doPost");
        String query = (String)request.getParameter("query");
        String userName = "";
        String firstName = "";        
        String middleName = ""; 
        String lastName = ""; 
        String email = ""; 
        String organization = ""; 
        String city = ""; 
        String state = ""; 
        String country = ""; 
        String domainName = ""; 
        boolean pass = true;
        String errormessage = "";        
        JSONObject jsonObj = null;
        
        try {
            jsonObj = new JSONObject(query);
            userName = jsonObj.getString("userName");
            firstName = jsonObj.getString("firstName");
            middleName = jsonObj.getString("middleName");
            lastName = jsonObj.getString("lastName");
            email = jsonObj.getString("email");
            organization = jsonObj.getString("organization");
            city = jsonObj.getString("city");
            state = jsonObj.getString("state");
            country = jsonObj.getString("country");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            errormessage = "error in parsing the json text string :" + query;
            pass = false;
        }

        if(middleName == null || middleName.equals("")) {
            middleName = "N/A";
        }
        if(email == null || email.equals("")) {
            email = "N/A";
        }
        if(organization == null || organization.equals("")) {
            organization = "N/A";
        }
        if(city == null || city.equals("")) {
            city = "N/A";
        }
        if(state == null || state.equals("")) {
            state = "N/A";
        }
        if(country == null || country.equals("")) {
            country = "N/A";
        }
        
        UserOperationsInterface uoi;
        if(Utils.environmentSwitch) {
            uoi = new UserOperationsESGFDBImpl();
        }
        else {
            uoi = new UserOperationsXMLImpl();
        }
        String userId = uoi.getUserIdFromUserName(userName);
        
        uoi.editUser(userId,firstName,middleName,lastName,email,userName,organization,city,state,country);
        LOG.debug("UpdateUserInfoController -->" + userName);
        
        String xmlOutput = "<EditOutput>";
        if(pass){
          //user has been added to group
          xmlOutput += "<status>success</status>";
          xmlOutput += "<comment>Your Account is being updated. Please wait...</comment>";
          xmlOutput += "</EditOutput>";
         
        } else {
          //user not added to group
          xmlOutput += "<status>fail</status>";
          xmlOutput += "<comment>" + errormessage + "</comment>";
          xmlOutput += "</EditOutput>";
        }
        JSONObject jo = XML.toJSONObject(xmlOutput);
        String jsonContent = jo.toString();        
        return jsonContent;
     }
}



