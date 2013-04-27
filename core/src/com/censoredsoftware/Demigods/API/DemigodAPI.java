package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Demigod.Demigod;
import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.DemigodsFactory;

public class DemigodAPI
{
	/**
	 * Creates a new Demigod for <code>player</code> with the name <code>charName</code> and the deity <code>charDeity</code> and return it.
	 * 
	 * @param player the player to create the character for.
	 * @param charName the name to use for the character.
	 * @param charDeity the deity to use for the character.
	 * @return Demigod
	 */
	public static Demigod create(OfflinePlayer player, String charName, String charDeity)
	{
		if(getCharByName(charName) == null)
		{
			// Define variables
			int charID = DemigodsData.generateInt(5);

			// Create the Character and it's CharacterClass.
			Demigod character = DemigodsFactory.playerCharacterClassFactory.create(player, charID, charName, true, charDeity, DeityAPI.getDeityAlliance(charDeity), 0, 50, 0, 0, 0, 0, 0, 0, 0, true);

			// Add character to the Character Data
			DemigodsData.characterData.saveData(charID, character);

			return character;
		}
		return null;
	}

	public static List<Demigod> getAllChars()
	{
		List<Demigod> characters = new ArrayList<Demigod>();
		for(int charID : DemigodsData.characterData.listKeys())
		{
			Demigod character = (Demigod) DemigodsData.characterData.getDataObject(charID);
			characters.add(character);
		}
		return characters;
	}

	public static Demigod getChar(int charID)
	{
		if(DemigodsData.characterData.containsKey(charID)) return (Demigod) DemigodsData.characterData.getDataObject(charID);
		else return null;
	}

	public static Demigod getCharByName(String charName)
	{
		for(Demigod character : getAllChars())
		{
			if(character.getName().equalsIgnoreCase(charName)) return character;
		}
		return null;
	}

	public static List<Demigod> getAllActive()
	{
		List<Demigod> active = new ArrayList<Demigod>();
		for(Demigod character : getAllChars())
		{
			if(character.isActive()) active.add(character);
		}
		return active;
	}

	public static OfflinePlayer getOwner(int charID)
	{
		return getChar(charID).getOwner();
	}

	// TODO Move this.

	public static boolean isCooledDown(Player player, String ability, long ability_time, boolean sendMsg)
	{
		if(ability_time > System.currentTimeMillis())
		{
			if(sendMsg) player.sendMessage(ChatColor.RED + ability + " has not cooled down!");
			return false;
		}
		else return true;
	}

	// TODO Move this.

	/*
	 * getDeityList() : Gets list of characters in aligned to a Deity.
	 */
	public static ArrayList<Demigod> getDeityList(String deity)
	{
		// Define variables
		ArrayList<Demigod> deityList = new ArrayList<Demigod>();
		for(Demigod character : getAllChars())
		{
			if(character.isDeity().equalsIgnoreCase(deity)) deityList.add(character);
		}
		return deityList;
	}

	/*
	 * getActiveDeityList() : Gets list of active characters in aligned to a Deity.
	 */
	public static ArrayList<Demigod> getActiveDeityList(String deity)
	{
		// Define variables
		ArrayList<Demigod> deityList = new ArrayList<Demigod>();
		for(Demigod character : getAllActive())
		{
			if(character.isDeity().equalsIgnoreCase(deity)) deityList.add(character);
		}
		return deityList;
	}

	/*
	 * getAllianceList() : Gets list of characters in an alliance.
	 */
	public static ArrayList<Demigod> getAllianceList(String alliance)
	{
		// Define variables
		ArrayList<Demigod> allianceList = new ArrayList<Demigod>();
		for(Demigod character : getAllChars())
		{
			if(character.getTeam().equalsIgnoreCase(alliance)) allianceList.add(character);
		}
		return allianceList;
	}

	/*
	 * getActiveAllianceList() : Gets list of active characters in an alliance.
	 */
	public static ArrayList<Demigod> getActiveAllianceList(String alliance)
	{
		// Define variables
		ArrayList<Demigod> allianceList = new ArrayList<Demigod>();
		for(Demigod character : getAllActive())
		{
			if(character.getTeam().equalsIgnoreCase(alliance)) allianceList.add(character);
		}
		return allianceList;
	}

	/*
	 * getImmortalList() : Gets list of currently immortal players.
	 */
	public static ArrayList<Demigod> getImmortalList()
	{
		// Define variables
		ArrayList<Demigod> immortalList = new ArrayList<Demigod>();
		for(Demigod character : getAllChars())
		{
			if(character.isImmortal()) immortalList.add(character);
		}
		return immortalList;
	}
}
