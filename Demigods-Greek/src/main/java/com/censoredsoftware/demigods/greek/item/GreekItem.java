package com.censoredsoftware.demigods.greek.item;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.censoredsoftware.demigods.engine.item.DivineItem;
import com.censoredsoftware.demigods.greek.item.armor.BootsOfPagos;
import com.censoredsoftware.demigods.greek.item.book.BookOfPrayer;
import com.censoredsoftware.demigods.greek.item.book.WelcomeBook;

public enum GreekItem implements DivineItem
{
	/**
	 * Books
	 */
	BOOK_OF_PRAYER(BookOfPrayer.name, BookOfPrayer.description, BookOfPrayer.category, BookOfPrayer.item, BookOfPrayer.recipe, BookOfPrayer.listener), WELCOME_BOOK(WelcomeBook.name, WelcomeBook.description, WelcomeBook.category, WelcomeBook.item, null, null),

	/**
	 * Weapons
	 */
	// BUTT_SWORD(ButtSword.buttSword, ButtSword.recipe, ButtSword.listener), DEATH_BOW(DeathBow.deathBow, DeathBow.recipe, DeathBow.listener);

	/**
	 * Armor
	 */
	BOOTS_OF_PAGOS(BootsOfPagos.name, BootsOfPagos.description, BootsOfPagos.category, BootsOfPagos.item, BootsOfPagos.recipe, BootsOfPagos.listener);

	private final String name;
	private final String description;
	private final Category category;
	private final ItemStack item;
	private final Recipe recipe;
	private final Listener listener;

	private GreekItem(String name, String description, Category category, ItemStack item, Recipe recipe, Listener listener)
	{
		this.name = name;
		this.description = description;
		this.category = category;
		this.item = item;
		this.recipe = recipe;
		this.listener = listener;
	}

    @Override
	public String toString()
	{
		return name;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public Category getCategory()
	{
		return category;
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
