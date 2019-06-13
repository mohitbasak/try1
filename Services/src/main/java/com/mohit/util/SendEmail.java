/**
 * 
 */
package com.mohit.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Session;
import javax.mail.Transport;

/**
 * @author Khalid.Khan
 *
 */
 	  
	public class SendEmail  {
		
		public boolean sendEmail(String to,final String from,String mesage,String subject) {
		String recipient = "khalid.khan@incture.com"; 
		String sender = "khalid.khan@incture.com";
		final String password="";
		String host = "127.0.0.1"; 
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host","smtp.office365.com");
		properties.setProperty("mail.smtp.auth","true");
		properties.setProperty("mail.smtp.starttls.enable","true");
		properties.setProperty("mail.smtp.user",sender);
		properties.setProperty("mail.smtp.password",password);
		
		
		Session session = Session.getDefaultInstance(properties, 
				new javax.mail.Authenticator() { 
				protected PasswordAuthentication getPasswordAuthentication() { 
				return new PasswordAuthentication(from,password);
				}}) ;
		  MimeMessage message = new MimeMessage(session); 
		  try {
		  message.setFrom(new InternetAddress(to)); 
		  message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		  message.setSubject(subject); 
		  message.setText(mesage); 
		  
		  Transport.send(message); 
	      System.out.println("Mail successfully sent"); 
		  }catch(Exception e) {
			 return false;
		  }
		  return true;
		}

}
