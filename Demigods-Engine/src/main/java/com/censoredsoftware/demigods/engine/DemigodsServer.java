package com.censoredsoftware.demigods.engine;

import com.censoredsoftware.censoredlib.helper.CensoredCentralizedClass;
import com.censoredsoftware.censoredlib.helper.ConversationManager;
import com.censoredsoftware.censoredlib.helper.MapDBFile;
import com.censoredsoftware.censoredlib.helper.TimedMapDBFile;
import com.censoredsoftware.demigods.base.DemigodsConversation;
import com.censoredsoftware.demigods.base.listener.ChatListener;
import com.censoredsoftware.demigods.base.listener.SpigotFeatures;
import com.censoredsoftware.demigods.engine.conversation.Administration;
import com.censoredsoftware.demigods.engine.conversation.Prayer;
import com.censoredsoftware.demigods.engine.data.TaskManager;
import com.censoredsoftware.demigods.engine.data.file.FileDataManager;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsCharacter;
import com.censoredsoftware.demigods.engine.entity.player.attribute.Skill;
import com.censoredsoftware.demigods.engine.mythos.*;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.censoredsoftware.shaded.org.mcstats.MetricsLite;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DemigodsServer extends CensoredCentralizedClass
{
	// Binary Data FIXME MOVE THESE AROUND AND FIT TO CODE STANDARDS
	public static final TimedMapDBFile TIMED = new TimedMapDBFile("TIMED.dg", FileDataManager.SAVE_PATH + "bin/");
	public static final MapDBFile SERVER = new MapDBFile("SERVER.dg", FileDataManager.SAVE_PATH + "bin/");
	// Binary Data FIXME MOVE THESE AROUND AND FIT TO CODE STANDARDS

	// Mythos
	private final Mythos mythos;

	DemigodsServer()
	{
		mythos = loadMythos();
	}

	public Mythos getMythos()
	{
		return mythos;
	}

	// Load everything else.
	protected boolean init()
	{
		// Initialize metrics
		try
		{
			(new MetricsLite(DemigodsPlugin.getInst())).start();
		}
		catch(Exception ignored)
		{
			// ignored
		}

		try
		{
			if(getMythos() == null)
			{
				Messages.severe("Demigods was unable to load a Mythos.");
				Messages.severe("Please install a Mythos plugin or place the default Demigods-Greek.jar into the plugins\\Demigods\\addons directory.");
				return false;
			}

			if(!DemigodsPlugin.getInst().getServer().getOnlineMode())
			{
				Messages.severe("Demigods might not work in offline mode.");
				Messages.severe("We depend on Mojang's servers for ids.");
				Messages.severe("Any player who joins and is not premium may be kicked from the game.");
			}

			// Check for world load errors
			if(loadWorlds() > 0)
			{
				Messages.severe("Demigods was unable to confirm any worlds.");
				Messages.severe("This may be caused by misspelled world names.");
				Messages.severe("Multi-world plugins can cause this message, and in that case this may be a false alarm.");
			}

			// Load listeners, commands, permissions, and the scoreboard
			loadListeners();
			loadCommands();
			loadPermissions(true);

			// Load the data
			Demigods.getDataManager().init();

			// Update usable characters
			DemigodsCharacter.Util.updateUsableCharacters();

			// Start threads
			TaskManager.startThreads();

			// Regenerate structures (on a delay)
			Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new BukkitRunnable()
			{
				@Override
				public void run()
				{
					StructureType.Util.regenerateStructures();
				}
			}, 120);

			if(isRunningSpigot())
			{
				Bukkit.getPluginManager().registerEvents(new SpigotFeatures(), DemigodsPlugin.getInst());
				Messages.info(("Spigot found, extra API features enabled."));
			}
			else Messages.warning(("Without Spigot, some features may not work."));

			// Handle online characters
			for(DemigodsCharacter character : DemigodsCharacter.Util.loadAll())
				character.getMeta().cleanSkills();

			return true;
		}
		catch(Exception errored)
		{
			errored.printStackTrace();
		}
		return false;
	}

	protected Mythos loadMythos()
	{
		ServicesManager servicesManager = DemigodsPlugin.getInst().getServer().getServicesManager();
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
		for(Listener listener : getMythos().getListeners())
			register.registerEvents(listener, DemigodsPlugin.getInst());

		// Abilities
		for(Ability ability : Ability.Util.getLoadedAbilities())
			if(ability.getListener() != null) register.registerEvents(ability.getListener(), DemigodsPlugin.getInst());

		// Structures
		for(StructureType structureType : Collections2.filter(getMythos().getStructures(), new Predicate<StructureType>()
		{
			@Override
			public boolean apply(StructureType structureType)
			{
				return structureType.getUniqueListener() != null;
			}
		}))
			if(structureType.getUniqueListener() != null) register.registerEvents(structureType.getUniqueListener(), DemigodsPlugin.getInst());

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
			if(conversation.getUniqueListener() != null) register.registerEvents(conversation.getUniqueListener(), DemigodsPlugin.getInst());

		// Divine Items
		for(DivineItem divineItem : getMythos().getDivineItems())
		{
			if(divineItem.getUniqueListener() != null) register.registerEvents(divineItem.getUniqueListener(), DemigodsPlugin.getInst());
			if(divineItem.getRecipe() != null) DemigodsPlugin.getInst().getServer().addRecipe(divineItem.getRecipe());
		}

		// Quit reason.
		// TODO Bukkit.getServer().getLogger().addHandler(new QuitReasonHandler());
	}

	protected void loadCommands()
	{
		ChatListener.init();
	}

	protected void loadPermissions(final boolean load)
	{
		final PluginManager register = Bukkit.getServer().getPluginManager();

		// Mythos
		for(Permission permission : getMythos().getPermissions())
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
		for(final Alliance alliance : getMythos().getAlliances())
		{
			// catch errors to avoid any possible buggy permissions
			try
			{
				registerPermission(register, new Permission(alliance.getPermission(), "The permission to use the " + alliance.getName() + " alliance.", alliance.getPermissionDefault(), new HashMap<String, Boolean>()
				{
					{
						for(Deity deity : Alliance.Util.getLoadedDeitiesInAlliance(alliance))
						{
							registerPermission(register, new Permission(deity.getPermission(), alliance.getPermissionDefault()), load);
							put(deity.getPermission(), deity.getPermissionDefault().equals(PermissionDefault.NOT_OP) ? alliance.getPermissionDefault().equals(PermissionDefault.TRUE) : deity.getPermissionDefault().equals(PermissionDefault.TRUE));
						}
					}
				}), load);
			}
			catch(Exception ignored)
			{
				// ignored
			}
		}

		// Skill types
		for(Skill.Type skill : Skill.Type.values())
			registerPermission(register, skill.getPermission(), load);
	}

	void uninit()
	{
		if(DemigodsPlugin.getReady())
		{
			// Save all the data.
			Demigods.getDataManager().save();

			// Handle online characters
			for(DemigodsCharacter character : DemigodsCharacter.Util.getOnlineCharacters())
			{
				// Toggle prayer off and clear the session
				Prayer.Util.togglePrayingSilent(character.getOfflinePlayer().getPlayer(), false, false);
				Prayer.Util.clearPrayerSession(character.getOfflinePlayer());

				// Toggle administration off and clear its session
				Administration.Util.toggleAdministration(character.getOfflinePlayer().getPlayer(), false, false);
				Administration.Util.clearAdministrationSession(character.getOfflinePlayer());
			}
		}

		// Cancel all threads, event calls, and unregister permissions.
		try
		{
			TaskManager.stopThreads();
			HandlerList.unregisterAll(DemigodsPlugin.getInst());
			unloadPermissions();
		}
		catch(Exception ignored)
		{
			// ignored
		}
	}

	void registerPermission(PluginManager register, Permission permission, boolean load)
	{
		if(load) register.addPermission(permission);
		else register.removePermission(permission);
	}

	void unloadPermissions()
	{
		loadPermissions(false);
	}

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
