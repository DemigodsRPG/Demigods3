package com.censoredsoftware.Demigods.Engine.Utility;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.censoredsoftware.Demigods.Engine.Structure.StructureBlockData;
import com.censoredsoftware.Demigods.Engine.Structure.StructureSchematic;
import com.google.common.collect.Sets;

public class GenerationUtility
{
	/**
	 * Generates a random location with the center being <code>reference</code>.
	 * Must be at least <code>min</code> blocks from the center and no more than <code>max</code> blocks away.
	 * 
	 * @param reference the location used as the center for reference.
	 * @param min the minimum number of blocks away.
	 * @param max the maximum number of blocks away.
	 * @return the random location generated.
	 */
	public static Location randomLocation(Location reference, int min, int max)
	{
		Location location = reference.clone();
		double randX = MiscUtility.generateIntRange(min, max);
		double randZ = MiscUtility.generateIntRange(min, max);
		location.add(randX, 0, randZ);
		double highestY = location.clone().getWorld().getHighestBlockYAt(location);
		location.setY(highestY);
		return location;
	}

	/**
	 * Returns a random location within the <code>chunk</code> passed in.
	 * 
	 * @param chunk the chunk that we will obtain the location from.
	 * @return the random location generated.
	 */
	public static Location randomChunkLocation(Chunk chunk)
	{
		Location reference = chunk.getBlock(MiscUtility.generateIntRange(1, 16), 64, MiscUtility.generateIntRange(1, 16)).getLocation();
		double locX = reference.getX();
		double locY = chunk.getWorld().getHighestBlockYAt(reference);
		double locZ = reference.getZ();
		return new Location(chunk.getWorld(), locX, locY, locZ);
	}

	/**
	 * Strictly checks the <code>reference</code> location to validate if the area is safe
	 * for automated generation.
	 * 
	 * @param reference the location to be checked
	 * @param area how big of an area (in blocks) to validate
	 * @return Boolean
	 */
	public static boolean canGenerateStrict(Location reference, int area)
	{
		Location location = reference.clone();
		location.subtract(0, 1, 0);
		location.add((area / 3), 0, (area / 2));

		// Check ground
		for(int i = 0; i < area; i++)
		{
			if(!location.getBlock().getType().isSolid()) return false;
			location.subtract(1, 0, 0);
		}

		// Check ground adjacent
		for(int i = 0; i < area; i++)
		{
			if(!location.getBlock().getType().isSolid()) return false;
			location.subtract(0, 0, 1);
		}

		// Check ground adjacent again
		for(int i = 0; i < area; i++)
		{
			if(!location.getBlock().getType().isSolid()) return false;
			location.add(1, 0, 0);
		}

		location.add(0, 1, 0);

		// Check air diagonally
		for(int i = 0; i < area + 1; i++)
		{
			if(!location.getBlock().getType().isTransparent()) return false;
			location.add(0, 1, 1);
			location.subtract(1, 0, 0);
		}

		return true;
	}

	/**
	 * Generates a mound at the given <code>location</code> with a top radius of <code>radius</code>based on the predominant material found. Used in situations
	 * such as generating structures on hills.
	 * 
	 * @param reference the center location to generate at.
	 * @param radius the radii that the top of the mound will be.
	 */
	public static void generateMound(Location reference, int radius)
	{
		Location location = reference.clone();

		// Get the blocks in the area
		// TODO
	}

	/**
	 * Returns a set of blocks in a radius of <code>radius</code> at the provided <code>location</code>.
	 * 
	 * @param location the center location to get the blocks from.
	 * @param radius the radius around the center block from which to get the blocks.
	 * @return Set<Block>
	 */
	public static Set<Block> getBlocks(Location location, int radius)
	{
		// Define variables
		Set<Block> blocks = Sets.newHashSet();
		blocks.add(location.getBlock());

		for(int x = 0; x <= radius; x++)
		{
			blocks.add(location.add(x, 0, x).getBlock());
		}

		return blocks;
	}

	public static void testStructure(Location target)
	{
		StructureSchematic sponge = new StructureSchematic(target, 0, 0, 0, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.SPONGE));
			}
		});
		StructureSchematic bottomRight = new StructureSchematic(target, 0, 0, -1, 1, 3, 0, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.SMOOTH_BRICK));
			}
		});
		StructureSchematic bottomLeft = new StructureSchematic(target, 0, 0, 1, 1, 3, 2, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.SMOOTH_BRICK));
			}
		});
		StructureSchematic bottomFront = new StructureSchematic(target, 1, 0, 0, 2, 3, 1, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.SMOOTH_BRICK));
			}
		});
		StructureSchematic bottomBack = new StructureSchematic(target, -1, 0, 0, 0, 3, 1, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.SMOOTH_BRICK));
			}
		});
		StructureSchematic piston = new StructureSchematic(target, 0, 4, 0, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.PISTON_STICKY_BASE));
			}
		});
		StructureSchematic redstoneBlock = new StructureSchematic(target, 0, 3, 0, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.REDSTONE_BLOCK));
			}
		});
		StructureSchematic lanternRight = new StructureSchematic(target, 0, 3, -1, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.REDSTONE_LAMP_OFF));
			}
		});
		StructureSchematic lanternLeft = new StructureSchematic(target, 0, 3, 1, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.REDSTONE_LAMP_OFF));
			}
		});
		StructureSchematic lanternFront = new StructureSchematic(target, 1, 3, 0, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.REDSTONE_LAMP_OFF));
			}
		});
		StructureSchematic lanternBack = new StructureSchematic(target, -1, 3, 0, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.REDSTONE_LAMP_OFF));
			}
		});
		StructureSchematic topRight = new StructureSchematic(target, 0, 4, -1, 1, 6, 0, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.SMOOTH_BRICK));
			}
		});
		StructureSchematic topLeft = new StructureSchematic(target, 0, 4, 1, 1, 6, 2, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.SMOOTH_BRICK));
			}
		});
		StructureSchematic topFront = new StructureSchematic(target, 1, 4, 0, 2, 6, 1, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.SMOOTH_BRICK));
			}
		});
		StructureSchematic topBack = new StructureSchematic(target, -1, 4, 0, 0, 6, 1, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.SMOOTH_BRICK));
			}
		});
		StructureSchematic lightSensor = new StructureSchematic(target, 0, 5, 0, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.DAYLIGHT_DETECTOR));
			}
		});
		StructureSchematic vineRight = new StructureSchematic(target, -1, 5, 1, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.VINE, (byte) 4, 40));
				add(new StructureBlockData(Material.AIR, (byte) 0, 60));
			}
		});
		StructureSchematic vineLeft = new StructureSchematic(target, 1, 5, -1, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.VINE, (byte) 1, 40));
				add(new StructureBlockData(Material.AIR, (byte) 0, 60));
			}
		});
		StructureSchematic vineFront = new StructureSchematic(target, 1, 5, 1, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.VINE, (byte) 4, 40));
				add(new StructureBlockData(Material.AIR, (byte) 0, 60));
			}
		});
		StructureSchematic vineBack = new StructureSchematic(target, -1, 5, -1, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.VINE, (byte) 1, 40));
				add(new StructureBlockData(Material.AIR, (byte) 0, 60));
			}
		});

		sponge.generate();
		bottomRight.generate();
		bottomLeft.generate();
		bottomFront.generate();
		bottomBack.generate();
		piston.generate();
		redstoneBlock.generate();
		lanternRight.generate();
		lanternLeft.generate();
		lanternFront.generate();
		lanternBack.generate();
		topRight.generate();
		topLeft.generate();
		topFront.generate();
		topBack.generate();
		lightSensor.generate();
		vineRight.generate();
		vineLeft.generate();
		vineFront.generate();
		vineBack.generate();
	}

	public static void spiral(Location target)
	{
		int X, Z;
		int N = 61;

		// create N-by-N array of integers 1 through N
		int[][] a = new int[N][N];
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				a[i][j] = 1 + N * i + j;

		// spiral
		for(int i = N - 1, j = 0; i > 0; i--, j++)
		{
			for(int k = j; k < i; k++)
			{
				X = j;
				Z = k;
				spiralGenerate(target, X, i, Z);
			}
			for(int k = j; k < i; k++)
			{
				X = k;
				Z = i;
				spiralGenerate(target, X, i, Z);
			}
			for(int k = i; k > j; k--)
			{
				X = i;
				Z = k;
				spiralGenerate(target, X, i, Z);
			}
			for(int k = i; k > j; k--)
			{
				X = k;
				Z = j;
				spiralGenerate(target, X, i, Z);
			}
			for(int x = 0; x < N; x++)
			{
				for(int z = 0; z < N; z++)
				{
					spiralGenerate(target, x, N, z);
				}
			}
		}

		// special case for middle element if N is odd
		if(N % 2 == 1)
		{
			X = (N - 1) / 2;
			Z = (N - 1) / 2;
			spiralGenerate(target, X, 1, Z);
		}
	}

	public static void spiralGenerate(Location target, int X, int Y, int Z)
	{
		new StructureSchematic(target, X, Y, Z, new HashSet<StructureBlockData>()
		{
			{
				add(new StructureBlockData(Material.STATIONARY_WATER));
			}
		}).generate();
	}
}
