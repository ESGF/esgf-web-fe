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
import esg.node.security.GroupRoleCredentialedDAO;
/**
/**
 * This controller deletes a group for the admin page
 * @author Matthew Harris 
 */
@Controller
@RequestMapping("/deletegroupproxy")
public class DeleteGroupController {
    
    private final static Logger LOG = Logger.getLogger(DeleteGroupController.class);
    
    private final static boolean debugFlag = false;

    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
    private GroupRoleCredentialedDAO myGroupRoleDAO;
    private UserInfo adminInfo;
    private UserInfo userInfo;
    private UserOperationsInterface uoi;
    private ESGFProperties myESGFProperties;
    private String openId; 
    
        
    public DeleteGroupController() {
        
        try {
            if(Utils.environmentSwitch) {
                // try to set up myUserInfoDAO here.
                this.myESGFProperties = new ESGFProperties();
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
        
        
        LOG.debug("IN DeleteGroupController Constructor");
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
        LOG.debug("DeleteGroupController doGet");

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
        String groupName =  "";
        String errormessage = "";        
        String info = "";
        String openId = "";
        boolean error = true;
        boolean pass = true;
        boolean check = true;
        Map<String,Set<String>> userGroupsAndRoles = null; 
        try {
            jsonObj = new JSONObject(query);
            groupName = jsonObj.getString("groupName");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            errormessage = "error in parsing the json text string :" + query;
            error = false;
        }

        try{
          openId = Utils.getIdFromHeaderCookie(request);
          adminInfo = myUserInfoDAO.getUserById(openId);
          this.myGroupRoleDAO = new GroupRoleCredentialedDAO(adminInfo,myESGFProperties);

          //remove all members from group
          List<User> users = uoi.getUsersFromGroup(groupName);
          for(User set : users){
            userInfo = myUserInfoDAO.getUserById(set.getOpenId());
            pass = myUserInfoDAO.deleteGroupFromUserPermissions(userInfo, groupName);
            if(!pass){
              System.out.println("Error removing " + set + " From " + ".");
            }
          }

          //delete group
          error = myGroupRoleDAO.deleteGroup(groupName);
          System.out.println(error);
          if(!error){
            errormessage = "Failed to delete User";
          }
          else{
            info = "The group " + groupName + " has been deleted.";
          }
       }
       catch(Exception e){
         error = false;
         errormessage = "failed with this exeception: " + e;
         System.out.println("exception cought : "+e);
       } 
        //TODO: remove group from file, let other nodes in federation know group is gone

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


