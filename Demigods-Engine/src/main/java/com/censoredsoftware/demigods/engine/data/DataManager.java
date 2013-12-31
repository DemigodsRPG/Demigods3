package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.censoredlib.data.ServerData;
import com.censoredsoftware.censoredlib.data.TimedData;
import com.censoredsoftware.censoredlib.data.inventory.CItemStack;
import com.censoredsoftware.censoredlib.data.location.CLocation;
import com.censoredsoftware.censoredlib.data.player.Notification;
import com.censoredsoftware.censoredlib.helper.ConfigFile;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.battle.Battle;
import com.censoredsoftware.demigods.engine.data.util.ServerDatas;
import com.censoredsoftware.demigods.engine.data.util.TimedDatas;
import com.censoredsoftware.demigods.engine.language.Text;
import com.censoredsoftware.demigods.engine.player.*;
import com.censoredsoftware.demigods.engine.structure.StructureData;
import com.google.common.base.Supplier;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataManager
{
	// Data
	public static ConcurrentMap<String, DPlayer> players;
	public static ConcurrentMap<UUID, CLocation> locations;
	public static ConcurrentMap<UUID, StructureData> structures;
	public static ConcurrentMap<UUID, DCharacter> characters;
	public static ConcurrentMap<UUID, DCharacter.Meta> characterMetas;
	public static ConcurrentMap<UUID, DDeath> deaths;
	public static ConcurrentMap<UUID, Skill> skills;
	public static ConcurrentMap<UUID, DCharacter.Inventory> inventories;
	public static ConcurrentMap<UUID, DCharacter.EnderInventory> enderInventories;
	public static ConcurrentMap<UUID, CItemStack> itemStacks;
	public static ConcurrentMap<UUID, DSavedPotion> savedPotions;
	public static ConcurrentMap<UUID, DPet> pets;
	public static ConcurrentMap<UUID, Notification> notifications;
	public static ConcurrentMap<UUID, Battle> battles;
	public static ConcurrentMap<UUID, TimedData> timedData;
	public static ConcurrentMap<UUID, ServerData> serverData;
	public static ConcurrentMap<UUID, TributeData> tributeData;
	private static Table<String, String, Object> tempData;

	public static final String SAVE_PATH;

	static
	{
		// Data folder
		SAVE_PATH = DemigodsPlugin.plugin().getDataFolder() + "/data/"; // Don't change this.

		for(File file : File.values())
			file.getConfigFile().loadToData();
		tempData = Tables.newCustomTable(new ConcurrentHashMap<String, Map<String, Object>>(), new Supplier<ConcurrentHashMap<String, Object>>()
		{
			@Override
			public ConcurrentHashMap<String, Object> get()
			{
				return new ConcurrentHashMap<>();
			}
		});
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
			player.kickPlayer(ChatColor.GREEN + Text.DATA_RESET_KICK.english());

		// Clear the data
		itemStacks.clear();
		locations.clear();
		players.clear();
		characters.clear();
		characterMetas.clear();
		inventories.clear();
		enderInventories.clear();
		skills.clear();
		notifications.clear();
		pets.clear();
		structures.clear();
		battles.clear();
		deaths.clear();
		timedData.clear();
		savedPotions.clear();
		tempData.clear();
		serverData.clear();

		save();

		// Reload the PLUGIN
		Bukkit.getServer().getPluginManager().disablePlugin(DemigodsPlugin.plugin());
		Bukkit.getServer().getPluginManager().enablePlugin(DemigodsPlugin.plugin());
	}

	/*
	 * Temporary data
	 */
	public static boolean hasKeyTemp(String row, String column)
	{
		return tempData.contains(row, column);
	}

	public static Object getValueTemp(String row, String column)
	{
		if(hasKeyTemp(row, column)) return tempData.get(row, column);
		else return null;
	}

	public static void saveTemp(String row, String column, Object value)
	{
		tempData.put(row, column, value);
	}

	public static void removeTemp(String row, String column)
	{
		if(hasKeyTemp(row, column)) tempData.remove(row, column);
	}

	/*
	 * Timed data
	 */
	public static void saveTimed(String key, String subKey, Object data, Integer seconds)
	{
		// Remove the data if it exists already
		TimedDatas.remove(key, subKey);

		// Create and save the timed data
		TimedData timedData = new TimedData();
		timedData.generateId();
		timedData.setKey(key);
		timedData.setSubKey(subKey);
		timedData.setData(data.toString());
		timedData.setSeconds(seconds);
		DataManager.timedData.put(timedData.getId(), timedData);
	}

	/*
	 * Timed data
	 */
	public static void saveTimedWeek(String key, String subKey, Object data)
	{
		// Remove the data if it exists already
		TimedDatas.remove(key, subKey);

		// Create and save the timed data
		TimedData timedData = new TimedData();
		timedData.generateId();
		timedData.setKey(key);
		timedData.setSubKey(subKey);
		timedData.setData(data.toString());
		timedData.setHours(168);
		DataManager.timedData.put(timedData.getId(), timedData);
	}

	public static void removeTimed(String key, String subKey)
	{
		TimedDatas.remove(key, subKey);
	}

	public static boolean hasTimed(String key, String subKey)
	{
		return TimedDatas.find(key, subKey) != null;
	}

	public static Object getTimedValue(String key, String subKey)
	{
		return TimedDatas.find(key, subKey).getData();
	}

	public static long getTimedExpiration(String key, String subKey)
	{
		return TimedDatas.find(key, subKey).getExpiration();
	}

	/*
	 * Server data
	 */
	public static void saveServerData(String key, String subKey, Object data)
	{
		// Remove the data if it exists already
		ServerDatas.remove(key, subKey);

		// Create and save the timed data
		ServerData serverData = new ServerData();
		serverData.generateId();
		serverData.setKey(key);
		serverData.setSubKey(subKey);
		serverData.setData(data.toString());
		DataManager.serverData.put(serverData.getId(), serverData);
	}

	public static void removeServerData(String key, String subKey)
	{
		ServerDatas.remove(key, subKey);
	}

	public static boolean hasServerData(String key, String subKey)
	{
		return ServerDatas.find(key, subKey) != null;
	}

	public static Object getServerDataValue(String key, String subKey)
	{
		return ServerDatas.find(key, subKey).getData();
	}

	public static enum File
	{
		PLAYER(new ConfigFile<String, DPlayer>()
		{
			@Override
			public DPlayer create(String mojangAccount, ConfigurationSection conf)
			{
				return new DPlayer(mojangAccount, conf);
			}

			@Override
			public ConcurrentMap<String, DPlayer> getLoadedData()
			{
				return DataManager.players;
			}

			@Override
			public String getSavePath()
			{
				return SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "players.yml";
			}

			@Override
			public Map<String, Object> serialize(String id)
			{
				return getLoadedData().get(id).serialize();
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
		}), LOCATION(new ConfigFile<UUID, CLocation>()
		{
			@Override
			public CLocation create(UUID uuid, ConfigurationSection conf)
			{
				return new CLocation(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, CLocation> getLoadedData()
			{
				return DataManager.locations;
			}

			@Override
			public String getSavePath()
			{
				return SAVE_PATH;
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
		}), STRUCTURE(new ConfigFile<UUID, StructureData>()
		{
			@Override
			public StructureData create(UUID uuid, ConfigurationSection conf)
			{
				return new StructureData(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, StructureData> getLoadedData()
			{
				return DataManager.structures;
			}

			@Override
			public String getSavePath()
			{
				return SAVE_PATH;
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
				return SAVE_PATH;
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
				return SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "metas.yml";
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
		}), DEATH(new ConfigFile<UUID, DDeath>()
		{
			@Override
			public DDeath create(UUID uuid, ConfigurationSection conf)
			{
				return new DDeath(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, DDeath> getLoadedData()
			{
				return DataManager.deaths;
			}

			@Override
			public String getSavePath()
			{
				return SAVE_PATH;
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
				return SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "skills.yml";
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
				return SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "inventories.yml";
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
		}), CHARACTER_ENDER_INVENTORY(new ConfigFile<UUID, DCharacter.EnderInventory>()
		{
			@Override
			public DCharacter.EnderInventory create(UUID uuid, ConfigurationSection conf)
			{
				return new DCharacter.EnderInventory(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, DCharacter.EnderInventory> getLoadedData()
			{
				return DataManager.enderInventories;
			}

			@Override
			public String getSavePath()
			{
				return SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "enderInventories.yml";
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
				enderInventories = loadFromFile();
			}
		}), ITEM_STACK(new ConfigFile<UUID, CItemStack>()
		{
			@Override
			public CItemStack create(UUID uuid, ConfigurationSection conf)
			{
				return new CItemStack(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, CItemStack> getLoadedData()
			{
				return DataManager.itemStacks;
			}

			@Override
			public String getSavePath()
			{
				return SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "itemstacks.yml";
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
		}), SAVED_POTION(new ConfigFile<UUID, DSavedPotion>()
		{
			@Override
			public DSavedPotion create(UUID uuid, ConfigurationSection conf)
			{
				return new DSavedPotion(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, DSavedPotion> getLoadedData()
			{
				return DataManager.savedPotions;
			}

			@Override
			public String getSavePath()
			{
				return SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "savedpotions.yml";
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
		}), PET(new ConfigFile<UUID, DPet>()
		{
			@Override
			public DPet create(UUID uuid, ConfigurationSection conf)
			{
				return new DPet(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, DPet> getLoadedData()
			{
				return DataManager.pets;
			}

			@Override
			public String getSavePath()
			{
				return SAVE_PATH;
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
				return SAVE_PATH;
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
				return SAVE_PATH;
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
				return SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "timeddata.yml";
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
		}), SERVER_DATA(new ConfigFile<UUID, ServerData>()
		{
			@Override
			public ServerData create(UUID uuid, ConfigurationSection conf)
			{
				return new ServerData(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, ServerData> getLoadedData()
			{
				return DataManager.serverData;
			}

			@Override
			public String getSavePath()
			{
				return SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "serverdata.yml";
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
				serverData = loadFromFile();
			}
		}), TRIBUTE_DATA(new ConfigFile<UUID, TributeData>()
		{
			@Override
			public TributeData create(UUID uuid, ConfigurationSection conf)
			{
				return new TributeData(uuid, conf);
			}

			@Override
			public ConcurrentMap<UUID, TributeData> getLoadedData()
			{
				return DataManager.tributeData;
			}

			@Override
			public String getSavePath()
			{
				return SAVE_PATH;
			}

			@Override
			public String getSaveFile()
			{
				return "tributedata.yml";
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
				tributeData = loadFromFile();
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
