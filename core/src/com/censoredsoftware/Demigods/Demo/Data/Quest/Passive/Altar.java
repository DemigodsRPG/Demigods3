package com.censoredsoftware.Demigods.Demo.Data.Quest.Passive;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.Demigods.API.*;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.Event.Altar.AltarCreateEvent;
import com.censoredsoftware.Demigods.Engine.Event.Character.CharacterCreateEvent;
import com.censoredsoftware.Demigods.Engine.Event.Character.CharacterSwitchEvent;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Quest.Quest;
import com.censoredsoftware.Demigods.Engine.Quest.Task;
import com.censoredsoftware.Demigods.Engine.Quest.TaskInfo;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedLocation;

public class Altar extends Quest
{

	private static String name = "Altar", permission = "demigods.altar";
	private static List<String> about = new ArrayList<String>()
	{
		{
			add("Altars are the central feature of Demigods 3.");
			add("Right click on the center block to view the Altar menu.");
			add("From there you can create characters, view warps, and much more!");
		}
	}, accepted = new ArrayList<String>()
	{
		{
			add("Accepted.");
		}
	}, complete = new ArrayList<String>()
	{
		{
			add("Complete.");
		}
	}, failed = new ArrayList<String>()
	{
		{
			add("Failed.");
		}
	};
	private static Type type = Type.PASSIVE;
	private static List<Task> tasks = new ArrayList<Task>()
	{
		{
			add(new AltarMenu(name, permission, about, accepted, complete, failed, type));
			add(new AltarGenerate(name, permission, about, accepted, complete, failed, type));
		}
	};

	public Altar()
	{
		super(name, permission, about, accepted, complete, failed, type, tasks);
	}
}

class AltarMenu extends Task
{
	private static String name = "Altar Menu";
	private static int order = 0;
	private static double reward = 0.0, penalty = 0.0;
	private static Listener listener = new Listener()
	{
		@EventHandler(priority = EventPriority.HIGHEST)
		public void altarInteract(PlayerInteractEvent event)
		{
			if(event.getClickedBlock() == null) return;

			// Define variables
			Player player = event.getPlayer();

			// First we check if the player is in an Altar and return if not
			if(BlockAPI.isAltar(event.getClickedBlock().getLocation()))
			{
				// Player is in an altar, let's do this
				if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

				if(event.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE) && !PlayerAPI.isPraying(player))
				{
					if(Demigods.config.getSettingBoolean("zones.use_dynamic_pvp_zones") && ZoneAPI.canTarget(player))
					{
						player.sendMessage(ChatColor.GRAY + "You cannot use an Altar when PvP is still possible.");
						player.sendMessage(ChatColor.GRAY + "Wait a few moments and then try again when it's safe.");
						event.setCancelled(true);
						return;
					}
					PlayerAPI.togglePraying(player, true);

					// First we clear chat
					clearChat(player);

					// Tell nearby players that the user is praying
					for(Entity entity : player.getNearbyEntities(16, 16, 16))
					{
						if(entity instanceof Player) ((Player) entity).sendMessage(ChatColor.AQUA + player.getName() + " has knelt at a nearby Altar.");
					}

					player.sendMessage(ChatColor.AQUA + " -- Prayer Menu --------------------------------------");

					altarMenu(player);

					// If they are in the process of creating a character we'll just skip them to the confirm screen
					if(DemigodsData.tempPlayerData.containsKey(player, "temp_createchar_finalstep") && DemigodsData.tempPlayerData.getDataBool(player, "temp_createchar_finalstep"))
					{
						clearChat(player);
						finalConfirmDeity(player);
					}

					event.setCancelled(true);
				}
				else if(event.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE) && PlayerAPI.isPraying(player))
				{
					PlayerAPI.togglePraying(player, false);

					// Clear whatever is being worked on in this Pray session
					DemigodsData.tempPlayerData.removeData(player, "temp_createchar");

					player.sendMessage(ChatColor.AQUA + "You are no longer praying.");
					player.sendMessage(ChatColor.GRAY + "Your movement and chat have been re-enabled.");
					event.setCancelled(true);
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void altarChatEvent(AsyncPlayerChatEvent event)
		{
			// Define variables
			Player player = event.getPlayer();
			Location location = player.getLocation();

			// First we check if the player is in/time an Altar and currently praying, if not we'll return
			if(ZoneAPI.zoneAltar(location) != null && PlayerAPI.isPraying(player))
			{
				// Cancel their chat
				event.setCancelled(true);

				// Define variables
				String message = event.getMessage();

				// Return to main menu
				if(message.equalsIgnoreCase("x") || message.startsWith("abort") || message.equalsIgnoreCase("menu") || message.equalsIgnoreCase("exit"))
				{
					// Remove now useless data
					DemigodsData.tempPlayerData.removeData(player, "temp_createchar");

					clearChat(player);

					player.sendMessage(ChatColor.YELLOW + " -> Main Menu ----------------------------------------");
					player.sendMessage(" ");

					altarMenu(player);
					return;
				}

				// Create Character
				if(message.equals("1") || message.contains("create") && message.contains("character"))
				{
					clearChat(player);

					player.sendMessage(ChatColor.YELLOW + " -> Creating Character --------------------------------");
					player.sendMessage(" ");

					chooseName(player);
					return;
				}

				/*
				 * Character creation sub-steps
				 */
				if(DemigodsData.tempPlayerData.containsKey(player, "temp_createchar"))
				{
					// Step 1 of character creation
					if(DemigodsData.tempPlayerData.getDataString(player, "temp_createchar").equals("choose_name"))
					{
						confirmName(player, message);
						return;
					}

					// Step 2 of character creation
					if(DemigodsData.tempPlayerData.getDataString(player, "temp_createchar").equals("confirm_name"))
					{
						if(message.equalsIgnoreCase("y") || message.contains("yes"))
						{
							chooseDeity(player);
							return;
						}
						else
						{
							chooseName(player);
							return;
						}
					}

					// Step 3 of character creation
					if(DemigodsData.tempPlayerData.getDataString(player, "temp_createchar").equals("choose_deity"))
					{
						confirmDeity(player, message);
						return;
					}

					// Step 4 of character creation
					if(DemigodsData.tempPlayerData.getDataString(player, "temp_createchar").equals("confirm_deity"))
					{
						if(message.equalsIgnoreCase("y") || message.contains("yes"))
						{
							deityConfirmed(player);
							return;
						}
						else
						{
							chooseDeity(player);
							return;
						}
					}

					// Step 5 of character creation
					if(DemigodsData.tempPlayerData.getDataString(player, "temp_createchar").equals("confirm_all"))
					{
						if(message.equalsIgnoreCase("y") || message.contains("yes"))
						{
							Inventory ii = Bukkit.getServer().createInventory(player, 27, "Place Your Tributes Here");
							player.openInventory(ii);
						}
						else
						{
							clearChat(player);
							player.sendMessage(ChatColor.YELLOW + " -> Main Menu ----------------------------------------");
							player.sendMessage(" ");
							altarMenu(player);
							return;
						}
					}
				}

				// View Characters
				else if(message.equals("2") || message.startsWith("view") && message.contains("characters"))
				{
					clearChat(player);

					player.sendMessage(ChatColor.YELLOW + " -> Viewing Characters --------------------------------");
					player.sendMessage(" ");

					viewChars(player);
				}
				// View Warps
				else if(message.equals("3") || message.startsWith("view") && message.contains("warps"))
				{
					if(PlayerAPI.getCurrentChar(player) == null) return;

					clearChat(player);

					player.sendMessage(ChatColor.YELLOW + " -> Viewing Warps --------------------------------");
					player.sendMessage(" ");

					viewWarps(player);
				}
				// View Characters
				else if(message.equals("4") || message.startsWith("view") && message.contains("invites"))
				{
					if(PlayerAPI.getCurrentChar(player) == null || !LocationAPI.hasInvites(PlayerAPI.getCurrentChar(player))) return;

					clearChat(player);

					player.sendMessage(ChatColor.YELLOW + " -> Viewing Invites --------------------------------");
					player.sendMessage(" ");

					viewInvites(player);
				}
				else if(message.startsWith("info"))
				{
					clearChat(player);

					// Define variables
					String charName = message.replace(" info", "").trim();
					PlayerCharacter character = CharacterAPI.getCharByName(charName);

					viewChar(player, character);
				}

				// Switch Character
				else if(message.startsWith("switch to"))
				{
					clearChat(player);

					// Define variables
					String charName = message.replace("switch to ", "").trim();

					switchChar(player, charName);
				}

				// Warp Name
				else if(message.startsWith("name warp"))
				{
					// Define variables
					String name = message.replace("name warp", "").trim();

					nameAltar(player, name);
				}

				// Warp Invite
				else if(message.startsWith("invite"))
				{
					// Define variables
					String name = message.replace("invite", "").trim();

					inviteWarp(player, name);
				}

				// Invite Accept
				else if(message.startsWith("accept invite"))
				{
					// Define variables
					String name = message.replace("accept invite", "").trim();

					acceptInvite(player, name);
				}

				// Warp Character
				else if(message.startsWith("warp to"))
				{
					// Define variables
					String warpName = message.replace("warp to ", "").trim();

					AltarMenu.warpChar(player, warpName);
				}
			}
		}

		@SuppressWarnings("unchecked")
		@EventHandler(priority = EventPriority.MONITOR)
		public void createCharacter(InventoryCloseEvent event)
		{
			try
			{
				if(!(event.getPlayer() instanceof Player)) return;
				Player player = (Player) event.getPlayer();

				// If it isn't a confirmation chest then exit
				if(!event.getInventory().getName().contains("Place Your Tributes Here")) return;

				// Exit if this isn't for character creation
				if(!PlayerAPI.isPraying(player) || !DemigodsData.tempPlayerData.containsKey(player, "temp_createchar_finalstep") || !DemigodsData.tempPlayerData.getDataBool(player, "temp_createchar_finalstep"))
				{
					player.sendMessage(ChatColor.RED + "(ERR: 2003) Please report this to an admin immediately:"); // TODO It should be more clear that this is a Demigods related error.
					if(!PlayerAPI.isPraying(player)) player.sendMessage(ChatColor.RED + "    Not praying."); // TODO This event fires twice in newer version of bukkit, ignore the error message for now.
					else if(!DemigodsData.tempPlayerData.containsKey(player, "temp_createchar_finalstep")) player.sendMessage(ChatColor.RED + "    Missing data.");
					else player.sendMessage(ChatColor.RED + "    Boolean set to false;");
					return;
				}

				// Define variables
				String chosenName = DemigodsData.tempPlayerData.getDataString(player, "temp_createchar_name");
				String chosenDeity = DemigodsData.tempPlayerData.getDataString(player, "temp_createchar_deity");
				String deityAlliance = DemigodsData.capitalize(DeityAPI.getDeity(chosenDeity).getInfo().getAlliance());

				// Check the chest items
				int items = 0;
				int neededItems = DeityAPI.getDeity(chosenDeity).getInfo().getClaimItems().size();

				for(ItemStack ii : event.getInventory().getContents())
				{
					if(ii != null)
					{
						for(Material item : DeityAPI.getDeity(chosenDeity).getInfo().getClaimItems())
						{
							if(ii.getType().equals(item))
							{
								items++;
							}
						}
					}
				}

				player.sendMessage(ChatColor.YELLOW + "The " + deityAlliance + "s are pondering your offerings...");
				if(neededItems == items)
				{
					// They were accepted, finish everything up!
					CharacterCreateEvent characterEvent = new CharacterCreateEvent(player, chosenName, chosenDeity);
					Bukkit.getServer().getPluginManager().callEvent(characterEvent);

					// Stop their praying, enable movement, enable chat
					PlayerAPI.togglePraying(player, false);

					// Remove old data now
					DemigodsData.tempPlayerData.removeData(player, "temp_createchar_finalstep");
					DemigodsData.tempPlayerData.removeData(player, "temp_createchar_name");
					DemigodsData.tempPlayerData.removeData(player, "temp_createchar_deity");
				}
				else
				{
					player.sendMessage(ChatColor.RED + "You have been denied entry into the lineage of " + chosenDeity + "!");
				}

				// Clear the confirmation case
				event.getInventory().clear();
			}
			catch(Exception e)
			{
				// Print error for debugging
				e.printStackTrace();
			}
		}
	};

	public AltarMenu(String quest, String permission, List<String> about, List<String> accepted, List<String> complete, List<String> failed, Quest.Type type)
	{
		super(new TaskInfo(name, quest, permission, order, reward, penalty, about, accepted, complete, failed, type, TaskInfo.Subtype.TECHNICAL), listener);
	}

	// Method for use within Altars
	protected static void altarMenu(Player player)
	{
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GRAY + " While using an Altar you are unable to move or chat.");
		player.sendMessage(ChatColor.GRAY + " You can return to the main menu at anytime by typing \"menu\".");
		player.sendMessage(ChatColor.GRAY + " Right-click the Altar again to stop Praying.");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GRAY + " To begin, choose an option by entering it's number in the chat:");
		player.sendMessage(" ");

		if(DemigodsData.tempPlayerData.containsKey(player, "temp_createchar"))
		{
			player.sendMessage(ChatColor.GRAY + "   [X.] " + ChatColor.RED + "Abort Character Creation");
		}
		else player.sendMessage(ChatColor.GRAY + "   [1.] " + ChatColor.GREEN + "Create New Character");

		player.sendMessage(ChatColor.GRAY + "   [2.] " + ChatColor.YELLOW + "View Characters");

		if(PlayerAPI.getCurrentChar(player) != null)
		{
			player.sendMessage(ChatColor.GRAY + "   [3.] " + ChatColor.BLUE + "View Warps");
			if(LocationAPI.hasInvites(PlayerAPI.getCurrentChar(player))) player.sendMessage(ChatColor.GRAY + "   [4.] " + ChatColor.DARK_PURPLE + "View Invites");
			player.sendMessage(" ");
			player.sendMessage(ChatColor.GRAY + " Type" + ChatColor.YELLOW + " invite <character name> " + ChatColor.GRAY + "to invite another player here.");
		}

		player.sendMessage(" ");
	}

	// View characters
	protected static void viewChars(Player player)
	{
		List<PlayerCharacter> chars = PlayerAPI.getChars(player);
		if(chars.isEmpty())
		{
			player.sendMessage(ChatColor.GRAY + "  You have no characters. Why not go make one?");
			player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " create character" + ChatColor.GRAY + " to do so.");
			player.sendMessage(" ");
			return;
		}

		player.sendMessage(ChatColor.LIGHT_PURPLE + "  Light purple " + ChatColor.GRAY + "represents your current character.");
		player.sendMessage(" ");

		for(PlayerCharacter character : chars)
		{
			String color = "";
			String name = character.getName();
			Deity deity = character.getDeity();
			int favor = character.getFavor();
			int maxFavor = character.getMaxFavor();
			ChatColor favorColor = character.getFavorColor();
			int ascensions = character.getAscensions();

			if(character.isActive()) color = ChatColor.LIGHT_PURPLE + "";

			player.sendMessage(ChatColor.GRAY + "  " + ChatColor.GRAY + color + name + ChatColor.GRAY + " [" + deity.getInfo().getColor() + deity + ChatColor.GRAY + " / Fav: " + favorColor + favor + ChatColor.GRAY + " (of " + ChatColor.GREEN + maxFavor + ChatColor.GRAY + ") / Asc: " + ChatColor.GREEN + ascensions + ChatColor.GRAY + "]");
		}

		player.sendMessage(" ");

		player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " <character name> info" + ChatColor.GRAY + " for detailed information. ");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " switch to <character name> " + ChatColor.GRAY + "to change your current");
		player.sendMessage(ChatColor.GRAY + "  character.");
		player.sendMessage(" ");
	}

	// View warps
	protected static void viewWarps(Player player)
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);
		if(character.getWarps().isEmpty())
		{
			player.sendMessage(ChatColor.GRAY + "  You have no Altar warps. Why not go make one?");
			player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " name warp <warp name>" + ChatColor.GRAY + " to name a warp here.");
			player.sendMessage(" ");
			return;
		}

		player.sendMessage(ChatColor.LIGHT_PURPLE + "  Light purple " + ChatColor.GRAY + "represents the closest warp.");
		player.sendMessage(" ");
		boolean hasWarp = false;

		for(TrackedLocation warp : character.getWarps())
		{
			String color = "";
			String name = warp.getName();
			int X = (int) warp.toLocation().getX();
			int Y = (int) warp.toLocation().getY();
			int Z = (int) warp.toLocation().getZ();
			String world = warp.toLocation().getWorld().getName().toUpperCase();

			if(ZoneAPI.zoneAltar(warp.toLocation()) == ZoneAPI.zoneAltar(player.getLocation()))
			{
				color = ChatColor.LIGHT_PURPLE + "";
				hasWarp = true;
			}

			player.sendMessage("  " + color + name + ChatColor.GRAY + " [" + "X: " + ChatColor.GREEN + X + ChatColor.GRAY + " / Y: " + ChatColor.GREEN + Y + ChatColor.GRAY + " / Z: " + ChatColor.GREEN + Z + ChatColor.GRAY + " / World: " + ChatColor.GREEN + world + ChatColor.GRAY + "]");
		}

		player.sendMessage(" ");

		player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " warp to <warp name> " + ChatColor.GRAY + "to warp.");
		if(!hasWarp) player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " name warp <warp name>" + ChatColor.GRAY + " to name a warp here.");
		else player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " name warp <warp name>" + ChatColor.GRAY + " to rename this warp.");
		player.sendMessage(" ");
	}

	// View warps
	protected static void viewInvites(Player player)
	{
		for(TrackedLocation invite : PlayerAPI.getCurrentChar(player).getInvites())
		{
			player.sendMessage(ChatColor.GRAY + "  " + invite.getName());
		}

		player.sendMessage(" ");

		player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " accept invite <invite name> " + ChatColor.GRAY + "to warp.");
		player.sendMessage(" ");
	}

	// View character
	protected static void viewChar(Player player, PlayerCharacter character)
	{
		player.sendMessage(ChatColor.YELLOW + " -> Viewing Character ---------------------------------");
		player.sendMessage(" ");

		String currentCharMsg = ChatColor.RED + "" + ChatColor.ITALIC + "(Inactive) " + ChatColor.RESET;
		String name = character.getName();
		Deity deity = character.getDeity();
		ChatColor deityColor = deity.getInfo().getColor();
		String alliance = character.getAlliance();
		int hp = character.getHealth();
		ChatColor hpColor = character.getHealthColor();
		int exp = Math.round(character.getExperience());
		int favor = character.getFavor();
		int maxFavor = character.getMaxFavor();
		ChatColor favorColor = character.getFavorColor();
		int devotion = character.getDevotion();
		int devotionGoal = character.getDevotionGoal();
		int ascensions = character.getAscensions();

		if(character.isActive()) currentCharMsg = ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "(Current) " + ChatColor.RESET;

		player.sendMessage("    " + currentCharMsg + ChatColor.YELLOW + name + ChatColor.GRAY + " > Allied to " + deityColor + deity + ChatColor.GRAY + " of the " + ChatColor.GOLD + alliance + "s");
		player.sendMessage(ChatColor.GRAY + "  --------------------------------------------------");
		player.sendMessage(ChatColor.GRAY + "    Health: " + ChatColor.WHITE + hpColor + hp + ChatColor.GRAY + " (of " + ChatColor.GREEN + 20 + ChatColor.GRAY + ")");
		player.sendMessage(ChatColor.GRAY + "    Experience: " + ChatColor.WHITE + exp);
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GRAY + "    Ascensions: " + ChatColor.GREEN + ascensions);
		player.sendMessage(ChatColor.GRAY + "    Devotion: " + ChatColor.WHITE + devotion + ChatColor.GRAY + " (" + ChatColor.YELLOW + (devotionGoal - devotion) + ChatColor.GRAY + " until next Ascension)");
		player.sendMessage(ChatColor.GRAY + "    Favor: " + favorColor + favor + ChatColor.GRAY + " (of " + ChatColor.GREEN + maxFavor + ChatColor.GRAY + ")");
		player.sendMessage(" ");

	}

	protected static void switchChar(Player player, String charName)
	{
		PlayerCharacter newChar = CharacterAPI.getCharByName(charName);

		if(newChar != null)
		{
			CharacterSwitchEvent event = new CharacterSwitchEvent(player, PlayerAPI.getCurrentChar(player), newChar);
			Bukkit.getServer().getPluginManager().callEvent(event);

			if(!event.isCancelled())
			{
				PlayerAPI.changeCurrentChar(player, newChar.getID());

				player.setDisplayName(newChar.getDeity().getInfo().getColor() + newChar.getName() + ChatColor.WHITE);
				player.setPlayerListName(newChar.getDeity().getInfo().getColor() + newChar.getName() + ChatColor.WHITE);

				// Save their previous character and chat number for later monitoring
				if(event.getCharacterFrom() != null) DemigodsData.playerData.saveData(player, "previous_char", event.getCharacterFrom().getID());
				DemigodsData.tempPlayerData.saveData(player, "temp_chat_number", 0);

				// Disable prayer
				PlayerAPI.togglePraying(player, false);
				player.sendMessage(ChatColor.AQUA + "You are no longer praying.");
				player.sendMessage(ChatColor.GRAY + "Your movement and chat have been re-enabled.");
			}
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Your current character couldn't be changed...");
			player.sendMessage(ChatColor.RED + "Please let an admin know.");
			PlayerAPI.togglePraying(player, false);
		}
	}

	// Choose name
	protected static void chooseName(Player player)
	{
		DemigodsData.tempPlayerData.saveData(player, "temp_createchar", "choose_name");
		player.sendMessage(ChatColor.AQUA + "  Enter a name: " + ChatColor.GRAY + "(Alpha-Numeric Only)");
		player.sendMessage(" ");
	}

	// Name confirmation
	protected static void confirmName(Player player, String message)
	{
		int maxCaps = Demigods.config.getSettingInt("character.max_caps_in_name");
		if(message.length() >= 15 || !StringUtils.isAlphanumeric(message) || PlayerAPI.hasCharName(player, message) || DemigodsData.hasCapitalLetters(message, maxCaps))
		{
			// Validate the name
			DemigodsData.tempPlayerData.saveData(player, "temp_createchar", "choose_name");
			if(message.length() >= 15) player.sendMessage(ChatColor.RED + "  That name is too long.");
			if(PlayerAPI.hasCharName(player, message)) player.sendMessage(ChatColor.RED + "  You already have a character with that name.");
			if(!StringUtils.isAlphanumeric(message)) player.sendMessage(ChatColor.RED + "  You can only use Alpha-Numeric characters.");
			if(DemigodsData.hasCapitalLetters(message, maxCaps)) player.sendMessage(ChatColor.RED + "  Too many capital letters. You can only have " + maxCaps + ".");
			player.sendMessage(ChatColor.AQUA + "  Enter a different name: " + ChatColor.GRAY + "(Alpha-Numeric Only)");
			player.sendMessage(" ");
		}
		else
		{
			DemigodsData.tempPlayerData.saveData(player, "temp_createchar", "confirm_name");
			String chosenName = message.replace(" ", "");
			player.sendMessage(ChatColor.AQUA + "  Are you sure you want to use " + ChatColor.YELLOW + chosenName + ChatColor.AQUA + "?" + ChatColor.GRAY + " (y/n)");
			player.sendMessage(" ");
			DemigodsData.tempPlayerData.saveData(player, "temp_createchar_name", chosenName);
		}
	}

	// Choose deity
	protected static void chooseDeity(Player player)
	{
		player.sendMessage(ChatColor.AQUA + "  Please choose a Deity: " + ChatColor.GRAY + "(Type in the name of the Deity)");
		for(String alliance : DeityAPI.getLoadedDeityAlliances())
		{
			for(Deity deity : DeityAPI.getAllDeitiesInAlliance(alliance))
				player.sendMessage(ChatColor.GRAY + "  -> " + ChatColor.YELLOW + DemigodsData.capitalize(deity.getInfo().getName()) + ChatColor.GRAY + " (" + alliance + ")");
		}
		player.sendMessage(" ");

		DemigodsData.tempPlayerData.saveData(player, "temp_createchar", "choose_deity");
	}

	// Deity confirmation
	protected static void confirmDeity(Player player, String message)
	{
		// Check their chosen Deity
		for(String alliance : DeityAPI.getLoadedDeityAlliances())
		{
			for(Deity deity : DeityAPI.getAllDeitiesInAlliance(alliance))
			{
				if(message.equalsIgnoreCase(deity.getInfo().getName()))
				{
					// Their chosen deity matches an existing deity, ask for confirmation
					String chosenDeity = message.replace(" ", "");
					player.sendMessage(ChatColor.AQUA + "  Are you sure you want to use " + ChatColor.YELLOW + DemigodsData.capitalize(chosenDeity) + ChatColor.AQUA + "?" + ChatColor.GRAY + " (y/n)");
					player.sendMessage(" ");
					DemigodsData.tempPlayerData.saveData(player, "temp_createchar_deity", DemigodsData.capitalize(chosenDeity.toLowerCase()));
					DemigodsData.tempPlayerData.saveData(player, "temp_createchar", "confirm_deity");
					return;
				}
			}
		}
		if(message.equalsIgnoreCase("_Alex"))
		{
			player.sendMessage(ChatColor.AQUA + "  Well you can't be _Alex... but he is awesome!");
			player.sendMessage(" ");

			// They can't be _Alex silly! Make them re-choose
			chooseDeity(player);
		}
	}

	// Confirmed deity
	@SuppressWarnings("unchecked")
	protected static void deityConfirmed(Player player)
	{
		// Define variables
		String chosenDeity = DemigodsData.tempPlayerData.getDataString(player, "temp_createchar_deity");

		// They accepted the Deity choice, now ask them to input their items so they can be accepted
		player.sendMessage(ChatColor.AQUA + "  Before you can confirm your lineage with " + ChatColor.YELLOW + chosenDeity + ChatColor.AQUA + ",");
		player.sendMessage(ChatColor.AQUA + "  you must first sacrifice the following items:");
		player.sendMessage(" ");
		for(Material item : DeityAPI.getDeity(chosenDeity).getInfo().getClaimItems())
		{
			player.sendMessage(ChatColor.GRAY + "  -> " + ChatColor.YELLOW + item.name());
		}
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GRAY + "  After you obtain these items, return to an Altar to");
		player.sendMessage(ChatColor.GRAY + "  confirm your new character.");
		player.sendMessage(" ");

		DemigodsData.tempPlayerData.saveData(player, "temp_createchar_finalstep", true);
	}

	// Final confirmation of deity
	@SuppressWarnings("unchecked")
	protected static void finalConfirmDeity(Player player)
	{
		// Define variables
		String chosenDeity = DemigodsData.tempPlayerData.getDataString(player, "temp_createchar_deity");

		// Save data
		DemigodsData.tempPlayerData.saveData(player, "temp_createchar_finalstep", true);
		DemigodsData.tempPlayerData.saveData(player, "temp_createchar", "confirm_all");

		// Send them the chat
		player.sendMessage(ChatColor.GREEN + " -> Confirming Character -------------------------------");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.AQUA + "  Do you have the following items in your inventory?" + ChatColor.GRAY + " (y/n)");
		player.sendMessage(" ");
		for(Material item : DeityAPI.getDeity(chosenDeity).getInfo().getClaimItems())
		{
			player.sendMessage(ChatColor.GRAY + "  -> " + ChatColor.YELLOW + item.name());
		}
		player.sendMessage(" ");
	}

	protected static void clearChat(Player player)
	{
		for(int x = 0; x < 120; x++)
			player.sendMessage(" ");
	}

	protected static void nameAltar(Player player, String name) // TODO Make warps store differently.
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);
		if(character.getWarps().isEmpty())
		{
			// Save named TrackedLocation for warp.
			character.addWarp(new TrackedLocation(DemigodsData.generateInt(5), player.getLocation(), name).getID());
			player.sendMessage(ChatColor.GRAY + "Your warp to this altar was named: " + ChatColor.YELLOW + name.toUpperCase() + ChatColor.GRAY + ".");
			return;
		}

		// Check for same names
		for(TrackedLocation warp : character.getWarps())
		{
			if(warp.getName().equalsIgnoreCase(name))
			{
				player.sendMessage(ChatColor.GRAY + "A warp by that name already exists.");
				return;
			}
		}

		// Check for same altars
		for(TrackedLocation warp : character.getWarps())
		{
			if(ZoneAPI.zoneAltar(warp.toLocation()) == ZoneAPI.zoneAltar(player.getLocation()))
			{
				character.removeWarp(warp.getID());
			}
		}

		// Save named TrackedLocation for warp.
		character.addWarp(new TrackedLocation(DemigodsData.generateInt(5), player.getLocation(), name).getID());
		player.sendMessage(ChatColor.GRAY + "Your warp to this Altar was named: " + ChatColor.YELLOW + name.toUpperCase() + ChatColor.GRAY + ".");
	}

	protected static void inviteWarp(Player player, String name)
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);
		PlayerCharacter invited = CharacterAPI.getCharByName(name);

		if(character == null) return;
		else if(invited == null)
		{
			player.sendMessage(" ");
			player.sendMessage(ChatColor.GRAY + "No such character exists, try again.");
			return;
		}
		else if(!invited.getOwner().isOnline() || invited.getOwner() == character.getOwner())
		{
			player.sendMessage(" ");
			player.sendMessage(invited.getDeity().getInfo().getColor() + invited.getName() + ChatColor.GRAY + " must be online to receive an invite.");
			return;
		}
		else if(!character.getAlliance().equalsIgnoreCase(invited.getAlliance()))
		{
			player.sendMessage(" ");
			player.sendMessage(invited.getDeity().getInfo().getColor() + invited.getName() + ChatColor.GRAY + " must be in your alliance to receive an invite.");
			return;
		}

		if(LocationAPI.alreadyInvited(character, invited)) LocationAPI.removeInvite(character, LocationAPI.getInvite(character, invited));

		LocationAPI.addInvite(character, invited);
		PlayerAPI.togglePraying(player, false);
		clearChat(player);

		player.sendMessage(invited.getDeity().getInfo().getColor() + invited.getName() + ChatColor.GRAY + " has been invited to this Altar.");
		invited.getOwner().getPlayer().sendMessage(invited.getDeity().getInfo().getColor() + character.getName() + ChatColor.GRAY + " has invited you to an Altar!");
		invited.getOwner().getPlayer().sendMessage(ChatColor.GRAY + "Head to a nearby Altar and " + ChatColor.DARK_PURPLE + "View Invites" + ChatColor.GRAY + ".");
	}

	protected static void acceptInvite(Player player, String name)
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);
		TrackedLocation invite = LocationAPI.getInvite(character, name);

		if(invite != null)
		{
			PlayerAPI.togglePraying(player, false);
			clearChat(player);

			player.teleport(invite.toLocation());

			player.sendMessage(ChatColor.GRAY + "Warp to " + ChatColor.YELLOW + invite.getName().toUpperCase() + ChatColor.GRAY + " complete.");

			LocationAPI.removeInvite(character, invite);
			return;
		}
		player.sendMessage(ChatColor.GRAY + "No invite by that name exists, try again.");
	}

	protected static void warpChar(Player player, String warpName)
	{
		for(TrackedLocation warp : PlayerAPI.getCurrentChar(player).getWarps())
		{
			if(warp.getName().equals(warpName.toUpperCase()))
			{
				PlayerAPI.togglePraying(player, false);
				clearChat(player);

				player.teleport(warp.toLocation());

				player.sendMessage(ChatColor.GRAY + "Warp to " + ChatColor.YELLOW + warpName.toUpperCase() + ChatColor.GRAY + " complete.");
				return;
			}
		}
		player.sendMessage(ChatColor.GRAY + "No warp by that name exists, try again.");
	}
}

class AltarGenerate extends Task
{
	private static String name = "Altar Generation";
	private static int order = 0;
	private static double reward = 0.0, penalty = 0.0;
	private static Listener listener = new Listener()
	{
		@EventHandler(priority = EventPriority.MONITOR)
		public void onChunkLoad(ChunkLoadEvent event)
		{
			// Define variables
			Location location = MiscAPI.randomChunkLocation(event.getChunk());

			// If it's a new chunk then we'll generate structures
			if(event.isNewChunk())
			{
				// Choose an arbitrary value and check the chance against it
				if(DemigodsData.randomPercentBool(Demigods.config.getSettingDouble("generation.altar_chance")))
				{
					if(BlockAPI.canGenerateSolid(location, 6))
					{
						// If another Altar doesn't exist nearby then make one
						if(!BlockAPI.altarNearby(location, Demigods.config.getSettingInt("generation.min_blocks_between_altars")))
						{
							AltarCreateEvent altarCreateEvent = new AltarCreateEvent(location, AltarCreateEvent.AltarCreateCause.GENERATED);
							Bukkit.getServer().getPluginManager().callEvent(altarCreateEvent);
							if(altarCreateEvent.isCancelled()) return;

							new com.censoredsoftware.Demigods.Engine.Block.Altar(DemigodsData.generateInt(5), location);

							location.getWorld().strikeLightningEffect(location);
							location.getWorld().strikeLightningEffect(location);

							for(Entity entity : event.getWorld().getEntities())
							{
								if(entity instanceof Player)
								{
									if(entity.getLocation().distance(location) < 400)
									{
										((Player) entity).sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "An Altar has spawned near you...");
									}
								}
							}
						}
					}
				}
			}
		}
	};

	public AltarGenerate(String quest, String permission, List<String> about, List<String> accepted, List<String> complete, List<String> failed, Quest.Type type)
	{
		super(new TaskInfo(name, quest, permission, order, reward, penalty, about, accepted, complete, failed, type, TaskInfo.Subtype.TECHNICAL), listener);
	}
}
