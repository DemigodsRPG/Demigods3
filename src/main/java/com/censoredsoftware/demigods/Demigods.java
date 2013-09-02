package com.censoredsoftware.demigods;

import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.command.DevelopmentCommands;
import com.censoredsoftware.demigods.command.GeneralCommands;
import com.censoredsoftware.demigods.command.MainCommand;
import com.censoredsoftware.demigods.conversation.Prayer;
import com.censoredsoftware.demigods.data.ThreadManager;
import com.censoredsoftware.demigods.helper.QuitReasonHandler;
import com.censoredsoftware.demigods.helper.WrappedCommand;
import com.censoredsoftware.demigods.helper.WrappedConversation;
import com.censoredsoftware.demigods.language.Translation;
import com.censoredsoftware.demigods.listener.*;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.structure.Structure;
import com.censoredsoftware.demigods.util.Configs;
import com.censoredsoftware.demigods.util.Messages;
import com.censoredsoftware.errornoise.ErrorNoise;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.mcstats.MetricsLite;

import java.util.Set;

public class Demigods
{
	// Constants
	public static String SAVE_PATH;

	// Public Static Access
	public static DemigodsPlugin PLUGIN;
	public static ConversationFactory CONVERSATION_FACTORY;
	public static Translation LANGUAGE;

	// Public Dependency Plugins
	public static WorldGuardPlugin WORLD_GUARD;
	public static boolean ERROR_NOISE;

	// Disabled Worlds
	protected static Set<String> DISABLED_WORLDS;
	protected static Set<String> COMMANDS;

	// Load everything.
	protected static void load()
	{
		// Allow static access.
		CONVERSATION_FACTORY = new ConversationFactory(PLUGIN);
		LANGUAGE = new Translation();

		// Initialize metrics
		try
		{
			(new MetricsLite(PLUGIN)).start();
		}
		catch(Exception ignored)
		{}

		// Configure depends
		loadDepends();

		// Start the data
		SAVE_PATH = PLUGIN.getDataFolder() + "/data/"; // Don't change this.

		// Check if there are no enabled worlds
		if(!loadWorlds())
		{
			Messages.severe("Demigods was unable to load any worlds.");
			Messages.severe("Please enable at least 1 world.");
			PLUGIN.getServer().getPluginManager().disablePlugin(PLUGIN);
		}

		// Load listeners and commands
		loadListeners();
		loadCommands();

		// Update usable characters
		DCharacter.Util.updateUsableCharacters();

		// Start threads
		ThreadManager.startThreads();

		if(MiscUtil.isRunningSpigot()) Messages.info(("Spigot found, will use extra API features."));
	}

	private static boolean loadWorlds()
	{
		DISABLED_WORLDS = Sets.newHashSet();
		for(String world : Collections2.filter(Configs.getSettingArrayListString("restrictions.disabled_worlds"), new Predicate<String>()
		{
			@Override
			public boolean apply(String world)
			{
				return PLUGIN.getServer().getWorld(world) != null;
			}
		}))
			if(PLUGIN.getServer().getWorld(world) != null) DISABLED_WORLDS.add(world);
		if(PLUGIN.getServer().getWorlds().size() == DISABLED_WORLDS.size()) return false;
		return true;
	}

	private static void loadListeners()
	{
		PluginManager register = Bukkit.getServer().getPluginManager();

		// Engine
		for(ListedListener listener : ListedListener.values())
			register.registerEvents(listener.getListener(), PLUGIN);

		// Disabled worlds
		if(!DISABLED_WORLDS.isEmpty()) register.registerEvents(new DisabledWorldListener(), PLUGIN);

		// Abilities
		for(Ability ability : Ability.Util.getLoadedAbilities())
			register.registerEvents(ability.getListener(), PLUGIN);

		// Structures
		for(Structure.Type structure : Sets.filter(Sets.newHashSet(Structure.Type.values()), new Predicate<Structure.Type>()
		{
			@Override
			public boolean apply(Structure.Type structure)
			{
				return structure.getUniqueListener() != null;
			}
		}))
			if(structure.getUniqueListener() != null) register.registerEvents(structure.getUniqueListener(), PLUGIN);

		// Conversations
		for(WrappedConversation conversation : Collections2.filter(Collections2.transform(Sets.newHashSet(ListedConversation.values()), new Function<ListedConversation, WrappedConversation>()
		{
			@Override
			public WrappedConversation apply(ListedConversation conversation)
			{
				return conversation.getConversation();
			}
		}), new Predicate<WrappedConversation>()
		{
			@Override
			public boolean apply(WrappedConversation conversation)
			{
				return conversation.getUniqueListener() != null;
			}
		}))
			if(conversation.getUniqueListener() != null) register.registerEvents(conversation.getUniqueListener(), PLUGIN);

		// Quit reason.
		Bukkit.getServer().getLogger().addHandler(new QuitReasonHandler());
	}

	private static void loadCommands()
	{
		COMMANDS = Sets.newHashSet();
		for(ListedCommand command : ListedCommand.values())
			COMMANDS.addAll(command.getCommand().getCommands());
		COMMANDS.add("demigod");
		COMMANDS.add("dg");
		COMMANDS.add("c");
		COMMANDS.add("o");
		COMMANDS.add("l");
		COMMANDS.add("a");
	}

	private static void loadDepends()
	{
		// WorldGuard
		Plugin depend = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if(depend instanceof WorldGuardPlugin) WORLD_GUARD = (WorldGuardPlugin) depend;
		ERROR_NOISE = Bukkit.getServer().getPluginManager().getPlugin("ErrorNoise") != null && Bukkit.getServer().getPluginManager().getPlugin("ErrorNoise") instanceof ErrorNoise;
	}

	public static class MiscUtil
	{
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
			return DISABLED_WORLDS.contains(location.getWorld().getName());
		}

		public static boolean isDisabledWorld(World world)
		{
			return DISABLED_WORLDS.contains(world.getName());
		}

		public static boolean isDemigodsCommand(String command)
		{
			return COMMANDS.contains(command);
		}
	}

	// Conversations
	public enum ListedConversation
	{
		PRAYER(new Prayer());

		private final WrappedConversation conversationInfo;

		private ListedConversation(WrappedConversation conversationInfo)
		{
			this.conversationInfo = conversationInfo;
		}

		public WrappedConversation getConversation()
		{
			return this.conversationInfo;
		}

		// Can't touch this. Naaaaaa na-na-na.. Ba-dum, ba-dum.
		public static interface Category extends Prompt
		{
			public String getChatName();

			public boolean canUse(ConversationContext context);
		}
	}

	// Listeners
	public enum ListedListener
	{
		BATTLE(new BattleListener()), CHAT(new ChatListener()), ENTITY(new EntityListener()), FLAG(new FlagListener()), GRIEF(new GriefListener()), PLAYER(new PlayerListener()), TRIBUTE(new TributeListener());

		private Listener listener;

		private ListedListener(Listener listener)
		{
			this.listener = listener;
		}

		public Listener getListener()
		{
			return listener;
		}
	}

	// Commands
	public enum ListedCommand
	{
		MAIN(new MainCommand()), GENERAL(new GeneralCommands()), DEVELOPMENT(new DevelopmentCommands());

		private WrappedCommand command;

		private ListedCommand(WrappedCommand command)
		{
			this.command = command;
		}

		public WrappedCommand getCommand()
		{
			return command;
		}
	}
}
