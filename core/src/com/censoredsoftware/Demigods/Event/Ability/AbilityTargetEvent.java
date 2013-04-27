package com.censoredsoftware.Demigods.Event.Ability;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterClass;

/*
 * Represents an event that is called when an ability is executed.
 */
public class AbilityTargetEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private final PlayerCharacterClass character;
	private final LivingEntity target;
	private boolean cancelled = false;

	public AbilityTargetEvent(final PlayerCharacterClass character, final LivingEntity target)
	{
		this.character = character;
		this.target = target;
	}

	/*
	 * getCharacter() : Gets the character involved.
	 */
	public PlayerCharacterClass getCharacter()
	{
		return this.character;
	}

	/*
	 * getTarget() : Gets the target involved.
	 */
	public LivingEntity getTarget()
	{
		return this.target;
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
