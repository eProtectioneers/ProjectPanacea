//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.cs4235.PGPClient.junit;

import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.eprotectioneers.panacea.cs4235.PGPClient.cryptex.EncryptionServiceEngine;

import junit.framework.TestCase;

/**
 * JUnit Test for the KeyFactory
 * @author eProtectioneers
 */
public class KeyFactoryTest extends TestCase
{
	/**
	 * Encryption Service Engine
	 */
	EncryptionServiceEngine ce;
	/**
	 * Private Key
	 */
	RSAPrivateKey privateKey;
	/**
	 * Public Key
	 */
	RSAPublicKey publicKey;
	/**
	 * Passphrase
	 */
	String passphrase;
	
	/**
	 * Message 1
	 */
	String message1;
	/**
	 * Message 2
	 */
	String message2;
	
	/**
	 * Setting up the Test Routine
	 */
	protected void setUp() throws Exception 
	{
		super.setUp();
		
		ce = EncryptionServiceEngine.getInstance();
		RSAKey[] keys = ce.generateRSAKeys();
		privateKey = (RSAPrivateKey) keys[0];
		publicKey = (RSAPublicKey) keys[1];
		passphrase = "eprotectioneers";
		
		message1 = "Normaly we would insert some strange " + 
				   "meaningless sentences ;) But this time " + 
				   "were trying keep the code meaningfull";
		message2 = "Project panacea is the only software " + 
				   "which has its own brain (not true :D)";
	}
	
	/**
	 * Test the Encoding functions
	 */
	public void testBase64Encoding()
	{
		String encoding1 = ce.Base64Encode(message1.getBytes());
		String encoding2 = ce.Base64Encode(message2.getBytes());
		
		String decoding1 = new String (ce.Base64Decode(encoding1));
		String decoding2 = new String (ce.Base64Decode(encoding2));
		
		assertEquals (message1, decoding1);
		assertEquals (message2, decoding2);
	}
	
	/**
	 * Testing the RSA Encryption
	 */
	public void testRSAEncryption () 
	{
		String cipher1 = ce.encrypt(publicKey, message1);
		String cipher2 = ce.encrypt(publicKey, message2);

		String plain1 = ce.decrypt(privateKey, cipher1);
		String plain2 = ce.decrypt(privateKey, cipher2);
		
		assertEquals (message1, plain1);
		assertEquals (message2, plain2);
	}
	
	/**
	 * Exporting the keys
	 */
	public void testExportKeys ()
	{
		String privateKeyEncoded = ce.getEncoded(privateKey);
		String publicKeyEncoded = ce.getEncoded(publicKey);
		
		RSAPrivateKey privKey = ce.parsePrivateKey(privateKeyEncoded);
		RSAPublicKey pubKey = ce.parsePublicKey(publicKeyEncoded);
		
		String cipher1 = ce.encrypt(pubKey, message1);
		String cipher2 = ce.encrypt(pubKey, message2);

		String plain1 = ce.decrypt(privKey, cipher1);
		String plain2 = ce.decrypt(privKey, cipher2);
		
		assertEquals (message1, plain1);
		assertEquals (message2, plain2);
		
		cipher1 = ce.encrypt(publicKey, message1);
		cipher2 = ce.encrypt(publicKey, message2);

		plain1 = ce.decrypt(privKey, cipher1);
		plain2 = ce.decrypt(privKey, cipher2);
		
		assertEquals (message1, plain1);
		assertEquals (message2, plain2);
		
		cipher1 = ce.encrypt(pubKey, message1);
		cipher2 = ce.encrypt(pubKey, message2);

		plain1 = ce.decrypt(privateKey, cipher1);
		plain2 = ce.decrypt(privateKey, cipher2);
		
		assertEquals (message1, plain1);
		assertEquals (message2, plain2);
	}
	
	/**
	 * Testing the AESEncryption
	 */
	public void testAESEncryption () 
	{
		String[] cipher1 = ce.encrypt(passphrase, message1);
		String[] cipher2 = ce.encrypt(passphrase, message2);
		
		String plain1 = ce.decrypt(cipher1[0], cipher1[1]);
		String plain2 = ce.decrypt(cipher2[0], cipher2[1]);
		
		assertEquals (message1, plain1);
		assertEquals (message2, plain2);
	}
	
	/**
	 * Testing DigitalSignature
	 */
	public void testDigitalSignature () 
	{
		String signature1 = ce.sign(privateKey, message1);
		String signature2 = ce.sign(privateKey, message2);
		
		assertTrue (ce.authenticate(publicKey, signature1, message1));
		assertTrue (ce.authenticate(publicKey, signature2, message2));
		
		assertFalse (ce.authenticate(publicKey, signature1, message2));
		assertFalse (ce.authenticate(publicKey, signature2, message1));
		
		RSAKey[] keys = ce.generateRSAKeys();
		privateKey = (RSAPrivateKey) keys[0];
		publicKey = (RSAPublicKey) keys[1];
		
		/* Warning: These throw exception */
		assertFalse (ce.authenticate(publicKey, signature1, message1));
		assertFalse (ce.authenticate(publicKey, signature2, message2));
	}
}
