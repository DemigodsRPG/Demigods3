package com.censoredsoftware.demigods.engine.item;

import java.util.NoSuchElementException;
import java.util.Set;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.censoredsoftware.censoredlib.util.Items;
import com.censoredsoftware.demigods.engine.Demigods;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

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
		UNENCHANTABLE;
	}

	public enum Category
	{
		ARMOR, WEAPON, CONSUMABLE, BOOK;
	}

	public static class Util
	{
		public static DivineItem findDivineItem(final ItemStack item)
		{
			try
			{
				return Iterables.find(Demigods.mythos().getDivineItems(), new Predicate<DivineItem>()
				{
					@Override
					public boolean apply(DivineItem foundItem)
					{
						return Items.areEqualIgnoreEnchantments(foundItem.getItem(), item);
					}
				});
			}
			catch(NoSuchElementException ignored)
			{}

			return null;
		}

		public static boolean hasFlag(final ItemStack item, Flag flag)
		{
			DivineItem divineItem = findDivineItem(item);
			return divineItem != null && divineItem.getFlags().contains(flag);
		}
	}
}
