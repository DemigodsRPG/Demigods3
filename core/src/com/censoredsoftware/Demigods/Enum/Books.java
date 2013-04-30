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
	ALEX_BOOK(new Book(25.0, ItemAPI.createBook(ChatColor.GOLD + "_Alex's Secret Book", "_Alex", new ArrayList<String>()
	{
		{
			add("Whoa... you found my book! Please be careful with it. It can be pretty powerful!");
			add("Shh!");
		}
	}, null))),

	/**
	 * HQM's Secret Book
	 */
	HQM_BOOK(new Book(25.0, ItemAPI.createBook(ChatColor.GOLD + "HQM's Secret Book", "HmmmQuestionMark", new ArrayList<String>()
	{
		{
			add("Much better than _Alex's book, rite?");
			add("ROFL");
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

	public class Book
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
