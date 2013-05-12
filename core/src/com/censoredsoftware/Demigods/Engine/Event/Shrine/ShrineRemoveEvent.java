package com.censoredsoftware.Demigods.Engine.Event.Shrine;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

/*
 * Represents an event that is called when a Shrine is created.
 */
public class ShrineRemoveEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private PlayerCharacter owner;
	private Location block;
	private boolean cancelled = false;

	public ShrineRemoveEvent(final PlayerCharacter owner, final Location block)
	{
		this.owner = owner;
		this.block = block;
	}

	/*
	 * getPlayer() : Gets the character/owner.
	 */
	public PlayerCharacter getOwner()
	{
		return this.owner;
	}

	/*
	 * getLocation() : Gets the Shrine's location.
	 */
	public Location getLocation()
	{
		return this.block;
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
