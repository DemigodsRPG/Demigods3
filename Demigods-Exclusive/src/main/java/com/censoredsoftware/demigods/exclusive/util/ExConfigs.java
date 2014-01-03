package com.censoredsoftware.demigods.exclusive.util;

import com.censoredsoftware.demigods.exclusive.ExclusiveMythos;
import org.bukkit.configuration.Configuration;

import java.util.List;

/**
 * Module to load configuration settings from any passed in PLUGIN's config.yml.
 */
public class ExConfigs
{
	/**
	 * Constructor to create a new Configs for the given PLUGIN's <code>instance</code>.
	 * 
	 * @param instance The demigods instance the Configs attaches to.
	 * @param copyDefaults Boolean for copying the default config.yml found inside this demigods over the config file utilized by this library.
	 */
	static
	{
		Configuration config = ExclusiveMythos.inst().getConfig();
		config.options().copyDefaults(true);
		ExclusiveMythos.inst().saveConfig();
	}

	/**
	 * Retrieve the Integer setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return Integer setting.
	 */
	public static int getSettingInt(String id)
	{
		if(ExclusiveMythos.inst().getConfig().isInt(id)) return ExclusiveMythos.inst().getConfig().getInt(id);
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
		if(ExclusiveMythos.inst().getConfig().isString(id)) return ExclusiveMythos.inst().getConfig().getString(id);
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
		return !ExclusiveMythos.inst().getConfig().isBoolean(id) || ExclusiveMythos.inst().getConfig().getBoolean(id);
	}

	/**
	 * Retrieve the Double setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return Double setting.
	 */
	public static double getSettingDouble(String id)
	{
		if(ExclusiveMythos.inst().getConfig().isDouble(id)) return ExclusiveMythos.inst().getConfig().getDouble(id);
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
		if(ExclusiveMythos.inst().getConfig().isList(id)) return ExclusiveMythos.inst().getConfig().getStringList(id);
		return null;
	}

	public static void addToSettingList(String id, String data)
	{
		List<String> list = getSettingList(id);
		list.add(data);
		ExclusiveMythos.inst().getConfig().set(id, list);
		ExclusiveMythos.inst().saveConfig();
	}

	public static void removeFromSettingList(String id, String data)
	{
		List<String> list = getSettingList(id);
		list.remove(data);
		ExclusiveMythos.inst().getConfig().set(id, list);
		ExclusiveMythos.inst().saveConfig();
	}
}
