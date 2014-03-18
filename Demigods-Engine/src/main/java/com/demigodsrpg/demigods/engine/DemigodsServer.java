package com.demigodsrpg.demigods.engine;

import com.censoredsoftware.library.helper.CensoredCentralizedClass;
import com.censoredsoftware.library.helper.ConversationManager;
import com.demigodsrpg.demigods.base.DemigodsConversation;
import com.demigodsrpg.demigods.base.listener.ChatListener;
import com.demigodsrpg.demigods.base.listener.SpigotFeatures;
import com.demigodsrpg.demigods.engine.conversation.Administration;
import com.demigodsrpg.demigods.engine.conversation.Prayer;
import com.demigodsrpg.demigods.engine.data.DataManager;
import com.demigodsrpg.demigods.engine.data.DemigodsWorld;
import com.demigodsrpg.demigods.engine.data.TaskManager;
import com.demigodsrpg.demigods.engine.data.WorldDataManager;
import com.demigodsrpg.demigods.engine.deity.Ability;
import com.demigodsrpg.demigods.engine.deity.Alliance;
import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsPlayer;
import com.demigodsrpg.demigods.engine.entity.player.attribute.Skill;
import com.demigodsrpg.demigods.engine.item.DivineItem;
import com.demigodsrpg.demigods.engine.mythos.Mythos;
import com.demigodsrpg.demigods.engine.mythos.MythosSet;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.demigodsrpg.demigods.engine.util.Messages;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcstats.MetricsLite;

import java.util.*;

public class DemigodsServer extends CensoredCentralizedClass
{
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

			// Load the data
			DataManager.initAllData();

			// Load listeners, commands, permissions, and the scoreboard
			loadListeners();
			loadCommands();
			loadPermissions(true);

			// Update characters
			DemigodsCharacter.updateUsableCharacters();

			// Start threads
			TaskManager.startThreads();

			// Regenerate structures (on a delay)
			Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new BukkitRunnable()
			{
				@Override
				public void run()
				{
					DemigodsStructureType.Util.regenerateStructures();
				}
			}, 120);

			if(isRunningSpigot())
			{
				Bukkit.getPluginManager().registerEvents(new SpigotFeatures(), DemigodsPlugin.getInst());
				Messages.info(("Spigot found, extra API features enabled."));
			}
			else Messages.warning(("Without Spigot, some features may not work."));

			// Clean skills
			for(DemigodsCharacter character : DemigodsCharacter.all())
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
		for(DemigodsStructureType structureType : Collections2.filter(getMythos().getStructures(), new Predicate<DemigodsStructureType>()
		{
			@Override
			public boolean apply(DemigodsStructureType structureType)
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
			DataManager.saveAllData();

			// Handle online characters
			for(DemigodsCharacter character : getOnlineCharacters())
			{
				// Toggle prayer off and clear the session
				Prayer.Util.togglePrayingSilent(character.getBukkitOfflinePlayer().getPlayer(), false, false);
				Prayer.Util.clearPrayerSession(character.getBukkitOfflinePlayer());

				// Toggle administration off and clear its session
				Administration.Util.toggleAdministration(character.getBukkitOfflinePlayer().getPlayer(), false, false);
				Administration.Util.clearAdministrationSession(character.getBukkitOfflinePlayer());
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

	// -- PLAYER -- //

	public Collection<DemigodsPlayer> getAllPlayers()
	{
		return DemigodsPlayer.all();
	}

	public Collection<DemigodsPlayer> getOnlinePlayers()
	{
		return Collections2.filter(DemigodsPlayer.all(), new Predicate<DemigodsPlayer>()
		{
			@Override
			public boolean apply(DemigodsPlayer player)
			{
				return player.getBukkitOfflinePlayer().isOnline();
			}
		});
	}

	// -- MORTAL -- //

	public Collection<OfflinePlayer> getMortals()
	{
		return Collections2.transform(Collections2.filter(DemigodsPlayer.all(), new Predicate<DemigodsPlayer>()
		{
			@Override
			public boolean apply(DemigodsPlayer player)
			{
				DemigodsCharacter character = player.getCharacter();
				return character == null || !character.isUsable() || !character.isActive();
			}
		}), new Function<DemigodsPlayer, OfflinePlayer>()
		{
			@Override
			public OfflinePlayer apply(DemigodsPlayer player)
			{
				return player.getBukkitOfflinePlayer();
			}
		});
	}

	public Set<Player> getOnlineMortals()
	{
		return Sets.filter(Sets.newHashSet(Bukkit.getOnlinePlayers()), new Predicate<Player>()
		{
			@Override
			public boolean apply(Player player)
			{
				DemigodsCharacter character = DemigodsPlayer.of(player).getCharacter();
				return character == null || !character.isUsable() || !character.isActive();
			}
		});
	}

	// -- CHARACTER -- //

	public DemigodsCharacter getCharacter(final String name)
	{
		try
		{
			return Iterators.find(DemigodsCharacter.all().iterator(), new Predicate<DemigodsCharacter>()
			{
				@Override
				public boolean apply(DemigodsCharacter loaded)
				{
					return loaded.getName().equalsIgnoreCase(name);
				}
			});
		}
		catch(Exception ignored)
		{}
		return null;
	}

	public Collection<DemigodsCharacter> getActiveCharacters()
	{
		return Collections2.filter(DemigodsCharacter.all(), new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				return character.isUsable() && character.isActive();
			}
		});
	}

	public Collection<DemigodsCharacter> getUsableCharacters()
	{
		return Collections2.filter(DemigodsCharacter.all(), new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				return character.isUsable();
			}
		});
	}

	public Collection<DemigodsCharacter> getOnlineCharactersWithDeity(final String deity)
	{
		return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				return character.isActive() && character.getBukkitOfflinePlayer().isOnline() && character.getDeity().getName().equalsIgnoreCase(deity);
			}
		});
	}

	public Collection<DemigodsCharacter> getOnlineCharactersWithAbility(final String abilityName)
	{
		return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				if(character.isActive() && character.getBukkitOfflinePlayer().isOnline())
				{
					for(Ability abilityToCheck : character.getDeity().getAbilities())
						if(abilityToCheck.getName().equalsIgnoreCase(abilityName)) return true;
				}
				return false;
			}
		});
	}

	public Collection<DemigodsCharacter> getOnlineCharactersWithAlliance(final Alliance alliance)
	{
		return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				return character.isActive() && character.getBukkitOfflinePlayer().isOnline() && character.getAlliance().equals(alliance);
			}
		});
	}

	public Collection<DemigodsCharacter> getOnlineCharactersWithoutAlliance(final Alliance alliance)
	{
		return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				return character.isActive() && character.getBukkitOfflinePlayer().isOnline() && !character.getAlliance().equals(alliance);
			}
		});
	}

	public Collection<DemigodsCharacter> getOnlineCharactersBelowAscension(final int ascension)
	{
		return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				return character.isActive() && character.getBukkitOfflinePlayer().isOnline() && character.getMeta().getAscensions() < ascension;
			}
		});
	}

	public Collection<DemigodsCharacter> getOnlineCharacters()
	{
		return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				return character.isActive() && character.getBukkitOfflinePlayer().isOnline();
			}
		});
	}

	public Collection<DemigodsCharacter> getAllCharactersWithDeity(final String deity)
	{
		return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				return character.isActive() && character.getDeity().getName().equalsIgnoreCase(deity);
			}
		});
	}

	public Collection<DemigodsCharacter> getAllCharactersWithAlliance(final Alliance alliance)
	{
		return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				return character.isActive() && character.getAlliance().equals(alliance);
			}
		});
	}

	public Collection<DemigodsCharacter> getCharactersWithPredicate(Predicate<DemigodsCharacter> predicate)
	{
		return Collections2.filter(getUsableCharacters(), predicate);
	}

	// -- WORLD -- //

	public DemigodsWorld getWorld(String name)
	{
		return WorldDataManager.getWorld(name);
	}

	public Collection<DemigodsWorld> getWorlds()
	{
		return WorldDataManager.getWorlds();
	}
}
