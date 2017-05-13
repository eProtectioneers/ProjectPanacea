package org.eprotectioneers.panacea.contactmanagement.models;

import java.io.*;
import java.util.Comparator;
import javax.swing.JOptionPane;

import org.eprotectioneers.panacea.contactmanagement.view.Page_Contact;


public class Contact implements Serializable {
	
	private static final String defaultpicpath=new File("images/EmptyProfile.png").getAbsolutePath();
	private final int id;
	private String _shownname;
	private String _firstname;
	private String _lastname;
	private String _emailaddress;
	private String _phonenumber;
	private String _address;
	private String _picturepath;
	private boolean _spam;
	
	/**
	 * @return the default picture path
	 */
	public static String getDefaultpicpath() {
		return defaultpicpath;
	}
	/**
	 * @return the id of the contact
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the shown name of the contact
	 */
	public String getShownname() {
		return _shownname;
	}
	/**
	 * Sets the
	 * @param _shownname of the contact
	 */
	public void setShownname(String shownname) {
		this._shownname = shownname;
	}
	/**
	 * @return the first name of the contact
	 */
	public String getFirstname() {
		return this._firstname;
	}
	/**
	 * sets the
	 * @param firstname of the contact
	 */
	public void setFirstname(String firstname) {
		this._firstname = firstname;
	}
	/**
	 * @return the last name of the contact
	 */
	public String getLastname() {
		return this._lastname;
	}
	/**
	 * sets the
	 * @param lastname of the contact
	 */
	public void setLastname(String lastname) {
		this._lastname = lastname;
	}
	/**
	 * @return the email address of the contact
	 */
	public String getEmailaddress() {
		return this._emailaddress;
	}
	/**
	 * sets the
	 * @param emailaddress of the contact
	 */
	public void setEmailaddress(String emailaddress){
		this._emailaddress=emailaddress;
	}
	/**
	 * @return the phone number of the contact
	 */
	public String getPhonenumber() {
		return this._phonenumber;
	}
	/**
	 * sets the
	 * @param phonenumber of the contact
	 */
	public void setPhonenumber(String phonenumber){
		this._phonenumber=phonenumber;
	}
	
	/**
	 * @return the address of the contact
	 */
	public String getAddress() {
		return this._address;
	}
	/**
	 * sets the
	 * @param address of the contact
	 */
	public void setAddress(String address){
		this._address=address;
	}
	
	/**
	 * @return the contact picture of the contact
	 */
	public String getPicturepath() {
		if(this._picturepath!=null)return this._picturepath;
		else return defaultpicpath;
	}
	/**
	 * sets the
	 * @param picturepath of the contacts picture
	 */
	public void setPicturepath(String picturepath) {
		if(picturepath==null||!(new File(picturepath).exists()))picturepath=defaultpicpath;
		this._picturepath = picturepath;
	}
	/**
	 * @return if the contact is spam
	 */
	public boolean isSpam() {
		return _spam;
	}
	/**
	 * sets if the contact is
	 * @param spam
	 */
	public boolean setSpam(boolean spam, boolean text) {
		if(text){
			if(spam){
				Object options[]={"yes", "no"};
				switch(JOptionPane.showOptionDialog(null, "Do you really want to add '"+this.getShownname()+"' to Spam?", "Add to Spam", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1])){
				case JOptionPane.YES_OPTION:
					this._spam=true;
					JOptionPane.showMessageDialog(null, "'"+getShownname()+"' added to Spam", "Spam Update", JOptionPane.INFORMATION_MESSAGE, null);
					break;
				default:
					this._spam=false;
					break;
				}	
			}else {
				JOptionPane.showMessageDialog(null, "'"+getShownname()+"' removed from Spam", "Spam Update", JOptionPane.INFORMATION_MESSAGE, null);
				this._spam = false;
			}
		}else this._spam = spam;
		DatabaseC.updateContact(this);
		return _spam;
	}
	
	/**
	 * constructor of Contact assigns the following values to the Contact:
	 * @param id
	 * @param shownname
	 * @param firstname
	 * @param lastname
	 * @param emailaddress
	 * @param phonenumber
	 * @param address
	 * @param picture
	 * @param spam
	 * @param groups
	 */
	public Contact(int id, String shownname, String firstname, String lastname, String emailaddress,
			String phonenumber, String address, String picturepath, boolean spam) {
		this.id=id;
		if(shownname==null||shownname.equals("")){
			if((shownname=firstname)==null||firstname.equals("")){
				shownname=emailaddress;
			}else if(!(shownname==null||shownname.equals("")))shownname+=" "+lastname;
		}
		this._shownname=shownname;
		this._firstname = firstname;
		this._lastname = lastname;
		this._emailaddress=emailaddress;
		this._phonenumber=phonenumber;
		this._address=address;
		this.setPicturepath(picturepath);
		this._spam=spam;
	}
	
	@Override
	public String toString() {
		String html="<html><body>" +
				"<table>"+
					"<tr>"+
						"<td valign=top>"+
						"<img src='file:///" +
						this.getPicturepath() +
						"' width=50 height=50 />"+
						"</td>"+
						"<td valign=top>"+
			            "<b><u>"+this.getShownname()+"</u></b><br>"+
			            this.getEmailaddress()+"<br>";			
			            if(!(this.getLastname().equals("")||this.getFirstname().equals("")))
			            	html+=this.getFirstname()+" "+this.getLastname()+"<br>";
			            if(!this.getPhonenumber().equals(""))
			            	html+=this.getPhonenumber()+"<br>";
			            if(!this.getAddress().equals(""))
			            	html+=this.getAddress()+"<br>";
			            if(this.isSpam())html+="Spam: &#10004";
			            else html+="Spam: &#10006";
			      html+="</td>"+
		            "</tr>"+
	    		  "</table>";
		return html;
	}
	
	public static class ContactComparator implements Comparator<Contact>{
		@Override
		public int compare(Contact c1, Contact c2) {
			return c1.getShownname().toLowerCase().compareTo(c2.getShownname().toLowerCase());
		}
	}
}
