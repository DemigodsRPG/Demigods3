package com.censoredsoftware.Demigods.Event.Demigod;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Demigod.Demigod;

/*
 * Represents an event that is called when a player switched characters.
 */
public class DemigodSwitchEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private OfflinePlayer player;
	private Demigod toCharacter;
	private Demigod fromCharacter;
	private boolean cancelled = false;

	public DemigodSwitchEvent(final OfflinePlayer player, final Demigod fromCharacter, final Demigod toCharacter)
	{
		this.player = player;
		this.toCharacter = toCharacter;
		this.fromCharacter = fromCharacter;
	}

	/*
	 * getOwner() : Gets the player.
	 */
	public OfflinePlayer getOwner()
	{
		return this.player;
	}

	/*
	 * getCharacterTo() : Gets the character being switched to.
	 */
	public Demigod getCharacterTo()
	{
		return this.toCharacter;
	}

	/*
	 * getCharacterTo() : Gets the character being switched to.
	 */
	public Demigod getCharacterFrom()
	{
		return this.fromCharacter;
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
