package com.censoredsoftware.Demigods.Event.Demigod;

import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Demigod.Demigod;

/*
 * Represents an event that is called when a player is killed by another player.
 */
public class DemigodBetrayDemigodEvent extends DemigodKillDemigodEvent
{
	private static final HandlerList handlers = new HandlerList();
	private Demigod killedChar;
	private String alliance;

	public DemigodBetrayDemigodEvent(final Demigod attacker, final Demigod killedChar, final String alliance)
	{
		super(attacker, killedChar);
		this.alliance = alliance;
	}

	/*
	 * getAlliance() : Gets the alliance involved.
	 */
	public String getAlliance()
	{
		return this.alliance;
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
