package com.censoredsoftware.Demigods.Enum;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.Demigods.API.ItemAPI;

public enum Books
{
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
		private Double chance;
		private ItemStack item;

		public Book(Double chance, ItemStack item)
		{
			this.chance = chance;
			this.item = item;
		}

		public ItemStack getItem()
		{
			return item;
		}

		public Double getChance()
		{
			return chance;
		}
	}
}
