package com.censoredsoftware.demigods.data;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.battle.Battle;
import com.censoredsoftware.demigods.helper.ConfigFile;
import com.censoredsoftware.demigods.item.DItemStack;
import com.censoredsoftware.demigods.language.Translation;
import com.censoredsoftware.demigods.location.DLocation;
import com.censoredsoftware.demigods.player.*;
import com.censoredsoftware.demigods.structure.Structure;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class DataManager
{
	// Data
	public static ConcurrentMap<String, DPlayer> players;
	public static ConcurrentMap<UUID, DLocation> locations;
	public static ConcurrentMap<UUID, Structure> structures;
	public static ConcurrentMap<UUID, DCharacter> characters;
	public static ConcurrentMap<UUID, DCharacter.Meta> characterMetas;
	public static ConcurrentMap<UUID, Death> deaths;
	public static ConcurrentMap<UUID, Skill> skills;
	public static ConcurrentMap<UUID, DCharacter.Inventory> inventories;
	public static ConcurrentMap<UUID, DItemStack> itemStacks;
	public static ConcurrentMap<UUID, SavedPotion> savedPotions;
	public static ConcurrentMap<UUID, Pet> pets;
	public static ConcurrentMap<UUID, Notification> notifications;
	public static ConcurrentMap<UUID, Battle> battles;
	public static ConcurrentMap<UUID, TimedData> timedData;

	private static ConcurrentMap<String, HashMap<String, Object>> tempData;

	static
	{
		for(File file : File.values())
			file.getConfigFile().loadToData();
		tempData = Maps.newConcurrentMap();
	}

	public static void save()
	{
		for(File file : File.values())
			file.getConfigFile().saveToFile();
	}

	public static void flushData()
	{
		// Kick everyone
		for(Player player : Bukkit.getOnlinePlayers())
			player.kickPlayer(ChatColor.GREEN + Demigods.LANGUAGE.getText(Translation.Text.DATA_RESET_KICK));

		// Clear the data
		itemStacks.clear();
		locations.clear();
		players.clear();
		characters.clear();
		characterMetas.clear();
		inventories.clear();
		skills.clear();
		notifications.clear();
		pets.clear();
		structures.clear();
		battles.clear();
		deaths.clear();
		timedData.clear();
		savedPotions.clear();

		tempData.clear();

		save();

		// Reload the PLUGIN
		Bukkit.getServer().getPluginManager().disablePlugin(Demigods.PLUGIN);
		Bukkit.getServer().getPluginManager().enablePlugin(Demigods.PLUGIN);
	}

	public static boolean hasKeyTemp(String key, String subKey)
	{
		return tempData.containsKey(key) && tempData.get(key).containsKey(subKey);
	}

	public static Object getValueTemp(String key, String subKey)
	{
		if(tempData.containsKey(key)) return tempData.get(key).get(subKey);
		else return null;
	}

	public static void saveTemp(String key, String subKey, Object value)
	{
		if(!tempData.containsKey(key)) tempData.put(key, new HashMap<String, Object>());
		tempData.get(key).put(subKey, value);
	}

	public static void removeTemp(String key, String subKey)
	{
		if(tempData.containsKey(key) && tempData.get(key).containsKey(subKey)) tempData.get(key).remove(subKey);
	}

	public static void saveTimed(String key, String subKey, Object data, Integer seconds)
	{
		// Remove the data if it exists already
		TimedData.Util.remove(key, subKey);

		// Create and save the timed data
		TimedData timedData = new TimedData();
		timedData.generateId();
		timedData.setKey(key);
		timedData.setSubKey(subKey);
		timedData.setData(data.toString());
		timedData.setSeconds(seconds);
		DataManager.timedData.put(timedData.getId(), timedData);
	}

	public static void removeTimed(String key, String subKey)
	{
		TimedData.Util.remove(key, subKey);
	}

	public static boolean hasTimed(String key, String subKey)
	{
		return TimedData.Util.find(key, subKey) != null;
	}

	public static Object getTimedValue(String key, String subKey)
	{
		return TimedData.Util.find(key, subKey).getData();
	}

	public static enum File
	{
		PLAYER(new ConfigFile<String, DPlayer>()
		{
			@Override
			public DPlayer create(String string, ConfigurationSection conf)
			{
				return new DPlayer(string, conf);
			}

			@Override
			public ConcurrentMap<String, DPlayer> getLoadedData()
			{
				return DataManager.players;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "players.yml";
			}

			@Override
			public Map<String, Object> serialize(String string)
			{
				return getLoadedData().get(string).serialize();
			}

			@Override
			public String convertFromString(String stringId)
			{
				return stringId;
			}

			@Override
			public void loadToData()
			{
				players = loadFromFile();
			}
		}), LOCATION(new ConfigFile<UUID, DLocation>()
		{
			@Override
			public DLocation create(UUID uuid, ConfigurationSection conf)
			{
				return new DLocation(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, DLocation> getLoadedData()
			{
				return DataManager.locations;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "locations.yml";
			}

			@Override
			public Map<String, Object> serialize(UUID uuid)
			{
				return getLoadedData().get(uuid).serialize();
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}

			@Override
			public void loadToData()
			{
				locations = loadFromFile();
			}
		}), STRUCTURE(new ConfigFile<UUID, Structure>()
		{
			@Override
			public Structure create(UUID uuid, ConfigurationSection conf)
			{
				return new Structure(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, Structure> getLoadedData()
			{
				return DataManager.structures;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "structures.yml";
			}

			@Override
			public Map<String, Object> serialize(UUID uuid)
			{
				return getLoadedData().get(uuid).serialize();
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}

			@Override
			public void loadToData()
			{
				structures = loadFromFile();
			}
		}), CHARACTER(new ConfigFile<UUID, DCharacter>()
		{
			@Override
			public DCharacter create(UUID uuid, ConfigurationSection conf)
			{
				return new DCharacter(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, DCharacter> getLoadedData()
			{
				return DataManager.characters;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "characters.yml";
			}

			@Override
			public Map<String, Object> serialize(UUID uuid)
			{
				return getLoadedData().get(uuid).serialize();
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}

			@Override
			public void loadToData()
			{
				characters = loadFromFile();
			}
		}), CHARACTER_META(new ConfigFile<UUID, DCharacter.Meta>()
		{
			@Override
			public DCharacter.Meta create(UUID uuid, ConfigurationSection conf)
			{
				return new DCharacter.Meta(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, DCharacter.Meta> getLoadedData()
			{
				return DataManager.characterMetas;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "characterMetas.yml";
			}

			@Override
			public Map<String, Object> serialize(UUID uuid)
			{
				return getLoadedData().get(uuid).serialize();
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}

			@Override
			public void loadToData()
			{
				characterMetas = loadFromFile();
			}
		}), DEATH(new ConfigFile<UUID, Death>()
		{
			@Override
			public Death create(UUID uuid, ConfigurationSection conf)
			{
				return new Death(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, Death> getLoadedData()
			{
				return DataManager.deaths;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "deaths.yml";
			}

			@Override
			public Map<String, Object> serialize(UUID uuid)
			{
				return getLoadedData().get(uuid).serialize();
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}

			@Override
			public void loadToData()
			{
				deaths = loadFromFile();
			}
		}), SKILL(new ConfigFile<UUID, Skill>()
		{
			@Override
			public Skill create(UUID uuid, ConfigurationSection conf)
			{
				return new Skill(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, Skill> getLoadedData()
			{
				return DataManager.skills;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "skill.yml"; // TODO This is named differently than all of the other files.
			}

			@Override
			public Map<String, Object> serialize(UUID uuid)
			{
				return getLoadedData().get(uuid).serialize();
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}

			@Override
			public void loadToData()
			{
				skills = loadFromFile();
			}
		}), CHARACTER_INVENTORY(new ConfigFile<UUID, DCharacter.Inventory>()
		{
			@Override
			public DCharacter.Inventory create(UUID uuid, ConfigurationSection conf)
			{
				return new DCharacter.Inventory(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, DCharacter.Inventory> getLoadedData()
			{
				return DataManager.inventories;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "characterInventories.yml";
			}

			@Override
			public Map<String, Object> serialize(UUID uuid)
			{
				return getLoadedData().get(uuid).serialize();
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}

			@Override
			public void loadToData()
			{
				inventories = loadFromFile();
			}
		}), ITEM_STACK(new ConfigFile<UUID, DItemStack>()
		{
			@Override
			public DItemStack create(UUID uuid, ConfigurationSection conf)
			{
				return new DItemStack(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, DItemStack> getLoadedData()
			{
				return DataManager.itemStacks;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "itemStacks.yml";
			}

			@Override
			public Map<String, Object> serialize(UUID uuid)
			{
				return getLoadedData().get(uuid).serialize();
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}

			@Override
			public void loadToData()
			{
				itemStacks = loadFromFile();
			}
		}), SAVED_POTION(new ConfigFile<UUID, SavedPotion>()
		{
			@Override
			public SavedPotion create(UUID uuid, ConfigurationSection conf)
			{
				return new SavedPotion(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, SavedPotion> getLoadedData()
			{
				return DataManager.savedPotions;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "savedPotions.yml";
			}

			@Override
			public Map<String, Object> serialize(UUID uuid)
			{
				return getLoadedData().get(uuid).serialize();
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}

			@Override
			public void loadToData()
			{
				savedPotions = loadFromFile();
			}
		}), PET(new ConfigFile<UUID, Pet>()
		{
			@Override
			public Pet create(UUID uuid, ConfigurationSection conf)
			{
				return new Pet(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, Pet> getLoadedData()
			{
				return DataManager.pets;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "pets.yml";
			}

			@Override
			public Map<String, Object> serialize(UUID uuid)
			{
				return getLoadedData().get(uuid).serialize();
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}

			@Override
			public void loadToData()
			{
				pets = loadFromFile();
			}
		}), NOTIFICATION(new ConfigFile<UUID, Notification>()
		{
			@Override
			public Notification create(UUID uuid, ConfigurationSection conf)
			{
				return new Notification(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, Notification> getLoadedData()
			{
				return DataManager.notifications;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "notifications.yml";
			}

			@Override
			public Map<String, Object> serialize(UUID uuid)
			{
				return getLoadedData().get(uuid).serialize();
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}

			@Override
			public void loadToData()
			{
				notifications = loadFromFile();
			}
		}), BATTLE(new ConfigFile<UUID, Battle>()
		{
			@Override
			public Battle create(UUID uuid, ConfigurationSection conf)
			{
				return new Battle(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, Battle> getLoadedData()
			{
				return DataManager.battles;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "battles.yml";
			}

			@Override
			public Map<String, Object> serialize(UUID uuid)
			{
				return getLoadedData().get(uuid).serialize();
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}

			@Override
			public void loadToData()
			{
				battles = loadFromFile();
			}
		}), TIMED_DATA(new ConfigFile<UUID, TimedData>()
		{
			@Override
			public TimedData create(UUID uuid, ConfigurationSection conf)
			{
				return new TimedData(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, TimedData> getLoadedData()
			{
				return DataManager.timedData;
			}

			@Override
			public String getSavePath()
			{
				return Demigods.SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "timedData.yml";
			}

			@Override
			public Map<String, Object> serialize(UUID uuid)
			{
				return getLoadedData().get(uuid).serialize();
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}

			@Override
			public void loadToData()
			{
				timedData = loadFromFile();
			}
		});

		private ConfigFile save;

		private File(ConfigFile save)
		{
			this.save = save;
		}

		public ConfigFile getConfigFile()
		{
			return save;
		}
	}
}
