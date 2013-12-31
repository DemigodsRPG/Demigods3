package com.censoredsoftware.demigods.engine.mythos;

import java.util.Set;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public interface DivineItem
{
	@Override
	String toString();

	String getName();

	String getDescription();

	Set<Flag> getFlags();

	Category getCategory();

	ItemStack getItem();

	Recipe getRecipe();

	Listener getUniqueListener();

	public enum Flag
	{
		UNENCHANTABLE;
	}

	public enum Category
	{
		ARMOR, WEAPON, CONSUMABLE, BOOK;
	}
}
