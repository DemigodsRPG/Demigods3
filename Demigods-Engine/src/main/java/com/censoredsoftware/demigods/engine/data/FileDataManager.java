package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.censoredlib.data.inventory.CItemStack;
import com.censoredsoftware.censoredlib.data.location.CLocation;
import com.censoredsoftware.censoredlib.data.player.Notification;
import com.censoredsoftware.censoredlib.helper.ConfigFile;
import com.censoredsoftware.censoredlib.helper.MapDBFile;
import com.censoredsoftware.censoredlib.helper.TimedMapDBFile;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.serializable.*;
import com.censoredsoftware.demigods.engine.language.English;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

// FIXME This file has too much stuff in it, the different YAML files should each get their own class file.

/**
 * This is the data management file for Demigods.
 */
public class FileDataManager implements DataManager
{
	// -- VARIABLES -- //

	// Data Folder
	private static final String SAVE_PATH = DemigodsPlugin.plugin().getDataFolder() + "/data/"; // Don't change this.;

	// -- YAML FILES -- //

	private static final DemigodsFile<String, DPlayer> PLAYER = new DemigodsFile<String, DPlayer>("players.yml")
	{
		@Override
		public DPlayer create(String mojangAccount, ConfigurationSection conf)
		{
			return new DPlayer(mojangAccount, conf);
		}

		@Override
		public String convertFromString(String stringId)
		{
			return stringId;
		}
	};
	private static final DemigodsFile<UUID, CLocation> LOCATION = new DemigodsFile<UUID, CLocation>("locations.yml")
	{
		@Override
		public CLocation create(UUID uuid, ConfigurationSection conf)
		{
			return new CLocation(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, StructureSave> STRUCTURE = new DemigodsFile<UUID, StructureSave>("structures.yml")
	{
		@Override
		public StructureSave create(UUID uuid, ConfigurationSection conf)
		{
			return new StructureSave(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, DCharacter> CHARACTER = new DemigodsFile<UUID, DCharacter>("characters.yml")
	{
		@Override
		public DCharacter create(UUID uuid, ConfigurationSection conf)
		{
			return new DCharacter(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}

	};
	private static final DemigodsFile<UUID, DCharacter.Meta> CHARACTER_META = new DemigodsFile<UUID, DCharacter.Meta>("metas.yml")
	{
		@Override
		public DCharacter.Meta create(UUID uuid, ConfigurationSection conf)
		{
			return new DCharacter.Meta(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, DDeath> DEATH = new DemigodsFile<UUID, DDeath>("deaths.yml")
	{
		@Override
		public DDeath create(UUID uuid, ConfigurationSection conf)
		{
			return new DDeath(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, Skill> SKILL = new DemigodsFile<UUID, Skill>("skills.yml")
	{
		@Override
		public Skill create(UUID uuid, ConfigurationSection conf)
		{
			return new Skill(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, DCharacter.Inventory> CHARACTER_INVENTORY = new DemigodsFile<UUID, DCharacter.Inventory>("inventories.yml")
	{
		@Override
		public DCharacter.Inventory create(UUID uuid, ConfigurationSection conf)
		{
			return new DCharacter.Inventory(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, DCharacter.EnderInventory> CHARACTER_ENDER_INVENTORY = new DemigodsFile<UUID, DCharacter.EnderInventory>("enderInventories.yml")
	{
		@Override
		public DCharacter.EnderInventory create(UUID uuid, ConfigurationSection conf)
		{
			return new DCharacter.EnderInventory(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, CItemStack> ITEM_STACK = new DemigodsFile<UUID, CItemStack>("itemstacks.yml")
	{
		@Override
		public CItemStack create(UUID uuid, ConfigurationSection conf)
		{
			return new CItemStack(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, DSavedPotion> SAVED_POTION = new DemigodsFile<UUID, DSavedPotion>("savedpotions.yml")
	{
		@Override
		public DSavedPotion create(UUID uuid, ConfigurationSection conf)
		{
			return new DSavedPotion(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, DPet> PET = new DemigodsFile<UUID, DPet>("pets.yml")
	{
		@Override
		public DPet create(UUID uuid, ConfigurationSection conf)
		{
			return new DPet(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, Notification> NOTIFICATION = new DemigodsFile<UUID, Notification>("notifications.yml")
	{
		@Override
		public Notification create(UUID uuid, ConfigurationSection conf)
		{
			return new Notification(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, Battle> BATTLE = new DemigodsFile<UUID, Battle>("battles.yml")
	{
		@Override
		public Battle create(UUID uuid, ConfigurationSection conf)
		{
			return new Battle(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, TributeData> TRIBUTE_DATA = new DemigodsFile<UUID, TributeData>("tributedata.yml")
	{
		@Override
		public TributeData create(UUID uuid, ConfigurationSection conf)
		{
			return new TributeData(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};

	// -- BINARY & MISC DATA FILES -- //

	// Timed and Server Data
	public static final TimedMapDBFile TIMED = new TimedMapDBFile("TIMED.dg", SAVE_PATH + "bin/");
	public static final MapDBFile SERVER = new MapDBFile("SERVER.dg", SAVE_PATH + "bin/");

	public static DemigodsFile[] yamlFiles()
	{
		return new DemigodsFile[] { PLAYER, LOCATION, STRUCTURE, CHARACTER, CHARACTER_META, DEATH, SKILL, CHARACTER_INVENTORY, CHARACTER_ENDER_INVENTORY, ITEM_STACK, SAVED_POTION, PET, NOTIFICATION, BATTLE, TRIBUTE_DATA };
	}

	// -- UTIL METHODS -- //

	// Prevent accidental double init.
	private static boolean didInit = false;

	@Override
	public void init()
	{
		// Check if init has happened already...
		if(didInit) throw new RuntimeException("Data tried to initialize more than once.");

		// Load YAML files.
		for(DemigodsFile data : yamlFiles())
			data.loadToData();

		// Load world data.
		for(World world : Bukkit.getWorlds())
			WorldDataManager.addWorld(world);

		// Let the plugin know that this has finished.
		didInit = true;
	}

	@Override
	public void save()
	{
		boolean compact = System.currentTimeMillis() % 100000000L == 0;

		for(DemigodsFile data : yamlFiles())
			data.saveToFile();

		for(World world : Bukkit.getWorlds())
		{
			WorldData dWorld = WorldDataManager.getWorld(world.getName());
			dWorld.save();
			if(compact) dWorld.compact();
		}

		TIMED.save();
		SERVER.save();
	}

	@Override
	public void flushData()
	{
		// Kick everyone
		for(Player player : Bukkit.getOnlinePlayers())
			player.kickPlayer(ChatColor.GREEN + English.DATA_RESET_KICK.getLine());

		// Clear the data
		for(DemigodsFile data : yamlFiles())
			data.clear();
		TempDataManager.TEMP.clear();
		TIMED.clear();
		SERVER.clear();

		save();

		// Reload the PLUGIN
		Bukkit.getServer().getPluginManager().disablePlugin(DemigodsPlugin.plugin());
		Bukkit.getServer().getPluginManager().enablePlugin(DemigodsPlugin.plugin());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, K> T getFor(final Class<T> clazz, final K key)
	{
		final String secondFromLast = className(clazz, 1);
		switch(className(clazz, 0))
		{
			case "DPlayer":
				return (T) PLAYER.__get(key);
			case "DCharacter":
				return (T) CHARACTER.__get(key);
			case "Meta":
			{
				if("DCharacter".equals(secondFromLast)) return (T) CHARACTER_META.__get(key);
			}
			case "Inventory":
			{
				if("DCharacter".equals(secondFromLast)) return (T) CHARACTER_INVENTORY.__get(key);
			}
			case "CItemStack":
				return (T) ITEM_STACK.__get(key);
			case "CLocation":
				return (T) LOCATION.__get(key);
			case "StructureSave":
				return (T) STRUCTURE.__get(key);
			case "EnderInventory":
			{
				if("DCharacter".equals(secondFromLast)) return (T) CHARACTER_ENDER_INVENTORY.__get(key);
			}
			case "DDeath":
				return (T) DEATH.__get(key);
			case "Skill":
				return (T) SKILL.__get(key);
			case "DPet":
				return (T) PET.__get(key);
			case "Notification":
				return (T) NOTIFICATION.__get(key);
			case "Battle":
				return (T) BATTLE.__get(key);
			case "TributeData":
				return (T) TRIBUTE_DATA.__get(key);
			default:
				throw new IllegalArgumentException("Not acceptable.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getAllOf(final Class<T> clazz)
	{
		final String secondFromLast = className(clazz, 1);
		switch(className(clazz, 0))
		{
			case "DPlayer":
				return (Collection<T>) PLAYER.values();
			case "DCharacter":
				return (Collection<T>) CHARACTER.values();
			case "Meta":
			{
				if("DCharacter".equals(secondFromLast)) return (Collection<T>) CHARACTER_META.values();
			}
			case "Inventory":
			{
				if("DCharacter".equals(secondFromLast)) return (Collection<T>) CHARACTER_INVENTORY.values();
			}
			case "CItemStack":
				return (Collection<T>) ITEM_STACK.values();
			case "CLocation":
				return (Collection<T>) LOCATION.values();
			case "StructureSave":
				return (Collection<T>) STRUCTURE.values();
			case "EnderInventory":
			{
				if("DCharacter".equals(secondFromLast)) return (Collection<T>) CHARACTER_ENDER_INVENTORY.values();
			}
			case "DDeath":
				return (Collection<T>) DEATH.values();
			case "Skill":
				return (Collection<T>) SKILL.values();
			case "DPet":
				return (Collection<T>) PET.values();
			case "Notification":
				return (Collection<T>) NOTIFICATION.values();
			case "Battle":
				return (Collection<T>) BATTLE.values();
			case "TributeData":
				return (Collection<T>) TRIBUTE_DATA.values();
			default:
				throw new IllegalArgumentException("Not acceptable.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ConcurrentMap<?, T> getMapFor(final Class<T> clazz)
	{
		final String secondFromLast = className(clazz, 1);
		switch(className(clazz, 0))
		{
			case "DPlayer":
				return (ConcurrentMap<?, T>) PLAYER.dataStore;
			case "DCharacter":
				return (ConcurrentMap<?, T>) CHARACTER.dataStore;
			case "Meta":
			{
				if("DCharacter".equals(secondFromLast)) return (ConcurrentMap<?, T>) CHARACTER_META.dataStore;
			}
			case "Inventory":
			{
				if("DCharacter".equals(secondFromLast)) return (ConcurrentMap<?, T>) CHARACTER_INVENTORY.dataStore;
			}
			case "CItemStack":
				return (ConcurrentMap<?, T>) ITEM_STACK.dataStore;
			case "CLocation":
				return (ConcurrentMap<?, T>) LOCATION.dataStore;
			case "StructureSave":
				return (ConcurrentMap<?, T>) STRUCTURE.dataStore;
			case "EnderInventory":
			{
				if("DCharacter".equals(secondFromLast)) return (ConcurrentMap<?, T>) CHARACTER_ENDER_INVENTORY.dataStore;
			}
			case "DDeath":
				return (ConcurrentMap<?, T>) DEATH.dataStore;
			case "Skill":
				return (ConcurrentMap<?, T>) SKILL.dataStore;
			case "DPet":
				return (ConcurrentMap<?, T>) PET.dataStore;
			case "Notification":
				return (ConcurrentMap<?, T>) NOTIFICATION.dataStore;
			case "Battle":
				return (ConcurrentMap<?, T>) BATTLE.dataStore;
			case "TributeData":
				return (ConcurrentMap<?, T>) TRIBUTE_DATA.dataStore;
			default:
				throw new IllegalArgumentException("Not acceptable.");
		}
	}

	@Override
	public <T, K> void saveFor(final Class<T> clazz, final K key, final T value)
	{
		final String secondFromLast = className(clazz, 1);
		switch(className(clazz, 0))
		{
			case "DPlayer":
			{
				PLAYER.__put(key, value);
				break;
			}
			case "DCharacter":
			{
				CHARACTER.__put(key, value);
				break;
			}
			case "Meta":
			{
				if("DCharacter".equals(secondFromLast))
				{
					CHARACTER_META.__put(key, value);
					break;
				}
			}
			case "Inventory":
			{
				if("DCharacter".equals(secondFromLast))
				{
					CHARACTER_INVENTORY.__put(key, value);
					break;
				}
			}
			case "CItemStack":
			{
				ITEM_STACK.__put(key, value);
				break;
			}
			case "CLocation":
			{
				LOCATION.__put(key, value);
				break;
			}
			case "StructureSave":
			{
				STRUCTURE.__put(key, value);
				break;
			}
			case "EnderInventory":
			{
				if("DCharacter".equals(secondFromLast))
				{
					CHARACTER_ENDER_INVENTORY.__put(key, value);
					break;
				}
			}
			case "DDeath":
			{
				DEATH.__put(key, value);
				break;
			}
			case "Skill":
			{
				SKILL.__put(key, value);
				break;
			}
			case "DPet":
			{
				PET.__put(key, value);
				break;
			}
			case "Notification":
			{
				NOTIFICATION.__put(key, value);
				break;
			}
			case "Battle":
			{
				BATTLE.__put(key, value);
				break;
			}
			case "TributeData":
			{
				TRIBUTE_DATA.__put(key, value);
				break;
			}
			default:
				throw new IllegalArgumentException("Not acceptable.");
		}
	}

	@Override
	public <T, K> void removeFor(final Class<T> clazz, final K key)
	{
		final String secondFromLast = className(clazz, 1);
		switch(className(clazz, 0))
		{
			case "DPlayer":
			{
				PLAYER.__remove(key);
				break;
			}
			case "DCharacter":
			{
				CHARACTER.__remove(key);
				break;
			}
			case "Meta":
			{
				if("DCharacter".equals(secondFromLast))
				{
					CHARACTER_META.__remove(key);
					break;
				}
			}
			case "Inventory":
			{
				if("DCharacter".equals(secondFromLast))
				{
					CHARACTER_INVENTORY.__remove(key);
					break;
				}
			}
			case "CItemStack":
			{
				ITEM_STACK.__remove(key);
				break;
			}
			case "CLocation":
			{
				LOCATION.__remove(key);
				break;
			}
			case "StructureSave":
			{
				STRUCTURE.__remove(key);
				break;
			}
			case "EnderInventory":
			{
				if("DCharacter".equals(secondFromLast))
				{
					CHARACTER_ENDER_INVENTORY.__remove(key);
					break;
				}
			}
			case "DDeath":
			{
				DEATH.__remove(key);
				break;
			}
			case "Skill":
			{
				SKILL.__remove(key);
				break;
			}
			case "DPet":
			{
				PET.__remove(key);
				break;
			}
			case "Notification":
			{
				NOTIFICATION.__remove(key);
				break;
			}
			case "Battle":
			{
				BATTLE.__remove(key);
				break;
			}
			case "TributeData":
			{
				TRIBUTE_DATA.__remove(key);
				break;
			}
			default:
				throw new IllegalArgumentException("Not acceptable.");
		}
	}

	private String className(Class clazz, int fromTheEnd)
	{
		String canonicalName = clazz.getCanonicalName();
		if(canonicalName != null)
		{
			String[] parts = canonicalName.split("\\.");
			return parts[parts.length - (1 + fromTheEnd)];
		}
		return "";
	}

	private UUID uuid(String id)
	{
		return UUID.fromString(id);
	}

	/**
	 * Abstract class extending ConfigFile for easy yaml file creation inside of Demigods.
	 * 
	 * @param <ID> The id type.
	 * @param <DATA> The data type.
	 */
	public abstract static class DemigodsFile<ID, DATA extends ConfigurationSerializable> extends ConfigFile<ID, DATA>
	{
		private final String saveFile;
		private ConcurrentMap<ID, DATA> dataStore = Maps.newConcurrentMap();

		protected DemigodsFile(String saveFile)
		{
			this.saveFile = saveFile;
		}

		@Override
		public final ConcurrentMap<ID, DATA> getLoadedData()
		{
			return dataStore;
		}

		@Override
		public final Map<String, Object> serialize(ID id)
		{
			return getLoadedData().get(id).serialize();
		}

		@Override
		public String getSavePath()
		{
			return SAVE_PATH;
		}

		@Override
		public final String getSaveFile()
		{
			return saveFile;
		}

		@Override
		public final void loadToData()
		{
			dataStore = loadFromFile();
		}

		public final boolean containsKey(ID key)
		{
			return dataStore.containsKey(key);
		}

		public final DATA get(ID key)
		{
			return dataStore.get(key);
		}

		@SuppressWarnings("unchecked")
		private DATA __get(Object key)
		{
			return dataStore.get((ID) key);
		}

		public final void put(ID key, DATA value)
		{
			dataStore.put(key, value);
		}

		@SuppressWarnings("unchecked")
		private void __put(Object key, Object value)
		{
			dataStore.put((ID) key, (DATA) value);
		}

		public final void remove(ID key)
		{
			dataStore.remove(key);
		}

		@SuppressWarnings("unchecked")
		private void __remove(Object key)
		{
			dataStore.remove((ID) key);
		}

		public final Set<ID> keySet()
		{
			return dataStore.keySet();
		}

		public final Set<Map.Entry<ID, DATA>> entrySet()
		{
			return dataStore.entrySet();
		}

		public final Collection<DATA> values()
		{
			return dataStore.values();
		}

		public final void clear()
		{
			dataStore.clear();
		}
	}
}
