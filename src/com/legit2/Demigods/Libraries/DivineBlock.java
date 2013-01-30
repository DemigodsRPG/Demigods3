package com.legit2.Demigods.Libraries;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class DivineBlock implements Serializable
{
	private static final long serialVersionUID = 8201132625259394712L;

	int id, pid;
	String type, deity, world;
	double X,Y,Z;
	boolean perm;
	
	public DivineBlock(int blockID, int parentID, boolean blockPerm, String blockType, String blockDeity, Location location)
	{
		id = blockID;
		pid = parentID;
		perm = blockPerm;
		type = blockType;
		deity = blockDeity;
		X = location.getX();
		Y = location.getY();
		Z = location.getZ();
		world = location.getWorld().getName();
	}
	
	public int getID()
	{
		return id;
	}
	
	public int getParent()
	{
		return pid;
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getDeity()
	{
		return deity;
	}
	
	public String getWorld()
	{
		return world;
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
		return new Location(w, X, Y, Z);
	}
	
	public Location toLocation()
	{
		return new Location(Bukkit.getServer().getWorld(world), X, Y, Z);
	}
	
	public boolean equals(DivineBlock other)
	{
		return ((X == other.getX()) && (Y == other.getY()) && (Z == other.getZ()) && world.equals(other.getWorld()));
	}
}