package org.eprotectioneers.panacea.contactmanagement.models;

import java.io.*;
import java.util.*;

import org.eprotectioneers.panacea.contactmanagement.view.Page_Group;



public class Group implements Serializable{
	
	private static final String defaultpicpath=DatabaseC.getUrlPath(Page_Group.class.getResource("/org/eprotectioneers/panacea/contactmanagement/view/PPNCA_Images/EmptyGroup.png").getPath()).substring(1);
	private final int id;
	private String _name;
	private String _description;
	private String _picturepath;
	
	/**
	 * @return the EmailAddresses of the Contacts in the group
	 */
	public ArrayList<String> getEmailaddresses(){
		ArrayList<String> emailaddresses=new ArrayList<String>();
		for(Contact c:DatabaseC.getContacts(DatabaseCG.getContacts(this))){
			emailaddresses.add(c.getEmailaddress());
		}
		return emailaddresses;		
	}
	
	/**
	 * @return the default picture path
	 */
	public static String getDefaultpicpath() {
		return defaultpicpath;
	}
	/**
	 * @return the id of the group
	 */
	public int getId() {
		return id;
	}	
	

	/**
	 * @return the name of the group
	 */
	public String getName() {
		return _name;
	}
	/**
	 * sets the
	 * @param name of the group
	 */
	public void setName(String name) {
		this._name = name;
	}
	/**
	 * @return the description of the group
	 */
	public String getDescription() {
		return _description;
	}
	/**
	 * sets the
	 * @param description to the group
	 */
	public void setDescription(String description) {
		this._description = description;
	}	
	/**
	 * @return the path of the group's picture
	 */
	public String getPicturepath() {
		if(this._picturepath!=null)return this._picturepath;
		else return defaultpicpath;
	}
	/**
	 * sets the
	 * @param picturepath to the group
	 */
	public void setPicturepath(String picturepath) {
		if(picturepath==null||!(new File(picturepath).exists()))picturepath=defaultpicpath;
		this._picturepath = picturepath;
	}
	
	/**
	 * constructor of Contact assigns the following values to the Group:
	 * @param id
	 * @param groupname
	 * @param description
	 * @param picturepath
	 */
	public Group(int id, String name, String description, String picturepath){
		this.id=id;
		this._name=name;
		this._description=description;
		this.setPicturepath(picturepath);
	}
	
	@Override
	public String toString(){
		String html="<html><body>" +
				"<table>"+
					"<tr>"+
						"<td valign=top>"+
						"<img src='file:///" +
						this.getPicturepath() +
						"' width=50 height=50 />"+
						"</td>"+
						"<td valign=top>"+
			            "<b><u>"+this.getName()+"</u></b><br>";
						if(!this.getDescription().equals(""))
							html+=getDescrHtml();			
			      html+="</td>"+
		            "</tr>"+
	    		  "</table>";
		return html;
	}
	
	private String getDescrHtml(){
		String html=_description;
		int maxlength=40+44*4;
		for(int i=40;i<html.length();i+=44){
			if(i==maxlength){
				html=html.substring(0,maxlength);
				html+="<br>...";
				break;
			}
			if(html.length()>i)html=html.substring(0, i)+"<br>"+html.substring(i,html.length());
		}
		return html;
	}
	
	public static class GroupComparator implements Comparator<Group>{
		@Override
		public int compare(Group g1, Group g2) {
			return g1.getName().toLowerCase().compareTo(g2.getName().toLowerCase());
		}
	}
}
