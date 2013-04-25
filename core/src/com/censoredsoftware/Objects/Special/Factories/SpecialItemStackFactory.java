package com.censoredsoftware.Objects.Special.Factories;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Objects.Special.SpecialItemStack;

/**
 * Factory for creating SpecialItemStack objects.
 */
public class SpecialItemStackFactory
{
	private Plugin plugin;

	/**
	 * Constructor for the factory.
	 * 
	 * @param instance The plugin using this factory.
	 */
	public SpecialItemStackFactory(Plugin instance)
	{
		this.plugin = instance;
	}

	/**
	 * Create a SpecialItemStack from an <code>itemStack</code>.
	 * 
	 * @param itemStack The ItemStack being converted.
	 * @return The SpecialItemStack.
	 */
	public SpecialItemStack create(ItemStack itemStack)
	{
		return new SpecialItemStack(plugin, itemStack, null);
	}

	/**
	 * Create a SpecialItemStack, named <code>name</code>, from an <code>itemStack</code>.
	 * 
	 * @param itemStack The ItemStack being converted.
	 * @param name The internal name of the SpecialItemStack.
	 * @return The SpecialItemStack.
	 */
	public SpecialItemStack create(ItemStack itemStack, String name)
	{
		return new SpecialItemStack(plugin, itemStack, name);
	}
}
