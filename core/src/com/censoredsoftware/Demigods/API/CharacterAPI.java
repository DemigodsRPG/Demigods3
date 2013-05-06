package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

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
			// Create the Character
			return new PlayerCharacter(player, charName, true, DeityAPI.getDeity(charDeity), 0, 50, 0, 0, 0, 0, 0, 0, 0, true);
		}
		return null;
	}

	public static Set<PlayerCharacter> getAllChars()
	{
		Set<PlayerCharacter> characters = new HashSet<PlayerCharacter>();
		for(PlayerCharacter character : PlayerCharacter.loadAll())
		{
			characters.add(character);
		}
		return characters;
	}

	public static PlayerCharacter getChar(long id)
	{
		return PlayerCharacter.load(id);
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

	public static OfflinePlayer getOwner(long charID)
	{
		return getChar(charID).getOwner();
	}

	// TODO Move these.

	public static boolean isCooledDown(Player player, String ability, boolean sendMsg)
	{
		if(DemigodsData.hasKeyTemp(player.getName(), ability + "_cooldown") && Long.parseLong(DemigodsData.getValueTemp(player.getName(), ability + "_cooldown").toString()) > System.currentTimeMillis())
		{
			if(sendMsg) player.sendMessage(ChatColor.RED + ability + " has not cooled down!");
			return false;
		}
		else return true;
	}

	public static void setCoolDown(Player player, String ability, long cooldown)
	{
		DemigodsData.setTemp(player.getName(), ability + "_cooldown", cooldown);
	}

	public static long getCoolDown(Player player, String ability)
	{
		return Long.parseLong(DemigodsData.getValueTemp(player.getName(), ability + "_cooldown").toString());
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
			if(character.getDeity().getInfo().getName().equalsIgnoreCase(deity)) deityList.add(character);
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
			if(character.getDeity().getInfo().getName().equalsIgnoreCase(deity)) deityList.add(character);
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
			if(character.getAlliance().equalsIgnoreCase(alliance)) allianceList.add(character);
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
			if(character.getAlliance().equalsIgnoreCase(alliance)) allianceList.add(character);
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
