package com.legit2.Demigods;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.legit2.Demigods.Utilities.DCharUtil;
import com.legit2.Demigods.Utilities.DDataUtil;

public class DDivineBlocks
{	
	/* ---------------------------------------------------
	 * Begin Shrine-related Methods
	 * ---------------------------------------------------
	 * 
	 *  createShrine() : Creates a shrine at (Location)location.
	 */
	public static void createShrine(OfflinePlayer player, String deity, Location location)
	{
		// TODO
	}
	
	/*
	 *  removeShrine() : Removes the shrine at (Location)location.
	 */
	public static void removeShrine(Location location)
	{
		// TODO
	}
	
	/*
	 *  getShrines() : Returns an ArrayList<Location> of (int)charID's Shrines.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Location> getShrines(int charID) throws Exception
	{		
		if(DDataUtil.hasCharData(charID, "shrines")) return (ArrayList<Location>) DDataUtil.getCharData(charID, "shrines");
		else return null;
	}
	
	/*
	 *  getShrines() : Returns an ArrayList<Location> of (Player)player's Shrines.
	 */
	public static ArrayList<Location> getAllShrines() throws Exception
	{		
		ArrayList<Location> shrines = new ArrayList<Location>();
		
		for(int charID : DDataUtil.getAllChars().keySet())
		{
			for(Location shrine : getShrines(charID))
			{
				if(!shrines.contains(shrine)) shrines.add(shrine);
			}
		}
		
		return shrines;
	}
	
	public static int getOwnerOfShrine(Location shrine) throws Exception
	{
		int charID = 0;
		
		for(int character : DDataUtil.getAllChars().keySet())
		{
			for(Location knownShrine : getShrines(character))
			{
				if(shrine.equals(knownShrine))
				{
					charID = character;
					break;
				}
			}
		}
		return charID;
	}
	
	public static String getDeityAtShrine(Location shrine) throws Exception
	{
		int charID = getOwnerOfShrine(shrine);
		String deity = DCharUtil.getDeity(charID);
		return deity;
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
	public static ArrayList<Location> getAllAltars() throws Exception
	{		
		return null; //TODO
	}
	
	public static ArrayList<Location> getAllDivineBlocks() throws Exception
	{
		ArrayList<Location> divineBlocks = new ArrayList<Location>();
		
		// Get all Shrines
		for(Location shrine : getAllShrines())
		{
			if(!divineBlocks.contains(shrine)) divineBlocks.add(shrine);
		}
		
		// Get all Altars
		for(Location altar : getAllAltars())
		{
			if(!divineBlocks.contains(altar)) divineBlocks.add(altar);
		}
		
		return divineBlocks;
	}

}