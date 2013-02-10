package org.esgf.accounts;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

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

import esg.security.attr.service.api.FederatedAttributeService;
import esg.security.attr.service.impl.FederatedAttributeServiceImpl;
import esg.security.registry.service.api.RegistryService;
import esg.security.registry.service.impl.RegistryServiceLocalXmlImpl;

import esg.node.security.UserInfo;
import esg.node.security.UserInfoCredentialedDAO;
import esg.node.security.GroupRoleDAO;

/**
 * Implementation of this controller will return a list of all the groups on this.node
 * The controller searches node records, and then returns a list of groups
 * @author Matthew Harris 
 */
@Controller
@RequestMapping("/showallgroupsproxy")
public class GetAllGroupsController {
   
    private final static Logger LOG = Logger.getLogger(GetAllGroupsController.class);
    
    private final static boolean debugFlag = true;

    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
    private GroupRoleDAO myGroupRoleDAO;
        
    public GetAllGroupsController() throws FileNotFoundException, IOException {
        /*
        if(Utils.environmentSwitch) {
            // try to set up myUserInfoDAO here.
            ESGFProperties myESGFProperties = new ESGFProperties();
            this.passwd = myESGFProperties.getAdminPassword();        
            this.myUserInfoDAO = new UserInfoCredentialedDAO(root,passwd,myESGFProperties);
            this.myGroupRoleDAO = new GroupRoleDAO(myESGFProperties);
        }
        */
        LOG.debug("IN GetAllGroupsController Constructor");
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
        LOG.debug("GetAllGroupsController doGet");

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
    @SuppressWarnings("deprecation")
    @RequestMapping(method=RequestMethod.POST)
    public @ResponseBody String doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, ParserConfigurationException, JDOMException, Exception{
        String query = (String)request.getParameter("query");
        String userName = "";
        boolean pass = true;
        String errormessage = "";
        JSONObject jsonObj = null;
        
        try {
            jsonObj = new JSONObject(query);
            userName = jsonObj.getString("userName");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            errormessage = "error in parsing the json text string :" + query;
            pass = false;
        }
        
        /* Returning only Groups from this.node */
          /*
            String groups = "";
            List<String[]> results = myGroupRoleDAO.getGroupEntriesNotFor(myUserInfoDAO.getUserById(userName).getOpenid());

            for(String[] string : results){
              groups = groups + Arrays.toString(string);
            }
            groups = groups + "[0, 1, 2, 3, 4]";
            LOG.debug("GetAllGroupsController -->" + userName);
          */

        /* Wish List */ 
          //List<String[]> resultsFederation = myGroupRoleDAO.getFederationGroupEntriesNotFor(myUserInfoDAO.getUserById(userName).getOpenid());
          
        /* Start of Luca's Test code */ 
          /*
            // set certificates for client-server handshake
            //CertUtils.setTruststore("/Users/cinquini/myApplications/apache-tomcat/esg-truststore.ts");
            //CertUtils.setKeystore("/Users/cinquini/myApplications/apache-tomcat/esg-datanode-rapidssl.ks");
          
            // initialize service
            final String ESGF_ATS = "/esg/config/esgf_ats.xml";
            final RegistryService registryService = new RegistryServiceLocalXmlImpl(ESGF_ATS);
            final String issuer = "ESGF Attribute Service";
            final FederatedAttributeService self = new FederatedAttributeServiceImpl(issuer, registryService);
                  
            // execute invocation
            String identifier = "https://esg-datanode.jpl.nasa.gov/esgf-idp/openid/lucacinquini";
            Map<String, Set<String>> attributes = self.getAttributes(identifier);
            for (String atype : attributes.keySet()) {
              for (String avalue : attributes.get(atype)) {
                  System.out.println("ATTRIBUTE TYPE="+atype+" VALUE="+avalue);
              }
            }
          */
        
          /* Matthews Code */
          String fileNameStatic = System.getenv().get("ESGF_HOME")+"/config/esgf_ats_static.xml";
          String fileNameDynamic = System.getenv().get("ESGF_HOME")+"/config/esgf_ats.xml";
          ArrayList<String> fileStatic = new ArrayList<String>();
          ArrayList<String> fileDynamic = new ArrayList<String>();
          String strFile = "";
          String newLine[];
          String strLine = "";
          String tmp = "";
        
          try{
            FileInputStream fstream = new FileInputStream(fileNameStatic);
            DataInputStream in = new DataInputStream(fstream);
            while ((strLine = in.readLine()) != null ){
              strLine = strLine.trim();
              if(strLine.length() == 0){
                continue;
              }
              else if(strLine.charAt(0) == '<' && strLine.charAt(strLine.length() - 1) == '>'){
                String finder[] = strLine.split(" ");
                if(finder[0].toString().equals("<attribute")){
                  fileStatic.add(strLine);  
                }
              }
              else if(strLine.charAt(0) == '<'){
                tmp = strLine;
              }
              else if(strLine.charAt(strLine.length() - 1) == '>'){
                tmp = tmp + strLine;
                tmp = tmp.replace("\n", " ");
                String finder[] = tmp.split(" ");
                if(finder[0].toString().equals("<attribute")){
                  fileStatic.add(tmp);
                }
                tmp = "";
              }
              else{
                tmp = tmp + strLine;
              }
            }
            in.close();
            /* ******************************************************** */
            FileInputStream dstream = new FileInputStream(fileNameDynamic);
            DataInputStream get = new DataInputStream(dstream);
            while ((strLine = get.readLine()) != null)   {
              strLine = strLine.trim();
              String finder[] = strLine.split(" ");
              if(finder[0].toString().equals("<attribute")){  
                fileDynamic.add(strLine);
              }
            }
            get.close();
          }
          catch (Exception e){
            System.err.println("Error: " + e.getMessage());
            pass = false;
            errormessage = "Can't get groups at this time";
          }

          for(int e = 0; e < fileDynamic.size(); e++){
            for(int r = 0; r < fileStatic.size(); r++){
              if(fileDynamic.get(e) == fileStatic.get(r)){
                fileStatic.remove(r);
              }
            }
          }

          fileDynamic.addAll(fileStatic);
          for(int c = 0; c < fileDynamic.size(); c++){
            strLine = fileDynamic.get(c);
            newLine = strLine.split("\"");
            if(newLine.length == 9){
              String temp = "";
              for(int i = 0; i < newLine.length; i++){
                if ( i % 2 != 0){
                  temp = temp + newLine[i] + ",";
                }  
              }
              strFile = strFile  + temp + "][";
            }
          }

          String xmlOutput = "<EditOutput>";
          if(pass){
            //Returning list of all groups
            xmlOutput += "<status>success</status>";
            xmlOutput += "<comment>" + strFile + "</comment>";
            xmlOutput += "</EditOutput>";
         
          } else {
            //warning warning error warning warning
            xmlOutput += "<status>fail</status>";
            xmlOutput += "<comment>" + errormessage + "</comment>";
            xmlOutput += "</EditOutput>";
          }
          JSONObject jo = XML.toJSONObject(xmlOutput);
          String jsonContent = jo.toString();        
          return jsonContent;
    }
}



