package com.censoredsoftware.demigods.item;

import com.censoredsoftware.demigods.item.book.BookOfPrayer;
import com.censoredsoftware.demigods.item.book.WelcomeBook;
import com.censoredsoftware.demigods.item.weapon.ButtSword;
import com.censoredsoftware.demigods.item.weapon.DeathBow;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public enum DivineItem
{
	/**
	 * Books
	 */
	BOOK_OF_PRAYER(BookOfPrayer.book, BookOfPrayer.recipe, BookOfPrayer.listener), WELCOME_BOOK(WelcomeBook.book, null, null),

	/**
	 * Weapons
	 */
	BUTT_SWORD(ButtSword.buttSword, ButtSword.recipe, ButtSword.listener), DEATH_BOW(DeathBow.deathBow, DeathBow.recipe, DeathBow.listener);

	private final ItemStack item;
	private final Recipe recipe;
	private final Listener listener;

	private DivineItem(ItemStack item, Recipe recipe, Listener listener)
	{
		this.item = item;
		this.recipe = recipe;
		this.listener = listener;
	}

	public ItemStack getItem()
	{
		return item;
	}

	public Recipe getRecipe()
	{
		return recipe;
	}

	public Listener getUniqueListener()
	{
		return listener;
	}
}
