package com.censoredsoftware.Demigods.Event.Battle;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Tracked.TrackedBattle;

public class BattleCombineEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private int battleID;
	private TrackedBattle first;
	private TrackedBattle second;
	private Long combineTime;
	private boolean cancelled = false;

	public BattleCombineEvent(int battleID, final TrackedBattle first, final TrackedBattle second, Long combineTime)
	{
		this.battleID = battleID;
		this.first = first;
		this.second = second;
		this.combineTime = combineTime;
	}

	/*
	 * getBattleID() : Gets the battle created.
	 */
	public int getBattleID()
	{
		return this.battleID;
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
