package com.censoredsoftware.Demigods.Event.Battle;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterClass;

public class BattleParticipateEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private int battleID;
	private PlayerCharacterClass defending;
	private PlayerCharacterClass attacking;
	private boolean cancelled = false;

	public BattleParticipateEvent(final int battleID, final PlayerCharacterClass defending, final PlayerCharacterClass attacking)
	{
		this.defending = defending;
		this.attacking = attacking;
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
