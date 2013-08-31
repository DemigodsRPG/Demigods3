package com.censoredsoftware.demigods.util;

import com.censoredsoftware.demigods.Demigods;
import org.bukkit.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Module to load configuration settings from any passed in plugin's config.yml.
 */
public class Configs
{
	/**
	 * Constructor to create a new Configs for the given plugin's <code>instance</code>.
	 * 
	 * @param instance The demigods instance the Configs attaches to.
	 * @param copyDefaults Boolean for copying the default config.yml found inside this demigods over the config file utilized by this library.
	 */
	static
	{
		Configuration config = Demigods.plugin.getConfig();
		config.options().copyDefaults(true);
		Demigods.plugin.saveConfig();
	}

	/**
	 * Retrieve the Integer setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return Integer setting.
	 */
	public static int getSettingInt(String id)
	{
		if(Demigods.plugin.getConfig().isInt(id)) return Demigods.plugin.getConfig().getInt(id);
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
		if(Demigods.plugin.getConfig().isString(id)) return Demigods.plugin.getConfig().getString(id);
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
		return !Demigods.plugin.getConfig().isBoolean(id) || Demigods.plugin.getConfig().getBoolean(id);
	}

	/**
	 * Retrieve the Double setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return Double setting.
	 */
	public static double getSettingDouble(String id)
	{
		if(Demigods.plugin.getConfig().isDouble(id)) return Demigods.plugin.getConfig().getDouble(id);
		else return -1;
	}

	/**
	 * Retrieve the List<String> setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return List<String> setting.
	 */
	public static List<String> getSettingArrayListString(String id)
	{
		List<String> strings = new ArrayList<String>();
		if(Demigods.plugin.getConfig().isList(id))
		{
			for(String s : Demigods.plugin.getConfig().getStringList(id))
				strings.add(s);
			return strings;
		}
		return null;
	}
}
