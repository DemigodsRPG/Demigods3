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
 * Module to handle tiered data based on a group of OfflinePlayers, holding Objects as data.
 */
public class TieredPlayerDataModule implements DataModule, Listener
{
	// Define HashMaps
	private Map<String, HashMap<String, Object>> tieredObjectData;

	private Plugin plugin;
	private String dataName;

	/**
	 * Create a new instance of the library for the Plugin <code>instance</code>.
	 * 
	 * @param instance The current instance of the plugin running this module.
	 * @param dataName The name of the data set being held in this module.
	 */
	public TieredPlayerDataModule(Plugin instance, String dataName)
	{
		this.tieredObjectData = new HashMap<String, HashMap<String, Object>>();
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
	public TieredPlayerDataModule()
	{
		this.tieredObjectData = new HashMap<String, HashMap<String, Object>>();

		// Create saves for all online players
		for(Player player : Bukkit.getOnlinePlayers())
		{
			createSave(player);
		}
	}

	/**
	 * Creates a save for <code>tier</code>, based on if they already have one or not.
	 * 
	 * @param tier The tier to save.
	 */
	public void createSave(OfflinePlayer tier)
	{
		if(!containsTier(tier)) tieredObjectData.put(tier.getName(), new HashMap<String, Object>());
	}

	/**
	 * Creates a save for <code>tier</code> with key <code>key</code>, based on if they already have one or not.
	 * 
	 * @param tier The tier to save.
	 * @param key The key to save.
	 */
	public void createSave(OfflinePlayer tier, String key)
	{
		if(!containsKey(tier, key)) tieredObjectData.get(tier.getName()).put(key, new HashMap<String, Object>());
	}

	/**
	 * Checks if the tieredObjectData Map contains <code>tier</code>.
	 * 
	 * @param tier The tier in the save.
	 * @return True if tieredObjectData contains the tier.
	 */
	public boolean containsTier(OfflinePlayer tier)
	{
		return tieredObjectData.get(tier.getName()) != null && tieredObjectData.containsKey(tier.getName());
	}

	/**
	 * Checks if the tieredObjectData Map contains <code>tier</code> and <code>key</code>.
	 * 
	 * @param tier The tier in the save.
	 * @param key The key in the save.
	 * @return True if tieredObjectData contains the tier.
	 */
	public boolean containsKey(OfflinePlayer tier, String key)
	{
		return containsTier(tier) && tieredObjectData.get(tier.getName()).get(key) != null && tieredObjectData.get(tier.getName()).containsKey(key);
	}

	/**
	 * Retrieve the Integer data from int <code>tier</code>.
	 * 
	 * @param tier The tier in the save.
	 * @return Integer data.
	 */
	public int getDataInt(OfflinePlayer tier, String key)
	{
		if(containsKey(tier, key)) return Integer.parseInt(tieredObjectData.get(tier.getName()).get(key).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the String data from int <code>tier</code>.
	 * 
	 * @param tier The tier in the save.
	 * @return String data.
	 */
	public String getDataString(OfflinePlayer tier, String key)
	{
		if(containsKey(tier, key)) return tieredObjectData.get(tier.getName()).get(key).toString();
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Boolean data from int <code>tier</code>.
	 * 
	 * @param tier The tier in the save.
	 * @return Boolean data.
	 */
	public boolean getDataBool(OfflinePlayer tier, String key)
	{
		if(containsKey(tier, key)) return Boolean.parseBoolean(tieredObjectData.get(tier.getName()).get(key).toString());
		return false; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Double data from int <code>tier</code>.
	 * 
	 * @param tier The tier in the save.
	 * @return Double data.
	 */
	public double getDataDouble(OfflinePlayer tier, String key)
	{
		if(containsKey(tier, key)) return Double.parseDouble(tieredObjectData.get(tier.getName()).get(key).toString());
		return -1.0; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Float data from int <code>tier</code> with <code>key</code>.
	 * 
	 * @param tier The tier in the save.
	 * @param key The key in the save.
	 * @return Float data.
	 */
	public float getDataFloat(OfflinePlayer tier, String key)
	{
		if(containsKey(tier, key)) return Float.parseFloat(tieredObjectData.get(tier.getName()).get(key).toString());
		return 0F; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Long data from int <code>tier</code>.
	 * 
	 * @param tier The tier in the save.
	 * @return Long data.
	 */
	public long getDataLong(OfflinePlayer tier, String key)
	{
		if(containsKey(tier, key)) return Long.parseLong(tieredObjectData.get(tier.getName()).get(key).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Object data from int <code>tier</code>.
	 * 
	 * @param tier The tier in the save.
	 * @return Object data.
	 */
	public Object getDataObject(OfflinePlayer tier, String key)
	{
		if(containsKey(tier, key)) return tieredObjectData.get(tier.getName()).get(key);
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Save the Object <code>data</code> for int <code>tier</code>.
	 * 
	 * @param tier The tier in the save.
	 * @param data The Object being saved.
	 */
	public void saveData(OfflinePlayer tier, String key, Object data)
	{
		if(!containsTier(tier)) createSave(tier);
		else if(!containsKey(tier, key)) createSave(tier, key);
		tieredObjectData.get(tier.getName()).put(key, data);
	}

	/**
	 * Remove the data from int <code>tier</code>.
	 * 
	 * @param tier The tier in the save.
	 */
	public void removeData(OfflinePlayer tier, String key)
	{
		if(!containsKey(tier, key)) return;
		tieredObjectData.get(tier.getName()).remove(key);
	}

	/**
	 * Return the list of tiers that have data held in this module.
	 * 
	 * @return The list of keys.
	 */
	public List<OfflinePlayer> listTiers()
	{
		List<OfflinePlayer> tiers = new ArrayList<OfflinePlayer>();
		for(String tier : tieredObjectData.keySet())
		{
			tiers.add(Bukkit.getOfflinePlayer(tier));
		}
		return tiers;
	}

	/**
	 * Return the list of keys that have data held in this module under a <code>tier</code>.
	 * 
	 * @param tier The tier being checked.
	 * @return The list of keys.
	 */
	public List<String> listKeys(OfflinePlayer tier)
	{
		List<String> keys = new ArrayList<String>();
		for(String key : tieredObjectData.get(tier.getName()).keySet())
		{
			keys.add(key);
		}
		return keys;
	}

	/**
	 * Grab the Map in it's entirely. Can only be accessed from other Modules (to prevent unsafe use).
	 */
	@Override
	public Map<String, HashMap<String, Object>> getMap()
	{
		return this.tieredObjectData;
	}

	@Override
	public void setMap(Map map)
	{
		try
		{
			this.tieredObjectData = map;
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
		if(this.plugin.equals(event.getPluginName()) && this.dataName.equals(event.getDataName())) setMap(event.getData());
	}
}
