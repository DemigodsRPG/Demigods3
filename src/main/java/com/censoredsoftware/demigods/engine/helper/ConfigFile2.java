package com.censoredsoftware.demigods.engine.helper;

import com.censoredsoftware.demigods.engine.util.Messages;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.util.Map;

public abstract class ConfigFile2 implements ConfigurationSerializable
{
	public abstract void unserialize(ConfigurationSection conf);

	public abstract String getSavePath();

	public abstract String getSaveFile();

	public abstract Map<String, Object> serialize();

	public void loadFromFile()
	{
		FileConfiguration fileData = getData();
		if(fileData != null) unserialize(fileData);
	}

	public FileConfiguration getData()
	{
		File dataFile = new File(getSavePath() + getSaveFile());
		if(!(dataFile.exists()))
		{
			createFile();
			return null;
		}
		return YamlConfiguration.loadConfiguration(dataFile);
	}

	public void createFile()
	{
		File dataFile = new File(getSavePath() + getSaveFile());
		if(!dataFile.exists())
		{
			try
			{
				(new File(getSavePath())).mkdirs();
				dataFile.createNewFile();
			}
			catch(Exception ignored)
			{}
		}
		saveDefaultFile(dataFile);
	}

	private boolean saveDefaultFile(File dataFile)
	{
		try
		{
			FileConfiguration saveFile = YamlConfiguration.loadConfiguration(dataFile);
			saveFile.addDefaults(serialize());
			saveFile.options().copyDefaults(true);
			saveFile.save(dataFile);
			return true;
		}
		catch(Exception caught)
		{
			Messages.warning("Could not save default configuration file.");
		}
		return false;
	}
}
