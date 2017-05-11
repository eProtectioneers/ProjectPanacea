package org.eprotectioneers.panacea.contactmanagement.models;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseCG {
	private static String dbUrl=DatabaseC.getDbUrl();

	public String getDbUrl() {
		return dbUrl;
	}
	
	
	public static boolean AddContactToGroup(Contact c, Group g){
		boolean success=true;
		String INSERT_DATA_SQL="INSERT INTO Contact_Group(CID,GID) VALUES (?,?)";
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
	            DriverManager.getConnection(dbUrl);
	            PreparedStatement pstmt = con.prepareStatement(INSERT_DATA_SQL)) {
        	pstmt.setInt(1, c.getId());
        	pstmt.setInt(2, g.getId());
        	pstmt.executeUpdate();
	    } catch (SQLException e){success=false;}
		return success;
	}
	
	public static boolean RemoveContactFromGroup(Contact c, Group g){
		boolean success=true;
		String DELETE_RELATIONSHIP_SQL="DELETE FROM Contact_Group WHERE CID="+c.getId()+" and GID="+g.getId();
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
	            DriverManager.getConnection(dbUrl);
	            Statement stmt = con.createStatement()) {
			stmt.execute(DELETE_RELATIONSHIP_SQL);
	    } catch (SQLException e){success=false;}
		return success;
	}
	
	public static ArrayList<Integer> getContacts(Group g){
		ArrayList<Integer> ret=new ArrayList<Integer>();
		String GET_CONTACTS_SQL="SELECT CID FROM Contact_Group WHERE GID="+g.getId();
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
			    DriverManager.getConnection(dbUrl);
			    PreparedStatement pstmt = con.prepareStatement(GET_CONTACTS_SQL);
			    ResultSet rs = pstmt.executeQuery()) {
		    while (rs.next()) {
		    	ret.add(rs.getInt(1));
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    return ret;
	}
	
	public static ArrayList<Integer> getContacts(int id){
		ArrayList<Integer> ret=new ArrayList<Integer>();
		String GET_CONTACTS_SQL="SELECT CID FROM Contact_Group WHERE GID="+id;
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
			    DriverManager.getConnection(dbUrl);
			    PreparedStatement pstmt = con.prepareStatement(GET_CONTACTS_SQL);
			    ResultSet rs = pstmt.executeQuery()) {
		    while (rs.next()) {
		    	ret.add(rs.getInt(1));
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    return ret;
	}
	
	public static ArrayList<Integer> getGroups(Contact c){
		ArrayList<Integer> ret=new ArrayList<Integer>();
		String GET_CONTACTS_SQL="SELECT GID FROM Contact_Group WHERE CID="+c.getId();
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
		    DriverManager.getConnection(dbUrl);
		    PreparedStatement pstmt = con.prepareStatement(GET_CONTACTS_SQL);
		    ResultSet rs = pstmt.executeQuery()) {
		    while (rs.next()) {
		    	ret.add(rs.getInt(1));
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    return ret;
	}
	
	public static ArrayList<Integer> getGroups(int id){
		ArrayList<Integer> ret=new ArrayList<Integer>();
		String GET_CONTACTS_SQL="SELECT GID FROM Contact_Group WHERE CID="+id;
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
		    DriverManager.getConnection(dbUrl);
		    PreparedStatement pstmt = con.prepareStatement(GET_CONTACTS_SQL);
		    ResultSet rs = pstmt.executeQuery()) {
		    while (rs.next()) {
		    	ret.add(rs.getInt(1));
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    return ret;
	}
}
