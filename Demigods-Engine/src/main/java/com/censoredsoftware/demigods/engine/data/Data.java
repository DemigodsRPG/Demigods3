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
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This is the data management file for Demigods.
 */
public class Data
{
	// -- VARIABLES -- //

	// Data Folder
	public static final String SAVE_PATH = DemigodsPlugin.plugin().getDataFolder() + "/data/"; // Don't change this.;

	// -- YAML FILES -- //

	public static final DemigodsFile<String, DPlayer> PLAYER = new DemigodsFile<String, DPlayer>("players.yml")
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
	public static final DemigodsFile<UUID, CLocation> LOCATION = new DemigodsFile<UUID, CLocation>("locations.yml")
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
	public static final DemigodsFile<UUID, StructureSave> STRUCTURE = new DemigodsFile<UUID, StructureSave>("structures.yml")
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
	public static final DemigodsFile<UUID, DCharacter> CHARACTER = new DemigodsFile<UUID, DCharacter>("characters.yml")
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
	public static final DemigodsFile<UUID, DCharacter.Meta> CHARACTER_META = new DemigodsFile<UUID, DCharacter.Meta>("metas.yml")
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
	public static final DemigodsFile<UUID, DDeath> DEATH = new DemigodsFile<UUID, DDeath>("deaths.yml")
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
	public static final DemigodsFile<UUID, Skill> SKILL = new DemigodsFile<UUID, Skill>("skills.yml")
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
	public static final DemigodsFile<UUID, DCharacter.Inventory> CHARACTER_INVENTORY = new DemigodsFile<UUID, DCharacter.Inventory>("inventories.yml")
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
	public static final DemigodsFile<UUID, DCharacter.EnderInventory> CHARACTER_ENDER_INVENTORY = new DemigodsFile<UUID, DCharacter.EnderInventory>("enderInventories.yml")
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
	public static final DemigodsFile<UUID, CItemStack> ITEM_STACK = new DemigodsFile<UUID, CItemStack>("itemstacks.yml")
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
	public static final DemigodsFile<UUID, DSavedPotion> SAVED_POTION = new DemigodsFile<UUID, DSavedPotion>("savedpotions.yml")
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
	public static final DemigodsFile<UUID, DPet> PET = new DemigodsFile<UUID, DPet>("pets.yml")
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
	public static final DemigodsFile<UUID, Notification> NOTIFICATION = new DemigodsFile<UUID, Notification>("notifications.yml")
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
	public static final DemigodsFile<UUID, Battle> BATTLE = new DemigodsFile<UUID, Battle>("battles.yml")
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
	public static final DemigodsFile<UUID, TributeData> TRIBUTE_DATA = new DemigodsFile<UUID, TributeData>("tributedata.yml")
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
	public static final TimedMapDBFile TIMED = new TimedMapDBFile("TIMED.dg", SAVE_PATH);
	public static final MapDBFile SERVER = new MapDBFile("SERVER.dg", SAVE_PATH);

	// -- CONSTRUCTOR -- //

	private Data()
	{}

	// -- UTIL METHODS -- //

	public static void init()
	{
		for(DemigodsFile data : yamlFiles())
			data.loadToData();

		for(World world : Bukkit.getWorlds())
			addWorld(world);
	}

	public static DemigodsFile[] yamlFiles()
	{
		return new DemigodsFile[] { PLAYER, LOCATION, STRUCTURE, CHARACTER, CHARACTER_META, DEATH, SKILL, CHARACTER_INVENTORY, CHARACTER_ENDER_INVENTORY, ITEM_STACK, SAVED_POTION, PET, NOTIFICATION, BATTLE, TRIBUTE_DATA };
	}

	public static void save()
	{
		boolean compact = System.currentTimeMillis() % 100000000L == 0;

		for(DemigodsFile data : yamlFiles())
			data.saveToFile();

		for(World world : Bukkit.getWorlds())
		{
			WorldData dWorld = getWorld(world.getName());
			dWorld.save();
			if(compact) dWorld.compact();
		}

		TIMED.save();
		SERVER.save();
	}

	public static void flushData()
	{
		// Kick everyone
    for(Player player : Bukkit.getOnlinePlayers())
			player.kickPlayer(ChatColor.GREEN + English.DATA_RESET_KICK.getLine());

		// Clear the data
		for(DemigodsFile data : yamlFiles())
			data.clear();
		TEMP.clear();

		save();

		// Reload the PLUGIN
		Bukkit.getServer().getPluginManager().disablePlugin(DemigodsPlugin.plugin());
		Bukkit.getServer().getPluginManager().enablePlugin(DemigodsPlugin.plugin());
	}

	// -- WORLD DATA -- //

	// World Data
	private static final ConcurrentMap<String, WorldData> WORLDS = Maps.newConcurrentMap();

	public static void addWorld(World world)
	{
		WorldData dWorld = new WorldData(world.getName(), world.getWorldFolder().getPath());
		WORLDS.put(world.getName(), dWorld);
	}

	public static void addWorld(WorldData world)
	{
		WORLDS.put(world.getName(), world);
	}

	public static void removeWorld(String name)
	{
		WORLDS.get(name).save();
		WORLDS.remove(name);
	}

	public static WorldData getWorld(String name)
	{
		return WORLDS.get(name);
	}

	// -- TEMP DATA -- //

	// Temp Data
	private static final Table<String, String, Object> TEMP = Tables.newCustomTable(new ConcurrentHashMap<String, Map<String, Object>>(), new Supplier<ConcurrentHashMap<String, Object>>()
	{
		@Override
		public ConcurrentHashMap<String, Object> get()
		{
			return new ConcurrentHashMap<>();
		}
	});

	public static boolean hasKeyTemp(String row, String column)
	{
		return TEMP.contains(row, column);
	}

	public static Object getValueTemp(String row, String column)
	{
		if(hasKeyTemp(row, column)) return TEMP.get(row, column);
		else return null;
	}

	public static void saveTemp(String row, String column, Object value)
	{
		TEMP.put(row, column, value);
	}

	public static void removeTemp(String row, String column)
	{
		if(hasKeyTemp(row, column)) TEMP.remove(row, column);
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

		public final void put(ID key, DATA value)
		{
			dataStore.put(key, value);
		}

		public final void remove(ID key)
		{
			dataStore.remove(key);
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
