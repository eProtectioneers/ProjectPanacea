package org.eprotectioneers.panacea.contactmanagement.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.util.*;

public class DatabaseC {
	private static final String dbUrl=getUrlPath("jdbc:sqlite:"+DatabaseC.class.getResource("/models/").getPath()+"Database/contact.db");
	
	public static String getUrlPath(String s){
		s=s.replaceAll("[+]", "PlUs9q5w1");
		try {
			s=URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s.replaceAll("PlUs9q5w1", "+");
	}
	
	public static String getDbUrl() {
		return getUrlPath("jdbc:sqlite:"+DatabaseC.class.getResource("/models/Database/contact.db").getPath());
	}

	public static boolean addContact(Contact contact){
		boolean success=true;
		String INSERT_DATA_SQL = "INSERT INTO Contact (ID, shownname, firstname, lastname, emailaddress, phonenumber, address, picturepath, spam) "
    			+ "VALUES (?,?,?,?,?,?,?,?,?)";
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
	            DriverManager.getConnection(dbUrl);
	            PreparedStatement pstmt = con.prepareStatement(INSERT_DATA_SQL)) {
        	pstmt.setInt(1, contact.getId());
        	pstmt.setBytes(2, contact.getShownname().getBytes());
        	pstmt.setBytes(3, contact.getFirstname().getBytes());
        	pstmt.setBytes(4, contact.getLastname().getBytes());
        	pstmt.setBytes(5, contact.getEmailaddress().getBytes());
        	pstmt.setBytes(6, contact.getPhonenumber().getBytes());
        	pstmt.setBytes(7, contact.getAddress().getBytes());
        	pstmt.setBytes(8, contact.getPicturepath().getBytes());
        	pstmt.setBoolean(9, contact.isSpam());
        	pstmt.executeUpdate();
	    } catch (SQLException e){success=false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return success;
	}
	
	public static boolean removeContact(Contact contact){
		boolean success=true;
		String DELETE_CONTACT_SQL = "DELETE FROM Contact WHERE ID="+contact.getId();
		String DELETE_CONTACT_GROUP_SQL = "DELETE FROM Contact_Group WHERE CID="+contact.getId();
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
			    DriverManager.getConnection(dbUrl);
				Statement stmt=con.createStatement();){
			stmt.execute(DELETE_CONTACT_SQL);
			stmt.execute(DELETE_CONTACT_GROUP_SQL);
	    } catch (SQLException e){success=false;}	
		return success;
	}
	
	public static ArrayList<Contact> getContacts(){
		ArrayList<Contact> ret=new ArrayList<Contact>();
		String READ_CONTACTS_SQL="SELECT * FROM Contact";
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
			    DriverManager.getConnection(dbUrl);
			    PreparedStatement pstmt = con.prepareStatement(READ_CONTACTS_SQL);
			    ResultSet rs = pstmt.executeQuery()) {
		    while (rs.next()) {
		    	String s[] =new String[7];
		    	for(int i=0;i<s.length;i++) {	
					try{
						s[i] = new String(rs.getBytes(i+2), "Windows-1252");	
					}catch(Exception e){s[i]=""; }
				}
		    	try{
		    		int id=rs.getInt(1);
			    	ret.add(new Contact(id, s[0], s[1], s[2], 
			    			s[3], s[4], s[5], s[6], rs.getBoolean(9)));
		    	}catch(Exception e){}
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    return ret;
	}
	
	public static ArrayList<Contact> getContacts(ArrayList<Integer> ids){
		ArrayList<Contact> ret=new ArrayList<Contact>();
		if(!ids.equals(new ArrayList<Integer>())){
			String READ_CONTACTS_SQL="SELECT * FROM Contact WHERE ID IN(";
			for(int i:ids)READ_CONTACTS_SQL+=i+",";
			READ_CONTACTS_SQL=READ_CONTACTS_SQL.substring(0, READ_CONTACTS_SQL.length()-1);
			READ_CONTACTS_SQL+=")";
			try{
				Class.forName("org.sqlite.JDBC");
			}
			catch(ClassNotFoundException e){
				e.printStackTrace();
			}
			
			try (Connection con = 
				    DriverManager.getConnection(dbUrl);
				    PreparedStatement pstmt = con.prepareStatement(READ_CONTACTS_SQL);
				    ResultSet rs = pstmt.executeQuery()) {
			    while (rs.next()) {
			    	String s[] =new String[7];
			    	for(int i=0;i<s.length;i++) {	
						try{
							s[i] = new String(rs.getBytes(i+2), "Windows-1252");	
						}catch(Exception e){s[i]=""; }
					}
			    	try{
			    		int id=rs.getInt(1);
				    	ret.add(new Contact(id, s[0], s[1], s[2], 
				    			s[3], s[4], s[5], s[6], rs.getBoolean(9)));
			    	}catch(Exception e){}
			    }
			} catch (SQLException e) {
			    e.printStackTrace();
			}
		}
	    return ret;
	}
	
	public static void updateContact(Contact newcontact){
		String UPDATE_CONTACT_SQL="UPDATE Contact SET "+
												"shownname=?,"+
												"firstname=?,"+
												"lastname=?,"+
												"emailaddress=?,"+
												"phonenumber=?,"+
												"address=?,"+
												"picturepath=?,"+
												"spam=? "+
												"WHERE ID=?";
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
			    DriverManager.getConnection(dbUrl);
				PreparedStatement pstmt=con.prepareStatement(UPDATE_CONTACT_SQL);){
			pstmt.setBytes(1, newcontact.getShownname().getBytes());
			pstmt.setBytes(2, newcontact.getFirstname().getBytes());
			pstmt.setBytes(3, newcontact.getLastname().getBytes());
			pstmt.setBytes(4, newcontact.getEmailaddress().getBytes());
			pstmt.setBytes(5, newcontact.getPhonenumber().getBytes());
			pstmt.setBytes(6, newcontact.getAddress().getBytes());
			pstmt.setBytes(7, newcontact.getPicturepath().getBytes());
			pstmt.setBoolean(8, newcontact.isSpam());
			pstmt.setInt(9, newcontact.getId());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	}
	
	public static int getNewIndex(){
		String READ_INDEX_SQL="SELECT ID FROM Contact WHERE ID=";
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		Connection con;
		try {
			con = DriverManager.getConnection(dbUrl);
			PreparedStatement pstmt;
			for(int i=1;i<10000;i++){
		    	pstmt = con.prepareStatement(READ_INDEX_SQL+i);
		        ResultSet rs = pstmt.executeQuery();
		        try{
		        	int i1=rs.getInt(1)+1;
		        }catch(Exception ex){
		        	con.close();
			    	return i;
		        }	
		        pstmt.close();
			} 
		}catch (SQLException e) {
		    e.printStackTrace();
		}
		return (Integer)null;
	}
}
