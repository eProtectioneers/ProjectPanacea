package org.eprotectioneers.panacea.cs4235.PGPClient.email;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.eprotectioneers.panacea.cs4235.PGPClient.cryptex.EncryptionServiceEngine;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_DataRepo;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_FileEngine;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_Preferences;

/**
 * This class represents a PGP email. Wrapps: javax.mail.Message
 * @author eProtectioneers
 */
public class PPCA_PGPMail
{
	/* PGP email protocol:
	 * PGP combines symmetric-key encryption and public-key encryption. 
	 * The message is encrypted using a symmetric encryption algorithm, 
	 * which requires a symmetric key. Each symmetric key is used only 
	 * once and is also called a session key. The session key is 
	 * protected by encrypting it with the receiver's public key thus 
	 * ensuring that only the receiver can decrypt the session key. The 
	 * encrypted message along with the encrypted session key is sent 
	 * to the receiver. 
	 */

	private final static String BEGIN_EMAIL = "-----BEGIN PGP EMAIL-----";
	private final static String END_EMAIL = "-----END PGP EMAIL-----";
	private final static String BEGIN_KEY = "\n-----BEGIN PGP KEY-----\n";
	private final static String END_KEY = "\n-----END PGP KEY-----\n";
	private final static String BEGIN_MESSAGE = "\n-----BEGIN PGP SIGNED MESSAGE-----\n";
	private final static String END_MESSAGE = "\n-----END PGP SIGNED MESSAGE-----\n";
	private final static String BEGIN_SIGNATURE = "\n-----BEGIN PGP SIGNATURE-----\n";
	private final static String END_SIGNATURE = "\n-----END PGP SIGNATURE-----\n";

	private boolean encrypted=false;
	
	public String to;
	public String subject;
	public String from;
	public String payload;
	public Message message;
	public boolean isAunthentic;
	
	public enum Type
	{
		NEW,
		REPLY,
		FORWARD
	}
	
	public void setEncrypted(boolean encrypted){
		this.encrypted=encrypted;
	}

	/**
	 * Constructor. Called by the sender.
	 * @param to the recipient
	 * @param subject the subject
	 * @param from the sender
	 * @param payload the email body
	 */
	public PPCA_PGPMail(String to, String subject, String from, String payload)
	{
		this.to = to;
		this.subject = subject;
		this.from = from;
		this.payload = payload;
		this.message = null;
	}

	/**
	 * Set Java message.
	 * @param message Java message
	 */
	public void setMessage(Message message)
	{
		this.message = message;
	}

	/**
	 * Encrypt the message body and sign it.
	 * @return the encrypted message
	 * @throws PPCA_PGPMailException 
	 */
	public Message prepare() throws PPCA_PGPMailException
	{
		String body=this.payload;
		if(encrypted)body = encryptContent(body);
		
		/* Create Java message */
		Session session = PPCA_MailClient.getSMTPSession();
		try 
		{
			message = new MimeMessage(session);
			message.setSubject(subject);
			message.setContent(body, "text/plain");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
		}

		this.isAunthentic = true;
		return message;
	}
	
	/**
	 * Encrypt email content.
	 * @param content the email content
	 * @return the encrypted email content
	 * @throws PPCA_PGPMailException 
	 */
	public String encryptContent(String payload) throws PPCA_PGPMailException
	{
		String result = "";
		
		PPCA_DataRepo or = PPCA_DataRepo.getInstance();
		PPCA_Preferences pref = or.getPreferences();
		String passphrase = pref.getPassphrase();
		
		if (passphrase.isEmpty())
			throw new PPCA_PGPMailException("Passphrase has not been initialized!!!");
		
		/* Encrypt the message */
		EncryptionServiceEngine ce = EncryptionServiceEngine.getInstance();
		String[] cipher = ce.encrypt(passphrase, payload);
		
		/* Encrypt the key */
		String publicKeyRaw = or.getPublicKey(this.to);
		if (publicKeyRaw == null)
			throw new PPCA_PGPMailException("Unable to parse public key.");
		RSAPublicKey publicKey = ce.parsePublicKey(publicKeyRaw);
		String cipherKey = ce.encrypt(publicKey, cipher[0]);

		/* Sign the message */
		String filepath = pref.getKeyDirectory() + File.separator + pref.getPrivateKeyFilePath();
		String privateKeyRaw = PPCA_FileEngine.read(filepath);
		if (privateKeyRaw == null)
			throw new PPCA_PGPMailException("Unable to parse private key.");
		RSAPrivateKey privateKey = ce.parsePrivateKey(privateKeyRaw);
		String signature = ce.sign(privateKey, payload);
		
		result = PPCA_PGPMail.BEGIN_EMAIL;

		result += PPCA_PGPMail.BEGIN_KEY;
		result += cipherKey;
		result += PPCA_PGPMail.END_KEY;

		result += PPCA_PGPMail.BEGIN_MESSAGE;
		result += cipher[1];
		result += PPCA_PGPMail.END_MESSAGE;

		result += PPCA_PGPMail.BEGIN_SIGNATURE;
		result += signature;
		result += PPCA_PGPMail.END_SIGNATURE;

		result += PPCA_PGPMail.END_EMAIL;
		
		return result;
	}

	/**
	 * Parse an email to PGP email.
	 * @param message the email to be parsed
	 * @return true on successful or false otherwise
	 */
	public static PPCA_PGPMail parse(Message message)
	{
		try 
		{
			String mTo = InternetAddress.toString(message.getAllRecipients());
			String mSubject = message.getSubject();
			String mFrom = InternetAddress.toString(message.getFrom());
			String mBody = processBody(message);

			if (mBody.contains(PPCA_PGPMail.BEGIN_EMAIL))
			{
				try
				{
					/* Clear the input */
					mBody = mBody.replace("\r", "");
					
					/* Extract the payload */
					String key = mBody.substring(mBody.indexOf(PPCA_PGPMail.BEGIN_KEY) + PPCA_PGPMail.BEGIN_KEY.length(), 
							mBody.indexOf(PPCA_PGPMail.END_KEY));
					String payload = mBody.substring(mBody.indexOf(PPCA_PGPMail.BEGIN_MESSAGE) + PPCA_PGPMail.BEGIN_MESSAGE.length(), 
							mBody.indexOf(PPCA_PGPMail.END_MESSAGE));
					String signature = mBody.substring(mBody.indexOf(PPCA_PGPMail.BEGIN_SIGNATURE) + PPCA_PGPMail.BEGIN_SIGNATURE.length(), 
							mBody.indexOf(PPCA_PGPMail.END_SIGNATURE));
					
					EncryptionServiceEngine ce = EncryptionServiceEngine.getInstance();
					PPCA_DataRepo or = PPCA_DataRepo.getInstance();
					PPCA_Preferences pref = or.getPreferences();
					
					/* Decrypt the key */
					String filepath = pref.getKeyDirectory() + File.separator + pref.getPrivateKeyFilePath();
					String privateKeyRaw = PPCA_FileEngine.read(filepath);
					if (privateKeyRaw == null)
						throw new PPCA_PGPMailException("Unable to parse private key.");
					RSAPrivateKey privateKey = ce.parsePrivateKey(privateKeyRaw);
					String plainKey = ce.decrypt(privateKey, key);;

					/* Decrypt the payload */
					payload = ce.decrypt(plainKey, payload);

					/* Retrieve Public Key */
					String publicKeyRaw = or.getPublicKey(mFrom);
					RSAPublicKey publicKey = ce.parsePublicKey(publicKeyRaw);

					PPCA_PGPMail email = new PPCA_PGPMail(mTo, mSubject, mFrom, payload);
					email.setMessage(message);
					email.isAunthentic = ce.authenticate(publicKey, signature, payload);
					return email;
				}
				catch (Exception e)
				{
					/* There is something wrong with the format */
					PPCA_PGPMail email = new PPCA_PGPMail(mTo, mSubject, mFrom, mBody);
					email.setMessage(message);
					email.isAunthentic = true;
					return email;
				}
			}
			else
			{
				/* This is regular email */
				PPCA_PGPMail email = new PPCA_PGPMail(mTo, mSubject, mFrom, mBody);
				email.setMessage(message);
				email.isAunthentic = true;
				return email;
			}
		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Process the message body.
	 * @param message the message body
	 * @return String representation of the message body
	 */
	private static String processBody(Message message)
	{
		return processPart(message, "");
	}

	private static String processPart(Part message, String intermediate)
	{
		try 
		{
			if (message.isMimeType("text/plain"))
			{
				intermediate = (String)message.getContent();
			}
			else if (message.isMimeType("multipart/*"))
			{
				Multipart mp = (Multipart) message.getContent();
				int count = mp.getCount();
				for (int i = 0; i < count; i++)
				{
					intermediate += processPart(mp.getBodyPart(i), intermediate);
				}
			}
			else if (message.isMimeType("message/rfc822"))
			{
				intermediate = "Unsupported Email Type: message/rfc822";
			}
			else
			{
				/*
				 * If not MIME type, fetch it and check its Java type.
				 */
				Object o = message.getContent();
				if (o instanceof String) 
				{
					intermediate += (String)o;
				} 
				else if (o instanceof InputStream) 
				{
					InputStream is = (InputStream)o;
					int c;
					while ((c = is.read()) != -1)
						intermediate += c;
				} 
				else 
				{
					intermediate = "Unsupported Email Type: Unknown";
				}
			}
		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return intermediate;
	}
}
