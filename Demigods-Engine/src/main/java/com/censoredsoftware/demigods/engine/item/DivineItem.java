package com.censoredsoftware.demigods.engine.item;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public interface DivineItem
{
	@Override
	public String toString();

	public String getName();

	public String getDescription();

	public Category getCategory();

	public ItemStack getItem();

	public Recipe getRecipe();

	public Listener getUniqueListener();

	public enum Category
	{
		ARMOR, WEAPON, CONSUMABLE, BOOK;
	}
}
