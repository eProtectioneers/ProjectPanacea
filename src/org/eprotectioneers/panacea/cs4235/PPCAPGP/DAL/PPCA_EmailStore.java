package org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eprotectioneers.panacea.cs4235.PGPClient.email.PPCA_PGPMail;

/**
 * This class represents a data store for Secure Mail
 * @author eProtectioneers
 */
public class PPCA_EmailStore {
	
	/**
	 * emailstore
	 */
	@SuppressWarnings("unused")
	private static PPCA_EmailStore emailStore;
	/**
	 * List of all emails in the Storage
	 */
	private List<PPCA_PGPMail> emails;
	/**
	 * Maximum emails (default is 50)
	 */
	public static final int MAX_COUNT = 50;

	/**
	 * Constructor
	 */
	public PPCA_EmailStore() {
		emails = new ArrayList<PPCA_PGPMail>(MAX_COUNT);
	}

	/**
	 * add a new email to the storage
	 * @param email Email to add
	 */
	public void add(PPCA_PGPMail email) {
		this.emails.add(0, email);

		while (this.emails.size() > MAX_COUNT) {
			this.emails.remove(this.emails.size() - 1);
		}
	}

	/**
	 * Add emails in chronological order to the storage
	 * @param email Array of emails to add
	 */
	public void add(PPCA_PGPMail[] emails) {
		List<PPCA_PGPMail> list = Arrays.asList(emails);
		this.emails.addAll(0, list);

		/* Truncate emails once the list exceed max count */
		while (this.emails.size() > MAX_COUNT) {
			this.emails.remove(this.emails.size() - 1);
		}
	}

	/**
	 * Get a email from the storage
	 * @param index Index of the email
	 * @return PPCA_PGPMail
	 */
	public PPCA_PGPMail getEmail(int index) {
		if (index > emails.size())
			return null;
		return emails.get(index);
	}

	/**
	 * Get all emails in the storage container
	 * @return PPCA_PGPMail Array with all emails
	 */
	public PPCA_PGPMail[] getEmails() {
		PPCA_PGPMail[] array = new PPCA_PGPMail[emails.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = emails.get(i);
		}
		return array;
	}

	/**
	 * Get all emails as a List
	 * @return List<PPCA_PGPMail> emails
	 */
	public List<PPCA_PGPMail> getEmailsList() {
		return emails;
	}

	/**
	 * Set the email list as emails
	 * @param emails List<PPCA_PGPMail>
	 */
	public void setEmailList(List<PPCA_PGPMail> emails) {
		this.emails = emails;
	}
}
