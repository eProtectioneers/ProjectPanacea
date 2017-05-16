package org.eprotectioneers.panacea.cs4235.PGPClient.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;

import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_DataRepo;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_EmailStore;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_Preferences;

/**
 * This represents a structure for a basic mail client.
 * Using POP3 Protocol to receive messages and
 * SMTP to send new emails
 * @author eProtectioneers
 */
public class PPCA_MailClient 
{
	/**
	 * Singleton PPCA_MailClient
	 */
	private static PPCA_MailClient emailClient;
	
	/**
	 * DataRepository
	 */
	private PPCA_DataRepo or;
	/**
	 * Preferences
	 */
	private PPCA_Preferences pref;
	/**
	 * EmailStore
	 */
	private PPCA_EmailStore es;

	/**
	 * Outbound connection string
	 */
	private final String outbound = "smtp.gmail.com";
	/**
	 * Inbound connection string
	 */
	private final String inbound = "pop.gmail.com";

	/**
	 * Constructor
	 */
	private PPCA_MailClient ()
	{
		or = PPCA_DataRepo.getInstance();
		pref = or.getPreferences();
		es = or.getEmailStore();
	}

	/**
	 * Get an instance of email client.
	 * @return an instance of email client
	 */
	public static PPCA_MailClient getInstance()
	{
		if (emailClient == null)
			emailClient = new PPCA_MailClient ();
		return emailClient;
	}

	/**
	 * Login to email account.
	 * @param username the username of an email account
	 * @param password the password of an email account
	 * @return true on success or false otherwise
	 */
	public boolean login (String username, String password)
	{
		//TODO: Figure out how to authenticate this	
		pref.setUsername(username);
		pref.setPassword(password);
		return true;
	}

	/**
	 * Return the properties of this email client.
	 * @return the properties of this email client
	 * @throws PPCA_PGPMailException 
	 */
	public static Session getSMTPSession() throws PPCA_PGPMailException
	{
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		try
		{
			Session session = Session.getInstance(props, new Authenticator() 
			{
				@Override
				protected PasswordAuthentication getPasswordAuthentication() 
				{
					PPCA_DataRepo or = PPCA_DataRepo.getInstance();
					PPCA_Preferences pref = or.getPreferences();
					String username = pref.getUsername();
					String password = pref.getPassword();
					return new PasswordAuthentication(username, password);
				}

			});
			return session;
		}
		catch (Exception e)
		{
			throw new PPCA_PGPMailException(e.getMessage());
		}
	}

	/**
	 * Send an email through SMTP. The email will be encrypted and signed.
	 * @param email the email to be sent
	 * @return true on success or false otherwise
	 */
	public boolean send (PPCA_PGPMail email)
	{		
		try 
		{			
			Message message = email.prepare();
			Transport.send(message);

			return true;
		} 
		catch (NoSuchProviderException e) 
		{
			e.printStackTrace();
		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
		} 
		catch (PPCA_PGPMailException e) 
		{
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Reply an email. A prefix "RE: " will be added to the 
	 * subject.
	 * @param email the email to be replied
	 * @param reply the content of email
	 * @return true on success or false otherwise
	 */
	@Deprecated
	public boolean reply(PPCA_PGPMail email, String reply)
	{

//		try 
//		{
//			Message message = email.message;
//			MimeMessage replyMessage = (MimeMessage) message.reply(false);
//			replyMessage.setFrom(new InternetAddress(pref.getUsername()));
//			replyMessage.setText(reply);
//			Transport.send(replyMessage);
//
//			return true;
//		} 
//		catch (MessagingException e) 
//		{
//			e.printStackTrace();
//		}

		return false;
	}

	/**
	 * Mark an email to be deleted.
	 * @param email the email to be deleted
	 * @return true on success or false otherwise
	 */
	@Deprecated
	public boolean delete(PPCA_PGPMail email)
	{
		return false;
	}

	/**
	 * Mark an email to be read
	 * @param email the email to be read
	 * @return true on success or false otherwise
	 */
	@Deprecated
	public boolean markRead(PPCA_PGPMail email)
	{
		return false;
	}

	/**
	 * Get all messages in INBOX folder.
	 * @param folder the folder
	 * @return all messages within a folder
	 */
	public PPCA_PGPMail[] getMessages()
	{
		PPCA_PGPMail[] emails = null;

		try 
		{
			String username = pref.getUsername();
			String password = pref.getPassword();

			Properties props = System.getProperties();
			Session session = Session.getInstance(props, null);
			Store store = session.getStore("pop3s");
			store.connect(inbound, username, password);

			Folder folder = store.getDefaultFolder();
			if (folder != null)
			{
				folder = store.getFolder("INBOX");
				folder.open(Folder.READ_WRITE);
				Message[] messages = folder.getMessages();

				FetchProfile fp = new FetchProfile();
				fp.add(FetchProfile.Item.ENVELOPE);
				fp.add(FetchProfile.Item.FLAGS);
				fp.add("X-Mailer");
				folder.fetch(messages, fp);

				int emailCount = PPCA_EmailStore.MAX_COUNT;
				if (emailCount > folder.getMessageCount())
					emailCount = folder.getMessageCount();

				emails = new PPCA_PGPMail[emailCount];
				for (int i = 0; i < emails.length; i++)
				{
					/* Messages will be received reversed! Switch index! */
					PPCA_PGPMail email = PPCA_PGPMail.parse(messages[messages.length - 1 - i]);
					emails[i] = email;
				}
				
				/* Store the emails */
				es.add(emails);
			}

			folder.close(false);
			store.close();
			or.saveEmailStore(es);
		} 
		catch (NoSuchProviderException e) 
		{
			e.printStackTrace();
		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
		}

		return emails;
	}
}
