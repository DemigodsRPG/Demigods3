package com.censoredsoftware.Demigods.API;

import java.util.List;
import java.util.Set;

import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.Block.Altar;
import com.censoredsoftware.Demigods.Engine.Block.Shrine;
import com.censoredsoftware.Demigods.Engine.Tracked.ComparableLocation;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedBlock;
import com.google.common.collect.Lists;

public class BlockAPI
{
	/**
	 * Grab the TrackedBlock from the data with id <code>id</code>.
	 * 
	 * @param id The ID of the block.
	 * @return TrackedBlock.
	 */
	public static TrackedBlock getBlock(Long id)
	{
		return TrackedBlock.load(id);
	}

	public static Set<TrackedBlock> getAllBlocks()
	{
		return TrackedBlock.loadAll();
	}

	/**
	 * Returns all Altars as an ArrayList.
	 * 
	 * @return the ArrayList of Altars.
	 */
	public static Set<Altar> getAllAltars()
	{
		return Altar.loadAll();
	}

	/**
	 * Returns all Shrines as an ArrayList.
	 * 
	 * @return the ArrayList of Shrines.
	 */
	public static List<Shrine> getAllShrines()
	{ // TODO Convert shrines.
	  // ArrayList<Shrine> shrines = new ArrayList<Shrine>();
	  // for(int key : DemigodsData.shrineData.listKeys())
	  // {
	  // shrines.add((Shrine) DemigodsData.shrineData.getDataObject(key));
	  // }
		return Lists.newArrayList();
	}

	/**
	 * Returns true if the block at the passed in <code>location</code> is protected.
	 * 
	 * @param location the location to check.
	 * @return true/false depending on if the block is protected or not.
	 */
	public static boolean isProtected(Location location)
	{
		// TODO Shrines. return !(getAllAltars() == null && getAllShrines() == null) && (isAltar(location) || isShrine(location));
		return isAltar(location);
	}

	/**
	 * Returns true if the block at the passed in <code>location</code> is an Altar.
	 * 
	 * @param location the location to check.
	 * @return true/false depending on if the block is an Altar or not.
	 */
	public static boolean isAltar(Location location)
	{
		return getAltar(location) != null;
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
		final Set<Altar> altars = getAllAltars();
		for(Altar altar : altars)
		{
			Location altarLocation = altar.getLocation();
			if(!altarLocation.getChunk().isLoaded() || !altarLocation.getWorld().equals(location.getWorld()) || altarLocation.distance(location) > 7) continue;
			if(altar.getBlocks().contains(new ComparableLocation(location))) return altar;
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
		final Set<Altar> altars = getAllAltars();
		if(altars == null) return false;

		for(Altar altar : altars)
		{
			Location altarLocation = altar.getLocation();
			if(!altarLocation.getWorld().equals(location.getWorld())) continue;
			if(altarLocation.distance(location) <= blocks) return true;
		}
		return false;
	}
}
