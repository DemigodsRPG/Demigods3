package com.censoredsoftware.Demigods.Engine.Event.Character;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Engine.Object.PlayerCharacter.PlayerCharacter;

/*
 * Represents an callAbilityEvent that is called when a player is killed by another player.
 */
public class CharacterKillCharacterEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private PlayerCharacter attacker;
	private PlayerCharacter killedChar;

	public CharacterKillCharacterEvent(final PlayerCharacter attacker, final PlayerCharacter killedChar)
	{
		this.attacker = attacker;
		this.killedChar = killedChar;
	}

	/*
	 * getCharacter() : Gets the character.
	 */
	public PlayerCharacter getCharacter()
	{
		return this.attacker;
	}

	/*
	 * getKilled() : Gets the player that was killed by the player.
	 */
	public PlayerCharacter getKilled()
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
