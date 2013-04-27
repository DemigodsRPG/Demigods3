package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.Event.Battle.BattleCombineEvent;
import com.censoredsoftware.Demigods.Event.Battle.BattleParticipateEvent;
import com.censoredsoftware.Demigods.Event.Battle.BattleStartEvent;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterClass;
import com.censoredsoftware.Demigods.Tracked.TrackedBattle;
import com.censoredsoftware.Demigods.Tracked.TrackedLocation;

public class BattleAPI
{
	private static final int BATTLEDISTANCE = 16; // TODO

	/**
	 * Returns the TrackedBattle object with the id <code>battleID</code>.
	 * 
	 * @param battleID the id to look for.
	 * @return TrackedBattle
	 */
	public static TrackedBattle getBattle(int battleID)
	{
		return (TrackedBattle) DemigodsData.battleData.getDataObject(battleID);
	}

	/**
	 * Returns an ArrayList of all Battles.
	 * 
	 * @return ArrayList
	 */
	public static ArrayList<TrackedBattle> getAll()
	{
		ArrayList<TrackedBattle> battles = new ArrayList<TrackedBattle>();
		for(int key : DemigodsData.battleData.listKeys())
		{
			battles.add(getBattle(key));
		}
		return battles;
	}

	/**
	 * Returns an ArrayList of all active Battles.
	 * 
	 * @return ArrayList
	 */
	public static ArrayList<TrackedBattle> getAllActive()
	{
		ArrayList<TrackedBattle> battles = new ArrayList<TrackedBattle>();
		for(int key : DemigodsData.battleData.listKeys())
		{
			if(getBattle(key).isActive()) battles.add(getBattle(key));
		}
		return battles;
	}

	/**
	 * Returns an ArrayList of all active battles near <code>location</code>.
	 * 
	 * @param location the location to check.
	 * @return TrackedBattle
	 */
	public static TrackedBattle getActiveBattle(Location location)
	{
		for(TrackedBattle battle : getAllActive())
		{
			if(isNearBattle(battle, location)) return battle;
		}
		return null;
	}

	/**
	 * Returns the current active TrackedBattle for <code>character</code>.
	 * 
	 * @param character the character whose active battle to return.
	 * @return TrackedBattle
	 */
	public static TrackedBattle getActiveBattle(PlayerCharacterClass character)
	{
		for(TrackedBattle battle : getAllActive())
		{
			if(isInBattle(battle, character)) return battle;
		}
		return null;
	}

	/**
	 * Returns true if <code>location</code> is near <code>battle</code>.
	 * 
	 * @param battle the battle to compare.
	 * @param location the location to compare.
	 * @return boolean
	 */
	public static boolean isNearBattle(TrackedBattle battle, Location location)
	{
		for(TrackedLocation battleLocation : battle.getLocations())
		{
			if(location.distance(battleLocation.toLocation()) <= BATTLEDISTANCE) return true;
		}
		return false;
	}

	/**
	 * Returns true if <code>character</code> is involved in the given <code>battle</code>.
	 * 
	 * @param battle the battle to check for.
	 * @param character the character to check.
	 * @return boolean
	 */
	public static boolean isInBattle(TrackedBattle battle, PlayerCharacterClass character)
	{
		for(int charID : battle.getCharIDs())
		{
			if(character.getID() == charID) return true;
		}
		return false;
	}

	/**
	 * Returns true if the <code>location</code> is near any active battle.
	 * 
	 * @param location the location to check.
	 * @return boolean
	 */
	public static boolean isNearAnyActiveBattle(Location location)
	{
		for(TrackedBattle battle : getAllActive())
		{
			if(isNearBattle(battle, location)) return true;
		}
		return false;
	}

	/**
	 * Returns true if the <code>character</code> is involved in an active battle.
	 * 
	 * @param character the character to check.
	 * @return boolean
	 */
	public static boolean isInAnyActiveBattle(PlayerCharacterClass character)
	{
		for(TrackedBattle battle : getAllActive())
		{
			if(isInBattle(battle, character)) return true;
		}
		return false;
	}

	/**
	 * Returns true if the <code>command</code> is blocked during battles.
	 * 
	 * @param command the command to check.
	 * @return boolean
	 */
	public static boolean isBlockedCommand(String command)
	{
		for(String blocked : Demigods.config.getSettingArrayListString("battles.blocked_commands"))
		{
			if(command.equalsIgnoreCase(blocked)) return true;
		}
		return false;
	}

	/**
	 * Checks all battles and sets them to inactive where need-be.
	 */
	public static void checkForInactiveBattles() // TODO Timed data.
	{
		for(TrackedBattle battle : getAllActive())
		{
			// int battleID = battle.getID();
			// if(!API.data.hasTimedData(battleID, "battle_active"))
			// {
			// BattleEndEvent battleEvent = new BattleEndEvent(battleID, System.currentTimeMillis());
			// API.misc.callEvent(battleEvent);
			// if(!battleEvent.isCancelled())
			// {
			// battle.setActive(false);
			// }
			// }
		}
	}

	/**
	 * Processes the given characters into a battle.
	 * 
	 * @param hitChar the character being hit.
	 * @param hittingChar the character doing to hitting.
	 */
	public static void battleProcess(PlayerCharacterClass hitChar, PlayerCharacterClass hittingChar)
	{
		TrackedBattle battle = null;
		TrackedBattle otherBattle = null;
		Player hit = hitChar.getOwner().getPlayer();
		Player hitting = hittingChar.getOwner().getPlayer();

		if(isInAnyActiveBattle(hitChar))
		{
			battle = getActiveBattle(hitChar);
			if(isInAnyActiveBattle(hittingChar) && getActiveBattle(hittingChar) != battle) otherBattle = getActiveBattle(hittingChar);
		}
		else if(isInAnyActiveBattle(hittingChar))
		{
			battle = getActiveBattle(hittingChar);
			if(isInAnyActiveBattle(hitChar) && getActiveBattle(hitChar) != battle) otherBattle = getActiveBattle(hitChar);
		}
		else if(isNearAnyActiveBattle(hit.getLocation()))
		{
			battle = getActiveBattle(hit.getLocation());
			if(isNearAnyActiveBattle(hitting.getLocation()) && getActiveBattle(hitting.getLocation()) != battle) otherBattle = getActiveBattle(hitting.getLocation());
		}
		else if(isNearAnyActiveBattle(hitting.getLocation()))
		{
			battle = getActiveBattle(hitting.getLocation());
			if(isNearAnyActiveBattle(hit.getLocation()) && getActiveBattle(hit.getLocation()) != battle) otherBattle = getActiveBattle(hit.getLocation());
		}

		if(battle == null)
		{
			Long startTime = System.currentTimeMillis();
			int battleID = DemigodsData.generateInt(5);
			BattleStartEvent battleEvent = new BattleStartEvent(battleID, hitChar, hittingChar, startTime);
			Bukkit.getServer().getPluginManager().callEvent(battleEvent);
			if(!battleEvent.isCancelled()) battle = new TrackedBattle(hittingChar, hitChar, startTime, battleID);
		}
		else
		{
			if(otherBattle == null)
			{
				int battleID = battle.getID();
				BattleParticipateEvent battleEvent = new BattleParticipateEvent(battleID, hitChar, hittingChar);
				Bukkit.getServer().getPluginManager().callEvent(battleEvent);
				if(!battleEvent.isCancelled())
				{
					battle.addCharacter(hitChar);
					battle.addCharacter(hittingChar);
					// API.data.saveTimedData(battleID, "battle_active", true, 10); // TODO Timed data.
				}
			}
			else
			{
				// Set other battles to inactive
				battle.setActive(false);
				otherBattle.setActive(false);

				BattleCombineEvent battleEvent = null;
				int battleID = DemigodsData.generateInt(5);
				TrackedBattle combinedBattle = null;
				if(battle.getStartTime() < otherBattle.getStartTime())
				{
					battleEvent = new BattleCombineEvent(battleID, battle, otherBattle, System.currentTimeMillis());
					Bukkit.getServer().getPluginManager().callEvent(battleEvent);
					if(!battleEvent.isCancelled())
					{
						combinedBattle = new TrackedBattle(CharacterAPI.getChar(battle.getWhoStarted()), hitChar, battle.getStartTime(), battleID);
						combinedBattle.addCharacter(hittingChar);
					}
					else return;
				}
				else
				{
					battleEvent = new BattleCombineEvent(battleID, otherBattle, battle, System.currentTimeMillis());
					Bukkit.getServer().getPluginManager().callEvent(battleEvent);
					if(!battleEvent.isCancelled())
					{
						combinedBattle = new TrackedBattle(CharacterAPI.getChar(otherBattle.getWhoStarted()), hitChar, otherBattle.getStartTime(), battleID);
						combinedBattle.addCharacter(hittingChar);
					}
					else return;
				}

				// Add all involved locations and characters from both other events
				ArrayList<Integer> charIDs = new ArrayList<Integer>();
				ArrayList<TrackedLocation> locations = new ArrayList<TrackedLocation>();

				// TrackedBattle
				for(int charID : battle.getCharIDs())
				{
					if(!charIDs.contains(charID)) charIDs.add(charID);
				}
				for(TrackedLocation location : battle.getLocations())
				{
					if(!locations.contains(location)) locations.add(location);
				}

				// Other TrackedBattle
				for(int charID : otherBattle.getCharIDs())
				{
					if(!charIDs.contains(charID)) charIDs.add(charID);
				}
				for(TrackedLocation location : otherBattle.getLocations())
				{
					if(!locations.contains(location)) locations.add(location);
				}

				// Overwrite data in the new combined battle
				combinedBattle.overwriteCharIDs(charIDs);
				combinedBattle.overwriteLocations(locations);
			}
		}
	}
}
