package com.censoredsoftware.demigods.item.divine.book;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.censoredsoftware.demigods.helper.ColoredStringBuilder;
import com.censoredsoftware.demigods.item.DivineItem;
import com.censoredsoftware.demigods.util.Items;
import com.google.common.collect.Lists;

public class WelcomeBook implements DivineItem.Item
{
	@Override
	public ItemStack getItem()
	{
		return Items.createBook(ChatColor.DARK_AQUA + "Read me!", "?????", new ArrayList<String>(2)
		{
			{
				add(new ColoredStringBuilder().black("Welcome, adventurer, to the world of ").bold().purple("Demigods RPG").removeBold().black("!").build());
				add("Allan please add details");
			}
		}, Lists.newArrayList("Important information."));

	}

	@Override
	public Recipe getRecipe()
	{
		return null;
	}

	@Override
	public Listener getUniqueListener()
	{
		return null;
	}
}
