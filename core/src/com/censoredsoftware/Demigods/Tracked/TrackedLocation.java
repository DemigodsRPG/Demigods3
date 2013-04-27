package com.censoredsoftware.Demigods.Tracked;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.censoredsoftware.Modules.Data.StringDataModule;

public class TrackedLocation
{
	private StringDataModule specialLocationData;

	public TrackedLocation(String world, double X, double Y, double Z, float pitch, float yaw, String name)
	{
		specialLocationData = new StringDataModule();
		specialLocationData.saveData("WORLD", world);
		specialLocationData.saveData("X", X);
		specialLocationData.saveData("Y", Y);
		specialLocationData.saveData("Z", Z);
		specialLocationData.saveData("PITCH", pitch);
		specialLocationData.saveData("YAW", yaw);
		if(name != null) specialLocationData.saveData("NAME", name);
	}

	public TrackedLocation(Location location, String name)
	{
		specialLocationData = new StringDataModule();
		specialLocationData.saveData("WORLD", location.getWorld().getName());
		specialLocationData.saveData("X", location.getX());
		specialLocationData.saveData("Y", location.getY());
		specialLocationData.saveData("Z", location.getZ());
		specialLocationData.saveData("PITCH", location.getPitch());
		specialLocationData.saveData("YAW", location.getYaw());
		if(name != null) specialLocationData.saveData("NAME", name);
	}

	public boolean hasName()
	{
		return specialLocationData.containsKey("NAME");
	}

	public String getName()
	{
		return specialLocationData.getDataString("NAME");
	}

	public synchronized void setName(String name)
	{
		specialLocationData.saveData("NAME", name);
	}

	public Location toLocation() throws NullPointerException
	{
		return new Location(Bukkit.getServer().getWorld(specialLocationData.getDataString("WORLD")), specialLocationData.getDataDouble("X"), specialLocationData.getDataDouble("Y"), specialLocationData.getDataDouble("Z"), specialLocationData.getDataFloat("YAW"), specialLocationData.getDataFloat("PITCH"));
	}

	public StringDataModule grabSpecialLocationData()
	{
		return this.specialLocationData;
	}
}
