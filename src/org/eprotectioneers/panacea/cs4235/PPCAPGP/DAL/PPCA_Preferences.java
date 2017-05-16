package org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL;

/**
 * A class containing the user preferences
 * @author eProtectioneers
 */
public class PPCA_Preferences 
{
	/**
	 * Keydirectory
	 */
	private String keyDirectory;
	/**
	 * Publickey File path
	 */
	private String publicKeyFilePath;
	/**
	 * Privatekey File path
	 */
	private String privateKeyFilePath;
	/**
	 * String passphrase
	 */
	private String passphrase;
	/**
	 * String username
	 */
	private String username;
	/**
	 * String password
	 */
	private String password;
	
	/**
	 * Constructor
	 */
	public PPCA_Preferences()
	{
		keyDirectory = "~/PanaceaKeys";
		privateKeyFilePath = "PPCAkey_rsa";
		publicKeyFilePath = privateKeyFilePath + ".pub";
		passphrase = "";
	}

	/**
	 * Get the Keydirectory
	 * @return String key directory
	 */
	public String getKeyDirectory() {
		return keyDirectory;
	}

	/**
	 * Set the keydirectory
	 * @param keyDirectory
	 */
	public void setKeyDirectory(String keyDirectory) {
		this.keyDirectory = keyDirectory;
	}

	/**
	 * Get the publickeyfilepath
	 * @return String pubkeypath
	 */
	public String getPublicKeyFilePath() {
		return publicKeyFilePath;
	}

	/**
	 * Set the publickeyfilepath
	 * @param publicKeyFilePath
	 */
	public void setPublicKeyFilePath(String publicKeyFilePath) {
		this.publicKeyFilePath = publicKeyFilePath;
	}

	/**
	 * Get the privatekeyfilepath
	 * @return String privatekeyFilepath
	 */
	public String getPrivateKeyFilePath() {
		return privateKeyFilePath;
	}

	/**
	 * Set the Privatekeyfilepath
	 * @param privateKeyFilePath
	 */
	public void setPrivateKeyFilePath(String privateKeyFilePath) {
		this.privateKeyFilePath = privateKeyFilePath;
	}

	/**
	 * Get the passphrase
	 * @return String passphrase
	 */
	public String getPassphrase() {
		return passphrase;
	}

	/**
	 * Set the passphrase
	 * @param passphrase
	 */
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	/**
	 * Get the Username
	 * @return String username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set the username
	 * @param username 
	 */
	public void setUsername(String username) {
		if (!username.contains("@gmail.com"))
			username += "@gmail.com";
		
		this.username = username;
	}

	/**
	 * Get the Password
	 * @return String password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
