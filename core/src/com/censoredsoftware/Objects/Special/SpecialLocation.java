package com.censoredsoftware.Objects.Special;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.censoredsoftware.Modules.DataPersistence.EnumDataModule;

public class SpecialLocation
{
	private EnumDataModule specialLocationData;

	public SpecialLocation(String world, double X, double Y, double Z, float pitch, float yaw, String name)
	{
		specialLocationData = new EnumDataModule();
		specialLocationData.saveData(SpecialLocationData.WORLD, world);
		specialLocationData.saveData(SpecialLocationData.X, X);
		specialLocationData.saveData(SpecialLocationData.Y, Y);
		specialLocationData.saveData(SpecialLocationData.Z, Z);
		specialLocationData.saveData(SpecialLocationData.PITCH, pitch);
		specialLocationData.saveData(SpecialLocationData.YAW, yaw);
		if(name != null) specialLocationData.saveData(SpecialLocationData.NAME, name);
	}

	public SpecialLocation(Location location, String name)
	{
		specialLocationData = new EnumDataModule();
		specialLocationData.saveData(SpecialLocationData.WORLD, location.getWorld().getName());
		specialLocationData.saveData(SpecialLocationData.X, location.getX());
		specialLocationData.saveData(SpecialLocationData.Y, location.getY());
		specialLocationData.saveData(SpecialLocationData.Z, location.getZ());
		specialLocationData.saveData(SpecialLocationData.PITCH, location.getPitch());
		specialLocationData.saveData(SpecialLocationData.YAW, location.getYaw());
		if(name != null) specialLocationData.saveData(SpecialLocationData.NAME, name);
	}

	public boolean hasName()
	{
		return specialLocationData.containsKey(SpecialLocationData.NAME);
	}

	public String getName()
	{
		return specialLocationData.getDataString(SpecialLocationData.NAME);
	}

	public synchronized void setName(String name)
	{
		specialLocationData.saveData(SpecialLocationData.NAME, name);
	}

	public Location toLocation() throws NullPointerException
	{
		return new Location(Bukkit.getServer().getWorld(specialLocationData.getDataString(SpecialLocationData.WORLD)), specialLocationData.getDataDouble(SpecialLocationData.X), specialLocationData.getDataDouble(SpecialLocationData.Y), specialLocationData.getDataDouble(SpecialLocationData.Z), specialLocationData.getDataFloat(SpecialLocationData.YAW), specialLocationData.getDataFloat(SpecialLocationData.PITCH));
	}

	public EnumDataModule grabSpecialLocationData()
	{
		return this.specialLocationData;
	}

	/**
	 * Enum defining the data being held in this object.
	 */
	public static enum SpecialLocationData
	{
		/**
		 * String: Name of the World.
		 */
		WORLD,

		/**
		 * Double: X coordinate.
		 */
		X,

		/**
		 * Double: Y coordinate.
		 */
		Y,

		/**
		 * Double: Z coordinate.
		 */
		Z,

		/**
		 * Float: Pitch value.
		 */
		PITCH,

		/**
		 * Float: Yaw value.
		 */
		YAW,

		/**
		 * String: Internal name of the location.
		 */
		NAME
	}
}
