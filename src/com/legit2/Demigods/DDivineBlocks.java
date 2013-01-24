package com.legit2.Demigods;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.legit2.Demigods.Utilities.DCharUtil;
import com.legit2.Demigods.Utilities.DDataUtil;
import com.legit2.Demigods.Utilities.DPlayerUtil;
import com.legit2.Demigods.Utilities.DUtil;

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
		OfflinePlayer player = DPlayerUtil.definePlayer(username);
		if(DDataUtil.hasPlayerData(player, "shrines")) return (ArrayList<Location>) DDataUtil.getPlayerData(player, "shrines");
		else return null;
	}
	
	/*
	 *  getShrines() : Returns an ArrayList<Location> of (Player)player's Shrines.
	 */
	public static ArrayList<Location> getAllShrines() throws Exception
	{		
		ArrayList<Location> shrines = new ArrayList<Location>();
		
		for(String username : DDataUtil.getAllPlayers().keySet())
		{
			for(Location shrine : getShrines(username))
			{
				if(!shrines.contains(shrine)) shrines.add(shrine);
			}
		}
		
		return shrines;
	}
	
	public static OfflinePlayer getOwnerOfShrine(Location shrine) throws Exception
	{
		OfflinePlayer owner = null;
		
		CHECKSHRINES:
		for(String username : DDataUtil.getAllPlayers().keySet())
		{
			for(Location knownShrine : getShrines(username))
			{
				if(shrine.equals(knownShrine))
				{
					owner = DPlayerUtil.definePlayer(username);
					break CHECKSHRINES;
				}
			}
		}
		
		return owner;
	}
	
	public static String getDeityAtShrine(Location shrine) throws Exception
	{
		OfflinePlayer player = getOwnerOfShrine(shrine);
		int charID = DPlayerUtil.getCurrentChar(player);
		String deity = null;
		
		DCharUtil.getDeity(player, charID);
		
		if(!DDataUtil.hasCharData(player, charID, "shrine")) continue;
		Location knownShrine = (Location) DDataUtil.getCharData(player, charID, "shrine");
		
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