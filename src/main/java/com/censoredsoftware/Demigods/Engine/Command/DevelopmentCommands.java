package com.censoredsoftware.Demigods.Engine.Command;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Battle;
import com.censoredsoftware.Demigods.Engine.Object.DCommand;
import com.censoredsoftware.Demigods.Engine.Object.DPlayer;
import com.censoredsoftware.Demigods.Engine.Utility.MiscUtility;
import com.google.common.collect.Lists;

public class DevelopmentCommands extends DCommand
{
	@Override
	public List<String> getCommands()
	{
		return Lists.newArrayList("test1", "test2", "test3", "hspawn", "soundtest", "removechar");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String labels, String[] args)
	{
		if(command.getName().equalsIgnoreCase("removechar")) return removeChar(sender, args);
		else if(command.getName().equalsIgnoreCase("test1")) return test1(sender, args);
		else if(command.getName().equalsIgnoreCase("test2")) return test2(sender, args);
		else if(command.getName().equalsIgnoreCase("test3")) return test3(sender, args);
		else if(command.getName().equalsIgnoreCase("hspawn")) return hspawn(sender);
		else if(command.getName().equalsIgnoreCase("soundtest")) return soundTest(sender, args);
		return false;
	}

	private static boolean test1(CommandSender sender, final String[] args)
	{
		Player player = (Player) sender;

		player.sendMessage("Disabling all battles...");

		for(Battle battle : Battle.Util.getAllActive())
		{
			battle.end();
		}

		player.sendMessage("All battles disabled!");

		return true;
	}

	private static boolean test2(CommandSender sender, final String[] args)
	{
		Player player = (Player) sender;

		if(args.length == 2 && MiscUtility.isInt(args[0]) && MiscUtility.isInt(args[1]))
		{
			try
			{
				int number = Integer.parseInt(args[0]);
				int divisor = Integer.parseInt(args[1]);
				long start = System.currentTimeMillis();
				int answer = MiscUtility.getClosestIntDivisibleBy(number, divisor);
				long end = System.currentTimeMillis();
				player.sendMessage("Pass: " + (number % divisor == 0));
				player.sendMessage("Closest: " + answer);
				player.sendMessage("Time: " + (int) ((end - start) / 1000) + " seconds.");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		return true;
	}

	private static boolean test3(CommandSender sender, final String[] args)
	{
		Player player = (Player) sender;

		if(Battle.Util.existsInRadius(player.getLocation()))
		{
			Battle getInfo = Battle.Util.getInRadius(player.getLocation());

			Demigods.message.chatTitle(ChatColor.DARK_AQUA + "Battle Info");
			Location center = getInfo.getStartLocation();
			player.sendMessage(ChatColor.YELLOW + "Center: " + ChatColor.GRAY + StringUtils.capitalize(center.getWorld().getName().toLowerCase()) + ": " + Math.round(center.getX()) + ", " + Math.round(center.getY()) + ", " + Math.round(center.getZ()));
			StringBuilder butts = new StringBuilder(ChatColor.YELLOW + "Participants: ");
			for(Battle.Participant participant : getInfo.getMeta().getParticipants())
				if(participant instanceof DPlayer.Character) butts.append(((DPlayer.Character) participant).getName() + ", ");
			player.sendMessage(ChatColor.YELLOW + "Participants: " + ChatColor.GRAY + butts.substring(0, 2) + ".");
		}

		return true;
	}

	private static boolean hspawn(CommandSender sender)
	{
		Player player = (Player) sender;

		// This SHOULD happen automatically, but bukkit doesn't do this, so we need to.

		if(player.isInsideVehicle() && player.getVehicle() instanceof Horse)
		{
			Horse horse = (Horse) player.getVehicle();
			horse.eject();
			horse.teleport(player.getLocation().getWorld().getSpawnLocation());
			horse.setPassenger(player);
			player.sendMessage(ChatColor.YELLOW + "Teleported to spawn...");
		}

		return true;
	}

	private static boolean soundTest(CommandSender sender, final String[] args)
	{
		if(sender instanceof ConsoleCommandSender) return false;
		Player player = (Player) sender;
		try
		{
			Sound sound = Sound.valueOf(args[0].toUpperCase());
			if(!MiscUtility.isFloat(args[1].toUpperCase()))
			{
				player.sendMessage(ChatColor.RED + "Set a pitch, ie: 1F");
				return false;
			}
			else
			{
				player.playSound(player.getLocation(), sound, 1F, Float.parseFloat(args[1].toUpperCase()));
				player.sendMessage(ChatColor.YELLOW + "Sound played.");
				return true;
			}
		}
		catch(Exception ignored)
		{}
		player.sendMessage(ChatColor.RED + "Wrong arguments, please try again.");
		return false;
	}

	private static boolean removeChar(CommandSender sender, String[] args)
	{
		if(args.length != 1) return false;

		// Define args
		Player player = Bukkit.getOfflinePlayer(sender.getName()).getPlayer();
		String charName = args[0];

		if(DPlayer.Util.hasCharName(player, charName))
		{
			DPlayer.Character character = DPlayer.Character.Util.getCharacterByName(charName);
			character.remove();

			sender.sendMessage(ChatColor.RED + "Character removed!");
		}
		else sender.sendMessage(ChatColor.RED + "There was an error while removing your character.");

		return true;
	}
}
