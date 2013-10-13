package com.censoredsoftware.demigods.engine.helper;

import com.censoredsoftware.demigods.engine.Demigods;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class ConfigFile<ID, DATA extends ConfigurationSerializable>
{
	public abstract DATA create(ID id, ConfigurationSection conf);

	public abstract ConcurrentMap<ID, DATA> getLoadedData();

	public abstract String getSavePath();

	public abstract String getSaveFile();

	public abstract Map<String, Object> serialize(ID id);

	public abstract ID convertFromString(String stringId);

	public ConcurrentMap<ID, DATA> loadFromFile()
	{
		final FileConfiguration data = Util.getData(getSavePath(), getSaveFile());
		ConcurrentHashMap<ID, DATA> map = new ConcurrentHashMap<ID, DATA>();
		for(String stringId : data.getKeys(false))
			map.put(convertFromString(stringId), create(convertFromString(stringId), data.getConfigurationSection(stringId)));
		return map;
	}

	public boolean saveToFile()
	{
		FileConfiguration saveFile = Util.getData(getSavePath(), getSaveFile());
		final Map<ID, DATA> currentFile = loadFromFile();

		for(ID id : Collections2.filter(getLoadedData().keySet(), new Predicate<ID>()
		{
			@Override
			public boolean apply(ID id)
			{
				return !currentFile.keySet().contains(id) || !currentFile.get(id).equals(getLoadedData().get(id));
			}
		}))
			saveFile.createSection(id.toString(), serialize(id));

		for(ID id : Collections2.filter(currentFile.keySet(), new Predicate<ID>()
		{
			@Override
			public boolean apply(ID id)
			{
				return !getLoadedData().keySet().contains(id);
			}
		}))
			saveFile.set(id.toString(), null);

		return Util.saveFile(getSavePath(), getSaveFile(), saveFile);
	}

	public abstract void loadToData();

	public static class Util
	{
		public static FileConfiguration getData(String path, String resource)
		{
			File dataFile = new File(path + resource);
			if(!(dataFile.exists())) createFile(path, resource);
			return YamlConfiguration.loadConfiguration(dataFile);
		}

		public static void createFile(String path, String resource)
		{
			File dataFile = new File(path + resource);
			if(!dataFile.exists())
			{
				(new File(path)).mkdir();
				Demigods.PLUGIN.saveResource(path.replace(Demigods.PLUGIN.getDataFolder() + "/", "") + resource, false);
			}
		}

		public static boolean saveFile(String path, String resource, FileConfiguration conf)
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
	}
}
