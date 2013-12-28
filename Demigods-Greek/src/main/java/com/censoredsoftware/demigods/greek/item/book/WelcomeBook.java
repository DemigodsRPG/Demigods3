package com.censoredsoftware.demigods.greek.item.book;

import com.censoredsoftware.censoredlib.util.Items;
import com.censoredsoftware.demigods.engine.item.DivineItem;
import com.censoredsoftware.demigods.greek.item.GreekItem;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class WelcomeBook extends GreekItem
{
	public final static String name = "Welcome Book";
	public final static String description = "Welcome, adventurer, to the world of Demigods!";
	public final static DivineItem.Category category = DivineItem.Category.BOOK;
	public final static ItemStack item = Items.createBook(ChatColor.DARK_AQUA + "Read me!", "Demigods", new ArrayList<String>(2)
	{
		{
			add("This is Demigods RPG!");
			add("Allan please add details");
		}
	}, Lists.newArrayList("Important information."));

	public WelcomeBook()
	{
		super(name, description, category, item, null, null);
	}
}
