package com.censoredsoftware.Demigods.Event.Demigod;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Demigod.Demigod;

/*
 * Represents an event that is called when a character is on a killstreak.
 */
public class DemigodKillstreakEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private Demigod character;
	private Demigod lastKilled;
	private int kills;

	public DemigodKillstreakEvent(final Demigod character, final Demigod lastKilled, final int kills)
	{
		this.character = character;
		this.lastKilled = lastKilled;
		this.kills = kills;
	}

	/*
	 * getCharacter() : Gets the character.
	 */
	public Demigod getCharacter()
	{
		return this.character;
	}

	/*
	 * getLastKilled() : Gets the character that was last killed by the character.
	 */
	public Demigod getLastKilled()
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
