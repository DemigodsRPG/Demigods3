package com.legit2.Demigods.Utilities;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.OfflinePlayer;

import com.legit2.Demigods.DDatabase;

public class DDataUtil 
{
	// Define HashMaps
	private static HashMap<String, HashMap<String, Object>> pluginData = new HashMap<String, HashMap<String, Object>>();
	private static HashMap<String, HashMap<String, Object>> playerData = new HashMap<String, HashMap<String, Object>>();
	private static HashMap<String, HashMap<Integer, HashMap<String, Object>>> charData = new HashMap<String, HashMap<Integer, HashMap<String, Object>>>();

	/* ---------------------------------------------------
	 * Begin Plugin Data Methods
	 * ---------------------------------------------------
	 * 
	 *  savePluginData() : Saves (String)dataID to pluginData HashMap.
	 */
	public static boolean savePluginData(String dataID, String dataKey, Object dataValue)
	{
		dataKey = dataKey.toLowerCase();
		
		if(pluginData.containsKey(dataID))
		{
			pluginData.get(dataID).put(dataKey, dataValue);
			return true;
		}
		else
		{
			pluginData.put(dataID, new HashMap<String, Object>());
			pluginData.get(dataID).put(dataKey, dataValue);
			return true;
		}
	}
	
	/*
	 *  removePluginData() : Removes (String)dataID from pluginData HashMap.
	 */
	public static boolean removePluginData(String dataID, String dataKey)
	{
		dataKey = dataKey.toLowerCase();
		
		if(pluginData.containsKey(dataID))
		{
			pluginData.remove(dataID);
			return true;
		}
		else return false;
	}
	
	/*
	 *  hasPluginData() : Returns true/false according to if (String)dataKey exists for
	 *  (String)dataID.
	 */
	public static boolean hasPluginData(String dataID, String dataKey)
	{
		dataKey = dataKey.toLowerCase();
		
		if(pluginData.containsKey(dataID))
		{
			if(pluginData.get(dataID).get(dataKey) != null) return true;
			else return false;
		}
		else return false;
	}
	
	/*
	 *  getPluginData() : Returns (Object)dataValue for (int)dataID's (String)dataKey.
	 */
	public static Object getPluginData(String dataID, String dataKey)
	{
		dataKey = dataKey.toLowerCase();
		
		if(pluginData.containsKey(dataID))
		{
			if(pluginData.get(dataID) != null) return pluginData.get(dataID).get(dataKey);
			else return null;
		}
		else return null;
	}
	
	/* ---------------------------------------------------
	 * Begin Player Data Methods
	 * ---------------------------------------------------
	 * 
	 *  savePlayerData() : Saves (String)dataKey to (int)playerID HashMap.
	 */
	public static boolean savePlayerData(OfflinePlayer player, String dataKey, Object dataValue)
	{
		String playerName = player.getName();
		dataKey = dataKey.toLowerCase();
		
		if(playerData.containsKey(playerName))
		{
			playerData.get(playerName).put(dataKey, dataValue);
			return true;
		}
		else return false;
	}
	
	/*
	 *  removePlayerData() : Removes (String)dataKey from (int)playerID's HashMap.
	 */
	public static boolean removePlayerData(OfflinePlayer player, String dataKey)
	{
		String playerName = player.getName();
		dataKey = dataKey.toLowerCase();
		
		if(playerData.containsKey(playerName))
		{
			playerData.get(playerName).remove(dataKey);
			return true;
		}
		else return false;
	}
	
	/*
	 *  hasPlayerData() : Returns true/false according to if (String)dataKey exists for (int)playerID.
	 */
	public static boolean hasPlayerData(OfflinePlayer player, String dataKey)
	{
		String playerName = player.getName();
		dataKey = dataKey.toLowerCase();
		
		if(playerData.containsKey(playerName))
		{
			if(playerData.get(playerName).get(dataKey) != null) return true;
			else return false;
		}
		else return false;
	}
	
	/*
	 *  getPlayerData() : Returns (Object)dataValue for (int)playerID's (String)dataKey.
	 */
	public static Object getPlayerData(OfflinePlayer player, String dataKey)
	{
		String playerName = player.getName();
		dataKey = dataKey.toLowerCase();
		
		if(playerData.containsKey(playerName))
		{
			if(playerData.get(playerName).get(dataKey) != null) return playerData.get(playerName).get(dataKey);
			else return null;
		}
		else return null;
	}
	
	/* ---------------------------------------------------
	 * Begin Character Data Methods
	 * ---------------------------------------------------
	 * 
	 *  hasChar() : Returns true/false depending on if the player has a character by the 
	 *  name (String)charName.
	 */
	public static boolean hasChar(OfflinePlayer player, String charName)
	{
		String playerName = player.getName();

		if(charData.containsKey(playerName))
		{
			for(Entry<Integer, HashMap<String, Object>> characters : charData.get(playerName).entrySet())
			{
				for(Entry<String, Object> character : characters.getValue().entrySet())
				{
					if(character.getKey().equalsIgnoreCase("char_name") && ((String) character.getValue()).equalsIgnoreCase(charName)) return true;
				}
			}
		}
		return false;
	}
	
	/*
	 *  charExists() : Returns true/false depening on if the character exists.
	 */
	public static boolean charExists(OfflinePlayer player, String charName)
	{
		String playerName = player.getName();;
		
		if(charData.containsKey(playerName))
		{
			if(charData.get(playerName).containsKey(charName)) return true;
			else return false;
		}
		return false;
	}
	
	/*
	 *  charExistsByID() : Returns true/false depening on if the character exists.
	 */
	public static boolean charExistsByID(OfflinePlayer player, int charID)
	{
		String playerName = player.getName();
		
		if(charData.containsKey(playerName))
		{
			if(charData.get(playerName).containsKey(charID)) return true;
			else return false;
		}
		return false;
	}

	/*
	 *  addChar() : Saves the (int)charID to the charData HashMap.
	 */
	public static boolean addChar(OfflinePlayer player, int charID)
	{
		String playerName = player.getName();;
		charData.get(playerName).put(charID, new HashMap<String, Object>());
		return true;
	}
	
	/*
	 *  removeChar() : Removes the (int)charID from the charData HashMap.
	 */
	public static boolean removeChar(OfflinePlayer player, int charID)
	{
		String playerName = player.getName();;
		charData.get(playerName).remove(charID);
		DDatabase.removeChar(player, charID);
		return true;
	}
	
	/*
	 *  saveCharData() : Saves (String)dataKey to (int)charID HashMap.
	 */
	public static boolean saveCharData(OfflinePlayer player, int charID, String dataKey, Object dataValue)
	{
		String playerName = player.getName();;
		dataKey = dataKey.toLowerCase();
		
		if(charData.containsKey(playerName) && charData.get(playerName).containsKey(charID))
		{
			charData.get(playerName).get(charID).put(dataKey, dataValue);
			return true;
		}
		else return false;
	}
	
	/*
	 *  removeCharData() : Removes (String)dataKey from (int)charID's HashMap.
	 */
	public static boolean removeCharData(OfflinePlayer player, int charID, String dataKey)
	{
		String playerName = player.getName();;
		dataKey = dataKey.toLowerCase();
		
		if(charData.containsKey(playerName) && charData.get(playerName).containsKey(charID))
		{
			charData.get(playerName).get(charID).remove(dataKey);
			return true;
		}
		else return false;
	}
	
	/*
	 *  hashCharData() : Returns true/false according to if (String)dataKey exists for (int)charID.
	 */
	public static boolean hasCharData(OfflinePlayer player, int charID, String dataKey)
	{
		String playerName = player.getName();;
		dataKey = dataKey.toLowerCase();
		
		if(charData.containsKey(playerName) && charData.get(playerName).containsKey(charID))
		{
			if(charData.get(playerName).get(charID).get(dataKey) != null) return true;
			else return false;
		}
		else return false;
	}
	
	/*
	 *  getCharData() : Returns (Object)dataValue for (int)charID's (String)dataKey.
	 */
	public static Object getCharData(OfflinePlayer player, int charID, String dataKey)
	{
		String playerName = player.getName();;
		dataKey = dataKey.toLowerCase();
		
		if(charData.containsKey(playerName) && charData.get(playerName).containsKey(charID))
		{
			if(charData.get(playerName).get(charID).get(dataKey) != null) return charData.get(playerName).get(charID).get(dataKey);
			else return null;
		}
		else return null;
	}
	
	/* ---------------------------------------------------
	 * Begin Miscellaneous Data Methods
	 * ---------------------------------------------------
	 *
	 *  addPlayer() : Saves new (String)username to HashMap playerData.
	 */
	public static boolean addPlayer(OfflinePlayer player, int playerID)
	{
		String playerName = player.getName();

		// Returns false if the player already has the playerData.
		if(newPlayer(player))
		{
			// Creates new player HashMap save.
			playerData.put(playerName, new HashMap<String, Object>());
			charData.put(playerName, new HashMap<Integer, HashMap<String, Object>>());
			return true;
		}
		else return false;
	}
	
	/*
	 *  newPlayer() : Checks to see if (String)username already has HashMap playerData.
	 */
	public static boolean newPlayer(OfflinePlayer player)
	{
		String playerName = player.getName();

		if(playerData.containsKey(playerName)) return false;
		else return true;
	}
	
	/*
	 *  removePlayer() : Removes the (OfflinePlayer)player from the playerData HashMap.
	 */
	public static boolean removePlayer(OfflinePlayer player)
	{
		String playerName = player.getName();;
		playerData.remove(playerName);
		charData.remove(playerName);
		DDatabase.removePlayer(player);
		return true;
	}
	
	/*
	 *  getAllPlayers() : Returns all players in the playerData HashMap.
	 */
	public static HashMap<String, HashMap<String, Object>> getAllPlayers()
	{
		return playerData;
	}
	
	/*
	 *  getAllPlayerData() : Returns all playerData for (Player)player.
	 */
	public static HashMap<String, Object> getAllPlayerData(OfflinePlayer player)
	{
		String playerName = player.getName();;
		return playerData.get(playerName);
	}
	
	/*
	 *  getAllPlayers() : Returns all players in the playerData HashMap.
	 */
	public static HashMap<String, HashMap<Integer, HashMap<String, Object>>> getAllChars()
	{
		return charData;
	}
	
	/*
	 *  getAllCharData() : Returns all charData for (int)charID.
	 */
	public static HashMap<String, Object> getAllCharData(OfflinePlayer player, int charID)
	{
		String playerName = player.getName();;
		return charData.get(playerName).get(charID);
	}
	
	/*
	 *  getAllPlayerChars() : Returns all charData for (int)charID.
	 */
	public static HashMap<Integer, HashMap<String, Object>> getAllPlayerChars(OfflinePlayer player)
	{
		String playerName = player.getName();;
		return charData.get(playerName);
	}
	
	/*
	 *  getAllPluginData() : Returns all pluginData.
	 */
	public static HashMap<String, HashMap<String, Object>> getAllPluginData()
	{
		return pluginData;
	}
}
