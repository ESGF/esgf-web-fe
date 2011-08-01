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
    
    
    /*
     * Used to extract the openid from the cookie in the header of a request
     */
    public static String getIdFromHeaderCookie(HttpServletRequest request) {
        LOG.debug("------Utils getIdFromHeaderCookie------");
        
        Cookie [] cookies = request.getCookies();
        
        String userId = "";
        
        for(int i=0;i<cookies.length;i++) {
            LOG.debug("CookieArray: " + i + " " + cookies[i].getName());
            if(cookies[i].getName().equals("esgf.idp.cookie")) {
                userId = cookies[i].getValue();
            }
        }

        LOG.debug("------End tils getIdFromHeaderCookie------");
        return userId;
    }
    
    //Used by ManageUsersController to obtain the "type"
    public static String getTypeFromQueryString(HttpServletRequest request) {
        LOG.debug("------Utils getTypeFromQueryString------");
        String type = "";
        Enumeration<String> paramEnum = request.getParameterNames();
        while(paramEnum.hasMoreElements()) { 
            String postContent = (String) paramEnum.nextElement();
            LOG.debug("POSTCONTENT: " + postContent + "\n\n\n");
            if(postContent.equalsIgnoreCase("type")) {
                type = request.getParameter(postContent);
            }
        }
        LOG.debug("------End Utils getTypeFromQueryString------");
        return type;
    }
    
    
    
    
    
    /**
     * headerStringInfo(HttpServletRequest request)
     * Private method that prints out the header contents of the request.  Used mainly for debugging.
     * 
     * @param request
     */
    public static void headerStringInfo(HttpServletRequest request) {
        LOG.debug("--------Utils Header String Info--------");
        Enumeration headerNames = request.getHeaderNames(); 
        while(headerNames.hasMoreElements()) { 
            String headerName = (String)headerNames.nextElement(); 
            LOG.debug(headerName+"-->"); 
            LOG.debug(request.getHeader(headerName)); 
        }
        LOG.debug("--------End Utils Header String Info--------");
    }
    /**
     * queryStringInfo(HttpServletRequest request)
     * Private method that prints out the contents of the request.  Used mainly for debugging.
     * 
     * @param request
     */
    public static void queryStringInfo(HttpServletRequest request) {
        LOG.debug("--------Utils Query String Info--------");
        Enumeration<String> paramEnum = request.getParameterNames();
        
        while(paramEnum.hasMoreElements()) { 
            String postContent = (String) paramEnum.nextElement();
            LOG.debug(postContent+"-->"); 
            LOG.debug(request.getParameter(postContent));
        }
        LOG.debug("--------End Utils Query String Info--------");
    }
    
    /*
     * Single level Element nesting debugger
     */
    public static void printElementContents(Element element) {
        LOG.debug("--------Utils printElementContents--------");
        List children = (List)element.getChildren();
        for(int i=0;i<children.size();i++)
        {
            LOG.debug("Element: " + i + " " + children.get(i));
        }
        LOG.debug("--------End Utils printElementContents--------");
    }
    
    
}
