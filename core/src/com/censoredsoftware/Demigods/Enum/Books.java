package com.censoredsoftware.Demigods.Enum;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.Demigods.API.ItemAPI;

public enum Books
{
	/**
	 * Instructions
	 */
	FIRST_JOIN(new Book(0.0, ItemAPI.createBook(ChatColor.DARK_AQUA + "Instructions", "Server", new ArrayList<String>()
	{
		{
			add(ChatColor.UNDERLINE + "Welcome, adventurer, to the world of Demigods!");
			add("Allan Please Add Details");
		}
	}, null))),

	/**
	 * _Alex's Secret Book
	 */
	ALEX_SECRET_BOOK(new Book(25.0, ItemAPI.createBook(ChatColor.GOLD + "_Alex's Secret Book", "_Alex", new ArrayList<String>()
	{
		{
			add("Whoa... you found my book! Please be careful with it. It can be pretty powerful!");
			add("Shh!");
		}
	}, null))),

	/**
	 * The Book of Ages
	 */
	BOOK_OF_AGES(new Book(75.0, ItemAPI.createBook(ChatColor.MAGIC + "The Book of Ages", "Steve", new ArrayList<String>()
	{
		{
			add(ChatColor.MAGIC + "You cannot comprehend the power contained in this book.");
		}
	}, null)));

	private Book value;

	private Books(Book value)
	{
		this.value = value;
	}

	public Book getBook()
	{
		return this.value;
	}

	public static class Book
	{
		private Double spawnChance;
		private ItemStack item;

		public Book(Double spawnChance, ItemStack item)
		{
			this.spawnChance = spawnChance;
			this.item = item;
		}

		public ItemStack getItem()
		{
			return item;
		}

		public Double getSpawnChance()
		{
			return spawnChance;
		}
	}
}
