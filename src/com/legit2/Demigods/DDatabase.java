package com.legit2.Demigods;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.google.common.base.Joiner;

public class DDatabase
{
	/*
	 *  initializeDatabase() : Loads the MySQL or SQLite database.
	 */
	public static void initializeDatabase()
	{
		// Check if MySQL is enabled in the configuration and if so, attempts to connect.
		if(DConfig.getSettingBoolean("mysql"))
		{
			DMySQL.createConnection();
			DMySQL.initializeMySQL();
			loadAllData();
		}
		else if(DConfig.getSettingBoolean("sqlite"))
		{
			// TODO: SQLite
		}
	}

	/*
	 *  uninitializeDatabase() : Unloads the MySQL or SQLite database.
	 */
	public static void uninitializeDatabase()
	{
		if(DConfig.getSettingBoolean("mysql") && DMySQL.checkConnection())
		{
			DMySQL.uninitializeMySQL();
		}
		else if(DConfig.getSettingBoolean("sqlite"))
		{
			// TODO: SQLite
		}
	}

	/*
	 *  addPlayer() : Adds the (String)username to the database with default values.
	 */
	public static void addPlayer(String username) throws SQLException
	{
		// First we save the player to a HashMap
		DSave.newPlayer(username);

		// Next we add them to the Database if needed
		if(DConfig.getSettingBoolean("mysql") && DMySQL.checkConnection())
		{	
			if(!DMySQL.dataExists(DMySQL.player_table, "player", username))
			{
				// Setup query string to add player to MySQL database
				String addPlayerQuery = "INSERT INTO " + DMySQL.player_table + " (player, alliance, deities, favor, ascensions, kills, deaths) VALUES ('" + username + "', NULL, NULL, 100, 1, 0, 0);";
				DMySQL.runQuery(addPlayerQuery);
				DUtil.info("User \"" + username + "\" added to database!");
			}
			else
			{
				DUtil.info("User " + username + " has already been added.");
			}
		}
		else if(DConfig.getSettingBoolean("sqlite"))
		{
			// TODO: SQLite
		}
	}

	/*
	 *  getPlayerInfo() : Grabs the player info from MySQL/FlatFile and returns (ResultSet)result.
	 */
	public static ResultSet getPlayerInfo(String username) throws SQLException
	{
		if(DConfig.getSettingBoolean("mysql") && DMySQL.checkConnection())
		{
			// Check to see if user exists
			String query = "SELECT * FROM " + DMySQL.player_table + " WHERE player = '" + username + "';";
			ResultSet result = null;

			result = DMySQL.runQuery(query);

			if(result.next()) return result;
			else
			{
				DUtil.severe(username + " tried to check their info but they aren't entered in the database!");
			}
		}
		else if(DConfig.getSettingBoolean("sqlite"))
		{
			// TODO: SQLite
		}

		return null;
	}

	/*
	 *  saveAllPlayerData() : Saves all player HashMap data to database.
	 */
	public static void saveAllPlayerData()
	{
		if(DConfig.getSettingBoolean("mysql") && DMySQL.checkConnection())
		{
			DUtil.info("Saving player data...");

			// Define variables
			int playerCount = 0;
			long startTimer = System.currentTimeMillis();

			for(Player player : DUtil.getOnlinePlayers())
			{
				// Add 1 to player count
				playerCount++;

				// Define variables
				String username = player.getName();
				HashMap<String, Object> player_data = DSave.getPlayerData(username);	

				// Initialize arrays and stuff					
				for(Map.Entry<String, Object> entry : player_data.entrySet())
				{
					String id = entry.getKey();
					Object data = entry.getValue();

					// Don't save if it's temporary data
					if(id.contains("temp")) continue;

					// Don't save them to the data table if they belong in the user table
					if(!id.equalsIgnoreCase("favor") && !id.equalsIgnoreCase("ascensions") && !id.equalsIgnoreCase("kills") && !id.equalsIgnoreCase("deaths"))
					{
						if(DMySQL.runQuery("UPDATE " + DMySQL.playerdata_table + " SET datavalue='" + data + "' WHERE datakey='" + id + "';") == null)
						{
							DMySQL.runQuery("INSERT INTO " + DMySQL.playerdata_table + " (player, datakey, datavalue) VALUES ('" + username + "', '" + id + "', '" + data + "');");
						}
					}
				}

				String deities = null;
				if(DUtil.getDeities(username) != null) deities = Joiner.on(",").join(DUtil.getDeities(username));
				// Save specific data to user table
				DMySQL.runQuery("UPDATE " + DMySQL.player_table + " SET alliance='" + DSave.getData(username, "alliance") + "', deities='" + deities + "', favor=" + DSave.getData(username, "favor") + ", ascensions=" + DSave.getData(username, "ascensions") + ", kills=" + DSave.getData(username, "kills") + ", deaths=" + DSave.getData(username, "deaths") + " WHERE player='" + username + "';");
			}

			// Stop the timer
			long stopTimer = System.currentTimeMillis();
			double totalTime = (double) (stopTimer - startTimer);

			DUtil.info("Success! Saved " + playerCount + " of " + DMySQL.getRows(DMySQL.runQuery("SELECT * FROM " + DMySQL.player_table + ";")) + " total players in " + totalTime/1000 + " seconds.");

		}
		else if(DConfig.getSettingBoolean("sqlite"))
		{
			// TODO: SQLite
		}
	}
	
	/*
	 *  loadAllData() : Loads all data from database into HashMaps.
	 */
	public static void loadAllData()
	{
		if(DConfig.getSettingBoolean("mysql") && DMySQL.checkConnection())
		{	
			DUtil.info("Loading player data...");

			// Define variables
			int playerCount = 0;
			long startStopwatch = System.currentTimeMillis();

			// Load data from player table.
			String selectAllPlayers = "SELECT * FROM " + DMySQL.player_table + ";";
			ResultSet all_players = DMySQL.runQuery(selectAllPlayers);
			try 
			{
				while(all_players.next())
				{
					playerCount++;

					// Define variables
					String username = all_players.getString("player");
					ArrayList<String> deities = null;
					if(all_players.getString("deities") != null) deities = new ArrayList<String>(Arrays.asList(all_players.getString("deities").split(",")));

					// Add HashMaps
					DSave.newPlayer(username);
					DSave.saveData(username, "favor", all_players.getString("favor"));
					DSave.saveData(username, "ascensions", all_players.getString("ascensions"));
					DSave.saveData(username, "kills", all_players.getString("kills"));
					DSave.saveData(username, "deaths", all_players.getString("deaths"));
					DSave.saveData(username, "alliance", all_players.getString("alliance"));
					DSave.saveData(username, "deities", deities);
				}
			}
			catch(SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Load data from playerdata table.
			String selectAllPlayerData = "SELECT * FROM " + DMySQL.playerdata_table + ";";
			ResultSet all_playerdata = DMySQL.runQuery(selectAllPlayerData);
			try 
			{
				while(all_playerdata.next())
				{					
					String username = all_playerdata.getString("player");

					DSave.saveData(username, all_playerdata.getString("datakey"), all_playerdata.getString("datavalue"));
				}
			}
			catch(SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long stopStopwatch = System.currentTimeMillis();
			double totalTime = (double) (stopStopwatch - startStopwatch);

			DUtil.info("Loaded data for " + playerCount + " players in " + totalTime/1000 + " seconds.");

		}
		else if(DConfig.getSettingBoolean("sqlite"))
		{
			// TODO: SQLite
		}
	}
}