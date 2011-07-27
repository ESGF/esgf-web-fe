package org.esgf.commonui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esgf.adminui.Group;
import org.esgf.adminui.User;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class UserOperations {

    private final static Logger LOG = Logger.getLogger(UserOperations.class);

    private static boolean writeLogFlag = false;
    private static boolean writeXMLTOLogFlag = false;
    private static boolean editLogFlag = false;
    private static boolean editXMLTOLogFlag = false;
    private static boolean deleteLogFlag = false;
    private static boolean deleteXMLTOLogFlag = false;
    private static boolean readLogFlag = false;
    private static boolean readXMLTOLogFlag = false;
    
    private final static String USERS_FILE = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe\\src\\java\\main\\db.users";
    
    
    
    
    
    /* Adding user info */
    public static void writeUserInfoToXML(final HttpServletRequest request, File file) {
        
        LOG.debug("------UserOperations WriteUserInfoTOXML------");
        
        String userName = request.getParameter("userName");
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String emailAddress = request.getParameter("emailAddress");
        String status = request.getParameter("status");
        
        /* this logic is deprecated and only used for testing - it utilizes the xml in users.store */
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
        Utils.queryStringInfo(request);
        
        try{

            Document document = (Document) builder.build(file);
            if(writeLogFlag)
                LOG.debug("Building document");
            
            Element rootNode = document.getRootElement();
            if(writeLogFlag)
                LOG.debug("root name: " + rootNode.getName());
            
            Element userElement = new Element("user");
            
            Enumeration<String> paramEnum = request.getParameterNames();
            
            Element lastNameEl = new Element("lastName");
            if(lastName != null && lastName != "") {
                lastNameEl.addContent(lastName);
            } 
            else {
                lastNameEl.addContent("N/A");
            }
            
            Element firstNameEl = new Element("firstName");
            if(firstName != null && firstName != "") {
                firstNameEl.addContent(firstName);
            } 
            else {
                firstNameEl.addContent("N/A");
            }
            
            Element userNameEl = new Element("userName");
            if(userName != null && userName != "") {
                userNameEl.addContent(userName);
            } 
            else {
                userNameEl.addContent("N/A");
            }
            
            Element emailEl = new Element("emailAddress");
            if(emailAddress != null && emailAddress != "") {
                emailEl.addContent(emailAddress);
            } 
            else {
                emailEl.addContent("N/A");
            }
            
            Element statusEl = new Element("status");
            
            if(status != null && status != "") {
                statusEl.addContent(status);
            } 
            else {
                statusEl.addContent("N/A");
            }
            
           
            
            userElement.addContent(lastNameEl);
            userElement.addContent(firstNameEl);
            userElement.addContent(userNameEl);
            userElement.addContent(emailEl);
            userElement.addContent(statusEl);
            
            ///Debugging for groups
            // MUST TAKE THIS OUT FOR PRODUCTION
            //
            Element groupsEl = new Element("groups");
            Element groupEl = new Element("group");
            Element groupNameEl = new Element("name");
            groupNameEl.setText("group1");
            groupEl.addContent(groupNameEl);
            groupsEl.addContent(groupEl);
            userElement.addContent(groupsEl);
            //
             // End debugging group
            //
            //Insert real code here^^^  
            
            rootNode.addContent(userElement);
            
            document.setContent(rootNode);
            
            XMLOutputter outputter = new XMLOutputter();
            xmlContent = outputter.outputString(rootNode);
            
            
            if(writeLogFlag) {
                if(writeXMLTOLogFlag) {
                    LOG.debug("NEW XMLCONTENT \n" + xmlContent);
                }
            }
            
            
            Writer output = null;
            output = new BufferedWriter(new FileWriter(file));
            output.write(xmlContent);
            
            if(writeLogFlag)
                LOG.debug("Writing to file");
        
            output.close();
            
            
        }catch(Exception e) {
            LOG.debug("Couldn't write new xml to file");
        }
        LOG.debug("------End UserOperations WriteUserInfoTOXML------");
        
    }


    public static Element getUserInfoFromUserNameXML(String userName, File file) throws JDOMException, IOException {
        LOG.debug("------UserOperations getUserInfoFromUserNameXML------");

        Element returnedEl = null;
        
        /*
        //final File file = new File(USERS_FILE);
        SAXBuilder builder = new SAXBuilder();
        
        Document document = (Document) builder.build(file);
        
        Element rootNode = document.getRootElement();
        
        if(debugFlag)   
            LOG.debug("root name: " + rootNode.getName());
        
        
        List users = (List)rootNode.getChildren();
        for(int i=0;i<users.size();i++)
        {
            Element userEl = (Element) users.get(i);
            Element userNameEl = userEl.getChild("userName");
            if(debugFlag)
                LOG.debug("USERNAME: " + userNameEl.getTextNormalize());
            if(userNameEl.getTextNormalize().contains(userName)) {
                if(debugFlag)
                    LOG.debug("found--->: " + userName);
                
                returnedEl = userEl;
            }
        }
        
        LOG.debug("returnedEl: " + returnedEl);
        */

        LOG.debug("------End UserOperations getUserInfoFromUserNameXML------");
        return returnedEl;
    }
    
    
    /* Editing user info */
    public static void editUserInfoInXML(final HttpServletRequest request,File file) {
        LOG.debug("------UserOperations editUserInfoFromXML------");
        
        String userName = request.getParameter("userName");
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String emailAddress = request.getParameter("emailAddress");
        String status = request.getParameter("status");
        
        // this logic is deprecated and only used for testing - it utilizes the xml in users.store 
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
        try{

            Document document = (Document) builder.build(file);
            
            Element rootNode = document.getRootElement();
            if(editLogFlag)
                LOG.debug("root name: " + rootNode.getName());
            
            List users = (List)rootNode.getChildren();
            //userArray =  new User[users.size()];
            
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element) users.get(i);
                if(editLogFlag)
                    LOG.debug(userName + " " + userEl.getChild("userName").getTextNormalize());
                if(userEl.getChild("userName").getTextNormalize().equals(userName)) {
                    if(editLogFlag)
                        LOG.debug("\t\t\tChange this one");
                    userEl.getChild("lastName").setText(lastName);
                    userEl.getChild("firstName").setText(firstName);
                    userEl.getChild("status").setText(status);
                    userEl.getChild("emailAddress").setText(emailAddress);
                    
                    //Debugging for groups
                    // MUST TAKE THIS OUT FOR PRODUCTION
                    //
                    if(userEl.getChild("groups") == null) {
                        Element groupsEl = new Element("groups");
                        Element groupEl = new Element("group");
                        groupEl.setText("group1");
                        groupsEl.addContent(groupEl);
                    }
                    
                }
            }
            
            
            
            

            XMLOutputter outputter = new XMLOutputter();
            xmlContent = outputter.outputString(rootNode);
            
            if(editLogFlag) {
                if(writeXMLTOLogFlag) {
                    LOG.debug("NEW XMLCONTENT \n" + xmlContent);
                }
            } 
                    
            Writer output = null;
            output = new BufferedWriter(new FileWriter(file));
            output.write(xmlContent);
            if(editLogFlag)
                LOG.debug("Writing to file");
            output.close();
            
            
        }catch(Exception e) {
            LOG.debug("Couldn't write new xml to file");
        }
        LOG.debug("------End UserOperations editUserInfoFromXML------");
    }
    
    
    

    /* helper function for getting users from xml store */
    public static User [] getAllUsersFromXML(File file) throws IOException {

        LOG.debug("------UserOperations getAllUsersFromXML------");
        
        //User [] userArray = null;
        
        /* this logic is deprecated and only used for testing - it utilizes the xml in users.store */
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        User [] userArray = new User[1];
        
        try{
            LOG.debug("Building document");
            
            Document document = (Document) builder.build(file);
            
            Element rootNode = document.getRootElement();
            LOG.debug("root name: " + rootNode.getName());
            
            List users = (List)rootNode.getChildren();
            userArray =  new User[users.size()];
            
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element) users.get(i);
                List attributes = (List)userEl.getChildren();
                
                String firstName = "";
                String lastName = "";
                String userName = "";
                String emailAddress = "";
                String status = "";
                String organization = "";
                String city = "";
                String state = "";
                String country = "";
                String openId = "";
                String DN = "";
                String groups = "";
                
                for(int j=0;j<attributes.size();j++)
                {
                    Element attEl = (Element) attributes.get(j);
                    if(attEl.getName().equals("firstName")) {
                        firstName = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("lastName")) {
                        lastName = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("userName")) {
                        userName = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("emailAddress")) {
                        emailAddress = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("status")) {
                        status = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("organization")) {
                        organization = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("city")) {
                        city = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("state")) {
                        state = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("country")) {
                        country = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("openId")) {
                        openId = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("DN")) {
                        DN = attEl.getTextNormalize();
                    }
                    else if(attEl.getName().equals("groups")) {
                        groups = attEl.getTextNormalize();
                        
                    }
                }
                
                //create new User object
                //generic groups for now
                Group grp = new Group("CDIAC","Standard","Valid");
                Group [] grps = {grp};
                String middleName = "mudd";
                User user = new User(firstName,lastName,middleName,userName,emailAddress,status,organization,city,state,country,openId,DN);
                
                userArray[i] = user;
            }
            
        }catch(Exception e) {
            LOG.debug("File not found");
            e.printStackTrace();
        }
        LOG.debug("------End UserOperations getAllUsersFromXML------");
        return userArray;
        //return null;
    }
    
    
    /* Deleting user info */
    public static void deleteUserInfoFromXML(final HttpServletRequest request,File file) {
        LOG.debug("------UserOperations deleteUserInfoFromXML------");
        String userName = request.getParameter("user");
        
        /* this logic is deprecated and only used for testing - it utilizes the xml in users.store */
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
        try{

            Document document = (Document) builder.build(file);
            
            Element rootNode = document.getRootElement();
            if(deleteLogFlag)
                LOG.debug("root name: " + rootNode.getName());
            
            List users = (List)rootNode.getChildren();
            //userArray =  new User[users.size()];
            
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element) users.get(i);
                if(deleteLogFlag)
                    LOG.debug(userName + " " + userEl.getChild("userName").getTextNormalize());
                if(userEl.getChild("userName").getTextNormalize().equals(userName)) {
                    if(deleteLogFlag)
                        LOG.debug("\t\t\tDelete this one");
                    rootNode.removeContent(userEl);
                }
            }
            

            XMLOutputter outputter = new XMLOutputter();
            xmlContent = outputter.outputString(rootNode);
            
            if(deleteLogFlag) {
                if(writeXMLTOLogFlag) {
                    LOG.debug("NEW XMLCONTENT \n" + xmlContent);
                }
            }
            Writer output = null;
            output = new BufferedWriter(new FileWriter(file));
            output.write(xmlContent);
            if(deleteLogFlag)
                LOG.debug("Writing to file");
            output.close();
            
        }catch(Exception e) {
            LOG.debug("Couldn't write new xml to file");
        }
        LOG.debug("------End UserOperations deleteUserInfoFromXML------");
    }
    
    
    
    
    public static Element getUsersFromGroupName() {
        return null;
    }
    
    
    public static Element getAdminUserFromGroupName() {
        return null;
    }
    
    
}
