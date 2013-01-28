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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
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
 * Implementation of this controller will add a user to the group and roll
 * The controller searches user records, and then adds them to a group
 * @author Matthew Harris 
 */
@Controller
@RequestMapping("/addgroupproxy")
public class RegisterForGroupsController {
   
    private final static Logger LOG = Logger.getLogger(RegisterForGroupsController.class);
    
    private final static boolean debugFlag = true;

    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
        
    public RegisterForGroupsController() throws FileNotFoundException, IOException {
        if(Utils.environmentSwitch) {
            // try to set up myUserInfoDAO here.
            ESGFProperties myESGFProperties = new ESGFProperties();
            this.passwd = myESGFProperties.getAdminPassword();        
            this.myUserInfoDAO = new UserInfoCredentialedDAO(root,passwd,myESGFProperties);
        }
        
        LOG.debug("IN RegisterForGroupsController Constructor");
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
        LOG.debug("RegisterForGroupsController doGet");

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
        String group = "";
        String regURL = "";
        String attURL = "";

        boolean pass = true;
        String retunedStatus = "";
        String retunedMessage = "";        
        JSONObject jsonObj = null;
        
        try {
            jsonObj = new JSONObject(query);
            userName = jsonObj.getString("userName");
            group = jsonObj.getString("group");
            regURL = jsonObj.getString("reg");
            attURL = jsonObj.getString("att");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            retunedMessage = "error in parsing the json text string :" + query;
            pass = false;
        }        
        
        LOG.debug("RegisterForGroupsController -->" + userName);
        
        String startOF = "<esgf:registrationRequest xmlns:esgf=\"http://www.esgf.org/\">";
        String userStart = "<esgf:user>";
        String userEnd = "</esgf:user>";
        String groupStart = "<esgf:group>";
        String groupEnd = "</esgf:group>";
        String role = "<esgf:role>user</esgf:role>";
        String endOF = "</esgf:registrationRequest>";

        String xmlString = startOF + userStart + userName + userEnd + groupStart + group + groupEnd + role + endOF;
        
        PostMethod post = new PostMethod(regURL);
        StringRequestEntity xml = new StringRequestEntity(xmlString, "application/xml", "UTF-8");
        post.setRequestEntity(xml);
        post.setParameter("xml", xmlString);
        HttpClient httpclient = new HttpClient();
      
        try{
            System.out.println("About to call: " + regURL + "\n");
            int result = httpclient.executeMethod(post);
            /* status code: 200 -> ok; 500 -> internal server error */
            /* <esgf:registrationResponse xmlns:esgf="http://www.esgf.org/" >
             * <esgf:result >
             * SUCCESS</esgf:result >
             * <esgf:message >
             * Registration outcome: user=https://patrick.llnl.gov/esgf-idp/openid/frank, group=cmip5research, role=user</esgf:message >
             * </esgf:registrationResponse > 
             * \n
            */
            if(result == 200){
                String retunedXML = post.getResponseBodyAsString();
                String parseXML[] = retunedXML.split(">");
                String parseOut[] = parseXML[2].split("<");
                //TODO -> what are all the results?
                System.out.println(parseOut[0]);
                if(parseOut[0].equals("SUCCESS")){
                  pass = true;
                  retunedMessage = "success[]You have been added to the group " + group + ".";
                }
                else if(parseOut[0].equals("EXISTING")){
                  pass = true;
                  retunedMessage = "existing[]You are already a member of the group " + group + ".";
                  
                }
                else if(parseOut[0].equals("PENDING")){
                  pass = true;
                  retunedMessage = "pending[]We have contacted the System Admin for " + group + ". Your membership is pending, you will recieve a email once your request has been reviewed."; 
                }
                else{
                  pass = false;  
                  retunedMessage = "You are unbale to be added to the group " + group + " at this time.";
                }
            }
            else{
              pass = false;  
              retunedMessage = "Could not conntact host node for the group " + group + "\n Please try again at a later time.\nStatus Code: " + result;                
            }
        }
        finally{
            post.releaseConnection();
        }
        
        /* for local node group registration only */
        /*  pass = myUserInfoDAO.addPermission(userName, group, role);
            retunedMessage = "You are already a member of this group.";

            else federation group
            pass = myUserInfoDAO.addPermissionFederation(userName, group, role, where);
        */
        String xmlOutput = "<EditOutput>";
        if(pass){
          xmlOutput += "<status>success</status>";
          xmlOutput += "<comment>" + retunedMessage + "</comment>";
        }
        else{
          xmlOutput += "<status>fail</status>";
          xmlOutput += "<comment>" + retunedMessage + "</comment>";
        }
        xmlOutput += "</EditOutput>";
        JSONObject jo = XML.toJSONObject(xmlOutput);
        String jsonContent = jo.toString();        
        return jsonContent;
    }
}



