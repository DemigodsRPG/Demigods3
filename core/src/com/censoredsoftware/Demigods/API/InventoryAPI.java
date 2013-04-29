package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class InventoryAPI
{
	public static String toString(Inventory inventory)
	{
		if(inventory == null) return "";
		String serialization = inventory.getSize() + ";";
		for(int i = 0; i < inventory.getSize(); i++)
		{
			ItemStack is = inventory.getItem(i);
			if(is != null)
			{
				String serializedItemStack = "";

				String isType = String.valueOf(is.getType().getId());
				serializedItemStack += "t@" + isType;

				if(is.getDurability() != 0)
				{
					String isDurability = String.valueOf(is.getDurability());
					serializedItemStack += ":d@" + isDurability;
				}

				if(is.getAmount() != 1)
				{
					String isAmount = String.valueOf(is.getAmount());
					serializedItemStack += ":a@" + isAmount;
				}

				Map<Enchantment, Integer> isEnch = is.getEnchantments();
				if(isEnch.size() > 0)
				{
					for(Map.Entry<Enchantment, Integer> ench : isEnch.entrySet())
					{
						serializedItemStack += ":e@" + ench.getKey().getId() + "@" + ench.getValue();
					}
				}

				serialization += i + "#" + serializedItemStack + ";";
			}
		}
		return serialization;
	}

	public static Inventory toInventory(String string)
	{
		if(string.equals("")) return null;
		String[] serializedBlocks = string.split(";");
		String invInfo = serializedBlocks[0];
		Inventory deserializedInventory = Bukkit.getServer().createInventory(null, Integer.valueOf(invInfo));

		for(int i = 1; i < serializedBlocks.length; i++)
		{
			String[] serializedBlock = serializedBlocks[i].split("#");
			int stackPosition = Integer.valueOf(serializedBlock[0]);

			if(stackPosition >= deserializedInventory.getSize())
			{
				continue;
			}

			ItemStack is = null;
			Boolean createdItemStack = false;

			String[] serializedItemStack = serializedBlock[1].split(":");
			for(String itemInfo : serializedItemStack)
			{
				String[] itemAttribute = itemInfo.split("@");
				if(itemAttribute[0].equals("t"))
				{
					is = new ItemStack(Material.getMaterial(Integer.valueOf(itemAttribute[1])));
					createdItemStack = true;
				}
				else if(itemAttribute[0].equals("d") && createdItemStack)
				{
					is.setDurability(Short.valueOf(itemAttribute[1]));
				}
				else if(itemAttribute[0].equals("a") && createdItemStack)
				{
					is.setAmount(Integer.valueOf(itemAttribute[1]));
				}
				else if(itemAttribute[0].equals("e") && createdItemStack)
				{
					is.addEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
				}
			}
			deserializedInventory.setItem(stackPosition, is);
		}

		return deserializedInventory;
	}

	/**
	 * Converts the <code>item</code> to a string and returns it.
	 * 
	 * @param item the item to convert.
	 * @return String
	 */
	public static String ItemToString(ItemStack item)
	{
		// Define first string and other reference variables
		String string = "#item@type:" + item.getType().getId() + "@amt:" + item.getAmount() + "@dur:" + item.getDurability();
		Material material = item.getType();

		// Add book meta or leather meta
		if(material.equals(Material.WRITTEN_BOOK))
		{
			BookMeta bookMeta = (BookMeta) item.getItemMeta();
			if(bookMeta.hasTitle()) string = string + "@title:" + bookMeta.getTitle();
			if(bookMeta.hasAuthor()) string = string + "@author:" + bookMeta.getAuthor();
			if(bookMeta.hasPages()) string = string + "@pages:" + StringUtils.join(bookMeta.getPages(), "&");
		}
		else if(material.equals(Material.LEATHER_HELMET) || material.equals(Material.LEATHER_CHESTPLATE) || material.equals(Material.LEATHER_LEGGINGS) || material.equals(Material.LEATHER_BOOTS))
		{
			// TODO: Add leather support
		}

		// Add main meta data
		if(item.hasItemMeta())
		{
			if(item.getItemMeta().hasDisplayName()) string = string + "@name:" + item.getItemMeta().getDisplayName();
			if(item.getItemMeta().hasLore()) string = string + "@lore:" + StringUtils.join(item.getItemMeta().getLore(), "&");
		}

		// Handle enchantments
		if(item.hasItemMeta() && item.getItemMeta().hasEnchants())
		{
			String enchantments = "@ench:";
			for(Map.Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet())
			{
				enchantments = enchantments + ench.getKey().getId() + "|" + ench.getValue() + "&";
			}
			string = string + enchantments;
		}

		// Combine all of the variables and return it
		return string + ";";
	}

	/**
	 * Parses the serialized item <code>string</code> into an actual ItemStack
	 * and returns it.
	 * 
	 * @param string the string to parse.
	 * @return ItemStack
	 */
	public static ItemStack parseItem(String string)
	{
		// Do initial check to make sure it's an item string
		if(!string.startsWith("#item")) return null;

		// Define variables
		Map<String, String> options = new HashMap<String, String>();

		// Split the options and apply them to a Map
		String[] itemOptions = string.substring(6).replace(";", "").split("@");
		for(String value : itemOptions)
		{
			String[] option = value.split(":");
			options.put(option[0], option[1]);
		}

		// Now use the Map to create the object
		ItemStack item = new ItemStack(Integer.parseInt(options.get("type")));
		item.setAmount(Integer.parseInt(options.get("amt")));
		item.setDurability(Short.parseShort(options.get("dur")));

		// Add applicable meta data
		if(options.containsKey("name")) item.getItemMeta().setDisplayName(options.get("name"));
		if(options.containsKey("lore")) item.getItemMeta().setLore(new ArrayList(Arrays.asList(options.get("lore").split("&"))));
		if(options.containsKey("ench"))
		{
			String[] enchantments = options.get("ench").substring(0, options.get("ench").length() - 1).split("&");

			for(String enchant : enchantments)
			{
				// Split the enchant into its parts
				String[] details = enchant.split("|");

				// Add the enchantment
				item.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(details[0])), Integer.parseInt(details[1]));
			}
		}

		// Return the item
		return item;
	}
}
