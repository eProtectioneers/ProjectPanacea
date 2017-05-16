package org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * UNUSED -> Using DB4Objects instead! (Future Release!)
 * This class represents a Data Access Layer to MySQL database.
 * @author eProtectioneers
 */
public class PPCA_LocalDBConnector extends PPCA_DBConnector 
{
	/**
	 * Notes:
	 * Any SQL statement should be queried in PreparedStatement
	 * to prevent SQL injection.
	 */

	/**
	 * Singleton PPCA_LocalDBConnector
	 */
	private static PPCA_LocalDBConnector dbConnector;

	/**
	 * Server connection string
	 */
	private final String SERVER = "jdbc:mysql://<<ipaddress>>/<<dbName>>";
	/**
	 * Username
	 */
	private final String USERNAME = "<<username>>";
	/**
	 * Password
	 */
	private final String PASSWORD = "<<password>>";
	
	/**
	 * Constructor
	 */
	private PPCA_LocalDBConnector()
	{
		
	}
	
	/**
	 * Get the Singleton instance
	 * @return PPCA_DBConnector instance
	 */
	public static PPCA_DBConnector getInstance() 
	{
		if (dbConnector == null)
			dbConnector = new PPCA_LocalDBConnector();
		return dbConnector;
	}

	/**
	 * Connect the database
	 */
	@Override
	public void connect() throws SQLException 
	{
		try 
		{
			/* Load JDBC Driver for MySQL */
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(SERVER, USERNAME, PASSWORD);
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (InstantiationException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e) 
		{
			throw e;
		}	
	}
	
	/**
	 * Get a public key.
	 * @param userid the user id associated with this public key
	 * @return the public key associated with the given user id
	 */
	public PublicKey getPublicKey(String userid)
	{
		return null;
	}
	
	/**
	 * Get a private key.
	 * @param userid the user id associated with this private key
	 * @return the private key associated with the given user id
	 */
	public PrivateKey getPrivateKey(String userid)
	{
		return null;
	}
	
	/**
	 * Get a passphrase.
	 * @param userid the user id of the public key
	 * @return the passphrase
	 */
	public String getPassphrase(String userid)
	{
		return null;
	}
		
	/**
	 * Store a public key with the given user id to the database.
	 * @param userid the user id of the public key
	 * @return true on success or false otherwise
	 */
	public boolean storePublicKey(String userid)
	{
		return false;
	}
	
	/**
	 * Store a private key with the given user id to the database.
	 * @param userid the user if of the private key
	 * @return true on success or false otherwise
	 */
	public boolean storePrivateKey(String userid)
	{
		return false;
	}
	
	/**
	 * Store a passphrase with the given user id to the database.
	 * @param userid the user id of the public key
	 * @return true on success or false otherwise
	 */
	public boolean storePassphrase(String userid)
	{
		return false;
	}
	
	/**
	 * Save user's preference.
	 * @param preference the user's preference
	 * @return true on success or false otherwise
	 */
	public boolean saveUserPreference(PPCA_Preferences preference)
	{
		return false;
	}
	
	/**
	 * Retrive user's preference
	 * @param preference the user's preference
	 * @return true on success or false otherwise
	 */
	public boolean retrieveUserPreference(PPCA_Preferences preference)
	{
		return false;
	}
	
	
}
