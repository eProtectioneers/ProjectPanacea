package org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL;

/**
 * Remote client key class
 * @author eProtectioneers
 */
public class PPCA_RemoteClientKey {
	
	/**
	 * username
	 */
	private String username;
	/**
	 * key
	 */
	private String key;
	
	/**
	 * Constructor
	 * @param username
	 * @param key
	 */
	public PPCA_RemoteClientKey(String username, String key) {
		this.username = username;
		this.key = key;
	}
	
	/**
	 * get the username
	 * @return String username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * Set the username 
	 * @param String username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * Get the key
	 * @return String key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * Set the key
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
}
