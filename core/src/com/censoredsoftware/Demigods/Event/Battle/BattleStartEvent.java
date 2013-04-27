package com.censoredsoftware.Demigods.Event.Battle;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterClass;

public class BattleStartEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private int battleID;
	private PlayerCharacterClass defending;
	private PlayerCharacterClass attacking;
	private Long startTime;
	private boolean cancelled = false;

	public BattleStartEvent(final int battleID, final PlayerCharacterClass defending, final PlayerCharacterClass attacking, final Long startTime)
	{
		this.battleID = battleID;
		this.defending = defending;
		this.attacking = attacking;
		this.startTime = startTime;
	}

	/*
	 * getBattleID() : Gets the battle created.
	 */
	public int getBattleID()
	{
		return this.battleID;
	}

	/*
	 * getDefending() : Gets the character defending.
	 */
	public PlayerCharacterClass getDefending()
	{
		return this.defending;
	}

	/*
	 * getAttacking() : Gets the that started the battle.
	 */
	public PlayerCharacterClass getAttacking()
	{
		return this.attacking;
	}

	/*
	 * getStartTime() : Gets the time that the battle started.
	 */
	public Long getStartTime()
	{
		return this.startTime;
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
