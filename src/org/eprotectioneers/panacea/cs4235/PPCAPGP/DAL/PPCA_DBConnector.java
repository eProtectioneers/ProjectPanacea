//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents a basic Data Access Layer to store data into
 * MySQL database. Any call to method in this class should be restricted
 * to internal usage due to security issue. Another class should
 * extend this class to provide interface to application. 
 * @author eProtectioneers
 */
public abstract class PPCA_DBConnector 
{
	protected Connection conn;

	/**
	 * Establish connection to the database.
	 * @return true on success or false otherwise
	 * @throws SQLException error in establishing a connection
	 */
	public abstract void connect() throws SQLException;

	/**
	 * Close the connection to the database.
	 */
	public void close() throws SQLException
	{
		try 
		{
			conn.close();
		} 
		catch (SQLException e) 
		{
			throw e;
		}
	}

	/**
	 * Check whether there is an active connection or not.
	 * @return true if there is an active connection or false otherwise
	 * @throws SQLException 
	 */
	protected boolean isConnected() throws SQLException
	{
		try 
		{
			if (conn == null || conn.isClosed())
				return false;
			return true;
		} 
		catch (SQLException e) 
		{
			throw e;
		}
	}

	/**
	 * Execute the SQL statement.
	 * @param query the SQL statement
	 * @return the result the result of SQL statement
	 * @throws SQLException 
	 */
	private ResultSet executeQuery(String query) throws SQLException 
	{
		ResultSet result = null;
		PreparedStatement statement = null;
		try 
		{
			if (!isConnected())
				connect();
			statement = conn.prepareStatement(query);
			result = statement.executeQuery();
			statement.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			throw e;
		}
		return result;
	}

	/**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement.  
	 * @param query the SQL statement
	 * @return true on success or false otherwise
	 * @throws SQLException 
	 */
	protected boolean executeUpdate(String query) throws SQLException 
	{
		int row = 0;
		PreparedStatement statement = null;
		try 
		{
			if (!isConnected())
				connect();
			statement = conn.prepareStatement(query);
			row = statement.executeUpdate();
			statement.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			throw e;
		}
		return row != 0 ? true : false;
	}
	
	/**
	 * Setup the database. This method should not be called from application.
	 * @param databaseName the name of the database
	 * @return true on success or false otherwise
	 * @throws SQLException
	 */
	public boolean setupDatabase(String databaseName) throws SQLException
	{
		return false;
	}

	/**
	 * Reset the database. It will empty all the table exists in the database.
	 * @param databaseName The name of database that will be reset
	 * @return true on success or false otherwise
	 * @throws SQLException 
	 */
	public boolean resetDatabase(String databaseName) throws SQLException 
	{
		boolean ret = true;
		String query = "SELECT table_name FROM information_schema.tables "
						+ "WHERE table_schema = ?";

		try 
		{
			if (!isConnected())
				connect();
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, databaseName);			
			ResultSet tableNames = statement.executeQuery();
			
			if (tableNames != null)
			{

				while (tableNames.next()) 
				{
					query = "TRUNCATE TABLE " + tableNames.getString(1);
					executeUpdate(query);
				}
				tableNames.close();
				conn.close();

			}
		} 
		catch (SQLException e) 
		{
			throw e;
		}
		return ret;
	}

	/**
	 * Create table in database with the given table name.
	 * @param tableName the name of the table
	 * @param args the table definition
	 * @return true on success or false otherwise
	 * @throws SQLException 
	 */
	public boolean createTable(String tableName, String args) throws SQLException 
	{
		String query = "CREATE TABLE " + tableName + " (" + args + ")";
		return executeUpdate(query);
	}

	/**
	 * Clear the specified table.
	 * @param tableName the name of the table.
	 * @return true on success or false otherwise
	 * @throws SQLException 
	 */
	public boolean clearTable (String tableName) throws SQLException 
	{
		String query = "TRUNCATE TABLE " + tableName;
		return executeUpdate(query);
	}

	/**
	 * Remove the specified table from the database.
	 * @param tableName the name of the table
	 * @return true on success or false otherwise
	 * @throws SQLException 
	 */
	public boolean dropTable(String tableName) throws SQLException 
	{
		String query = "DROP TABLE IF EXISTS " + tableName;
		return executeUpdate(query);
	}

	/**
	 * Check if specific table exists in the database or not.
	 * @param tableName the name of the table
	 * @return true if the table exists or false otherwise
	 * @throws SQLException 
	 */
	protected boolean isTableExist(String tableName) throws SQLException 
	{
		String query = "SHOW TABLES";
		ResultSet result = executeQuery(query);
		if (result != null) 
		{
			try 
			{
				while (result.next()) 
				{
					if (result.getString(1).equalsIgnoreCase(tableName)) 
						return true;
				}
				result.close();
				return false;
			} 
			catch (SQLException e) 
			{
				throw e;
			}
		}
		return false;
	}

	/**
	 * Insert a column into the database.
	 * @param tableName the name of the table
	 * @param columnName the name of the column
	 * @param args table definition and/or option
	 * @return true on success or false otherwise
	 * @throws SQLException 
	 */
	protected boolean insertColumn(String tableName, String columnName, String args) throws SQLException 
	{
		String query = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName 
						+ " " + args;
		return executeUpdate(query);
	}

	/**
	 * Remove the specified column from the table.
	 * @param tableName the name of the table
	 * @param columnName the name of the column
	 * @return true on success or false otherwise
	 * @throws SQLException 
	 */
	protected boolean dropColumn(String tableName, String columnName) throws SQLException 
	{

		String query = "ALTER TABLE " + tableName + " DROP " + columnName;
		return executeUpdate(query);
	}

	/**
	 * Check if specific column exists in the table.
	 * @param tableName the name of the table
	 * @param columnName the name of the column
	 * @return true if the column exists in the database or false otherwise
	 * @throws SQLException 
	 */
	protected boolean isColumnExist(String tableName, String columnName) throws SQLException 
	{
		String query = "SHOW COLUMNS FROM " + tableName;
		ResultSet result = executeQuery(query);
		if (result != null)
		{
			try 
			{
				while (result.next()) 
				{
					if (result.getString(1).equalsIgnoreCase(columnName))
						return true;
				}
				result.close();
				return false;
			} 
			catch (SQLException e) 
			{
				throw e;
			}
		}
		return false;
	}
}
