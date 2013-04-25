package com.censoredsoftware.Objects.Character.Factories;

import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Objects.Character.PlayerCharacterInventory;

/**
 * Factory for creating PlayerCharacterInventory objects.
 */
public class PlayerCharacterInventoryFactory
{
	private Plugin plugin;

	/**
	 * Constructor for the factory.
	 * 
	 * @param instance The plugin using this factory.
	 */
	public PlayerCharacterInventoryFactory(Plugin instance)
	{
		this.plugin = instance;
	}

	/**
	 * Create a PlayerCharacterInventory from an <code>inventory</code>.
	 * 
	 * @param inventory The Inventory being converted.
	 * @return The PlayerCharacterInventory.
	 */
	public PlayerCharacterInventory create(Inventory inventory)
	{
		return new PlayerCharacterInventory(plugin, inventory, null);
	}

	/**
	 * Create a PlayerCharacterInventory, named <code>name</code>, from an <code>inventory</code>.
	 * 
	 * @param inventory The Inventory being converted.
	 * @param name The name of the PlayerCharacterInventory.
	 * @return The PlayerCharacterInventory.
	 */
	public PlayerCharacterInventory create(Inventory inventory, String name)
	{
		return new PlayerCharacterInventory(plugin, inventory, name);
	}
}
