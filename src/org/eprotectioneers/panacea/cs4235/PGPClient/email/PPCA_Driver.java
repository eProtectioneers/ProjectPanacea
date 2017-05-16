//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.cs4235.PGPClient.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Email function test class
 * @author eProtectioneers
 */
public class PPCA_Driver {
	
	/**
	 * Main
	 * To test the email functionality
	 * (To use it use your own password and account!)
	 * @param args unused
	 */
	public static void main(String args[]){
		
		//Settings
		String host = "smtp.gmail.com";
		int port = 465;
		String user = "eprotectioneers";
		String password = "fakepassword";
		
		//setting up the protocols
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtps.host", host);
		props.put("mail.smtp.auth", "true");
		
		//creating a new session
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);
		try{
			Transport transport = session.getTransport();
			MimeMessage message = new MimeMessage(session);
			
			//Setting the subject
			message.setSubject("Panacea Testing Mail - Dont reply!");
			//Setting the content of the mime mail
			message.setContent("THis is a standard test mail to see the functionality of PPCA.", "text/plain");
			//Adding a recipient
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("eProtectioneers@gmail.com"));
			
			//Configuring the transport of the message
			transport.connect(host, port, user, password);
			transport.sendMessage(message,  message.getRecipients(Message.RecipientType.TO));
			transport.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
