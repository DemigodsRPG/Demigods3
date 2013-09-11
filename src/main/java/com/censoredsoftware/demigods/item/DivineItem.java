package com.censoredsoftware.demigods.item;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.censoredsoftware.demigods.item.divine.ButtSword;
import com.censoredsoftware.demigods.item.divine.DeathBow;
import com.censoredsoftware.demigods.item.divine.book.BookOfPrayer;
import com.censoredsoftware.demigods.item.divine.book.WelcomeBook;

public enum DivineItem
{
	BOOK_OF_PRAYER(new BookOfPrayer()), WELCOME_BOOK(new WelcomeBook()), BUTT_SWORD(new ButtSword()), DEATH_BOW(new DeathBow());

	private final Item item;

	private DivineItem(Item item)
	{
		this.item = item;
	}

	public Item getSpecialItem()
	{
		return item;
	}

	public static interface Item
	{
		public ItemStack getItem();

		public Recipe getRecipe();

		public Listener getUniqueListener();
	}
}
