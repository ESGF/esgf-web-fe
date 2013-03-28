package org.esgf.email;

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
	

	
	/*
	
	
	
	
	
	//receiver
	public static String RECEIVER_USERNAME = "jfharney@gmail.com";
	
	//header and body text
	public static String DEFAULT_HEADER_TEXT = "Your data has been successfully staged!";
	public static String DEFAULT_BODY_TEXT = "Dear ESGF User, " +
			"\nThe data that you have ordered is now available on disk.  Please download the attached wget script to extract the files.";
	
	//default files names to be extracted
	public static String [] DEFAULT_FILE_NAMES = {"http://google.com","http://yahoo.com"};
	
	//default wget script name
	public static final String DEFAULT_SCRIPT_FILE_NAME = "wget.sh";
	
	

	public static final String DEFAULT_INITIAL_EMAIL_HEADER = 
			"Your SRM request has been submitted";

	public static final String DEFAULT_INITIAL_EMAIL_BODY = 
			"Your request for file(s) in link\n"+ "url" +"\n has been submitted to SRM. It may take some time to retreive the" +
         		" data. You will receive another email when the data is ready for download along with the download link. " +
         		"\nThe link will be active for about 4 days after which it will be deactivated and you will be asked to resubmit " +
         		"your request. \n\n";
	

	public static final String DEFAULT_WGET_EMAIL_HEADER = 
			"Your SRM request has been completed";
	
	public static final String DEFAULT_WGET_EMAIL_BODY = 
			"";
			*/
}
