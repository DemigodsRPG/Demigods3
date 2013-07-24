package com.censoredsoftware.Demigods.Engine.Object.Structure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.Utility.MiscUtility;
import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.Ranges;

public class StructureCuboid
{
	private int X, Y, Z, XX, YY, ZZ;
	private int eX, eY, eZ, eXX, eYY, eZZ;
	private boolean cuboid;
	private boolean exclude;
	private boolean excludeCuboid;
	private List<StructureBlockData> blockData;

	/**
	 * Constructor for a StructureCuboid (non-cuboid).
	 * 
	 * @param X The relative X coordinate of the schematic from the reference location.
	 * @param Y The relative Y coordinate of the schematic from the reference location.
	 * @param Z The relative Z coordinate of the schematic from the reference location.
	 * @param blockData The StructureBlockData objects of this schematic.
	 */
	public StructureCuboid(int X, int Y, int Z, List<StructureBlockData> blockData)
	{
		if(blockData.size() == 0 || blockData.size() > 10) throw new IllegalArgumentException("Incorrect block data list size.");
		this.X = this.XX = X;
		this.Y = this.YY = Y;
		this.Z = this.ZZ = Z;
		this.cuboid = false;
		this.exclude = false;
		this.excludeCuboid = false;
		this.blockData = blockData;
	}

	/**
	 * Constructor for a StructureCuboid (cuboid).
	 * 
	 * @param X The relative X coordinate of the schematic from the reference location.
	 * @param Y The relative Y coordinate of the schematic from the reference location.
	 * @param Z The relative Z coordinate of the schematic from the reference location.
	 * @param XX The second relative X coordinate of the schematic from the reference location, creating a cuboid.
	 * @param YY The second relative Y coordinate of the schematic from the reference location, creating a cuboid.
	 * @param ZZ The second relative Z coordinate of the schematic from the reference location, creating a cuboid.
	 * @param blockData The StructureBlockData objects of this schematic.
	 */
	public StructureCuboid(int X, int Y, int Z, int XX, int YY, int ZZ, List<StructureBlockData> blockData)
	{
		if(blockData.size() == 0 || blockData.size() > 10) throw new IllegalArgumentException("Incorrect block data list size.");
		this.X = X;
		this.Y = Y;
		this.Z = Z;
		this.XX = XX;
		this.YY = YY;
		this.ZZ = ZZ;
		this.cuboid = true;
		this.exclude = false;
		this.excludeCuboid = false;
		this.blockData = blockData;
	}

	/**
	 * Excluding for a StructureCuboid (non-cuboid).
	 * 
	 * @param X The relative X coordinate of the schematic from the reference location.
	 * @param Y The relative Y coordinate of the schematic from the reference location.
	 * @param Z The relative Z coordinate of the schematic from the reference location.
	 * @return This schematic.
	 */
	public StructureCuboid exclude(int X, int Y, int Z)
	{
		this.eX = this.eXX = X;
		this.eY = this.eYY = Y;
		this.eZ = this.eZZ = Z;
		this.exclude = true;
		return this;
	}

	/**
	 * Excluding for a StructureCuboid (cuboid).
	 * 
	 * @param X The relative X coordinate of the schematic from the reference location.
	 * @param Y The relative Y coordinate of the schematic from the reference location.
	 * @param Z The relative Z coordinate of the schematic from the reference location.
	 * @param XX The second relative X coordinate of the schematic from the reference location, creating a cuboid.
	 * @param YY The second relative Y coordinate of the schematic from the reference location, creating a cuboid.
	 * @param ZZ The second relative Z coordinate of the schematic from the reference location, creating a cuboid.
	 * @return This schematic.
	 */
	public StructureCuboid exclude(int X, int Y, int Z, int XX, int YY, int ZZ)
	{
		this.eX = X;
		this.eY = Y;
		this.eZ = Z;
		this.eXX = XX;
		this.eYY = YY;
		this.eZZ = ZZ;
		this.exclude = true;
		this.excludeCuboid = true;
		return this;
	}

	/**
	 * Get the material of the object (a random material is chosen based on the configured odds).
	 * 
	 * TODO This method needs work, I'm not sure this is the more efficient way to do what we want.
	 * 
	 * @return A material.
	 */
	public StructureBlockData getStructureBlockData()
	{
		if(blockData.size() == 1) return blockData.get(0);
		return new ArrayList<StructureBlockData>(10)
		{
			{
				for(StructureBlockData block : blockData)
					for(int i = 0; i < block.getOdds(); i++)
						add(block);
			}
		}.get(MiscUtility.generateIntRange(0, 9));
	}

	/**
	 * Get the block locations in this object.
	 * 
	 * @param reference The reference location.
	 * @return A set of locations.
	 */
	public Set<Location> getBlockLocations(final Location reference)
	{
		return new HashSet<Location>()
		{
			{
				if(cuboid) addAll(rangeLoop(reference, X, XX, Y, YY, Z, ZZ));
				else add(getLocation(reference, X, Y, Z));
				if(exclude)
				{
					if(excludeCuboid) removeAll(rangeLoop(reference, eX, eXX, eY, eYY, eZ, eZZ));
					else remove(getLocation(reference, eX, eY, eZ));
				}
			}
		};
	}

	/**
	 * Generate this schematic.
	 * 
	 * @param reference The reference Location.
	 */
	public void generate(Location reference)
	{
		for(Location location : getBlockLocations(reference))
		{
			StructureBlockData data = getStructureBlockData();
			location.getBlock().setTypeIdAndData(data.getMaterial().getId(), data.getData(), false);
		}
	}

	/**
	 * Get a relative location, based on the <code>X</code>, <code>Y</code>, <code>Z</code> coordinates relative to the object's central location.
	 * 
	 * @param X Relative X coordinate.
	 * @param Y Relative Y coordinate.
	 * @param Z Relative Z coordinate.
	 * @return New relative location.
	 */
	public static Location getLocation(Location reference, int X, int Y, int Z)
	{
		return reference.clone().add(X, Y, Z);
	}

	/**
	 * Get a cuboid selection as a HashSet.
	 * 
	 * @param reference The reference location.
	 * @param X The relative X coordinate.
	 * @param XX The second relative X coordinate.
	 * @param Y The relative Y coordinate.
	 * @param YY The second relative Y coordinate.
	 * @param Z The relative Z coordinate.
	 * @param ZZ The second relative Z coordinate.
	 * @return The HashSet collection of a cuboid selection.
	 */
	public static Set<Location> rangeLoop(final Location reference, final int X, final int XX, final int Y, final int YY, final int Z, final int ZZ)
	{
		return new HashSet<Location>()
		{
			{
				for(int x : Ranges.closed(X < XX ? X : XX, X < XX ? XX : X).asSet(DiscreteDomains.integers()))
					for(int y : Ranges.closed(Y < YY ? Y : YY, Y < YY ? YY : Y).asSet(DiscreteDomains.integers()))
						for(int z : Ranges.closed(Z < ZZ ? Z : ZZ, Z < ZZ ? ZZ : Z).asSet(DiscreteDomains.integers()))
							add(getLocation(reference, x, y, z));
			}
		};
	}

}
