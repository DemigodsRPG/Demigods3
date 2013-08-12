package com.censoredsoftware.demigods.item;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.core.bukkit.ColoredStringBuilder;
import com.censoredsoftware.core.util.Items;
import com.google.common.collect.Lists;

public enum Book
{
	/**
	 * Instructions
	 */
	FIRST_JOIN(Items.createBook(ChatColor.DARK_AQUA + "Read me!", "?????", new ArrayList<String>(2)
	{
		{
			add(new ColoredStringBuilder().black("Welcome, adventurer, to the world of ").bold().purple("Demigods RPG").removeBold().black("!").build());
			add("Allan please add details");
		}
	}, Lists.newArrayList("Important information.")));

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
