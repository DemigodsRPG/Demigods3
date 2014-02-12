package com.demigodsrpg.demigods.engine.data.file;

import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.battle.Battle;
import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.demigodsrpg.demigods.engine.data.DataManager;
import com.demigodsrpg.demigods.engine.data.TempDataManager;
import com.demigodsrpg.demigods.engine.data.TimedData;
import com.demigodsrpg.demigods.engine.entity.DemigodsPet;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsPlayer;
import com.demigodsrpg.demigods.engine.entity.player.attribute.*;
import com.demigodsrpg.demigods.engine.inventory.DemigodsEnderInventory;
import com.demigodsrpg.demigods.engine.inventory.DemigodsPlayerInventory;
import com.demigodsrpg.demigods.engine.item.DemigodsItemStack;
import com.demigodsrpg.demigods.engine.language.English;
import com.demigodsrpg.demigods.engine.tribute.TributeData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * This is the data management file for Demigods.
 */
@SuppressWarnings("unchecked")
public class FileDataManager extends DataManager
{
	// -- VARIABLES -- //

	// Data Folder
	public static final String SAVE_PATH = DemigodsPlugin.getInst().getDataFolder() + "/data/"; // Don't change this.

	// -- YAML FILES -- //

	private static final DemigodsFile<String, DemigodsPlayer> PLAYER = new DemigodsFile<String, DemigodsPlayer>("p.demi", SAVE_PATH)
	{
		@Override
		public DemigodsPlayer create(String mojangAccount, ConfigurationSection conf)
		{
			return new DemigodsPlayer(mojangAccount, conf);
		}

		@Override
		public String convertFromString(String stringId)
		{
			return stringId;
		}
	};
	private static final DemigodsFile<UUID, DemigodsCharacter> CHARACTER = new DemigodsFile<UUID, DemigodsCharacter>("c.demi", SAVE_PATH)
	{
		@Override
		public DemigodsCharacter create(UUID uuid, ConfigurationSection conf)
		{
			return new DemigodsCharacter(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}

	};
	private static final DemigodsFile<UUID, DemigodsCharacterMeta> CHARACTER_META = new DemigodsFile<UUID, DemigodsCharacterMeta>("m.demi", SAVE_PATH)
	{
		@Override
		public DemigodsCharacterMeta create(UUID uuid, ConfigurationSection conf)
		{
			return new DemigodsCharacterMeta(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, Death> DEATH = new DemigodsFile<UUID, Death>("d.demi", SAVE_PATH)
	{
		@Override
		public Death create(UUID uuid, ConfigurationSection conf)
		{
			return new Death(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, Skill> SKILL = new DemigodsFile<UUID, Skill>("s.demi", SAVE_PATH)
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
	private static final DemigodsFile<UUID, DemigodsPlayerInventory> CHARACTER_INVENTORY = new DemigodsFile<UUID, DemigodsPlayerInventory>("i.demi", SAVE_PATH)
	{
		@Override
		public DemigodsPlayerInventory create(UUID uuid, ConfigurationSection conf)
		{
			return new DemigodsPlayerInventory(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, DemigodsEnderInventory> CHARACTER_ENDER_INVENTORY = new DemigodsFile<UUID, DemigodsEnderInventory>("e.demi", SAVE_PATH)
	{
		@Override
		public DemigodsEnderInventory create(UUID uuid, ConfigurationSection conf)
		{
			return new DemigodsEnderInventory(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, DemigodsItemStack> ITEM_STACK = new DemigodsFile<UUID, DemigodsItemStack>("it.demi", SAVE_PATH)
	{
		@Override
		public DemigodsItemStack create(UUID uuid, ConfigurationSection conf)
		{
			return new DemigodsItemStack(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, TimedData> TIMED_DATA = new DemigodsFile<UUID, TimedData>("ti.demi", SAVE_PATH)
	{
		@Override
		public TimedData create(UUID uuid, ConfigurationSection conf)
		{
			return new TimedData(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, DemigodsPotionEffect> SAVED_POTION = new DemigodsFile<UUID, DemigodsPotionEffect>("po.demi", SAVE_PATH)
	{
		@Override
		public DemigodsPotionEffect create(UUID uuid, ConfigurationSection conf)
		{
			return new DemigodsPotionEffect(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, DemigodsPet> PET = new DemigodsFile<UUID, DemigodsPet>("pe.demi", SAVE_PATH)
	{
		@Override
		public DemigodsPet create(UUID uuid, ConfigurationSection conf)
		{
			return new DemigodsPet(uuid, conf);
		}

		@Override
		public UUID convertFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}
	};
	private static final DemigodsFile<UUID, Notification> NOTIFICATION = new DemigodsFile<UUID, Notification>("n.demi", SAVE_PATH)
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
	private static final DemigodsFile<UUID, Battle> BATTLE = new DemigodsFile<UUID, Battle>("b.demi", SAVE_PATH)
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
	private static final DemigodsFile<UUID, TributeData> TRIBUTE_DATA = new DemigodsFile<UUID, TributeData>("tr.demi", SAVE_PATH)
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

	// -- YAML DATA FILES -- //

	public static DemigodsFile[] yamlFiles()
	{
		return new DemigodsFile[] { PLAYER, CHARACTER, CHARACTER_META, DEATH, SKILL, TIMED_DATA, CHARACTER_INVENTORY, CHARACTER_ENDER_INVENTORY, ITEM_STACK, SAVED_POTION, PET, NOTIFICATION, BATTLE, TRIBUTE_DATA };
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

		// Let the plugin know that this has finished.
		didInit = true;
	}

	@Override
	public void save()
	{
		for(DemigodsFile data : yamlFiles())
			data.saveToFile();
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
		TempDataManager.clear();

		save();

		// Reload the PLUGIN
		Bukkit.getServer().getPluginManager().disablePlugin(DemigodsPlugin.getInst());
		Bukkit.getServer().getPluginManager().enablePlugin(DemigodsPlugin.getInst());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends DataAccess<K, V>, K> V getFor(final Class<V> clazz, final K key)
	{
		if(getFile(clazz).containsKey(key)) return getFile(clazz).get(key);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V extends DataAccess<K, V>> Collection<V> getAllOf(final Class<V> clazz)
	{
		return getFile(clazz).values();
	}

	@Override
	public <K, V extends DataAccess<K, V>> ConcurrentMap<K, V> getMapFor(final Class<V> clazz)
	{
		return getFile(clazz).getLoadedData();
	}

	@Override
	public <K, V extends DataAccess<K, V>> void putFor(final Class<V> clazz, final K key, final V value)
	{
		getFile(clazz).put(key, value);
	}

	@Override
	public <K, V extends DataAccess<K, V>> void removeFor(final Class<V> clazz, final K key)
	{
		getFile(clazz).remove(key);
	}

	// FIXME
	// FIXME Switch/case is a bad idea in this file, I'm putting it in here for testing ONLY.
	// FIXME It needs to be replaced with something else, probably if/else.
	// FIXME

	private <K, V extends DataAccess<K, V>> DemigodsFile<K, V> getFile(Class<V> clazz)
	{
		switch(className(clazz, 0))
		{
			case "DemigodsPlayer":
				return (DemigodsFile) PLAYER;
			case "DemigodsCharacter":
				return (DemigodsFile) CHARACTER;
			case "DemigodsCharacterMeta":
				return (DemigodsFile) CHARACTER_META;
			case "DemigodsPlayerInventory":
				return (DemigodsFile) CHARACTER_INVENTORY;
			case "DemigodsItemStack":
				return (DemigodsFile) ITEM_STACK;
			case "TimedData":
				return (DemigodsFile) TIMED_DATA;
			case "DemigodsEnderInventory":
				return (DemigodsFile) CHARACTER_ENDER_INVENTORY;
			case "Death":
				return (DemigodsFile) DEATH;
			case "Skill":
				return (DemigodsFile) SKILL;
			case "DemigodsPet":
				return (DemigodsFile) PET;
			case "Notification":
				return (DemigodsFile) NOTIFICATION;
			case "Battle":
				return (DemigodsFile) BATTLE;
			case "TributeData":
				return (DemigodsFile) TRIBUTE_DATA;
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
}
