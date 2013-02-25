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
/**
 * This controller returns a users info for the admin page
 * @author Matthew Harris 
 */
@Controller
@RequestMapping("/getusersinfoproxy")
public class GetUsersInfoController {
    
    private final static Logger LOG = Logger.getLogger(GetUsersInfoController.class);
    
    private final static boolean debugFlag = false;

    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
    private UserOperationsInterface uoi; 
    
        
    public GetUsersInfoController() {
        
        try {
            if(Utils.environmentSwitch) {
                // try to set up myUserInfoDAO here.
                ESGFProperties myESGFProperties = new ESGFProperties();
                this.passwd = myESGFProperties.getAdminPassword();        
                this.myUserInfoDAO = new UserInfoCredentialedDAO(root,passwd,myESGFProperties);
                //declare a UserOperations "Object"
                if(Utils.environmentSwitch) {
                  uoi = new UserOperationsESGFDBImpl();
                }
                else {
                  uoi = new UserOperationsXMLImpl();
                }
            }
        } 
        catch(Exception e) {
            e.printStackTrace();
        }
        
        
        LOG.debug("IN GetUsersInfoController Constructor");
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
        LOG.debug("GetUsersInfoController doGet");

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
        String openId =  "";
        boolean error = false;
        String errormessage = "";        
        JSONObject jsonObj = null;
        
        try {
            jsonObj = new JSONObject(query);
            openId = jsonObj.getString("openId");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            errormessage = "error in parsing the json text string :" + query;
            error = true;
        }

        // method call for openids tied to passed email
        //User u = uoi.getUserObjectFromUserOpenID(openId);
        UserInfo u = myUserInfoDAO.getUserByOpenid(openId);
        System.out.println(u + " " + openId);

        String info = "";
        if(u != null){
          String userName = u.getUserName();
          String lastName = u.getLastName();
          String middleName = u.getMiddleName();
          String firstName = u.getFirstName();
          String userEmail = u.getEmail();
          String userOrg = u.getOrganization();
          String userCity = u.getCity();
          String userState = u.getState();
          String userCountry = u.getCountry();
          info = userName + "," + lastName + "," + middleName + "," + firstName + "," + userEmail + "," + userOrg + "," + userCity + "," + userState + "," + userCountry + "," + openId;
        }
        else{
          errormessage = "No such user";
          error = true;
        }

        String xmlOutput = "<EditOutput>";
        if(error){
          xmlOutput += "<status>fail</status>";
          xmlOutput += "<comment>" + errormessage + "</comment>";
        }
        else{
          xmlOutput += "<status>success</status>";
          xmlOutput += "<comment>" + info + "</comment>";
        
        }
          xmlOutput += "</EditOutput>";
        
        JSONObject jo = XML.toJSONObject(xmlOutput);

        String jsonContent = jo.toString();        
        return jsonContent;

    }
        
}


