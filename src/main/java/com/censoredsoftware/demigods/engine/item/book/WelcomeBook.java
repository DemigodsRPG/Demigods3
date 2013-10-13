package com.censoredsoftware.demigods.engine.item.book;

import com.censoredsoftware.demigods.engine.helper.ColoredStringBuilder;
import com.censoredsoftware.demigods.engine.util.Items;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class WelcomeBook
{
	public final static ItemStack book = Items.createBook(ChatColor.DARK_AQUA + "Read me!", "?????", new ArrayList<String>(2)
	{
		{
			add(new ColoredStringBuilder().black("Welcome, adventurer, to the world of ").bold().purple("Demigods RPG").removeBold().black("!").build());
			add("Allan please add details");
		}
	}, Lists.newArrayList("Important information."));
}
