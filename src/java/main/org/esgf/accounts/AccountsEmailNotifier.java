package org.esgf.accounts;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URLEncoder;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import esg.node.security.UserInfo;

/**
 * Implementation of {@link AccountsNotifier} that uses email as notification mechanism.
 * 
 * @author Luca Cinquini
 *
 */
public class AccountsEmailNotifier implements AccountsNotifier {
    
    private static final Log log = LogFactory.getLog(AccountsEmailNotifier.class);
    
    private Session session = null;
    private String messageTemplate = null;
    private String sender;
    
    private final String subject = "ESGF Account Creation Notification";
    private final String disableAccountUri = "/disableAccount";
 
    
    private final static String MESSAGE_TEMPLATE_FILE = "org/esgf/accounts/accountCreated.txt";
    private Pattern openid_pattern = Pattern.compile("@@user_openid@@");
    private Pattern url_pattern = Pattern.compile("@@disable_url@@");
    
    /**
     * Constructor needs property values from application configuration file.
     * @param props
     */
    public AccountsEmailNotifier(final Properties props) {
        
        try {
            log.info("Initializing AccountsEmailNotifier...");
    
            final File file = new ClassPathResource(MESSAGE_TEMPLATE_FILE).getFile();
            //final File file = new File(props.getProperty("accounts.templates.accountCreated"));
            messageTemplate = FileUtils.readFileToString(file);
            
            sender = props.getProperty("mail.admin.address","esg-admin@llnl.gov");
    
            session = Session.getInstance(props, null);
            
        } catch(IOException e) {
            log.error(e.getMessage());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accountCreated(final String serverURL, final UserInfo user, final String verificationToken) {

        final Message msg = new MimeMessage(session);
        
        try {
            msg.setHeader("X-Mailer","ESGF DataNode IshMailer");
            msg.setSentDate(new Date());
         
            msg.setFrom(new InternetAddress(sender));
            msg.setSubject(subject);
            
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            
            String msgText = new String(messageTemplate);
            msgText = replace(msgText, openid_pattern, user.getOpenid());
           
            // build disable account URL from user's assigned openid
            final String url = serverURL
                             + disableAccountUri 
                             + "?openid="+URLEncoder.encode(user.getOpenid(),"UTF-8")
                             + "&token="+verificationToken;
            msgText = replace(msgText, url_pattern, url);    
            
            msg.setText(msgText);
            
            log.debug("Sending email to: "+user.getEmail());
            log.debug(printMessage(msg));
            
            Transport.send(msg);

        } catch(MessagingException ex) {
            log.error("Problem Sending Email Notification to: "+user.getEmail(),ex);
        } catch(IOException ex) {
            log.error("Problem Sending Email Notification to: "+user.getEmail(),ex);
        }
        
    }
    
    /**
     * Utility method to manipulate the message template.
     * @param message
     * @param pattern
     * @param text
     * @return
     */
    private String replace(String message, final Pattern pattern, final String text) {
        final Matcher matcher = pattern.matcher(message);
        final String tmp =  matcher.replaceAll(text);
        return tmp;     
    }
    
    /**
     * Utility method to print out the email message.
     * @param msg
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    private String printMessage(final Message msg) throws MessagingException, IOException  {
        
        final String newline  = System.getProperty("line.separator");
        
        final StringWriter sw = new StringWriter();
        sw.append(newline);
        sw.append("Sender=").append((msg.getFrom()[0]).toString()).append(newline);
        sw.append("Subject=").append(msg.getSubject()).append(newline);
        sw.append("Sent Date=").append(msg.getSentDate().toString()).append(newline);
        sw.append("Text=").append(msg.getContent().toString());
   
        return sw.toString();        
        
    }
    

}
