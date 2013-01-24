package com.legit2.Demigods;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.legit2.Demigods.Utilities.DUtil;

import lib.PatPeter.SQLibrary.MySQL;

public class DMySQL
{
	// Define variables
	private static MySQL mysql;
		// Plugin-specific
		public static String plugindata_table = "dg_plugindata";
	
		// Player-specific
		public static String player_table = "dg_players";
		public static String playerdata_table = "dg_playerdata";
		
		// Deity-specific
		public static String deitydata_table = "dg_deitydata";
		
		// Character-specific
		public static String character_table = "dg_characters";
		public static String chardata_table = "dg_chardata";

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
			
			// Create Plugin Data Table
			createTable(
				plugindata_table,
				"entry_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
				"data_id VARCHAR(128)," +
				"datakey VARCHAR(128)," +
				"datavalue VARCHAR(256)"
			);

			// Create Player Table
			createTable(
				player_table,
				"player_id INT NOT NULL PRIMARY KEY," +
				"player_name VARCHAR(24)," +
				"player_characters VARCHAR(256)," +
				"player_kills INT(11)," +
				"player_deaths INT(11)," +
				"player_firstlogin MEDIUMTEXT," +
				"player_lastlogin MEDIUMTEXT"
			);
			
			// Create Player Data Table
			createTable(
				playerdata_table,
				"entry_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
				"player_id INT," +
				"datakey VARCHAR(128)," +
				"datavalue VARCHAR(256)"
			);
			
			// Create Character Table
			createTable(
				character_table,
				"char_id INT NOT NULL PRIMARY KEY," +
				"player_id INT," +
				"char_active TINYINT(1)," +
				"char_name VARCHAR(14)," +
				"char_deity VARCHAR(24)," +
				"char_alliance VARCHAR(24)," +
				"char_immortal TINYINT(1)," +
				"char_hp INT(11)," +
				"char_exp FLOAT," +
				"char_lastX DOUBLE," +
				"char_lastY DOUBLE," +
				"char_lastZ DOUBLE," +
				"char_lastW VARCHAR(24)," +
				"char_favor INT(11)," +
				"char_devotion INT(11)," +
				"char_ascensions INT(11)"
			);
			
			// Create Character Data Table
			createTable(
				chardata_table,
				"entry_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
				"char_id INT," +
				"datakey VARCHAR(48)," +
				"datavalue VARCHAR(256)"
			);
			
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
		DUtil.info("Disabling MySQL...");
		mysql.close();
		DUtil.info("MySQL disabled!");
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
		int port = DConfig.getSettingInt("database.mysql.port");
		
		mysql = new MySQL(Logger.getLogger("Minecraft"), "[Demigods] ", host, port, db_name, username, password);
		
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
		if(!mysql.isTable(table))
		{
			// Log operation to console
			DUtil.info("Creating table \"" + table + "\"");
			
			// Create the table
			String query = "CREATE TABLE " + table + " (" + options + ");";
			try
			{
				mysql.query(query);
			}
			catch(SQLException e) 
			{
				DUtil.severe("There was a problem with creating table: " + table);
				e.printStackTrace();
			}
			
			// Table creation successful
			DUtil.info("Table \"" + table + "\" created!");
		}
		else
		{
			// do nothing
		}
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

