package org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eprotectioneers.panacea.cs4235.PGPClient.email.PPCA_PGPMail;

import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectSet;

/**
 * DataRepository Class
 * Handles the data stored in the DB4O Database
 * in a local file "PPCA_Storage"
 * @author eProtectioneers
 */
public class PPCA_DataRepo {

	/**
	 * Database file name
	 */
	public static String DB_NAME = "PPCA_Storage";
	/**
	 * Singleton PPCA_DataRepo
	 */
	public static PPCA_DataRepo instance;

	/**
	 * ObjectContainer
	 */
	private EmbeddedObjectContainer db;
	/**
	 * Map with Strings and RemoteClientKeys
	 */
	private Map<String, PPCA_RemoteClientKey> publicKeys;
	/**
	 * PPCA_Preferences
	 */
	private PPCA_Preferences preferences;
	/**
	 * PPCA_EmailStore
	 */
	private PPCA_EmailStore emailStore;

	/**
	 * Constructor
	 */
	private PPCA_DataRepo() {
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_NAME);
		refreshModels();
	}

	/**
	 * Get instance of the Repository
	 * @return PPCA_DataRepo instance
	 */
	public static PPCA_DataRepo getInstance() {
		if (instance == null) {
			instance = new PPCA_DataRepo();
		}

		return instance;
	}

	/**
	 * Save the EmailRepository
	 * @param emailStore 
	 */
	public void saveEmailStore(PPCA_EmailStore emailStore) {
		db.store(emailStore.getEmailsList());
		db.store(emailStore);
		db.commit();
		refreshModels();
	}

	/**
	 * Getter for the emailstore
	 * @return
	 */
	public PPCA_EmailStore getEmailStore() {
		return emailStore;
	}

	/**
	 * Save the preferences
	 * @param preferences
	 */
	public void savePreferences(PPCA_Preferences preferences) {
		db.store(preferences);
		db.commit();
		refreshModels();
	}

	/**
	 * Getter for the preferences
	 * @return
	 */
	public PPCA_Preferences getPreferences() {
		return preferences;
	}

	/**
	 * Save a new publickey
	 * @param username
	 * @param publicKey
	 */
	public void savePublicKey(String username, String publicKey) {

		PPCA_RemoteClientKey key = publicKeys.get(username);
		if (key != null) {
			key.setKey(publicKey);
		} else {
			key = new PPCA_RemoteClientKey(username, publicKey);
		}

		db.store(key);
		db.commit();
		refreshModels();
	}

	/**
	 * Get a public key
	 * @param username
	 * @return String publicKey
	 */
	public String getPublicKey(String username) {
		PPCA_RemoteClientKey key = publicKeys.get(username);
		if (key != null) {
			return key.getKey();
		}

		return null;
	}

	/**
	 * Delete a public key from the repository
	 * @param username
	 */
	public void deletePublicKey(String username) {
		PPCA_RemoteClientKey key = publicKeys.get(username);
		if (key != null) {
			db.delete(key.getKey());
			db.commit();
			refreshModels();
		}
	}
	
	/**
	 * Close the database connection without commit
	 */
	public static void closeConnection(){
		instance.db.close();
	}

	/**
	 * Get the Keystore
	 * @return List<PPCA_RemoteClientKey>
	 */
	public List<PPCA_RemoteClientKey> getKeyStore() {
		return new ArrayList<PPCA_RemoteClientKey>(publicKeys.values());
	}

	/**
	 * Refresh the models
	 */
	private void refreshModels() {

		// refresh Preferences
		ObjectSet<PPCA_Preferences> result = db.queryByExample(PPCA_Preferences.class);
		if (result.hasNext()) {
			preferences = (PPCA_Preferences) result.next();
		} else {
			preferences = new PPCA_Preferences();
			db.store(preferences);
			db.commit();
		}

		// refresh Public Keys;

		publicKeys = new HashMap<String, PPCA_RemoteClientKey>();

		List<PPCA_RemoteClientKey> keys = db.queryByExample(PPCA_RemoteClientKey.class);
		if (keys.size() > 0) {
			for (PPCA_RemoteClientKey k : keys) {
				publicKeys.put(k.getUsername(), k);
			}
		}

		// refresh EmailStore
		emailStore = new PPCA_EmailStore();
		List<PPCA_PGPMail> emails = new ArrayList<PPCA_PGPMail>();
		emailStore.setEmailList(emails);
		
		ObjectSet<PPCA_EmailStore> result2 = db.queryByExample(emailStore);
		if (result2.hasNext()) {
			emailStore = result2.next();
		} else {
			
			db.store(emailStore);
			db.commit();
		}
	}

	/**
	 * Simple run test
	 * @param args unused
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		PPCA_DataRepo or = PPCA_DataRepo.getInstance();
		PPCA_EmailStore es = or.getEmailStore();
	}
}
