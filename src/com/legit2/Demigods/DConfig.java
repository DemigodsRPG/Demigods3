package com.legit2.Demigods;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.legit2.Demigods.Utilities.DMiscUtil;

public class DConfig
{
	// Define variables
	private static FileConfiguration config = null;
	private static File configFile = null;
	private static String configFolder = "Configs";
	static Demigods plugin = DMiscUtil.getPlugin();
	private static Configuration mainConfig;

	public static void initializeConfig()
	{
		mainConfig = plugin.getConfig().getRoot();
		mainConfig.options().copyDefaults(true);
		plugin.saveConfig();
	}
	
	public static int getSettingInt(String id)
	{
		if(plugin.getConfig().isInt(id))
		{
			return plugin.getConfig().getInt(id);
		}
		else return -1;
	}
	
	public static String getSettingString(String id)
	{
		if(plugin.getConfig().isString(id))
		{
			return plugin.getConfig().getString(id);
		}
		else return null;
	}
	
	public static boolean getSettingBoolean(String id)
	{
		if(plugin.getConfig().isBoolean(id))
		{
			return plugin.getConfig().getBoolean(id);
		}
		else return true;
	}
	
	public static double getSettingDouble(String id)
	{
		if(plugin.getConfig().isDouble(id))
		{
			return plugin.getConfig().getDouble(id);
		}
		else return -1;
	}
	
	/*
	 *  saveConfig() : Saves the custom configuration (String)name to file system.
	 */
	public static void saveConfig(String name)
	{
		if(config == null || configFile == null) return; 
		
		try
		{
			getConfig(name).save(configFile);
		}
		catch(IOException e)
		{
			DMiscUtil.severe("There was an error when saving file: " + name + ".yml");
			DMiscUtil.severe("Error: " + e);
		}
	}

	/*
	 *  saveDefaultConfig() : Saves the defaults for custom configuration (String)name to file system.
	 */
	public static void saveDefaultConfig(String name)
	{
		configFile = new File(plugin.getDataFolder() + File.separator + configFolder, name + ".yml");
		if(!configFile.exists())
		{
			plugin.saveResource(configFolder + File.separator + name + ".yml", false);
		}
	}
	
	/*
	 *  getConfig() : Grabs the custom configuration file from file system.
	 */
	public static FileConfiguration getConfig(String name)
	{
		if(config == null)
		{
			reloadConfig(name);
		}
		return config;
	}
	
	/*
	 *  reloadConfig() : Reloads the custom configuration (String)name to refresh values.
	 */
	public static void reloadConfig(String name)
	{
		if(configFile == null)
		{
			configFile = new File(plugin.getDataFolder() + File.separator + configFolder, name + ".yml");
		}
		config = YamlConfiguration.loadConfiguration(configFile);

		// Look for defaults in the jar
		InputStream defConfigStream = plugin.getResource(configFolder + File.separator + name + ".yml");
		if(defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}
}
