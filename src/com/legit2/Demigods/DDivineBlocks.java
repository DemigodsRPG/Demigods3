package com.legit2.Demigods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;

import com.legit2.Demigods.Libraries.DivineLocation;
import com.legit2.Demigods.Utilities.DCharUtil;
import com.legit2.Demigods.Utilities.DDataUtil;
import com.legit2.Demigods.Utilities.DObjUtil;

public class DDivineBlocks
{	
	/* ---------------------------------------------------
	 * Begin Shrine-related Methods
	 * ---------------------------------------------------
	 * 
	 *  createShrine() : Creates a shrine at (Location)location.
	 */
	public static void createShrine(int charID, ArrayList<Location> locations)
	{
		int blockID = DObjUtil.generateInt(5);
		
		ArrayList<DivineLocation> shrines = new ArrayList<DivineLocation>();
		for(Location location : locations)
		{
			DivineLocation shrine = new DivineLocation(location);
			shrines.add(shrine);
		}
		
		DDataUtil.saveBlockData(blockID, "block_type", "shrine");
		DDataUtil.saveBlockData(blockID, "block_owner", charID);
		DDataUtil.saveBlockData(blockID, "block_location", shrines);
	}
	
	/*
	 *  removeShrine() : Removes the shrine at (Location)location.
	 */
	public static void removeShrine(Location location)
	{
		int blockID = getID(location);
		DDataUtil.removeAllBlockData(blockID);
	}
	
	/*
	 *  getShrines() : Returns an ArrayList<Location> of (int)charID's Shrines.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Location> getShrines(int charID)
	{
		if(DDataUtil.hasCharData(charID, "char_shrines"))
		{
			return (ArrayList<Location>) DDataUtil.getCharData(charID, "char_shrines");
		}
		else return null;
	}
	
	/*
	 *  getAllShrines() : Returns an ArrayList<Location> of (Player)player's Shrines.
	 */
	public static ArrayList<Location> getAllShrines()
	{		
		ArrayList<Location> shrines = new ArrayList<Location>();
		
		for(int charID : DDataUtil.getAllChars().keySet())
		{
			if(getShrines(charID) != null)
			{
				for(Location shrine : getShrines(charID))
				{
					if(!shrines.contains(shrine)) shrines.add(shrine);
				}
			}
		}
		
		return shrines;
	}
	
	/*
	 *  getOwnerOfShrine() : Returns the owner of the shrine at (Location)location.
	 */
	public static int getOwnerOfShrine(Location location)
	{
		for(int charID : DDataUtil.getAllChars().keySet())
		{
			for(Location knownLoc : getShrines(charID))
			{
				if(knownLoc.equals(location)) return charID;
			}
		}
		return -1;
	}
	
	/*
	 *  getDeityAtShrine() : Returns the deity of the shrine at (Location)location.
	 */
	public static String getDeityAtShrine(Location location)
	{
		int charID = getOwnerOfShrine(location);
		return DCharUtil.getDeity(charID);
	}
	
	/* ---------------------------------------------------
	 * Begin Altar-related Methods
	 * ---------------------------------------------------
	 * 
	 *  createAltar() : Creates an altar at (Location)location.
	 */
	public static void createAltar(Location location)
	{
		// TODO
	}
	
	/*
	 *  removeAltar() : Removes the altar from (Location)location.
	 */
	public static void removeAltar(Location location)
	{
		// TODO
	}
	
	/*
	 *  getAltars() : Returns an ArrayList<Location> the server's Altars.
	 */
	public static ArrayList<Location> getAllAltars()
	{		
		return null; //TODO
	}
	
	/* ---------------------------------------------------
	 * Begin Overall DivineBlock Methods
	 * ---------------------------------------------------
	 * 
	 *  getAllDivineBlocks() : Returns an arraylist of all divine block locations.
	 */
	public static ArrayList<Location> getAllDivineBlocks()
	{
		ArrayList<Location> divineBlocks = new ArrayList<Location>();
		
		// Get all Shrines
		if(getAllShrines() != null)
		{
			for(Location shrine : getAllShrines())
			{
				divineBlocks.add(shrine);
			}
		}
			
		// Get all Altars
		if(getAllAltars() != null)
		{
			for(Location altar : getAllAltars())
			{
				divineBlocks.add(altar);
			}
		}
		
		return divineBlocks;
	}

	/*
	 *  getID() : Returns the (int)blockID for the (Location)location.
	 */
	public static int getID(Location location)
	{
		HashMap<Integer, HashMap<String, Object>> divineBlocks = DDataUtil.getAllBlockData();
		for(Entry<Integer, HashMap<String, Object>> divineBlock : divineBlocks.entrySet())
		{
			// Define character-specific variables
			int blockID = divineBlock.getKey();
			DivineLocation block = new DivineLocation(location);
			
			if(((DivineLocation) divineBlocks.get(blockID).get("block_location")).equals(block)) return blockID;
		}
		return -1;
	}
}