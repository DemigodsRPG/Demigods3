package com.censoredsoftware.library.serializable.yaml;

import com.censoredsoftware.library.serializable.DataSerializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * A simple yaml file intended to be extended by the same class that holds the data.
 * <strong>IMPORTANT:</strong> All SimpleYamlFile child classes should have default data for the serialize method to digest.
 * 
 * @param <V> The type of the child class.
 */
@SuppressWarnings("unchecked")
public abstract class SimpleYamlFile<V extends SimpleYamlFile> extends KeylessYamlConvertible implements YamlFile, DataSerializable
{
	/**
	 * Create a new instance of the child class from a configuration section.
	 * 
	 * @param conf A ConfigurationSection.
	 * @return A new instance made from provided data.
	 */
	public abstract V valueFromData(ConfigurationSection conf);

	@Override
	public V getCurrentFileData()
	{
		// Save defaults.
		saveDataToFile();

		// Grab the data.
		FileConfiguration fileData = YamlFileUtil.getConfiguration(getDirectoryPath(), getFullFileName());
		if(fileData != null) return valueFromData(fileData);
		return null;
	}

	@Override
	public boolean saveDataToFile()
	{
		// Create the file reference for this method.
		File dataFile = new File(getDirectoryPath() + getFullFileName());

		// Check if the file exists.
		if(!(dataFile.exists()))
		{
			// Nope, gotta make one.
			YamlFileUtil.createFile(dataFile);
		}

		// Create a YamlConfiguration from the file.
		FileConfiguration saveFile = YamlConfiguration.loadConfiguration(dataFile);

		// Add and copy the defaults.
		saveFile.addDefaults(serialize());
		saveFile.options().copyDefaults(true);
		try
		{
			// Save the file!
			saveFile.save(dataFile);
			return true;
		}
		catch(Exception ignored)
		{}
		return false;
	}

	@Override
	public final V valueFromData(Object... data)
	{
		if(data == null || data.length < 1 || !(data[0] instanceof ConfigurationSection)) return null;
		return valueFromData((ConfigurationSection) data[0]);
	}
}
