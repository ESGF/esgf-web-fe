package org.esgf.email;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.esgf.srm.SRMUtils;

import esg.common.util.ESGFProperties;

public class Email {

	private static boolean atHome = false;
	
	private String to;
	private String from;
	private String headerText;
	private String bodyText;
	
	private String [] file_ids;
	private String [] file_urls;
	
	private Attachment attachment;
	
	
	
	public static void main(String [] args) {
		
		
		
		Email e = new Email();
		
		//e.sendEmail();
		
		Attachment attachment1 = new Attachment();
		
		attachment1.setAttachmentName("wget.sh");
		attachment1.setAttachmentContent("New wget content");
		
		e.setAttachment(attachment1);

		e.setHeaderText("EMAIL1");
		e.setBodyText("EMAILBODY1");
		System.out.println(e);
		
		e.sendEmail();
		
		Attachment attachment2 = new Attachment();
		
		attachment2.setAttachmentName("wget.sh");
		
		String wgetContent = "";
		
		String [] file_urls = {
			 "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-12.nc",
			 "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-01.nc", 
			 "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-02.nc", 
			 "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-03.nc" 
		};
		
		
		/*
		String [] resulting_file_urls = SRMUtils.simulateSRM(file_urls);
		
		
		for(int i=0;i<resulting_file_urls.length;i++) {
			wgetContent += "wget " + resulting_file_urls[i] + "\n";
		}
		
		
		attachment2.setAttachmentContent(wgetContent);
		
		e.setAttachment(attachment2);
		e.setHeaderText("EMAIL2");
		e.setBodyText("EMAILBODY2 - from srm controller");
		
		System.out.println(e);
		
		e.sendEmail();
		
		System.out.println("Emails sent");
		*/
	}

	
	public Email() {
		
		this.from = EmailUtils.DEFAULT_FROM;
		this.to = EmailUtils.DEFAULT_TO;
		this.headerText = EmailUtils.DEFAULT_HEADER_TEXT;
		this.bodyText = EmailUtils.DEFAULT_BODY_TEXT;
		this.attachment = null;//new Attachment(EmailUtils.DEFAULT_ATTACHMENT_NAME,EmailUtils.DEFAULT_ATTACHMENT_CONTENT);
		
		this.file_ids = EmailUtils.DEFAULT_FILE_IDS;
		this.file_urls = EmailUtils.DEFAULT_FILE_URLS;
		
	}
	
	public Email(String [] file_ids,String [] file_urls) {

		this.from = EmailUtils.DEFAULT_FROM;
		this.to = EmailUtils.DEFAULT_TO;
		this.headerText = EmailUtils.DEFAULT_HEADER_TEXT;
		this.bodyText = EmailUtils.DEFAULT_BODY_TEXT;
		
		this.file_ids = file_ids;
		this.file_urls = file_urls;

	}

	public void sendEmail() {
		
		
		Properties props = new Properties();
		
		Session session = null;

		if(atHome)  {
			/*
			props.put("mail.smtp.host", EmailUtils.MAIL_SMTP_HOST);
			props.put("mail.smtp.socketFactory.port", EmailUtils.MAIL_SMTP_PORT);
			props.put("mail.smtp.socketFactory.class", EmailUtils.MAIL_SMTP_SOCKETFACTORY_CLASS);
			props.put("mail.smtp.auth", EmailUtils.MAIL_SMTP_AUTH);
			props.put("mail.smtp.port", EmailUtils.MAIL_SMTP_PORT);
			
			
			try{
				session = Session.getDefaultInstance(props,
						new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(EmailUtils.SENDER_USERNAME,EmailUtils.SENDER_PASSWORD);
							}
						});
				
				MimeMessage message = new MimeMessage(session);
	          
				message.setFrom(new InternetAddress(this.from));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.to));
	          
				message.setSubject(this.headerText);
				//message.setText(this.bodyText);
				Multipart mp = new MimeMultipart();
				
				MimeBodyPart mbp1 = new MimeBodyPart();
				mbp1.setText(this.bodyText);
			      
				//message.setText();
				mp.addBodyPart(mbp1);
			      
				MimeBodyPart mbp2 = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(this.attachment.getAttachmentName());
	
				this.writeFileAttachment();
		    	
				mbp2.setDataHandler(new DataHandler(fds));
				mbp2.setFileName(fds.getName());
				mp.addBodyPart(mbp2);
	          
	
				message.setContent(mp);
				
				Transport.send(message);
		          
				this.deleteFileAttachment();
			          
			} catch(Exception e) {
				e.printStackTrace();
			}
			*/
		}
		else {
			ESGFProperties myESGFProperties = null;
			System.out.println("Using localhost");
	        try {
				props = new ESGFProperties();
				
				
		        this.from = "esgf-user@lists.llnl.gov";
		        myESGFProperties = new ESGFProperties();

		        Properties properties = myESGFProperties;
		        properties.setProperty("mail.smtp.host","localhost");
		        session = Session.getDefaultInstance(properties);

		        
		          MimeMessage message = new MimeMessage(session);
		          message.setFrom(new InternetAddress(from));
		          message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.to));
		          
		          
		          if((this.attachment != null)) {
		        	  message.setSubject(this.headerText);
		        	  message.setText(this.bodyText);
		        	  Multipart mp = new MimeMultipart();
					
		        	  MimeBodyPart mbp1 = new MimeBodyPart();
		        	  mbp1.setText(this.bodyText);
				      
		        	  //message.setText();
		        	  mp.addBodyPart(mbp1);
				      
		        	  MimeBodyPart mbp2 = new MimeBodyPart();
		        	  FileDataSource fds = new FileDataSource(this.attachment.getAttachmentName());
		
		        	  this.writeFileAttachment();
			    	
		        	  mbp2.setDataHandler(new DataHandler(fds));
		        	  mbp2.setFileName(fds.getName());
		        	  mp.addBodyPart(mbp2);
		          
		
		        	  message.setContent(mp);
					
		        	  Transport.send(message);
			          
		        	  this.deleteFileAttachment();
		          } else {
		        	  message.setSubject(this.headerText);
		        	  //message.setText(this.bodyText);
		        	  Multipart mp = new MimeMultipart();
					
		        	  MimeBodyPart mbp1 = new MimeBodyPart();
		        	  mbp1.setText(this.bodyText);
				      
		        	  mp.addBodyPart(mbp1);
				      
		        	  message.setContent(mp);
					
		        	  Transport.send(message);
			          
		        	  this.deleteFileAttachment();
		          }
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * @return the headerText
	 */
	public String getHeaderText() {
		return headerText;
	}

	/**
	 * @param headerText the headerText to set
	 */
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	/**
	 * @return the bodyText
	 */
	public String getBodyText() {
		return bodyText;
	}

	/**
	 * @param bodyText the bodyText to set
	 */
	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	
	public void deleteFileAttachment() {
		
		FileOutputStream fop = null;
		File file;
 
		try {
			 
			file = new File(this.attachment.getAttachmentName());
			fop = new FileOutputStream(file);
 
			
			if(file.delete()){
				System.out.println(file.getName() + " is deleted!");
			}else{
				System.out.println("Delete operation is failed.");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void writeFileAttachment() {
		
		
		
		
		
		FileOutputStream fop = null;
		File file;
		String content = this.attachment.getAttachmentContent();
 
		try {
 
			file = new File(this.attachment.getAttachmentName());
			
			fop = new FileOutputStream(file);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
    	
	}

	
	
	public String toString() {
		
		String emailStr = "";
		
		emailStr += "\n----Email----" + "\n";
		
		emailStr += "\n" + "To: " + this.to;
		emailStr += "\n" + "From: " + this.from + "\n";
		emailStr += "\n" + "HeaderText: \n" + this.headerText + "\n";
		emailStr += "\n" + "BodyText: \n\n" + this.bodyText + "\n";
		
		emailStr += "\n" + "Attachment name: \n" + this.attachment.getAttachmentName() + "\n";
		
		emailStr += "\n" + "Attachment content: \n" + this.attachment.getAttachmentContent() + "\n";
		
		emailStr += "\n----End Email----" + "\n";
		
		return emailStr;
		
	}


	/**
	 * @return the attachment
	 */
	public Attachment getAttachment() {
		return attachment;
	}


	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}
	
}
