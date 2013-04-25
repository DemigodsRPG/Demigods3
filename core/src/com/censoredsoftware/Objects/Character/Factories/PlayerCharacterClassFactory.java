package com.censoredsoftware.Objects.Character.Factories;

import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Objects.Character.PlayerCharacter;
import com.censoredsoftware.Objects.Character.PlayerCharacterClass;

/**
 * Factory for creating PlayerCharacterClass objects.
 */
public class PlayerCharacterClassFactory
{
	private Plugin plugin;
	private int globalMaxFavor;

	/**
	 * Constructor for the factory.
	 * 
	 * @param instance The plugin using this factory.
	 */
	public PlayerCharacterClassFactory(Plugin instance, int globalMaxFavor)
	{
		this.plugin = instance;
		this.globalMaxFavor = globalMaxFavor;
	}

	/**
	 * Create a PlayerCharacterClass from scratch.
	 * 
	 * @param character The character holding this class.
	 * @param className The name of the class.
	 * @param teamName The team this character class belongs to.
	 * @param favor The favor points.
	 * @param maxFavor The max favor.
	 * @param devotion The devotion points.
	 * @param ascensions The ascension points.
	 * @param offense The offense level.
	 * @param defense The defense level.
	 * @param stealth The stealth level.
	 * @param support The support level.
	 * @param passive The passive level.
	 * @param active True if active.
	 * @return The PlayerCharacterClass object.
	 */
	public PlayerCharacterClass create(PlayerCharacter character, String className, String teamName, int favor, int maxFavor, int devotion, int ascensions, int offense, int defense, int stealth, int support, int passive, boolean active)
	{
		return new PlayerCharacterClass(plugin, globalMaxFavor, character, className, teamName, favor, maxFavor, devotion, ascensions, offense, defense, stealth, support, passive, active);
	}
}
