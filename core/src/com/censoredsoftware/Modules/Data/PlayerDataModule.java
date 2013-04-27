package com.censoredsoftware.Modules.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Modules.Persistence.Event.LoadYAMLEvent;

/**
 * Module to handle data based on a group of OfflinePlayers, holding Objects as data.
 */
public class PlayerDataModule implements DataModule, Listener
{
	// Define HashMaps
	private Map<String, Object> playerData;

	private Plugin plugin;
	private String dataName;

	/**
	 * Create a new instance of the library for the Plugin <code>instance</code>.
	 * 
	 * @param instance The current instance of the plugin running this module.
	 * @param dataName The name of the data set being held in this module.
	 */
	public PlayerDataModule(Plugin instance, String dataName)
	{
		this.playerData = new HashMap<String, Object>();
		this.plugin = instance;
		this.dataName = dataName;

		// Start the load listeners
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		// Create saves for all online players
		for(Player player : Bukkit.getOnlinePlayers())
		{
			createSave(player);
		}
	}

	/**
	 * Create a new instance of the library.
	 */
	public PlayerDataModule()
	{
		this.playerData = new HashMap<String, Object>();

		// Create saves for all online players
		for(Player player : Bukkit.getOnlinePlayers())
		{
			createSave(player);
		}
	}

	/**
	 * Creates a save for <code>player</code>, based on if they already have one or not.
	 * 
	 * @param player The player to save.
	 */
	public void createSave(OfflinePlayer player)
	{
		if(!containsPlayer(player)) playerData.put(player.getName(), new HashMap<String, Object>());
	}

	/**
	 * Checks if the playerData Map contains <code>player</code>.
	 * 
	 * @param player The player in the save.
	 * @return True if playerData contains the player.
	 */
	public boolean containsPlayer(OfflinePlayer player)
	{
		return playerData.get(player.getName()) != null && playerData.containsKey(player.getName());
	}

	/**
	 * Retrieve the Integer data from OfflinePlayer <code>player</code>.
	 * 
	 * @param player The player in the save.
	 * @return Integer data.
	 */
	public int getDataInt(OfflinePlayer player)
	{
		if(containsPlayer(player)) return Integer.parseInt(playerData.get(player.getName()).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the String data from OfflinePlayer <code>player</code>.
	 * 
	 * @param player The player in the save.
	 * @return String data.
	 */
	public String getDataString(OfflinePlayer player)
	{
		if(containsPlayer(player)) return playerData.get(player.getName()).toString();
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Boolean data from OfflinePlayer <code>player</code>.
	 * 
	 * @param player The player in the save.
	 * @return Boolean data.
	 */
	public boolean getDataBool(OfflinePlayer player)
	{
		if(containsPlayer(player)) return Boolean.parseBoolean(playerData.get(player.getName()).toString());
		return false; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Double data from OfflinePlayer <code>player</code>.
	 * 
	 * @param player The player in the save.
	 * @return Double data.
	 */
	public double getDataDouble(OfflinePlayer player)
	{
		if(containsPlayer(player)) return Double.parseDouble(playerData.get(player.getName()).toString());
		return -1.0; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Float data from OfflinePlayer <code>player</code>.
	 * 
	 * @param player The player in the save.
	 * @return Float data.
	 */
	public float getDataFloat(OfflinePlayer player)
	{
		if(containsPlayer(player)) return Float.parseFloat(playerData.get(player).toString());
		return 0F; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Long data from OfflinePlayer <code>player</code>.
	 * 
	 * @param player The player in the save.
	 * @return Long data.
	 */
	public long getDataLong(OfflinePlayer player)
	{
		if(containsPlayer(player)) return Long.parseLong(playerData.get(player.getName()).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Object data from OfflinePlayer <code>player</code>.
	 * 
	 * @param player The player in the save.
	 * @return Object data.
	 */
	public Object getDataObject(OfflinePlayer player)
	{
		if(containsPlayer(player)) return playerData.get(player.getName());
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Save the Object <code>data</code> for OfflinePlayer <code>player</code>.
	 * 
	 * @param player The player in the save.
	 * @param data The Object being saved.
	 */
	public void saveData(OfflinePlayer player, Object data)
	{
		if(!containsPlayer(player)) createSave(player);
		playerData.put(player.getName(), data);
	}

	/**
	 * Remove the data from OfflinePlayer <code>player</code>.
	 * 
	 * @param player The player in the save.
	 */
	public void removeData(OfflinePlayer player)
	{
		if(!containsPlayer(player)) return;
		playerData.remove(player.getName());
	}

	/**
	 * Return the list of players that have data held in this module.
	 * 
	 * @return The list of players.
	 */
	public List<OfflinePlayer> listPlayers()
	{
		List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
		for(Map.Entry<String, Object> entry : playerData.entrySet())
		{
			players.add(Bukkit.getOfflinePlayer(entry.getKey()));
		}
		return players;
	}

	/**
	 * Grab the Map in it's entirely. Can only be accessed from other Modules (to prevent unsafe use).
	 */
	@Override
	public Map<String, Object> getMap()
	{
		return this.playerData;
	}

	@Override
	public void setMap(Map map)
	{
		try
		{
			this.playerData = map;
		}
		catch(Exception ignored)
		{}
	}

	@Override
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLoadYAML(LoadYAMLEvent event)
	{
		if(this.dataName == null) return;
		// Override the data inside of this module with the loaded data if the data name is the same
		if(this.plugin.getName().equals(event.getPluginName()) && this.dataName.equals(event.getDataName())) setMap(event.getData());
	}
}
