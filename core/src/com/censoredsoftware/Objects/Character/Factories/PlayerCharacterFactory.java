package com.censoredsoftware.Objects.Character.Factories;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Objects.Character.PlayerCharacter;

/**
 * Factory for creating PlayerCharacter objects.
 */
public class PlayerCharacterFactory
{
	private Plugin plugin;

	/**
	 * Constructor for the factory.
	 * 
	 * @param instance The plugin using this factory.
	 */
	public PlayerCharacterFactory(Plugin instance)
	{
		this.plugin = instance;
	}

	/**
	 * Create a new PlayerCharacter from a <code>player</code>.
	 * 
	 * @param player The player being converted.
	 * @param charID The ID of the PlayerCharacter.
	 * @param charName The name of the PlayerCharacter.
	 * @return The PlayerCharacter object.
	 */
	public PlayerCharacter create(OfflinePlayer player, int charID, String charName)
	{
		return new PlayerCharacter(plugin, player, charID, charName);
	}
}
