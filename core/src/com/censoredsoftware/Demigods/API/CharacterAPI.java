package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.DemigodsFactory;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterClass;

public class CharacterAPI
{
	/**
	 * Creates a new PlayerCharacterClass for <code>player</code> with the name <code>charName</code> and the deity <code>charDeity</code> and return it.
	 * 
	 * @param player the player to create the character for.
	 * @param charName the name to use for the character.
	 * @param charDeity the deity to use for the character.
	 * @return PlayerCharacterClass
	 */
	public static PlayerCharacterClass create(OfflinePlayer player, String charName, String charDeity)
	{
		if(getCharByName(charName) == null)
		{
			// Define variables
			int charID = DemigodsData.generateInt(5);

			// Create the Character and it's CharacterClass.
			PlayerCharacterClass character = DemigodsFactory.playerCharacterClassFactory.create(player, charID, charName, true, charDeity, DeityAPI.getDeityAlliance(charDeity), 0, 50, 0, 0, 0, 0, 0, 0, 0, true);

			// Add character to the Character Data
			DemigodsData.characterData.saveData(charID, character);

			return character;
		}
		return null;
	}

	public static List<PlayerCharacterClass> getAllChars()
	{
		List<PlayerCharacterClass> characters = new ArrayList<PlayerCharacterClass>();
		for(int charID : DemigodsData.characterData.listKeys())
		{
			PlayerCharacterClass character = (PlayerCharacterClass) DemigodsData.characterData.getDataObject(charID);
			characters.add(character);
		}
		return characters;
	}

	public static PlayerCharacterClass getChar(int charID)
	{
		if(DemigodsData.characterData.containsKey(charID)) return (PlayerCharacterClass) DemigodsData.characterData.getDataObject(charID);
		else return null;
	}

	public static PlayerCharacterClass getCharByName(String charName)
	{
		for(PlayerCharacterClass character : getAllChars())
		{
			if(character.getName().equalsIgnoreCase(charName)) return character;
		}
		return null;
	}

	public static List<PlayerCharacterClass> getAllActive()
	{
		List<PlayerCharacterClass> active = new ArrayList<PlayerCharacterClass>();
		for(PlayerCharacterClass character : getAllChars())
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
	public static ArrayList<PlayerCharacterClass> getDeityList(String deity)
	{
		// Define variables
		ArrayList<PlayerCharacterClass> deityList = new ArrayList<PlayerCharacterClass>();
		for(PlayerCharacterClass character : getAllChars())
		{
			if(character.getClassName().equalsIgnoreCase(deity)) deityList.add(character);
		}
		return deityList;
	}

	/*
	 * getActiveDeityList() : Gets list of active characters in aligned to a Deity.
	 */
	public static ArrayList<PlayerCharacterClass> getActiveDeityList(String deity)
	{
		// Define variables
		ArrayList<PlayerCharacterClass> deityList = new ArrayList<PlayerCharacterClass>();
		for(PlayerCharacterClass character : getAllActive())
		{
			if(character.getClassName().equalsIgnoreCase(deity)) deityList.add(character);
		}
		return deityList;
	}

	/*
	 * getAllianceList() : Gets list of characters in an alliance.
	 */
	public static ArrayList<PlayerCharacterClass> getAllianceList(String alliance)
	{
		// Define variables
		ArrayList<PlayerCharacterClass> allianceList = new ArrayList<PlayerCharacterClass>();
		for(PlayerCharacterClass character : getAllChars())
		{
			if(character.getTeam().equalsIgnoreCase(alliance)) allianceList.add(character);
		}
		return allianceList;
	}

	/*
	 * getActiveAllianceList() : Gets list of active characters in an alliance.
	 */
	public static ArrayList<PlayerCharacterClass> getActiveAllianceList(String alliance)
	{
		// Define variables
		ArrayList<PlayerCharacterClass> allianceList = new ArrayList<PlayerCharacterClass>();
		for(PlayerCharacterClass character : getAllActive())
		{
			if(character.getTeam().equalsIgnoreCase(alliance)) allianceList.add(character);
		}
		return allianceList;
	}

	/*
	 * getImmortalList() : Gets list of currently immortal players.
	 */
	public static ArrayList<PlayerCharacterClass> getImmortalList()
	{
		// Define variables
		ArrayList<PlayerCharacterClass> immortalList = new ArrayList<PlayerCharacterClass>();
		for(PlayerCharacterClass character : getAllChars())
		{
			if(character.isClassActive()) immortalList.add(character);
		}
		return immortalList;
	}
}
