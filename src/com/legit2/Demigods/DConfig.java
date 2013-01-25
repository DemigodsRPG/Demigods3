package com.legit2.Demigods;

import org.bukkit.configuration.Configuration;

import com.legit2.Demigods.Utilities.DMiscUtil;

public class DConfig
{
	static Demigods plugin = DMiscUtil.getPlugin();
	private static Configuration config;

	public static void initializeConfig()
	{
		config = plugin.getConfig().getRoot();
		config.options().copyDefaults(true);
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
}
