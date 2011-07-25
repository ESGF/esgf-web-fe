package org.esgf.commonui;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esgf.adminui.AccountsController;
import org.esgf.adminui.User;
import org.jdom.Document;
import org.jdom.Element;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.ui.Model;

public class Utils {
    
    private final static Logger LOG = Logger.getLogger(Utils.class);

    private final static boolean debugFlag = true;
    
    
    public static String getIdFromHeaderCookie(HttpServletRequest request) {
        LOG.debug("GetCookieFromHeader");
        
        Cookie [] cookies = request.getCookies();
        
        String userId = "";
        
        for(int i=0;i<cookies.length;i++) {
            LOG.debug("CookieArray: " + i + " " + cookies[i].getName());
            if(cookies[i].getName().equals("esgf.idp.cookie")) {
                userId = cookies[i].getValue();
            }
        }
        
        return userId;
    }
    
    //Used by ManageUsersController to obtain the "type"
    public static String getTypeFromQueryString(HttpServletRequest request) {
        String type = "";
        Enumeration<String> paramEnum = request.getParameterNames();
        while(paramEnum.hasMoreElements()) { 
            String postContent = (String) paramEnum.nextElement();
            if(postContent.equalsIgnoreCase("type")) {
                type = request.getParameter(postContent);
            }
        }
        return type;
    }
    
    public static User populateUserObjectFromIdXML(String id, File file) throws JDOMException, IOException {
        User user = new User();
        
        Element element = getUserInfoFromUserNameXML(id, file);
        
        
        String user_userName = element.getChild("userName").getTextNormalize();
        LOG.debug("USERNAME: " + user_userName);
        String user_lastName = element.getChild("lastName").getTextNormalize();
        String user_firstName = element.getChild("firstName").getTextNormalize();
        String user_emailAddress = element.getChild("emailAddress").getTextNormalize();
        String user_status = element.getChild("status").getTextNormalize();
        String user_organization = "";
        String user_city = "";
        String user_state = "";
        String user_country = "";
        String user_openId = "";
        String user_DN = "";
        /*
        String user_organization = element.getChild("organization").getTextNormalize();
        String user_city = element.getChild("city").getTextNormalize();
        String user_state = element.getChild("state").getTextNormalize();
        String user_country = element.getChild("country").getTextNormalize();
        String user_openId = element.getChild("openId").getTextNormalize();
        String user_DN = element.getChild("DN").getTextNormalize();
        */
        user = new User(user_userName,
             user_lastName,
             user_firstName,
             user_emailAddress,
             user_status,
             user_organization,
             user_city,
             user_state,
             user_country,
             user_openId,
             user_DN,
             null);
        
        return user;
    }
    
    
    
    public static Element getUserInfoFromUserNameXML(String userName, File file) throws JDOMException, IOException {
        LOG.debug("In get UserInfoFromUserName: " + userName);
        //final File file = new File(USERS_FILE);
        SAXBuilder builder = new SAXBuilder();
        
        Document document = (Document) builder.build(file);
        
        Element rootNode = document.getRootElement();
        
        if(debugFlag)   
            LOG.debug("root name: " + rootNode.getName());
        
        Element returnedEl = null;
        
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
        
        
        return returnedEl;
    }
    
    
    /**
     * headerStringInfo(HttpServletRequest request)
     * Private method that prints out the header contents of the request.  Used mainly for debugging.
     * 
     * @param request
     */
    public static void headerStringInfo(HttpServletRequest request) {
        LOG.debug("--------Header String Info--------");
        Enumeration headerNames = request.getHeaderNames(); 
        while(headerNames.hasMoreElements()) { 
            String headerName = (String)headerNames.nextElement(); 
            LOG.debug(headerName+"-->"); 
            LOG.debug(request.getHeader(headerName)); 
        }
        LOG.debug("--------End Header String Info--------");
    }
    /**
     * queryStringInfo(HttpServletRequest request)
     * Private method that prints out the contents of the request.  Used mainly for debugging.
     * 
     * @param request
     */
    public static void queryStringInfo(HttpServletRequest request) {
        LOG.debug("--------Query String Info--------");
        Enumeration<String> paramEnum = request.getParameterNames();
        
        while(paramEnum.hasMoreElements()) { 
            String postContent = (String) paramEnum.nextElement();
            LOG.debug(postContent+"-->"); 
            LOG.debug(request.getParameter(postContent));
        }
        LOG.debug("--------End Query String Info--------");
    }
    
    /*
     * Single level Element nesting debugger
     */
    public static void printElementContents(Element element) {
        List children = (List)element.getChildren();
        for(int i=0;i<children.size();i++)
        {
            LOG.debug("Element: " + i + " " + children.get(i));
        }
    }
    
    
}
