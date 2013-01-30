package com.legit2.Demigods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
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
		DDataUtil.saveBlockData(blockID, "block_permanent", true);
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
			if((divineBlock.getValue().get("block_type")).toString().equalsIgnoreCase("shrine"))
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
	 *  createNewAltar() : Creates a new altar at (Location)location.
	 */
	public static void createAltar(Location location)
	{
		int parentID = createDivineParentBlock(location, 116, "all", "altar");
		location.subtract(0, 2, 0);
		generateAltar(location, parentID);
	}
	
	/*
	 *  createAltar() : Creates an altar at (Location)location for (int)parentID.
	 */
	public static void generateAltar(Location location, int parentID)
	{	
		// Split the location so we can build off of it
		location.getBlock();
		double locX = location.getX();
		double locY = location.getY();
		double locZ = location.getZ();
		World locWorld = location.getWorld();
		
		// Create magical table stand
		createDivineBlock(new Location(locWorld, locX, locY + 1, locZ), parentID, 98);
		
		createDivineBlock(new Location(locWorld, locX + 2, locY + 4, locZ + 2), parentID, 98);
		createDivineBlock(new Location(locWorld, locX - 2, locY + 4, locZ - 2), parentID, 98);
		createDivineBlock(new Location(locWorld, locX + 2, locY + 4, locZ - 2), parentID, 98);
		createDivineBlock(new Location(locWorld, locX - 2, locY + 4, locZ + 2), parentID, 98);
		createDivineBlock(new Location(locWorld, locX + 2, locY + 5, locZ + 2), parentID, 126, (byte) 1);
		createDivineBlock(new Location(locWorld, locX - 2, locY + 5, locZ - 2), parentID, 126, (byte) 1);
		createDivineBlock(new Location(locWorld, locX + 2, locY + 5, locZ - 2), parentID, 126, (byte) 1);
		createDivineBlock(new Location(locWorld, locX - 2, locY + 5, locZ + 2), parentID, 126, (byte) 1);
		
		createDivineBlock(new Location(locWorld, locX, locY + 6, locZ), parentID, 126, (byte) 1);
		createDivineBlock(new Location(locWorld, locX - 1, locY + 5, locZ - 1), parentID, 5, (byte) 1);
		createDivineBlock(new Location(locWorld, locX - 1, locY + 5, locZ), parentID, 5, (byte) 1);
		createDivineBlock(new Location(locWorld, locX - 1, locY + 5, locZ + 1), parentID, 5, (byte) 1);
		createDivineBlock(new Location(locWorld, locX + 1, locY + 5, locZ - 1), parentID, 5, (byte) 1);
		createDivineBlock(new Location(locWorld, locX + 1, locY + 5, locZ), parentID, 5, (byte) 1);
		createDivineBlock(new Location(locWorld, locX + 1, locY + 5, locZ + 1), parentID, 5, (byte) 1);
		createDivineBlock(new Location(locWorld, locX, locY + 5, locZ), parentID, 5, (byte) 1);
		createDivineBlock(new Location(locWorld, locX, locY + 5, locZ - 1), parentID, 5, (byte) 1);
		createDivineBlock(new Location(locWorld, locX, locY + 5, locZ + 1), parentID, 5, (byte) 1);
		
		createDivineBlock(new Location(locWorld, locX + 3, locY, locZ + 3), parentID, 44, (byte) 5);
		createDivineBlock(new Location(locWorld, locX - 3, locY, locZ - 3), parentID, 44, (byte) 5);
		createDivineBlock(new Location(locWorld, locX + 3, locY, locZ - 3), parentID, 44, (byte) 5);
		createDivineBlock(new Location(locWorld, locX - 3, locY, locZ + 3), parentID, 44, (byte) 5);

		createDivineBlock(new Location(locWorld, locX + 2, locY + 3, locZ + 2), parentID, 44, (byte) 13);
		createDivineBlock(new Location(locWorld, locX - 2, locY + 3, locZ - 2), parentID, 44, (byte) 13);
		createDivineBlock(new Location(locWorld, locX + 2, locY + 3, locZ - 2), parentID, 44, (byte) 13);
		createDivineBlock(new Location(locWorld, locX - 2, locY + 3, locZ + 2), parentID, 44, (byte) 13);
				
		// Left beam
		createDivineBlock(new Location(locWorld, locX + 1, locY + 4, locZ - 2), parentID, 98);
		createDivineBlock(new Location(locWorld, locX, locY + 4, locZ - 2), parentID, 98, (byte) 3);
		createDivineBlock(new Location(locWorld, locX - 1, locY + 4, locZ - 2), parentID, 98);
		createDivineBlock(new Location(locWorld, locX + 1, locY + 5, locZ - 2), parentID, 126, (byte) 1);
		createDivineBlock(new Location(locWorld, locX, locY + 5, locZ - 2), parentID, 126, (byte) 1);
		createDivineBlock(new Location(locWorld, locX - 1, locY + 5, locZ - 2), parentID, 126, (byte) 1);
		// Right beam
		createDivineBlock(new Location(locWorld, locX + 1, locY + 4, locZ + 2), parentID, 98);
		createDivineBlock(new Location(locWorld, locX, locY + 4, locZ + 2), parentID, 98, (byte) 3);
		createDivineBlock(new Location(locWorld, locX - 1, locY + 4, locZ + 2), parentID, 98);
		createDivineBlock(new Location(locWorld, locX + 1, locY + 5, locZ + 2), parentID, 126, (byte) 1);
		createDivineBlock(new Location(locWorld, locX, locY + 5, locZ + 2), parentID, 126, (byte) 1);
		createDivineBlock(new Location(locWorld, locX - 1, locY + 5, locZ + 2), parentID, 126, (byte) 1);
		// Top beam
		createDivineBlock(new Location(locWorld, locX + 2, locY + 4, locZ + 1), parentID, 98);
		createDivineBlock(new Location(locWorld, locX + 2, locY + 4, locZ), parentID, 98, (byte) 3);
		createDivineBlock(new Location(locWorld, locX + 2, locY + 4, locZ - 1), parentID, 98);
		createDivineBlock(new Location(locWorld, locX + 2, locY + 5, locZ + 1), parentID, 126, (byte) 1);
		createDivineBlock(new Location(locWorld, locX + 2, locY + 5, locZ), parentID, 126, (byte) 1);
		createDivineBlock(new Location(locWorld, locX + 2, locY + 5, locZ - 1), parentID, 126, (byte) 1);
		// Bottom beam
		createDivineBlock(new Location(locWorld, locX - 2, locY + 4, locZ + 1), parentID, 98);
		createDivineBlock(new Location(locWorld, locX - 2, locY + 4, locZ), parentID, 98, (byte) 3);
		createDivineBlock(new Location(locWorld, locX - 2, locY + 4, locZ - 1), parentID, 98);
		createDivineBlock(new Location(locWorld, locX - 2, locY + 5, locZ + 1), parentID, 126, (byte) 1);
		createDivineBlock(new Location(locWorld, locX - 2, locY + 5, locZ), parentID, 126, (byte) 1);
		createDivineBlock(new Location(locWorld, locX - 2, locY + 5, locZ - 1), parentID, 126, (byte) 1);

		
		// Set locations to use for building
		Location topLeft = new Location(locWorld, locX + 2, locY + 1, locZ - 2);
		Location topRight = new Location(locWorld, locX + 2, locY + 1, locZ + 2);
		Location botLeft = new Location(locWorld, locX - 2, locY + 1, locZ - 2);
		Location botRight = new Location(locWorld, locX - 2, locY + 1, locZ + 2);
		
		// Top left of platform
		createDivineBlock(topLeft, parentID, 44, (byte) 5);
		createDivineBlock(topLeft.subtract(1, 0, 0), parentID, 44, (byte) 5);
		createDivineBlock(topLeft.add(0, 0, 1), parentID, 44, (byte) 5);
		createDivineBlock(topLeft.add(1, 0, 0), parentID, 44, (byte) 5);
		// Top right of platform
		createDivineBlock(topRight, parentID, 44, (byte) 5);
		createDivineBlock(topRight.subtract(1, 0, 0), parentID, 44, (byte) 5);
		createDivineBlock(topRight.subtract(0, 0, 1), parentID, 44, (byte) 5);
		createDivineBlock(topRight.add(1, 0, 0), parentID, 44, (byte) 5);
		// Bottom left of platform
		createDivineBlock(botLeft, parentID, 44, (byte) 5);
		createDivineBlock(botLeft.add(1, 0, 0), parentID, 44, (byte) 5);
		createDivineBlock(botLeft.add(0, 0, 1), parentID, 44, (byte) 5);
		createDivineBlock(botLeft.subtract(1, 0, 0), parentID, 44, (byte) 5);
		// Bottom right of platform
		createDivineBlock(botRight, parentID, 44, (byte) 5);
		createDivineBlock(botRight.subtract(0, 0, 1), parentID, 44, (byte) 5);
		createDivineBlock(botRight.add(1, 0, 0), parentID, 44, (byte) 5);
		createDivineBlock(botRight.add(0, 0, 1), parentID, 44, (byte) 5);
		
		// Create central structure of platform
		for(int i = 1; i<3; i++) createDivineBlock(new Location(locWorld, locX, locY + 1, locZ + i), parentID, 44, (byte) 5);
		for(int i = 1; i<3; i++) createDivineBlock(new Location(locWorld, locX, locY + 1, locZ - i), parentID, 44, (byte) 5);
		for(int i = 1; i<3; i++) createDivineBlock(new Location(locWorld, locX - i, locY + 1, locZ), parentID, 44, (byte) 5);
		for(int i = 1; i<3; i++) createDivineBlock(new Location(locWorld, locX + i, locY + 1, locZ), parentID, 44, (byte) 5);
		
		// Build steps on all sides.
		Location leftSteps = new Location(locWorld, locX + 2, locY, locZ - 4);
		Location rightSteps = new Location(locWorld, locX + 2, locY, locZ + 4);
		Location topSteps = new Location(locWorld, locX + 4, locY, locZ - 2);
		Location botSteps = new Location(locWorld, locX - 4, locY, locZ - 2);
	
		// Create left steps
		createDivineBlock(leftSteps, parentID, 44, (byte) 5);
		for(int i = 1; i<5; i++) createDivineBlock(leftSteps.subtract(1, 0, 0), parentID, 44, (byte) 5);
		createDivineBlock(leftSteps.add(0, 0, 1), parentID, 98);
		for(int i = 1; i<5; i++) createDivineBlock(leftSteps.add(1, 0, 0), parentID, 98);
		
		// Create right steps
		createDivineBlock(rightSteps, parentID, 44, (byte) 5);
		for(int i = 1; i<5; i++) createDivineBlock(rightSteps.subtract(1, 0, 0), parentID, 44, (byte) 5);
		createDivineBlock(rightSteps.subtract(0, 0, 1), parentID, 98);
		for(int i = 1; i<5; i++) createDivineBlock(rightSteps.add(1, 0, 0), parentID, 98);
		
		// Create top steps
		createDivineBlock(topSteps, parentID, 44, (byte) 5);
		for(int i = 1; i<5; i++) createDivineBlock(topSteps.add(0, 0, 1), parentID, 44, (byte) 5);
		createDivineBlock(topSteps.subtract(1, 0, 0), parentID, 98);
		for(int i = 1; i<5; i++) createDivineBlock(topSteps.subtract(0, 0, 1), parentID, 98);
		
		// Create bottom steps
		createDivineBlock(botSteps, parentID, 44, (byte) 5);
		for(int i = 1; i<5; i++) createDivineBlock(botSteps.add(0, 0, 1), parentID, 44, (byte) 5);
		createDivineBlock(botSteps.add(1, 0, 0), parentID, 98);
		for(int i = 1; i<5; i++) createDivineBlock(botSteps.subtract(0, 0, 1), parentID, 98);
		
		// Create left step towers
		for(int i = 0; i<3; i++) createDivineBlock(leftSteps.add(0, 1, 0), parentID, 98);
		createDivineBlock(leftSteps.add(0, 1, 0), parentID, 126, (byte) 1);
		createDivineBlock(leftSteps.subtract(4, 0, 0), parentID, 98);
		createDivineBlock(leftSteps, parentID, 126, (byte) 1);
		for(int i = 0; i<3; i++) createDivineBlock(leftSteps.subtract(0, 1, 0), parentID, 98);
	
		// Create right step towers
		for(int i = 0; i<3; i++) createDivineBlock(rightSteps.add(0, 1, 0), parentID, 98);
		createDivineBlock(rightSteps.add(0, 1, 0), parentID, 126, (byte) 1);
		createDivineBlock(rightSteps.subtract(4, 0, 0), parentID, 98);
		createDivineBlock(rightSteps, parentID, 126, (byte) 1);
		for(int i = 0; i<3; i++) createDivineBlock(rightSteps.subtract(0, 1, 0), parentID, 98);
	
		// Create top step towers
		for(int i = 0; i<3; i++) createDivineBlock(topSteps.add(0, 1, 0), parentID, 98);
		createDivineBlock(topSteps.add(0, 1, 0), parentID, 126, (byte) 1);
		createDivineBlock(topSteps.add(0, 0, 4), parentID, 98);
		createDivineBlock(topSteps, parentID, 126, (byte) 1);
		for(int i = 0; i<3; i++) createDivineBlock(topSteps.subtract(0, 1, 0), parentID, 98);
	
		// Create bottom step towers
		for(int i = 0; i<3; i++) createDivineBlock(botSteps.add(0, 1, 0), parentID, 98);
		createDivineBlock(botSteps.add(0, 1, 0), parentID, 126, (byte) 1);
		createDivineBlock(botSteps.add(0, 0, 4), parentID, 98);
		createDivineBlock(botSteps, parentID, 126, (byte) 1);
		for(int i = 0; i<3; i++) createDivineBlock(botSteps.subtract(0, 1, 0), parentID, 98);
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
		ArrayList<Location> altars = new ArrayList<Location>();
		for(Entry<Integer, HashMap<String, Object>> divineBlock : DDataUtil.getAllBlockData().entrySet())
		{
			if((divineBlock.getValue().get("block_type")).toString().equalsIgnoreCase("altar") && DObjUtil.toBoolean(divineBlock.getValue().get("block_permanent")))
			{
				DivineLocation block = (DivineLocation) divineBlock.getValue().get("block_location");
				altars.add(block.toLocation());
			}
		}
		return altars;
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
	public static int createDivineParentBlock(Location location, int blockType, String blockDeity, String divineType)
	{
		int blockID = DObjUtil.generateInt(5);
		DDataUtil.saveBlockData(blockID, "block_parent", blockID);
		DDataUtil.saveBlockData(blockID, "block_permanent", true);
		DDataUtil.saveBlockData(blockID, "block_type", divineType);
		DDataUtil.saveBlockData(blockID, "block_deity", blockDeity);
		DDataUtil.saveBlockData(blockID, "block_location", new DivineLocation(location));
		location.getBlock().setTypeId(blockType);
		return blockID;
	}
	
	/*
	 *  createDivineBlock() : Creates a divine block at (Location)location with (Material)type.
	 */
	public static int createDivineBlock(Location location, int parentID, int blockType)
	{
		int blockID = DObjUtil.generateInt(5);
		
		DDataUtil.saveBlockData(blockID, "block_parent", parentID);
		DDataUtil.saveBlockData(blockID, "block_permanent", false);
		DDataUtil.saveBlockData(blockID, "block_type", getDivineBlockType(parentID));
		DDataUtil.saveBlockData(blockID, "block_deity", getDivineBlockDeity(parentID));
		DDataUtil.saveBlockData(blockID, "block_location", new DivineLocation(location));
		
		location.getBlock().setTypeId(blockType);
		return blockID;
	}
	public static int createDivineBlock(Location location, int parentID, int blockType, byte byteData)
	{
		int blockID = createDivineBlock(location, parentID, blockType);
		location.getBlock().setData(byteData);
		return blockID;
	}
	
	/*
	 *  removeDivineBlock() : Removes the shrine at (Location)location.
	 */
	public static void removeDivineBlock(Location location)
	{
		// TODO: Make this remove all blocks with X parent ID
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
	 *  getDivineBlockType() : Returns the (String)divineType for (int)parentID.
	 */
	public static String getDivineBlockType(int blockID)
	{
		if(DDataUtil.hasBlockData(blockID, "block_type"))
		{
			return DDataUtil.getBlockData(blockID, "block_type").toString();
		}
		return null;
	}
	
	/*
	 *  getDivineBlockDeity() : Returns the (String)blockDeity for (int)parentID.
	 */
	public static String getDivineBlockDeity(int blockID)
	{
		if(DDataUtil.hasBlockData(blockID, "block_deity"))
		{
			return DDataUtil.getBlockData(blockID, "block_deity").toString();
		}
		return null;
	}
	
	/*
	 *  getAllDivineBlocks() : Returns an ArrayList of all divine block locations.
	 */
	public static ArrayList<Location> getAllDivineBlocks()
	{
		ArrayList<Location> blocks = new ArrayList<Location>();
		for(Entry<Integer, HashMap<String, Object>> divineBlock : DDataUtil.getAllBlockData().entrySet())
		{
			DivineLocation block = (DivineLocation) divineBlock.getValue().get("block_location");
			blocks.add(block.toLocation());
		}
		return blocks;
	}
	
	// IT'S A DIVING BLOCK, DAMNIT
}