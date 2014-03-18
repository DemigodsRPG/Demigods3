package com.censoredsoftware.library.serializable.yaml;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Package private utility for common file related methods.
 */
class YamlFileUtil
{
	/**
	 * Private constructor.
	 */
	private YamlFileUtil()
	{}

	/**
	 * Get the FileConfiguration at a given location.
	 * If no file exists, create one.
	 * 
	 * @param path The file directory path.
	 * @param fileName The file name.
	 * @return The configuration.
	 */
	static FileConfiguration getConfiguration(String path, String fileName)
	{
		File dataFile = new File(path + fileName);
		if(!(dataFile.exists())) createFile(dataFile);
		return YamlConfiguration.loadConfiguration(dataFile);
	}

	/**
	 * Create a new file.
	 * 
	 * @param dataFile The file object.
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	static void createFile(File dataFile)
	{
		try
		{
			// Create the directories.
			(dataFile.getParentFile()).mkdirs();

			// Create the new file.
			dataFile.createNewFile();
		}
		catch(Exception errored)
		{
			throw new RuntimeException("CensoredLib couldn't create a data file!", errored);
		}
	}

	/**
	 * Save the file.
	 * 
	 * @param path The file directory path.
	 * @param fileName The file name.
	 * @param conf The bukkit handled file configuration.
	 * @return Saved successfully.
	 */
	static boolean saveFile(String path, String fileName, FileConfiguration conf)
	{
		try
		{
			conf.save(path + fileName);
			return true;
		}
		catch(Exception ignored)
		{}
		return false;
	}
}
