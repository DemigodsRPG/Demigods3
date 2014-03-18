package com.censoredsoftware.library.util;

import org.bukkit.Effect;
import org.bukkit.Location;

public class Spigots
{
	/**
	 * Play a particle with the Spigot API.
	 * 
	 * @param location The location of the particle.
	 * @param effect The effect (particle) type.
	 * @param offsetX Offset from the location's X coordinate.
	 * @param offsetY Offset from the location's Y coordinate.
	 * @param offsetZ Offset from the location's Z coordinate.
	 * @param speed The speed of the particle effect.
	 * @param particles The number of particles.
	 * @param viewRadius The radius for which players can view this effect.
	 */
	public static void playParticle(Location location, Effect effect, int offsetX, int offsetY, int offsetZ, float speed, int particles, int viewRadius)
	{
		try
		{
			location.getWorld().spigot().playEffect(location, effect, 1, 1, offsetX, offsetY, offsetZ, speed, particles, viewRadius);
		}
		catch(Exception ignored)
		{}
	}
}
