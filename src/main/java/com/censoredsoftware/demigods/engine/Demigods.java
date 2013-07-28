package com.censoredsoftware.demigods.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.demigods.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.command.DCommand;
import com.censoredsoftware.demigods.engine.command.DevelopmentCommands;
import com.censoredsoftware.demigods.engine.command.GeneralCommands;
import com.censoredsoftware.demigods.engine.command.MainCommand;
import com.censoredsoftware.demigods.engine.conversation.DConversation;
import com.censoredsoftware.demigods.engine.data.DataManager;
import com.censoredsoftware.demigods.engine.data.ThreadManager;
import com.censoredsoftware.demigods.engine.element.Ability;
import com.censoredsoftware.demigods.engine.element.Deity;
import com.censoredsoftware.demigods.engine.element.Structure;
import com.censoredsoftware.demigods.engine.element.Task;
import com.censoredsoftware.demigods.engine.exception.DemigodsStartupException;
import com.censoredsoftware.demigods.engine.language.Translation;
import com.censoredsoftware.demigods.engine.language.TranslationManager;
import com.censoredsoftware.demigods.engine.listener.*;
import com.censoredsoftware.demigods.engine.util.Configs;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Demigods
{
	// Public Static Access
	public static DemigodsPlugin plugin;
	public static ConversationFactory conversation;

	// Public Dependency Plugins
	public static WorldGuardPlugin worldguard;

	// The Game Data
	protected static Map<String, Deity> deities;
	protected static Set<Task.List> quests;
	protected static Set<Structure> structures;
	protected static Set<DConversation> conversasions;
	protected static Listener listener;

	// The engine Default Text
	public static Translation text;

	public interface ListedDeity
	{
		public Deity getDeity();
	}

	public interface ListedTaskSet
	{
		public Task.List getTaskSet();
	}

	public interface ListedStructure
	{
		public Structure getStructure();
	}

	public interface ListedConversation
	{
		public DConversation getConversation();
	}

	public Demigods(DemigodsPlugin instance, final ListedDeity[] deities, final ListedTaskSet[] taskSets, final ListedStructure[] structures, final ListedConversation[] conversations, final Listener EpisodeListener) throws DemigodsStartupException
	{
		// Allow static access.
		plugin = instance;
		conversation = new ConversationFactory(instance);

		// Setup utilities.
		new Configs(instance, true);
		new Messages(instance);

		Demigods.deities = new HashMap<String, Deity>()
		{
			{
				for(ListedDeity deity : deities)
					put(deity.getDeity().getInfo().getName(), deity.getDeity());
			}
		};
		Demigods.quests = new HashSet<Task.List>()
		{
			{
				for(ListedTaskSet taskSet : taskSets)
					add(taskSet.getTaskSet());
			}
		};
		Demigods.structures = new HashSet<Structure>()
		{
			{
				for(ListedStructure structure : structures)
					add(structure.getStructure());
			}
		};
		Demigods.conversasions = new HashSet<DConversation>()
		{
			{
				for(DConversation.Required conversation : DConversation.Required.values())
					add(conversation.getConversation());
				if(conversations != null) for(ListedConversation conversation : conversations)
					add(conversation.getConversation());
			}
		};

		Demigods.text = getTranslation();

		// Initialize soft data.
		new DataManager();
		if(!DataManager.isConnected())
		{
			Messages.severe("Demigods was unable to connect to a Redis server.");
			Messages.severe("A Redis server is required for Demigods to run.");
			Messages.severe("Please install and configure a Redis server. (" + ChatColor.UNDERLINE + "http://redis.io" + ChatColor.RESET + ")");
			instance.getServer().getPluginManager().disablePlugin(instance);
			throw new DemigodsStartupException();
		}

		// Initialize metrics.
		try
		{
			// (new Metrics(instance)).start();
		}
		catch(Exception ignored)
		{}

		// Finish loading the plugin based on the game data.
		loadDepends(instance);
		loadListeners(instance);
		loadCommands();

		// Start game threads.
		ThreadManager.startThreads(instance);

		// Finally, regenerate structures
		Structure.Util.regenerateStructures();

		if(runningSpigot()) Messages.info(("Spigot found, will use extra API features."));
	}

	/**
	 * Get the translation involved.
	 * 
	 * @return The translation.
	 */
	public Translation getTranslation()
	{
		// Default to EnglishCharNames
		return new TranslationManager.English();
	}

	protected static void loadListeners(DemigodsPlugin instance)
	{
		// engine
		instance.getServer().getPluginManager().registerEvents(new BattleListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new CommandListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new EntityListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new FlagListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new GriefListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new InventoryListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new PlayerListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new TributeListener(), instance);

		// Main Episode Listener
		// TODO: instance.getServer().getPluginManager().registerEvents(listener, instance);

		// Deities
		for(Deity deity : getLoadedDeities().values())
		{
			if(deity.getAbilities() == null) continue;
			for(Ability ability : deity.getAbilities())
			{
				if(ability.getListener() != null) instance.getServer().getPluginManager().registerEvents(ability.getListener(), instance);
			}
		}

		// Tasks
		for(Task.List quest : getLoadedQuests())
		{
			if(quest.getTasks() == null) continue;
			for(Task task : quest)
			{
				if(task.getListener() != null) instance.getServer().getPluginManager().registerEvents(task.getListener(), instance);
			}
		}

		// Structures
		for(Structure structure : getLoadedStructures())
		{
			if(structure.getUniqueListener() == null) continue;
			instance.getServer().getPluginManager().registerEvents(structure.getUniqueListener(), instance);
		}

		// Conversations
		for(DConversation conversation : getLoadedConversations())
		{
			if(conversation.getUniqueListener() == null) continue;
			instance.getServer().getPluginManager().registerEvents(conversation.getUniqueListener(), instance);
		}
	}

	protected static void loadCommands()
	{
		DCommand.Util.registerCommand(new MainCommand());
		DCommand.Util.registerCommand(new GeneralCommands());
		DCommand.Util.registerCommand(new DevelopmentCommands());
	}

	protected static void loadDepends(DemigodsPlugin instance)
	{
		// WorldGuard
		Plugin depend = instance.getServer().getPluginManager().getPlugin("WorldGuard");
		if(depend instanceof WorldGuardPlugin) worldguard = (WorldGuardPlugin) depend;
	}

	public static Map<String, Deity> getLoadedDeities()
	{
		return Demigods.deities;
	}

	public static Set<Task.List> getLoadedQuests()
	{
		return Demigods.quests;
	}

	public static Set<Structure> getLoadedStructures()
	{
		return Demigods.structures;
	}

	public static Set<DConversation> getLoadedConversations()
	{
		return Demigods.conversasions;
	}

	public static boolean runningSpigot()
	{
		try
		{
			Bukkit.getServer().getWorlds().get(0).spigot();
			return true;
		}
		catch(Throwable ignored)
		{}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
