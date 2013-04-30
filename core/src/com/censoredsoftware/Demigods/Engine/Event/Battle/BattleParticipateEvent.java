package com.censoredsoftware.Demigods.Engine.Event.Battle;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

public class BattleParticipateEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private int battleID;
	private PlayerCharacter defending;
	private PlayerCharacter attacking;
	private boolean cancelled = false;

	public BattleParticipateEvent(final int battleID, final PlayerCharacter defending, final PlayerCharacter attacking)
	{
		this.defending = defending;
		this.attacking = attacking;
	}

	/*
	 * getDefending() : Gets the character defending.
	 */
	public PlayerCharacter getDefending()
	{
		return this.defending;
	}

	/*
	 * getAttacking() : Gets the that started the battle.
	 */
	public PlayerCharacter getAttacking()
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
