package com.censoredsoftware.Demigods.Event.Character;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterClass;

/*
 * Represents an event that is called when a character is on a killstreak.
 */
public class CharacterKillstreakEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private PlayerCharacterClass character;
	private PlayerCharacterClass lastKilled;
	private int kills;

	public CharacterKillstreakEvent(final PlayerCharacterClass character, final PlayerCharacterClass lastKilled, final int kills)
	{
		this.character = character;
		this.lastKilled = lastKilled;
		this.kills = kills;
	}

	/*
	 * getCharacter() : Gets the character.
	 */
	public PlayerCharacterClass getCharacter()
	{
		return this.character;
	}

	/*
	 * getLastKilled() : Gets the character that was last killed by the character.
	 */
	public PlayerCharacterClass getLastKilled()
	{
		return this.lastKilled;
	}

	/*
	 * getKills() : Gets number of kills in this killstreak.
	 */
	public int getKills()
	{
		return this.kills;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
