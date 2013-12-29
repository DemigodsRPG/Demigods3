package com.censoredsoftware.demigods.greek.item;

import java.util.Set;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.censoredsoftware.demigods.engine.item.DivineItem;
import com.censoredsoftware.demigods.greek.item.armor.BootsOfPagos;
import com.censoredsoftware.demigods.greek.item.armor.FaultyBootsOfHermes;
import com.censoredsoftware.demigods.greek.item.book.BookOfPrayer;
import com.censoredsoftware.demigods.greek.item.book.WelcomeBook;
import com.censoredsoftware.demigods.greek.item.weapon.BowOfTria;

public abstract class GreekItem implements DivineItem
{
	/**
	 * Books
	 */
	public static final BookOfPrayer BOOK_OF_PRAYER = new BookOfPrayer();
	public static final WelcomeBook WELCOME_BOOK = new WelcomeBook();

	/**
	 * Weapons
	 */
	public static final BowOfTria BOW_OF_TRIA = new BowOfTria();

	/**
	 * Armor
	 */
	public static final BootsOfPagos BOOTS_OF_PAGOS = new BootsOfPagos();
	public static final FaultyBootsOfHermes FAULTY_BOOTS_OF_HERMES = new FaultyBootsOfHermes();

	private final String name;
	private final String description;
	private final Set<Flag> flags;
	private final Category category;
	private final ItemStack item;
	private final Recipe recipe;
	private final Listener listener;

	public GreekItem(String name, String description, Set<Flag> flags, Category category, ItemStack item, Recipe recipe, Listener listener)
	{
		this.name = name;
		this.description = description;
		this.flags = flags;
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

	public Set<Flag> getFlags()
	{
		return flags;
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
