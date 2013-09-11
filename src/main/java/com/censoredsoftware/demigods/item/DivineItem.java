package com.censoredsoftware.demigods.item;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.censoredsoftware.demigods.item.divine.ButtSword;

public enum DivineItem
{
	BUTT_SWORD(new ButtSword());

	private final Item item;

	private DivineItem(Item item)
	{
		this.item = item;
	}

	public Item getSpecialItem()
	{
		return item;
	}

	public static interface Item
	{
		public ItemStack getItem();

		public Recipe getRecipe();

		public Listener getUniqueListener();
	}
}
