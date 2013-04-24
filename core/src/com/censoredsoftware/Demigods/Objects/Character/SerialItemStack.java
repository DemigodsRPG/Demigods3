package com.censoredsoftware.Demigods.Objects.Character;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SerialItemStack implements Serializable
{
	private static final long serialVersionUID = -5645654430614861947L;

	private int type;
	private int amount;
	private short durability;
	private HashMap<Integer, Integer> enchantments = new HashMap<Integer, Integer>();
	private String displayName = null, author = null, title = null;
	private List<String> lore = null, pages = null;
	private Map<String, Object> bookMeta = null;

	public SerialItemStack(ItemStack item)
	{
		this.type = item.getTypeId();
		this.durability = item.getDurability();
		this.amount = item.getAmount();

		if(item.hasItemMeta())
		{
			if(item.getType().equals(Material.WRITTEN_BOOK))
			{
				BookMeta bookMeta = (BookMeta) item.getItemMeta();

				this.bookMeta = bookMeta.serialize();

				if(bookMeta.hasAuthor()) this.author = bookMeta.getAuthor();
				if(bookMeta.hasPages()) this.pages = bookMeta.getPages();
				if(bookMeta.hasLore()) this.lore = bookMeta.getLore();
				if(bookMeta.hasTitle()) this.title = bookMeta.getTitle();
				if(bookMeta.hasDisplayName()) this.displayName = bookMeta.getDisplayName();

				if(bookMeta.hasEnchants())
				{
					for(Entry<Enchantment, Integer> ench : bookMeta.getEnchants().entrySet())
					{
						this.enchantments.put(ench.getKey().getId(), ench.getValue());
					}
				}
			}

			if(item.getItemMeta().hasEnchants())
			{
				for(Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet())
				{
					this.enchantments.put(ench.getKey().getId(), ench.getValue());
				}
			}
			if(item.getItemMeta().hasDisplayName()) this.displayName = item.getItemMeta().getDisplayName();
			if(item.getItemMeta().hasLore()) this.lore = item.getItemMeta().getLore();
		}
	}

	/*
	 * toItemStack() : Converts the SerialItemStack to a usuable ItemStack.
	 */
	public ItemStack toItemStack()
	{
		ItemStack item = new ItemStack(this.type, this.amount);

		if(item.getType().equals(Material.WRITTEN_BOOK))
		{
			BookMeta meta = (BookMeta) item.getItemMeta();
			if(this.title != null) meta.setTitle(this.title);
			if(this.author != null) meta.setAuthor(this.author);
			if(this.pages != null) meta.setPages(this.pages);
			if(this.lore != null) meta.setLore(this.lore);
			if(this.displayName != null) meta.setDisplayName(this.displayName);
			item.setItemMeta(meta);
		}
		else
		{
			ItemMeta meta = item.getItemMeta();
			if(this.displayName != null) meta.setDisplayName(this.displayName);
			if(this.lore != null) meta.setLore(this.lore);
			item.setItemMeta(meta);
		}

		if(this.enchantments != null)
		{
			for(Entry<Integer, Integer> ench : this.enchantments.entrySet())
			{
				item.addEnchantment(Enchantment.getById(ench.getKey()), ench.getValue());
			}
		}

		// Set data for the Item
		item.setAmount(this.amount);
		item.setDurability(this.durability);

		return item;
	}
}
