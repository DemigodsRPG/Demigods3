package com.censoredsoftware.Demigods.Engine.Structure;

import org.bukkit.Material;

public class StructureBlockData
{
	private Material material;
	private byte data;
	private int odds;

	/**
	 * Constructor for BlockData with only Material given.
	 * 
	 * @param material Material of the block.
	 */
	public StructureBlockData(Material material)
	{
		this.material = material;
		this.data = 0;
		this.odds = 100;
	}

	/**
	 * Constructor for BlockData with only Material given and odds given.
	 * 
	 * @param material Material of the block.
	 * @param odds The odds of this object being generated.
	 */
	public StructureBlockData(Material material, int odds)
	{
		this.material = material;
		this.data = 0;
		this.odds = odds;
	}

	/**
	 * Constructor for BlockData with only Material and byte data given.
	 * 
	 * @param material Material of the block.
	 * @param data Byte data of the block.
	 */
	public StructureBlockData(Material material, byte data)
	{
		this.material = material;
		this.data = data;
		this.odds = 100;
	}

	/**
	 * Constructor for BlockData with Material, byte data, and odds given.
	 * 
	 * @param material Material of the block.
	 * @param data Byte data of the block.
	 * @param odds The odds of this object being generated.
	 */
	public StructureBlockData(Material material, byte data, int odds)
	{
		this.material = material;
		this.data = data;
		this.odds = odds;
	}

	/**
	 * Get the Material of this object.
	 * 
	 * @return A Material.
	 */
	public Material getMaterial()
	{
		return this.material;
	}

	/**
	 * Get the byte data of this object.
	 * 
	 * @return Byte data.
	 */
	public byte getData()
	{
		return this.data;
	}

	/**
	 * Get the odds of this object generating.
	 * 
	 * @return Odds (as an integer, out of 100).
	 */
	public int getOdds()
	{
		return this.odds;
	}
}
