package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;

public class SpigotUtility
{
	public static boolean runningSpigot()
	{
		try
		{
			Bukkit.getServer().getWorlds().get(0).spigot();
			return true;
		}
		catch(Exception ignored)
		{}
		return false;
	}

	public static void playParticle(Location location, Effect effect, int offsetX, int offsetY, int offsetZ, float speed, int particles, int viewRadius)
	{
		if(!runningSpigot()) throw new IllegalArgumentException("Spigot is required to use this feature.");
		location.getWorld().spigot().playEffect(location, effect, 1, 1, offsetX, offsetY, offsetZ, speed, particles, viewRadius);
	}
}
