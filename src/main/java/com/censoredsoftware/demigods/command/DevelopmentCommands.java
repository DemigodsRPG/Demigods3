package com.censoredsoftware.demigods.command;

import com.censoredsoftware.demigods.battle.Battle;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.helper.CommandWrapper;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.structure.Structure;
import com.censoredsoftware.demigods.structure.global.Altar;
import com.censoredsoftware.demigods.util.Messages;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.NoSuchElementException;
import java.util.Set;

public class DevelopmentCommands extends CommandWrapper
{
	public DevelopmentCommands()
	{
		super(false, DevelopmentCommand.values());
	}

	public static enum DevelopmentCommand implements WrappedCommandListItem
	{
		/**
		 * TEST1
		 */
		TEST1(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return "test1";
			}

			@Override
			public Set<WrappedSubCommand> getSubCommands()
			{
				return null;
			}

			@Override
			public boolean processMain(CommandSender sender, String[] args)
			{
				Player player = (Player) sender;

				for(Battle battle : Battle.Util.getAllActive())
					battle.end();

				return true;
			}
		}),
		/**
		 * TEST2
		 */
		TEST2(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return null;
			}

			@Override
			public Set<WrappedSubCommand> getSubCommands()
			{
				return null;
			}

			@Override
			public boolean processMain(CommandSender sender, String[] args)
			{
				Player player = (Player) sender;

				Messages.broadcast(ChatColor.RED + "Removing all non-altar structures.");

				for(Structure save : Collections2.filter(Structure.Util.loadAll(), new Predicate<Structure>()
				{
					@Override
					public boolean apply(Structure structure)
					{
						return !structure.getType().equals(Structure.Type.ALTAR);
					}
				}))
					save.remove();

				Messages.broadcast(ChatColor.RED + "All non-altar structures have been removed.");

				return true;
			}
		}),
		/**
		 * TEST3
		 */
		TEST3(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return "test3";
			}

			@Override
			public Set<WrappedSubCommand> getSubCommands()
			{
				return null;
			}

			@Override
			public boolean processMain(CommandSender sender, String[] args)
			{
				Player player = (Player) sender;

				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				if(character == null)
				{
					player.sendMessage(ChatColor.RED + "You are mortal, we do not track mortals.");
					return true;
				}

				player.sendMessage("# of " + character.getAlliance() + "s Online: " + DCharacter.Util.getOnlineCharactersWithAlliance(character.getAlliance()).size());
				player.sendMessage("# of Enemies Online: " + DCharacter.Util.getOnlineCharactersWithoutAlliance(character.getAlliance()).size());

				return true;
			}
		}),
		/**
		 * HSPAWN
		 */
		HSPAWN(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return "hspawn";
			}

			@Override
			public Set<WrappedSubCommand> getSubCommands()
			{
				return null;
			}

			@Override
			public boolean processMain(CommandSender sender, String[] args)
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
		}),
		/**
		 * NEAREST_ALTAR
		 */
		NEAREST_ALTAR(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return "nearestaltar";
			}

			@Override
			public Set<WrappedSubCommand> getSubCommands()
			{
				return null;
			}

			@Override
			public boolean processMain(CommandSender sender, String[] args)
			{
				Player player = (Player) sender;

				if(Altar.Util.isAltarNearby(player.getLocation()))
				{
					Structure save = Altar.Util.getAltarNearby(player.getLocation());
					player.teleport(save.getReferenceLocation().clone().add(2.0, 1.5, 0));
					player.sendMessage(ChatColor.YELLOW + "Nearest Altar found.");
				}
				else player.sendMessage(ChatColor.YELLOW + "There is no alter nearby.");

				return true;
			}
		}),
		/**
		 * OBELISK
		 */
		OBELISK(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return "obelisk";
			}

			@Override
			public Set<WrappedSubCommand> getSubCommands()
			{
				return Sets.newHashSet(
				/**
				 * ADD
				 */
				new WrappedSubCommand()
				{
					@Override
					public String getName()
					{
						return "add";
					}

					@Override
					public boolean processSubCommand(CommandSender sender, String[] args)
					{
						Player player = (Player) sender;
						Structure obelisk = Structure.Util.getInRadiusWithFlag(player.getLocation(), Structure.Flag.NO_GRIEFING);
						DCharacter workWith = obeliskGetCharacter(args[1], args[2]);
						if(!obelisk.getMembers().contains(workWith.getId()))
						{
							obelisk.addMember(workWith.getId());
							player.sendMessage(workWith.getDeity().getColor() + workWith.getName() + ChatColor.YELLOW + " has been added to the Obelisk!");
						}
						else player.sendMessage(ChatColor.RED + "Already a member.");
						return true;
					}
				},
				/**
				 * REMOVE
				 */
				new WrappedSubCommand()
				{
					@Override
					public String getName()
					{
						return "remove";
					}

					@Override
					public boolean processSubCommand(CommandSender sender, String[] args)
					{
						Player player = (Player) sender;
						Structure obelisk = Structure.Util.getInRadiusWithFlag(player.getLocation(), Structure.Flag.NO_GRIEFING);
						DCharacter workWith = obeliskGetCharacter(args[1], args[2]);
						if(obelisk.getMembers().contains(workWith.getId()))
						{
							obelisk.removeMember(workWith.getId());
							player.sendMessage(workWith.getDeity().getColor() + workWith.getName() + ChatColor.YELLOW + " has been removed from the Obelisk!");
						}
						else player.sendMessage(ChatColor.RED + "Not a member.");
						return true;
					}
				});
			}

			@Override
			public boolean shouldContinue(CommandSender sender, String[] args)
			{
				if(sender instanceof ConsoleCommandSender) return false;

				Player player = (Player) sender;

				if(args.length != 3)
				{
					player.sendMessage(ChatColor.RED + "Not enough arguments.");
					return false;
				}

				Structure obelisk = Structure.Util.getInRadiusWithFlag(player.getLocation(), Structure.Flag.NO_GRIEFING);
				if(obelisk != null)
				{
					DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
					if(!obelisk.getOwner().equals(character.getId()))
					{
						player.sendMessage(ChatColor.RED + "You don't control this Obelisk.");
						return false;
					}

					DCharacter workWith = obeliskGetCharacter(args[1], args[2]);

					if(workWith == null)
					{
						player.sendMessage(ChatColor.RED + "Character/Player (" + args[2] + ") not found.");
						return false;
					}

					if(!DCharacter.Util.areAllied(workWith, character))
					{
						player.sendMessage(ChatColor.RED + "You are not allied with " + workWith.getDeity().getColor() + character.getName() + ChatColor.RED + ".");
						return false;
					}

					if(!obelisk.getOwner().equals(character.getId()))
					{
						player.sendMessage(ChatColor.RED + "You don't control this Obelisk.");
						return true;
					}

					return true;
				}
				player.sendMessage(ChatColor.RED + "No Obelisk found.");
				return false;
			}

			@Override
			public boolean processMain(CommandSender sender, String[] args)
			{
				return true;
			}
		});

		private WrappedCommand command;

		private DevelopmentCommand(WrappedCommand command)
		{
			this.command = command;
		}

		@Override
		public WrappedCommand getCommand()
		{
			return command;
		}
	}

	private static DCharacter obeliskGetCharacter(String type, final String name)
	{
		if(type.equalsIgnoreCase("character")) return DCharacter.Util.getCharacterByName(name);
		if(!type.equalsIgnoreCase("player")) return null;
		try
		{
			return DPlayer.Util.getPlayer(Bukkit.getOfflinePlayer(Iterators.find(DataManager.players.keySet().iterator(), new Predicate<String>()
			{
				@Override
				public boolean apply(String playerName)
				{
					return playerName.equalsIgnoreCase(name);
				}
			}))).getCurrent();
		}
		catch(NoSuchElementException ignored)
		{}
		return null;
	}
}