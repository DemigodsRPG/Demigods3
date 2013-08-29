package com.censoredsoftware.demigods.command;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.battle.Battle;
import com.censoredsoftware.demigods.helper.ColoredStringBuilder;
import com.censoredsoftware.demigods.helper.ListedCommand;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.structure.Altar;
import com.censoredsoftware.demigods.structure.Structure;
import com.censoredsoftware.demigods.util.Errors;
import com.censoredsoftware.demigods.util.Unicodes;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.Set;

public class DevelopmentCommands extends ListedCommand
{
	@Override
	public Set<String> getCommands()
	{
		return Sets.newHashSet("test1", "test2", "test3", "hspawn");
	}

	@Override
	public boolean processCommand(CommandSender sender, Command command, String[] args)
	{
		if(command.getName().equalsIgnoreCase("test1")) return test1(sender, args);
		else if(command.getName().equalsIgnoreCase("test2")) return test2(sender, args);
		else if(command.getName().equalsIgnoreCase("test3")) return test3(sender, args);
		else if(command.getName().equalsIgnoreCase("hspawn")) return hspawn(sender);
		else if(command.getName().equalsIgnoreCase("nearestaltar")) return nearestAltar(sender);
		return false;
	}

	private static boolean test1(CommandSender sender, final String[] args)
	{
		Player player = (Player) sender;

		for(Battle battle : Battle.Util.getAllActive())
			battle.end();

		player.sendMessage("All battles disabled!");

		return true;
	}

	private static boolean test2(CommandSender sender, final String[] args)
	{
		Player player = (Player) sender;

		if(Demigods.errorNoise) Errors.triggerError(ChatColor.GREEN + player.getName(), new ColoredStringBuilder().gray(" " + Unicodes.rightwardArrow() + " ").red("Test error.").build());

		return true;
	}

	private static boolean test3(CommandSender sender, final String[] args)
	{
		Player player = (Player) sender;

		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

		if(character == null)
		{
			player.sendMessage(ChatColor.RED + "You are mortal, we do not track mortals.");
			return true;
		}

		player.sendMessage("# of Teammates Online: " + DCharacter.Util.getOnlineCharactersWithAlliance(character.getAlliance()).size());
		player.sendMessage("# of Enemies Online: " + DCharacter.Util.getOnlineCharactersWithoutAlliance(character.getAlliance()).size());

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
			horse.teleport(player.getWorld().getSpawnLocation());
			horse.setPassenger(player);
			player.sendMessage(ChatColor.YELLOW + "Teleported to spawn...");
		}

		return true;
	}

	private static boolean nearestAltar(CommandSender sender)
	{
		Player player = (Player) sender;

		if(Altar.Util.isAltarNearby(player.getLocation()))
		{
			Structure.Save save = Altar.Util.getAltarNearby(player.getLocation());
			player.teleport(save.getReferenceLocation().clone().add(2, 0, 0));
			player.sendMessage(ChatColor.YELLOW + "Nearest Altar found.");
		}
		else player.sendMessage(ChatColor.YELLOW + "There is no alter nearby.");

		return true;
	}
}
