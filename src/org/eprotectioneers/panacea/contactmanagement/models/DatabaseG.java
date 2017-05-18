//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.models;

import java.sql.*;
import java.util.*;

/**
 * A class, to handle the table Group from the SQLite db
 * @author eProtectioneers
 */
public class DatabaseG {
	private static String dbUrl=DatabaseC.getDbUrl();

	/**
	 * @return the dbUrl
	 */
	public String getDbUrl() {
		return dbUrl;
	}
	
	/**
	 * Add the
	 * @param group
	 * @return success
	 */
	public static boolean addGroup(Group group){
		boolean success=true;
		String INSERT_DATA_SQL = "INSERT INTO CGroup (ID, groupname, description, picturepath) "
    			+ "VALUES (?,?,?,?)";
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
	            DriverManager.getConnection(dbUrl);
	            PreparedStatement pstmt = con.prepareStatement(INSERT_DATA_SQL)) {
        	pstmt.setInt(1, group.getId());
        	pstmt.setBytes(2, group.getName().getBytes());
        	pstmt.setBytes(3, group.getDescription().getBytes());
        	pstmt.setBytes(4, group.getPicturepath().getBytes());
        	pstmt.executeUpdate();
	    } catch (SQLException e){success=false;}	
		return success;
	}
	
	/**
	 * Remove the
	 * @param group
	 * @return success
	 */
	public static boolean removeGroup(Group group){
		boolean success=true;
		String DELETE_GROUP_SQL = "DELETE FROM CGroup WHERE ID="+group.getId();
		String DELETE_CONTACT_GROUP_SQL = "DELETE FROM Contact_Group WHERE GID="+group.getId();
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
			    DriverManager.getConnection(dbUrl);
				Statement stmt=con.createStatement();){
			stmt.execute(DELETE_GROUP_SQL);
			stmt.execute(DELETE_CONTACT_GROUP_SQL);
	    } catch (SQLException e){success=false;}	
		return success;
	}
	
	/**
	 * @return all the Groups
	 */
	public static ArrayList<Group> getGroups(){
		ArrayList<Group> ret=new ArrayList<Group>();
		String READ_GROUPS_SQL="SELECT * FROM CGroup";
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
			    DriverManager.getConnection(dbUrl);
			    PreparedStatement pstmt = con.prepareStatement(READ_GROUPS_SQL);
			    ResultSet rs = pstmt.executeQuery()) {
		    while (rs.next()) {
		    	String s[]=new String[3];
		    	for(int i=0;i<s.length;i++){
		    		try{
		    			s[i] = new String(rs.getBytes(i+2), "Windows-1252");
		    		}catch(Exception ex){s[i]="";}
		    	}
		    	int id=rs.getInt(1);
		    	ret.add(new Group(id, s[0] ,s[1], s[2]));
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    return ret;
	}
	
	/**
	 * @param ids
	 * @return the Groups with the given ids
	 */
	public static ArrayList<Group> getGroups(ArrayList<Integer> ids){
		ArrayList<Group> ret=new ArrayList<Group>();
		if(!ids.equals(new ArrayList<Integer>())){
			String READ_GROUPS_SQL="SELECT * FROM CGroup WHERE ID IN(";
			for(int i:ids)READ_GROUPS_SQL+=i+",";
			READ_GROUPS_SQL=READ_GROUPS_SQL.substring(0, READ_GROUPS_SQL.length()-1);
			READ_GROUPS_SQL+=")";
			try{
				Class.forName("org.sqlite.JDBC");
			}
			catch(ClassNotFoundException e){
				e.printStackTrace();
			}
			try (Connection con = 
				    DriverManager.getConnection(dbUrl);
				    PreparedStatement pstmt = con.prepareStatement(READ_GROUPS_SQL);
				    ResultSet rs = pstmt.executeQuery()) {
			    while (rs.next()) {
			    	String s[]=new String[3];
			    	for(int i=0;i<s.length;i++){
			    		try{
			    			s[i]=new String(rs.getBytes(i+2), "Windows-1252");
			    		}catch(Exception ex){s[i]="";}
			    	}
			    	int id=rs.getInt(1);
			    	ret.add(new Group(id, s[0] ,s[1], s[2]));
			    }
			} catch (SQLException e) {
			    e.printStackTrace();
			}
		}
	    return ret;
	}
	
	/**
	 * Update a Group
	 * @param newcontact
	 */
	public static void updateGroup(Group newgroup){
		String UDATE_GROUP_SQL="UPDATE CGroup SET "+
											"groupname=?,"+
											"description=?,"+
											"picturepath=? "+
											"WHERE ID=?";
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try (Connection con = 
			    DriverManager.getConnection(dbUrl);
				PreparedStatement pstmt=con.prepareStatement(UDATE_GROUP_SQL);){
			pstmt.setBytes(1, newgroup.getName().getBytes());
			pstmt.setBytes(2, newgroup.getDescription().getBytes());
			pstmt.setBytes(3, newgroup.getPicturepath().getBytes());
			pstmt.setInt(4, newgroup.getId());
			
			pstmt.executeUpdate();	
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * @return the lowest available id
	 */
	public static int getNewIndex(){
		String READ_INDEX_SQL="SELECT ID FROM CGroup WHERE ID=";
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
		        	int i1=rs.getInt(1);
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
