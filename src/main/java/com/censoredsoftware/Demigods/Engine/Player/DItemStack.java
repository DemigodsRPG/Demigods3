package com.censoredsoftware.Demigods.Engine.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import redis.clients.johm.*;

@Model
public class DItemStack
{
	@Id
	private Long id;
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
	private int type;

	void setType(ItemType type)
	{
		this.type = type.getId();
	}

	void setTypeId(int typeId)
	{
		this.typeId = typeId;
	}

	void setByteId(byte byteId)
	{
		this.byteId = byteId;
	}

	void setAmount(int amount)
	{
		this.amount = amount;
	}

	void setDurability(short durability)
	{
		this.durability = durability;
	}

	void setName(String name)
	{
		this.name = name;
	}

	void setLore(List<String> lore)
	{
		this.lore = lore;
	}

	void setEnchantments(ItemStack item)
	{
		// If it has enchantments then save them
		if(item.hasItemMeta() && item.getItemMeta().hasEnchants())
		{
			// Create the new HashMap
			enchantments = new HashMap<Integer, Integer>();

			for(Map.Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet())
			{
				enchantments.put(ench.getKey().getId(), ench.getValue());
			}
		}
	}

	void setBookMeta(ItemStack item)
	{
		// If it's a written book then save the book-specific information
		if(item.getType().equals(Material.WRITTEN_BOOK))
		{
			// Save the type as book
			this.type = ItemType.WRITTEN_BOOK.getId();

			// Define the book meta
			BookMeta bookMeta = (BookMeta) item.getItemMeta();

			// Save the book meta
			this.title = bookMeta.getTitle();
			this.author = bookMeta.getAuthor();
			this.pages = bookMeta.getPages();
		}
	}

	public Long getId()
	{
		return this.id;
	}

	/**
	 * Returns the DItemStack as an actual, usable ItemStack.
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
		if(this.name != null) itemMeta.setDisplayName(this.name);
		if(this.lore != null && !this.lore.isEmpty()) itemMeta.setLore(this.lore);

		// Save the meta
		item.setItemMeta(itemMeta);

		// Apply enchantments if they exist
		if(enchantments != null && !enchantments.isEmpty())
		{
			for(Map.Entry<Integer, Integer> ench : this.enchantments.entrySet())
			{
				item.addUnsafeEnchantment(Enchantment.getById(ench.getKey()), ench.getValue());
			}
		}

		// If it's a book, apply the information
		if(type == ItemType.WRITTEN_BOOK.getId())
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

	public static class Util
	{
		public static DItemStack create(ItemStack item)
		{
			DItemStack trackedItem = new DItemStack();
			trackedItem.setTypeId(item.getTypeId());
			trackedItem.setByteId(item.getData().getData());
			trackedItem.setAmount(item.getAmount());
			trackedItem.setDurability(item.getDurability());
			if(item.hasItemMeta())
			{
				if(item.getItemMeta().hasDisplayName()) trackedItem.setName(item.getItemMeta().getDisplayName());
				if(item.getItemMeta().hasLore()) trackedItem.setLore(item.getItemMeta().getLore());
			}
			trackedItem.setEnchantments(item);
			trackedItem.setBookMeta(item);

			save(trackedItem);
			return trackedItem;
		}

		public static void save(DItemStack item)
		{
			JOhm.save(item);
		}

		public static DItemStack load(long id)
		{
			return JOhm.get(DItemStack.class, id);
		}

		public static Set<DItemStack> loadAll()
		{
			return JOhm.getAll(DItemStack.class);
		}
	}

	/**
	 * The type enum.
	 */
	public static enum ItemType
	{
		STANDARD(0), WRITTEN_BOOK(1);

		private int id;

		private ItemType(int id)
		{
			this.id = id;
		}

		public int getId()
		{
			return this.id;
		}
	}
}
