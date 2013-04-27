package com.censoredsoftware.Demigods.Event.Demigod;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/*
 * Represents an event that is called when a character is created.
 */
public class DemigodCreateEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private OfflinePlayer owner;
	private String name;
	private String deity;
	private boolean cancelled = false;

	public DemigodCreateEvent(final OfflinePlayer owner, final String name, final String deity)
	{
		this.owner = owner;
		this.name = name;
		this.deity = deity;
	}

	/*
	 * getOwner() : Gets the player.
	 */
	public OfflinePlayer getOwner()
	{
		return this.owner;
	}

	/*
	 * getName() : Gets the name of the character.
	 */
	public String getName()
	{
		return this.name;
	}

	/*
	 * getDeity() : Gets the deity involved.
	 */
	public String getDeity()
	{
		return this.deity;
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
