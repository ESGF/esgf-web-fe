package org.esgf.commonui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
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
    
    /* Adding user info */
    public static void writeUserInfoToXML(final HttpServletRequest request, File file) {
        
        LOG.debug("*****In WriteUserInfoTOXML*****");

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
            
            /*Debugging for groups
             * MUST TAKE THIS OUT FOR PRODUCTION
             */
            Element groupsEl = new Element("groups");
            Element groupEl = new Element("group");
            Element groupNameEl = new Element("name");
            groupNameEl.setText("group1");
            groupEl.addContent(groupNameEl);
            groupsEl.addContent(groupEl);
            userElement.addContent(groupsEl);
            /*
             * End debugging group
             */
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

        LOG.debug("*****End WriteUserInfoTOXML*****\n\n\n");
    }

}
