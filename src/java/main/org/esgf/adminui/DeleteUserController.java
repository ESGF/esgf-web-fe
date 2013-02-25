package org.esgf.adminui;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

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
 * This controller deletes a users users for the admin page
 * @author Matthew Harris 
 */
@Controller
@RequestMapping("/deleteuserproxy")
public class DeleteUserController {
    
    private final static Logger LOG = Logger.getLogger(DeleteUserController.class);
    
    private final static boolean debugFlag = false;

    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
    private UserOperationsInterface uoi; 
    
        
    public DeleteUserController() {
        
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
        
        
        LOG.debug("IN DeleteUserController Constructor");
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
        LOG.debug("DeleteUserController doGet");

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
        JSONObject jsonObj = null;
        String query = (String)request.getParameter("query");
        String userName =  "";
        String errormessage = "";        
        String info ="";
        boolean error = true;
        boolean pass = true;
        UserInfo userInfo = null;
        Map<String,Set<String>> userGroupsAndRoles = null; 
        try {
            jsonObj = new JSONObject(query);
            userName = jsonObj.getString("userName");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            errormessage = "error in parsing the json text string :" + query;
            error = false;
        }

        //remove from all groups
        try{
          userInfo = myUserInfoDAO.getUserById(userName);
          userGroupsAndRoles = uoi.getUserPermissionsFromOpenID(userInfo.getOpenid());
        
          for (Object key : userGroupsAndRoles.keySet()) {
            String name = key.toString();
            for(int parts = 0; parts < userGroupsAndRoles.get(key).size(); parts++){
              Iterator<String> myit = userGroupsAndRoles.get(key).iterator();
              while(myit.hasNext()){
                String tmproles = myit.next();
                pass = myUserInfoDAO.deletePermission(userInfo, name , tmproles);
              }                    
            }
          }
        }
       catch(Exception e){
         error = false;
         errormessage = "failing here : " + e;
         System.out.println("exception cought : "+e);
       } 
        
        //delete user
        error = myUserInfoDAO.deleteUser(userInfo);
        if(!error){
          errormessage = "Failed to delete User";
        }
        else{
          info = "The user account for " + userName + " has been deleted.";
        }

        //TODO: should let other nodes this user is deleted incase they where registered in a group on another node

        String xmlOutput = "<EditOutput>";
        if(error){
          xmlOutput += "<status>success</status>";
          xmlOutput += "<comment>" + info + "</comment>";
        }
        else{
          xmlOutput += "<status>fail</status>";
          xmlOutput += "<comment>" + errormessage + "</comment>";
        
        }
          xmlOutput += "</EditOutput>";
        
        JSONObject jo = XML.toJSONObject(xmlOutput);

        String jsonContent = jo.toString();        
        return jsonContent;

    }
        
}


