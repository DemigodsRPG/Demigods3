package com.censoredsoftware.Demigods.Engine.Event.Ability;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Engine.Ability.AbilityInfo;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

/*
 * Represents an event that is called when an ability is executed.
 */
public class AbilityEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private String name;
	private PlayerCharacter character;
	private String deity;
	private int cost;
	private AbilityInfo info;
	private boolean cancelled = false;

	public AbilityEvent(final String name, final PlayerCharacter character, final int cost, AbilityInfo info)
	{
		this.name = name;
		this.character = character;
		this.deity = character.getDeity().getInfo().getName();
		this.cost = cost;
		this.info = info;
	}

	public String getName()
	{
		return this.name;
	}

	public PlayerCharacter getCharacter()
	{
		return this.character;
	}

	public String getDeity()
	{
		return this.deity;
	}

	public int getCost()
	{
		return this.cost;
	}

	public AbilityInfo getInfo()
	{
		return this.info;
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
