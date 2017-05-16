//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.cs4235.PGPClient.cryptex;

//Imports
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

//Imports Crypto
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//Imports Bouncy Castle
import org.bouncycastle.util.encoders.Base64;

/**
 * This class represents a key factory that will produce RSA and AES
 * keys for cryptography purposes.
 * @author eProtectioneers
 */
public class EncryptionServiceEngine 
{
	/**
	 * singleton keyfactory
	 */
	private static EncryptionServiceEngine keyFactory;

	/**	
	 * contstructor
	 */
	private EncryptionServiceEngine()
	{

	}

	/**
	 * Get instance of key factory.
	 * @return the instance of key factory
	 */
	public static EncryptionServiceEngine getInstance()
	{
		if (keyFactory == null)
			keyFactory = new EncryptionServiceEngine();
		return keyFactory;
	}

	/**
	 * Convert byte array to a Base64 encoded string.
	 * @param raw the byte array to be converted
	 * @return Base64 encoded string
	 */
	public String Base64Encode(byte[] raw)
	{
		return new String (Base64.encode(raw));
	}

	/**
	 * Convert Base64 encoded string to a byte array.
	 * @param raw the Base64 encoded string to be converted
	 * @return the byte array
	 */
	public byte[] Base64Decode(String raw)
	{
		return Base64.decode(raw.getBytes());
	}

	/**
	 * Generate a pair of 1024-bit RSA keys. The public key and private key 
	 * can be obtained by calling getRSAPublicKey() and getRSAPrivateKey()
	 * respectively. Every time this function is called, a new pair of
	 * keys will be generated.
	 * @return an array of RSAKey:
	 * <ul>
	 * <li>RSAKey[0] contains RSAPrivateKey</li>
	 * <li>RSAKey[1] contains RSAPublicKey</li>
	 * </ul>
	 */
	public RSAKey[] generateRSAKeys()
	{
		RSAKey[] keys = new RSAKey[2];
		try 
		{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);

			KeyPair kp = kpg.generateKeyPair();
			keys[0] = (RSAPrivateKey)kp.getPrivate();
			keys[1] = (RSAPublicKey)kp.getPublic();
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		return keys;
	}

	/**
	 * Get Base64 encoded value of a RSA private key.
	 * @param privateKey the RSA private key
	 * @return the Base64 encoded value
	 */
	public String getEncoded(RSAPrivateKey privateKey)
	{
		byte[] raw = privateKey.getEncoded();
		return Base64Encode (raw);
	}

	/**
	 * Get Base64 encoded value of a RSA public key.
	 * @param publicKey the RSA public key
	 * @return the Base64 encoded value
	 */
	public String getEncoded(RSAPublicKey publicKey)
	{
		byte[] raw = publicKey.getEncoded();
		return Base64Encode (raw);
	}

	/**
	 * Parse a Base64 encoded of RSA private key.
	 * @param privateKey the Base64 encoded private key
	 * @return the RSA private key
	 */
	public RSAPrivateKey parsePrivateKey(String privateKey)
	{
		try 
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64Decode(privateKey));
			return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidKeySpecException e) 
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Parse a Base64 encoded of RSA public key.
	 * @param privateKey the Base64 encoded public key
	 * @return the RSA public key
	 */
	public RSAPublicKey parsePublicKey(String publicKey)
	{
		try 
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64Decode(publicKey));
			return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidKeySpecException e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Calculate a fingerprint of a RSA public key using MD5 Hash.
	 * @param publicKey the RSA public key
	 * @return the fingerprint
	 */
	public String fingerprint(RSAPublicKey publicKey) 
	{
		String result = "MD5::";
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(publicKey.getEncoded());
			byte[] digest = md.digest();

			for (int i = 0; i < digest.length; i++) 
			{
				if (i != 0) 
					result += ":";
				int b = digest[i] & 0xff;
				String hex = Integer.toHexString(b);
				if (hex.length() == 1)
					result += "0";
				result += hex;
			}
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Generate a 256-bit symmetric AES key.
	 * @param passphares the passphrase to generate random key
	 * @return the AES key
	 */
	private SecretKey generateAESKey(byte[] passphrase)
	{
		SecretKey key = null;
		try 
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] password = md.digest(passphrase);
			key = new SecretKeySpec(password, "AES");
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} 

		return key;
	}

	/**
	 * Generate an Initialization Vector
	 * @return the Initialization Vector
	 */
	private byte[] generateIV()
	{
		Random r = new SecureRandom();
		byte[] iv = new byte[16];
		r.nextBytes(iv);
		return iv;
	}

	/**
	 * Decode an Initialization Vector from a string
	 * @param raw the byte to be decoded
	 * @return the Initialization Vector spec
	 */
	private IvParameterSpec IVDecode (byte[] raw)
	{
		return new IvParameterSpec(raw);
	}

	/**
	 * Encrypt a payload using RSAPublicKey.
	 * @param publicKey the RSA public key used to encrypt
	 * @param payload the plaintext to be encrypted
	 * @return Base64 encoded of encrypted payload
	 */
	public String encrypt(RSAPublicKey publicKey, String payload)
	{
		String ciphertext = null;
		try 
		{
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] ciphertextRaw = cipher.doFinal(payload.getBytes());
			ciphertext = Base64Encode(ciphertextRaw);
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} 
		catch (NoSuchPaddingException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidKeyException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalBlockSizeException e) 
		{
			e.printStackTrace();
		} 
		catch (BadPaddingException e) 
		{
			e.printStackTrace();
		}

		return ciphertext;
	}

	/**
	 * Decrypt a payload using RSAPrivateKey.
	 * @param privateKey the RSA private key used to decrypt
	 * @param payload the Base64 encoded of encrypted payload
	 * @return the plaintext
	 */
	public String decrypt(RSAPrivateKey privateKey, String payload)
	{
		String plaintext = null;
		try 
		{
			byte[] payloadRaw = Base64Decode(payload);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] plaintextRaw = cipher.doFinal(payloadRaw);
			plaintext = new String (plaintextRaw);
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} 
		catch (NoSuchPaddingException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidKeyException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalBlockSizeException e) 
		{
			e.printStackTrace();
		} 
		catch (BadPaddingException e) 
		{
			e.printStackTrace();
		}

		return plaintext;
	}

	/**
	 * Encrypt a payload using AES symmetric key. A random 128-bit AES key 
	 * and Initialization Vector will be generated.
	 * @param passphrase the passphrase to generate the key
	 * @param payload the plaintext to be encrypted
	 * @return a String array:
	 * <ul>
	 * <li>String[0] contains Base64 encoded of secret key</li>
	 * <li>String[1] contains Base64 encoded of encrypted payload</li>
	 * </ul>
	 */
	public String[] encrypt(String passphrase, String payload)
	{
		String[] messages = new String[2];
		byte[] aesKeyRaw = passphrase.getBytes();
		byte[] ivRaw = generateIV();
		SecretKey aesKey = generateAESKey(aesKeyRaw);
		IvParameterSpec iv = new IvParameterSpec(ivRaw);

		try 
		{
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey, iv);
			byte[] ciphertextRaw = cipher.doFinal(payload.getBytes());

			/* The first 16 bytes will be the IV */
			messages[0] = Base64Encode(ivRaw) + "::" + Base64Encode(aesKeyRaw);
			messages[1] = Base64Encode(ciphertextRaw);
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} 
		catch (NoSuchPaddingException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidKeyException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidAlgorithmParameterException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalBlockSizeException e) 
		{
			e.printStackTrace();
		} 
		catch (BadPaddingException e) 
		{
			e.printStackTrace();
		}

		return messages;
	}

	/**
	 * Decrypt a payload using AES symmetric key.
	 * @param secretKey the AES symmetric key used to decrypt with 
	 * Initialization Vector appended
	 * @param payload the Base64 encoded of encrypted payload
	 * @return the plaintext
	 */
	public String decrypt(String secretKey, String payload)
	{
		/* The first 16 bytes will be the IV */
		String ivEncoded = secretKey.substring(0, secretKey.indexOf("::"));
		String keyEncoded = secretKey.substring(secretKey.indexOf("::") + 2);
		byte[] ivRaw = Base64Decode(ivEncoded);
		byte[] keyRaw = Base64Decode(keyEncoded);
		SecretKey aesKey = generateAESKey(keyRaw);
		IvParameterSpec iv = IVDecode(ivRaw);

		try 
		{
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, aesKey, iv);
			byte[] plaintext = cipher.doFinal(Base64Decode(payload));
			return new String(plaintext);
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} 
		catch (NoSuchPaddingException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidKeyException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidAlgorithmParameterException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalBlockSizeException e) 
		{
			e.printStackTrace();
		} 
		catch (BadPaddingException e) 
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create a digital signature.
	 * @param privateKey the signer's private key used to sign the payload
	 * @param payload the payload in cleartext to be signed
	 * @return Base64 encoded of digital signature
	 */
	public String sign(RSAPrivateKey privateKey, String payload)
	{
		/* http://upload.wikimedia.org/wikipedia/commons/2/2b/Digital_Signature_diagram.svg */

		String ciphertext = null;
		try 
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(payload.getBytes());

			/* Encrypt the hash using signer's private key */
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] ciphertextRaw = cipher.doFinal(hash);
			ciphertext = Base64Encode(ciphertextRaw);
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} 
		catch (NoSuchPaddingException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidKeyException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalBlockSizeException e) 
		{
			e.printStackTrace();
		} 
		catch (BadPaddingException e) 
		{
			e.printStackTrace();
		}

		return ciphertext;
	}

	/**
	 * Authenticate a digital signature.
	 * @param publicKey the signer's public key used to authenticate the payload
	 * @param signature Base64 encoded of digital signature
	 * @param payload the payload in cleartext to be authenticated
	 * @return true if the payload is authentic of false otherwise
	 */
	public boolean authenticate(RSAPublicKey publicKey, String signature, String payload)
	{
		try 
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(payload.getBytes());

			// Decrypt the signature using the signer's public key
			byte[] signatureRaw = Base64Decode(signature);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] sign = cipher.doFinal(signatureRaw);

			// Compare the hash with the signature
			return Arrays.equals(hash, sign);
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} 
		catch (NoSuchPaddingException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidKeyException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalBlockSizeException e) 
		{
			e.printStackTrace();
		} 
		catch (BadPaddingException e) 
		{
			e.printStackTrace();
		}

		return false;
	}
}
