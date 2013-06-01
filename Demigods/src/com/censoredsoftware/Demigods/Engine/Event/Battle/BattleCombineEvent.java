package com.censoredsoftware.Demigods.Engine.Event.Battle;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Engine.Tracked.TrackedBattle;

public class BattleCombineEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private TrackedBattle first;
	private TrackedBattle second;
	private Long combineTime;
	private boolean cancelled = false;

	public BattleCombineEvent(final TrackedBattle first, final TrackedBattle second, Long combineTime)
	{
		this.first = first;
		this.second = second;
		this.combineTime = combineTime;
	}

	/*
	 * getFirst() : Gets the first battle involved (based on time).
	 */
	public TrackedBattle getFirst()
	{
		return this.first;
	}

	/*
	 * getSecond() : Gets the second battle involved (based on time).
	 */
	public TrackedBattle getSecond()
	{
		return this.second;
	}

	/*
	 * getCombineTime() : Gets the time that the battles combined.
	 */
	public Long getCombineTime()
	{
		return this.combineTime;
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
