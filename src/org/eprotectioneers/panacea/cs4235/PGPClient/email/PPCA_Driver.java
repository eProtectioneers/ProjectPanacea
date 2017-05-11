package org.eprotectioneers.panacea.cs4235.PGPClient.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PPCA_Driver {
	
	public static void main(String args[]){
//		String host = "smtp.gmail.com";
//		int port = 587;
//		String username = "------";
//		String password = "123456"; //(Not really my PW :D )
//		
//		Properties props = new Properties();
//		props.put("mail.smtp.auth", true);
//		props.put("mail.smtp.starttls.enable", true);
//		
//		Session session = Session.getInstance(props);
//		
//		try{
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress("-@gmail.com"));
//			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("eprotectioneers@gmail.com"));
//			message.setSubject("This is a test mail, to ensure functionality!");
//			message.setText("Hello world!");
//			
//			Transport transport = session.getTransport("smtp");
//			transport.connect(host, port, username, password);
//			Transport.send(message);
//		} catch (MessagingException e){
//			throw new RuntimeException(e);
//		}
//		
		
		String host = "smtp.gmail.com";
		int port = 465;
		String user = "eprotectioneers";
		String password = "fakepassword";
		
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtps.host", host);
		props.put("mail.smtp.auth", "true");
		
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);
		try{
			Transport transport = session.getTransport();
			
			MimeMessage message = new MimeMessage(session);
			message.setSubject("Panacea Testing Mail - Dont reply!");
			message.setContent("THis is a standard test mail to see the functionality of PPCA.", "text/plain");
			
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("eProtectioneers@gmail.com"));
			
			transport.connect(host, port, user, password);
			transport.sendMessage(message,  message.getRecipients(Message.RecipientType.TO));
			
			transport.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
