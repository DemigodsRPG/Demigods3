package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.DemigodsFactory;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacter;

public class CharacterAPI
{
	/**
	 * Creates a new PlayerCharacter for <code>player</code> with the name <code>charName</code> and the deity <code>charDeity</code> and return it.
	 * 
	 * @param player the player to create the character for.
	 * @param charName the name to use for the character.
	 * @param charDeity the deity to use for the character.
	 * @return Demigod
	 */
	public static PlayerCharacter create(OfflinePlayer player, String charName, String charDeity)
	{
		if(getCharByName(charName) == null)
		{
			// Define variables
			int charID = DemigodsData.generateInt(5);

			// Create the Character and it's CharacterClass.
			PlayerCharacter character = DemigodsFactory.playerCharacterFactory.create(player, charID, charName, true, charDeity, DeityAPI.getDeityAlliance(charDeity), 0, 50, 0, 0, 0, 0, 0, 0, 0, true);

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

	public static OfflinePlayer getOwner(int charID)
	{
		return getChar(charID).getOwner();
	}

	// TODO Move these.

	public static boolean isCooledDown(Player player, String ability, boolean sendMsg)
	{
		if(DemigodsData.tempPlayerData.containsKey(player, ability + "_cooldown") && DemigodsData.tempPlayerData.getDataLong(player, ability + "_cooldown") > System.currentTimeMillis())
		{
			if(sendMsg) player.sendMessage(ChatColor.RED + ability + " has not cooled down!");
			return false;
		}
		else return true;
	}

	public static void setCoolDown(Player player, String ability, long cooldown)
	{
		DemigodsData.tempPlayerData.saveData(player, ability + "_cooldown", cooldown);
	}

	public static long getCoolDown(Player player, String ability)
	{
		return DemigodsData.tempPlayerData.getDataLong(player, ability + "_cooldown");
	}

	// TODO Move these.

	/*
	 * getDeityList() : Gets list of characters in aligned to a Deity.
	 */
	public static ArrayList<PlayerCharacter> getDeityList(String deity)
	{
		// Define variables
		ArrayList<PlayerCharacter> deityList = new ArrayList<PlayerCharacter>();
		for(PlayerCharacter character : getAllChars())
		{
			if(character.isDeity().equalsIgnoreCase(deity)) deityList.add(character);
		}
		return deityList;
	}

	/*
	 * getActiveDeityList() : Gets list of active characters in aligned to a Deity.
	 */
	public static ArrayList<PlayerCharacter> getActiveDeityList(String deity)
	{
		// Define variables
		ArrayList<PlayerCharacter> deityList = new ArrayList<PlayerCharacter>();
		for(PlayerCharacter character : getAllActive())
		{
			if(character.isDeity().equalsIgnoreCase(deity)) deityList.add(character);
		}
		return deityList;
	}

	/*
	 * getAllianceList() : Gets list of characters in an alliance.
	 */
	public static ArrayList<PlayerCharacter> getAllianceList(String alliance)
	{
		// Define variables
		ArrayList<PlayerCharacter> allianceList = new ArrayList<PlayerCharacter>();
		for(PlayerCharacter character : getAllChars())
		{
			if(character.getTeam().equalsIgnoreCase(alliance)) allianceList.add(character);
		}
		return allianceList;
	}

	/*
	 * getActiveAllianceList() : Gets list of active characters in an alliance.
	 */
	public static ArrayList<PlayerCharacter> getActiveAllianceList(String alliance)
	{
		// Define variables
		ArrayList<PlayerCharacter> allianceList = new ArrayList<PlayerCharacter>();
		for(PlayerCharacter character : getAllActive())
		{
			if(character.getTeam().equalsIgnoreCase(alliance)) allianceList.add(character);
		}
		return allianceList;
	}

	/*
	 * getImmortalList() : Gets list of currently immortal players.
	 */
	public static ArrayList<PlayerCharacter> getImmortalList()
	{
		// Define variables
		ArrayList<PlayerCharacter> immortalList = new ArrayList<PlayerCharacter>();
		for(PlayerCharacter character : getAllChars())
		{
			if(character.isImmortal()) immortalList.add(character);
		}
		return immortalList;
	}
}
