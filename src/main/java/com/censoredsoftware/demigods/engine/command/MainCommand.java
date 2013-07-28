package com.censoredsoftware.demigods.engine.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.censoredsoftware.demigods.engine.util.Admins;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Unicodes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.data.DataManager;
import com.censoredsoftware.demigods.engine.element.Ability;
import com.censoredsoftware.demigods.engine.element.Deity;
import com.censoredsoftware.demigods.engine.language.TranslationManager;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.engine.util.Configs;
import com.google.common.collect.Lists;

public class MainCommand extends DCommand
{
	@Override
	public List<String> getCommands()
	{
		return Lists.newArrayList("demigods", "deity");
	}

	@Override
	public boolean process(CommandSender sender, Command command, String[] args)
	{
		// Check for console first
		if(sender instanceof ConsoleCommandSender) return Messages.noConsole((ConsoleCommandSender) sender);

		// Check args and pass onto dg_extended() if need be
		if(args.length > 0)
		{
			dg_extended(sender, args);
			return true;
		}

		// Define Player
		Player player = Bukkit.getOfflinePlayer(sender.getName()).getPlayer();

		// Check Permissions
		if(!player.hasPermission("demigods.basic")) return Messages.noPermission(player);

		if(command.getName().equals("deity") && DPlayer.Util.getPlayer(player).getCurrent() != null && DPlayer.Util.getPlayer(player).getCurrent().canUse())
		{
			Deity deity = DPlayer.Util.getPlayer(player).getCurrent().getDeity();
			player.chat("/dg " + deity.getInfo().getAlliance().toLowerCase() + " " + deity.getInfo().getName().toLowerCase());
			return true;
		}
		else if(command.getName().equals("deity"))
		{
			player.sendMessage(ChatColor.RED + "This command requires you to have a character.");
			return true;
		}

		Messages.tagged(sender, "Documentation");
		for(String alliance : Deity.getLoadedDeityAlliances())
		{
			if(!sender.hasPermission("demigods." + alliance.toLowerCase())) continue;
			sender.sendMessage(ChatColor.GRAY + " /dg " + alliance.toLowerCase());
		}
		sender.sendMessage(ChatColor.GRAY + " /dg info");
		sender.sendMessage(ChatColor.GRAY + " /dg commands");
		if(player.hasPermission("demigods.admin")) sender.sendMessage(ChatColor.RED + " /dg admin");
		sender.sendMessage(" ");
		sender.sendMessage(ChatColor.WHITE + " Use " + ChatColor.YELLOW + "/check" + ChatColor.WHITE + " to see your player information.");
		return true;
	}

	private static boolean dg_extended(CommandSender sender, String[] args)
	{
		// Define Player
		Player player = (Player) sender;

		// Define args
		String category = args[0];
		String option1 = null, option2 = null, option3 = null, option4 = null;
		if(args.length >= 2) option1 = args[1];
		if(args.length >= 3) option2 = args[2];
		if(args.length >= 4) option3 = args[3];
		if(args.length >= 5) option4 = args[4];

		// Check Permissions
		if(!player.hasPermission("demigods.basic")) return Messages.noPermission(player);

		if(category.equalsIgnoreCase("admin"))
		{
			dg_admin(sender, option1, option2, option3, option4);
		}
		else if(category.equalsIgnoreCase("commands"))
		{
			Messages.tagged(sender, "Command Directory");
			sender.sendMessage(ChatColor.GRAY + " There's nothing here..."); // TODO
		}
		else if(category.equalsIgnoreCase("info"))
		{
			if(option1 == null)
			{
				Messages.tagged(sender, "Information Directory");
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
				Messages.tagged(sender, "About the Plugin");
				sender.sendMessage(ChatColor.WHITE + " Not to be confused with other RPG plugins that focus on skills and classes alone, " + ChatColor.GREEN + "demigods" + ChatColor.WHITE + " adds culture and conflict that will keep players coming back even after they've maxed out their levels and found all of the diamonds in a 50km radius.");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.GREEN + " Demigods" + ChatColor.WHITE + " is unique in its system of rewarding players for both adventuring (tributes) and conquering (PvP) with a wide array of fun and useful skills.");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.WHITE + " Re-enact mythological battles and rise from a mere player to a full-fledged Olympian as you form new Alliances with mythical groups and battle to the bitter end.");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.GRAY + " Developed by: " + ChatColor.GREEN + "_Alex" + ChatColor.GRAY + " and " + ChatColor.GREEN + "HmmmQuestionMark");
				sender.sendMessage(ChatColor.GRAY + " Website: " + ChatColor.YELLOW + "http://demigodsrpg.com/");
				sender.sendMessage(ChatColor.GRAY + " Source: " + ChatColor.YELLOW + "https://github.com/CensoredSoftware/Minecraft-Demigods");
			}
			else if(option1.equalsIgnoreCase("characters"))
			{
				Messages.tagged(sender, "Characters");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Characters.");
			}
			else if(option1.equalsIgnoreCase("shrine"))
			{
				Messages.tagged(sender, "Shrines");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Shrines.");
			}
			else if(option1.equalsIgnoreCase("tribute"))
			{
				Messages.tagged(sender, "Tributes");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Tributes.");
			}
			else if(option1.equalsIgnoreCase("player"))
			{
				Messages.tagged(sender, "Players");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Players.");
			}
			else if(option1.equalsIgnoreCase("pvp"))
			{
				Messages.tagged(sender, "PVP");
				sender.sendMessage(ChatColor.GRAY + " This is some info about PVP.");
			}
			else if(option1.equalsIgnoreCase("stats"))
			{
				Messages.tagged(sender, "Stats");
				sender.sendMessage(ChatColor.GRAY + " Read some server-wide stats for Demigods.");
			}
			else if(option1.equalsIgnoreCase("rankings"))
			{
				Messages.tagged(sender, "Rankings");
				sender.sendMessage(ChatColor.GRAY + " This is some ranking info about Demigods.");
			}
		}

		for(String alliance : Deity.getLoadedDeityAlliances())
		{
			if(!sender.hasPermission("demigods." + alliance.toLowerCase())) continue;
			if(category.equalsIgnoreCase(alliance))
			{
				if(args.length < 2)
				{
					Messages.tagged(sender, alliance + " Directory");
					for(Deity deity : Deity.Util.getAllDeitiesInAlliance(alliance))
						sender.sendMessage(ChatColor.GRAY + " /dg " + alliance.toLowerCase() + " " + deity.getInfo().getName().toLowerCase());
				}
				else
				{
					for(final Deity deity : Deity.Util.getAllDeitiesInAlliance(alliance))
					{
						assert option1 != null;
						if(option1.equalsIgnoreCase(deity.getInfo().getName()))
						{
							try
							{
								for(String toPrint : new ArrayList<String>()
								{
									{
										addAll(deity.getInfo().getLore());
										for(Ability ability : deity.getAbilities())
										{
											for(String detail : ability.getInfo().getDetails())
											{
												StringBuilder details = new StringBuilder(ChatColor.GRAY + " " + Unicodes.rightwardArrow() + " ");
												if(ability.getInfo().getCommand() != null) details.append(ChatColor.GREEN + "/" + ability.getInfo().getCommand().toLowerCase() + ChatColor.WHITE + ": ");
												details.append(ChatColor.WHITE + detail);
												add(details.toString());
											}
										}
									}
								})
								{
									sender.sendMessage(toPrint);
								}
								return true;
							}
							catch(Exception e)
							{
								sender.sendMessage(ChatColor.RED + "(ERR: 3001)  Please report this immediately.");
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

	private static boolean dg_admin(CommandSender sender, String option1, String option2, String option3, String option4)
	{
		Player player = Bukkit.getOfflinePlayer(sender.getName()).getPlayer();
		Player toEdit;
		DCharacter character;
		int amount;

		if(!player.hasPermission("demigods.admin")) return Messages.noPermission(player);

		if(option1 == null)
		{
			Messages.tagged(sender, "Admin Directory");
			sender.sendMessage(ChatColor.GRAY + " /dg admin wand");
			sender.sendMessage(ChatColor.GRAY + " /dg admin debug");
			sender.sendMessage(ChatColor.GRAY + " /dg admin check <p> <char>");
			sender.sendMessage(ChatColor.GRAY + " /dg admin remove [player|character] <name>");
			sender.sendMessage(ChatColor.GRAY + " /dg admin set [maxfavor|favor|devotion|ascensions] <p> <amt>");
			sender.sendMessage(ChatColor.GRAY + " /dg admin add [maxfavor|favor|devotion|ascensions] <p> <amt>");
			sender.sendMessage(ChatColor.GRAY + " /dg admin sub [maxfavor|favor|devotion|ascensions] <p> <amt>");
			sender.sendMessage(ChatColor.RED + " /dg admin clear data yesdoitforsurepermanantly");
		}

		if(option1 != null)
		{
			if(option1.equalsIgnoreCase("clear") && option2 != null && option2.equalsIgnoreCase("data") && option3 != null && option3.equalsIgnoreCase("yesdoitforsurepermanantly"))
			{
				player.sendMessage(ChatColor.RED + Demigods.text.getText(TranslationManager.Text.ADMIN_CLEAR_DATA_STARTING));
				DataManager.flushData();
				player.sendMessage(ChatColor.GREEN + Demigods.text.getText(TranslationManager.Text.ADMIN_CLEAR_DATA_FINISHED));
				return true;
			}
			if(option1.equalsIgnoreCase("wand"))
			{
				if(!Admins.wandEnabled(player))
				{
					DataManager.saveTemp(player.getName(), "temp_admin_wand", true);
					player.sendMessage(ChatColor.RED + "Your admin wand has been enabled for " + Material.getMaterial(Configs.getSettingInt("admin.wand_tool")));
				}
				else if(Admins.wandEnabled(player))
				{
					DataManager.removeTemp(player.getName(), "temp_admin_wand");
					player.sendMessage(ChatColor.RED + "You have disabled your admin wand.");
				}
				return true;
			}
			else if(option1.equalsIgnoreCase("debug"))
			{
				if(!DataManager.hasKeyTemp(player.getName(), "temp_admin_debug") || !Boolean.parseBoolean(DataManager.getValueTemp(player.getName(), "temp_admin_debug").toString()))
				{
					DataManager.saveTemp(player.getName(), "temp_admin_debug", true);
					player.sendMessage(ChatColor.RED + "You have enabled debugging.");
				}
				else if(DataManager.hasKeyTemp(player.getName(), "temp_admin_debug") && Boolean.parseBoolean(DataManager.getValueTemp(player.getName(), "temp_admin_debug").toString()))
				{
					DataManager.removeTemp(player.getName(), "temp_admin_debug");
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
					Messages.tagged(sender, ChatColor.RED + toCheck.getName() + " Player Check");
					sender.sendMessage(" Characters:");

					final Set<DCharacter> chars = DPlayer.Util.getCharacters(toCheck);

					for(DCharacter checkingChar : chars)
					{
						player.sendMessage(ChatColor.GRAY + "   (#: " + checkingChar.getId() + ") Name: " + checkingChar.getName() + " / Deity: " + checkingChar.getDeity());
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
						DCharacter removing = DCharacter.Util.getCharacterByName(option3);
						String removingName = removing.getName();

						// Remove the data
						removing.remove();

						sender.sendMessage(ChatColor.RED + "Character \"" + removingName + "\" removed.");
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
					character = DPlayer.Util.getPlayer(toEdit).getCurrent();
					amount = Integer.parseInt(option4);
				}

				if(option2.equalsIgnoreCase("maxfavor"))
				{
					// Set the favor
					character.getMeta().setMaxFavor(amount);

					sender.sendMessage(ChatColor.GREEN + "Max favor set to " + amount + " for " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character's max favor has been set to " + amount + ".");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("favor"))
				{
					// Set the favor
					character.getMeta().setFavor(amount);

					sender.sendMessage(ChatColor.GREEN + "Favor set to " + amount + " for " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character's favor has been set to " + amount + ".");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("ascensions"))
				{
					// Set the ascensions
					character.getMeta().setAscensions(amount);

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
					character = DPlayer.Util.getPlayer(toEdit).getCurrent();
					amount = Integer.parseInt(option4);
				}

				if(option2.equalsIgnoreCase("maxfavor"))
				{
					// Set the favor
					character.getMeta().setMaxFavor(character.getMeta().getMaxFavor() + amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " added to " + toEdit.getName() + "'s current character's max favor.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character's max favor has been increased by " + amount + ".");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("favor"))
				{
					// Set the favor
					character.getMeta().addFavor(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " favor added to " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character has been given " + amount + " favor.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("ascensions"))
				{
					// Set the ascensions
					character.getMeta().addAscensions(amount);

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
					character = DPlayer.Util.getPlayer(toEdit).getCurrent();
					amount = Integer.parseInt(option4);
				}

				if(option2.equalsIgnoreCase("maxfavor"))
				{
					// Set the favor
					character.getMeta().subtractFavor(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " removed from " + toEdit.getName() + "'s current character's max favor.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.RED + "Your current character's max favor has been reduced by " + amount + ".");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				if(option2.equalsIgnoreCase("favor"))
				{
					// Set the favor
					character.getMeta().subtractFavor(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " favor removed from " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.RED + "Your current character has had " + amount + " favor removed.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("ascensions"))
				{
					// Set the ascensions
					character.getMeta().subtractAscensions(amount);

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
}
