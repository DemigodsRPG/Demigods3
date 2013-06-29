package com.censoredsoftware.Demigods.Episodes.Demo.Task.Static;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsPlayer;
import com.censoredsoftware.Demigods.Engine.Object.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Old.Shrine;
import com.censoredsoftware.Demigods.Engine.Object.Task.Task;
import com.censoredsoftware.Demigods.Engine.Object.Task.TaskInfo;
import com.censoredsoftware.Demigods.Engine.Object.Task.TaskSet;
import com.censoredsoftware.Demigods.Engine.Utility.DataUtility;
import com.censoredsoftware.Demigods.Engine.Utility.ItemValueUtility;

public class ShrineTasks extends TaskSet
{
	private static String name = "Shrine", permission = "demigods.shrine";
	private static Type type = Type.PASSIVE;

	private static List<String> about = new ArrayList<String>()
	{
		{
			add("Shrines are an integral part of Demigods.");
			add("Right-click a gold block with a book to create one.");
			add("With your Shrine you can tribute to your Deity!");
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

	private static List<Task> tasks = new ArrayList<Task>()
	{
		{
			add(new Tribute(name, permission, about, accepted, complete, failed, type));
		}
	};

	public ShrineTasks()
	{
		super(name, permission, about, accepted, complete, failed, type, tasks);
	}
}

class Tribute extends Task
{
	// Define required task variables
	private static String name = "Tributing";
	private static int order = 0;
	private static double reward = 0.0, penalty = 0.0;

	private static Listener listener = new Listener()
	{
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onShrineInteract(PlayerInteractEvent event)
		{
			// Return if the player is mortal
			if(!DemigodsPlayer.isImmortal(event.getPlayer())) return;
			if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

			// Define variables
			Location location = event.getClickedBlock().getLocation();
			Player player = event.getPlayer();
			PlayerCharacter character = DemigodsPlayer.getPlayer(player).getCurrent();

			if(Shrine.isShrine(location))
			{
				// Cancel the interaction
				event.setCancelled(true);

				// Define the shrine
				Shrine shrine = Shrine.getShrine(location);

				// Return if they aren't clicking the gold block
				if(!event.getClickedBlock().getType().equals(Material.GOLD_BLOCK)) return;

				// Return if the player is mortal
				if(character == null || !character.isImmortal())
				{
					player.sendMessage(ChatColor.RED + "You must be immortal to use that!");
					return;
				}

				if(character.getDeity().equals(shrine.getDeity()))
				{
					useShrine(character, shrine);
				}
				else
				{
					player.sendMessage(ChatColor.YELLOW + "You must be allied to " + shrine.getDeity().getInfo().getName() + " in order to tribute here.");
				}
			}
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPlayerTribute(InventoryCloseEvent event)
		{
			// Return if it's not a player
			if(!(event.getPlayer() instanceof Player)) return;

			// Define player and character
			Player player = (Player) event.getPlayer();
			PlayerCharacter character = DemigodsPlayer.getPlayer(player).getCurrent();

			// Make sure they have a character and are immortal
			if(character == null || !character.isImmortal()) return;

			// If it isn't a tribute chest then break the method
			if(!event.getInventory().getName().contains("Shrine") || !Shrine.isShrine(player.getTargetBlock(null, 10).getLocation())) return;

			// Get the creator of the shrine
			PlayerCharacter shrineOwner = PlayerCharacter.getCharacterByName(DataUtility.getValueTemp(player.getName(), character.getName()).toString());

			// Calculate the tribute value
			int tributeValue = 0, items = 0;
			for(ItemStack item : event.getInventory().getContents())
			{
				if(item != null)
				{
					tributeValue += ItemValueUtility.getTributeValue(item);
					items += item.getAmount();
				}
			}

			// Return if it's empty
			if(items == 0) return;

			// Handle the multiplier
			tributeValue *= Demigods.config.getSettingDouble("multipliers.favor");

			// Get the current favor for comparison
			int favorBefore = character.getMeta().getFavor();
			int maxFavorBefore = character.getMeta().getMaxFavor();

			// Update the character's favor
			character.getMeta().addFavor(tributeValue / 3);
			character.getMeta().addMaxFavor(tributeValue);

			// Define the shrine owner
			OfflinePlayer shrineOwnerPlayer = shrineOwner.getOfflinePlayer();

			// Handle messaging and Shrine owner updating
			if(character.getMeta().getMaxFavor() >= Demigods.config.getSettingInt("caps.favor"))
			{
				// They have already met the max favor cap
				player.sendMessage(ChatColor.YELLOW + character.getDeity().getInfo().getName() + " is pleased!");
				if(character.getMeta().getFavor() > favorBefore) player.sendMessage(ChatColor.GRAY + "You have been blessed with " + ChatColor.GREEN + (tributeValue / 3) + ChatColor.GRAY + " favor.");

				if(!player.getName().equals(shrineOwnerPlayer.getName()))
				{
					// Give them some of the blessings
					shrineOwner.getMeta().addMaxFavor(tributeValue / 5);

					// Message them
					if(shrineOwnerPlayer.isOnline() && DemigodsPlayer.getPlayer(shrineOwner.getOfflinePlayer()).getCurrent().getId().equals(shrineOwner.getId()))
					{
						((Player) shrineOwnerPlayer).sendMessage(ChatColor.YELLOW + "Someone just tributed at your shrine!");
						((Player) shrineOwnerPlayer).sendMessage(ChatColor.GRAY + "Your favor cap is now " + ChatColor.GREEN + shrineOwner.getMeta().getMaxFavor() + ChatColor.GRAY + "!");
					}
				}
			}
			else if(character.getMeta().getMaxFavor() > maxFavorBefore)
			{
				// Message the tributer
				player.sendMessage(ChatColor.YELLOW + character.getDeity().getInfo().getName() + " is pleased!");
				player.sendMessage(ChatColor.GRAY + "Your favor cap is now " + ChatColor.GREEN + character.getMeta().getMaxFavor() + ChatColor.GRAY + ".");

				if(!player.getName().equals(shrineOwnerPlayer.getName()))
				{
					// Define variables
					int ownerFavorBefore = shrineOwner.getMeta().getMaxFavor();

					// Give them some of the blessings
					shrineOwner.getMeta().addMaxFavor(tributeValue / 5);

					// Message them
					if(shrineOwnerPlayer.isOnline() && DemigodsPlayer.getPlayer(shrineOwner.getOfflinePlayer()).getCurrent().getId().equals(shrineOwner.getId()))
					{
						((Player) shrineOwnerPlayer).sendMessage(ChatColor.YELLOW + "Someone just tributed at your shrine!");
						if(shrineOwner.getMeta().getMaxFavor() > ownerFavorBefore) ((Player) shrineOwnerPlayer).sendMessage(ChatColor.GRAY + "Your favor cap has increased to " + ChatColor.GREEN + shrineOwner.getMeta().getMaxFavor() + ChatColor.GRAY + "!");
					}
				}
			}
			else if(items > 0)
			{
				// They aren't good enough, let them know!
				player.sendMessage(ChatColor.RED + "Your tributes were insufficient for " + character.getDeity().getInfo().getName() + "'s blessings.");
			}

			// Clear the tribute case
			event.getInventory().clear();
		}
	};

	private static void useShrine(PlayerCharacter character, Shrine shrine)
	{
		Player player = character.getOfflinePlayer().getPlayer();
		String shrineOwner = shrine.getCharacter().getName();
		Deity shrineDeity = shrine.getDeity();

		// Open the tribute inventory
		Inventory ii = Bukkit.getServer().createInventory(player, 27, "Shrine of " + shrineDeity);
		player.openInventory(ii);
		DataUtility.saveTemp(player.getName(), character.getName(), shrineOwner);
	}

	public Tribute(String quest, String permission, List<String> about, List<String> accepted, List<String> complete, List<String> failed, TaskSet.Type type)
	{
		super(new TaskInfo(name, quest, permission, order, reward, penalty, about, accepted, complete, failed, type, TaskInfo.Subtype.TECHNICAL), listener);
	}
}
