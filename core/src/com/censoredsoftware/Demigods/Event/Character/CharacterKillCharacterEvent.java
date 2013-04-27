package com.censoredsoftware.Demigods.Event.Character;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterClass;

/*
 * Represents an event that is called when a player is killed by another player.
 */
public class CharacterKillCharacterEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private PlayerCharacterClass attacker;
	private PlayerCharacterClass killedChar;

	public CharacterKillCharacterEvent(final PlayerCharacterClass attacker, final PlayerCharacterClass killedChar)
	{
		this.attacker = attacker;
		this.killedChar = killedChar;
	}

	/*
	 * getCharacter() : Gets the character.
	 */
	public PlayerCharacterClass getCharacter()
	{
		return this.attacker;
	}

	/*
	 * getKilled() : Gets the player that was killed by the player.
	 */
	public PlayerCharacterClass getKilled()
	{
		return this.killedChar;
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
}
