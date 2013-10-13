package com.censoredsoftware.demigods;

import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.command.DevelopmentCommands;
import com.censoredsoftware.demigods.command.GeneralCommands;
import com.censoredsoftware.demigods.command.MainCommand;
import com.censoredsoftware.demigods.conversation.Prayer;
import com.censoredsoftware.demigods.data.ThreadManager;
import com.censoredsoftware.demigods.data.TributeManager;
import com.censoredsoftware.demigods.deity.Alliance;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.deity.ListedAlliance;
import com.censoredsoftware.demigods.deity.ListedDeity;
import com.censoredsoftware.demigods.helper.QuitReasonHandler;
import com.censoredsoftware.demigods.helper.WrappedCommand;
import com.censoredsoftware.demigods.helper.WrappedConversation;
import com.censoredsoftware.demigods.item.DivineItem;
import com.censoredsoftware.demigods.language.Translation;
import com.censoredsoftware.demigods.listener.*;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.Skill;
import com.censoredsoftware.demigods.structure.ListedStructure;
import com.censoredsoftware.demigods.structure.Structure;
import com.censoredsoftware.demigods.trigger.Trigger;
import com.censoredsoftware.demigods.util.Configs;
import com.censoredsoftware.demigods.util.Messages;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.mcstats.MetricsLite;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Demigods
{
	// Constants
	public static String SAVE_PATH;

	// Public Static Access
	public static final DemigodsPlugin PLUGIN;
	public static final ConversationFactory CONVERSATION_FACTORY;
	public static final Translation LANGUAGE;

	// Disabled Stuff
	public static ImmutableSet<String> DISABLED_WORLDS;
	public static ImmutableSet<String> COMMANDS;

	// Mythos
	public static final Mythos MYTHOS;

	// Load what is possible to load right away.
	static
	{
		// Allow static access.
		PLUGIN = (DemigodsPlugin) Bukkit.getServer().getPluginManager().getPlugin("Demigods");
		CONVERSATION_FACTORY = new ConversationFactory(PLUGIN);

		// Language data.
		LANGUAGE = new Translation();

		// Load the Mythos.
		ServicesManager servicesManager = PLUGIN.getServer().getServicesManager();
		RegisteredServiceProvider<Mythos> mythosProvider = servicesManager.getRegistration(Mythos.class);
		if(mythosProvider != null)
		{
			MYTHOS = mythosProvider.getProvider();
			Messages.info("The " + MYTHOS.getName() + " mythos created by " + MYTHOS.getAuthor() + " has loaded!");
		}
		else MYTHOS = new Mythos()
		{
			@Override
			public String getName()
			{
				return "Greek";
			}

			@Override
			public String getDescription()
			{
				return "Greek mythology, as described by Hesiod, Homer, and other Greek bards.";
			}

			@Override
			public String getAuthor()
			{
				return "_Alex and HmmmQuestionMark";
			}

			@Override
			public Set<Alliance> getAlliances()
			{
				return Sets.newHashSet((Alliance[]) ListedAlliance.values());
			}

			@Override
			public Set<Deity> getDeities()
			{
				return Sets.newHashSet((Deity[]) ListedDeity.values());
			}

			@Override
			public Set<Structure> getStructures()
			{
				return Sets.newHashSet((Structure[]) ListedStructure.values());
			}

			@Override
			public Set<Listener> getListeners()
			{
				return Sets.newHashSet();
			}

			@Override
			public Set<Permission> getPermissions()
			{
				return Sets.newHashSet();
			}

			@Override
			public Set<Trigger> getTriggers()
			{
				return Sets.newHashSet(ThreadManager.ListedTrigger.getTriggers());
			}
		};

		// Initialize metrics
		try
		{
			(new MetricsLite(PLUGIN)).start();
		}
		catch(Exception ignored)
		{}
	}

	// Load everything else.
	protected static void load()
	{
		// Start the data
		SAVE_PATH = PLUGIN.getDataFolder() + "/data/"; // Don't change this.

		// Check if there are no enabled worlds
		if(!loadWorlds())
		{
			Messages.severe("Demigods was unable to load any worlds.");
			Messages.severe("Please enable at least 1 world.");
			PLUGIN.getServer().getPluginManager().disablePlugin(PLUGIN);
		}

		// Load listeners, commands, and permissions
		loadListeners();
		loadCommands();
		loadPermissions();

		// Update usable characters
		DCharacter.Util.updateUsableCharacters();

		// Start threads
		ThreadManager.startThreads();

		// Regenerate structures
		Structure.Util.regenerateStructures();

		// Initialize tribute tracking
		TributeManager.initializeTributeTracking();

		if(Util.isRunningSpigot()) Messages.info(("Spigot found, will use extra API features."));
		else Messages.warning(("Without Spigot, some features may not work."));
	}

	private static boolean loadWorlds()
	{
		Set<String> disabledWorlds = Sets.newHashSet();
		for(String world : Collections2.filter(Configs.getSettingArrayListString("restrictions.disabled_worlds"), new Predicate<String>()
		{
			@Override
			public boolean apply(String world)
			{
				return PLUGIN.getServer().getWorld(world) != null;
			}
		}))
			if(PLUGIN.getServer().getWorld(world) != null) disabledWorlds.add(world);
		DISABLED_WORLDS = ImmutableSet.copyOf(disabledWorlds);
		return PLUGIN.getServer().getWorlds().size() != DISABLED_WORLDS.size();
	}

	private static void loadListeners()
	{
		PluginManager register = Bukkit.getServer().getPluginManager();

		// Engine
		for(ListedListener listener : ListedListener.values())
			register.registerEvents(listener.getListener(), PLUGIN);

		// Mythos
		for(Listener listener : MYTHOS.getListeners())
			register.registerEvents(listener, PLUGIN);

		// Disabled worlds
		if(!DISABLED_WORLDS.isEmpty()) register.registerEvents(new ZoneListener(), PLUGIN);

		// Abilities
		for(Ability ability : Ability.Util.getLoadedAbilities())
			if(ability.getListener() != null) register.registerEvents(ability.getListener(), PLUGIN);

		// Structures
		for(Structure structure : Sets.filter(MYTHOS.getStructures(), new Predicate<Structure>()
		{
			@Override
			public boolean apply(Structure structure)
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

		// Special Items
		for(DivineItem divineItem : DivineItem.values())
		{
			if(divineItem.getUniqueListener() != null) register.registerEvents(divineItem.getUniqueListener(), PLUGIN);
			if(divineItem.getRecipe() != null) PLUGIN.getServer().addRecipe(divineItem.getRecipe());
		}

		// Quit reason.
		Bukkit.getServer().getLogger().addHandler(new QuitReasonHandler());
	}

	private static void loadCommands()
	{
		Set<String> commands = Sets.newHashSet();
		for(ListedCommand command : ListedCommand.values())
			commands.addAll(command.getCommand().getCommands());
		commands.add("demigod");
		commands.add("dg");
		commands.add("c");
		commands.add("o");
		commands.add("l");
		commands.add("a");
		commands.add("v");
		commands.add("n");
		COMMANDS = ImmutableSet.copyOf(commands);
	}

	private static void loadPermissions()
	{
		final PluginManager register = Bukkit.getServer().getPluginManager();

		// Engine
		for(ListedPermission permission : ListedPermission.values())
		{
			// catch errors to avoid any possible buggy permissions
			try
			{
				for(Map.Entry<String, Boolean> entry : permission.getPermission().getChildren().entrySet())
					register.addPermission(new Permission(entry.getKey(), entry.getValue() ? PermissionDefault.TRUE : PermissionDefault.FALSE));
				register.addPermission(permission.getPermission());
			}
			catch(Exception ignored)
			{}
		}

		// Mythos
		for(Permission permission : MYTHOS.getPermissions())
		{
			// catch errors to avoid any possible buggy permissions
			try
			{
				register.addPermission(permission);
			}
			catch(Exception ignored)
			{}
		}

		// Alliances, Deities, and Abilities
		for(final Alliance alliance : MYTHOS.getAlliances())
		{
			register.addPermission(new Permission(alliance.getPermission(), "The permission to use the " + alliance.getName() + " alliance.", alliance.getPermissionDefault(), new HashMap<String, Boolean>()
			{
				{
					for(Deity deity : Alliance.Util.getLoadedDeitiesInAlliance(alliance))
					{
						register.addPermission(new Permission(deity.getPermission(), alliance.getPermissionDefault()));
						put(deity.getPermission(), alliance.getPermissionDefault().equals(PermissionDefault.TRUE));
					}
				}
			}));
		}

		// Skill types
		for(Skill.Type skill : Skill.Type.values())
			register.addPermission(skill.getPermission());
	}

	public static class Util
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
			public String getChatName(ConversationContext context);

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

	// Permissions
	public enum ListedPermission
	{
		BASIC(new Permission("demigods.basic", "The very basic permissions for Demigods.", PermissionDefault.TRUE, new HashMap<String, Boolean>()
		{
			{
				put("demigods.basic.create", true);
				put("demigods.basic.forsake", true);
			}
		})), ADMIN(new Permission("demigods.admin", "The admin permissions for Demigods.", PermissionDefault.OP)), PVP_AREA_COOLDOWN(new Permission("demigods.bypass.pvpareacooldown", "Bypass the wait for leaving/entering PVP zones.", PermissionDefault.FALSE));

		private Permission permission;

		private ListedPermission(Permission permission)
		{
			this.permission = permission;
		}

		public Permission getPermission()
		{
			return permission;
		}
	}
}
