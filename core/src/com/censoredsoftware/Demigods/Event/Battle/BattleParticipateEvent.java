package com.censoredsoftware.Demigods.Event.Battle;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Demigod.Demigod;

public class BattleParticipateEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private int battleID;
	private Demigod defending;
	private Demigod attacking;
	private boolean cancelled = false;

	public BattleParticipateEvent(final int battleID, final Demigod defending, final Demigod attacking)
	{
		this.defending = defending;
		this.attacking = attacking;
	}

	/*
	 * getDefending() : Gets the character defending.
	 */
	public Demigod getDefending()
	{
		return this.defending;
	}

	/*
	 * getAttacking() : Gets the that started the battle.
	 */
	public Demigod getAttacking()
	{
		return this.attacking;
	}

	/*
	 * getBattleID() : Gets the battleID.
	 */
	public int getBattleID()
	{
		return this.battleID;
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
	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
}
