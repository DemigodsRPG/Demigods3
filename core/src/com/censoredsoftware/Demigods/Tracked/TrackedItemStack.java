package com.censoredsoftware.Demigods.Tracked;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.censoredsoftware.Modules.Data.IntegerDataModule;
import com.censoredsoftware.Modules.Data.StringDataModule;

public class TrackedItemStack
{
	private StringDataModule specialItemStackData;
	private IntegerDataModule enchantmentsData;

	public TrackedItemStack(ItemStack item, String name)
	{
		specialItemStackData = new StringDataModule();
		enchantmentsData = new IntegerDataModule();

		if(name != null) specialItemStackData.saveData("NAME", name);

		specialItemStackData.saveData("TYPE", item.getTypeId());
		specialItemStackData.saveData("DURABILITY", item.getDurability());
		specialItemStackData.saveData("AMOUNT", item.getAmount());

		if(item.hasItemMeta())
		{
			if(item.getType().equals(Material.WRITTEN_BOOK))
			{
				BookMeta bookMeta = (BookMeta) item.getItemMeta();
				if(bookMeta.hasAuthor()) specialItemStackData.saveData("AUTHOR", bookMeta.getAuthor());
				if(bookMeta.hasPages()) specialItemStackData.saveData("PAGES", bookMeta.getPages());
				if(bookMeta.hasLore()) specialItemStackData.saveData("LORE", bookMeta.getLore());
				if(bookMeta.hasTitle()) specialItemStackData.saveData("TITLE", bookMeta.getTitle());
				if(bookMeta.hasDisplayName()) specialItemStackData.saveData("DISPLAY_NAME", bookMeta.getDisplayName());
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
			if(item.getItemMeta().hasDisplayName()) specialItemStackData.saveData("DISPLAY_NAME", item.getItemMeta().getDisplayName());
			if(item.getItemMeta().hasLore()) specialItemStackData.saveData("LORE", item.getItemMeta().getLore());
		}
	}

	public boolean hasName()
	{
		return specialItemStackData.containsKey("NAME");
	}

	public String getName()
	{
		return specialItemStackData.getDataString("NAME");
	}

	public void setName(String name)
	{
		specialItemStackData.saveData("NAME", name);
	}

	/*
	 * toItemStack() : Converts the TrackedItemStack to a usable ItemStack.
	 */
	public ItemStack toItemStack()
	{
		ItemStack item = new ItemStack(specialItemStackData.getDataInt("TYPE"), specialItemStackData.getDataInt("AMOUNT"));

		if(item.getType().equals(Material.WRITTEN_BOOK))
		{
			BookMeta meta = (BookMeta) item.getItemMeta();
			if(specialItemStackData.containsKey("TITLE")) meta.setTitle(specialItemStackData.getDataString("TITLE"));
			if(specialItemStackData.containsKey("AUTHOR")) meta.setAuthor(specialItemStackData.getDataString("AUTHOR"));
			if(specialItemStackData.containsKey("PAGES")) meta.setPages((List<String>) specialItemStackData.getDataObject("PAGES"));
			if(specialItemStackData.containsKey("LORE")) meta.setLore((List<String>) specialItemStackData.getDataObject("LORE"));
			if(specialItemStackData.containsKey("DISPLAY_NAME")) meta.setDisplayName(specialItemStackData.getDataString("DISPLAY_NAME"));
			item.setItemMeta(meta);
		}
		else
		{
			ItemMeta meta = item.getItemMeta();
			if(specialItemStackData.containsKey("DISPLAY_NAME")) meta.setDisplayName(specialItemStackData.getDataString("DISPLAY_NAME"));
			if(specialItemStackData.containsKey("LORE")) meta.setLore((List<String>) specialItemStackData.getDataObject("LORE"));
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
		item.setAmount(specialItemStackData.getDataInt("AMOUNT"));
		item.setDurability(specialItemStackData.getDataShort("DURABILITY"));

		return item;
	}

	public StringDataModule grabSpecialItemStackData()
	{
		return this.specialItemStackData;
	}

	public IntegerDataModule grabEnchantmentsData()
	{
		return this.enchantmentsData;
	}
}
