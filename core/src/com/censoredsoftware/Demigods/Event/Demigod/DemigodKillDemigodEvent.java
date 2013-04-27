package com.censoredsoftware.Demigods.Event.Demigod;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Demigod.Demigod;

/*
 * Represents an event that is called when a player is killed by another player.
 */
public class DemigodKillDemigodEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private Demigod attacker;
	private Demigod killedChar;

	public DemigodKillDemigodEvent(final Demigod attacker, final Demigod killedChar)
	{
		this.attacker = attacker;
		this.killedChar = killedChar;
	}

	/*
	 * getCharacter() : Gets the character.
	 */
	public Demigod getCharacter()
	{
		return this.attacker;
	}

	/*
	 * getKilled() : Gets the player that was killed by the player.
	 */
	public Demigod getKilled()
	{
		return this.killedChar;
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
