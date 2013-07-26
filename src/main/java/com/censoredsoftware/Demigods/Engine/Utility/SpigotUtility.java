package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Effect;
import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Exception.SpigotNotFoundException;

public class SpigotUtility
{
	public static void playParticle(Location location, Effect effect, int offsetX, int offsetY, int offsetZ, float speed, int particles, int viewRadius)
	{
		if(!Demigods.runningSpigot()) throw new SpigotNotFoundException();
		location.getWorld().spigot().playEffect(location, effect, 1, 1, offsetX, offsetY, offsetZ, speed, particles, viewRadius);
	}
}
