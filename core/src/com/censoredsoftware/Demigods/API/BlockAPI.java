package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.Block.Altar;
import com.censoredsoftware.Demigods.Engine.Block.Shrine;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedBlock;

public class BlockAPI
{
	/**
	 * Grab the TrackedBlock from the data with id <code>id</code>.
	 * 
	 * @param id The ID of the block.
	 * @return TrackedBlock.
	 */
	public static TrackedBlock getBlock(int id)
	{
		return (TrackedBlock) DemigodsData.trackedBlockData.getDataObject(id);
	}

	public static List<TrackedBlock> getBlocks()
	{
		List<TrackedBlock> blocks = new ArrayList<TrackedBlock>();
		for(int charID : DemigodsData.trackedBlockData.listKeys())
		{
			TrackedBlock block = (TrackedBlock) DemigodsData.trackedBlockData.getDataObject(charID);
			blocks.add(block);
		}
		return blocks;
	}

	/**
	 * Returns all protected blocks as an ArrayList.
	 * 
	 * @return the ArrayList of locations.
	 */
	public static ArrayList<Location> getAllBlocks()
	{
		ArrayList<Location> locations = new ArrayList<Location>();

		for(Altar altar : getAllAltars())
			locations.add(altar.getLocation());
		for(Shrine shrine : getAllShrines())
			locations.add(shrine.getLocation());

		return locations;
	}

	/**
	 * Returns all Altars as an ArrayList.
	 * 
	 * @return the ArrayList of Altars.
	 */
	public static ArrayList<Altar> getAllAltars()
	{
		ArrayList<Altar> altars = new ArrayList<Altar>();
		for(int key : DemigodsData.altarData.listKeys())
		{
			altars.add((Altar) DemigodsData.altarData.getDataObject(key));
		}
		return altars;
	}

	/**
	 * Returns all Shrines as an ArrayList.
	 * 
	 * @return the ArrayList of Shrines.
	 */
	public static ArrayList<Shrine> getAllShrines()
	{
		ArrayList<Shrine> shrines = new ArrayList<Shrine>();
		for(int key : DemigodsData.shrineData.listKeys())
		{
			shrines.add((Shrine) DemigodsData.shrineData.getDataObject(key));
		}
		return shrines;
	}

	/**
	 * Returns true if the block at the passed in <code>location</code> is protected.
	 * 
	 * @param location the location to check.
	 * @return true/false depending on if the block is protected or not.
	 */
	public static boolean isProtected(Location location)
	{
		return !(getAllAltars() == null && getAllShrines() == null) && (isAltar(location) || isShrine(location));
	}

	/**
	 * Returns true if the block at the passed in <code>location</code> is an Altar.
	 * 
	 * @param location the location to check.
	 * @return true/false depending on if the block is an Altar or not.
	 */
	public static boolean isAltar(Location location)
	{
		if(getAllAltars() == null) return false;

		for(Altar altar : getAllAltars())
		{
			if(altar.locationMatches(location)) return true;
		}
		return false;
	}

	/**
	 * Returns true if the block at the passed in <code>location</code> is a Shrine.
	 * 
	 * @param location the location to check.
	 * @return true/false depending on if the block is an Shrine or not.
	 */
	public static boolean isShrine(Location location)
	{
		if(getAllShrines() == null) return false;

		for(Shrine altar : getAllShrines())
		{
			if(altar.getLocation().equals(location)) return true;
		}
		return false;
	}

	/**
	 * Returns the Altar at the <code>location</code>.
	 * 
	 * @param location the location to check.
	 * @return the Altar at <code>location</code>.
	 */
	public static Altar getAltar(Location location)
	{
		if(getAllAltars() == null) return null;

		for(Altar altar : getAllAltars())
		{
			if(altar.locationMatches(location)) return altar;
		}
		return null;
	}

	/**
	 * Returns the Shrine at the <code>location</code>.
	 * 
	 * @param location the location to check.
	 * @return the Shrine at <code>location</code>.
	 */
	public static Shrine getShrine(Location location)
	{
		if(getAllShrines() == null) return null;

		for(Shrine shrine : getAllShrines())
		{
			if(shrine.getLocation().equals(location)) return shrine;
		}
		return null;
	}

	/**
	 * Checks the <code>reference</code> location to validate if the area is safe
	 * for automated generation.
	 * 
	 * @param reference the location to be checked
	 * @param area how big of an area (in blocks) to validate
	 * @return based on if the location is safe to generate at
	 */
	public static boolean canGenerateSolid(Location reference, int area)
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
	 * Returns true/false depending on if there is an Altar within <code>blocks</code> of <code>location</code>.
	 * 
	 * @param location the location used as the center to check from.
	 * @param blocks the radius of blocks to check with.
	 * @return
	 */
	public static boolean altarNearby(Location location, int blocks)
	{
		if(getAllAltars() == null) return false;

		for(Altar altar : getAllAltars())
		{
			if(altar.getLocation().distance(location) <= blocks) return true;
		}
		return false;
	}
}
