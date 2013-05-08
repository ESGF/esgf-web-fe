package org.esgf.adminui;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.esgf.commonui.UserOperationsESGFDBImpl;
import org.esgf.commonui.UserOperationsInterface;
import org.esgf.commonui.UserOperationsXMLImpl;
import org.esgf.commonui.Utils;
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.jdom.JDOMException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import esg.common.util.ESGFProperties;
import esg.node.security.UserInfo;
import esg.node.security.UserInfoCredentialedDAO;
/**
/**
 * This controller returns all users for the admin page
 * @author Matthew Harris 
 */
@Controller
@RequestMapping("/getallusersproxy")
public class GetAllUsersController {
    
    private final static Logger LOG = Logger.getLogger(GetAllUsersController.class);
    
    private final static boolean debugFlag = false;

    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
    private UserOperationsInterface uoi; 
    
    public static void main(String [] args) {    
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        final MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        GetAllUsersController gauc = new GetAllUsersController();
        mockRequest.addParameter("query", "rootAdmin");
        try {
            gauc.doPost(mockRequest, mockResponse);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    } 
    
    public GetAllUsersController() {
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
        LOG.debug("IN GetAllUsersController Constructor");
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
        LOG.debug("GetAllUsersController doGet");
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
        String username =  "";
        String isRoot = "";
        boolean error = false;
        String errormessage = "";        
        JSONObject jsonObj = null;
        
        try {
            jsonObj = new JSONObject(query);
            username = jsonObj.getString("userName");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            errormessage = "error in parsing the json text string :" + query;
            error = true;
        }

        // method call for openids tied to passed email
        List<User> openidList = uoi.getAllUsers();
        if(openidList == null) System.out.println("id is null");

        String ids = "";

        for(User u : openidList){
          String userName = u.getUserName();
          String lastName = u.getLastName();
          String firstName = u.getFirstName();
          String userEmail = u.getEmailAddress();
          String openId = u.getOpenId();
          ids += userName + "," + lastName + "," + firstName + "," + userEmail + "," + openId + "][";
        }

        //System.out.println(ids);
        
        //is user the root admin and not a group admin
        isRoot = Utils.getIdFromHeaderCookie(request);
        UserInfo u = myUserInfoDAO.getUserByOpenid(isRoot);
        System.out.println(u.getUserName());
        if(!u.getUserName().equals("rootAdmin")){
          errormessage = "You are not root. Only the root admin may edit users information.";
          error = true;
        }

        String xmlOutput = "<EditOutput>";
        if(error){
          xmlOutput += "<status>fail</status>";
          xmlOutput += "<comment>" + errormessage + "</comment>";
        }
        else{
          xmlOutput += "<status>success</status>";
          xmlOutput += "<comment>" + ids + "</comment>";
        
        }
          xmlOutput += "</EditOutput>";
        
        JSONObject jo = XML.toJSONObject(xmlOutput);

        String jsonContent = jo.toString();        
        return jsonContent;
    }
}