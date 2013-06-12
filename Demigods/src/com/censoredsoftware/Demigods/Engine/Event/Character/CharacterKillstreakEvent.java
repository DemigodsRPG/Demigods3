package com.censoredsoftware.Demigods.Engine.Event.Character;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

// TODO Remove this event and replace with a method to be called when this event would be triggered.

/*
 * Represents an event that is called when a character is on a killstreak.
 */
public class CharacterKillstreakEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private PlayerCharacter character;
	private PlayerCharacter lastKilled;
	private int kills;

	public CharacterKillstreakEvent(final PlayerCharacter character, final PlayerCharacter lastKilled, final int kills)
	{
		this.character = character;
		this.lastKilled = lastKilled;
		this.kills = kills;
	}

	/*
	 * getCharacter() : Gets the character.
	 */
	public PlayerCharacter getCharacter()
	{
		return this.character;
	}

	/*
	 * getLastKilled() : Gets the character that was last killed by the character.
	 */
	public PlayerCharacter getLastKilled()
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
