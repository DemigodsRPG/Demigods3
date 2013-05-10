package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.Demigods.API.AdminAPI;
import com.censoredsoftware.Demigods.API.BlockAPI;
import com.censoredsoftware.Demigods.API.PlayerAPI;
import com.censoredsoftware.Demigods.API.ValueAPI;
import com.censoredsoftware.Demigods.Engine.Block.Shrine;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.Event.Shrine.ShrineCreateEvent;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

public class ShrineListener implements Listener
{
	public static double FAVOR_MULTIPLIER = Demigods.config.getSettingDouble("multipliers.favor");

	@EventHandler(priority = EventPriority.HIGH)
	public void shrineBlockInteract(PlayerInteractEvent event)
	{
		// Return if the player is mortal
		if(!PlayerAPI.isImmortal(event.getPlayer())) return;
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

		// Define variables
		Location location = event.getClickedBlock().getLocation();
		Player player = event.getPlayer();
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);
		String charAlliance = character.getAlliance();
		Deity charDeity = character.getDeity();

		if(event.getClickedBlock().getType().equals(Material.GOLD_BLOCK) && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getItemInHand().getType() == Material.BOOK)
		{
			try
			{
				// Shrine created!
				ShrineCreateEvent shrineCreateEvent = new ShrineCreateEvent(character, location);
				Bukkit.getServer().getPluginManager().callEvent(shrineCreateEvent);
				if(shrineCreateEvent.isCancelled()) return;

				new Shrine(DemigodsData.generateInt(5), character, location);
				location.getWorld().strikeLightningEffect(location);

				if(player.getItemInHand().getAmount() > 1)
				{
					ItemStack books = new ItemStack(player.getItemInHand().getType(), player.getInventory().getItemInHand().getAmount() - 1);
					player.setItemInHand(books);
				}
				else player.getInventory().remove(Material.BOOK);

				player.sendMessage(ChatColor.GRAY + "The " + ChatColor.YELLOW + charAlliance + "s" + ChatColor.GRAY + " are pleased...");
				player.sendMessage(ChatColor.GRAY + "You have created a Shrine in the name of " + ChatColor.YELLOW + charDeity + ChatColor.GRAY + "!");
			}
			catch(Exception e)
			{
				// Creation of shrine failed...
				e.printStackTrace();
			}
		}

		useShrine(player, location);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void shrineEntityInteract(PlayerInteractEntityEvent event)
	{
		// Define variables
		Location location = event.getRightClicked().getLocation().subtract(0.5, 1.0, 0.5);
		Player player = event.getPlayer();

		useShrine(player, location);
	}

	private void useShrine(Player player, Location location)
	{
		// First handle admin wand
		if(AdminAPI.useWand(player) && BlockAPI.isShrine(location)) // TODO TimedObject data.
		{
			// if(API.data.hasTimedData(player, "temp_destroy_shrine"))
			// {
			// Shrine shrine = BlockAPI.getShrine(location);
			//
			// ShrineRemoveEvent shrineRemoveEvent = new ShrineRemoveEvent(shrine.getOwner(), location);
			// Bukkit.getServer().getPluginManager().callEvent(shrineRemoveEvent);
			// if(shrineRemoveEvent.isCancelled()) return;
			//
			// // We can destroy the Shrine
			// BlockAPI.getShrine(location).remove();
			// DemigodsData.tempPlayerData.removeData(player, "temp_destroy_shrine");
			//
			// // Drop the block of gold and book
			// location.getWorld().dropItemNaturally(location, new ItemStack(Material.GOLD_BLOCK, 1));
			// location.getWorld().dropItemNaturally(location, new ItemStack(Material.BOOK, 1));
			//
			// player.sendMessage(ChatColor.GREEN + "Shrine removed!");
			// return;
			// }
			// else
			// {
			// API.data.saveTimedData(player, "temp_destroy_shrine", true, 5);
			// player.sendMessage(ChatColor.RED + "Right-click this Shrine again to remove it.");
			// return;
			// }
		}

		// Define variables
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		// Return if the player is mortal
		if(character == null || !character.isImmortal())
		{
			player.sendMessage(ChatColor.RED + "You must be immortal to use that!");
			return;
		}

		try
		{
			// Check if block is divine
			Shrine shrine = BlockAPI.getShrine(location);
			if(shrine == null) return;

			String shrineOwner = shrine.getOwner().getName();
			String shrineDeity = shrine.getDeity();

			if(BlockAPI.isShrine(location))
			{
				// Check if character has deity
				if(character.isDeity(shrineDeity))
				{
					// Open the tribute inventory
					Inventory ii = Bukkit.getServer().createInventory(player, 27, "Shrine of " + shrineDeity);
					player.openInventory(ii);
					DemigodsData.setTemp(player.getName(), character.getName(), shrineOwner);
					return;
				}
				player.sendMessage(ChatColor.YELLOW + "You must be allied to " + shrineDeity + " in order to tribute here.");
			}
		}
		catch(Exception e)
		{
			// Print error for debugging
			e.printStackTrace();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void playerTribute(InventoryCloseEvent event)
	{
		try
		{
			if(!(event.getPlayer() instanceof Player)) return;
			Player player = (Player) event.getPlayer();
			PlayerCharacter character = PlayerAPI.getCurrentChar(player);
			if(character == null || !character.isImmortal()) return;

			Deity charDeity = character.getDeity();

			// If it isn't a tribute chest then break the method
			if(!event.getInventory().getName().contains("Shrine")) return;

			// Get the creator of the shrine
			PlayerCharacter shrineOwner = PlayerCharacter.getCharacterByName(DemigodsData.getValueTemp(player.getName(), character.getName()).toString());
			DemigodsData.removeTemp(player.getName(), character.getName());

			// Calculate value of chest
			int tributeValue = 0, items = 0;
			for(ItemStack ii : event.getInventory().getContents())
			{
				if(ii != null)
				{
					tributeValue += ValueAPI.getTributeValue(ii);
					items++;
				}
			}

			tributeValue *= FAVOR_MULTIPLIER;

			// Process tributes and send messages
			int favorBefore = character.getMaxFavor();
			int devotionBefore = character.getDevotion();

			// Update the character's favor and devotion
			character.getMeta().addMaxFavor(tributeValue / 5);
			character.getMeta().addDevotion(tributeValue);

			if(character.getDevotion() > devotionBefore) player.sendMessage(ChatColor.GRAY + "Your devotion to " + ChatColor.YELLOW + charDeity + ChatColor.GRAY + " has increased to " + ChatColor.GREEN + character.getDevotion() + ChatColor.GRAY + ".");
			if(character.getMaxFavor() > favorBefore) player.sendMessage(ChatColor.GRAY + "Your favor cap has increased to " + ChatColor.GREEN + character.getMaxFavor() + ChatColor.GRAY + ".");

			if(favorBefore != character.getMaxFavor() && devotionBefore != character.getDevotion() && items > 0)
			{
				// Update the shrine owner's devotion and let them know
				OfflinePlayer shrineOwnerPlayer = shrineOwner.getOwner();
				if(!character.equals(shrineOwnerPlayer))
				{
					// TODO: FIX THIS
					// DCharUtil.addDevotion(shrineOwner, tributeValue / 7);
					if(shrineOwnerPlayer.isOnline())
					{
						((Player) shrineOwnerPlayer).sendMessage(ChatColor.YELLOW + "Someone just tributed at your shrine!");
						((Player) shrineOwnerPlayer).sendMessage(ChatColor.GRAY + "Your devotion has increased to " + shrineOwner.getDevotion() + "!");
					}
				}
			}
			else
			{
				// If they aren't good enough let them know
				if(items > 0) player.sendMessage(ChatColor.RED + "Your tributes were insufficient for " + charDeity + "'s blessings.");
			}

			// Clear the tribute case
			event.getInventory().clear();
		}
		catch(Exception e)
		{
			// Print error for debugging
			e.printStackTrace();
		}
	}
}
