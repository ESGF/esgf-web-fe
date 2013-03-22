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
 * This Controller will update a groups information name, desc, vis, and auto
 *
 * @author Matthew Harris
 *
 */
@Controller
@RequestMapping("/updategroupinfoproxy")
public class EditGroupController {
    
    private final static Logger LOG = Logger.getLogger(EditGroupController.class);
    
    private final static boolean debugFlag = true;

    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
    private GroupRoleCredentialedDAO myGroupRoleDAO;
    private ESGFProperties myESGFProperties; 
    
    public EditGroupController() {
        System.out.println("enviSwitch: " + Utils.environmentSwitch);
        
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
        LOG.debug("IN EditGroupController Constructor");
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
        LOG.debug("EditGroupController doGet");
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
        UserInfo userInfo;
        UserInfo adminInfo;
        
        String openId;
        String query = (String)request.getParameter("query");
        String name = "";
        String description = "";
        String visable = "";
        String approval = "";
        String groupId = "";
        int id = -1;
        boolean error = false;
        boolean vis = false;
        boolean auto = false;
        String errormessage = "";        
        JSONObject jsonObj = null;
        
        try {
            jsonObj = new JSONObject(query);
            groupId = jsonObj.getString("groupId");
            name= jsonObj.getString("groupName");
            description = jsonObj.getString("groupDesc");
            visable = jsonObj.getString("groupVis");
            approval = jsonObj.getString("groupAuto");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            errormessage = "error in parsing the json text string :" + query;
            error = true;
        }

        try{
          openId = Utils.getIdFromHeaderCookie(request);
          adminInfo = myUserInfoDAO.getUserById(openId);
          this.myGroupRoleDAO = new GroupRoleCredentialedDAO(adminInfo,myESGFProperties);
          
          if(visable.equals("t")){
            vis = true;
          }
          if(approval.equals("t")){
            auto = true;
          }
          id = Integer.parseInt(groupId);
          error = myGroupRoleDAO.updateWholeGroup(id, name, description, vis, auto);
          System.out.println(error);
        }
        catch(Exception e){
          error = false;
          errormessage = "Could not update " + name + " at this time. " + e;
        }

        // wrap into json string return
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


