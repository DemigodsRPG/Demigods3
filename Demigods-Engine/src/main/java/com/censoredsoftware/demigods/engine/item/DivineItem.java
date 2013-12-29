package com.censoredsoftware.demigods.engine.item;

import java.util.Set;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public interface DivineItem
{
	@Override
	public String toString();

	public String getName();

	public String getDescription();

	public Set<Flag> getFlags();

	public Category getCategory();

	public ItemStack getItem();

	public Recipe getRecipe();

	public Listener getUniqueListener();

	public enum Flag
	{
		UNENCHANTABLE, UNREPAIRABLE;
	}

	public enum Category
	{
		ARMOR, WEAPON, CONSUMABLE, BOOK;
	}
}
