package org.esgf.email;

import org.esgf.srm.utils.SRMUtils;

import esg.common.util.ESGFProperties;
import esg.node.security.UserInfo;
import esg.node.security.UserInfoCredentialedDAO;

public class EmailUtils {

	
	//smtp params
	public static String MAIL_SMTP_HOST = "smtp.gmail.com";
	public static String MAIL_SMTP_SOCKETFACTORY_PORT = "465";
	public static String MAIL_SMTP_SOCKETFACTORY_CLASS = "javax.net.ssl.SSLSocketFactory";
	public static String MAIL_SMTP_AUTH = "true";
	public static String MAIL_SMTP_PORT = "465";

	//default from
	public static String DEFAULT_FROM = "esgf-user@lists.llnl.gov";
	
	//default to
	public static String DEFAULT_TO = "jfharney@gmail.com";

	//default header text
	public static String DEFAULT_HEADER_TEXT = "Default Header";
	
	//default body text
	public static String DEFAULT_BODY_TEXT = "Default body";
	
	
	//default file names
	public static String [] DEFAULT_FILE_IDS = {"file_id1","file_id2"};
	
	//default url names 
	public static String [] DEFAULT_FILE_URLS = {"file_url1","file_url2"};
	

	public static String getEmailAddrFromOpenId(String openid) {
	    
	    
        String emailAddr = null;
        
        if(SRMUtils.useDefaultEmail) {
            emailAddr = "jfharney@gmail.com";
        } else {
            UserInfoCredentialedDAO myUserInfoDAO;

            try{
                ESGFProperties myESGFProperties = new ESGFProperties();
                
                String passwd = myESGFProperties.getAdminPassword();   
                
                System.out.println("Getting email address from openid: " + openid);
                System.out.println("passwd: " + passwd);
                
                myUserInfoDAO = new UserInfoCredentialedDAO("rootAdmin",passwd,myESGFProperties);
                UserInfo userInfo = myUserInfoDAO.getUserById(openid);
                emailAddr = userInfo.getEmail();

                
            } catch(Exception e) {
                System.out.println("\n\tNo email found... using default");
                //e.printStackTrace();
                emailAddr = "jfharney@gmail.com";
            }
        }
        
        
        
        return emailAddr;
    }
	
}
