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

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Modules.Data.DataModule;
import com.censoredsoftware.Modules.Data.DataStubModule;
import com.censoredsoftware.Modules.Persistence.Event.LoadFileEvent;
import com.censoredsoftware.Modules.Persistence.Event.LoadFileStubEvent;

public class JsonPersistenceModule implements PersistenceModule
{
	private Plugin plugin;
	private String path, dataName;
	private Map map;
	private File jsonFile;
	private JsonArray persistance;
	private Logger log = Logger.getLogger("Minecraft");

	public JsonPersistenceModule(boolean load, Plugin instance, String path, String dataName)
	{
		this.plugin = instance;
		this.path = path;
		this.dataName = dataName;

		folderStructure(this.path);

		this.jsonFile = new File(plugin.getDataFolder() + File.separator + "Data" + File.separator + path + File.separator + dataName + ".json");

		initialize(load);
	}

	/**
	 * Make sure the folder structure is in place.
	 */
	private void folderStructure(String path)
	{
		// Define the folders
		File dataFolder = new File(plugin.getDataFolder() + File.separator + "Data" + File.separator + path);

		// If they don't exist, create them now
		if(!dataFolder.exists()) dataFolder.mkdirs();
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
				this.persistance = Json.createArrayBuilder().build();
			}
			catch(Exception e)
			{
				log.severe("[" + plugin.getName() + "] Unable to save file: " + dataName + ".json");
				log.severe("[" + plugin.getName() + "] Error: " + e);
				e.printStackTrace();
				log.severe("[" + plugin.getName() + "] Please check your write permissions and try again.");
			}
			return;
		}
		else
		{
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

		buildFile(dataName, this.map, false);

		return save();
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

	private void buildFile(String holder, Map map, boolean insert)
	{
		JsonArrayBuilder entireFile = Json.createArrayBuilder();
		JsonObjectBuilder keyHolder = Json.createObjectBuilder();
		JsonArrayBuilder entryHolder = Json.createArrayBuilder();
		JsonObjectBuilder entry = Json.createObjectBuilder();
		for(Object key : map.keySet())
		{
			if(map.get(key) instanceof List)
			{
				JsonArrayBuilder entryList = Json.createArrayBuilder();
				for(Object data : (List) map.get(key))
				{
					if(DemigodsData.isInt(data.toString())) entryList.add(Integer.parseInt(data.toString()));
					else if(DemigodsData.isLong(data.toString())) entryList.add(Long.parseLong(data.toString()));
					else if(DemigodsData.isFloat(data.toString())) entryList.add(Float.parseFloat(data.toString()));
					else if(DemigodsData.isDouble(data.toString())) entryList.add(Double.parseDouble(data.toString()));
					else if(DemigodsData.isBoolean(data.toString())) entryList.add(Boolean.parseBoolean(data.toString()));
					else entryList.add(data.toString());
				}
				entry.add(key.toString(), entryList);
			}
			else if(DemigodsData.isInt(map.get(key).toString())) entry.add(key.toString(), Integer.parseInt(map.get(key).toString()));
			else if(DemigodsData.isLong(map.get(key).toString())) entry.add(key.toString(), Long.parseLong(map.get(key).toString()));
			else if(DemigodsData.isFloat(map.get(key).toString())) entry.add(key.toString(), Float.parseFloat(map.get(key).toString()));
			else if(DemigodsData.isDouble(map.get(key).toString())) entry.add(key.toString(), Double.parseDouble(map.get(key).toString()));
			else if(DemigodsData.isBoolean(map.get(key).toString())) entry.add(key.toString(), Boolean.parseBoolean(map.get(key).toString()));
			else entry.add(key.toString(), map.get(key).toString());
		}

		entryHolder.add(entry);
		keyHolder.add(holder, entryHolder);
		entireFile.add(keyHolder);
		if(insert && this.persistance != null)
		{
			for(JsonValue value : this.persistance)
			{
				entireFile.add(value);
			}
		}
		this.persistance = entireFile.build();
	}

	private boolean save()
	{
		try
		{
			if(this.persistance.equals(loadFromFile(this.jsonFile)))
			{
				Demigods.message.severe(dataName + " SAME.");
				return true;
			}
			this.jsonFile.delete();
			this.jsonFile.createNewFile();
			JsonWriter writer = Json.createWriter(new FileOutputStream(jsonFile));
			writer.writeArray(this.persistance);
			writer.close();
			Demigods.message.severe(dataName + " DIFFERENT.");
			return true;
		}
		catch(Exception e)
		{
			log.severe("[" + plugin.getName() + "] Unable to save file: " + dataName + ".json");
			log.severe("[" + plugin.getName() + "] Error: " + e);
			e.printStackTrace();
			log.severe("[" + plugin.getName() + "] Please check your write permissions and try again.");
		}
		return false;
	}

	/**
	 * Save the data that this module handles.
	 * 
	 * @return True if successful.
	 */
	@Override
	public boolean save(List stubs)
	{
		this.persistance = Json.createArrayBuilder().build();
		for(Object stub : stubs)
		{
			if(!(stub instanceof DataStubModule)) continue;
			buildFile(String.valueOf(((DataStubModule) stub).getID()), ((DataStubModule) stub).getMap(), true);
		}
		return save();
	}

	private JsonArray loadFromFile(File file)
	{
		JsonArray array = null;
		try
		{
			JsonReader reader = Json.createReader(file.toURI().toURL().openStream());
			array = reader.readArray();
		}
		catch(Exception ignored)
		{}

		// Prevent NullPointerException Error
		if(array == null) return Json.createArrayBuilder().build();
		return array;
	}

	/**
	 * Load the data from the YAML file.
	 * 
	 * @return The data as a Map.
	 */
	@Override
	public void load()
	{
		JsonArray entireFile = loadFromFile(this.jsonFile);

		// Prevent NullPointerException Error
		if(entireFile == null) return;

		// Define variables
		Map map = new HashMap();
		boolean stub = false;
		int stubID = -1;

		for(JsonValue value : entireFile)
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
										if(DemigodsData.isInt(data_.getKey().toString())) map.put(Integer.parseInt(data_.getKey().toString()), new ArrayList<Object>()
										{
											{
												for(JsonValue data__ : finalArray)
												{
													if(DemigodsData.isInt(data__.toString())) add(Integer.parseInt(data__.toString()));
													else if(DemigodsData.isLong(data__.toString())) add(Long.parseLong(data__.toString()));
													else if(DemigodsData.isFloat(data__.toString())) add(Float.parseFloat(data__.toString()));
													else if(DemigodsData.isDouble(data__.toString())) add(Double.parseDouble(data__.toString()));
													else if(DemigodsData.isBoolean(data__.toString())) add(Boolean.parseBoolean(data__.toString()));
													else add(Integer.parseInt(data__.toString()), data__.toString());
												}
											}
										});
										else map.put(data_.getKey().toString(), new ArrayList<Object>()
										{
											{
												for(JsonValue data__ : finalArray)
												{
													if(DemigodsData.isInt(data__.toString())) add(Integer.parseInt(data__.toString()));
													else if(DemigodsData.isLong(data__.toString())) add(Long.parseLong(data__.toString()));
													else if(DemigodsData.isFloat(data__.toString())) add(Float.parseFloat(data__.toString()));
													else if(DemigodsData.isDouble(data__.toString())) add(Double.parseDouble(data__.toString()));
													else if(DemigodsData.isBoolean(data__.toString())) add(Boolean.parseBoolean(data__.toString()));
													else add(Integer.parseInt(data__.toString()), data__.toString());
												}
											}
										});
									}
									else if(data_.getValue() != null)
									{
										if(DemigodsData.isInt(data_.getKey().toString()))
										{
											if(DemigodsData.isInt(data_.getValue().toString())) map.put(Integer.parseInt(data_.getKey().toString()), Integer.parseInt(data_.getValue().toString()));
											else if(DemigodsData.isLong(data_.getValue().toString())) map.put(Integer.parseInt(data_.getKey().toString()), Long.parseLong(data_.getValue().toString()));
											else if(DemigodsData.isFloat(data_.getValue().toString())) map.put(Integer.parseInt(data_.getKey().toString()), Float.parseFloat(data_.getValue().toString()));
											else if(DemigodsData.isDouble(data_.getValue().toString())) map.put(Integer.parseInt(data_.getKey().toString()), Double.parseDouble(data_.getValue().toString()));
											else if(DemigodsData.isBoolean(data_.getValue().toString())) map.put(Integer.parseInt(data_.getKey().toString()), Boolean.parseBoolean(data_.getValue().toString()));
											else map.put(Integer.parseInt(data_.getKey().toString()), data_.getValue().toString());
										}
										else
										{
											if(DemigodsData.isInt(data_.getValue().toString())) map.put(data_.getKey().toString(), Integer.parseInt(data_.getValue().toString()));
											else if(DemigodsData.isLong(data_.getValue().toString())) map.put(data_.getKey().toString(), Long.parseLong(data_.getValue().toString()));
											else if(DemigodsData.isFloat(data_.getValue().toString())) map.put(data_.getKey().toString(), Float.parseFloat(data_.getValue().toString()));
											else if(DemigodsData.isDouble(data_.getValue().toString())) map.put(data_.getKey().toString(), Double.parseDouble(data_.getValue().toString()));
											else if(DemigodsData.isBoolean(data_.getValue().toString())) map.put(data_.getKey().toString(), Boolean.parseBoolean(data_.getValue().toString()));
											else map.put(data_.getKey().toString(), data_.getValue().toString());
										}
									}
								}
							}
						}
					}

					if(stubID != -1 && !map.isEmpty())
					{
						plugin.getServer().getPluginManager().callEvent(new LoadFileStubEvent(plugin.getName(), path, dataName, stubID, map));
						stub = true;
					}

				}
			}
		}

		if(!stub && !map.isEmpty())
		{
			plugin.getServer().getPluginManager().callEvent(new LoadFileEvent(plugin.getName(), path, dataName, map));
			Demigods.message.broadcast(dataName + ":");
			for(Object key : map.keySet())
			{
				Demigods.message.broadcast("  ->  " + key.toString() + ": " + map.get(key).toString());
			}
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
