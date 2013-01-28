package com.legit2.Demigods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

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
	public static void createShrine(int charID, Location location)
	{
		int blockID = DObjUtil.generateInt(5);
		DivineLocation blockLoc = new DivineLocation(location);
		DDataUtil.saveBlockData(blockID, "block_type", "shrine");
		DDataUtil.saveBlockData(blockID, "block_parent", charID);
		DDataUtil.saveBlockData(blockID, "block_deity", DCharUtil.getDeity(charID));
		DDataUtil.saveBlockData(blockID, "block_location", blockLoc);
	}
	
	/*
	 *  getAllShrines() : Returns an ArrayList<Location> of (Player)player's Shrines.
	 */
	public static ArrayList<Location> getAllShrines()
	{		
		ArrayList<Location> shrines = new ArrayList<Location>();
		for(Entry<Integer, HashMap<String, Object>> divineBlock : DDataUtil.getAllBlockData().entrySet())
		{
			if(((String) divineBlock.getValue().get("block_type")).equalsIgnoreCase("shrine"))
			{
				DivineLocation block = (DivineLocation) divineBlock.getValue().get("block_location");
				shrines.add(block.toLocation());
			}
		}
		return shrines;
	}
	
	/*
	 *  getOwnerOfShrine() : Returns the owner of the shrine at (Location)location.
	 */
	public static int getShrineOwner(Location location)
	{		
		for(Entry<Integer, HashMap<String, Object>> divineBlock : DDataUtil.getAllBlockData().entrySet())
		{	
			DivineLocation block = (DivineLocation) divineBlock.getValue().get("block_location");
			if(block.toLocation().equals(location)) return DObjUtil.toInteger(divineBlock.getValue().get("block_parent"));
		}
		return -1;
	}
	
	/*
	 *  getDeityAtShrine() : Returns the deity of the shrine at (Location)location.
	 */
	public static String getShrineDeity(Location location)
	{
		for(Entry<Integer, HashMap<String, Object>> divineBlock : DDataUtil.getAllBlockData().entrySet())
		{			
			DivineLocation block = (DivineLocation) divineBlock.getValue().get("block_location");
			if(block.toLocation().equals(location)) return divineBlock.getValue().get("block_deity").toString();
		}
		return null;
	}
	
	/* ---------------------------------------------------
	 * Begin Altar-related Methods
	 * ---------------------------------------------------
	 * 
	 *  createAltar() : Creates an altar at (Location)location.
	 */
	public static void createAltar(Location location)
	{		
		// Split the location so we can build off of it
		double locX = location.getX();
		double locY = location.getY();
		double locZ = location.getZ();
		World locWorld = location.getWorld();
				
		// Create enchantment table
		location.add(0, 1, 0).getBlock().setType(Material.ENCHANTMENT_TABLE);

		// Set locations to use for building
		Location topLeft = new Location(locWorld, locX + 2, locY, locZ - 2);
		Location topRight = new Location(locWorld, locX + 2, locY, locZ + 2);
		Location botLeft = new Location(locWorld, locX - 2, locY, locZ - 2);
		Location botRight = new Location(locWorld, locX - 2, locY, locZ + 2);
		
		// Top left of platform
		topLeft.getBlock().setTypeId(98);
		topLeft.subtract(1, 0, 0).getBlock().setTypeId(98);
		topLeft.add(0, 0, 1).getBlock().setTypeId(98);
		topLeft.add(1, 0, 0).getBlock().setTypeId(98);
		
		// Top right of platform
		topRight.getBlock().setTypeId(98);
		topRight.subtract(1, 0, 0).getBlock().setTypeId(98);
		topRight.subtract(0, 0, 1).getBlock().setTypeId(98);
		topRight.add(1, 0, 0).getBlock().setTypeId(98);

		// Bottom left of platform
		botLeft.getBlock().setTypeId(98);
		botLeft.add(1, 0, 0).getBlock().setTypeId(98);
		botLeft.add(0, 0, 1).getBlock().setTypeId(98);
		botLeft.subtract(1, 0, 0).getBlock().setTypeId(98);
		
		// Bottom right of platform
		botRight.getBlock().setTypeId(98);
		botRight.subtract(0, 0, 1).getBlock().setTypeId(98);
		botRight.add(1, 0, 0).getBlock().setTypeId(98);
		botRight.add(0, 0, 1).getBlock().setTypeId(98);

		// Create central structure of platform
		for(int i = 0; i<3; i++) new Location(locWorld, locX + i, locY, locZ).getBlock().setTypeId(98);
		for(int i = 0; i<3; i++) new Location(locWorld, locX, locY, locZ + i).getBlock().setTypeId(98);
		for(int i = 0; i<3; i++) new Location(locWorld, locX, locY, locZ - i).getBlock().setTypeId(98);
		for(int i = 0; i<3; i++) new Location(locWorld, locX - i, locY, locZ).getBlock().setTypeId(98);
		for(int i = 0; i<3; i++) new Location(locWorld, locX + i, locY, locZ).getBlock().setTypeId(98);
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
	 *  getID() : Returns the (int)blockID for the (Location)location.
	 */
	public static int getID(Location location)
	{
		HashMap<Integer, HashMap<String, Object>> divineBlocks = DDataUtil.getAllBlockData();
		for(Entry<Integer, HashMap<String, Object>> divineBlock : divineBlocks.entrySet())
		{
			// Define character-specific variables
			int blockID = divineBlock.getKey();
			DivineLocation block = (DivineLocation) divineBlock.getValue().get("block_location");
			if(block.toLocation().equals(location)) return blockID;
		}
		return -1;
	}
	
	/*
	 *  getDivineBlocks() : Returns an ArrayList<Location> of (int)parentID's Divine Blocks.
	 */
	public static ArrayList<Location> getDivineBlocks(int parentID)
	{
		ArrayList<Location> blocks = new ArrayList<Location>();
		for(Entry<Integer, HashMap<String, Object>> divineBlock : DDataUtil.getAllBlockData().entrySet())
		{
			if(divineBlock.getValue().get("block_parent").equals(parentID))
			{
				DivineLocation block = (DivineLocation) divineBlock.getValue().get("block_location");
				blocks.add(block.toLocation());
			}
		}
		return blocks;
	}
	
	/*
	 *  createDivineParentBlock() : Creates a divine block at (Location)location with (Material)type.
	 */
	public static int createDivineParentBlock(int blockType, String divineType, Location location)
	{
		int blockID = DObjUtil.generateInt(5);
		createDivineBlock(blockID, divineType, blockType, location);		
		return blockID;
	}
	
	/*
	 *  createDivineBlock() : Creates a divine block at (Location)location with (Material)type.
	 */
	public static void createDivineBlock(int parentID, String parentType, int blockType, Location location)
	{
		int blockID = DObjUtil.generateInt(5);
		
		location.getBlock().setTypeId(blockType);
		
		DDataUtil.saveBlockData(blockID, "block_parent", parentID);
		DDataUtil.saveBlockData(blockID, "block_type", parentType);
		DDataUtil.saveBlockData(blockID, "block_location", new DivineLocation(location));
	}
	
	/*
	 *  removeDivineBlock() : Removes the shrine at (Location)location.
	 */
	public static void removeDivineBlock(Location location)
	{
		int blockID = getID(location);
		DDataUtil.removeAllBlockData(blockID);
	}
	
	/*
	 *  isDivineBlock() : Returns a boolean for if the (Location)location is a divine block.
	 */
	public static boolean isDivineBlock(Location location)
	{
		if(getAllShrines() != null) for(Location shrine : getAllShrines()) if(shrine.equals(location)) return true;
		if(getAllAltars() != null) for(Location altar : getAllAltars()) if(altar.equals(location)) return true;
		return false;
	}
	
	/*
	 *  getAllDivineBlocks() : Returns an ArrayList of all divine block locations.
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
	
	// IT'S A DIVING BLOCK, DAMNIT
}