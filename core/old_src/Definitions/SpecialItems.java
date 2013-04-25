package com.censoredsoftware.Demigods.Definitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class SpecialItems
{
	/*
	 * Returns all weapons that can spawn/drop/etc from Demigods
	 * 
	 * Map format is as follows: Map<Rarity, Item> / Rarity: % / Item: ItemStack
	 */
	public static Map<Double, ItemStack> getBooks()
	{
		Map<Double, ItemStack> items = new HashMap<Double, ItemStack>();
		ArrayList<String> pages = new ArrayList<String>();

		// _Alex's Secret Book
		pages.clear();
		pages.add("Whoa... you found my book! Please be careful with it. It can be pretty powerful!");
		pages.add("Shh!");
		items.put(25.0, API.item.createBook(ChatColor.GOLD + "_Alex's Secret Book", "_Alex", pages, null));

		// A Terrible Joke Book
		pages.clear();
		pages.add("I want to die peacefully in my sleep, like my grandfather... Not screaming and yelling like the passengers in his car.");
		pages.add("hehehhe");
		items.put(100.0, API.item.createBook(ChatColor.GREEN + "Joke Book #1", "A Terrible Comedian", pages, null));

		// Hidden Allure
		pages.clear();
		pages.add("The thirst for power has no end... It is more responsible for suffering than most evils in this world.");
		pages.add("Those with power are responsible for the end of time. The end is near and it will assure my omnipotence.");
		pages.add("I will be watching you.");
		for(int i = 0; i < 25; i++)
		{
			pages.add("...");
		}

		items.put(75.0, API.item.createBook(ChatColor.BLACK + "Allure of Fate", "The Watcher", pages, null));

		return items;
	}
}
