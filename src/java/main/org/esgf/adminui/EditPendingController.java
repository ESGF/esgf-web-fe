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
import esg.node.security.GroupRoleDAO;
import esg.node.security.GroupRoleCredentialedDAO;
/**
 * This Controller will update a users request to join a group
 *
 * @author Matthew Harris
 *
 */
@Controller
@RequestMapping("/updatependingproxy")
public class EditPendingController {
    
    private final static Logger LOG = Logger.getLogger(EditPendingController.class);
    
    private final static boolean debugFlag = true;

    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
    private GroupRoleCredentialedDAO myGroupRoleDAO;
    private String openId;
    private UserInfo adminInfo;
    private ESGFProperties myESGFProperties; 
    
    public EditPendingController() {
        
        if(Utils.environmentSwitch) {
            try {
             // try to set up myUserInfoDAO here.
                myESGFProperties = new ESGFProperties();
                this.passwd = myESGFProperties.getAdminPassword();        
                this.myUserInfoDAO = new UserInfoCredentialedDAO(root,passwd,myESGFProperties);

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        LOG.debug("IN EditPendingController Constructor");
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
        LOG.debug("EditPendingController doGet");
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
        String groupName = "";
        String groupId = "";
        String userId = "";
        String choice = "";
        
        int gid = 0;
        int uid = 0;

        boolean error = false;
        String errormessage = "";        
        JSONObject jsonObj = null;
        
        try {
            jsonObj = new JSONObject(query);
            groupName = jsonObj.getString("groupName");
            groupId = jsonObj.getString("groupId");
            userId =  jsonObj.getString("userId");
            choice = jsonObj.getString("choice");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            errormessage = "error in parsing the json text string :" + query;
            error = true;
        }

        try{
          openId = Utils.getIdFromHeaderCookie(request);
          adminInfo = myUserInfoDAO.getUserById(openId);
          this.myGroupRoleDAO = new GroupRoleCredentialedDAO(adminInfo,myESGFProperties);

          gid = Integer.parseInt(groupId);
          uid = Integer.parseInt(userId);
          
          if(choice.equals("approved")){
            error = myGroupRoleDAO.setApproved(uid, gid);//call approve
            System.out.println("the returned bool = " + error);
            if(error) errormessage = "Error approving request";
          }
          else if(choice.equals("reject")){
            error = myGroupRoleDAO.setReject(uid, gid);//cal delete
            System.out.println("the returned bool = " + error);
            if(error) errormessage = "Error rejecting request";
          }
          else{
            error = false;
            errormessage = "Not a vaild option";
          }
        }
        catch(Exception e){
          error = false;
          errormessage = "Could not update at this time: " + e;
        }

        //TODO:email service to inform user

        String xmlOutput = "<EditOutput>";
        
        if (error) {
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
}


