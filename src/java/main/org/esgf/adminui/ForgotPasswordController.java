package org.esgf.adminui;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
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
 * Implementation of this controller will reset the users password and email them this change
 * The controller searches user records, and resets users password
 * @author Matthew Harris 
 */
@Controller
@RequestMapping("/forgotpasswordproxy")
public class ForgotPasswordController {
    
    private final static Logger LOG = Logger.getLogger(ForgotPasswordController.class);
    
    private final static boolean debugFlag = true;

    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
    
        
    public ForgotPasswordController() throws FileNotFoundException, IOException {
        if(Utils.environmentSwitch) {
            // try to set up myUserInfoDAO here.
            ESGFProperties myESGFProperties = new ESGFProperties();
            this.passwd = myESGFProperties.getAdminPassword();        
            this.myUserInfoDAO = new UserInfoCredentialedDAO(root,passwd,myESGFProperties);
        }
        
        LOG.debug("IN ForgotPasswordController Constructor");
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
        LOG.debug("ForgotPasswordController doGet");

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
        String email =  "";
        String openid = "";
        boolean error = false;
        String errormessage = "";        
        JSONObject jsonObj = null;
        
        try {
            jsonObj = new JSONObject(query);
            email = jsonObj.getString("email");
            openid = jsonObj.getString("openid");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            errormessage = "error in parsing the json text string :" + query;
            error = true;
        }

        // get userInfo for username
        LOG.debug("ForgotPasswordController -->" + openid);
        try {
            UserInfo userInfo = myUserInfoDAO.getUserById(openid);
            // create new password for openid
            String password = "password";
            // method call for changing a users password
            if (myUserInfoDAO.setPassword(userInfo, password)) {
              error = false;
            } else {
              error = true;
              LOG.debug("Error happened in changing the user password.");
            }
        } catch(Exception e) {
            error = true;
            LOG.debug("Error in getGroupsFromUser");
        }
        
        String xmlOutput = "<EditOutput>";
        if(error){
          //password did not set
          xmlOutput += "<status>fail</status>";
          xmlOutput += "<comment>" + error + "</comment>";
          xmlOutput += "</EditOutput>";

        } else {
          //password has reset 
          //email user their new password
          xmlOutput += "<status>success</status>";
          xmlOutput += "<comment>" + "</comment>";
          xmlOutput += "</EditOutput>";

        }
                
        JSONObject jo = XML.toJSONObject(xmlOutput);

        String jsonContent = jo.toString();        
        return jsonContent;

    }
        
}



