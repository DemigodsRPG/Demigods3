package com.censoredsoftware.Demigods.Engine.Event.Character;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

/*
 * Represents an event that is called when a player switched characters.
 */
public class CharacterSwitchEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private OfflinePlayer player;
	private PlayerCharacter toCharacter;
	private PlayerCharacter fromCharacter;
	private boolean cancelled = false;

	public CharacterSwitchEvent(final OfflinePlayer player, final PlayerCharacter fromCharacter, final PlayerCharacter toCharacter)
	{
		this.player = player;
		this.toCharacter = toCharacter;
		this.fromCharacter = fromCharacter;
	}

	/*
	 * getOfflinePlayer() : Gets the player.
	 */
	public OfflinePlayer getOwner()
	{
		return this.player;
	}

	/*
	 * getCharacterTo() : Gets the character being switched to.
	 */
	public PlayerCharacter getCharacterTo()
	{
		return this.toCharacter;
	}

	/*
	 * getCharacterTo() : Gets the character being switched to.
	 */
	public PlayerCharacter getCharacterFrom()
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
