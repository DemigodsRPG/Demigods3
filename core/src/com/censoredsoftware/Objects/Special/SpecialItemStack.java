package com.censoredsoftware.Objects.Special;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Modules.DataPersistence.EnumDataModule;
import com.censoredsoftware.Modules.DataPersistence.IntegerDataModule;

public class SpecialItemStack
{
	private EnumDataModule specialItemStackData;
	private IntegerDataModule enchantmentsData;

	public SpecialItemStack(Plugin instance, ItemStack item, String name)
	{
		specialItemStackData = new EnumDataModule(instance);
		enchantmentsData = new IntegerDataModule(instance);

		if(name != null) specialItemStackData.saveData(SpecialItemStackData.NAME, name);

		specialItemStackData.saveData(SpecialItemStackData.TYPE, item.getTypeId());
		specialItemStackData.saveData(SpecialItemStackData.DURABILITY, item.getDurability());
		specialItemStackData.saveData(SpecialItemStackData.AMOUNT, item.getAmount());

		if(item.hasItemMeta())
		{
			if(item.getType().equals(Material.WRITTEN_BOOK))
			{
				BookMeta bookMeta = (BookMeta) item.getItemMeta();
				if(bookMeta.hasAuthor()) specialItemStackData.saveData(SpecialItemStackData.AUTHOR, bookMeta.getAuthor());
				if(bookMeta.hasPages()) specialItemStackData.saveData(SpecialItemStackData.PAGES, bookMeta.getPages());
				if(bookMeta.hasLore()) specialItemStackData.saveData(SpecialItemStackData.LORE, bookMeta.getLore());
				if(bookMeta.hasTitle()) specialItemStackData.saveData(SpecialItemStackData.TITLE, bookMeta.getTitle());
				if(bookMeta.hasDisplayName()) specialItemStackData.saveData(SpecialItemStackData.DISPLAY_NAME, bookMeta.getDisplayName());
				if(bookMeta.hasEnchants())
				{
					for(Entry<Enchantment, Integer> ench : bookMeta.getEnchants().entrySet())
					{
						enchantmentsData.saveData(ench.getKey().getId(), ench.getValue());
					}
				}
			}

			if(item.getItemMeta().hasEnchants())
			{
				for(Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet())
				{
					enchantmentsData.saveData(ench.getKey().getId(), ench.getValue());
				}
			}
			if(item.getItemMeta().hasDisplayName()) specialItemStackData.saveData(SpecialItemStackData.DISPLAY_NAME, item.getItemMeta().getDisplayName());
			if(item.getItemMeta().hasLore()) specialItemStackData.saveData(SpecialItemStackData.LORE, item.getItemMeta().getLore());
		}
	}

	public boolean hasName()
	{
		return specialItemStackData.containsKey(SpecialItemStackData.NAME);
	}

	public String getName()
	{
		return specialItemStackData.getDataString(SpecialItemStackData.NAME);
	}

	public void setName(String name)
	{
		specialItemStackData.saveData(SpecialItemStackData.NAME, name);
	}

	/*
	 * toItemStack() : Converts the SpecialItemStack to a usable ItemStack.
	 */
	public ItemStack toItemStack()
	{
		ItemStack item = new ItemStack(specialItemStackData.getDataInt(SpecialItemStackData.TYPE), specialItemStackData.getDataInt(SpecialItemStackData.AMOUNT));

		if(item.getType().equals(Material.WRITTEN_BOOK))
		{
			BookMeta meta = (BookMeta) item.getItemMeta();
			if(specialItemStackData.containsKey(SpecialItemStackData.TITLE)) meta.setTitle(specialItemStackData.getDataString(SpecialItemStackData.TITLE));
			if(specialItemStackData.containsKey(SpecialItemStackData.AUTHOR)) meta.setAuthor(specialItemStackData.getDataString(SpecialItemStackData.AUTHOR));
			if(specialItemStackData.containsKey(SpecialItemStackData.PAGES)) meta.setPages((List<String>) specialItemStackData.getDataObject(SpecialItemStackData.PAGES));
			if(specialItemStackData.containsKey(SpecialItemStackData.LORE)) meta.setLore((List<String>) specialItemStackData.getDataObject(SpecialItemStackData.LORE));
			if(specialItemStackData.containsKey(SpecialItemStackData.DISPLAY_NAME)) meta.setDisplayName(specialItemStackData.getDataString(SpecialItemStackData.DISPLAY_NAME));
			item.setItemMeta(meta);
		}
		else
		{
			ItemMeta meta = item.getItemMeta();
			if(specialItemStackData.containsKey(SpecialItemStackData.DISPLAY_NAME)) meta.setDisplayName(specialItemStackData.getDataString(SpecialItemStackData.DISPLAY_NAME));
			if(specialItemStackData.containsKey(SpecialItemStackData.LORE)) meta.setLore((List<String>) specialItemStackData.getDataObject(SpecialItemStackData.LORE));
			item.setItemMeta(meta);
		}

		if(enchantmentsData.listKeys() != null)
		{
			for(Integer key : enchantmentsData.listKeys())
			{
				item.addEnchantment(Enchantment.getById(key), enchantmentsData.getDataInt(key));
			}
		}

		// Set data for the Item
		item.setAmount(specialItemStackData.getDataInt(SpecialItemStackData.AMOUNT));
		item.setDurability(specialItemStackData.getDataShort(SpecialItemStackData.DURABILITY));

		return item;
	}

	/**
	 * Enum defining the data being held in this object.
	 */
	public static enum SpecialItemStackData
	{
		/**
		 * Integer: Material type ID.
		 */
		TYPE,

		/**
		 * Integer: Amount of item.
		 */
		AMOUNT,

		/**
		 * Short: Durability of item.
		 */
		DURABILITY,

		/**
		 * String: Display name of item/book.
		 */
		DISPLAY_NAME,

		/**
		 * String: Author of book.
		 */
		AUTHOR,

		/**
		 * String: Title of book.
		 */
		TITLE,

		/**
		 * List<String>: Lore of item.
		 */
		LORE,

		/**
		 * List<String>: Pages of book.
		 */
		PAGES,

		/**
		 * String: Internal name of the SpecialItemStack.
		 */
		NAME
	}
}
