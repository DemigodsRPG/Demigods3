package com.censoredsoftware.demigods.helper;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Map;

public abstract class ConfigFile
{
	private Plugin plugin;

	public ConfigFile(Plugin plugin)
	{
		this.plugin = plugin;
	}

	public FileConfiguration getData(String path, String resource)
	{
		File dataFile = new File(path + resource);
		if(!(dataFile.exists())) createFile(path, resource);
		return YamlConfiguration.loadConfiguration(dataFile);
	}

	public void createFile(String path, String resource)
	{
		File dataFile = new File(path + resource);
		if(!dataFile.exists())
		{
			(new File(path)).mkdir();
			plugin.saveResource(path.replace(plugin.getDataFolder() + "/", "") + resource, false);
		}
	}

	public boolean saveFile(String path, String resource, FileConfiguration conf)
	{
		try
		{
			conf.save(path + resource);
			return true;
		}
		catch(Exception ignored)
		{}
		return false;
	}

	public abstract Map loadFromFile();

	public abstract boolean saveToFile();
}
