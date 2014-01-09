package com.censoredsoftware.demigods.engine;

import com.censoredsoftware.censoredlib.helper.CensoredCentralizedClass;
import com.censoredsoftware.censoredlib.helper.ConversationManager;
import com.censoredsoftware.demigods.base.DemigodsConversation;
import com.censoredsoftware.demigods.base.listener.ChatListener;
import com.censoredsoftware.demigods.base.listener.SpigotFeatures;
import com.censoredsoftware.demigods.engine.data.Data;
import com.censoredsoftware.demigods.engine.data.TaskManager;
import com.censoredsoftware.demigods.engine.data.serializable.DCharacter;
import com.censoredsoftware.demigods.engine.data.serializable.DPlayer;
import com.censoredsoftware.demigods.engine.data.serializable.Skill;
import com.censoredsoftware.demigods.engine.mythos.*;
import com.censoredsoftware.demigods.engine.util.Configs;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.censoredsoftware.shaded.org.mcstats.MetricsLite;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Demigods extends CensoredCentralizedClass
{
	// Constants
	public static Scoreboard BOARD;

	// Public Static Access
	public static final ConversationFactory CONVERSATION_FACTORY;
	public static final ScoreboardManager SCOREBOARD_MANAGER;

	// Mythos
	private final Mythos enabledMythos;

	// Instance of This Class
	private static Demigods INST;

	private Demigods()
	{
		enabledMythos = loadMythos();
	}

	// Load what is possible to load right away.
	static
	{
		// Conversation factory static access.
		CONVERSATION_FACTORY = new ConversationFactory(DemigodsPlugin.plugin());

		// Scoreboard manager.
		SCOREBOARD_MANAGER = Bukkit.getScoreboardManager();
		BOARD = SCOREBOARD_MANAGER.getNewScoreboard();

		// Load the Mythos.
		INST = new Demigods();
	}

	public static Mythos mythos()
	{
		return INST.enabledMythos;
	}

	// Load everything else.
	protected static boolean init()
	{
		// Initialize metrics
		try
		{
			(new MetricsLite(DemigodsPlugin.plugin())).start();
		}
		catch(Exception ignored)
		{
			// ignored
		}

		try
		{
			if(mythos() == null)
			{
				Messages.severe("Demigods was unable to load a Mythos.");
				Messages.severe("Please install a Mythos plugin or place the default Demigods-Greek.jar into the plugins\\Demigods\\addons directory.");
				return false;
			}

			if(!DemigodsPlugin.plugin().getServer().getOnlineMode())
			{
				Messages.severe("Demigods might not work in offline mode.");
				Messages.severe("We depend on Mojang's servers for ids.");
				Messages.severe("Any player who joins and is not premium may be kicked from the game.");
			}

			// Check for world load errors
			if(INST.loadWorlds() > 0)
			{
				Messages.severe("Demigods was unable to confirm any worlds.");
				Messages.severe("This may be caused by misspelled world names.");
				Messages.severe("Multi-world plugins can cause this message, and in that case this may be a false alarm.");
			}

			// Load listeners, commands, permissions, and the scoreboard
			INST.loadListeners();
			INST.loadCommands();
			INST.loadPermissions(true);
			INST.loadScoreboard();

			// Load the data
			Data.init();

			// Update usable characters
			DCharacter.Util.updateUsableCharacters();

			// Start threads
			TaskManager.startThreads();

			// Regenerate structures
			Structure.Util.regenerateStructures();

			if(Util.isRunningSpigot())
			{
				Bukkit.getPluginManager().registerEvents(new SpigotFeatures(), DemigodsPlugin.plugin());
				Messages.info(("Spigot found, extra API features enabled."));
			}
			else Messages.warning(("Without Spigot, some features may not work."));

			// Handle online characters
			for(DCharacter character : DCharacter.Util.loadAll())
				character.getMeta().cleanSkills();

			return true;
		}
		catch(Exception errored)
		{
			Messages.logException(errored);
		}
		return false;
	}

	protected Mythos loadMythos()
	{
		ServicesManager servicesManager = DemigodsPlugin.plugin().getServer().getServicesManager();
		Collection<RegisteredServiceProvider<Mythos>> mythosProviders = servicesManager.getRegistrations(Mythos.class);
		if(Iterables.any(mythosProviders, new Predicate<RegisteredServiceProvider<Mythos>>()
		{
			@Override
			public boolean apply(RegisteredServiceProvider<Mythos> mythosProvider)
			{
				return mythosProvider.getProvider().isPrimary();
			}
		}))
		{
			// Decide the primary Mythos
			Mythos reiningMythos = null;
			int reiningPriority = 5;

			Set<Mythos> workingSet = Sets.newHashSet();
			for(RegisteredServiceProvider<Mythos> mythosProvider : mythosProviders)
			{
				Mythos mythos = mythosProvider.getProvider();
				workingSet.add(mythos);
				Messages.info("The " + mythos.getTitle() + " Mythos has enabled!");
				Messages.info("-> Created by " + mythos.getAuthor() + ".");
				Messages.info("-> " + mythos.getTagline());
				if(!mythosProvider.getProvider().isPrimary()) continue;
				if(mythosProvider.getPriority().ordinal() < reiningPriority) // not really sure how Bukkit handles priority, presuming the same way as EventPriority
				{
					reiningMythos = mythos;
					reiningPriority = mythosProvider.getPriority().ordinal();
				}
			}

			if(reiningMythos != null)
			{
				workingSet.remove(reiningMythos);
				if(reiningMythos.useBaseGame() || reiningMythos.allowSecondary() && !workingSet.isEmpty()) reiningMythos = new MythosSet(reiningMythos, reiningMythos.allowSecondary() ? workingSet : new HashSet<Mythos>());
				reiningMythos.lock();
				return reiningMythos;
			}
		}
		return null;
	}

	protected int loadWorlds()
	{
		return Zones.init();
	}

	protected void loadListeners()
	{
		PluginManager register = Bukkit.getServer().getPluginManager();

		// Mythos
		for(Listener listener : mythos().getListeners())
			register.registerEvents(listener, DemigodsPlugin.plugin());

		// Abilities
		for(Ability ability : Ability.Util.getLoadedAbilities())
			if(ability.getListener() != null) register.registerEvents(ability.getListener(), DemigodsPlugin.plugin());

		// Structures
		for(Structure structure : Collections2.filter(mythos().getStructures(), new Predicate<Structure>()
		{
			@Override
			public boolean apply(Structure structure)
			{
				return structure.getUniqueListener() != null;
			}
		}))
			if(structure.getUniqueListener() != null) register.registerEvents(structure.getUniqueListener(), DemigodsPlugin.plugin());

		// Conversations
		for(ConversationManager conversation : Collections2.filter(Collections2.transform(Sets.newHashSet(DemigodsConversation.values()), new Function<DemigodsConversation, ConversationManager>()
		{
			@Override
			public ConversationManager apply(DemigodsConversation conversation)
			{
				return conversation.getConversation();
			}
		}), new Predicate<ConversationManager>()
		{
			@Override
			public boolean apply(ConversationManager conversation)
			{
				return conversation.getUniqueListener() != null;
			}
		}))
			if(conversation.getUniqueListener() != null) register.registerEvents(conversation.getUniqueListener(), DemigodsPlugin.plugin());

		// Divine Items
		for(DivineItem divineItem : enabledMythos.getDivineItems())
		{
			if(divineItem.getUniqueListener() != null) register.registerEvents(divineItem.getUniqueListener(), DemigodsPlugin.plugin());
			if(divineItem.getRecipe() != null) DemigodsPlugin.plugin().getServer().addRecipe(divineItem.getRecipe());
		}

		// Quit reason.
		// TODO Bukkit.getServer().getLogger().addHandler(new QuitReasonHandler());
	}

	protected void loadScoreboard()
	{
		// Alliances
		for(final Alliance alliance : mythos().getAlliances())
		{
			// Register the team
			Team team = BOARD.registerNewTeam(alliance.getName());

			// Define team properties
			team.setAllowFriendlyFire(Configs.getSettingBoolean("restrictions.friendly_fire"));
			team.setCanSeeFriendlyInvisibles(true);

			// In-use characters (including offline)
			for(DCharacter character : DCharacter.Util.getCharactersWithPredicate(new Predicate<DCharacter>()
			{
				@Override
				public boolean apply(DCharacter character)
				{
					return character.isActive() && alliance.equals(character.getAlliance());
				}
			}))
				team.addPlayer(character.getOfflinePlayer());
		}

		// Register the Mortal alliance
		Team mortals = BOARD.registerNewTeam("Mortal");

		// Define team properties
		mortals.setAllowFriendlyFire(Configs.getSettingBoolean("restrictions.friendly_fire"));
		mortals.setCanSeeFriendlyInvisibles(true);

		// All mortals (including offline)
		for(OfflinePlayer mortal : DPlayer.Util.getMortals())
			mortals.addPlayer(mortal);
	}

	protected void loadCommands()
	{
		ChatListener.init();
	}

	protected void loadPermissions(final boolean load)
	{
		final PluginManager register = Bukkit.getServer().getPluginManager();

		// Mythos
		for(Permission permission : mythos().getPermissions())
		{
			// catch errors to avoid any possible buggy permissions
			try
			{
				for(Map.Entry<String, Boolean> entry : permission.getChildren().entrySet())
					registerPermission(register, new Permission(entry.getKey(), entry.getValue() ? PermissionDefault.TRUE : PermissionDefault.FALSE), load);
				registerPermission(register, permission, load);
			}
			catch(Exception ignored)
			{
				// ignored
			}
		}

		// Alliances, Deities, and Abilities
		for(final Alliance alliance : mythos().getAlliances())
		{
			registerPermission(register, new Permission(alliance.getPermission(), "The permission to use the " + alliance.getName() + " alliance.", alliance.getPermissionDefault(), new HashMap<String, Boolean>()
			{
				{
					for(Deity deity : Alliance.Util.getLoadedDeitiesInAlliance(alliance))
					{
						registerPermission(register, new Permission(deity.getPermission(), alliance.getPermissionDefault()), load);
						put(deity.getPermission(), alliance.getPermissionDefault().equals(PermissionDefault.TRUE));
					}
				}
			}), load);
		}

		// Skill types
		for(Skill.Type skill : Skill.Type.values())
			registerPermission(register, skill.getPermission(), load);
	}

	static void uninit()
	{
		if(DemigodsPlugin.getReady())
		{
			// Save all the data.
			Data.save();

			// Handle online characters
			for(DCharacter character : DCharacter.Util.getOnlineCharacters())
			{
				// Toggle prayer off and clear the session
				DPlayer.Util.togglePrayingSilent(character.getOfflinePlayer().getPlayer(), false, false);
				DPlayer.Util.clearPrayerSession(character.getOfflinePlayer());

				// Toggle administration off and clear its session
				DPlayer.Util.toggleAdministration(character.getOfflinePlayer().getPlayer(), false, false);
				DPlayer.Util.clearAdministrationSession(character.getOfflinePlayer());
			}
		}

		// Cancel all threads, event calls, and unregister permissions.
		try
		{
			TaskManager.stopThreads();
			HandlerList.unregisterAll(DemigodsPlugin.plugin());
			Demigods.unloadPermissions();
		}
		catch(Exception ignored)
		{
			// ignored
		}
	}

	static void registerPermission(PluginManager register, Permission permission, boolean load)
	{
		if(load) register.addPermission(permission);
		else register.removePermission(permission);
	}

	static void unloadPermissions()
	{
		INST.loadPermissions(false);
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
			catch(Exception ignored)
			{
				// ignored
			}
			return false;
		}
	}
}
