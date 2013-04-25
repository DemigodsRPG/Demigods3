package com.censoredsoftware.Modules.DataPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * Module to handle data based on a group of Integers, holding Objects as data.
 */
public class IntegerDataModule extends DataModule implements Listener
{
	// Define HashMaps
	private Map<Integer, Object> integerData = new HashMap<Integer, Object>();

	private Plugin plugin;
	private String dataName;

	/**
	 * Create a new instance of the library for the Plugin <code>instance</code>.
	 * 
	 * @param instance The current instance of the plugin running this module.
	 * @param dataName The name of the data set being held in this module.
	 */
	public IntegerDataModule(Plugin instance, String dataName)
	{
		this.plugin = instance;
		this.dataName = dataName;

		// Start the load listeners
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Create a new instance of the library for the Plugin <code>instance</code>.
	 * 
	 * @param instance The current instance of the plugin running this module.
	 */
	public IntegerDataModule(Plugin instance)
	{
		this.plugin = instance;
	}

	/**
	 * Creates a save for <code>key</code>, based on if they already have one or not.
	 * 
	 * @param key The key to save.
	 */
	public void createSave(int key)
	{
		if(!containsKey(key)) integerData.put(key, new HashMap<String, Object>());
	}

	/**
	 * Checks if the integerData Map contains <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return True if integerData contains the key.
	 */
	public boolean containsKey(int key)
	{
		return integerData.get(key) != null && integerData.containsKey(key);
	}

	/**
	 * Retrieve the Integer data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Integer data.
	 */
	public int getDataInt(int key)
	{
		if(containsKey(key)) return Integer.parseInt(integerData.get(key).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the String data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return String data.
	 */
	public String getDataString(int key)
	{
		if(containsKey(key)) return integerData.get(key).toString();
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Boolean data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Boolean data.
	 */
	public boolean getDataBool(int key)
	{
		if(containsKey(key)) return Boolean.parseBoolean(integerData.get(key).toString());
		return false; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Double data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Double data.
	 */
	public double getDataDouble(int key)
	{
		if(containsKey(key)) return Double.parseDouble(integerData.get(key).toString());
		return -1.0; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Long data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Long data.
	 */
	public long getDataLong(int key)
	{
		if(containsKey(key)) return Long.parseLong(integerData.get(key).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Float data from int <code>key</code>.
	 * 
	 * @param key The tier in the save.
	 * @return Float data.
	 */
	public float getDataFloat(int key)
	{
		if(containsKey(key)) return Float.parseFloat(integerData.get(key).toString());
		return 0F; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Object data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Object data.
	 */
	public Object getDataObject(int key)
	{
		if(containsKey(key)) return integerData.get(key);
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Save the Object <code>data</code> for int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @param data The Object being saved.
	 */
	public void saveData(int key, Object data)
	{
		if(!containsKey(key)) createSave(key);
		integerData.put(key, data);
	}

	/**
	 * Remove the data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 */
	public void removeData(int key)
	{
		if(!containsKey(key)) return;
		integerData.remove(key);
	}

	/**
	 * Return the list of keys that have data held in this module.
	 * 
	 * @return The list of keys.
	 */
	public List<Integer> listKeys()
	{
		List<Integer> keys = new ArrayList<Integer>();
		for(Map.Entry<Integer, Object> entry : integerData.entrySet())
		{
			keys.add(entry.getKey());
		}
		return keys;
	}

	/**
	 * Grab the Map in it's entirely. Can only be accessed from other Modules (to prevent unsafe use).
	 */
	@Override
	Map<Integer, Object> grabMap()
	{
		return this.integerData;
	}

	@Override
	protected void overrideMap(Map map)
	{
		try
		{
			this.integerData = map;
		}
		catch(Exception ignored)
		{}
	}

	@Override
	@EventHandler(priority = EventPriority.LOWEST)
	void onLoadYAML(LoadYAMLEvent event)
	{
		if(this.dataName == null) return;
		// Override the data inside of this module with the loaded data if the data name is the same
		if(this.plugin.equals(event.getPluginName()) && this.dataName.equals(event.getDataName())) overrideMap(event.getData());
	}
}
