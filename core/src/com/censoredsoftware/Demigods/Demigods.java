package com.censoredsoftware.Demigods;

import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import com.bekvon.bukkit.residence.Residence;
import com.censoredsoftware.Demigods.API.AdminAPI;
import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.API.DeityAPI;
import com.censoredsoftware.Demigods.API.PlayerAPI;
import com.censoredsoftware.Demigods.Event.Character.CharacterBetrayCharacterEvent;
import com.censoredsoftware.Demigods.Event.Character.CharacterKillCharacterEvent;
import com.censoredsoftware.Demigods.Listener.*;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Tracked.TrackedDisconnectReason;
import com.censoredsoftware.Modules.*;
import com.massivecraft.factions.P;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Demigods
{
	// Public Modules
	public static ConfigModule config;
	public static MessageModule message;
	public static PermissionModule permission;

	// Public Static Access DemigodsPlugin
	public static DemigodsPlugin demigods;

	// Public Dependency Plugins
	public static WorldGuardPlugin worldguard;
	public static P factions;
	public static Residence residence;

	// Protected Modules
	protected static BukkitUpdateModule update;
	protected static LatestTweetModule notice;

	// On-load Deity ClassPath List
	protected static List<String> deityPathList = new ArrayList<String>();

	protected Demigods(DemigodsPlugin instance)
	{
		// Allow Static Access
		demigods = instance;

		// Public Modules
		config = new ConfigModule(instance, true);
		message = new MessageModule(instance, config.getSettingBoolean("tag_messages"));
		permission = new PermissionModule();

		// Create All Object Factories
		new DemigodsFactory(instance);

		// Initialize Data
		new DemigodsData(instance);
	}

	protected static void loadListeners(DemigodsPlugin instance)
	{
		instance.getServer().getPluginManager().registerEvents(new AbilityListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new AltarListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new BattleListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new BlockListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new CharacterListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new ChatListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new ChunkListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new CommandListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new DebugListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new EntityListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new PlayerListener(), instance);
		instance.getServer().getPluginManager().registerEvents(new ShrineListener(), instance);
		Demigods.message.getLog().setFilter(new TrackedDisconnectReason());
	}

	protected static void loadCommands(DemigodsPlugin instance)
	{
		// Define Main CommandExecutor
		Commands ce = new Commands();

		// Define General Commands
		instance.getCommand("dg").setExecutor(ce);
		instance.getCommand("check").setExecutor(ce);
		instance.getCommand("owner").setExecutor(ce);
		instance.getCommand("removechar").setExecutor(ce);
		instance.getCommand("viewmaps").setExecutor(ce);
	}

	protected static void loadDepends(DemigodsPlugin instance)
	{
		// WorldGuard
		Plugin depend = instance.getServer().getPluginManager().getPlugin("WorldGuard");
		if(depend instanceof WorldGuardPlugin) worldguard = (WorldGuardPlugin) depend;

		// Factions
		depend = instance.getServer().getPluginManager().getPlugin("Factions");
		if(depend instanceof P) factions = (P) depend;

		// Residence
		depend = instance.getServer().getPluginManager().getPlugin("Residence");
		if(depend instanceof Residence) residence = (Residence) depend;
	}

	protected static void loadExpansions(DemigodsPlugin instance)
	{
		// Check for expansions
		for(Plugin plugin : instance.getServer().getPluginManager().getPlugins())
		{
			if(plugin instanceof DemigodsExpansion)
			{
				DemigodsExpansion expansion = (DemigodsExpansion) plugin;
				if(expansion.loadExpansion(instance))
				{
					for(String deityPath : expansion.getDeityPaths())
					{
						if(!deityPathList.contains(deityPath))
						{
							// Add to preliminary list of deities to load
							deityPathList.add(deityPath);
							DemigodsData.deityLoaders.saveData(deityPath, expansion.getClass().getClassLoader());
						}
					}
				}
			}
		}
	}

	protected static void loadDeities() // TODO Replace this.
	{
		message.info("Loading deities...");

		// Find all deities
		CodeSource demigodsSrc = Demigods.class.getProtectionDomain().getCodeSource();
		if(demigodsSrc != null)
		{
			try
			{
				URL demigodsJar = demigodsSrc.getLocation();
				ZipInputStream demigodsZip = new ZipInputStream(demigodsJar.openStream());

				ZipEntry demigodsFile;

				// Define variables
				long startTimer = System.currentTimeMillis();

				while((demigodsFile = demigodsZip.getNextEntry()) != null)
				{
					if(demigodsFile.getName().contains("$")) continue;
					String deityName = demigodsFile.getName().replace("/", ".").replace(".class", "").replaceAll("\\d*$", "");
					if(deityName.contains("_deity"))
					{
						deityPathList.add(deityName);
					}
				}

				for(String deityPath : deityPathList)
				{
					ClassLoader loader = DeityAPI.getClassLoader(deityPath);

					// Load everything else for the Deity (Listener, etc.)
					String loadMessage = (String) DeityAPI.invokeDeityMethod(deityPath, loader, "loadDeity");
					String name = (String) DeityAPI.invokeDeityMethod(deityPath, loader, "getName");
					String alliance = (String) DeityAPI.invokeDeityMethod(deityPath, loader, "getAlliance");

					ChatColor color = (ChatColor) DeityAPI.invokeDeityMethod(deityPath, loader, "getColor");
					ArrayList<String> commands = (ArrayList<String>) DeityAPI.invokeDeityMethod(deityPath, loader, "getCommands");
					ArrayList<Material> claimItems = (ArrayList<Material>) DeityAPI.invokeDeityMethod(deityPath, loader, "getClaimItems");

					// Add to data
					DemigodsData.deityPaths.saveData(name, deityPath);
					DemigodsData.deityTeams.saveData(name, alliance);
					DemigodsData.deityColors.saveData(name, color);
					DemigodsData.deityCommands.saveData(name, commands);
					DemigodsData.deityClaimItems.saveData(name, claimItems);

					// Display the success message
					message.info(loadMessage);
				}
				// Stop the timer
				long stopTimer = System.currentTimeMillis();
				double totalTime = (double) (stopTimer - startTimer);

				message.info("All deities loaded in " + totalTime / 1000 + " seconds.");
			}
			catch(Exception e)
			{
				message.severe("There was a problem while loading deities!");
				e.printStackTrace();
			}
		}
	}

	protected static void unload(DemigodsPlugin instance)
	{
		HandlerList.unregisterAll(instance);
		Scheduler.stopThreads(instance);
	}
}

class Scheduler
{
	static void startThreads(DemigodsPlugin instance)
	{
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, new Runnable()
		{
			@Override
			public void run()
			{
				DemigodsData.save(true);
			}
		}, 30, 60);
	}

	static void stopThreads(DemigodsPlugin instance)
	{
		instance.getServer().getScheduler().cancelTasks(instance);
	}
}

class EventFactory implements Listener
{

	@EventHandler(priority = EventPriority.MONITOR)
	public static void onEntityDeath(EntityDeathEvent event)
	{
		Entity entity = event.getEntity();
		if(entity instanceof Player)
		{
			Player player = (Player) entity;
			PlayerCharacter playerChar = null;
			if(PlayerAPI.getCurrentChar(player) != null) playerChar = PlayerAPI.getCurrentChar(player);

			// if(playerChar != null)
			// {
			// if(playerChar.getKillstreak() > 3) Demigods.message.broadcast(ChatColor.YELLOW + playerChar.getName() + ChatColor.GRAY + "'s killstreak has ended.");
			// playerChar.setKillstreak(0);
			// }

			EntityDamageEvent damageEvent = player.getLastDamageCause();

			if(damageEvent instanceof EntityDamageByEntityEvent)
			{
				EntityDamageByEntityEvent damageByEvent = (EntityDamageByEntityEvent) damageEvent;
				Entity damager = damageByEvent.getDamager();

				if(damager instanceof Player)
				{
					Player attacker = (Player) damager;
					PlayerCharacter attackChar = null;
					if(PlayerAPI.getCurrentChar(attacker) != null) attackChar = PlayerAPI.getCurrentChar(attacker);
					if(PlayerAPI.areAllied(attacker, player))
					{
						Bukkit.getServer().getPluginManager().callEvent(new CharacterBetrayCharacterEvent(attackChar, playerChar, PlayerAPI.getCurrentAlliance(player)));
					}
					else
					{
						Bukkit.getServer().getPluginManager().callEvent(new CharacterKillCharacterEvent(attackChar, playerChar));
					}

					if(attackChar != null)
					{
						// Killstreak
						// int killstreak = attackChar.getKillstreak();
						// attackChar.setKillstreak(killstreak + 1);
						// if(attackChar.getKillstreak() > 2)
						// {
						// Demigods.message.callEvent(new CharacterKillstreakEvent(attackChar, playerChar, killstreak + 1));
						// }

						// TODO Dominating
					}
				}
			}
		}
	}
}

class Commands implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(command.getName().equalsIgnoreCase("dg")) return dg(sender, args);
		else if(command.getName().equalsIgnoreCase("check")) return check(sender, args);
		else if(command.getName().equalsIgnoreCase("owner")) return owner(sender, args);

		// TESTING ONLY
		else if(command.getName().equalsIgnoreCase("removechar")) return removeChar(sender, args);

		// Debugging
		else if(command.getName().equalsIgnoreCase("viewmaps")) return viewMaps(sender);

		return false;
	}

	/*
	 * Command: "dg"
	 */
	private static boolean dg(CommandSender sender, String[] args)
	{
		if(args.length > 0)
		{
			dg_extended(sender, args);
			return true;
		}

		// Define Player
		Player player = Bukkit.getOfflinePlayer(sender.getName()).getPlayer();

		// Check Permissions
		if(!Demigods.permission.hasPermissionOrOP(player, "demigods.basic")) return Demigods.message.noPermission(player);

		Demigods.message.tagged(sender, "Documentation");
		for(String alliance : DeityAPI.getLoadedDeityAlliances())
		{
			sender.sendMessage(ChatColor.GRAY + " /dg " + alliance.toLowerCase());
		}
		sender.sendMessage(ChatColor.GRAY + " /dg info");
		sender.sendMessage(ChatColor.GRAY + " /dg commands");
		if(Demigods.permission.hasPermissionOrOP(player, "demigods.admin")) sender.sendMessage(ChatColor.RED + " /dg admin");
		sender.sendMessage(" ");
		sender.sendMessage(ChatColor.WHITE + " Use " + ChatColor.YELLOW + "/check" + ChatColor.WHITE + " to see your player information.");
		return true;
	}

	/*
	 * Command: "dg_extended"
	 */
	@SuppressWarnings("unchecked")
	private static boolean dg_extended(CommandSender sender, String[] args)
	{
		// Define Player
		Player player = Bukkit.getOfflinePlayer(sender.getName()).getPlayer();

		// Define args
		String category = args[0];
		String option1 = null, option2 = null, option3 = null, option4 = null;
		if(args.length >= 2) option1 = args[1];
		if(args.length >= 3) option2 = args[2];
		if(args.length >= 4) option3 = args[3];
		if(args.length >= 5) option4 = args[4];

		// Check Permissions
		if(!Demigods.permission.hasPermissionOrOP(player, "demigods.basic")) return Demigods.message.noPermission(player);

		if(category.equalsIgnoreCase("admin"))
		{
			dg_admin(sender, option1, option2, option3, option4);
		}
		// else if(category.equalsIgnoreCase("save"))
		// {
		// if(!Demigods.permission.hasPermissionOrOP(player, "demigods.admin")) return Demigods.message.noPermission(player);
		//
		// Demigods.message.broadcast(ChatColor.RED + "Manually forcing Demigods save...");
		// if(DFlatFile.save()) Demigods.message.broadcast(ChatColor.GREEN + "Save complete!");
		// else
		// {
		// Demigods.message.broadcast(ChatColor.RED + "There was a problem with saving...");
		// Demigods.message.broadcast(ChatColor.RED + "Check the log immediately.");
		// }
		// }
		else if(category.equalsIgnoreCase("commands"))
		{
			Demigods.message.tagged(sender, "Command Directory");
			sender.sendMessage(ChatColor.GRAY + " There's nothing here...");
		}
		else if(category.equalsIgnoreCase("info"))
		{
			if(option1 == null)
			{
				Demigods.message.tagged(sender, "Information Directory");
				sender.sendMessage(ChatColor.GRAY + " /dg info characters");
				sender.sendMessage(ChatColor.GRAY + " /dg info shrines");
				sender.sendMessage(ChatColor.GRAY + " /dg info tributes");
				sender.sendMessage(ChatColor.GRAY + " /dg info players");
				sender.sendMessage(ChatColor.GRAY + " /dg info pvp");
				sender.sendMessage(ChatColor.GRAY + " /dg info stats");
				sender.sendMessage(ChatColor.GRAY + " /dg info rankings");
				sender.sendMessage(ChatColor.GRAY + " /dg info demigods");
			}
			else if(option1.equalsIgnoreCase("demigods"))
			{
				Demigods.message.tagged(sender, "About the Plugin");
				sender.sendMessage(ChatColor.WHITE + " Not to be confused with other RPG plugins that focus on skills and classes alone, " + ChatColor.GREEN + "Demigods" + ChatColor.WHITE + " adds culture and conflict that will keep players coming back even after they've maxed out their levels and found all of the diamonds in a 50km radius.");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.GREEN + " Demigods" + ChatColor.WHITE + " is unique in its system of rewarding players for both adventuring (tributes) and conquering (PvP) with a wide array of fun and usefull skills.");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.WHITE + " Re-enact mythological battles and rise from a PlayerCharacter to a full-fledged Olympian as you form new Alliances with mythical groups and battle to the bitter end.");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.GRAY + " Developed by: " + ChatColor.GREEN + "_Alex" + ChatColor.GRAY + " and " + ChatColor.GREEN + "HmmmQuestionMark");
				sender.sendMessage(ChatColor.GRAY + " Website: " + ChatColor.YELLOW + "http://demigodsrpg.com/");
				sender.sendMessage(ChatColor.GRAY + " Source: " + ChatColor.YELLOW + "https://github.com/Clashnia/Minecraft-Demigods");
			}
			else if(option1.equalsIgnoreCase("characters"))
			{
				Demigods.message.tagged(sender, "Characters");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Characters.");
			}
			else if(option1.equalsIgnoreCase("shrine"))
			{
				Demigods.message.tagged(sender, "Shrines");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Shrines.");
			}
			else if(option1.equalsIgnoreCase("tribute"))
			{
				Demigods.message.tagged(sender, "Tributes");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Tributes.");
			}
			else if(option1.equalsIgnoreCase("player"))
			{
				Demigods.message.tagged(sender, "Players");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Players.");
			}
			else if(option1.equalsIgnoreCase("pvp"))
			{
				Demigods.message.tagged(sender, "PVP");
				sender.sendMessage(ChatColor.GRAY + " This is some info about PVP.");
			}
			else if(option1.equalsIgnoreCase("stats"))
			{
				Demigods.message.tagged(sender, "Stats");
				sender.sendMessage(ChatColor.GRAY + " Read some server-wide stats for Demigods.");
			}
			else if(option1.equalsIgnoreCase("rankings"))
			{
				Demigods.message.tagged(sender, "Rankings");
				sender.sendMessage(ChatColor.GRAY + " This is some ranking info about Demigods.");
			}
		}

		for(String alliance : DeityAPI.getLoadedDeityAlliances())
		{
			if(category.equalsIgnoreCase(alliance))
			{
				if(args.length < 2)
				{
					Demigods.message.tagged(sender, alliance + " Directory");
					for(String deity : DeityAPI.getAllDeitiesInAlliance(alliance))
						sender.sendMessage(ChatColor.GRAY + " /dg " + alliance.toLowerCase() + " " + deity.toLowerCase());
				}
				else
				{
					for(String deity : DeityAPI.getAllDeitiesInAlliance(alliance))
					{
						assert option1 != null;
						if(option1.equalsIgnoreCase(deity))
						{
							try
							{
								for(String toPrint : (ArrayList<String>) DeityAPI.invokeDeityMethodWithPlayer(DeityAPI.getDeityPath(deity), DeityAPI.getClassLoader(deity), "getInfo", player))
									sender.sendMessage(toPrint);
								return true;
							}
							catch(Exception e)
							{
								sender.sendMessage(ChatColor.RED + "(ERR: 3001)  Please report this immediatly.");
								e.printStackTrace(); // DEBUG
								return true;
							}
						}
					}
					sender.sendMessage(ChatColor.DARK_RED + " No such deity, please try again.");
					return false;
				}
			}
		}

		return true;
	}

	// Admin Directory
	private static boolean dg_admin(CommandSender sender, String option1, String option2, String option3, String option4)
	{
		Player player = Bukkit.getOfflinePlayer(sender.getName()).getPlayer();
		Player toEdit;
		PlayerCharacter character;
		int amount;

		if(!Demigods.permission.hasPermissionOrOP(player, "demigods.admin")) return Demigods.message.noPermission(player);

		if(option1 == null)
		{
			Demigods.message.tagged(sender, "Admin Directory");
			sender.sendMessage(ChatColor.GRAY + " /dg admin wand");
			sender.sendMessage(ChatColor.GRAY + " /dg admin debug");
			sender.sendMessage(ChatColor.GRAY + " /dg admin check <p> <char>");
			sender.sendMessage(ChatColor.GRAY + " /dg admin remove [player|character] <name>");
			sender.sendMessage(ChatColor.GRAY + " /dg admin set [maxfavor|favor|devotion|ascensions] <p> <amt>");
			sender.sendMessage(ChatColor.GRAY + " /dg admin add [maxfavor|favor|devotion|ascensions] <p> <amt>");
			sender.sendMessage(ChatColor.GRAY + " /dg admin sub [maxfavor|favor|devotion|ascensions] <p> <amt>");
		}

		if(option1 != null)
		{
			if(option1.equalsIgnoreCase("wand"))
			{
				if(!AdminAPI.wandEnabled(player))
				{
					DemigodsData.tempPlayerData.saveData(player, "temp_admin_wand", true);
					player.sendMessage(ChatColor.RED + "Your admin wand has been enabled for " + Material.getMaterial(Demigods.config.getSettingInt("admin.wand_tool")));
				}
				else if(AdminAPI.wandEnabled(player))
				{
					DemigodsData.tempPlayerData.removeData(player, "temp_admin_wand");
					player.sendMessage(ChatColor.RED + "You have disabled your admin wand.");
				}
				return true;
			}
			else if(option1.equalsIgnoreCase("debug"))
			{
				if(!DemigodsData.tempPlayerData.containsKey(player, "temp_admin_debug") || !DemigodsData.tempPlayerData.getDataBool(player, "temp_admin_debug"))
				{
					DemigodsData.tempPlayerData.saveData(player, "temp_admin_debug", true);
					player.sendMessage(ChatColor.RED + "You have enabled debugging.");
				}
				else if(DemigodsData.tempPlayerData.containsKey(player, "temp_admin_debug") && DemigodsData.tempPlayerData.getDataBool(player, "temp_admin_debug"))
				{
					DemigodsData.tempPlayerData.removeData(player, "temp_admin_debug");
					player.sendMessage(ChatColor.RED + "You have disabled debugging.");
				}
			}
			else if(option1.equalsIgnoreCase("check"))
			{
				if(option2 == null)
				{
					sender.sendMessage(ChatColor.RED + "You need to specify a player.");
					sender.sendMessage("/dg admin check <p>");
					return true;
				}

				// Define variables
				Player toCheck = Bukkit.getPlayer(option2);

				if(option3 == null)
				{
					Demigods.message.tagged(sender, ChatColor.RED + toCheck.getName() + " Player Check");
					sender.sendMessage(" Characters:");

					List<PlayerCharacter> chars = PlayerAPI.getChars(toCheck);

					for(PlayerCharacter checkingChar : chars)
					{
						player.sendMessage(ChatColor.GRAY + "   (#: " + checkingChar.getID() + ") Name: " + checkingChar.getName() + " / Deity: " + checkingChar.isDeity());
					}
				}
				else
				{
					// TODO: Display specific character information when called for.
				}
			}
			else if(option1.equalsIgnoreCase("remove"))
			{
				if(option2 == null || option3 == null)
				{
					sender.sendMessage(ChatColor.RED + "You need to be more specific with what you want to remove.");
					return true;
				}
				else
				{
					if(option2.equalsIgnoreCase("player"))
					{
						// TODO: Full player data removal
					}
					else if(option2.equalsIgnoreCase("character"))
					{
						int charID = CharacterAPI.getCharByName(option3).getID();

						// Remove the data
						DemigodsData.characterData.removeData(charID);

						sender.sendMessage(ChatColor.RED + "Character \"" + CharacterAPI.getChar(charID).getName() + "\" removed.");
					}
				}
			}
			else if(option1.equalsIgnoreCase("set"))
			{
				if(option2 == null || option3 == null)
				{
					sender.sendMessage(ChatColor.RED + "You need to specify a player and amount.");
					return true;
				}
				else
				{
					// Define variables
					toEdit = Bukkit.getPlayer(option3);
					character = PlayerAPI.getCurrentChar(toEdit);
					amount = Integer.parseInt(option4);
				}

				if(option2.equalsIgnoreCase("maxfavor"))
				{
					// Set the favor
					character.setMaxFavor(amount);

					sender.sendMessage(ChatColor.GREEN + "Max favor set to " + amount + " for " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character's max favor has been set to " + amount + ".");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("favor"))
				{
					// Set the favor
					character.setFavor(amount);

					sender.sendMessage(ChatColor.GREEN + "Favor set to " + amount + " for " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character's favor has been set to " + amount + ".");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("devotion"))
				{
					// Set the devotion
					character.setDevotion(amount);

					sender.sendMessage(ChatColor.GREEN + "Devotion set to " + amount + " for " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character's devotion has been set to " + amount + ".");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("ascensions"))
				{
					// Set the ascensions
					character.setAscensions(amount);

					sender.sendMessage(ChatColor.GREEN + "Ascensions set to " + amount + " for " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character's Ascensions have been set to " + amount + ".");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
			}
			else if(option1.equalsIgnoreCase("add"))
			{
				if(option2 == null)
				{
					sender.sendMessage(ChatColor.RED + "You need to be more specific.");
					return true;
				}
				else if(option3 == null)
				{
					sender.sendMessage(ChatColor.RED + "You must select a player and amount.");
					return true;
				}
				else
				{
					// Define variables
					toEdit = Bukkit.getPlayer(option3);
					character = PlayerAPI.getCurrentChar(toEdit);
					amount = Integer.parseInt(option4);
				}

				if(option2.equalsIgnoreCase("maxfavor"))
				{
					// Set the favor
					character.setMaxFavor(character.getMaxFavor() + amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " added to " + toEdit.getName() + "'s current character's max favor.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character's max favor has been increased by " + amount + ".");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("favor"))
				{
					// Set the favor
					character.giveFavor(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " favor added to " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character has been given " + amount + " favor.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("devotion"))
				{
					// Set the devotion
					character.giveDevotion(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " devotion added to " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character has been given " + amount + " devotion.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("ascensions"))
				{
					// Set the ascensions
					character.giveAscensions(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " Ascension(s) added to " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character has been given " + amount + " Ascensions.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
			}
			else if(option1.equalsIgnoreCase("sub"))
			{
				if(option2 == null)
				{
					sender.sendMessage(ChatColor.RED + "You need to be more specific.");
					return true;
				}
				else if(option3 == null)
				{
					sender.sendMessage(ChatColor.RED + "You must select a player and amount.");
					return true;
				}
				else
				{
					// Define variables
					toEdit = Bukkit.getPlayer(option3);
					character = PlayerAPI.getCurrentChar(toEdit);
					amount = Integer.parseInt(option4);
				}

				if(option2.equalsIgnoreCase("maxfavor"))
				{
					// Set the favor
					character.subtractFavor(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " removed from " + toEdit.getName() + "'s current character's max favor.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.RED + "Your current character's max favor has been reduced by " + amount + ".");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				if(option2.equalsIgnoreCase("favor"))
				{
					// Set the favor
					character.subtractFavor(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " favor removed from " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.RED + "Your current character has had " + amount + " favor removed.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("devotion"))
				{
					// Set the devotion
					character.subtractDevotion(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " devotion removed from " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.RED + "Your current character has had " + amount + " devotion removed.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("ascensions"))
				{
					// Set the ascensions
					character.subtractAscensions(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " Ascension(s) removed from " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.RED + "Your current character has had " + amount + " Ascension(s) removed.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Invalid category selected.");
				sender.sendMessage("/dg admin [set|add|sub] [maxfavor|favor|devotion|ascensions] <p> <amt>");
				return true;
			}
		}

		return true;
	}

	/*
	 * Command: "check"
	 */
	private static boolean check(CommandSender sender, String[] args)
	{
		Player player = Bukkit.getOfflinePlayer(sender.getName()).getPlayer();
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(character == null || !character.isImmortal())
		{
			player.sendMessage(ChatColor.RED + "You cannot use that command, mortal.");
			return true;
		}

		// Define variables
		int kills = PlayerAPI.getKills(player);
		int deaths = PlayerAPI.getDeaths(player);
		// int killstreak = character.getKillstreak();
		String charName = character.getName();
		String deity = character.isDeity();
		String alliance = character.getTeam();
		int favor = character.getFavor();
		int maxFavor = character.getMaxFavor();
		int devotion = character.getDevotion();
		int ascensions = character.getAscensions();
		int devotionGoal = character.getDevotionGoal();
		// int powerOffense = character.getPower(AbilityEvent.AbilityType.OFFENSE);
		// int powerDefense = character.getPower(AbilityEvent.AbilityType.DEFENSE);
		// int powerStealth = character.getPower(AbilityEvent.AbilityType.STEALTH);
		// int powerSupport = character.getPower(AbilityEvent.AbilityType.SUPPORT);
		// int powerPassive = character.getPower(AbilityEvent.AbilityType.PASSIVE);
		ChatColor deityColor = (ChatColor) DemigodsData.deityColors.getDataObject(deity);
		ChatColor favorColor = character.getFavorColor();

		// if(args.length == 1 && (args[0].equalsIgnoreCase("level") || args[0].equalsIgnoreCase("levels")))
		// {
		// // Send the user their info via chat
		// Demigods.message.tagged(sender, "Levels Check");
		//
		// sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Offense: " + ChatColor.GREEN + powerOffense);
		// sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Defense: " + ChatColor.GREEN + powerDefense);
		// sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Stealth: " + ChatColor.GREEN + powerStealth);
		// sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Support: " + ChatColor.GREEN + powerSupport);
		// sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Passive: " + ChatColor.GREEN + powerPassive);
		//
		// return true;
		// }

		// Send the user their info via chat
		Demigods.message.tagged(sender, "Player Check");

		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Character: " + ChatColor.AQUA + charName);
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Deity: " + deityColor + deity + ChatColor.WHITE + " of the " + ChatColor.GOLD + DemigodsData.capitalize(alliance) + "s");
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Favor: " + favorColor + favor + ChatColor.GRAY + " (of " + ChatColor.GREEN + maxFavor + ChatColor.GRAY + ")");
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Ascensions: " + ChatColor.GREEN + ascensions);
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Devotion: " + ChatColor.GREEN + devotion + ChatColor.GRAY + " (" + ChatColor.YELLOW + (devotionGoal - devotion) + ChatColor.GRAY + " until next Ascension)");
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Kills: " + ChatColor.GREEN + kills + ChatColor.WHITE + " / Deaths: " + ChatColor.RED + deaths + ChatColor.WHITE); // + " / Killstreak: " + ChatColor.RED + killstreak);

		return true;
	}

	/*
	 * Command: "owner"
	 */
	private static boolean owner(CommandSender sender, String[] args)
	{
		Player player = Bukkit.getOfflinePlayer(sender.getName()).getPlayer();

		if(args.length < 1)
		{
			player.sendMessage(ChatColor.RED + "You must select a character.");
			player.sendMessage(ChatColor.RED + "/owner <character>");
			return true;
		}

		PlayerCharacter charToCheck = CharacterAPI.getCharByName(args[0]);

		if(charToCheck.getName() == null)
		{
			player.sendMessage(ChatColor.RED + "That character doesn't exist.");
			return true;
		}
		else
		{
			player.sendMessage(DeityAPI.getDeityColor(charToCheck.isDeity()) + charToCheck.getName() + ChatColor.YELLOW + " belongs to " + charToCheck.getOwner().getName() + ".");
			return true;
		}
	}

	/*
	 * Command: "viewMaps"
	 */
	private static boolean viewMaps(CommandSender sender)
	{
		sender.sendMessage("-- Players ------------------");
		sender.sendMessage(" ");

		for(OfflinePlayer player : DemigodsData.playerData.listTiers())
		{
			sender.sendMessage(player.getName() + ": ");
			for(String key : DemigodsData.playerData.listKeys(player))
			{
				sender.sendMessage("  - " + key.toString() + ": " + DemigodsData.playerData.getDataObject(player, key).toString());
			}
		}

		sender.sendMessage(" ");
		sender.sendMessage("-- Characters ---------------");
		sender.sendMessage(" ");

		for(PlayerCharacter character : CharacterAPI.getAllChars())
		{
			sender.sendMessage(character.getName() + "."); // TODO Warp data and such.
		}

		DemigodsData.save(true); // TODO For testing only.

		return true;
	}

	/*
	 * Command: "removeChar"
	 */
	private static boolean removeChar(CommandSender sender, String[] args)
	{
		if(args.length != 1) return false;

		// Define args
		Player player = Bukkit.getOfflinePlayer(sender.getName()).getPlayer();
		String charName = args[0];

		if(PlayerAPI.hasCharName(player, charName))
		{
			PlayerCharacter character = CharacterAPI.getCharByName(charName);
			int charID = character.getID();
			DemigodsData.characterData.removeData(charID);

			sender.sendMessage(ChatColor.RED + "Character removed!");
		}
		else sender.sendMessage(ChatColor.RED + "There was an error while removing your character.");

		return true;
	}
}
