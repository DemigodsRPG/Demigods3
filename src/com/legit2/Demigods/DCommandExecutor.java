package com.legit2.Demigods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.legit2.Demigods.Libraries.ReflectCommand;
import com.legit2.Demigods.Utilities.*;

public class DCommandExecutor implements CommandExecutor
{
	static Demigods plugin;
	
	public DCommandExecutor(Demigods instance)
	{
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (command.getName().equalsIgnoreCase("dg")) return dg(sender,args);
		else if (command.getName().equalsIgnoreCase("check")) return check(sender);
		else if (command.getName().equalsIgnoreCase("setalliance")) return setAlliance(sender,args);
		else if (command.getName().equalsIgnoreCase("setfavor")) return setFavor(sender,args);
		else if (command.getName().equalsIgnoreCase("setascensions")) return setAscensions(sender,args);
		else if (command.getName().equalsIgnoreCase("setdevotion")) return setDevotion(sender,args);
		else if (command.getName().equalsIgnoreCase("createchar")) return createChar(sender,args);
		else if (command.getName().equalsIgnoreCase("viewmaps")) return viewMaps(sender);
		//else if (command.getName().equalsIgnoreCase("removeplayer")) return removePlayer(sender,args);
		
		// BETA TESTING ONLY
		else if (command.getName().equalsIgnoreCase("test1")) return test1(sender);

		return false;
	}
	
	/*
	 *  Command: "test1"
	 */
	public static boolean test1(CommandSender sender)
	{
		if(DUtil.hasPermissionOrOP((Player) DPlayerUtil.definePlayer(sender.getName()), "demigods.admin"))
		{
			DUtil.serverMsg(ChatColor.RED + "Manually forcing Demigods save...");
			if(DDatabase.saveAllData())
			{
				DUtil.serverMsg(ChatColor.GREEN + "Save complete!");
			}
			else
			{
				DUtil.serverMsg(ChatColor.RED + "There was a problem with saving...");
				DUtil.serverMsg(ChatColor.RED + "An admin should check the log immediately.");
			}
		}
		else DUtil.noPermission((Player) DPlayerUtil.definePlayer(sender.getName()));
		return true;
	}

	/*
	 *  Command: "dg"
	 */
	@ReflectCommand.Command(name = "dg", sender = ReflectCommand.Sender.PLAYER, permission = "demigods.basic")
	public static boolean dg(CommandSender sender, String[] args)
	{		
		if(args.length > 0)
		{
			dg_info(sender, args);
			return true;
		}
				
		// Define Player
		Player player = (Player) DPlayerUtil.definePlayer(sender.getName());
		
		// Check Permissions
		if(!DUtil.hasPermissionOrOP(player, "demigods.basic")) return DUtil.noPermission(player);
		
		DUtil.taggedMessage(sender, "Information Directory");
		for(String alliance : DDeityUtil.getLoadedDeityAlliances()) sender.sendMessage(ChatColor.GRAY + "/dg " + alliance.toLowerCase());
		sender.sendMessage(ChatColor.GRAY + "/dg claim");
		sender.sendMessage(ChatColor.GRAY + "/dg shrine");
		sender.sendMessage(ChatColor.GRAY + "/dg tribute");
		sender.sendMessage(ChatColor.GRAY + "/dg player");
		sender.sendMessage(ChatColor.GRAY + "/dg pvp");
		sender.sendMessage(ChatColor.GRAY + "/dg rankings");
		if(DUtil.hasPermissionOrOP(player, "demigods.admin")) sender.sendMessage(ChatColor.RED + "/dg admin");
		sender.sendMessage(ChatColor.WHITE + "Use " + ChatColor.YELLOW + "/check" + ChatColor.WHITE + " to see your player information.");
		return true;
	}

	/*
	 *  Command: "dg_info"
	 */
	@SuppressWarnings("unchecked")
	public static boolean dg_info(CommandSender sender, String[] args)
	{
		// Define Player
		Player player = (Player) DPlayerUtil.definePlayer(sender.getName());
		
		// Define args
		String category = args[0];
		
		// Check Permissions
		if(!DUtil.hasPermissionOrOP(player, "demigods.basic")) return DUtil.noPermission(player);
		
		for(String alliance : DDeityUtil.getLoadedDeityAlliances())
		{
			if(category.equalsIgnoreCase(alliance))
			{
				if(args.length < 2)
				{
					DUtil.taggedMessage(sender, alliance + " Directory");
				
					for(String deity : DDeityUtil.getAllDeitiesInAlliance(alliance))
					{
						sender.sendMessage(ChatColor.GRAY + "/dg " + alliance.toLowerCase() + " " + deity.toLowerCase());
					}
				}
				else
				{
					for(String deity : DDeityUtil.getAllDeitiesInAlliance(alliance))
					{
						if(args[1].equalsIgnoreCase(deity))
						{
							try
							{
								for(String toPrint : (ArrayList<String>) DDeityUtil.invokeDeityMethodWithString(DDeityUtil.getDeityClass(deity), "getInfo", player.getName()))
								{
									sender.sendMessage(toPrint);
								}
								return true;
							}
							catch (Exception e)
							{
								sender.sendMessage(ChatColor.RED + "Something went wrong with deity loading.");
								return true;
							}
						}
					}
					sender.sendMessage(ChatColor.DARK_RED + "No such deity, please try again.");
					return false;
				}
			}
		}
		
	
		if(category.equalsIgnoreCase("save"))
		{
			if(DUtil.hasPermissionOrOP(player, "demigods.admin"))
			{
				DUtil.serverMsg(ChatColor.RED + "Manually forcing Demigods save...");
				if(DDatabase.saveAllData())
				{
					DUtil.serverMsg(ChatColor.GREEN + "Save complete!");
				}
				else
				{
					DUtil.serverMsg(ChatColor.RED + "There was a problem with saving...");
					DUtil.serverMsg(ChatColor.RED + "An admin should check the log immediately.");
				}
			}
			else DUtil.noPermission(player);
		}
		else if(category.equalsIgnoreCase("claim"))
		{
			DUtil.taggedMessage(sender, "Claiming");
			sender.sendMessage(ChatColor.GRAY + " This is some info about Claiming.");
		}
		else if(category.equalsIgnoreCase("shrine"))
		{
			DUtil.taggedMessage(sender, "Shrines");
			sender.sendMessage(ChatColor.GRAY + " This is some info about Shrines.");
		}
		else if(category.equalsIgnoreCase("tribute"))
		{
			DUtil.taggedMessage(sender, "Tributes");
			sender.sendMessage(ChatColor.GRAY + " This is some info about Tributes.");
		}
		else if(category.equalsIgnoreCase("player"))
		{
			DUtil.taggedMessage(sender, "Players");
			sender.sendMessage(ChatColor.GRAY + " This is some info about Players.");
		}
		else if(category.equalsIgnoreCase("pvp"))
		{
			DUtil.taggedMessage(sender, "PVP");
			sender.sendMessage(ChatColor.GRAY + " This is some info about PVP.");
		}
		else if(category.equalsIgnoreCase("stats"))
		{
			DUtil.taggedMessage(sender, "Stats");
			sender.sendMessage(ChatColor.GRAY + " These are some stats for Demigods.");
		}
		else if(category.equalsIgnoreCase("rankings"))
		{
			DUtil.taggedMessage(sender, "Rankings");
			sender.sendMessage(ChatColor.GRAY + " This is some ranking info about Demigods.");
		}
		else if(category.equalsIgnoreCase("admin"))
		{
			DUtil.taggedMessage(sender, ChatColor.RED + "Admin Commands");
			sender.sendMessage(ChatColor.GRAY + "/setalliance <player> <alliance>");
			sender.sendMessage(ChatColor.GRAY + "/givedeity <player> <deity>");
			sender.sendMessage(ChatColor.GRAY + "/setdevotion <player> <deity> <amount>");
			sender.sendMessage(ChatColor.GRAY + "/setfavor <player> <amount>");
			sender.sendMessage(ChatColor.GRAY + "/setascensions <player> <amount>");
		}
		
		return true;
	}
	
	/*
	 *  Command: "check"
	 */
	public static boolean check(CommandSender sender)
	{
		// Define Player and Username
		Player player = (Player) DPlayerUtil.definePlayer(sender.getName());
		//String username = player.getName();
		
		if(!DPlayerUtil.isImmortal(player))
		{
			player.sendMessage(ChatColor.RED + "You cannot use that command, mortal.");
			return true;
		}		
			
		// Send the user their info via chat
		DUtil.customTaggedMessage(sender, "Demigods Player Check", null);
		
		/*
		sender.sendMessage(ChatColor.RESET + "Name: " + ChatColor.AQUA + username + ChatColor.RESET + " of the " + ChatColor.ITALIC + DObjUtil.capitalize(alliance) + "s");
		sender.sendMessage("Favor: " + ChatColor.GREEN + favor);
		sender.sendMessage("Ascensions: " + ChatColor.GREEN + ascensions);
		sender.sendMessage(" ");
		
		sender.sendMessage("Deities: ");
		
			// List each deity separately
			for(Object deity : deity_list)
			{
				sender.sendMessage("  " + deity);
			}
			
		sender.sendMessage(" ");
		sender.sendMessage("Kills: " + ChatColor.GREEN + kills + ChatColor.WHITE + " / Deaths: " + ChatColor.RED + deaths);
		*/
		
		return true;
	}
	
	/*
	 *  Command: "viewMaps"
	 */
	public static boolean viewMaps(CommandSender sender)
	{
		sender.sendMessage("-- Players ------------------");
		sender.sendMessage(" ");

		for(Entry<String, HashMap<String, Object>> player : DDataUtil.getAllPlayers().entrySet())
		{

			String playerName = player.getKey();
			HashMap<String, Object> playerData = player.getValue();
			
			sender.sendMessage(playerName + ": ");

			for(Entry<String, Object> playerDataEntry : playerData.entrySet())
			{
				sender.sendMessage("  - " + playerDataEntry.getKey() + ": " + playerDataEntry.getValue());
			}
		}
		
		sender.sendMessage(" ");
		sender.sendMessage("-- Characters ---------------");
		sender.sendMessage(" ");

		for(Entry<Integer, HashMap<String, Object>> character : DDataUtil.getAllPlayerChars(Bukkit.getPlayer(sender.getName())).entrySet())
		{
			int charID = character.getKey();
			HashMap<String, Object> charData = character.getValue();
			
			sender.sendMessage(charID + ": ");

			for(Entry<String, Object> charDataEntry : charData.entrySet())
			{
				sender.sendMessage("  - " + charDataEntry.getKey() + ": " + charDataEntry.getValue());
			}
		}
		return true;
	}
	
	/*
	 *  Command: "createChar"
	 */
	public static boolean createChar(CommandSender sender, String[] args)
	{
		if(args.length != 2) return false;
		
		// Define args
		Player player = (Player) DPlayerUtil.definePlayer(sender.getName());
		String charName = args[0];
		String charDeity = args[1];
		
		if(DCharUtil.createChar(player, charName, charDeity)) sender.sendMessage(ChatColor.YELLOW + "Character " + charName + "(" + charDeity + ") created!");
		else player.sendMessage(ChatColor.RED + "You already have a character with that name.");

		return true;
	}
	
	/*
	 *  Command: "removePlayer"
	 *
	public static boolean removePlayer(CommandSender sender, String[] args)
	{	
		if(args.length != 1) return false;
		
		// Define args
		String username = args[0];

		try
		{
			DDatabase.removePlayer(username);
			DDatabase.addPlayer(username);
		}
		catch(Exception e)
		{
			
		}
		
		Bukkit.getPlayer(username).sendMessage(ChatColor.YELLOW + "You have been reset in the Demigods database by " + ChatColor.AQUA + sender.getName() + ChatColor.YELLOW + ".");
		sender.sendMessage(ChatColor.RED + "You have removed " + ChatColor.AQUA + username + ChatColor.RED + " from the database!");
		
		return true;
	}
	*/
	
	/*
	 *  Command: "setalliance"
	 */
	public static boolean setAlliance(CommandSender sender, String[] args)
	{	
		if(args.length != 2) return false;
		
		// Define args
		String username = args[0];
		Player player = (Player) DPlayerUtil.definePlayer(username); 
		int charID = DPlayerUtil.getCurrentChar(player);
		String alliance = args[1];
		
		DCharUtil.setAlliance(player, charID, alliance);
		sender.sendMessage(ChatColor.YELLOW + "You've given " + alliance + " to " + username + "!");
		
		return true;
	}
	
	/*
	 *  Command: "setfavor"
	 */
	public static boolean setFavor(CommandSender sender, String[] args)
	{
		if(args.length != 2) return false;
		
		// Define args
		String username = args[0];
		Player player = (Player) DPlayerUtil.definePlayer(username); 
		Integer favor = new Integer(args[1]);
		
		DCharUtil.setFavor(player, favor);
		sender.sendMessage(ChatColor.YELLOW + "You've set " + username + "'s " + ChatColor.GREEN + "favor " + ChatColor.YELLOW + "to " + ChatColor.GREEN + favor +  ChatColor.YELLOW + "!");
		
		return true;
	}
	
	/*
	 *  Command: "setascensions"
	 */
	public static boolean setAscensions(CommandSender sender, String[] args)
	{
		if(args.length != 2) return false;
		
		// Define args
		String username = args[0];
		Player player = (Player) DPlayerUtil.definePlayer(username); 
		Integer ascensions = new Integer(args[1]);
		
		DCharUtil.setAscensions(player, ascensions);
		sender.sendMessage(ChatColor.YELLOW + "You've set " + username + "'s " + ChatColor.GREEN + "ascensions " + ChatColor.YELLOW + "to " + ChatColor.GREEN + ascensions +  ChatColor.YELLOW + "!");
		
		return true;
	}
	
	/*
	 *  Command: "setdevotion"
	 */
	public static boolean setDevotion(CommandSender sender, String[] args)
	{
		if(args.length != 3) return false;
		
		// Define args
		String username = args[0];
		Player player = (Player) DPlayerUtil.definePlayer(username); 
		int charID = DPlayerUtil.getCurrentChar(player);
		String deity = args[1];
		Integer devotion = new Integer(args[2]);
		
		DCharUtil.setDevotion(player, charID, devotion);
		sender.sendMessage(ChatColor.YELLOW + "You've set " + username + "'s " + ChatColor.GREEN + "devotion " + ChatColor.YELLOW + "for " + ChatColor.GREEN + deity + ChatColor.YELLOW + " to " + ChatColor.GREEN + devotion +  ChatColor.YELLOW + "!");
		
		return true;
	}

	
}