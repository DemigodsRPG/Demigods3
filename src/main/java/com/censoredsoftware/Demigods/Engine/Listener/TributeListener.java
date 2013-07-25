package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
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
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerWrapper;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Structure;
import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureFlag;
import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureSave;
import com.censoredsoftware.Demigods.Engine.Utility.DataUtility;
import com.censoredsoftware.Demigods.Engine.Utility.ItemValueUtility;

public class TributeListener implements Listener
{
	// TODO This seems way to complicated for something as simple as tributing...
	// TODO Maybe we're just doing too much in one method-- I'm not sure...

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTributeInteract(PlayerInteractEvent event)
	{
		// Return if the player is mortal
		if(!PlayerWrapper.isImmortal(event.getPlayer())) return;
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

		// Define variables
		Location location = event.getClickedBlock().getLocation();
		Player player = event.getPlayer();
		PlayerCharacter character = PlayerWrapper.getPlayer(player).getCurrent();

		if(Structure.partOfStructureWithFlag(location, StructureFlag.TRIBUTE_LOCATION))
		{
			// Cancel the interaction
			event.setCancelled(true);

			// Define the shrine
			StructureSave save = Structure.getStructure(location);

			// Return if they aren't clicking the gold block
			if(!event.getClickedBlock().getLocation().equals(save.getClickableBlock())) return;

			// Return if the player is mortal
			if(character == null || !character.isImmortal())
			{
				player.sendMessage(ChatColor.RED + "You must be immortal to use that!");
				return;
			}

			if(save.getSettingHasOwner() && save.getOwner() != null && !character.getDeity().equals(save.getOwner().getDeity()))
			{
				player.sendMessage(ChatColor.YELLOW + "You must be allied to " + save.getOwner().getDeity().getInfo().getName() + " in order to tribute here.");
				return;
			}
			tribute(character, save);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerTribute(InventoryCloseEvent event)
	{
		// Define player and character
		Player player = (Player) event.getPlayer();
		PlayerCharacter character = PlayerWrapper.getPlayer(player).getCurrent();

		// Make sure they have a character and are immortal
		if(character == null || !character.isImmortal()) return;

		// If it isn't a tribute chest then break the method
		if(!event.getInventory().getName().contains("Tribute to") || !Structure.partOfStructureWithFlag(player.getTargetBlock(null, 10).getLocation(), StructureFlag.TRIBUTE_LOCATION)) return;

		// Get the creator of the shrine
		StructureSave save = StructureSave.load(Long.valueOf(DataUtility.getValueTemp(player.getName(), character.getName()).toString()));

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
		if(save.getSettingHasOwner())
		{
			PlayerCharacter shrineOwner = save.getOwner();
			OfflinePlayer shrineOwnerPlayer = shrineOwner.getOfflinePlayer();

			if(character.getMeta().getMaxFavor() >= Demigods.config.getSettingInt("caps.favor") && !player.getName().equals(shrineOwnerPlayer.getName()))
			{
				// Give them some of the blessings
				shrineOwner.getMeta().addMaxFavor(tributeValue / 5);

				// Message them
				if(shrineOwnerPlayer.isOnline() && PlayerWrapper.getPlayer(shrineOwner.getOfflinePlayer()).getCurrent().getId().equals(shrineOwner.getId()))
				{
					((Player) shrineOwnerPlayer).sendMessage(ChatColor.YELLOW + "Someone just tributed at your shrine!");
					((Player) shrineOwnerPlayer).sendMessage(ChatColor.GRAY + "Your favor cap is now " + ChatColor.GREEN + shrineOwner.getMeta().getMaxFavor() + ChatColor.GRAY + "!");
				}
			}
			else if(character.getMeta().getMaxFavor() > maxFavorBefore && !player.getName().equals(shrineOwnerPlayer.getName()))
			{
				// Define variables
				int ownerFavorBefore = shrineOwner.getMeta().getMaxFavor();

				// Give them some of the blessings
				shrineOwner.getMeta().addMaxFavor(tributeValue / 5);

				// Message them
				if(shrineOwnerPlayer.isOnline() && PlayerWrapper.getPlayer(shrineOwner.getOfflinePlayer()).getCurrent().getId().equals(shrineOwner.getId()))
				{
					((Player) shrineOwnerPlayer).sendMessage(ChatColor.YELLOW + "Someone just tributed at your shrine!");
					if(shrineOwner.getMeta().getMaxFavor() > ownerFavorBefore) ((Player) shrineOwnerPlayer).sendMessage(ChatColor.GRAY + "Your favor cap has increased to " + ChatColor.GREEN + shrineOwner.getMeta().getMaxFavor() + ChatColor.GRAY + "!");
				}
			}
		}

		// Handle messaging and Shrine owner updating
		if(character.getMeta().getMaxFavor() >= Demigods.config.getSettingInt("caps.favor"))
		{
			// They have already met the max favor cap
			player.sendMessage(ChatColor.YELLOW + character.getDeity().getInfo().getName() + " is pleased!");
			if(character.getMeta().getFavor() > favorBefore) player.sendMessage(ChatColor.GRAY + "You have been blessed with " + ChatColor.GREEN + (tributeValue / 3) + ChatColor.GRAY + " favor.");
		}
		else if(character.getMeta().getMaxFavor() > maxFavorBefore)
		{
			// Message the tributer
			player.sendMessage(ChatColor.YELLOW + character.getDeity().getInfo().getName() + " is pleased!");
			player.sendMessage(ChatColor.GRAY + "Your favor cap is now " + ChatColor.GREEN + character.getMeta().getMaxFavor() + ChatColor.GRAY + ".");
		}
		else if(items > 0)
		{
			// They aren't good enough, let them know!
			player.sendMessage(ChatColor.RED + "Your tributes were insufficient for " + character.getDeity().getInfo().getName() + "'s blessings.");
		}

		// Clear the tribute case
		event.getInventory().clear();
	}

	private static void tribute(PlayerCharacter character, StructureSave save)
	{
		Player player = character.getOfflinePlayer().getPlayer();
		Deity shrineDeity = character.getDeity();

		// Open the tribute inventory
		Inventory ii = Bukkit.getServer().createInventory(player, 27, "Tribute to " + shrineDeity);
		player.openInventory(ii);
		DataUtility.saveTemp(player.getName(), character.getName(), save.getId());
	}
}
