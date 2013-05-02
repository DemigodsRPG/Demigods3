package com.censoredsoftware.Modules.Persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.json.*;

import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Modules.Data.DataModule;
import com.censoredsoftware.Modules.Data.DataStubModule;
import com.censoredsoftware.Modules.Persistence.Event.LoadFileEvent;
import com.censoredsoftware.Modules.Persistence.Event.LoadFileStubEvent;

public class JsonPersistenceModule implements PersistenceModule
{
	private JsonReader reader;
	private Plugin plugin;
	private String path, dataName;
	private Map map;
	private File jsonFile, backupFile;
	private JsonArray persistance;
	private Logger log = Logger.getLogger("Minecraft");

	public JsonPersistenceModule(boolean load, Plugin instance, String path, String dataName)
	{
		this.plugin = instance;
		this.path = path;
		this.dataName = dataName;

		folderStructure(this.path);

		this.jsonFile = new File(plugin.getDataFolder() + File.separator + path + File.separator + dataName + ".json");
		this.backupFile = new File(plugin.getDataFolder() + path + File.separator + "backup" + File.separator + dataName + ".json");

		initialize(load);
	}

	/**
	 * Make sure the folder structure is in place.
	 */
	private void folderStructure(String path)
	{
		// Define the folders
		File dataFolder = new File(plugin.getDataFolder() + File.separator + path);
		File backupFolder = new File(plugin.getDataFolder() + File.separator + "backup" + File.separator + path);

		// If they don't exist, create them now
		if(!dataFolder.exists()) dataFolder.mkdirs();
		if(!backupFolder.exists()) backupFolder.mkdirs();
	}

	/**
	 * Initialize the PlayerCharacterYAML.
	 */
	private void initialize(boolean load)
	{
		if(!this.jsonFile.exists())
		{
			try
			{
				this.jsonFile.createNewFile();
				this.persistance = Json.createBuilderFactory(null).createArrayBuilder().build();
			}
			catch(Exception e)
			{
				log.severe("[" + plugin.getName() + "] Unable to create save file: " + dataName + ".json");
				log.severe("[" + plugin.getName() + "] Error: " + e);
				log.severe("[" + plugin.getName() + "] Please check your write permissions and try again.");
			}
			return;
		}
		else
		{
			try
			{
				this.reader = Json.createReader(jsonFile.toURI().toURL().openStream());
				this.persistance = reader.readArray();
			}
			catch(Exception e)
			{
				log.severe("[" + plugin.getName() + "] Unable to create read file: " + dataName + ".json");
				log.severe("[" + plugin.getName() + "] Error: " + e);
			}
			if(load) load();
		}
	}

	/**
	 * Save the data that this module handles.
	 * 
	 * @return True if successful.
	 */
	@Override
	public boolean save(DataModule dataModule)
	{
		// Grab the latest map for saving
		this.map = dataModule.getMap();

		if(map == null || map.isEmpty()) return true;

		JsonArrayBuilder entireFile = Json.createArrayBuilder();
		JsonObjectBuilder keyHolder = Json.createObjectBuilder();
		JsonArrayBuilder entryHolder = Json.createArrayBuilder();
		JsonObjectBuilder entry = Json.createObjectBuilder();
		for(Object key : this.map.keySet())
		{
			if(this.map.get(key) instanceof List)
			{
				JsonArrayBuilder entryList = Json.createArrayBuilder();
				for(Object data : (List) this.map.get(key))
				{
					entryList.add(data.toString());
				}
				entry.add(key.toString(), entryList);
			}
			else entry.add(key.toString(), this.map.get(key).toString());
		}
		entryHolder.add(entry);
		keyHolder.add(dataName, entryHolder);
		entireFile.add(keyHolder);
		this.persistance = entireFile.build();

		try
		{
			if(this.backupFile.exists()) this.backupFile.delete();
			this.jsonFile.renameTo(this.backupFile);
			this.jsonFile.createNewFile();
			JsonWriter writer = Json.createWriter(new FileOutputStream(jsonFile));
			writer.writeArray(persistance);
			writer.close();
			return true;
		}
		catch(Exception e)
		{
			log.severe("[" + plugin.getName() + "] Unable to save file: " + dataName + ".json");
			log.severe("[" + plugin.getName() + "] Error: " + e);
			log.severe("[" + plugin.getName() + "] Please check your write permissions and try again.");
		}
		return false;
	}

	@Override
	public boolean save(final DataStubModule stub)
	{
		return save(new ArrayList<DataStubModule>()
		{
			{
				add(stub);
			}
		});
	}

	/**
	 * Save the data that this module handles.
	 * 
	 * @return True if successful.
	 */
	@Override
	public boolean save(List stubs)
	{
		JsonArrayBuilder entireFile = Json.createArrayBuilder();
		JsonObjectBuilder keyHolder = Json.createObjectBuilder();
		for(Object stub : stubs)
		{
			if(!(stub instanceof DataStubModule)) continue;
			JsonArrayBuilder entryHolder = Json.createArrayBuilder();
			JsonObjectBuilder entry = Json.createObjectBuilder();
			for(Object key : ((DataStubModule) stub).getMap().keySet())
			{
				if(((DataStubModule) stub).getMap().get(key) instanceof List)
				{
					JsonArrayBuilder entryList = Json.createArrayBuilder();
					for(Object data : (List) ((DataStubModule) stub).getMap().get(key))
					{
						entryList.add(data.toString());
					}
					entry.add(key.toString(), entryList);
				}
				else entry.add(key.toString(), ((DataStubModule) stub).getMap().get(key).toString());
			}
			entryHolder.add(entry);
			keyHolder.add(String.valueOf(((DataStubModule) stub).getID()), entryHolder);
			entireFile.add(keyHolder);
		}

		this.persistance = entireFile.build();

		try
		{
			if(this.backupFile.exists()) this.backupFile.delete();
			this.jsonFile.renameTo(this.backupFile);
			this.jsonFile.createNewFile();
			JsonWriter writer = Json.createWriter(new FileOutputStream(jsonFile));
			writer.writeArray(persistance);
			writer.close();
			return true;
		}
		catch(Exception e)
		{
			log.severe("[" + plugin.getName() + "] Unable to save file: " + dataName + ".json");
			log.severe("[" + plugin.getName() + "] Error: " + e);
			log.severe("[" + plugin.getName() + "] Please check your write permissions and try again.");
		}
		return false;
	}

	/**
	 * Revert to the backup file.
	 */
	public void revertBackup()
	{
		backupFile.renameTo(jsonFile);
	}

	/**
	 * Load the data from the YAML file.
	 * 
	 * @return The data as a Map.
	 */
	@Override
	public void load()
	{
		// Prevent NullPointerException Error
		if(this.persistance == null) return;

		// Define variables
		Map map = new HashMap();
		int stubID = -1;

		for(JsonValue value : this.persistance)
		{
			if(value instanceof JsonObject)
			{
				JsonObject keyHolder = (JsonObject) value;
				for(Map.Entry entry_ : keyHolder.entrySet())
				{
					if(DemigodsData.isInt(entry_.getKey().toString())) stubID = Integer.parseInt(entry_.getKey().toString());
					if(entry_.getValue() instanceof JsonArray)
					{
						JsonArray entryHolder = (JsonArray) entry_.getValue();
						for(JsonValue value_ : entryHolder)
						{
							if(value_ instanceof JsonObject)
							{
								JsonObject data = (JsonObject) value_;
								for(Map.Entry data_ : data.entrySet())
								{
									if(data_.getValue() instanceof JsonArray)
									{
										final JsonArray finalArray = (JsonArray) data_.getValue();
										map.put(data_.getKey().toString(), new ArrayList<Object>()
										{
											{
												for(JsonValue data__ : finalArray)
												{
													JsonValue.ValueType type = data__.getValueType();
													if(DemigodsData.isInt(data__.toString()))
													{
														add(Integer.parseInt(data__.toString()));
													}
													else switch(type)
													{
														case STRING:
														{
															add(data__.toString());
															break;
														}
														case TRUE:
														{
															add(true);
															break;
														}
														case FALSE:
														{
															add(false);
															break;
														}
														case NUMBER:
														{
															add(Long.parseLong(data__.toString()));
															break;
														}
													}
												}
											}
										});
									}
									else
									{
										Object mapData = null;
										try
										{
											mapData = data.getString(data_.getKey().toString());
										}
										catch(Exception ignored)
										{}
										try
										{
											mapData = data.getInt(data_.getKey().toString());
										}
										catch(Exception ignored)
										{}
										try
										{
											mapData = data.getBoolean(data_.getKey().toString());
										}
										catch(Exception ignored)
										{}

										if(mapData != null) map.put(data_.getKey().toString(), mapData);
									}
								}
							}
						}
					}
				}
			}
		}

		if(stubID != -1 && !map.isEmpty()) plugin.getServer().getPluginManager().callEvent(new LoadFileStubEvent(plugin.getName(), path, dataName, stubID, map));
		else if(!map.isEmpty()) plugin.getServer().getPluginManager().callEvent(new LoadFileEvent(plugin.getName(), path, dataName, map));
	}
}
