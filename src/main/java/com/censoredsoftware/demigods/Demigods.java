package com.censoredsoftware.demigods;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.censoredsoftware.core.bukkit.ListedConversation;
import com.censoredsoftware.core.module.Configs;
import com.censoredsoftware.core.module.Messages;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.command.DevelopmentCommands;
import com.censoredsoftware.demigods.command.GeneralCommands;
import com.censoredsoftware.demigods.command.MainCommand;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.data.ThreadManager;
import com.censoredsoftware.demigods.exception.DemigodsStartupException;
import com.censoredsoftware.demigods.language.Translation;
import com.censoredsoftware.demigods.listener.*;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.structure.MassiveStructurePart;
import com.censoredsoftware.demigods.task.Task;
import com.censoredsoftware.demigods.util.Structures;
import com.google.common.collect.Sets;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Demigods
{
	// Public Static Access
	public static DemigodsPlugin plugin;
	public static ConversationFactory conversation;
	public static Messages message;
	public static Configs config;

	// Public Dependency Plugins
	public static WorldGuardPlugin worldguard;

	// Disabled Worlds
	protected static Set<String> disabledWorlds;
	protected static Set<String> commands;

	// The engine Default Text
	public static Translation language;

	public Demigods(DemigodsPlugin instance) throws DemigodsStartupException
	{
		// Allow static access.
		plugin = instance;
		conversation = new ConversationFactory(instance);

		// Setup utilities.
		config = new Configs(instance, true);
		message = new Messages(instance);

		// Setup language.
		language = new Translation();

		if(!loadWorlds(instance))
		{
			message.severe("Demigods was unable to load any worlds.");
			message.severe("Please configure at least 1 world for Demigods.");
			instance.getServer().getPluginManager().disablePlugin(instance);
			throw new DemigodsStartupException();
		}

		// Initialize data
		new DataManager();
		if(!DataManager.isConnected())
		{
			message.severe("Demigods was unable to connect to a Redis server.");
			message.severe("A Redis server is required for Demigods to run.");
			message.severe("Please install and configure a Redis server. (" + ChatColor.UNDERLINE + "http://redis.io" + ChatColor.RESET + ")");
			instance.getServer().getPluginManager().disablePlugin(instance);
			throw new DemigodsStartupException();
		}

		// Update usable characters
		DCharacter.Util.updateUsableCharacters();

		// Initialize metrics
		try
		{
			// (new Metrics(instance)).start();
		}
		catch(Exception ignored)
		{}

		// Finish loading the plugin based on the game data
		loadDepends(instance);
		loadListeners(instance);
		loadCommands(instance);

		// Start game threads
		ThreadManager.startThreads(instance);

		// Finally, regenerate structures
		Structures.regenerateStructures();

		if(isRunningSpigot()) message.info(("Spigot found, will use extra API features."));
	}

	public static boolean loadWorlds(DemigodsPlugin instance)
	{
		disabledWorlds = Sets.newHashSet();
		for(String world : config.getSettingArrayListString("restrictions.disabled_worlds"))
		{
			if(instance.getServer().getWorld(world) != null) disabledWorlds.add(world);
		}
		if(instance.getServer().getWorlds().size() == disabledWorlds.size()) return false;
		return true;
	}

	protected static void loadListeners(DemigodsPlugin instance)
	{
		PluginManager register = instance.getServer().getPluginManager();

		// Engine
		register.registerEvents(new BattleListener(), instance);
		register.registerEvents(new CommandListener(), instance);
		register.registerEvents(new EntityListener(), instance);
		register.registerEvents(new FlagListener(), instance);
		register.registerEvents(new GriefListener(), instance);
		register.registerEvents(new InventoryListener(), instance);
		register.registerEvents(new PlayerListener(), instance);
		register.registerEvents(new TributeListener(), instance);

		// Disabled worlds
		if(!disabledWorlds.isEmpty()) register.registerEvents(new DisabledWorldListener(), instance);

		// Deities
		for(Elements.ListedDeity deity : Elements.Deities.values())
		{
			if(deity.getDeity().getAbilities() == null) continue;
			for(Ability ability : deity.getDeity().getAbilities())
			{
				if(ability.getListener() != null) register.registerEvents(ability.getListener(), instance);
			}
		}

		// Tasks
		for(Elements.ListedTaskSet quest : Elements.Tasks.values())
		{
			if(quest.getTaskSet().getTasks() == null) continue;
			for(Task task : quest.getTaskSet())
			{
				if(task.getListener() != null) register.registerEvents(task.getListener(), instance);
			}
		}

		// Structures
		for(Elements.ListedStructure structure : Elements.Structures.values())
		{
			if(structure instanceof MassiveStructurePart || structure.getStructure().getUniqueListener() == null) continue;
			register.registerEvents(structure.getStructure().getUniqueListener(), instance);
		}

		// Conversations
		for(ListedConversation.ConversationData conversation : Elements.Conversations.values())
		{
			if(conversation.getConversation().getUniqueListener() == null) continue;
			register.registerEvents(conversation.getConversation().getUniqueListener(), instance);
		}
	}

	protected static void loadCommands(DemigodsPlugin instance)
	{
		commands = Sets.newHashSet();
		MainCommand main = new MainCommand();
		GeneralCommands general = new GeneralCommands();
		DevelopmentCommands development = new DevelopmentCommands();
		main.register(instance, false);
		general.register(instance, false);
		development.register(instance, true);
		commands.addAll(main.getCommands());
		commands.addAll(general.getCommands());
		commands.addAll(development.getCommands());
		commands.add("dg");
		commands.add("demigod");
	}

	protected static void loadDepends(DemigodsPlugin instance)
	{
		// WorldGuard
		Plugin depend = instance.getServer().getPluginManager().getPlugin("WorldGuard");
		if(depend instanceof WorldGuardPlugin) worldguard = (WorldGuardPlugin) depend;
	}

	public static boolean isRunningSpigot()
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

	public static boolean isDisabledWorld(Location location)
	{
		return disabledWorlds.contains(location.getWorld().getName());
	}

	public static boolean isDisabledWorld(World world)
	{
		return disabledWorlds.contains(world.getName());
	}

	public static boolean isDemigodsCommand(String command)
	{
		return commands.contains(command);
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
