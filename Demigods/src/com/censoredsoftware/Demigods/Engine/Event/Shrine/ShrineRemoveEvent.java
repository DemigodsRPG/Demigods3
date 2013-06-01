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
	private Location location;
	private boolean cancelled = false;

	public ShrineRemoveEvent(final PlayerCharacter owner, final Location location)
	{
		this.owner = owner;
		this.location = location;
	}

	public PlayerCharacter getCharacter()
	{
		return this.owner;
	}

	public Location getLocation()
	{
		return this.location;
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
