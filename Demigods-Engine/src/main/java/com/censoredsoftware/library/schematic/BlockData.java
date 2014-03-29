package com.censoredsoftware.library.schematic;

import com.censoredsoftware.library.exception.BlockDataException;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class BlockData
{
	private Material material;
	private byte data;
	private int odds;
	private boolean physics;

	/**
	 * Constructor for BlockData with only Material given.
	 *
	 * @param material Material of the block.
	 */
	public BlockData(Material material)
	{
		this.material = material;
		this.data = 0;
		this.odds = 100;
		this.physics = false;
	}

	/**
	 * Constructor for BlockData with only Material given.
	 *
	 * @param material Material of the block.
	 */
	public BlockData(Material material, boolean physics)
	{
		this.material = material;
		this.data = 0;
		this.odds = 100;
		this.physics = physics;
	}

	/**
	 * Constructor for BlockData with only Material given and odds given.
	 *
	 * @param material Material of the block.
	 * @param odds     The odds of this object being generated.
	 */
	public BlockData(Material material, int odds)
	{
		if(odds == 0 || odds > 100) throw new BlockDataException();
		this.material = material;
		this.data = 100;
		this.odds = odds;
		this.physics = false;
	}

	/**
	 * Constructor for BlockData with only Material given and odds given.
	 *
	 * @param material Material of the block.
	 * @param odds     The odds of this object being generated.
	 */
	public BlockData(Material material, int odds, boolean physics)
	{
		if(odds == 0 || odds > 100) throw new BlockDataException();
		this.material = material;
		this.data = 100;
		this.odds = odds;
		this.physics = physics;
	}

	/**
	 * Constructor for BlockData with only Material and byte data given.
	 *
	 * @param material Material of the block.
	 * @param data     Byte data of the block.
	 */
	public BlockData(Material material, byte data)
	{
		this.material = material;
		this.data = data;
		this.odds = 100;
		this.physics = false;
	}

	/**
	 * Constructor for BlockData with only Material and byte data given.
	 *
	 * @param material Material of the block.
	 * @param data     Byte data of the block.
	 */
	public BlockData(Material material, byte data, boolean physics)
	{
		this.material = material;
		this.data = data;
		this.odds = 100;
		this.physics = physics;
	}

	/**
	 * Constructor for BlockData with Material, byte data, and odds given.
	 *
	 * @param material Material of the block.
	 * @param data     Byte data of the block.
	 * @param odds     The odds of this object being generated.
	 */
	public BlockData(Material material, byte data, int odds)
	{
		if(odds == 0 || odds > 100) throw new BlockDataException();
		this.material = material;
		this.data = data;
		this.odds = odds;
		this.physics = false;
	}

	/**
	 * Constructor for BlockData with Material, byte data, and odds given.
	 *
	 * @param material Material of the block.
	 * @param data     Byte data of the block.
	 * @param odds     The odds of this object being generated.
	 */
	public BlockData(Material material, byte data, int odds, boolean physics)
	{
		if(odds == 0 || odds > 100) throw new BlockDataException();
		this.material = material;
		this.data = data;
		this.odds = odds;
		this.physics = physics;
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
	 * @return Odds (as an integer, out of 5).
	 */
	public int getOdds()
	{
		return this.odds;
	}

	/**
	 * Get the physics boolean.
	 *
	 * @return If physics should apply on generation.
	 */
	public boolean getPhysics()
	{
		return this.physics;
	}

	public static enum Preset
	{
		STONE_BRICK(new ArrayList<BlockData>(3)
		{
			{
				add(new BlockData(Material.SMOOTH_BRICK, 80));
				add(new BlockData(Material.SMOOTH_BRICK, (byte) 1, 10));
				add(new BlockData(Material.SMOOTH_BRICK, (byte) 2, 10));
			}
		}), SANDY_GRASS(new ArrayList<BlockData>(2)
	{
		{
			add(new BlockData(Material.SAND, 65));
			add(new BlockData(Material.GRASS, 35));
		}
	}), PRETTY_FLOWERS_AND_GRASS(new ArrayList<BlockData>(4)
	{
		{
			add(new BlockData(Material.AIR, 50));
			add(new BlockData(Material.LONG_GRASS, (byte) 1, 35, true));
			add(new BlockData(Material.YELLOW_FLOWER, 9, true));
			add(new BlockData(Material.RED_ROSE, 6, true));
		}
	}), VINE_1(new ArrayList<BlockData>(2)
	{
		{
			add(new BlockData(Material.VINE, (byte) 1, 40));
			add(new BlockData(Material.AIR, 60));
		}
	}), VINE_4(new ArrayList<BlockData>(2)
	{
		{
			add(new BlockData(Material.VINE, (byte) 4, 40));
			add(new BlockData(Material.AIR, 60));
		}
	});

		private List<BlockData> data;

		private Preset(List<BlockData> data)
		{
			this.data = data;
		}

		public List<BlockData> getData()
		{
			return data;
		}
	}
}
