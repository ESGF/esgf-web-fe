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
 * This controller returns all users for the admin page
 * @author Matthew Harris 
 */
@Controller
@RequestMapping("/manipulationproxy")
public class ManipulationController {
    
    private final static Logger LOG = Logger.getLogger(ManipulationController.class);
    
    private final static boolean debugFlag = false;

    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
    private UserOperationsInterface uoi; 
    
        
    public ManipulationController() {
        
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
        
        
        LOG.debug("IN ManipulationController Constructor");
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
        LOG.debug("ManipulationController doGet");

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
        String action =  "";
        String isRoot = "";
        String userName = "";
        String groupName = "";
        String roles = "";
        String returnmessage = "";
        boolean error = true;
        boolean queryError = false;
        JSONObject jsonObj = null;
        
        try {
          jsonObj = new JSONObject(query);
          action = jsonObj.getString("action");
          userName = jsonObj.getString("user");
          groupName = jsonObj.getString("group");
          roles = jsonObj.getString("roles");
            
          UserInfo userInfo = myUserInfoDAO.getUserById(userName);
          String[] role = roles.split(",");
          
          //add and edit are the same thing on the back end...
          if(action.equals("ADD") || action.equals("EDIT")){
            for(int i = 0; i < role.length; i++){
              queryError = myUserInfoDAO.addPermission(userInfo, groupName, role[i]);
              if(queryError){ 
                returnmessage += userName + " has been added to " + groupName + " with the role " + role[i] + "][";
              }
              else {
                returnmessage += userName + " already has the role " + role[i] +  " in the group " +  groupName + "][";
              }
            } 
          }
          else if(action.equals("REMOVE")){
            for(int i = 0; i < role.length; i++){
              queryError = myUserInfoDAO.deletePermission(userInfo, groupName, role[i]);
              if(queryError){ 
                returnmessage += userName + " has been removed from " + groupName + " with the role " + role[i] + "][";
              }
              else {
                returnmessage += userName + " was not in " + groupName + " with the role " + role[i] + "][";
              }
            }
          }
          else{
            error = false;
            returnmessage = "unrecognized action code.";
         }
      } 
      catch (JSONException e) {
        LOG.debug("error in parsing the json text string :" + query);
        returnmessage = "error in parsing the json text string :" + query + ".";
        error = false;
      }

      System.out.println(returnmessage);
       
      String xmlOutput = "<EditOutput>";
      if(error){
        xmlOutput += "<status>success</status>";
        xmlOutput += "<comment>" + returnmessage+ "</comment>";
      }
      else{
        xmlOutput += "<status>fail</status>";
        xmlOutput += "<comment>" + returnmessage + "</comment>";
      }
      xmlOutput += "</EditOutput>";
        
      JSONObject jo = XML.toJSONObject(xmlOutput);

      String jsonContent = jo.toString();        
      return jsonContent;
    }
}
