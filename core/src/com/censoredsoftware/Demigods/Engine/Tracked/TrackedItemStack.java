package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import redis.clients.johm.*;

@Model
public class TrackedItemStack
{
	@Id
	private long id;
	@Attribute
	private int typeId;
	@Attribute
	private byte byteId;
	@Attribute
	private int amount;
	@Attribute
	private short durability;
	@CollectionMap(key = Integer.class, value = Integer.class)
	@Indexed
	private Map<Integer, Integer> enchantments; // Format: Map<ENCHANTMENT_ID, LEVEL>
	@Attribute
	private String name;
	@CollectionList(of = String.class)
	@Indexed
	private List<String> lore;
	@Attribute
	private String author;
	@Attribute
	private String title;
	@CollectionList(of = String.class)
	@Indexed
	private List<String> pages;
	@Attribute
	private ItemType type;

	/**
	 * Splits the object into its saveable pieces.
	 * 
	 * @param item the item to save.
	 */
	public TrackedItemStack(ItemStack item)
	{
		// Save the type first (could be changed later)
		this.type = ItemType.STANDARD;

		// Set the main variables
		this.typeId = item.getTypeId();
		this.byteId = item.getData().getData();
		this.amount = item.getAmount();
		this.durability = item.getDurability();
		if(item.getItemMeta().hasDisplayName()) this.name = item.getItemMeta().getDisplayName();
		if(item.getItemMeta().hasLore()) this.lore = item.getItemMeta().getLore();

		// If it has enchantments then save them
		if(item.getItemMeta().hasEnchants())
		{
			// Create the new HashMap
			enchantments = new HashMap<Integer, Integer>();

			for(Map.Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet())
			{
				enchantments.put(ench.getKey().getId(), ench.getValue());
			}
		}

		// If it's a written book then save the book-specific information
		if(item.getType().equals(Material.WRITTEN_BOOK))
		{
			// Save the type as book
			this.type = ItemType.WRITTEN_BOOK;

			// Define the book meta
			BookMeta bookMeta = (BookMeta) item.getItemMeta();

			// Save the book meta
			this.title = bookMeta.getTitle();
			this.author = bookMeta.getAuthor();
			this.pages = bookMeta.getPages();
		}
	}

	/**
	 * Returns the TrackedItemStack as an actual, usable ItemStack.
	 * 
	 * @return ItemStack
	 */
	public ItemStack toItemStack()
	{
		// Create the first instance of the item
		ItemStack item = new ItemStack(this.typeId, this.byteId);

		// Set main values
		item.setAmount(this.amount);
		item.setDurability(this.durability);

		// Define the item meta
		ItemMeta itemMeta = item.getItemMeta();

		// Set the meta
		itemMeta.setDisplayName(this.name);
		itemMeta.setLore(this.lore);

		// Save the meta
		item.setItemMeta(itemMeta);

		// Apply enchantments if they exist
		if(!enchantments.isEmpty())
		{
			for(Map.Entry<Integer, Integer> ench : this.enchantments.entrySet())
			{
				item.addUnsafeEnchantment(Enchantment.getById(ench.getKey()), ench.getValue());
			}
		}

		// If it's a book, apply the information
		if(type.equals(ItemType.WRITTEN_BOOK))
		{
			// Get the book meta
			BookMeta bookMeta = (BookMeta) item.getItemMeta();

			bookMeta.setTitle(this.title);
			bookMeta.setAuthor(this.author);
			bookMeta.setPages(this.pages);

			item.setItemMeta(bookMeta);
		}

		// Return that sucka
		return item;
	}

	/**
	 * The type enum.
	 */
	public enum ItemType
	{
		STANDARD, WRITTEN_BOOK;
	}
}
