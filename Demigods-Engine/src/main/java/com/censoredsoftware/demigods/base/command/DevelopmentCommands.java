package com.censoredsoftware.demigods.base.command;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.censoredsoftware.censoredlib.helper.WrappedCommand;
import com.censoredsoftware.censoredlib.schematic.Schematic;
import com.censoredsoftware.censoredlib.util.Images;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.Data;
import com.censoredsoftware.demigods.engine.data.serializable.DCharacter;
import com.censoredsoftware.demigods.engine.data.serializable.DPlayer;
import com.censoredsoftware.demigods.engine.data.serializable.StructureSave;
import com.censoredsoftware.demigods.engine.mythos.Structure;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

public class DevelopmentCommands extends WrappedCommand
{
	public DevelopmentCommands()
	{
		super(DemigodsPlugin.plugin(), false);
	}

	@Override
	public Set<String> getCommandNames()
	{
		return Sets.newHashSet("obelisk", "test1", "test2", "test3");
	}

	@Override
	public boolean processCommand(CommandSender sender, Command command, String[] args)
	{
		if("test1".equalsIgnoreCase(command.getName())) return test1(sender, args);
		if("test2".equalsIgnoreCase(command.getName())) return test2(sender, args);
		if("test3".equalsIgnoreCase(command.getName())) return test3(sender, args);
		return "obelisk".equalsIgnoreCase(command.getName()) && obelisk(sender, args);
	}

	private static boolean test1(CommandSender sender, final String[] args)
	{
		Player player = (Player) sender;

		if(args.length > 0) Data.getWorld(player.getWorld().getName()).setString(player.getName(), args[0]);
		else player.sendMessage(Data.getWorld(player.getWorld().getName()).getString(player.getName()));

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
				final int theTask = Images.convertImageToSchematic(DemigodsPlugin.plugin(), veryImage, 1356);

				thisTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(DemigodsPlugin.plugin(), new Runnable()
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
		catch(Exception suchError)
		{
			player.sendMessage(ChatColor.RED + "many problems. " + suchError.getMessage());
			Messages.logException(suchError);
		}

		return true;
	}

	private static boolean test3(CommandSender sender, final String[] args)
	{
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

		StructureSave obelisk = Iterables.getFirst(Structure.Util.getInRadiusWithFlag(player.getLocation(), Structure.Flag.NO_GRIEFING), null);
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

			if("add".equalsIgnoreCase(args[0]))
			{
				if(!obelisk.getSanctifiers().contains(workWith.getId()))
				{
					obelisk.addSanctifier(workWith.getId());
					player.sendMessage(workWith.getDeity().getColor() + workWith.getName() + ChatColor.YELLOW + " has been added to the Obelisk!");
				}
				else player.sendMessage(ChatColor.RED + "Already a member.");
			}
			else if("remove".equalsIgnoreCase(args[0]))
			{
				if(obelisk.getSanctifiers().contains(workWith.getId()))
				{
					obelisk.removeSanctifier(workWith.getId());
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
		if("character".equalsIgnoreCase(type)) return DCharacter.Util.getCharacterByName(name);
		if(!"player".equalsIgnoreCase(type)) return null;
		try
		{
			return Iterators.find(Data.PLAYER.values().iterator(), new Predicate<DPlayer>()
			{
				@Override
				public boolean apply(DPlayer dPlayer)
				{
					return dPlayer.getPlayerName().equals(name);
				}
			}).getCurrent();
		}
		catch(NoSuchElementException ignored)
		{
			// ignored
		}
		return null;
	}
}
