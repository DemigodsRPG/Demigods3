package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TrackedModelFactory
{
	public static TrackedLocation createTrackedLocation(String world, double X, double Y, double Z, float yaw, float pitch)
	{
		TrackedLocation trackedLocation = new TrackedLocation();
		trackedLocation.setWorld(world);
		trackedLocation.setX(X);
		trackedLocation.setY(Y);
		trackedLocation.setZ(Z);
		trackedLocation.setYaw(yaw);
		trackedLocation.setPitch(pitch);
		TrackedLocation.save(trackedLocation);
		return trackedLocation;
	}

	public static TrackedLocation createTrackedLocation(Location location)
	{
		TrackedLocation trackedLocation = new TrackedLocation();
		trackedLocation.setWorld(location.getWorld().getName());
		trackedLocation.setX(location.getX());
		trackedLocation.setY(location.getY());
		trackedLocation.setZ(location.getZ());
		trackedLocation.setYaw(location.getYaw());
		trackedLocation.setPitch(location.getPitch());
		TrackedLocation.save(trackedLocation);
		return trackedLocation;
	}

	public static TrackedPlayer createTrackedPlayer(OfflinePlayer player)
	{
		TrackedPlayer trackedPlayer = new TrackedPlayer();
		trackedPlayer.setPlayer(player.getName());
		trackedPlayer.setLastLoginTime(player.getLastPlayed());
		TrackedPlayer.save(trackedPlayer);
		return trackedPlayer;
	}

	public static TrackedPlayerInventory createTrackedPlayerInventory(PlayerInventory inventory)
	{
		TrackedPlayerInventory trackedInventory = new TrackedPlayerInventory();
		// Save armor
		trackedInventory.setHelmet(createTrackedItemStack(inventory.getHelmet()));
		trackedInventory.setChestplate(createTrackedItemStack(inventory.getChestplate()));
		trackedInventory.setLeggings(createTrackedItemStack(inventory.getLeggings()));
		trackedInventory.setBoots(createTrackedItemStack(inventory.getBoots()));

		// Define the new items map and the slot counter and save the contents
		trackedInventory.setItems(new HashMap<Integer, TrackedItemStack>());
		TrackedPlayerInventory.save(trackedInventory.processInventory(inventory));
		return trackedInventory;
	}

	public static TrackedItemStack createTrackedItemStack(ItemStack item)
	{
		TrackedItemStack trackedItem = new TrackedItemStack();
		trackedItem.setTypeId(item.getTypeId());
		trackedItem.setByteId(item.getData().getData());
		trackedItem.setAmount(item.getAmount());
		trackedItem.setDurability(item.getDurability());
		if(item.getItemMeta().hasDisplayName()) trackedItem.setName(item.getItemMeta().getDisplayName());
		if(item.getItemMeta().hasLore()) trackedItem.setLore(item.getItemMeta().getLore());
		trackedItem.setEnchantments(item);
		trackedItem.setBookMeta(item);
		TrackedItemStack.save(trackedItem);
		return trackedItem;
	}
}
