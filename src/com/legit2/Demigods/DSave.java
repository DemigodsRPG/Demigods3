package com.legit2.Demigods;

import java.util.HashMap;

public class DSave
{
	// Define HashMaps
	private static HashMap<String, HashMap<String, Object>> playerData = new HashMap<String, HashMap<String, Object>>();
	private static HashMap<String, HashMap<String, HashMap<String, Object>>> playerDeityData = new HashMap<String, HashMap<String, HashMap<String, Object>>>();
	
	/*
	 *  isNewPlayer() : Checks to see if (String)username already has HashMap playerData.
	 */
	public static boolean isNewPlayer(String username)
	{
		// Set variables
		username = username.toLowerCase();

		if(playerData.containsKey(username)) return false;
		else return true;
	}
	
	/*
	 *  newPlayer() : Saves new (String)username to HashMap playerData.
	 */
	public static boolean newPlayer(String username)
	{
		// Set variables
		username = username.toLowerCase();

		// Returns false if the player already has the playerData.
		if(isNewPlayer(username))
		{
			// Creates new player HashMap save.
			playerData.put(username, new HashMap<String, Object>());
			playerDeityData.put(username, new HashMap<String, HashMap<String, Object>>());
			
			savePlayerData(username, "favor", 0);
			savePlayerData(username, "ascensions", 0);
			savePlayerData(username, "kills", 0);
			savePlayerData(username, "deaths", 0);
			return true;
		}
		else return false;
	}
	
	/*
	 *  hasData() : Checks to see if data (String)id exists in (String)username HashMap.
	 */
	public static boolean hasData(String username, String id)
	{
		// Set variables
		username = username.toLowerCase();
		id = id.toLowerCase();
		
		if(playerData.get(username) == null) return false;
		if(playerData.get(username).get(id) != null && playerData.get(username).containsKey(id)) return true;
		else return false;
	}
	
	/*
	 *  hasDeityData() : Checks to see if deity data (String)id exists in (String)username HashMap.
	 */
	public static boolean hasDeityData(String username, String deity, String id)
	{
		// Set variables
		username = username.toLowerCase();
		id = id.toLowerCase();
		deity = deity.toLowerCase();

		if(playerDeityData.get(username) == null || playerDeityData.get(username).get(deity) == null) return false;
		else if(playerDeityData.get(username).get(deity).get(id) != null && playerDeityData.get(username).get(deity).containsKey(id)) return true;
		else return true;
	}
	
	/*
	 *  hasDataEqualTo() : Checks to see if data (String)id exists in (String)username HashMap with (Object)playerData.
	 */
	public static boolean hasDataEqualTo(String username, String id, Object data)
	{
		// Set variables
		username = username.toLowerCase();
		id = id.toLowerCase();
		
		if(playerData.get(username).get(id) != null && playerData.get(username).get(id).equals(data)) return true;
		else return false;
	}
	
	/*
	 *  savePlayerData() : Saves (Object)data to (String)username in HashMap with (String)id.
	 */
	public static boolean savePlayerData(String username, String id, Object data)
	{
		// Set variables
		username = username.toLowerCase();
		id = id.toLowerCase();
		
		// Returns false if the player is new.
		if(isNewPlayer(username)) return false;
		
		// Save the data now.
		playerData.get(username).put(id, data);
		
		return true;
	}
	
	/*
	 *  saveDeityData() : Saves (Object)data to (String)username in HashMap with (String)id.
	 */
	public static boolean saveDeityData(String username, String deity, String id, Object data)
	{
		// Set variables
		username = username.toLowerCase();
		id = id.toLowerCase();
		deity = deity.toLowerCase();
		
		// Returns false if the player is new.
		if(isNewPlayer(username)) return false;
		
		if(DUtil.hasDeity(username, deity))
		{			
			// Save the data now.
			if(playerDeityData.get(username).get(deity) != null) playerDeityData.get(username).get(deity).put(id, data);
			else
			{
				HashMap<String, Object> newData = new HashMap<String, Object>();
				newData.put(id, data);
				playerDeityData.get(username).put(deity, newData);
			}
			return true;
		}
		else return false;
	}
	
	/*
	 *  removePlayerData() : Removes (String)id data for (String)username.
	 */
	public static boolean removePlayerData(String username, String id)
	{
		// Set variables
		username = username.toLowerCase();
		id = id.toLowerCase();
				
		// Remove data
		if(hasData(username, id)) playerData.get(username).remove(id);
		
		return true;
	}
	
	/*
	 *  removeAllPlayerData() : Removes all HashMap data for (String)username.
	 */
	public static boolean removeAllPlayerData(String username)
	{
		// Set variables
		username = username.toLowerCase();
				
		// Remove data
		playerData.remove(username);
		return true;
	}
	
	/*
	 *  getPlayerData() : Returns a specific HashMap from (String)username's data with (String)id.
	 */
	public static Object getPlayerData(String username, String id)
	{
		// Set variables
		username = username.toLowerCase();
		id = id.toLowerCase();

		// If player has specific data with correct id, return it
		if(hasData(username, id)) return playerData.get(username).get(id);
		return null;
	}
	
	/*
	 *  getDeityData() : Returns a specific HashMap from (String)username's data with (String)id.
	 */
	public static Object getDeityData(String username, String deity, String id)
	{
		// Set variables
		username = username.toLowerCase();
		id = id.toLowerCase();
		deity = deity.toLowerCase();
		
		// If player has specific data with correct id, return it
		if(hasDeityData(username, deity, id)) return playerDeityData.get(username).get(deity).get(id);
		return null;
	}
	
	/*
	 *  removeDeityData() : Removes (String)id deity ata for (String)username.
	 */
	public static boolean removeDeityData(String username, String deity, String id)
	{
		// Set variables
		username = username.toLowerCase();
		id = id.toLowerCase();
		deity = deity.toLowerCase();
				
		// Remove data
		if(hasDeityData(username, deity, id)) playerDeityData.get(username).get(deity).remove(id);
		
		return true;
	}
	
	/*
	 *  removeAllDeityData() : Removes all deity HashMap data for (String)username.
	 */
	public static boolean removeAllDeityData(String username, String deity)
	{
		// Set variables
		username = username.toLowerCase();
		deity = deity.toLowerCase();
		
		// Remove data
		playerDeityData.get(username).remove(deity);
		return true;
	}
	
	/*
	 *  getPlayerData() : Returns a HashMap of all of (String)username's player data.
	 */
	public static HashMap<String, Object> getAllPlayerData(String username)
	{
		// Set variables
		username = username.toLowerCase();
		
		// If player is not new, return
		if(!isNewPlayer(username)) return playerData.get(username);
		return null;
	}
	
	/*
	 *  getDeityData() : Returns a HashMap of all of (String)username's deity data.
	 */
	public static HashMap<String, HashMap<String, Object>> getAllDeityData(String username)
	{
		// Set variables
		username = username.toLowerCase();
		
		// If player is not new, return
		if(!isNewPlayer(username)) return playerDeityData.get(username);
		return null;
	}
	
	/*
	 *  getAllData() : Returns all HashMaps currently loaded.
	 */
	public static HashMap<String, HashMap<String, Object>> getAllData()
	{
		return playerData;
	}
	
	/*
	 *  removeAllData() : Removes all HashMaps.
	 */
	public static void removeAllData()
	{
		playerData.clear();
		playerDeityData.clear();
	}
}
