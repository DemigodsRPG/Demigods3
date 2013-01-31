package com.legit2.Demigods.Libraries;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DivineBlock implements Serializable
{
	private static final long serialVersionUID = 8201132625259394712L;

	int id, pid;
	String type, deity, world;
	double X,Y,Z;
	boolean perm;
	int material;
	byte matbyte;
	
	public DivineBlock(Location location, int blockID, int parentID, boolean blockPerm, String blockType, String blockDeity)
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
		material = 0;
		matbyte = 0;
	}
	
	public DivineBlock(Location location, int blockID, int parentID, boolean blockPerm, String blockType, String blockDeity, int materialID)
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
		material = materialID;
		matbyte = 0;
	}
	
	public DivineBlock(Location location, int blockID, int parentID, boolean blockPerm, String blockType, String blockDeity, int materialID, byte byteData)
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
		material = materialID;
		matbyte = byteData;
	}
	
	public int getID()
	{
		return id;
	}
	
	public int getParent()
	{
		return pid;
	}
	
	public int getMaterial()
	{
		return material;
	}
	
	public byte getMaterialByteData()
	{
		return matbyte;
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
	
	public Location getLocation()
	{
		return new Location(Bukkit.getServer().getWorld(world), X, Y, Z);
	}
	
	public boolean isPermanent()
	{
		return perm;
	}
	
	public boolean equals(DivineBlock other)
	{
		return ((X == other.getX()) && (Y == other.getY()) && (Z == other.getZ()) && world.equals(other.getWorld()));
	}
}