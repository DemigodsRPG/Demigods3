package com.censoredsoftware.demigods.engine.mythos;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Set;

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
		UNENCHANTABLE
	}

	public enum Category
	{
		ARMOR, WEAPON, CONSUMABLE, BOOK
	}
}
