package com.censoredsoftware.Modules.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Modules.Persistence.Event.LoadFileEvent;

/**
 * Module to handle data based on a group of Strings, holding Objects as data.
 */
public class StringDataModule implements DataModule, Listener
{
	// Define HashMaps
	private Map<String, Object> stringData;

	private Plugin plugin;
	private String dataName;

	/**
	 * Create a new instance of the library for the Plugin <code>instance</code>.
	 * 
	 * @param instance The current instance of the plugin running this module.
	 * @param dataName The name of the data set being held in this module.
	 */
	public StringDataModule(Plugin instance, String dataName)
	{
		this.stringData = new HashMap<String, Object>();
		this.plugin = instance;
		this.dataName = dataName;

		// Start the load listeners
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Create a new instance of the library.
	 */
	public StringDataModule()
	{
		this.stringData = new HashMap<String, Object>();
	}

	/**
	 * Creates a save for <code>key</code>, based on if they already have one or not.
	 * 
	 * @param key The key to save.
	 */
	public void createSave(String key)
	{
		if(!containsKey(key)) stringData.put(key, new HashMap<String, Object>());
	}

	/**
	 * Checks if the stringData Map contains <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return True if stringData contains the key.
	 */
	public boolean containsKey(String key)
	{
		return stringData.get(key) != null && stringData.containsKey(key);
	}

	/**
	 * Retrieve the Integer data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Integer data.
	 */
	public int getDataInt(String key)
	{
		if(containsKey(key)) return Integer.parseInt(stringData.get(key).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the String data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return String data.
	 */
	public String getDataString(String key)
	{
		if(containsKey(key)) return stringData.get(key).toString();
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Boolean data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Boolean data.
	 */
	public boolean getDataBool(String key)
	{
		if(containsKey(key)) return Boolean.parseBoolean(stringData.get(key).toString());
		return false; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Double data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Double data.
	 */
	public double getDataDouble(String key)
	{
		if(containsKey(key)) return Double.parseDouble(stringData.get(key).toString());
		return -1.0; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Float data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Float data.
	 */
	public float getDataFloat(String key)
	{
		if(containsKey(key)) return Float.parseFloat(stringData.get(key).toString());
		return 0F; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Long data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Long data.
	 */
	public long getDataLong(String key)
	{
		if(containsKey(key)) return Long.parseLong(stringData.get(key).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Short data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Short data.
	 */
	public short getDataShort(String key)
	{
		if(containsKey(key)) return Short.parseShort(stringData.get(key).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Object data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Object data.
	 */
	public Object getDataObject(String key)
	{
		if(containsKey(key)) return stringData.get(key);
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Save the Object <code>data</code> for int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @param data The Object being saved.
	 */
	public void saveData(String key, Object data)
	{
		if(!containsKey(key)) createSave(key);
		stringData.put(key, data);
	}

	/**
	 * Remove the data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 */
	public void removeData(String key)
	{
		if(!containsKey(key)) return;
		stringData.remove(key);
	}

	/**
	 * Return the list of keys that have data held in this module.
	 * 
	 * @return The list of keys.
	 */
	public List<String> listKeys()
	{
		List<String> keys = new ArrayList<String>();
		for(Map.Entry<String, Object> entry : stringData.entrySet())
		{
			keys.add(entry.getKey());
		}
		return keys;
	}

	/**
	 * Grab the Map in it's entirely. Can only be accessed from other Modules (to prevent unsafe use).
	 */
	@Override
	public Map<String, Object> getMap()
	{
		return this.stringData;
	}

	@Override
	public void setMap(Map map)
	{
		try
		{
			this.stringData = map;
		}
		catch(Exception ignored)
		{}
	}

	@Override
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLoadFile(LoadFileEvent event)
	{
		if(this.dataName == null) return;
		// Override the data inside of this module with the loaded data if the data name is the same
		if(this.plugin.getName().equals(event.getPluginName()) && this.dataName.equals(event.getDataName())) setMap(event.getData());
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
