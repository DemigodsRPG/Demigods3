package com.legit2.Demigods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.legit2.Demigods.Libraries.ReflectCommand;

public class DCommandExecutor
{
	static Demigods plugin;
	
	public DCommandExecutor(Demigods instance)
	{
		plugin = instance;
	}
	
	/*
	 *  Command: "viewhashmaps"
	 */
	@ReflectCommand.Command(name = "viewhashmaps", sender = ReflectCommand.Sender.EVERYONE, permission = "demigods.basic")
	public static boolean viewhashmaps(CommandSender sender)
	{
		HashMap<String, Object> player_data = DSave.getAllPlayerData(sender.getName());	
		HashMap<String, HashMap<String, Object>> player_deities = DSave.getAllDeityData(sender.getName());	

		// Loop through player data entry set and add to database
		for(Map.Entry<String, Object> entry : player_data.entrySet())
		{
			String id = entry.getKey();
			Object data = entry.getValue();

			sender.sendMessage(id + ": " + data.toString());
		}
		
		for(Map.Entry<String, HashMap<String, Object>> deity : player_deities.entrySet())
		{
			String deity_name = deity.getKey();
			HashMap<String, Object> deity_data = deity.getValue();
			
			for(Map.Entry<String, Object> entry : deity_data.entrySet())
			{
				String id = entry.getKey();
				Object data = entry.getValue();
				
				sender.sendMessage(deity_name + ": " + id + ", " + data);
			}
		}
		
		return true;
	}

	/*
	 *  Command: "dg"
	 */
	@ReflectCommand.Command(name = "dg", sender = ReflectCommand.Sender.PLAYER, permission = "demigods.basic")
	public static boolean dg(CommandSender sender, String arg1, String arg2)
	{
		if(arg1 != "noargs")
		{
			dg_info(sender, arg1, arg2);
			return true;
		}
				
		// Define Player
		Player player = DUtil.definePlayer(sender);
		
		// Check Permissions
		if(!DUtil.hasPermissionOrOP(player, "demigods.basic")) return DUtil.noPermission(player);
		
		DUtil.taggedMessage(sender, "Information Directory");
		for(String alliance : DUtil.getLoadedDeityAlliances()) sender.sendMessage(ChatColor.GRAY + "/dg " + alliance.toLowerCase());
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
	public static boolean dg_info(CommandSender sender, String category, String subcategory)
	{
		// Define Player
		Player player = DUtil.definePlayer(sender);
		
		// Check Permissions
		if(!DUtil.hasPermissionOrOP(player, "demigods.basic")) return DUtil.noPermission(player);
		
		for(String alliance : DUtil.getLoadedDeityAlliances())
		{
			if(category.equalsIgnoreCase("alliance"))
			{
				if(subcategory == "noargs")
				{
					DUtil.taggedMessage(sender, alliance + " Directory");
				
					for(String deity : DUtil.getAllDeitiesInAlliance(alliance))
					{
						sender.sendMessage(ChatColor.GRAY + "/dg " + alliance.toLowerCase() + " " + deity.toLowerCase());
					}
				}
				else
				{
					for(String deity : DUtil.getAllDeitiesInAlliance(alliance))
					{
						if(subcategory.equalsIgnoreCase(deity))
						{
							try
							{
								for(String toPrint : (ArrayList<String>) DUtil.invokeDeityMethodWithString(deity, "getInfo", player.getName()))
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
	@ReflectCommand.Command(name = "check", sender = ReflectCommand.Sender.EVERYONE, usage = "/check", permission = "demigods.basic")
	public static boolean check(CommandSender sender)
	{
		// Define Player and Username
		Player player = DUtil.definePlayer(sender);
		String username = player.getName();
		
		if(!DUtil.isImmortal(username))
		{
			player.sendMessage(ChatColor.RED + "You cannot use that command, mortal.");
			return true;
		}
				
		// Define variables
		HashMap<String, Object> player_data = DUtil.getAllPlayerData(username);
		HashMap<String, HashMap<String, Object>> player_deities = DUtil.getAllDeityData(username);

		String favor = null;
		String ascensions = null;
		String kills = null;
		String deaths = null;
		String alliance = null;
		ArrayList<String> deity_list = new ArrayList<String>();
		
		// Loop through player data entry set and them to variables
		for(Map.Entry<String, Object> entry : player_data.entrySet())
		{
			String id = entry.getKey();
			Object data = entry.getValue();

			// Don't save if it's temporary data
			if(id.equalsIgnoreCase("alliance")) alliance = data.toString();
			if(id.equalsIgnoreCase("favor")) favor = data.toString();
			if(id.equalsIgnoreCase("ascensions")) ascensions = data.toString();
			if(id.equalsIgnoreCase("kills")) kills = data.toString();
			if(id.equalsIgnoreCase("deaths")) deaths = data.toString();
		}
		
		// Loop through deity data entry set and add them to variables
		for(Map.Entry<String, HashMap<String, Object>> deity : player_deities.entrySet())
		{
			String deity_name = deity.getKey();
			HashMap<String, Object> deity_data = deity.getValue();
			
			for(Map.Entry<String, Object> entry : deity_data.entrySet())
			{
				// Create variables
				String id = entry.getKey();
				Object data = entry.getValue();
				String devotion = null;
				
				// Don't save if it's temporary data
				if(id.contains("devotion"))
				{
					devotion = data.toString();
					deity_list.add(deity_name + " [" + devotion + "]");
				}
			}
		}
		
			
		// Send the user their info via chat
		DUtil.taggedMessage(sender, "Player check: " + ChatColor.YELLOW + username);
		sender.sendMessage("Alliance: " + ChatColor.DARK_GREEN + alliance);
		sender.sendMessage("Deities: " + ChatColor.DARK_GREEN + deity_list.toString());
		sender.sendMessage("Favor: " + ChatColor.GREEN + favor);
		sender.sendMessage("Ascensions: " + ChatColor.GREEN + ascensions);
		sender.sendMessage(" ");
		sender.sendMessage("Kills: " + ChatColor.GREEN + kills + ChatColor.WHITE + " / Deaths: " + ChatColor.RED + deaths);
	
		return true;
	}
	
	/*
	 *  Command: "setalliance"
	 */
	@ReflectCommand.Command(name = "setalliance", sender = ReflectCommand.Sender.PLAYER, permission = "demigods.basic")
	public static boolean setAlliance(CommandSender sender, String arg1, String arg2)
	{
		if(arg1 == "noargs") return false;
		
		DUtil.setAlliance(arg1, arg2);
		sender.sendMessage(ChatColor.YELLOW + "You've given " + arg2 + " to " + arg1 + "!");
		
		return true;
	}
	
	/*
	 *  Command: "setfavor"
	 */
	@ReflectCommand.Command(name = "setfavor", sender = ReflectCommand.Sender.PLAYER, permission = "demigods.basic")
	public static boolean setFavor(CommandSender sender, String arg1, String arg2)
	{
		if(arg1 == "noargs") return false;
		
		DUtil.setFavor(arg1, new Integer(arg2));
		sender.sendMessage(ChatColor.YELLOW + "You've set " + arg1 + "'s " + ChatColor.GREEN + "favor " + ChatColor.YELLOW + "to " + ChatColor.GREEN + arg2 +  ChatColor.YELLOW + "!");
		
		return true;
	}
	
	/*
	 *  Command: "setascensions"
	 */
	@ReflectCommand.Command(name = "setascensions", sender = ReflectCommand.Sender.PLAYER, permission = "demigods.basic")
	public static boolean setAscensions(CommandSender sender, String arg1, String arg2)
	{
		if(arg1 == "noargs") return false;
		
		DUtil.setAscensions(arg1, new Integer(arg2));
		sender.sendMessage(ChatColor.YELLOW + "You've set " + arg1 + "'s " + ChatColor.GREEN + "ascensions " + ChatColor.YELLOW + "to " + ChatColor.GREEN + arg2 +  ChatColor.YELLOW + "!");
		
		return true;
	}
	
	/*
	 *  Command: "setdevotion"
	 */
	@ReflectCommand.Command(name = "setdevotion", sender = ReflectCommand.Sender.PLAYER, permission = "demigods.basic")
	public static boolean setDevotion(CommandSender sender, String arg1, String arg2, String arg3)
	{
		if(arg1 == "noargs") return false;
		
		DUtil.setDevotion(arg1, arg2, new Integer(arg3));
		sender.sendMessage(ChatColor.YELLOW + "You've set " + arg1 + "'s " + ChatColor.GREEN + "devotion " + ChatColor.YELLOW + "for " + ChatColor.GREEN + arg2 + ChatColor.YELLOW + " to " + ChatColor.GREEN + arg3 +  ChatColor.YELLOW + "!");
		
		return true;
	}
	
	/*
	 *  Command: "givedeity"
	 */
	@ReflectCommand.Command(name = "givedeity", sender = ReflectCommand.Sender.PLAYER, permission = "demigods.basic")
	public static boolean giveDeity(CommandSender sender, String arg1, String arg2)
	{
		if(arg1 == "noargs") return false;
		
		DUtil.giveDeity(arg1, arg2);
		DUtil.setImmortal(arg1, true);
		DUtil.setFavor(arg1, 9999);
		DUtil.setAscensions(arg1, 9999);
		DUtil.setDevotion(arg1, arg2, 9999);
		DUtil.setKills(arg1, 9999);
		
		sender.sendMessage(ChatColor.YELLOW + "You've given " + arg2 + " to " + arg1 + "!");
		
		return true;
	}
}