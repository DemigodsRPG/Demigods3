package com.censoredsoftware.Demigods.Engine.Utility;

import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

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
		Set<Block> blocks = Sets.newHashSet();

		blocks.add(location.getBlock());

		for(int i = 0; i <= radius - 1; i++)
		{
			blocks.add(location.getBlock().getRelative(i + (2 * i), 0, 0));
			blocks.add(location.getBlock().getRelative(i - (2 * i), 0, 0));
			blocks.add(location.getBlock().getRelative(0, 0, i + (2 * i)));
			blocks.add(location.getBlock().getRelative(0, 0, i - (2 * i)));

		}

		return blocks;
	}
}
