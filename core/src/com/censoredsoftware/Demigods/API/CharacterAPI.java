package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacterFactory;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedPlayer;

public class CharacterAPI
{
	public static void characterCreation(OfflinePlayer player, String chosenName, String chosenDeity)
	{
		PlayerCharacter character = PlayerCharacterFactory.createCharacter(player, chosenName, chosenDeity);

		// Remove temporary data
		DemigodsData.removeTemp(player.getName(), "temp_createchar");

		Demigods.message.broadcast("Creating character.");

		if(player.isOnline())
		{
			Demigods.message.broadcast("Setting character.");

			Player online = player.getPlayer();
			online.setDisplayName(DeityAPI.getDeity(chosenDeity).getInfo().getColor() + chosenName + ChatColor.WHITE);
			online.setPlayerListName(DeityAPI.getDeity(chosenDeity).getInfo().getColor() + chosenName + ChatColor.WHITE);

			online.sendMessage(ChatColor.GREEN + "You have been accepted into the lineage of " + chosenDeity + "!");
			online.getWorld().strikeLightningEffect(online.getLocation());

			for(int i = 0; i < 20; i++)
				online.getWorld().spawn(online.getLocation(), ExperienceOrb.class);

			// Switch current character
			TrackedPlayer.getTracked(player).switchCharacter(character);
		}
	}

	public static Set<PlayerCharacter> getAllChars()
	{
		return PlayerCharacter.loadAll();
	}

	public static PlayerCharacter getChar(Long id)
	{
		return PlayerCharacter.load(id);
	}

	public static PlayerCharacter getCharByName(String charName)
	{
		for(PlayerCharacter character : getAllChars())
		{
			if(character.getName().equalsIgnoreCase(charName)) return character;
		}
		throw new NullPointerException("No character by that name exists.");
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
		return getChar(charID).getPlayer();
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
