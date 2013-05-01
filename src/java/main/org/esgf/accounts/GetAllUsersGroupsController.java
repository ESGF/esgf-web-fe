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
import java.util.HashMap;
import java.util.Iterator;

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
import org.springframework.beans.factory.annotation.Autowired;
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
import esg.node.security.GroupRoleCredentialedDAO;

/**
 * Implementation of this controller will return a list of all the groups on this.node
 * The controller searches node records, and then returns a list of groups
 * @author Matthew Harris 
 */
@Controller
@RequestMapping("/showallusersgroupsproxy")
public class GetAllUsersGroupsController {
   
    private final static Logger LOG = Logger.getLogger(GetAllUsersGroupsController.class);
    
    private final static boolean debugFlag = true;

    private String passwd;
    private String root = "rootAdmin";     
    private UserInfoCredentialedDAO myUserInfoDAO;
    private GroupRoleCredentialedDAO myGroupRoleDAO;
    private FederatedAttributeService fas;
    private ESGFProperties myESGFProperties; 
    @Autowired 
    public GetAllUsersGroupsController(FederatedAttributeService fas) throws FileNotFoundException, IOException {
        this.fas = fas;

        if(Utils.environmentSwitch) {
            try {
             // try to set up myUserInfoDAO here.
                myESGFProperties = new ESGFProperties();
                this.passwd = myESGFProperties.getAdminPassword();        
                this.myUserInfoDAO = new UserInfoCredentialedDAO(root,passwd,myESGFProperties);
                
            } catch(Exception e) {
                e.printStackTrace();
            }
            
        LOG.debug("IN GetAllUsersGroupsController Constructor");
      }
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
        LOG.debug("GetAllUsersGroupsController doGet");
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
        String  openId= "";
        boolean pass = true;
        String errormessage = "";
        JSONObject jsonObj = null;
        
        try {
            jsonObj = new JSONObject(query);
            openId = jsonObj.getString("openId");
        } catch (JSONException e) {
            LOG.debug("error in parsing the json text string :" + query);
            errormessage = "error in parsing the json text string :" + query;
            System.err.println(errormessage);
            pass = false;
        }
        
          //Getting List of all group names to extract group desc
              String fileNameStatic = System.getenv().get("ESGF_HOME")+"/config/esgf_ats_static.xml"; //File is messy, created by humans.
              String fileNameDynamic = System.getenv().get("ESGF_HOME")+"/config/esgf_ats.xml";       //File is clean, created by machine.
              ArrayList<String> fileStatic = new ArrayList<String>();
              ArrayList<String> fileDynamic = new ArrayList<String>();
              String strLine = "";
              String tmp = "";
              List<String> myroles = new ArrayList<String>();
              String returnString ="";
              
              try{
                //get all groups and info out of dynamic file
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
                //get all grops and info out of static file

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
                //merge both sets of groups and info
                for(int e = 0; e < fileDynamic.size(); e++){
                  String dynamicLength[] = fileDynamic.get(e).split("\"");
                  if(dynamicLength.length != 9){
                    fileDynamic.remove(e);
                  }
                  else{
                    for(int r = 0; r < fileStatic.size(); r++){
                      String staticLength[] = fileStatic.get(r).split("\"");
                      if(fileDynamic.get(e) == fileStatic.get(r)){
                        fileStatic.remove(r);
                      }
                      else if(staticLength.length != 9){
                        fileStatic.remove(r);
                      }
                    }
                  }
                }
                fileDynamic.addAll(fileStatic);
              }
              catch (Exception e){
                System.err.println("Error: " + e.getMessage());
              }

            //get map of all groups and info this.user is in
            try {
              Map<String,Set<String>> userGroupsAndRoles = fas.getAttributes(openId);
                
              for (Object key : userGroupsAndRoles.keySet()) {
                String gn = key.toString();

	            }
              //Putting the two together for delivery
              for (Object key : userGroupsAndRoles.keySet()) {
                String name = key.toString();
                for(int count = 0; count < fileDynamic.size(); count++){
                  String spliter[] = fileDynamic.get(count).split("\"");
                  if(spliter[1].equals(name)){ //yes user is in this group
                    returnString += "[" + spliter[1] + "," + spliter[5] + ",";
                    for(int parts = 0; parts < userGroupsAndRoles.get(key).size(); parts++){
                        String tmproles = "";
                        Iterator<String> myit = userGroupsAndRoles.get(key).iterator();
                        while(myit.hasNext()){
                            tmproles += myit.next() + " ";
                        }
                        //myroles.add(tmproles);
                        returnString += tmproles + "]";
                    }
                  }
                }
              }
            } catch (Exception e) {
              e.printStackTrace();
              System.err.println("Error: " + e.getMessage());
            }
            
          String xmlOutput = "<EditOutput>";
          if(pass){
            //Returning list of all groups
            xmlOutput += "<status>success</status>";
            xmlOutput += "<comment>" + returnString + "</comment>";
         
          } else {
            //warning warning error warning warning
            xmlOutput += "<status>fail</status>";
            xmlOutput += "<comment>" + errormessage + "</comment>";
          }
          xmlOutput += "</EditOutput>";
          JSONObject jo = XML.toJSONObject(xmlOutput);
          String jsonContent = jo.toString();        
          return jsonContent;
    }
}



