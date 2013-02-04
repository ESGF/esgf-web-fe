package org.esgf.adminui;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

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
 * Implementation of edit userinfo controller responsible for edit user info, like password, city, state, country information
 * The controller searches user record, and call DAO object to persist the changes into database
 *
 * @author amy huang
 *
 */
@Controller
@RequestMapping("/edituserinfoproxy")
public class EditUserInfoController {
    
    private final static Logger LOG = Logger.getLogger(EditUserInfoController.class);
    
    private final static boolean debugFlag = true;

    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
    
        
    public EditUserInfoController() throws FileNotFoundException, IOException {
        if(Utils.environmentSwitch) {
            
            // try to set up myUserInfoDAO here.
            ESGFProperties myESGFProperties = new ESGFProperties();
            this.passwd = myESGFProperties.getAdminPassword();        
            this.myUserInfoDAO = new UserInfoCredentialedDAO(root,passwd,myESGFProperties);
            
        }
        
        LOG.debug("IN EditUserInfoController Constructor");
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
        LOG.debug("EditUserInfoController doGet");

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
        String username = "";
        String type = "";
        String newpasswd = "";
        String verifypasswd = "";
        String oldpasswd = "";
        boolean error = false;
        String errormessage = "";        
        JSONObject jsonObj = null;
        
        try {
            jsonObj = new JSONObject(query);
            username = jsonObj.getString("userName");
            type = jsonObj.getString("type");
            newpasswd = jsonObj.getString("newpasswd");
            verifypasswd = jsonObj.getString("verifypasswd");
            oldpasswd = jsonObj.getString("oldpasswd");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            errormessage = "error in parsing the json text string :" + query;
            error = true;
        }
        

        
        if (type.equals("editUserInfo")) {
            error = processUserPasswordChange(username, oldpasswd, newpasswd, verifypasswd);
            if (error) {
                errormessage = "Error happened in changing the user password";
            }
        } else {
            error = true;
            errormessage = "Wrong type of action, the current type is " + type;
            LOG.debug("Wrong type of action, the current type is " + type);
        }
        
        // wrap into json string return
        String xmlOutput = "<EditOutput>";
        
        if (!error) { 
            xmlOutput += "<status>success</status>";
            xmlOutput += "<comment></comment>";
        } else {
            xmlOutput += "<status>fail</status>";
            xmlOutput = "<comment>" + errormessage + "</comment>";
        }
        
        xmlOutput += "</EditOutput>";
        
        JSONObject jo = XML.toJSONObject(xmlOutput);

        String jsonContent = jo.toString();        
        return jsonContent;
    }
    
    /**
     * @param  request  HttpServletRequest object containing the query string
     * @param  response  HttpServletResponse object containing the ExtractGroupInfoControllermetadata in json format
     * @throws JDOMException 
     *
     */
    private boolean processUserPasswordChange(String username, String oldpasswd, String newpasswd, String verifypasswd) {
        LOG.debug("EditUserInfoController -->" + username);
        boolean error = false;
        try {
            UserInfo userInfo = myUserInfoDAO.getUserById(username);
            if (newpasswd.equals(verifypasswd)) {
                if (myUserInfoDAO.changePassword(userInfo, oldpasswd, newpasswd)) {
                    error = false;
                } else {
                    error = true;
                    LOG.debug("Error happened in changing the user password.");
                }
            } else {
                error = true;
                LOG.debug("Error happened in changing the user password, the passwords don't match.");
            }
        } catch(Exception e) {
            error = true;
            LOG.debug("Error in getGroupsFromUser");
        }
        return error;
    }
        
}


