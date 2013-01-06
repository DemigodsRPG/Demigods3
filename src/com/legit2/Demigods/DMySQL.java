package com.legit2.Demigods;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import lib.PatPeter.SQLibrary.MySQL;

public class DMySQL
{
	// Define variables
	private static MySQL mysql;
	public static String player_table = "dg_players";
	public static String playerdata_table = "dg_playerdata";
	public static String deitydata_table = "dg_deitydata";
	
	/*
	 *  initializeDatabase() : Sets up the database and creates user table if needed.
	 */
	public static void initializeMySQL()
	{
		DUtil.info("Initializing MySQL...");
				
		if(checkConnection())
		{
			// Success! Tell the world!
			DUtil.info("MySQL Connection Successful!");
			
			createTable(player_table, "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, player VARCHAR(24), alliance varchar(24), deities VARCHAR(256), favor INT(11), ascensions INT(11), kills INT(11), deaths INT(11)");
			createTable(playerdata_table, "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, player VARCHAR(24), datakey VARCHAR(128), datavalue VARCHAR(256)");
			createTable(deitydata_table, "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, player VARCHAR(24), deity VARCHAR(24), datakey VARCHAR(128), datavalue VARCHAR(256)");
		}
		else
		{
			// Connection failed... :(
			DUtil.severe("MySQL Connection Failed!"); 
		}
	}

	/*
	 *  uninitializeDatabase() : Performs final database operations and closes the MySQL connection.
	 */
	public static void uninitializeMySQL()
	{
		closeConnection();
	}
	
	/*
	 *  createConnection() : Establishes a connection to the MySQL database.
	 */
	public static boolean createConnection()
	{
		String db_name = DConfig.getSettingString("database.mysql.db_name");
		String username = DConfig.getSettingString("database.mysql.username");
		String password = DConfig.getSettingString("database.mysql.password");
		String host = DConfig.getSettingString("database.mysql.host");
		String port = DConfig.getSettingString("database.mysql.port");
		
		mysql = new MySQL(Logger.getLogger("Minecraft"), "", host, port, db_name, username, password);
		
		// Initialize handler
		try
		{
			mysql.open();
		}
		catch (Exception e)
		{
			DUtil.severe(e.getMessage());
		}
		
		if(mysql.checkConnection()) return true;
		else return false;
	}
	
	/*
	 *  createTable() : Checks to see if the (String)table exists and if not, create it with (String)options.
	 */
	public static void createTable(String table, String options)
	{
		// Check to see if tables exists, if not then create it
		if(!mysql.checkTable(table))
		{
			// Log operation to console
			DUtil.info("Creating table \"" + table + "\"");
			
			// Create the table
			String query = "CREATE TABLE " + table + " (" + options + ");";
			mysql.createTable(query);
			
			// Table creation successful
			DUtil.info("Table \"" + table + "\" created!");
		}
	}
	
	/*
	 *  closeConnection() : Closes the connection to the MySQL database.
	 */
	public static boolean closeConnection()
	{
		mysql.close();
		return true;
	}
	
	/*
	 *  checkConnection() : Checks the connection to the MySQL database.
	 */
	public static boolean checkConnection()
	{
		if(mysql.checkConnection()) return true;
		else return false;
	}
	
	/*
	 *  runQuery() : Runs the (String)query on the MySQL database.
	 */
	public static ResultSet runQuery(String query)
	{
		try
		{
			return mysql.query(query);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 *  dataExists() : Checks to see if (String)value exists under (String)column in (String)table.
	 */
	public static boolean dataExists(String table, String column, String value)
	{
		String query = "SELECT * FROM " + table + " WHERE " + column + " = '" + value + "';";
		ResultSet result = null;
		
		result = runQuery(query);
		
		try
		{
			if(!result.next()) return false;
			else
			{
				return true;
			}
		}
		catch(SQLException e)
		{
			DUtil.severe("There was an error when checking for existing MySQL data:");
			e.printStackTrace();
		}
		return true;
	}
	
	/*
	 *  multDataExists() : Checks to see if (String)value exists under (String)column in (String)table with multiple values.
	 */
	public static boolean multDataExists(String table, String[] column, String[] value)
	{
		String query = "SELECT * FROM " + table + " WHERE " + column[0] + " = '" + value[0] + "' AND " + column[1] + " = '" + value[1] + "';";
		ResultSet result = null;
		
		result = runQuery(query);
		
		try
		{
			if(!result.next()) return false;
			else
			{
				return true;
			}
		}
		catch(SQLException e)
		{
			DUtil.severe("There was an error when checking for existing MySQL data:");
			e.printStackTrace();
		}
		return true;
	}
	
	/*
	 *  getRows() : Returns the number of rows in a query.
	 */
	public static int getRows(ResultSet result)
	{
		int totalRows = 0;
		try
        {
        	result.last();
	        totalRows = result.getRow();
	        result.beforeFirst();
        }
        catch(Exception ex)
        {
        	return 0;
        }
        return totalRows;  
	}

}

