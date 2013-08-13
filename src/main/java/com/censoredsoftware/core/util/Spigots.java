package com.censoredsoftware.core.util;

import org.bukkit.Effect;
import org.bukkit.Location;

public class Spigots
{
	public static void playParticle(Location location, Effect effect, int offsetX, int offsetY, int offsetZ, float speed, int particles, int viewRadius)
	{
		location.getWorld().spigot().playEffect(location, effect, 1, 1, offsetX, offsetY, offsetZ, speed, particles, viewRadius);
	}
}
