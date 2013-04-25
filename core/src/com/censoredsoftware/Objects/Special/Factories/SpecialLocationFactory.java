package com.censoredsoftware.Objects.Special.Factories;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Objects.Special.SpecialLocation;

/**
 * Factory for creating SpecialLocation objects.
 */
public class SpecialLocationFactory
{
	private Plugin plugin;

	/**
	 * Constructor for the factory.
	 * 
	 * @param instance The plugin using this factory.
	 */
	public SpecialLocationFactory(Plugin instance)
	{
		this.plugin = instance;
	}

	/**
	 * Create a SpecialLocation from an <code>location</code>.
	 * 
	 * @param location The Location being converted.
	 * @return The SpecialLocation.
	 */
	public SpecialLocation create(Location location)
	{
		return new SpecialLocation(plugin, location, null);
	}

	/**
	 * Create a SpecialLocation, named <code>name</code>, from an <code>location</code>.
	 * 
	 * @param location The Location being converted.
	 * @param name The name of the SpecialLocation.
	 * @return The SpecialLocation.
	 */
	public SpecialLocation create(Location location, String name)
	{
		return new SpecialLocation(plugin, location, name);
	}

	/**
	 * Create a SpecialLocation from scratch.
	 * 
	 * @param world The name of the world.
	 * @param X The X coordinate.
	 * @param Y The Y coordinate.
	 * @param Z The Z coordinate.
	 * @param pitch The pitch.
	 * @param yaw The yaw.
	 * @return The SpecialLocation.
	 */
	public SpecialLocation create(String world, double X, double Y, double Z, float pitch, float yaw)
	{
		return new SpecialLocation(plugin, world, X, Y, Z, pitch, yaw, null);
	}

	/**
	 * Create a SpecialLocation, named <code>name</code>, from scratch.
	 * 
	 * @param world The name of the world.
	 * @param X The X coordinate.
	 * @param Y The Y coordinate.
	 * @param Z The Z coordinate.
	 * @param pitch The pitch.
	 * @param yaw The yaw.
	 * @param name The name of the SpecialLocation.
	 * @return The SpecialLocation.
	 */
	public SpecialLocation create(String world, double X, double Y, double Z, float pitch, float yaw, String name)
	{
		return new SpecialLocation(plugin, world, X, Y, Z, pitch, yaw, name);
	}
}
