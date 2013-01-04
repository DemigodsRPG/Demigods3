package com.legit2.Demigods.Serializable;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.World;

import com.legit2.Demigods.Demigods;

public class WriteLocation implements Serializable 
{

	private static final long serialVersionUID = 8201132625259394712L;

	int X,Y,Z;
	String WORLD;
	
	public WriteLocation(String world, int x, int y, int z)
	{
		X = x;
		Y = y;
		Z = z;
		WORLD = world;
	}
	
	public String getWorld()
	{
		return WORLD;
	}
	
	public int getX()
	{
		return X;
	}
	
	public int getY()
	{
		return Y;
	}
	
	public int getZ()
	{
		return Z;
	}
	
	public Location toLocationNewWorld(World w)
	{
		return new Location(w, X,Y,Z);
	}
	
	public Location toLocation(Demigods instance) 
	{
		return new Location(instance.getServer().getWorld(WORLD), X, Y, Z);
	}
	
	public boolean equalsApprox(WriteLocation other)
	{
		return ((X == other.getX()) && (Y == other.getY()) && (Z == other.getZ()) && WORLD.equals(other.getWorld()));
	}
}