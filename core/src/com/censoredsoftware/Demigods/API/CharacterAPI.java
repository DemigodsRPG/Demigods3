package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.DemigodsFactory;
import com.censoredsoftware.Objects.Character.PlayerCharacter;
import com.censoredsoftware.Objects.Character.PlayerCharacterClass;

public class CharacterAPI
{
	/**
	 * Creates a new PlayerCharacter for <code>player</code> with the name <code>charName</code> and the deity <code>charDeity</code> and return it.
	 * 
	 * @param player the player to create the character for.
	 * @param charName the name to use for the character.
	 * @param charDeity the deity to use for the character.
	 * @return PlayerCharacter
	 */
	public static PlayerCharacter create(OfflinePlayer player, String charName, String charDeity)
	{
		if(getCharByName(charName) == null)
		{
			// Define variables
			int charID = DemigodsData.generateInt(5);

			// Create the Character and it's CharacterClass.
			PlayerCharacter character = DemigodsFactory.playerCharacterFactory.create(player, charID, charName);
			PlayerCharacterClass characterClass = DemigodsFactory.playerCharacterClassFactory.create(character, charDeity, DeityAPI.getDeityAlliance(charDeity), 0, 50, 0, 0, 0, 0, 0, 0, 0, true);
			character.setCharacterClass(characterClass);

			// Add character to the Character Data
			DemigodsData.characterData.saveData(charID, character);

			return character;
		}
		return null;
	}

	public static List<PlayerCharacter> getAllChars()
	{
		List<PlayerCharacter> characters = new ArrayList<PlayerCharacter>();
		for(int charID : DemigodsData.characterData.listKeys())
		{
			PlayerCharacter character = (PlayerCharacter) DemigodsData.characterData.getDataObject(charID);
			characters.add(character);
		}
		return characters;
	}

	public static PlayerCharacter getChar(int charID)
	{
		if(DemigodsData.characterData.containsKey(charID)) return (PlayerCharacter) DemigodsData.characterData.getDataObject(charID);
		else return null;
	}

	public static PlayerCharacter getCharByName(String charName)
	{
		for(PlayerCharacter character : getAllChars())
		{
			if(character.getName().equalsIgnoreCase(charName)) return character;
		}
		return null;
	}

	public static List<PlayerCharacter> getAllActive()
	{
		List<PlayerCharacter> active = new ArrayList<PlayerCharacter>();
		for(PlayerCharacter character : getAllChars())
		{
			if(character.isActive()) active.add(character);
		}
		return active;
	}

	/*
	 * getOwner() : Returns the (OfflinePlayer)player who owns (int)charID.
	 */
	public OfflinePlayer getOwner(int charID)
	{
		return getChar(charID).getOwner();
	}

	// TODO Move this.
	/*
	 * isCooledDown() : Checks if the passed in ability has cooled down or not.
	 */
	public boolean isCooledDown(Player player, String ability, long ability_time, boolean sendMsg)
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
	public ArrayList<PlayerCharacter> getDeityList(String deity)
	{
		// Define variables
		ArrayList<PlayerCharacter> deityList = new ArrayList<PlayerCharacter>();
		for(PlayerCharacter character : getAllChars())
		{
			if(character.getCharacterClass().getClassName().equalsIgnoreCase(deity)) deityList.add(character);
		}
		return deityList;
	}

	/*
	 * getActiveDeityList() : Gets list of active characters in aligned to a Deity.
	 */
	public ArrayList<PlayerCharacter> getActiveDeityList(String deity)
	{
		// Define variables
		ArrayList<PlayerCharacter> deityList = new ArrayList<PlayerCharacter>();
		for(PlayerCharacter character : getAllActive())
		{
			if(character.getCharacterClass().getClassName().equalsIgnoreCase(deity)) deityList.add(character);
		}
		return deityList;
	}

	/*
	 * getAllianceList() : Gets list of characters in an alliance.
	 */
	public ArrayList<PlayerCharacter> getAllianceList(String alliance)
	{
		// Define variables
		ArrayList<PlayerCharacter> allianceList = new ArrayList<PlayerCharacter>();
		for(PlayerCharacter character : getAllChars())
		{
			if(character.getCharacterClass().getTeam().equalsIgnoreCase(alliance)) allianceList.add(character);
		}
		return allianceList;
	}

	/*
	 * getActiveAllianceList() : Gets list of active characters in an alliance.
	 */
	public ArrayList<PlayerCharacter> getActiveAllianceList(String alliance)
	{
		// Define variables
		ArrayList<PlayerCharacter> allianceList = new ArrayList<PlayerCharacter>();
		for(PlayerCharacter character : getAllActive())
		{
			if(character.getCharacterClass().getTeam().equalsIgnoreCase(alliance)) allianceList.add(character);
		}
		return allianceList;
	}

	/*
	 * getImmortalList() : Gets list of currently immortal players.
	 */
	public ArrayList<PlayerCharacter> getImmortalList()
	{
		// Define variables
		ArrayList<PlayerCharacter> immortalList = new ArrayList<PlayerCharacter>();
		for(PlayerCharacter character : getAllChars())
		{
			if(character.getCharacterClass().isActive()) immortalList.add(character);
		}
		return immortalList;
	}
}
