package com.censoredsoftware.Demigods.Engine.Object.Structure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.Utility.MiscUtility;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ranges;

public class StructureSchematic
{
	private int X, Y, Z, XX, YY, ZZ;
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
		return new HashSet<Location>()
		{
			{
				for(int x : Ranges.closed(X < XX ? X : XX, X < XX ? XX : X).asSet(integers()))
				{
					for(int y : Ranges.closed(Y < YY ? Y : YY, Y < YY ? YY : Y).asSet(integers()))
					{
						for(int z : Ranges.closed(Z < ZZ ? Z : ZZ, Z < ZZ ? ZZ : Z).asSet(integers()))
						{
							add(getLocation(reference, x, y, z));
						}
					}
				}
			}
		};
	}

	public void generate(Location reference)
	{
		for(Location location : getBlockLocations(reference))
		{
			StructureBlockData data = getStructureBlockData();
			location.getBlock().setTypeIdAndData(data.getMaterial().getId(), data.getData(), false);
		}
	}

	public static DiscreteDomain<Integer> integers()
	{
		return new DiscreteDomain<Integer>()
		{
			@Override
			public Integer next(Integer integer)
			{
				return integer + 1;
			}

			@Override
			public Integer previous(Integer integer)
			{
				return integer - 1;
			}

			@Override
			public long distance(Integer integer, Integer integer2)
			{
				return Math.abs(integer - integer2);
			}
		};
	}
}
