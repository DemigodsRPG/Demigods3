package com.censoredsoftware.demigods.engine.util;

import java.util.List;

import org.bukkit.configuration.Configuration;

import com.censoredsoftware.demigods.engine.Demigods;

/**
 * Module to load configuration settings from any passed in PLUGIN's config.yml.
 */
public class Configs
{
	/**
	 * Constructor to create a new Configs for the given PLUGIN's <code>instance</code>.
	 * 
	 * @param instance The demigods instance the Configs attaches to.
	 * @param copyDefaults Boolean for copying the default config.yml found inside this demigods over the config file utilized by this library.
	 */
	static
	{
		Configuration config = Demigods.PLUGIN.getConfig();
		config.options().copyDefaults(true);
		Demigods.PLUGIN.saveConfig();
	}

	/**
	 * Retrieve the Integer setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return Integer setting.
	 */
	public static int getSettingInt(String id)
	{
		if(Demigods.PLUGIN.getConfig().isInt(id)) return Demigods.PLUGIN.getConfig().getInt(id);
		else return -1;
	}

	/**
	 * Retrieve the String setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return String setting.
	 */
	public static String getSettingString(String id)
	{
		if(Demigods.PLUGIN.getConfig().isString(id)) return Demigods.PLUGIN.getConfig().getString(id);
		else return null;
	}

	/**
	 * Retrieve the Boolean setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return Boolean setting.
	 */
	public static boolean getSettingBoolean(String id)
	{
		return !Demigods.PLUGIN.getConfig().isBoolean(id) || Demigods.PLUGIN.getConfig().getBoolean(id);
	}

	/**
	 * Retrieve the Double setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return Double setting.
	 */
	public static double getSettingDouble(String id)
	{
		if(Demigods.PLUGIN.getConfig().isDouble(id)) return Demigods.PLUGIN.getConfig().getDouble(id);
		else return -1;
	}

	/**
	 * Retrieve the List<String> setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return List<String> setting.
	 */
	public static List<String> getSettingList(String id)
	{
		if(Demigods.PLUGIN.getConfig().isList(id)) return Demigods.PLUGIN.getConfig().getStringList(id);
		return null;
	}

	public static void addToSettingList(String id, String data)
	{
		List<String> list = getSettingList(id);
		list.add(data);
		Demigods.PLUGIN.getConfig().set(id, list);
		Demigods.PLUGIN.saveConfig();
	}

	public static void removeFromSettingList(String id, String data)
	{
		List<String> list = getSettingList(id);
		list.remove(data);
		Demigods.PLUGIN.getConfig().set(id, list);
		Demigods.PLUGIN.saveConfig();
	}
}
