package org.esgf.adminui;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Enumeration;
import java.util.List;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
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
        /*
        if(Utils.environmentSwitch) {
            // try to set up myUserInfoDAO here.
            ESGFProperties myESGFProperties = new ESGFProperties();
            this.passwd = myESGFProperties.getAdminPassword();        
            this.myUserInfoDAO = new UserInfoCredentialedDAO(root,passwd,myESGFProperties);
        }
        */
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
        String openid = "";
        String password = "";
        String email = "";
        boolean error = false;
        String errormessage = "";        
        JSONObject jsonObj = null;
        
        try {
            jsonObj = new JSONObject(query);
            openid = jsonObj.getString("openid");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            errormessage = "error in parsing the json text string :" + query;
            error = true;
        }

        // get userInfo for username and email
        LOG.debug("ForgotPasswordController -->" + openid);
        try {
            UserInfo userInfo = myUserInfoDAO.getUserById(openid);
            email = userInfo.getEmail();

            // create new password for openid
            String ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            int len = 8;
            StringBuffer sb = new StringBuffer(len);
            for (int i = 0; i < len; i++) {
              int ndx = (int) (Math.random() * ALPHA_NUM.length());
              sb.append(ALPHA_NUM.charAt(ndx));
            } 
            password = sb.toString();

            // method call for setting a users password
            if (myUserInfoDAO.setPassword(userInfo, password)) {
              error = false;
              String to = email;
              String from = "esgf-user@lists.llnl.gov";
              ESGFProperties myESGFProperties = null;
              myESGFProperties = new ESGFProperties();

              Properties properties = myESGFProperties;
              Session session = Session.getDefaultInstance(properties);

              try{
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject("New ESGF Password");
                message.setText("Dear user " + openid + ",\n" 
                                + "Someone, hopefully you, has requested a new password for the Earth System Grid Federation (ESGF) linked to your email address.\n" 
                                + "Your new password is:\n"
                                + password
                                + "\n Please remember to change this password next time you log in, to something you will remember.\n"
                                + "Sincerely Earth System Grid Federation");
                Transport.send(message);
              } catch (MessagingException mex) {
                mex.printStackTrace();
              }  
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
          xmlOutput += "<comment>A new password has been sent to the email address linked to this openid.</comment>";
          xmlOutput += "</EditOutput>";

        }
        JSONObject jo = XML.toJSONObject(xmlOutput);
        String jsonContent = jo.toString();        
        return jsonContent;
    }
}



