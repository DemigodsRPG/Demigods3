package com.censoredsoftware.Modules;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;

/**
 * Module to load configuration settings from any passed in plugin's config.yml.
 */
public class ConfigModule
{
	private static Plugin plugin;

	/**
	 * Constructor to create a new ConfigModule for the given plugin's <code>instance</code>.
	 * 
	 * @param instance The plugin instance the ConfigModule attaches to.
	 * @param copyDefaults Boolean for copying the default config.yml found inside this plugin over the config file utilized by this library.
	 */
	public ConfigModule(Plugin instance, boolean copyDefaults)
	{
		this.plugin = instance;
		helpFile(plugin);
		Configuration config = plugin.getConfig();
		config.options().copyDefaults(copyDefaults);
		plugin.saveConfig();
	}

	/**
	 * On first run copy the entire config.yml file (comments and all).
	 * 
	 * @param plugin The plugin running this config module.
	 */
	private void helpFile(Plugin plugin)
	{
		InputStream input = plugin.getResource("config.creole");
		File file = new File(plugin.getDataFolder() + File.separator + "config.creole");
		if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
		if(file.exists()) file.delete();
		try
		{
			OutputStream out = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int length;
			while((length = input.read(buffer)) > 0)
				out.write(buffer, 0, length);
			out.close();
			input.close();
			return;
		}
		catch(Exception ignored)
		{}
	}

	/**
	 * Retrieve the Integer setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return Integer setting.
	 */
	public int getSettingInt(String id)
	{
		if(plugin.getConfig().isInt(id)) return plugin.getConfig().getInt(id);
		else return -1;
	}

	/**
	 * Retrieve the String setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return String setting.
	 */
	public String getSettingString(String id)
	{
		if(plugin.getConfig().isString(id)) return plugin.getConfig().getString(id);
		else return null;
	}

	/**
	 * Retrieve the Boolean setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return Boolean setting.
	 */
	public boolean getSettingBoolean(String id)
	{
		return !plugin.getConfig().isBoolean(id) || plugin.getConfig().getBoolean(id);
	}

	/**
	 * Retrieve the Double setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return Double setting.
	 */
	public double getSettingDouble(String id)
	{
		if(plugin.getConfig().isDouble(id)) return plugin.getConfig().getDouble(id);
		else return -1;
	}

	/**
	 * Retrieve the List<String> setting for String <code>id</code>.
	 * 
	 * @param id The String key for the setting.
	 * @return List<String> setting.
	 */
	public List<String> getSettingArrayListString(String id)
	{
		List<String> strings = new ArrayList<String>();
		if(plugin.getConfig().isList(id))
		{
			for(String s : plugin.getConfig().getStringList(id))
				strings.add(s);
			return strings;
		}
		else return null;
	}
}
