package com.censoredsoftware.Demigods.PlayerCharacter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

/**
 * Module to handle all saving related methods.
 */
public class PlayerCharacterYAML
{
	private Plugin plugin;
	private String path, pluginName;
	final private String dataName;
	private Map map;
	final private File yamlFile, backupFile;
	private FileConfiguration persistance;
	private Logger log = Logger.getLogger("Minecraft");

	/**
	 * Constructor for creating the PlayerCharacterYAML.
	 * 
	 * @param instance The instance of the Plugin creating the PlayerCharacterYAML.
	 * @param dataName The name of data set, and the name of the file.
	 */
	public PlayerCharacterYAML(boolean load, Plugin instance, String path, String dataName)
	{
		this.plugin = instance;
		if(path != null) this.path = path;
		else this.path = "";
		this.pluginName = this.plugin.getName();
		this.dataName = dataName;

		folderStructure(this.path);

		this.yamlFile = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + path + dataName + ".yml");
		this.backupFile = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "backup" + File.separator + path + dataName + ".yml");

		initialize(load);
	}

	/**
	 * Make sure the folder structure is in place.
	 */
	private void folderStructure(String path)
	{
		// Define the folders
		File dataFolder = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + path);
		File backupFolder = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "backup" + File.separator + path);

		// If they don't exist, create them now
		if(!dataFolder.exists()) dataFolder.mkdirs();
		if(!backupFolder.exists()) backupFolder.mkdirs();
	}

	/**
	 * Initialize the PlayerCharacterYAML.
	 */
	private void initialize(boolean load)
	{
		if(!this.yamlFile.exists())
		{
			try
			{
				this.yamlFile.createNewFile();
				this.persistance = YamlConfiguration.loadConfiguration(yamlFile);
			}
			catch(Exception e)
			{
				log.severe("[" + pluginName + "] Unable to create save file: " + dataName + ".yml");
				log.severe("[" + pluginName + "] Error: " + e);
				log.severe("[" + pluginName + "] Please check your write permissions and try again.");
			}
			return;
		}
		else
		{
			this.persistance = YamlConfiguration.loadConfiguration(yamlFile);
			if(load) load();
		}
	}

	/**
	 * Save the data that this module handles.
	 * 
	 * @return True if successful.
	 */
	public boolean save(PlayerCharacter character)
	{
		// Grab the latest map for saving
		this.map = character.grabMap();

		try
		{
			this.persistance.createSection(dataName, this.map);
			if(this.backupFile.exists()) this.backupFile.delete();
			this.yamlFile.renameTo(this.backupFile);
			this.yamlFile.createNewFile();
			this.persistance.save(this.yamlFile);
			return true;
		}
		catch(Exception e)
		{
			log.severe("[" + pluginName + "] Unable to save file: " + dataName + ".yml");
			log.severe("[" + pluginName + "] Error: " + e);
			log.severe("[" + pluginName + "] Please check your write permissions and try again.");
		}
		return false;
	}

	/**
	 * Revert to the backup file.
	 */
	public void revertBackup()
	{
		backupFile.renameTo(yamlFile);
	}

	/**
	 * Load the data from the YAML file.
	 * 
	 * @return The data as a Map.
	 */
	public void load()
	{
		// Prevent NullPointerException Error
		if(this.persistance == null || this.persistance.getConfigurationSection(dataName) == null) return;

		// Define variables
		Map map = new HashMap();
		int error = 0;

		// Loop through the data in the file
		for(String key : this.persistance.getConfigurationSection(dataName).getKeys(false))
		{
			try
			{
				Object data = this.persistance.get(dataName + "." + key);
				map.put(key, data);
			}
			catch(Exception e)
			{
				error++;
				log.severe("[" + pluginName + "] Unable to load from file: " + dataName + ".yml");
				log.severe("[" + pluginName + "] Error: " + e);
			}
		}

		// Call the LoadYAMLEvent if need be
		if(!map.isEmpty() && error == 0) plugin.getServer().getPluginManager().callEvent(new PlayerCharacterLoadYAMLEvent(pluginName, path, dataName, map));
	}
}

/**
 * An event that is triggered when data is loaded from a YAML file.
 */
class PlayerCharacterLoadYAMLEvent extends Event implements Cancellable
{
	private String pluginName, path, dataName;
	private Map map;
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;

	/**
	 * Constructor for the LoadYAMLEvent.
	 * 
	 * @param pluginName Name of the plugin the data belongs to.
	 * @param dataName Name of the data set being loaded.
	 * @param map The data that was loaded.
	 */
	PlayerCharacterLoadYAMLEvent(String pluginName, String path, String dataName, Map map)
	{
		this.pluginName = pluginName;
		this.path = path;
		this.dataName = dataName;
		this.map = map;
	}

	/**
	 * Returns the plugin name of the loaded file.
	 * 
	 * @return The plugin name.
	 */
	String getPluginName()
	{
		return this.pluginName;
	}

	/**
	 * Returns the path of the loaded file.
	 * 
	 * @return The path.
	 */
	String getPath()
	{
		return this.path;
	}

	/**
	 * Returns the data name of the loaded file.
	 * 
	 * @return The data name.
	 */
	String getDataName()
	{
		return this.dataName;
	}

	/**
	 * Returns the data that was loaded.
	 * 
	 * @return The map data.
	 */
	Map getData()
	{
		return this.map;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	@Override
	public boolean isCancelled()
	{
		return this.cancelled;
	}

	@Override
	public synchronized void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
}
