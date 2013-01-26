package com.legit2.Demigods.Libraries;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class DivineLocation implements Serializable
{
	private static final long serialVersionUID = 8201132625259394712L;

	double X,Y,Z;
	String WORLD;
	public DivineLocation(Location location)
	{
		X = location.getX();
		Y = location.getX();
		Z = location.getX();
		WORLD = location.getWorld().getName();
	}
	
	public String getWorld()
	{
		return WORLD;
	}
	
	public double getX()
	{
		return X;
	}
	
	public double getY()
	{
		return Y;
	}
	
	public double getZ()
	{
		return Z;
	}
	
	public Location toLocationNewWorld(World w)
	{
		return new Location(w, X,Y,Z);
	}
	
	public Location toLocation()
	{
		return new Location(Bukkit.getServer().getWorld(WORLD), X, Y, Z);
	}
	
	public boolean equals(DivineLocation other)
	{
		return ((X == other.getX()) && (Y == other.getY()) && (Z == other.getZ()) && WORLD.equals(other.getWorld()));
	}
}