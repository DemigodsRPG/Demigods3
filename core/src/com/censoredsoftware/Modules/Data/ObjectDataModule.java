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
 * Module to handle data based on a group of Objects, holding Objects as data.
 */
public class ObjectDataModule implements DataModule, Listener
{
	// Define HashMaps
	private Map<Object, Object> objectData;

	private Plugin plugin;
	private String dataName;

	/**
	 * Create a new instance of the library for the Plugin <code>instance</code>.
	 * 
	 * @param instance The current instance of the plugin running this module.
	 * @param dataName The name of the data set being held in this module.
	 */
	public ObjectDataModule(Plugin instance, String dataName)
	{
		this.objectData = new HashMap<Object, Object>();
		this.plugin = instance;
		this.dataName = dataName;

		// Start the load listeners
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Create a new instance of the library.
	 */
	public ObjectDataModule()
	{
		this.objectData = new HashMap<Object, Object>();
	}

	/**
	 * Creates a save for <code>key</code>, based on if they already have one or not.
	 * 
	 * @param key The key to save.
	 */
	public void createSave(Object key)
	{
		if(!containsKey(key)) objectData.put(key, new HashMap<String, Object>());
	}

	/**
	 * Checks if the objectData Map contains <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return True if objectData contains the key.
	 */
	public boolean containsKey(Object key)
	{
		return objectData.get(key) != null && objectData.containsKey(key);
	}

	/**
	 * Retrieve the Integer data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Integer data.
	 */
	public int getDataInt(Object key)
	{
		if(containsKey(key)) return Integer.parseInt(objectData.get(key).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the String data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return String data.
	 */
	public String getDataString(Object key)
	{
		if(containsKey(key)) return objectData.get(key).toString();
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Boolean data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Boolean data.
	 */
	public boolean getDataBool(Object key)
	{
		if(containsKey(key)) return Boolean.parseBoolean(objectData.get(key).toString());
		return false; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Double data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Double data.
	 */
	public double getDataDouble(Object key)
	{
		if(containsKey(key)) return Double.parseDouble(objectData.get(key).toString());
		return -1.0; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Float data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Float data.
	 */
	public float getDataFloat(Object key)
	{
		if(containsKey(key)) return Float.parseFloat(objectData.get(key).toString());
		return 0F; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Long data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Long data.
	 */
	public long getDataLong(Object key)
	{
		if(containsKey(key)) return Long.parseLong(objectData.get(key).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Object data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Object data.
	 */
	public Object getDataObject(Object key)
	{
		if(containsKey(key)) return objectData.get(key);
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Save the Object <code>data</code> for int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @param data The Object being saved.
	 */
	public void saveData(Object key, Object data)
	{
		if(!containsKey(key)) createSave(key);
		objectData.put(key, data);
	}

	/**
	 * Remove the data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 */
	public void removeData(Object key)
	{
		if(!containsKey(key)) return;
		objectData.remove(key);
	}

	/**
	 * Return the list of keys that have data held in this module.
	 * 
	 * @return The list of keys.
	 */
	public List<Object> listKeys()
	{
		List<Object> keys = new ArrayList<Object>();
		for(Map.Entry<Object, Object> entry : objectData.entrySet())
		{
			keys.add(entry.getKey());
		}
		return keys;
	}

	/**
	 * Grab the Map in it's entirely. Can only be accessed from other Modules (to prevent unsafe use).
	 */
	@Override
	public Map<Object, Object> getMap()
	{
		return this.objectData;
	}

	@Override
	public void setMap(Map map)
	{
		try
		{
			this.objectData = map;
		}
		catch(Exception ignored)
		{}
	}

	@Override
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLoadYAML(LoadFileEvent event)
	{
		if(this.dataName == null) return;
		// Override the data inside of this module with the loaded data if the data name is the same
		if(this.plugin.getName().equals(event.getPluginName()) && this.dataName.equals(event.getDataName())) setMap(event.getData());
	}
}
