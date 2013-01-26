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
	public static ArrayList<DivineLocation> getShrines(int charID)
	{
		ArrayList<DivineLocation> shrines = new ArrayList<DivineLocation>();
		
		for(Entry<Integer, HashMap<String, Object>> divineBlock : DDataUtil.getAllBlockData().entrySet())
		{			
			if(divineBlock.getValue().get("shrine_owner").equals(charID))
			{
				shrines.add((DivineLocation) divineBlock.getValue().get("shrine_location"));
			}
		}
		return shrines;

	}
	
	/*
	 *  getAllShrines() : Returns an ArrayList<Location> of (Player)player's Shrines.
	 */
	public static ArrayList<DivineLocation> getAllShrines()
	{		
		ArrayList<DivineLocation> shrines = new ArrayList<DivineLocation>();
		
		for(Entry<Integer, HashMap<String, Object>> divineBlock : DDataUtil.getAllBlockData().entrySet())
		{
			shrines.add((DivineLocation) divineBlock.getValue().get("shrine_location"));
		}
		
		return shrines;
	}
	
	/*
	 *  getOwnerOfShrine() : Returns the owner of the shrine at (Location)location.
	 */
	public static int getOwnerOfShrine(Location location)
	{		
		for(Entry<Integer, HashMap<String, Object>> divineBlock : DDataUtil.getAllBlockData().entrySet())
		{			
			if(divineBlock.getValue().get("shrine_location").equals(new DivineLocation(location)))
			{
				return DObjUtil.toInteger(divineBlock.getValue().get("shrine_owner"));
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
	public static ArrayList<DivineLocation> getAllAltars()
	{		
		return null; //TODO
	}
	
	/* ---------------------------------------------------
	 * Begin Overall DivineBlock Methods
	 * ---------------------------------------------------
	 *
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
	 
	/*
	 *  getAllDivineBlocks() : Returns an arraylist of all divine block locations.
	 */
	public static ArrayList<DivineLocation> getAllDivineBlocks()
	{
		ArrayList<DivineLocation> divineBlocks = new ArrayList<DivineLocation>();
		
		// Get all Shrines
		if(getAllShrines() != null)
		{
			for(DivineLocation shrine : getAllShrines())
			{
				divineBlocks.add(shrine);
			}
		}
			
		// Get all Altars
		if(getAllAltars() != null)
		{
			for(DivineLocation altar : getAllAltars())
			{
				divineBlocks.add(altar);
			}
		}
		
		return divineBlocks;
	}
}