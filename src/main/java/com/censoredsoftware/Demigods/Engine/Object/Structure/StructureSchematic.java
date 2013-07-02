package com.censoredsoftware.Demigods.Engine.Object.Structure;

import com.censoredsoftware.Demigods.Engine.Utility.MiscUtility;
import com.google.common.collect.Lists;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StructureSchematic
{
	private int X, Y, Z, XX, YY, ZZ;
	private boolean cuboid;
	private Set<StructureBlockData> blockData;

	/**
	 * Constructor for a StructureSchematic (non-cuboid).
	 * 
	 * @param X The relative X coordinate of the schematic from the reference location.
	 * @param Y The relative Y coordinate of the schematic from the reference location.
	 * @param Z The relative Z coordinate of the schematic from the reference location.
	 * @param blockData The StructureBlockData objects of this schematic.
	 */
	public StructureSchematic(int X, int Y, int Z, Set<StructureBlockData> blockData)
	{
		this.X = this.XX = X;
		this.Y = this.YY = Y;
		this.Z = this.ZZ = Z;
		this.cuboid = false;
		this.blockData = blockData;
	}

	/**
	 * Constructor for a StructureSchematic (cuboid).
	 * 
	 * @param X The relative X coordinate of the schematic from the reference location.
	 * @param Y The relative Y coordinate of the schematic from the reference location.
	 * @param Z The relative Z coordinate of the schematic from the reference location.
	 * @param XX The second relative X coordinate of the schematic from the reference location, creating a cuboid.
	 * @param YY The second relative Y coordinate of the schematic from the reference location, creating a cuboid.
	 * @param ZZ The second relative Z coordinate of the schematic from the reference location, creating a cuboid.
	 * @param blockData The StructureBlockData objects of this schematic.
	 */
	public StructureSchematic(int X, int Y, int Z, int XX, int YY, int ZZ, Set<StructureBlockData> blockData)
	{
		this.X = X;
		this.Y = Y;
		this.Z = Z;
		this.XX = XX;
		this.YY = YY;
		this.ZZ = ZZ;
		this.cuboid = true;
		this.blockData = blockData;
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
		List<StructureBlockData> blockData = Lists.newArrayList();
		for(StructureBlockData block : this.blockData)
		{
			if(block.getOdds() == 0) continue;
			if(block.getOdds() == 100) return block;
			for(int i = 0; i < block.getOdds(); i++)
			{
				blockData.add(block);
			}
		}
		return blockData.get(MiscUtility.generateIntRange(0, blockData.size()));
	}

	/**
	 * Get a relative location, based on the <code>X</code>, <code>Y</code>, <code>Z</code> coordinates relative to the object's central location.
	 * 
	 * @param X Relative X coordinate.
	 * @param Y Relative Y coordinate.
	 * @param Z Relative Z coordinate.
	 * @return New relative location.
	 */
	public Location getLocation(Location reference, int X, int Y, int Z)
	{
		return reference.clone().add(X, Y, Z);
	}

	/**
	 * Get the block locations in this object.
	 * 
	 * @return A set of locations.
	 */
	public Set<Location> getBlockLocations(final Location reference)
	{
		if(cuboid)
		{
			final int X = this.X < this.XX ? this.X : this.XX, XX = this.X > this.XX ? this.X : this.XX;
			final int Y = this.Y < this.YY ? this.Y : this.YY, YY = this.Y > this.YY ? this.Y : this.YY;
			final int Z = this.Z < this.ZZ ? this.Z : this.ZZ, ZZ = this.Z > this.ZZ ? this.Z : this.ZZ;

			return new HashSet<Location>()
			{
				{
					for(int i = X; i < XX; i++)
					{
						for(int o = Y; o < YY; o++)
						{
							for(int p = Z; p < ZZ; p++)
							{
								add(getLocation(reference, i, o, p));
							}
						}
					}
				}
			};
		}
		else
		{
			final int X = this.X, Y = this.Y, Z = this.Z;
			return new HashSet<Location>()
			{
				{
					add(getLocation(reference, X, Y, Z));
				}
			};
		}
	}

	public void generate(Location reference)
	{
		for(Location location : getBlockLocations(reference))
		{
			StructureBlockData data = getStructureBlockData();
			location.getBlock().setTypeIdAndData(data.getMaterial().getId(), data.getData(), false);
		}
	}
}
