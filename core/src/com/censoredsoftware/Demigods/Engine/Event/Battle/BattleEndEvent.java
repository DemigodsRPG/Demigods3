package com.censoredsoftware.Demigods.Engine.Event.Battle;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BattleEndEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private int battleID;
	private Long endTime;
	private boolean cancelled = false;

	public BattleEndEvent(final int battleID, final Long endTime)
	{
		this.battleID = battleID;
		this.endTime = endTime;
	}

	/*
	 * getID() : Gets the battleID.
	 */
	public int getID()
	{
		return this.battleID;
	}

	/*
	 * getEndTime() : Gets the end time.
	 */
	public Long getEndTime()
	{
		return this.endTime;
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

	@Override
	public boolean isCancelled()
	{
		return this.cancelled;
	}

	@Override
	public synchronized void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
}
