package com.censoredsoftware.demigods.episodes.demo.item;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.demigods.engine.util.ItemUtility;

public enum Book
{
	/**
	 * Instructions
	 */
	FIRST_JOIN(ItemUtility.createBook(ChatColor.DARK_AQUA + "Instructions", "Server", new ArrayList<String>(2)
	{
		{
			add("Welcome, adventurer, to the world of demigods!");
			add("Allan please add details");
		}
	}, null));

	private ItemStack value;

	private Book(ItemStack value)
	{
		this.value = value;
	}

	public ItemStack getBook()
	{
		return this.value;
	}
}
