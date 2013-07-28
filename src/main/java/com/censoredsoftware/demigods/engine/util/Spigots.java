package com.censoredsoftware.demigods.engine.util;

import org.bukkit.Effect;
import org.bukkit.Location;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.exception.SpigotNotFoundException;

public class Spigots
{
	public static void playParticle(Location location, Effect effect, int offsetX, int offsetY, int offsetZ, float speed, int particles, int viewRadius)
	{
		if(!Demigods.runningSpigot()) throw new SpigotNotFoundException();
		location.getWorld().spigot().playEffect(location, effect, 1, 1, offsetX, offsetY, offsetZ, speed, particles, viewRadius);
	}
}
