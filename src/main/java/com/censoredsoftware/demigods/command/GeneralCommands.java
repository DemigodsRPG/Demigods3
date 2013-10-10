package com.censoredsoftware.demigods.command;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.battle.Battle;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.deity.Alliance;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.helper.CommandWrapper;
import com.censoredsoftware.demigods.language.Symbol;
import com.censoredsoftware.demigods.language.Translation;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.util.*;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class GeneralCommands extends CommandWrapper
{
	public enum GeneralCommand implements WrappedCommandListItem
	{
		/**
		 * The demigods command, tons of documentation for the entire plugin.
		 */
		DEMIGODS(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return "demigods";
			}

			@Override
			public Set<WrappedSubCommand> getSubCommands()
			{
				return Sets.newHashSet(
				/**
				 * ADMIN
				 */
				new WrappedSubCommand()
				{
					@Override
					public String getName()
					{
						return "admin";
					}

					@Override
					public boolean processSubCommand(CommandSender sender, String[] args)
					{
						return dg_admin(sender, args);
					}
				},
				/**
				 * INFO
				 */
				new WrappedSubCommand()
				{
					@Override
					public String getName()
					{
						return "info";
					}

					@Override
					public boolean processSubCommand(CommandSender sender, String[] args)
					{
						String option1 = null;
						if(args.length >= 2) option1 = args[1];
						if(option1 == null)
						{
							Messages.tagged(sender, "Information Directory");
							sender.sendMessage(ChatColor.GRAY + " /dg info characters");
							sender.sendMessage(ChatColor.GRAY + " /dg info shrines");
							sender.sendMessage(ChatColor.GRAY + " /dg info obelisks");
							sender.sendMessage(ChatColor.GRAY + " /dg info players");
							sender.sendMessage(ChatColor.GRAY + " /dg info pvp");
							sender.sendMessage(ChatColor.GRAY + " /dg info skills");
							sender.sendMessage(ChatColor.GRAY + " /dg info demigods");
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
						else if(option1.equalsIgnoreCase("sender"))
						{
							Messages.tagged(sender, "Players");
							sender.sendMessage(ChatColor.GRAY + " This is some info about Players.");
						}
						else if(option1.equalsIgnoreCase("skills"))
						{
							Messages.tagged(sender, "Skills");
							sender.sendMessage(ChatColor.GRAY + " This is some info about Skills.");
						}
						else if(option1.equalsIgnoreCase("pvp"))
						{
							Messages.tagged(sender, "PVP");
							sender.sendMessage(ChatColor.GRAY + " This is some info about PVP.");
						}
						else if(option1.equalsIgnoreCase("demigods"))
						{
							Messages.tagged(sender, "About the Plugin");
							sender.sendMessage(ChatColor.WHITE + " Not to be confused with other RPG plugins that focus on skills and classes alone, " + ChatColor.GREEN + "Demigods" + ChatColor.WHITE + " adds culture and conflict that will keep players coming back even after they've maxed out their levels and found all of the diamonds in a 50km radius.");
							sender.sendMessage(" ");
							sender.sendMessage(ChatColor.GREEN + " Demigods" + ChatColor.WHITE + " is unique in its system of rewarding players for both adventuring (tributes) and conquering (PvP) with a wide array of fun and useful skills.");
							sender.sendMessage(" ");
							sender.sendMessage(ChatColor.WHITE + " Re-enact mythological battles and rise from a mere sender to a full-fledged Olympian as you form new Alliances with mythical groups and battle to the bitter end.");
							sender.sendMessage(" ");
							sender.sendMessage(ChatColor.GRAY + " Developed by: " + ChatColor.GREEN + "_Alex" + ChatColor.GRAY + " and " + ChatColor.GREEN + "HmmmQuestionMark");
							sender.sendMessage(ChatColor.GRAY + " Website: " + ChatColor.YELLOW + "demigodsrpg.com");
							sender.sendMessage(ChatColor.GRAY + " Source: " + ChatColor.YELLOW + "github.com/CensoredSoftware/Minecraft-Demigods");
						}

						return true;
					}
				});
			}

			@Override
			public boolean shouldContinue(CommandSender sender, String[] args)
			{
				// Check Console TODO Make console commands?
				if(sender instanceof ConsoleCommandSender)
				{
					Messages.noConsole((ConsoleCommandSender) sender);
					return false;
				}

				// Check Permissions
				if(!sender.hasPermission("demigods.basic"))
				{
					Messages.noPermission(sender);
					return false;
				}

				return true;
			}

			@Override
			public boolean processMain(CommandSender sender, String[] args)
			{
				if(args.length > 0)
				{
					// Define args
					String category = args[0];
					String option1 = null;
					if(args.length >= 2) option1 = args[1];

					for(Alliance alliance : Alliance.values())
					{
						if(!sender.hasPermission(alliance.getPermission())) continue;
						if(category.equalsIgnoreCase(alliance.getName()))
						{
							if(args.length < 2)
							{
								Messages.tagged(sender, alliance + " Directory");
								for(Deity deity : Alliance.Util.getLoadedPlayableDeitiesInAlliance(alliance))
									sender.sendMessage(ChatColor.GRAY + " /dg " + alliance.getName().toLowerCase() + " " + deity.getColor() + deity.getName().toLowerCase());
							}
							else
							{
								for(final Deity deity : Alliance.Util.getLoadedMajorPlayableDeitiesInAllianceWithPerms(alliance, sender))
								{
									assert option1 != null;
									if(option1.equalsIgnoreCase(deity.getName()))
									{
										try
										{
											for(String toPrint : new ArrayList<String>()
											{
												{
													addAll(deity.getLore());
													for(Ability ability : deity.getAbilities())
													{
														for(String detail : ability.getDetails())
														{
															StringBuilder details = new StringBuilder(ChatColor.GRAY + "   " + Symbol.RIGHTWARD_ARROW + " ");
															if(ability.getCommand() != null) details.append(ChatColor.GREEN + "/").append(ability.getCommand().toLowerCase()).append(ChatColor.WHITE).append(": ");
															details.append(ChatColor.WHITE).append(detail);
															add(details.toString());
															if(ability.getCost() > 0) add("      " + ChatColor.GRAY + "-" + ChatColor.WHITE + " Favor cost: " + ChatColor.RED + ability.getCost());
														}
													}
													add(" ");
												}
											})
												sender.sendMessage(toPrint);
											return true;
										}
										catch(Exception e)
										{
											e.printStackTrace();
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

				// Main menu
				Messages.tagged(sender, "Documentation");
				for(Alliance alliance : Alliance.values())
				{
					if(!sender.hasPermission(alliance.getPermission())) continue;
					sender.sendMessage(ChatColor.GRAY + " /dg " + alliance.getName().toLowerCase());
				}
				sender.sendMessage(ChatColor.GRAY + " /dg info");
				if(sender.hasPermission("demigods.admin")) sender.sendMessage(ChatColor.RED + " /dg admin");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.WHITE + " Use " + ChatColor.YELLOW + "/check" + ChatColor.WHITE + " to see your player information.");
				return true;
			}
		}),
		/**
		 * The deity command, a shortcut to the current character's deity documentation.
		 */
		DEITY(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return "deity";
			}

			@Override
			public Set<WrappedSubCommand> getSubCommands()
			{
				return null;
			}

			@Override
			public boolean shouldContinue(CommandSender sender, String[] args)
			{
				// Check Console TODO Make console commands?
				if(sender instanceof ConsoleCommandSender)
				{
					Messages.noConsole((ConsoleCommandSender) sender);
					return false;
				}

				// Check Permissions
				if(!sender.hasPermission("demigods.basic"))
				{
					Messages.noPermission(sender);
					return false;
				}

				return true;
			}

			@Override
			public boolean processMain(CommandSender sender, String[] args)
			{
				Player player = (Player) sender;

				if(DPlayer.Util.getPlayer(player).getCurrent() != null && DPlayer.Util.getPlayer(player).getCurrent().isUsable())
				{
					Deity deity = DPlayer.Util.getPlayer(player).getCurrent().getDeity();
					player.chat("/dg " + deity.getAlliance().getName().toLowerCase() + " " + deity.getName().toLowerCase());
					return true;
				}

				player.sendMessage(ChatColor.RED + "This command requires you to have a character.");
				return true;
			}
		}),
		/**
		 * The check command, gives useful stats to players.
		 */
		CHECK(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return "check";
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
					player.sendMessage(ChatColor.RED + "You are nothing but a mortal. You have no worthy statistics.");
					return true;
				}

				// Define variables
				int kills = character.getKillCount();
				int deaths = character.getDeathCount();
				String charName = character.getName();
				String deity = character.getDeity().getName();
				Alliance alliance = character.getAlliance();

				int favor = character.getMeta().getFavor();
				int maxFavor = character.getMeta().getMaxFavor();
				int ascensions = character.getMeta().getAscensions();
				int skillPoints = character.getMeta().getSkillPoints();
				ChatColor deityColor = character.getDeity().getColor();
				ChatColor favorColor = Strings.getColor(character.getMeta().getFavor(), character.getMeta().getMaxFavor());

				// Set player status
				String status = ChatColor.YELLOW + "Ready.";
				if(!character.canPvp()) status = ChatColor.DARK_AQUA + "Safe.";
				else if(Battle.Util.isInBattle(character)) status = ChatColor.GOLD + "In battle.";

				// Send the user their info via chat
				Messages.tagged(sender, "Player Check");

				sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Character: " + deityColor + charName);
				sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Deity: " + deityColor + deity + ChatColor.WHITE + " of the " + ChatColor.GOLD + alliance.getName() + "s");
				sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Favor: " + favorColor + favor + ChatColor.GRAY + " (of " + ChatColor.GREEN + maxFavor + ChatColor.GRAY + ")");
				sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Ascensions: " + ChatColor.GREEN + ascensions);
				sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Available Skill Points: " + ChatColor.GREEN + skillPoints);
				sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Kills: " + ChatColor.GREEN + kills + ChatColor.WHITE + " / Deaths: " + ChatColor.RED + deaths);
				sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Status: " + status);

				return true;
			}
		}), OWNER(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return "owner";
			}

			@Override
			public Set<WrappedSubCommand> getSubCommands()
			{
				return null;
			}

			@Override
			public boolean processMain(CommandSender sender, String[] args)
			{
				// Check permissions
				if(!sender.hasPermission("demigods.basic")) return Messages.noPermission(sender);

				Player player = (Player) sender;
				if(args.length < 1)
				{
					player.sendMessage(ChatColor.RED + "You must select a character.");
					player.sendMessage(ChatColor.RED + "/owner <character>");
					return true;
				}
				DCharacter charToCheck = DCharacter.Util.getCharacterByName(args[0]);
				if(charToCheck == null) player.sendMessage(ChatColor.RED + "That character doesn't exist.");
				else player.sendMessage(charToCheck.getDeity().getColor() + charToCheck.getName() + ChatColor.YELLOW + " belongs to " + charToCheck.getOfflinePlayer().getName() + ".");
				return true;
			}
		}),
		/**
		 * The alliance command, allows players to send messages to all other online players in their alliance.
		 */
		ALLIANCE(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return "name";
			}

			@Override
			public Set<WrappedSubCommand> getSubCommands()
			{
				return null;
			}

			@Override
			public boolean processMain(CommandSender sender, String[] args)
			{
				// Check permissions
				if(!sender.hasPermission("demigods.basic")) return Messages.noPermission(sender);

				Player player = (Player) sender;
				if(args.length < 1) return false;

				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
				if(character == null)
				{
					player.sendMessage(Demigods.LANGUAGE.getText(Translation.Text.DISABLED_MORTAL));
					return true;
				}
				else character.chatWithAlliance(Joiner.on(" ").join(args));
				return true;
			}
		}),
		/**
		 * The binds command, shows a player what abilties are bound to certain items.
		 */
		BINDS(new WrappedCommand()
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
				// Check permissions
				if(!sender.hasPermission("demigods.basic")) return Messages.noPermission(sender);

				// Define variables
				Player player = (Player) sender;
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				if(character != null && !character.getMeta().getBinds().isEmpty())
				{
					player.sendMessage(ChatColor.YELLOW + Titles.chatTitle("Currently Bound Abilities"));
					player.sendMessage(" ");

					// Get the binds and display info
					for(Map.Entry<String, Object> entry : character.getMeta().getBinds().entrySet())
						player.sendMessage(ChatColor.GREEN + "    " + StringUtils.capitalize(entry.getKey().toLowerCase()) + ChatColor.GRAY + " is bound to " + (Strings.beginsWithVowel(entry.getValue().toString()) ? "an " : "a ") + ChatColor.ITALIC + Strings.beautify(entry.getValue().toString()).toLowerCase() + ChatColor.GRAY + ".");

					player.sendMessage(" ");
				}
				else player.sendMessage(ChatColor.RED + "You currently have no ability binds.");

				return true;
			}
		}),
		/**
		 * The leaderboard command, shows the ranks of the top 15 players based on kills and deaths.
		 */
		LEADERBOARD(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return "leaderboard";
			}

			@Override
			public Set<WrappedSubCommand> getSubCommands()
			{
				return null;
			}

			@Override
			public boolean processMain(CommandSender sender, String[] args)
			{
				// Define variables
				List<DCharacter> characters = Lists.newArrayList(DCharacter.Util.getAllUsable());
				Map<UUID, Double> scores = Maps.newLinkedHashMap();
				for(int i = 0; i < characters.size(); i++)
				{
					DCharacter character = characters.get(i);
					double score = character.getKillCount() - character.getDeathCount();
					if(score > 0) scores.put(character.getId(), score);
				}

				// Sort rankings
				scores = Maps2.sortByValue(scores, false);

				// Print info
				Messages.tagged(sender, "Leaderboard");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.GRAY + "    Rankings are determined by kills and deaths.");
				sender.sendMessage(" ");

				int length = characters.size() > 15 ? 16 : characters.size() + 1;
				List<Map.Entry<UUID, Double>> list = Lists.newArrayList(scores.entrySet());
				int count = 0;

				for(int i = list.size() - 1; i >= 0; i--)
				{
					count++;
					Map.Entry<UUID, Double> entry = list.get(i);

					if(count >= length) break;
					DCharacter character = DCharacter.Util.load(entry.getKey());
					sender.sendMessage(ChatColor.GRAY + "    " + ChatColor.RESET + count + ". " + character.getDeity().getColor() + character.getName() + ChatColor.RESET + ChatColor.GRAY + " (" + character.getPlayerName() + ") " + ChatColor.RESET + "Kills: " + ChatColor.GREEN + character.getKillCount() + ChatColor.WHITE + " / Deaths: " + ChatColor.RED + character.getDeathCount());
				}

				sender.sendMessage(" ");
				return true;
			}
		}),
		/**
		 * The values command, tells a player what the tribute value of the item they are holding is.
		 */
		VALUES(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return "values";
			}

			@Override
			public Set<WrappedSubCommand> getSubCommands()
			{
				return null;
			}

			@Override
			public boolean processMain(CommandSender sender, String[] args)
			{
				// Define variables
				Player player = (Player) sender;
				int count = 0;

				// Send header
				Messages.tagged(sender, "Current High Value Tributes");
				sender.sendMessage(" ");

				for(Map.Entry<Material, Integer> entry : Maps2.sortByValue(ItemValues.getTributeValuesMap(), true).entrySet())
				{
					// Handle count
					if(count >= 10) break;
					count++;

					// Display value
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.YELLOW + Strings.beautify(entry.getKey().name()) + ChatColor.GRAY + " (currently worth " + ChatColor.GREEN + entry.getValue() + ChatColor.GRAY + " per item)");
				}

				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.ITALIC + "Values are constantly changing based on how players");
				sender.sendMessage(ChatColor.ITALIC + "tribute, so check back often!");

				if(player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR))
				{
					sender.sendMessage(" ");
					sender.sendMessage(ChatColor.GRAY + "The items in your hand are worth " + ChatColor.GREEN + ItemValues.getValue(player.getItemInHand()) + ChatColor.GRAY + ".");
				}

				return true;
			}
		}),
		/**
		 * The names command, tells a player the real names, current nick names, and alliances.
		 */
		NAMES(new WrappedCommand()
		{
			@Override
			public String getName()
			{
				return "names";
			}

			@Override
			public Set<WrappedSubCommand> getSubCommands()
			{
				return null;
			}

			@Override
			public boolean processMain(CommandSender sender, String[] args)
			{
				// Print info
				Messages.tagged(sender, "Online Player Names");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.GRAY + "    " + ChatColor.UNDERLINE + "Immortals:");
				sender.sendMessage(" ");

				// Characters
				for(DCharacter character : DCharacter.Util.getOnlineCharacters())
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + character.getDeity().getColor() + character.getName() + ChatColor.GRAY + " is owned by " + ChatColor.WHITE + character.getPlayerName() + ChatColor.GRAY + ".");

				sender.sendMessage(" ");

				Set<Player> mortals = DPlayer.Util.getOnlineMortals();

				if(mortals.isEmpty()) return true;

				sender.sendMessage(ChatColor.GRAY + "    " + ChatColor.UNDERLINE + "Mortals:");
				sender.sendMessage(" ");

				// Mortals
				for(Player mortal : mortals)
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.WHITE + mortal.getDisplayName() + ".");

				sender.sendMessage(" ");

				return true;
			}
		});

		private WrappedCommand command;

		private GeneralCommand(WrappedCommand command)
		{
			this.command = command;
		}

		public WrappedCommand getCommand()
		{
			return command;
		}
	}

	public GeneralCommands()
	{
		super(false, GeneralCommand.values());
	}

	private static boolean dg_admin(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;

		if(!sender.hasPermission("demigods.admin")) return Messages.noPermission(sender);

		// Display main admin options menu
		if(args.length < 2)
		{
			Messages.tagged(sender, "Admin Directory");
			sender.sendMessage(ChatColor.GRAY + " /dg admin wand");
			sender.sendMessage(ChatColor.GRAY + " /dg admin debug");
			for(AdminCommands command : AdminCommands.values())
			{
				sender.sendMessage(ChatColor.GRAY + " " + command.getCommand().getName());
			}
			sender.sendMessage(ChatColor.DARK_RED + " /dg admin clear data yesdoitforsurepermanently");
			return true;
		}

		// Handle automatic commands
		for(AdminCommands command : AdminCommands.values())
		{
			if(args[1].equalsIgnoreCase(command.getCommand().getRootCommand())) return command.getCommand().doCommand(player, args);
		}

		// Handle manual commands
		if(args[1].equalsIgnoreCase("wand"))
		{
			if(!Admins.wandEnabled(player))
			{
				DataManager.saveTemp(sender.getName(), "temp_admin_wand", true);
				sender.sendMessage(ChatColor.RED + "Your admin wand has been enabled for " + Material.getMaterial(Configs.getSettingInt("admin.wand_tool")));
			}
			else if(Admins.wandEnabled(player))
			{
				DataManager.removeTemp(sender.getName(), "temp_admin_wand");
				sender.sendMessage(ChatColor.RED + "You have disabled your admin wand.");
			}
			return true;
		}
		else if(args[1].equalsIgnoreCase("debug"))
		{
			if(!DataManager.hasKeyTemp(sender.getName(), "temp_admin_debug") || !Boolean.parseBoolean(DataManager.getValueTemp(sender.getName(), "temp_admin_debug").toString()))
			{
				DataManager.saveTemp(sender.getName(), "temp_admin_debug", true);
				sender.sendMessage(ChatColor.RED + "You have enabled debugging.");
			}
			else if(DataManager.hasKeyTemp(sender.getName(), "temp_admin_debug") && Boolean.parseBoolean(DataManager.getValueTemp(sender.getName(), "temp_admin_debug").toString()))
			{
				DataManager.removeTemp(sender.getName(), "temp_admin_debug");
				sender.sendMessage(ChatColor.RED + "You have disabled debugging.");
			}
		}
		else if(args[1].equalsIgnoreCase("clear") && args[1].equalsIgnoreCase("data") && args[2].equalsIgnoreCase("yesdoitforsurepermanently"))
		{
			sender.sendMessage(ChatColor.RED + Demigods.LANGUAGE.getText(Translation.Text.ADMIN_CLEAR_DATA_STARTING));
			DataManager.flushData();
			sender.sendMessage(ChatColor.GREEN + Demigods.LANGUAGE.getText(Translation.Text.ADMIN_CLEAR_DATA_FINISHED));
			return true;
		}

		return true;
	}

	static class doCheck implements AdminCommand
	{
		@Override
		public String getName()
		{
			return "/dg admin check [player|character] <name>";
		}

		@Override
		public String getRootCommand()
		{
			return "check";
		}

		@Override
		public boolean doCommand(Player sender, String[] args)
		{
			if(args.length < 4)
			{
				// Not enough parameters, return
				sender.sendMessage(ChatColor.RED + "You didn't specify enough parameters.");
				return true;
			}

			if(args[2].equalsIgnoreCase("player"))
			{
				// Define the player
				DPlayer player = DPlayer.Util.getPlayer(Bukkit.getOfflinePlayer(args[3]));

				// Display the information
				if(player != null)
				{
					Messages.tagged(sender, "Player Information");
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Name: " + player.getPlayerName());
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Last Login: " + Times.getTimeTagged(player.getLastLoginTime(), true) + " ago");
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Last Logout: " + Times.getTimeTagged(player.getLastLogoutTime(), true) + " ago");
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Can PvP? " + (player.canPvp() ? ChatColor.GREEN : ChatColor.RED) + Strings.beautify("" + player.canPvp()));
					if(player.hasCurrent()) sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Current Character: " + player.getCurrent().getDeity().getColor() + player.getCurrent().getName());
					if(player.getCharacters() != null && !player.getCharacters().isEmpty())
					{
						sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Characters:");
						for(DCharacter character : player.getCharacters())
							sender.sendMessage(ChatColor.GRAY + "   - " + ChatColor.WHITE + character.getName() + ChatColor.RESET + " (" + character.getDeity().getColor() + character.getDeity().getName() + ChatColor.RESET + ")");
					}
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "No player found with that name.");
				}
			}
			else if(args[2].equalsIgnoreCase("character"))
			{
				// Define the character
				DCharacter character = DCharacter.Util.getCharacterByName(args[3]);

				// Display the information
				if(character != null)
				{
					Messages.tagged(sender, "Character Information");
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Name: " + character.getDeity().getColor() + character.getName());
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Deity: " + character.getDeity().getColor() + character.getDeity().getName());
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Alliance: " + ChatColor.WHITE + character.getAlliance());
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Favor: " + Strings.getColor(character.getMeta().getFavor(), character.getMeta().getMaxFavor()) + character.getMeta().getFavor() + ChatColor.WHITE + " (of " + ChatColor.GREEN + character.getMeta().getMaxFavor() + ChatColor.WHITE + ")");
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Ascensions: " + ChatColor.GREEN + character.getMeta().getAscensions());
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Available Skill Points: " + ChatColor.GREEN + character.getMeta().getSkillPoints());
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Kills: " + ChatColor.GREEN + character.getKillCount() + ChatColor.WHITE + " / Deaths: " + ChatColor.RED + character.getDeathCount());
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Owner: " + ChatColor.WHITE + character.getPlayerName() + " (" + (character.getOfflinePlayer().isOnline() ? ChatColor.GREEN + "online" : ChatColor.RED + "offline") + ChatColor.WHITE + ")");
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Active? " + (character.isActive() ? ChatColor.GREEN : ChatColor.RED) + Strings.beautify("" + character.isActive()));
					sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Usable? " + (character.isUsable() ? ChatColor.GREEN : ChatColor.RED) + Strings.beautify("" + character.isUsable()));
					sender.sendMessage(" ");
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "No character found with that name.");
				}
			}

			return true;
		}
	}

	static class doRemove implements AdminCommand
	{
		@Override
		public String getName()
		{
			return "/dg admin remove [player|character] <name>";
		}

		@Override
		public String getRootCommand()
		{
			return "remove";
		}

		@Override
		public boolean doCommand(Player sender, String[] args)
		{
			if(args.length < 4)
			{
				// Not enough parameters, return
				sender.sendMessage(ChatColor.RED + "You didn't specify enough parameters.");
				return true;
			}

			if(args[2].equalsIgnoreCase("player"))
			{
				// Define the player
				DPlayer player = DPlayer.Util.getPlayer(Bukkit.getOfflinePlayer(args[3]));

				// Remove their data if not null
				if(player != null)
				{
					// Remove them
					player.remove();

					// Send success message
					sender.sendMessage(ChatColor.RED + player.getPlayerName() + " has been removed successfully!");
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "No player found with that name.");
				}
			}
			else if(args[2].equalsIgnoreCase("character"))
			{
				// Define the character
				DCharacter character = DCharacter.Util.getCharacterByName(args[3]);

				// Remove their data if not null
				if(character != null)
				{
					// Remove them
					character.remove();

					// Send success message
					sender.sendMessage(ChatColor.RED + character.getName() + " has been removed successfully!");
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "No player found with that name.");
				}
			}

			return true;
		}
	}

	static class doSet implements AdminCommand
	{
		@Override
		public String getName()
		{
			return "/dg admin set [fav|maxfav|sp] <character> <amt>";
		}

		@Override
		public String getRootCommand()
		{
			return "set";
		}

		@Override
		public boolean doCommand(Player sender, String[] args)
		{
			if(args.length < 5)
			{
				// Not enough parameters, return
				sender.sendMessage(ChatColor.RED + "You didn't specify enough parameters.");
				return true;
			}

			DCharacter character = DCharacter.Util.getCharacterByName(args[3]);
			int amount = Integer.parseInt(args[4]);
			String updatedValue = null;

			if(character != null)
			{
				Player owner = character.getOfflinePlayer().getPlayer();

				if(args[2].equalsIgnoreCase("fav"))
				{
					// Update the amount
					character.getMeta().setFavor(amount);

					// Set what was updated
					updatedValue = "favor";
				}
				else if(args[2].equalsIgnoreCase("maxfav"))
				{
					// Update the amount
					character.getMeta().setMaxFavor(amount);

					// Set what was updated
					updatedValue = "max favor";
				}
				else if(args[2].equalsIgnoreCase("sp"))
				{
					// Update the amount
					character.getMeta().setSkillPoints(amount);

					// Set what was updated
					updatedValue = "skill points";
				}
				else
				{
					// Nothing was edited
					owner.sendMessage(ChatColor.RED + "Invalid value to update specified.");
				}

				// Message the administrator to confirm
				sender.sendMessage(ChatColor.GREEN + Strings.beautify(updatedValue) + " updated for " + character.getName() + ".");

				// Message the edited player
				if(character.getOfflinePlayer().isOnline())
				{
					owner.sendMessage(ChatColor.GREEN + "Your character's (" + character.getName() + ") " + updatedValue + " has been set to " + amount + ".");
					owner.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "No character could be found.");
			}

			return true;
		}
	}

	static class doAdd implements AdminCommand
	{
		@Override
		public String getName()
		{
			return "/dg admin add [fav|maxfav|sp] <character> <amt>";
		}

		@Override
		public String getRootCommand()
		{
			return "add";
		}

		@Override
		public boolean doCommand(Player sender, String[] args)
		{
			if(args.length < 5)
			{
				// Not enough parameters, return
				sender.sendMessage(ChatColor.RED + "You didn't specify enough parameters.");
				return true;
			}

			DCharacter character = DCharacter.Util.getCharacterByName(args[3]);
			int amount = Integer.parseInt(args[4]);
			String updatedValue = null;

			if(character != null)
			{
				Player owner = character.getOfflinePlayer().getPlayer();

				if(args[2].equalsIgnoreCase("fav"))
				{
					// Update the amount
					character.getMeta().addFavor(amount);

					// Set what was updated
					updatedValue = "favor";
				}
				else if(args[2].equalsIgnoreCase("maxfav"))
				{
					// Update the amount
					character.getMeta().addMaxFavor(amount);

					// Set what was updated
					updatedValue = "max favor";
				}
				else if(args[2].equalsIgnoreCase("sp"))
				{
					// Update the amount
					character.getMeta().addSkillPoints(amount);

					// Set what was updated
					updatedValue = "skill points";
				}
				else
				{
					// Nothing was edited
					owner.sendMessage(ChatColor.RED + "Invalid value to update specified.");
				}

				// Message the administrator to confirm
				sender.sendMessage(ChatColor.GREEN + Strings.beautify(updatedValue) + " updated for " + character.getName() + ".");

				// Message them
				if(character.getOfflinePlayer().isOnline())
				{
					owner.sendMessage(ChatColor.GREEN + "Your character's (" + character.getName() + ") " + updatedValue + " has been increased to " + amount + ".");
					owner.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "No character could be found.");
			}

			return true;
		}
	}

	static class doSub implements AdminCommand
	{
		@Override
		public String getName()
		{
			return "/dg admin sub [fav|maxfav|sp] <character> <amt>";
		}

		@Override
		public String getRootCommand()
		{
			return "sub";
		}

		@Override
		public boolean doCommand(Player sender, String[] args)
		{
			if(args.length < 5)
			{
				// Not enough parameters, return
				sender.sendMessage(ChatColor.RED + "You didn't specify enough parameters.");
				return true;
			}

			DCharacter character = DCharacter.Util.getCharacterByName(args[3]);
			int amount = Integer.parseInt(args[4]);
			String updatedValue = null;

			if(character != null)
			{
				Player owner = character.getOfflinePlayer().getPlayer();

				if(args[2].equalsIgnoreCase("fav"))
				{
					// Update the amount
					character.getMeta().subtractFavor(amount);

					// Set what was updated
					updatedValue = "favor";
				}
				else if(args[2].equalsIgnoreCase("maxfav"))
				{
					// Update the amount
					character.getMeta().subtractMaxFavor(amount);

					// Set what was updated
					updatedValue = "max favor";
				}
				else if(args[2].equalsIgnoreCase("sp"))
				{
					// Update the amount
					character.getMeta().subtractSkillPoints(amount);

					// Set what was updated
					updatedValue = "skill points";
				}
				else
				{
					// Nothing was edited
					owner.sendMessage(ChatColor.RED + "Invalid value to update specified.");
				}

				// Message the administrator to confirm
				sender.sendMessage(ChatColor.GREEN + Strings.beautify(updatedValue) + " updated for " + character.getName() + ".");

				// Message them
				if(character.getOfflinePlayer().isOnline())
				{
					owner.sendMessage(ChatColor.GREEN + "Your character's (" + character.getName() + ") " + updatedValue + " has been decreased to " + amount + ".");
					owner.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "No character could be found.");
			}

			return true;
		}
	}

	public enum AdminCommands
	{
		CHECK(new doCheck()), REMOVE(new doRemove()), SET(new doSet()), ADD(new doAdd()), SUBTRACT(new doSub());

		private AdminCommand command;

		private AdminCommands(AdminCommand command)
		{
			this.command = command;
		}

		public AdminCommand getCommand()
		{
			return this.command;
		}
	}

	interface AdminCommand
	{
		public String getName();

		public String getRootCommand();

		public boolean doCommand(Player sender, String[] args);
	}
}
