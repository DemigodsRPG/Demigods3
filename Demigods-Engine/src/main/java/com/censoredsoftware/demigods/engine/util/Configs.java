package com.censoredsoftware.demigods.engine.util;

import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import org.bukkit.configuration.Configuration;

import java.util.List;

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
		Configuration config = DemigodsPlugin.plugin().getConfig();
		config.options().copyDefaults(true);
		DemigodsPlugin.plugin().saveConfig();
	}

	/**
	 * Retrieve the Integer setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return Integer setting.
	 */
	public static int getSettingInt(String id)
	{
		if(DemigodsPlugin.plugin().getConfig().isInt(id)) return DemigodsPlugin.plugin().getConfig().getInt(id);
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
		if(DemigodsPlugin.plugin().getConfig().isString(id)) return DemigodsPlugin.plugin().getConfig().getString(id);
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
		return !DemigodsPlugin.plugin().getConfig().isBoolean(id) || DemigodsPlugin.plugin().getConfig().getBoolean(id);
	}

	/**
	 * Retrieve the Float setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return Float setting.
	 */
	public static float getSettingFloat(String id)
	{
		String floatValue = "-1F";
		if(DemigodsPlugin.plugin().getConfig().isString(id)) floatValue = DemigodsPlugin.plugin().getConfig().getString(id);
		try
		{
			return Float.valueOf(floatValue);
		}
		catch(Exception ignored)
		{}
		return -1F;
	}

	/**
	 * Retrieve the Double setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return Double setting.
	 */
	public static double getSettingDouble(String id)
	{
		if(DemigodsPlugin.plugin().getConfig().isDouble(id)) return DemigodsPlugin.plugin().getConfig().getDouble(id);
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
		if(DemigodsPlugin.plugin().getConfig().isList(id)) return DemigodsPlugin.plugin().getConfig().getStringList(id);
		return null;
	}

	public static void addToSettingList(String id, String data)
	{
		List<String> list = getSettingList(id);
		list.add(data);
		DemigodsPlugin.plugin().getConfig().set(id, list);
		DemigodsPlugin.plugin().saveConfig();
	}

	public static void removeFromSettingList(String id, String data)
	{
		List<String> list = getSettingList(id);
		list.remove(data);
		DemigodsPlugin.plugin().getConfig().set(id, list);
		DemigodsPlugin.plugin().saveConfig();
	}
}
