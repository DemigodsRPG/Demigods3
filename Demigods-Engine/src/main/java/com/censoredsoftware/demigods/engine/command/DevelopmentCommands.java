package com.censoredsoftware.demigods.engine.command;

import com.censoredsoftware.censoredlib.helper.WrappedCommand;
import com.censoredsoftware.censoredlib.schematic.Schematic;
import com.censoredsoftware.censoredlib.util.Images;
import com.censoredsoftware.censoredlib.util.WorldGuards;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.data.DataManager;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.engine.structure.Structure;
import com.censoredsoftware.demigods.engine.structure.StructureData;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class DevelopmentCommands extends WrappedCommand
{
	public DevelopmentCommands()
	{
		super(Demigods.PLUGIN, false);
	}

	@Override
	public Set<String> getCommands()
	{
		return Sets.newHashSet("obelisk", "test1", "test2", "test3");
	}

	@Override
	public boolean processCommand(CommandSender sender, Command command, String[] args)
	{
		if(command.getName().equalsIgnoreCase("test1")) return test1(sender, args);
		else if(command.getName().equalsIgnoreCase("test2")) return test2(sender, args);
		else if(command.getName().equalsIgnoreCase("test3")) return test3(sender, args);
		else if(command.getName().equalsIgnoreCase("obelisk")) return obelisk(sender, args);
		return false;
	}

	private static boolean test1(CommandSender sender, final String[] args)
	{
		Player player = (Player) sender;

		sender.sendMessage("ZONES_NO_PVP: " + Zones.inNoPvpZone(player.getLocation()));

		sender.sendMessage("WG_NO_PVP: " + !WorldGuards.canPVP(player.getLocation()));

		// for(Battle battle : Battle.Util.getAllActive())
		// battle.end();

		return true;
	}

	private static int thisTask = -1;
	private static List<Schematic> toGen = null;
	private static int taskCount = -1;

	private static boolean test2(CommandSender sender, final String[] args)
	{
		Player player = (Player) sender;

		try
		{
			player.sendMessage(" Here we go! ");

			URL doge = new URL(args[0]);

			BufferedImage veryImage = ImageIO.read(doge);

			// veryImage = Images.getScaledImage(veryImage, 128, 128);

			final Location render = player.getLocation();

			final String playerName = player.getName();

			if(player.isOp())
			{
				final int theTask = Images.convertImageToSchematic(Demigods.PLUGIN, veryImage, 1356);

				thisTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Demigods.PLUGIN, new Runnable()
				{

					@Override
					public void run()
					{
						if(taskCount != -1 && toGen != null)
						{
							toGen.get(taskCount).generate(render);
							Messages.info(" GENERATING: " + taskCount + " / " + toGen.size() + " left!");
							taskCount--;
							if(taskCount == -1)
							{
								toGen = null;
								OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
								if(player.isOnline()) player.getPlayer().sendMessage(" Done! ");
								Bukkit.getScheduler().cancelTask(thisTask);
							}
						}
						else if(Images.getConvertedSchematics(theTask) != null)
						{
							Messages.info(" FOUND: Begin generation sequence! ");
							toGen = Images.getConvertedSchematics(theTask);
							taskCount = toGen.size() - 1;
							Images.removeSchematicList(theTask);
						}
					}
				}, 40, 10);
			}

			player.sendMessage(" Hold on... ");
		}
		catch(Throwable suchError)
		{
			player.sendMessage(ChatColor.RED + "many problems. " + suchError.getMessage());
			suchError.printStackTrace();
		}

		return true;

		// Player player = (Player) sender;

		// StructureData obelisk = Structure.Util.getInRadiusWithFlag(player.getLocation(), Structure.Flag.NO_GRIEFING);
		// if(obelisk != null)
		// {
		// Get all of the connected obelisks
		// for(StructureData save : Structure.Util.getStructureWeb(obelisk, Structure.Flag.NO_GRIEFING, 20))
		// {
		// if(save == obelisk) continue;
		// player.sendMessage(save.getId().toString());
		// }
		// }
		// else player.sendMessage(ChatColor.RED + "No Obelisk found.");

		// return true;

		// Player player = (Player) sender;

		// Messages.broadcast(ChatColor.RED + "Removing all non-altar structures.");

		// for(StructureData save : Collections2.filter(StructureData.Util.loadAll(), new Predicate<StructureData>()
		// {
		// @Override
		// public boolean apply(StructureData structure)
		// {
		// return !structure.getType().equals(GreekStructure.ALTAR);
		// }
		// }))
		// save.remove();

		// Messages.broadcast(ChatColor.RED + "All non-altar structures have been removed.");

		// if(Demigods.ERROR_NOISE) Errors.triggerError(ChatColor.GREEN + player.getName(), new ColoredStringBuilder().gray(" " + Unicodes.getRightwardArrow() + " ").red("Test error.").build());

		// return true;
	}

	private static boolean test3(CommandSender sender, final String[] args)
	{
		Player player = (Player) sender;

		return true;
	}

	/**
	 * Temp command while testing obelisks.
	 */
	private static boolean obelisk(CommandSender sender, final String[] args)
	{
		Player player = (Player) sender;

		if(args.length != 3)
		{
			player.sendMessage(ChatColor.RED + "Not enough arguments.");
			return false;
		}

		StructureData obelisk = Iterables.getFirst(Structure.Util.getInRadiusWithFlag(player.getLocation(), Structure.Flag.NO_GRIEFING), null);
		if(obelisk != null)
		{
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
			if(!obelisk.getOwner().equals(character.getId()))
			{
				player.sendMessage(ChatColor.RED + "You don't control this Obelisk.");
				return true;
			}

			DCharacter workWith = obeliskGetCharacter(args[1], args[2]);

			if(workWith == null)
			{
				player.sendMessage(ChatColor.RED + "Character/Player (" + args[2] + ") not found.");
				return true;
			}

			if(!DCharacter.Util.areAllied(workWith, character))
			{
				player.sendMessage(ChatColor.RED + "You are not allied with " + workWith.getDeity().getColor() + character.getName() + ChatColor.RED + ".");
				return true;
			}

			if(args[0].equalsIgnoreCase("add"))
			{
				if(!obelisk.getMembers().contains(workWith.getId()))
				{
					obelisk.addMember(workWith.getId());
					player.sendMessage(workWith.getDeity().getColor() + workWith.getName() + ChatColor.YELLOW + " has been added to the Obelisk!");
				}
				else player.sendMessage(ChatColor.RED + "Already a member.");
			}
			else if(args[0].equalsIgnoreCase("remove"))
			{
				if(obelisk.getMembers().contains(workWith.getId()))
				{
					obelisk.removeMember(workWith.getId());
					player.sendMessage(workWith.getDeity().getColor() + workWith.getName() + ChatColor.YELLOW + " has been removed from the Obelisk!");
				}
				else player.sendMessage(ChatColor.RED + "Not a member.");
			}
		}
		else player.sendMessage(ChatColor.RED + "No Obelisk found.");

		return true;
	}

	private static DCharacter obeliskGetCharacter(String type, final String name)
	{
		if(type.equalsIgnoreCase("character")) return DCharacter.Util.getCharacterByName(name);
		if(!type.equalsIgnoreCase("player")) return null;
		try
		{
			return Iterators.find(DataManager.players.values().iterator(), new Predicate<DPlayer>()
			{
				@Override
				public boolean apply(DPlayer dPlayer)
				{
					return dPlayer.getPlayerName().equals(name);
				}
			}).getCurrent();
		}
		catch(NoSuchElementException ignored)
		{}
		return null;
	}
}
