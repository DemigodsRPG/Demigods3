package com.legit2.Demigods.Libraries.Objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class SerialItemStack implements Serializable
{
	private static final long serialVersionUID = -5645654430614861947L;

	final int type;
	final int amount;
	final short durability;
	final HashMap<Integer, Integer> enchantments = new HashMap<Integer, Integer>();
	String displayName = null;
	List<String> lore = null;
	MaterialData materialData;

	public SerialItemStack(ItemStack item)
	{
		this.type = item.getTypeId();
		this.durability = item.getDurability();
		this.amount = item.getAmount();

		if(item.hasItemMeta())
		{
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

		// Set data for the Item
		item.setAmount(this.amount);
		item.setDurability(this.durability);
		item.setData(materialData);
		ItemMeta meta = item.getItemMeta();
		if(this.displayName != null) meta.setDisplayName(this.displayName);
		if(this.lore != null) meta.setLore(this.lore);
		item.setItemMeta(meta);

		if(this.enchantments != null)
		{
			for(Entry<Integer, Integer> ench : this.enchantments.entrySet())
			{
				item.addEnchantment(Enchantment.getById(ench.getKey()), ench.getValue());
			}
		}

		return item;
	}
}
