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
 * Module to handle data based on a group of Enums, holding Objects as data.
 */
public class EnumDataModule extends DataModule implements Listener
{
	// Define HashMaps
	private Map<Enum, Object> enumData;

	private Plugin plugin;
	private String dataName;

	/**
	 * Create a new instance of the library for the Plugin <code>instance</code>.
	 * 
	 * @param instance The current instance of the plugin running this module.
	 * @param dataName The name of the data set being held in this module.
	 */
	public EnumDataModule(Plugin instance, String dataName)
	{
		this.enumData = new HashMap<Enum, Object>();
		this.plugin = instance;
		this.dataName = dataName;

		// Start the load listeners
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Create a new instance of the library.
	 */
	public EnumDataModule()
	{
		this.enumData = new HashMap<Enum, Object>();
	}

	/**
	 * Creates a save for <code>key</code>, based on if they already have one or not.
	 * 
	 * @param key The key to save.
	 */
	public void createSave(Enum key)
	{
		if(!containsKey(key)) enumData.put(key, new HashMap<String, Object>());
	}

	/**
	 * Checks if the enumData Map contains <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return True if enumData contains the key.
	 */
	public boolean containsKey(Enum key)
	{
		return enumData.get(key) != null && enumData.containsKey(key);
	}

	/**
	 * Retrieve the Integer data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Integer data.
	 */
	public int getDataInt(Enum key)
	{
		if(containsKey(key)) return Integer.parseInt(enumData.get(key).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the String data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return String data.
	 */
	public String getDataString(Enum key)
	{
		if(containsKey(key)) return enumData.get(key).toString();
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Boolean data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Boolean data.
	 */
	public boolean getDataBool(Enum key)
	{
		if(containsKey(key)) return Boolean.parseBoolean(enumData.get(key).toString());
		return false; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Double data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Double data.
	 */
	public double getDataDouble(Enum key)
	{
		if(containsKey(key)) return Double.parseDouble(enumData.get(key).toString());
		return -1.0; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Float data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Float data.
	 */
	public float getDataFloat(Enum key)
	{
		if(containsKey(key)) return Float.parseFloat(enumData.get(key).toString());
		return 0F; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Long data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Long data.
	 */
	public long getDataLong(Enum key)
	{
		if(containsKey(key)) return Long.parseLong(enumData.get(key).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Short data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Short data.
	 */
	public short getDataShort(Enum key)
	{
		if(containsKey(key)) return Short.parseShort(enumData.get(key).toString());
		return -1; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Retrieve the Object data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Object data.
	 */
	public Object getDataObject(Enum key)
	{
		if(containsKey(key)) return enumData.get(key);
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Save the Object <code>data</code> for int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @param data The Object being saved.
	 */
	public void saveData(Enum key, Object data)
	{
		if(!containsKey(key)) createSave(key);
		enumData.put(key, data);
	}

	/**
	 * Remove the data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 */
	public void removeData(Enum key)
	{
		if(!containsKey(key)) return;
		enumData.remove(key);
	}

	/**
	 * Return the list of keys that have data held in this module.
	 * 
	 * @return The list of keys.
	 */
	public List<Enum> listKeys()
	{
		List<Enum> keys = new ArrayList<Enum>();
		for(Map.Entry<Enum, Object> entry : enumData.entrySet())
		{
			keys.add(entry.getKey());
		}
		return keys;
	}

	/**
	 * Grab the Map in it's entirely. Can only be accessed from other Modules (to prevent unsafe use).
	 */
	@Override
	Map<Enum, Object> grabMap()
	{
		return this.enumData;
	}

	@Override
	protected void overrideMap(Map map)
	{
		try
		{
			this.enumData = map;
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
