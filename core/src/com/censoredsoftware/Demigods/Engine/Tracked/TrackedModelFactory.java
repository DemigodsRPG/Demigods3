package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
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
		trackedInventory.setHelmet(new TrackedItemStack(inventory.getHelmet()));
		trackedInventory.setChestplate(new TrackedItemStack(inventory.getChestplate()));
		trackedInventory.setLeggings(new TrackedItemStack(inventory.getLeggings()));
		trackedInventory.setBoots(new TrackedItemStack(inventory.getBoots()));

		// Define the new items map and the slot counter and save the contents
		trackedInventory.setItems(new HashMap<Integer, TrackedItemStack>());
		TrackedPlayerInventory.save(trackedInventory.processInventory(inventory));
		return trackedInventory;
	}
}
