package com.censoredsoftware.demigods.item;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.core.util.Items;

public enum Book
{
	/**
	 * Instructions
	 */
	FIRST_JOIN(Items.createBook(ChatColor.DARK_AQUA + "Instructions", "Server", new ArrayList<String>(2)
	{
		{
			add("Welcome, adventurer, to the world of demigods!");
			add("Allan please add details");
		}
	}, null));

	private final ItemStack value;

	private Book(ItemStack value)
	{
		this.value = value;
	}

	public ItemStack getBook()
	{
		return this.value;
	}
}
