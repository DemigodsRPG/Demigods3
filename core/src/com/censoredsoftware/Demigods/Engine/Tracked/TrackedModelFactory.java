package com.censoredsoftware.Demigods.Engine.Tracked;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

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
}
