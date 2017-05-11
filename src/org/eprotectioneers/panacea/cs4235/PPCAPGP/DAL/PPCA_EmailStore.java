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
	@SuppressWarnings("unused")
	private static PPCA_EmailStore emailStore;
	private List<PPCA_PGPMail> emails;
	public static final int MAX_COUNT = 50;

	/**
	 * Constructor
	 */
	public PPCA_EmailStore() {
		emails = new ArrayList<PPCA_PGPMail>(MAX_COUNT);
	}

	public void add(PPCA_PGPMail email) {
		this.emails.add(0, email);

		while (this.emails.size() > MAX_COUNT) {
			this.emails.remove(this.emails.size() - 1);
		}
	}

	/**
	 * Add a PGPEmail to the store. The newer
	 * 
	 * @param email
	 *            the PGPEmails in chronological order
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
	 * Get PGPEmail.
	 * 
	 * @param index
	 *            the email index
	 * @return the PGPEmail
	 */
	public PPCA_PGPMail getEmail(int index) {
		if (index > emails.size())
			return null;
		return emails.get(index);
	}

	/**
	 * Get all PGPEmail in the store
	 * 
	 * @return the PGPEmail
	 */
	public PPCA_PGPMail[] getEmails() {
		PPCA_PGPMail[] array = new PPCA_PGPMail[emails.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = emails.get(i);
		}
		return array;
	}

	public List<PPCA_PGPMail> getEmailsList() {
		return emails;
	}

	public void setEmailList(List<PPCA_PGPMail> emails) {
		this.emails = emails;
	}
}
