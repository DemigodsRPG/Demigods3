package com.censoredsoftware.Demigods.Engine;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import org.bukkit.plugin.Plugin;
import org.mcstats.Metrics;

import com.censoredsoftware.Demigods.DemigodsPlugin;
import com.censoredsoftware.Demigods.Engine.Command.DemigodsCommand;
import com.censoredsoftware.Demigods.Engine.Command.DevelopmentCommands;
import com.censoredsoftware.Demigods.Engine.Command.GeneralCommands;
import com.censoredsoftware.Demigods.Engine.Listener.*;
import com.censoredsoftware.Demigods.Engine.Module.BukkitDevModule;
import com.censoredsoftware.Demigods.Engine.Module.ConfigModule;
import com.censoredsoftware.Demigods.Engine.Module.MessageModule;
import com.censoredsoftware.Demigods.Engine.Object.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.Object.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Object.Language.Translation;
import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureInfo;
import com.censoredsoftware.Demigods.Engine.Object.Task.Task;
import com.censoredsoftware.Demigods.Engine.Object.Task.TaskSet;
import com.censoredsoftware.Demigods.Engine.Utility.DataUtility;
import com.censoredsoftware.Demigods.Engine.Utility.SchedulerUtility;
import com.censoredsoftware.Demigods.Engine.Utility.TextUtility;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Demigods
{
	// Public Static Access
	public static DemigodsPlugin plugin;

	// Public Modules
	public static ConfigModule config;
	public static MessageModule message;

	// Public Dependency Plugins
	public static WorldGuardPlugin worldguard;

	// Protected Modules
	protected static BukkitDevModule update;

	// The Game Data
	protected static Deque<Deity> deities;
	protected static Deque<TaskSet> quests;
	protected static Deque<StructureInfo> structures;

	// The Engline Default Text
	public static Translation text;

	public interface ListedDeity
	{
		public Deity getDeity();
	}

	public interface ListedTaskSet
	{
		public TaskSet getTaskSet();
	}

	public interface ListedStructure
	{
		public StructureInfo getStructure();
	}

	public Demigods(DemigodsPlugin instance, final ListedDeity[] deities, final ListedTaskSet[] taskSets, final ListedStructure[] structures)
	{
		// Allow static access.
		plugin = instance;

		// Setup public modules.
		config = new ConfigModule(instance, true);
		message = new MessageModule(instance, config.getSettingBoolean("misc.tag_messages"));

		// Setup protected modules.
		update = new BukkitDevModule(instance, "http://dev.bukkit.org/server-mods/demigods/files.rss", "/dg update", "demigods.update", config.getSettingBoolean("update.auto"), config.getSettingBoolean("update.notify"), 10);

		// Define the game data.
		Demigods.deities = new ArrayDeque<Deity>()
		{
			{
				for(ListedDeity deity : deities)
				{
					add(deity.getDeity());
				}
			}
		};
		Demigods.quests = new ArrayDeque<TaskSet>()
		{
			{
				for(ListedTaskSet taskSet : taskSets)
				{
					add(taskSet.getTaskSet());
				}
			}
		};
		Demigods.structures = new ArrayDeque<StructureInfo>()
		{
			{
				for(ListedStructure structure : structures)
				{
					add(structure.getStructure());
				}
			}
		};

		Demigods.text = getTranslation();

		// Initialize soft data.
		new DataUtility();
		if(!DataUtility.isConnected())
		{
			message.severe("Demigods was unable to connect to a redis server.");
			message.severe("A redis server is REQUIRED for Demigods to run.");
			message.severe("Please install and configure a redis server.");
			instance.getServer().getPluginManager().disablePlugin(instance);
			return;
		}

		// Initialize metrics.
		try
		{
			Metrics metrics = new Metrics(instance);
			// metrics.start();
		}
		catch(IOException ignored)
		{}

		// Finish loading the demigods based on the game data.
		loadDepends(instance);
		loadListeners(instance);
		loadCommands(instance);

		// Finally, regenerate structures
		// DemigodsBlock.regenerateStructures();

		// Start game threads.
		SchedulerUtility.startThreads(instance);

		message.info("Successfully enabled.");
	}

	/**
	 * Get the translation involved.
	 * 
	 * @return The translation.
	 */
	public Translation getTranslation()
	{
		// Default to EnglishCharNames
		return new TextUtility.English();
	}

	protected static void loadListeners(DemigodsPlugin instance)
	{
		// Engine
		instance.getServer().getPluginManager().registerEvents(new AbilityListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new BattleListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new CharacterListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new ChatListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new ChunkListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new CommandListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new EntityListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new PlayerListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new StructureListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new PrayerListener(), instance);

		// Deities
		for(Deity deity : getLoadedDeities())
		{
			if(deity.getAbilities() == null) continue;
			for(Ability ability : deity.getAbilities())
			{
				instance.getServer().getPluginManager().registerEvents(ability.getListener(), instance);
			}
		}

		// Tasks
		for(TaskSet quest : getLoadedQuests())
		{
			if(quest.getTasks() == null) continue;
			for(Task task : quest.getTasks())
			{
				instance.getServer().getPluginManager().registerEvents(task.getListener(), instance);
			}
		}

		// Structures
		for(StructureInfo structure : getLoadedStructures())
		{
			if(structure.getUniqueListener() == null) continue;
			instance.getServer().getPluginManager().registerEvents(structure.getUniqueListener(), instance);
		}
	}

	protected static void loadCommands(DemigodsPlugin instance)
	{
		// Main Demigods Command
		instance.getCommand("demigods").setExecutor(new DemigodsCommand());

		// General Commands
		GeneralCommands genCommands = new GeneralCommands();
		instance.getCommand("check").setExecutor(genCommands);
		instance.getCommand("owner").setExecutor(genCommands);

		// Development/Backend Commands
		DevelopmentCommands devCommands = new DevelopmentCommands();
		instance.getCommand("test1").setExecutor(devCommands);
		instance.getCommand("test2").setExecutor(devCommands);
		instance.getCommand("soundtest").setExecutor(devCommands);
		instance.getCommand("removechar").setExecutor(devCommands);
	}

	protected static void loadDepends(DemigodsPlugin instance)
	{
		// WorldGuard
		Plugin depend = instance.getServer().getPluginManager().getPlugin("WorldGuard");
		if(depend instanceof WorldGuardPlugin) worldguard = (WorldGuardPlugin) depend;
	}

	public static Deque<Deity> getLoadedDeities()
	{
		return Demigods.deities;
	}

	public static Deque<TaskSet> getLoadedQuests()
	{
		return Demigods.quests;
	}

	public static Deque<StructureInfo> getLoadedStructures()
	{
		return Demigods.structures;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
