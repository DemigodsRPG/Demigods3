package com.legit2.Demigods;

import java.util.ArrayList;

import org.bukkit.Location;

public class DDivineBlocks
{	
	public static void createShrine(String username, String deity, Location shrineLocation)
	{
		// TODO
	}
	
	public static void removeShrine(String username, String deity)
	{
		// TODO
	}
	
	/*
	 *  getShrines() : Returns an ArrayList<Location> of (Player)player's Shrines.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Location> getShrines(String username) throws Exception
	{		
		// Set variables
		username = username.toLowerCase();

		if(DSave.hasPlayerData(username, "shrines"))
		{
			return (ArrayList<Location>) DSave.getPlayerData(username, "shrines");
		}
		else return null;
	}
	
	/*
	 *  getShrines() : Returns an ArrayList<Location> of (Player)player's Shrines.
	 */
	public static ArrayList<Location> getAllShrines() throws Exception
	{		
		ArrayList<Location> shrines = new ArrayList<Location>();
		
		for(String username : DSave.getAllPlayersData().keySet())
		{
			for(Location shrine : getShrines(username))
			{
				if(!shrines.contains(shrine)) shrines.add(shrine);
			}
		}
		
		return shrines;
	}
	
	public static String getOwnerOfShrine(Location shrine) throws Exception
	{
		String owner = null;
		
		CHECKSHRINES:
		for(String username : DSave.getAllPlayersData().keySet())
		{
			for(Location knownShrine : getShrines(username))
			{
				if(shrine.equals(knownShrine))
				{
					owner = username;
					break CHECKSHRINES;
				}
			}
		}
		
		return owner;
	}
	
	public static String getDeityAtShrine(Location shrine) throws Exception
	{
		String owner = getOwnerOfShrine(shrine);
		String deity = null;
		
		for(String knownDeity : DUtil.getDeities(owner))
		{
			if(!DSave.hasDeityData(owner, knownDeity, "shrine")) continue;
			
			Location knownShrine = (Location) DSave.getDeityData(owner, knownDeity, "shrine");
			if(shrine.equals(knownShrine))
			{
				deity = knownDeity;
				break;
			}
		}
		
		return deity;
	}
	
	public static void createAltar(Location altarLocation)
	{
		// TODO
	}
	
	public static void removeAltar(Location altarLocation)
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