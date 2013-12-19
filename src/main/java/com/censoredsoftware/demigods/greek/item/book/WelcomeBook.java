package com.censoredsoftware.demigods.greek.item.book;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.censoredlib.helper.ColoredStringBuilder;
import com.censoredsoftware.censoredlib.util.Items;
import com.google.common.collect.Lists;

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
