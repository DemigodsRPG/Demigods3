package com.censoredsoftware.Demigods;

import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.bekvon.bukkit.residence.Residence;
import com.censoredsoftware.Demigods.API.DeityAPI;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Modules.*;
import com.massivecraft.factions.P;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Demigods
{
	// Public Modules
	public static ConfigModule config;
	public static MessageModule message;
	public static PermissionModule permission;

	// Protected Modules
	protected static BukkitUpdateModule update;
	protected static LatestTweetModule notice;

	// Protected Dependency Plugins
	protected static WorldGuardPlugin worldguard;
	protected static P factions;
	protected static Residence residence;

	// On-load Deity ClassPath List
	public static ArrayList<String> deityPathList = new ArrayList<String>();

	Demigods(DemigodsPlugin instance)
	{
		// Create All Object Factories
		new DemigodsFactory(instance);

		// Public Modules
		config = new ConfigModule(instance, true);
		message = new MessageModule(instance, config.getSettingBoolean("tag_messages"));
		permission = new PermissionModule();
	}

	// @Override
	// public void onEnable() // TODO Replace this.
	// {
	// loadDeities();
	//
	// // SpecialLocationData loading
	// DFlatFile.load();
	//
	// Scheduler.startThreads(instance);
	//
	// misc.getLog().setFilter(new DisconnectReasonFilter());
	// }

	// @Override
	// public void onDisable() // TODO Replace this.
	// {
	// // Disable Plugin
	// HandlerList.unregisterAll(instance);
	// Scheduler.stopThreads(instance);
	// DFlatFile.save();
	// }

	static void loadListeners(DemigodsPlugin instance)
	{

	}

	static void loadCommands(DemigodsPlugin instance)
	{
		// Define Main CommandExecutor
		Commands ce = new Commands();

		// Define General Commands
		instance.getCommand("dg").setExecutor(ce);
		instance.getCommand("check").setExecutor(ce);
		instance.getCommand("owner").setExecutor(ce);
		instance.getCommand("removechar").setExecutor(ce);
		instance.getCommand("test1").setExecutor(ce);
		instance.getCommand("viewmaps").setExecutor(ce);
		instance.getCommand("viewblocks").setExecutor(ce);
	}

	public void loadExpansions(DemigodsPlugin instance)
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

	@SuppressWarnings("unchecked")
	public void loadDeities() // TODO Replace this.
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
}

class Scheduler
{
	static void startThreads(DemigodsPlugin instance)
	{

	}

	static void stopThreads(DemigodsPlugin instance)
	{
		instance.getServer().getScheduler().cancelTasks(instance);
	}
}

/**
 * class EventFactory implements Listener
 * {
 * 
 * @EventHandler(priority = EventPriority.MONITOR)
 *                        public static void onEntityDeath(EntityDeathEvent event)
 *                        {
 *                        Entity entity = event.getEntity();
 *                        if(entity instanceof Player)
 *                        {
 *                        Player player = (Player) entity;
 *                        PlayerCharacter playerChar = null;
 *                        if(API.player.getCurrentChar(player) != null) playerChar = API.player.getCurrentChar(player);
 * 
 *                        if(playerChar != null)
 *                        {
 *                        if(playerChar.getKillstreak() > 3) API.misc.serverMsg(ChatColor.YELLOW + playerChar.getName() + ChatColor.GRAY + "'s killstreak has ended.");
 *                        playerChar.setKillstreak(0);
 *                        }
 * 
 *                        EntityDamageEvent damageEvent = player.getLastDamageCause();
 * 
 *                        if(damageEvent instanceof EntityDamageByEntityEvent)
 *                        {
 *                        EntityDamageByEntityEvent damageByEvent = (EntityDamageByEntityEvent) damageEvent;
 *                        Entity damager = damageByEvent.getDamager();
 * 
 *                        if(damager instanceof Player)
 *                        {
 *                        Player attacker = (Player) damager;
 *                        PlayerCharacter attackChar = null;
 *                        if(API.player.getCurrentChar(attacker) != null) attackChar = API.player.getCurrentChar(attacker);
 *                        if(API.player.areAllied(attacker, player))
 *                        {
 *                        API.misc.callEvent(new CharacterBetrayCharacterEvent(attackChar, playerChar, API.player.getCurrentAlliance(player)));
 *                        }
 *                        else
 *                        {
 *                        API.misc.callEvent(new CharacterKillCharacterEvent(attackChar, playerChar));
 *                        }
 * 
 *                        if(attackChar != null)
 *                        {
 *                        // Killstreak
 *                        int killstreak = attackChar.getKillstreak();
 *                        attackChar.setKillstreak(killstreak + 1);
 *                        if(attackChar.getKillstreak() > 2)
 *                        {
 *                        API.misc.callEvent(new CharacterKillstreakEvent(attackChar, playerChar, killstreak + 1));
 *                        }
 * 
 *                        // TODO Dominating
 *                        }
 *                        }
 *                        }
 *                        }
 *                        }
 *                        }
 */

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
		else if(command.getName().equalsIgnoreCase("viewblocks")) return viewBlocks(sender);

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
		Player player = (Player) API.player.definePlayer(sender.getName());

		// Check Permissions
		if(!API.misc.hasPermissionOrOP(player, "demigods.basic")) return API.misc.noPermission(player);

		API.misc.taggedMessage(sender, "Documentation");
		for(String alliance : API.deity.getLoadedDeityAlliances())
			sender.sendMessage(ChatColor.GRAY + " /dg " + alliance.toLowerCase());
		sender.sendMessage(ChatColor.GRAY + " /dg info");
		sender.sendMessage(ChatColor.GRAY + " /dg commands");
		if(API.misc.hasPermissionOrOP(player, "demigods.admin")) sender.sendMessage(ChatColor.RED + " /dg admin");
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
		Player player = (Player) API.player.definePlayer(sender.getName());

		// Define args
		String category = args[0];
		String option1 = null, option2 = null, option3 = null, option4 = null;
		if(args.length >= 2) option1 = args[1];
		if(args.length >= 3) option2 = args[2];
		if(args.length >= 4) option3 = args[3];
		if(args.length >= 5) option4 = args[4];

		// Check Permissions
		if(!API.misc.hasPermissionOrOP(player, "demigods.basic")) return API.misc.noPermission(player);

		if(category.equalsIgnoreCase("admin"))
		{
			dg_admin(sender, option1, option2, option3, option4);
		}
		else if(category.equalsIgnoreCase("save"))
		{
			if(!API.misc.hasPermissionOrOP(player, "demigods.admin")) return API.misc.noPermission(player);

			API.misc.serverMsg(ChatColor.RED + "Manually forcing Demigods save...");
			if(DFlatFile.save()) API.misc.serverMsg(ChatColor.GREEN + "Save complete!");
			else
			{
				API.misc.serverMsg(ChatColor.RED + "There was a problem with saving...");
				API.misc.serverMsg(ChatColor.RED + "Check the log immediately.");
			}
		}
		else if(category.equalsIgnoreCase("commands"))
		{
			API.misc.taggedMessage(sender, "Command Directory");
			sender.sendMessage(ChatColor.GRAY + " There's nothing here...");
		}
		else if(category.equalsIgnoreCase("info"))
		{
			if(option1 == null)
			{
				API.misc.taggedMessage(sender, "Information Directory");
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
				API.misc.taggedMessage(sender, "About the Plugin");
				sender.sendMessage(ChatColor.WHITE + " Not to be confused with other RPG plugins that focus on skills and classes alone, " + ChatColor.GREEN + "Demigods" + ChatColor.WHITE + " adds culture and conflict that will keep players coming back even after they've maxed out their levels and found all of the diamonds in a 50km radius.");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.GREEN + " Demigods" + ChatColor.WHITE + " is unique in its system of rewarding players for both adventuring (tributes) and conquering (PvP) with a wide array of fun and usefull skills.");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.WHITE + " Re-enact mythological battles and rise from a Demigod to a full-fledged Olympian as you form new Alliances with mythical groups and battle to the bitter end.");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.GRAY + " Developed by: " + ChatColor.GREEN + "_Alex" + ChatColor.GRAY + " and " + ChatColor.GREEN + "HmmmQuestionMark");
				sender.sendMessage(ChatColor.GRAY + " Website: " + ChatColor.YELLOW + "http://demigodsrpg.com/");
				sender.sendMessage(ChatColor.GRAY + " Source: " + ChatColor.YELLOW + "https://github.com/Clashnia/Minecraft-Demigods");
			}
			else if(option1.equalsIgnoreCase("update"))
			{
				if(!API.misc.hasPermissionOrOP(player, "demigods.admin")) return API.misc.noPermission(player);

				if(API.data.getConfirmed(sender, "update"))
				{
					API.data.confirm(sender, "update", false);
					if(API.update.check())
					{
						API.misc.taggedMessage(sender, "Beginning download...");
						if(API.update.execute()) API.misc.taggedMessage(sender, "Download complete. " + ChatColor.YELLOW + "Please reload the server!");
						else API.misc.taggedMessage(sender, "Download failed. " + ChatColor.WHITE + "Please try again later.");
					}
					else
					{
						API.misc.taggedMessage(sender, "You are already running the latest version.");
					}
					return true;
				}
				else
				{
					API.misc.taggedMessage(sender, "Currently, version " + ChatColor.YELLOW + API.getDescription().getVersion() + ChatColor.WHITE + " is installed.");
					API.misc.taggedMessage(sender, "The latest version up for download is " + ChatColor.YELLOW + API.update.getLatestVersion() + ChatColor.WHITE + ".");
					API.misc.taggedMessage(sender, "If you would still like to update, please use ");
					API.misc.taggedMessage(sender, ChatColor.YELLOW + "/dg update " + ChatColor.WHITE + "again.");
					API.data.confirm(sender, "update", true);
					return true;
				}
			}
			else if(option1.equalsIgnoreCase("characters"))
			{
				API.misc.taggedMessage(sender, "Characters");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Characters.");
			}
			else if(option1.equalsIgnoreCase("shrine"))
			{
				API.misc.taggedMessage(sender, "Shrines");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Shrines.");
			}
			else if(option1.equalsIgnoreCase("tribute"))
			{
				API.misc.taggedMessage(sender, "Tributes");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Tributes.");
			}
			else if(option1.equalsIgnoreCase("player"))
			{
				API.misc.taggedMessage(sender, "Players");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Players.");
			}
			else if(option1.equalsIgnoreCase("pvp"))
			{
				API.misc.taggedMessage(sender, "PVP");
				sender.sendMessage(ChatColor.GRAY + " This is some info about PVP.");
			}
			else if(option1.equalsIgnoreCase("stats"))
			{
				API.misc.taggedMessage(sender, "Stats");
				sender.sendMessage(ChatColor.GRAY + " Read some server-wide stats for Demigods.");
			}
			else if(option1.equalsIgnoreCase("rankings"))
			{
				API.misc.taggedMessage(sender, "Rankings");
				sender.sendMessage(ChatColor.GRAY + " This is some ranking info about Demigods.");
			}
		}

		for(String alliance : API.deity.getLoadedDeityAlliances())
		{
			if(category.equalsIgnoreCase(alliance))
			{
				if(args.length < 2)
				{
					API.misc.taggedMessage(sender, alliance + " Directory");
					for(String deity : API.deity.getAllDeitiesInAlliance(alliance))
						sender.sendMessage(ChatColor.GRAY + " /dg " + alliance.toLowerCase() + " " + deity.toLowerCase());
				}
				else
				{
					for(String deity : API.deity.getAllDeitiesInAlliance(alliance))
					{
						assert option1 != null;
						if(option1.equalsIgnoreCase(deity))
						{
							try
							{
								for(String toPrint : (ArrayList<String>) API.deity.invokeDeityMethodWithPlayer(API.deity.getDeityClass(deity), API.deity.getClassLoader(deity), "getInfo", player))
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
		Player player = (Player) API.player.definePlayer(sender.getName());
		Player toEdit;
		PlayerCharacter character;
		int amount;

		if(!API.misc.hasPermissionOrOP(player, "demigods.admin")) return API.misc.noPermission(player);

		if(option1 == null)
		{
			API.misc.taggedMessage(sender, "Admin Directory");
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
				if(!API.admin.wandEnabled(player))
				{
					API.data.savePlayerData(player, "temp_admin_wand", true);
					player.sendMessage(ChatColor.RED + "Your admin wand has been enabled for " + Material.getMaterial(API.config.getSettingInt("admin_wand_tool")));
				}
				else if(API.admin.wandEnabled(player))
				{
					API.data.removePlayerData(player, "temp_admin_wand");
					player.sendMessage(ChatColor.RED + "You have disabled your admin wand.");
				}
				return true;
			}
			else if(option1.equalsIgnoreCase("debug"))
			{
				if(!API.data.hasPlayerData(player, "temp_admin_debug") || API.data.getPlayerData(player, "temp_admin_debug").equals(false))
				{
					API.data.savePlayerData(player, "temp_admin_debug", true);
					player.sendMessage(ChatColor.RED + "You have enabled debugging.");
				}
				else if(API.data.hasPlayerData(player, "temp_admin_debug") && API.data.getPlayerData(player, "temp_admin_debug").equals(true))
				{
					API.data.removePlayerData(player, "temp_admin_debug");
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
					API.misc.taggedMessage(sender, ChatColor.RED + toCheck.getName() + " Player Check");
					sender.sendMessage(" Characters:");

					List<Integer> chars = API.player.getChars(toCheck);

					for(Integer checkingCharID : chars)
					{
						PlayerCharacter checkingChar = API.character.getChar(checkingCharID);
						player.sendMessage(ChatColor.GRAY + "   (#: " + checkingCharID + ") Name: " + checkingChar.getName() + " / Deity: " + checkingChar.getDeity());
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
						int charID = API.character.getCharByName(option3).getID();

						// Remove the data
						API.data.removeChar(charID);

						sender.sendMessage(ChatColor.RED + "Character \"" + API.character.getChar(charID).getName() + "\" removed.");
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
					character = API.player.getCurrentChar(toEdit);
					amount = API.object.toInteger(option4);
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
					character = API.player.getCurrentChar(toEdit);
					amount = API.object.toInteger(option4);
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
					character = API.player.getCurrentChar(toEdit);
					amount = API.object.toInteger(option4);
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
		Player player = (Player) API.player.definePlayer(sender.getName());
		PlayerCharacter character = API.player.getCurrentChar(player);

		if(character == null || !character.isImmortal())
		{
			player.sendMessage(ChatColor.RED + "You cannot use that command, mortal.");
			return true;
		}

		// Define variables
		int kills = API.player.getKills(player);
		int deaths = API.player.getDeaths(player);
		int killstreak = character.getKillstreak();
		String charName = character.getName();
		String deity = character.getDeity();
		String alliance = character.getAlliance();
		int favor = character.getFavor();
		int maxFavor = character.getMaxFavor();
		int devotion = character.getDevotion();
		int ascensions = character.getAscensions();
		int devotionGoal = character.getDevotionGoal();
		int powerOffense = character.getPower(AbilityEvent.AbilityType.OFFENSE);
		int powerDefense = character.getPower(AbilityEvent.AbilityType.DEFENSE);
		int powerStealth = character.getPower(AbilityEvent.AbilityType.STEALTH);
		int powerSupport = character.getPower(AbilityEvent.AbilityType.SUPPORT);
		int powerPassive = character.getPower(AbilityEvent.AbilityType.PASSIVE);
		ChatColor deityColor = (ChatColor) API.data.getPluginData("temp_deity_colors", deity);
		ChatColor favorColor = character.getFavorColor();

		if(args.length == 1 && (args[0].equalsIgnoreCase("level") || args[0].equalsIgnoreCase("levels")))
		{
			// Send the user their info via chat
			API.misc.taggedMessage(sender, "Levels Check");

			sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Offense: " + ChatColor.GREEN + powerOffense);
			sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Defense: " + ChatColor.GREEN + powerDefense);
			sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Stealth: " + ChatColor.GREEN + powerStealth);
			sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Support: " + ChatColor.GREEN + powerSupport);
			sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Passive: " + ChatColor.GREEN + powerPassive);

			return true;
		}

		// Send the user their info via chat
		API.misc.taggedMessage(sender, "Player Check");

		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Character: " + ChatColor.AQUA + charName);
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Deity: " + deityColor + deity + ChatColor.WHITE + " of the " + ChatColor.GOLD + API.object.capitalize(alliance) + "s");
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Favor: " + favorColor + favor + ChatColor.GRAY + " (of " + ChatColor.GREEN + maxFavor + ChatColor.GRAY + ")");
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Ascensions: " + ChatColor.GREEN + ascensions);
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Devotion: " + ChatColor.GREEN + devotion + ChatColor.GRAY + " (" + ChatColor.YELLOW + (devotionGoal - devotion) + ChatColor.GRAY + " until next Ascension)");
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Kills: " + ChatColor.GREEN + kills + ChatColor.WHITE + " / Deaths: " + ChatColor.RED + deaths + ChatColor.WHITE + " / Killstreak: " + ChatColor.RED + killstreak);

		return true;
	}

	/*
	 * Command: "owner"
	 */
	private static boolean owner(CommandSender sender, String[] args)
	{
		Player player = (Player) API.player.definePlayer(sender.getName());

		if(args.length < 1)
		{
			player.sendMessage(ChatColor.RED + "You must select a character.");
			player.sendMessage(ChatColor.RED + "/owner <character>");
			return true;
		}

		PlayerCharacter charToCheck = API.character.getCharByName(args[0]);

		if(charToCheck.getName() == null)
		{
			player.sendMessage(ChatColor.RED + "That character doesn't exist.");
			return true;
		}
		else
		{
			player.sendMessage(API.deity.getDeityColor(charToCheck.getDeity()) + charToCheck.getName() + ChatColor.YELLOW + " belongs to " + charToCheck.getOwner().getName() + ".");
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

		for(Map.Entry<String, HashMap<String, Object>> player : API.data.getAllPlayers().entrySet())
		{

			String playerName = player.getKey();
			HashMap<String, Object> playerData = player.getValue();

			sender.sendMessage(playerName + ": ");

			for(Map.Entry<String, Object> playerDataEntry : playerData.entrySet())
			{
				sender.sendMessage("  - " + playerDataEntry.getKey() + ": " + playerDataEntry.getValue());
			}
		}

		sender.sendMessage(" ");
		sender.sendMessage("-- Characters ---------------");
		sender.sendMessage(" ");

		for(Map.Entry<Integer, HashMap<String, Object>> character : API.data.getAllPlayerChars((Player) sender).entrySet())
		{
			int charID = character.getKey();
			HashMap<String, Object> charData = character.getValue();

			sender.sendMessage(charID + ": ");

			for(Map.Entry<String, Object> charDataEntry : charData.entrySet())
			{
				sender.sendMessage("  - " + charDataEntry.getKey() + ": " + charDataEntry.getValue());
			}
		}
		return true;
	}

	/*
	 * Command: "viewBlocks"
	 */
	private static boolean viewBlocks(CommandSender sender)
	{
		for(Map.Entry<String, HashMap<Integer, Object>> block : API.data.getAllBlockData().entrySet())
		{
			String blockID = block.getKey();
			HashMap<Integer, Object> blockData = block.getValue();

			sender.sendMessage(blockID + ": ");

			for(Map.Entry<Integer, Object> blockDataEntry : blockData.entrySet())
			{
				sender.sendMessage("  - " + blockDataEntry.getKey() + ": " + blockDataEntry.getValue());
			}
		}
		return true;
	}

	/*
	 * Command: "removeChar"
	 */
	private static boolean removeChar(CommandSender sender, String[] args)
	{
		if(args.length != 1) return false;

		// Define args
		Player player = (Player) API.player.definePlayer(sender.getName());
		String charName = args[0];

		if(API.player.hasCharName(player, charName))
		{
			PlayerCharacter character = API.character.getCharByName(charName);
			int charID = character.getID();
			API.data.removeChar(charID);

			sender.sendMessage(ChatColor.RED + "Character removed!");
		}
		else sender.sendMessage(ChatColor.RED + "There was an error while removing your character.");

		return true;
	}
}
