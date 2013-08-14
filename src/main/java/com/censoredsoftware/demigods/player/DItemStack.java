package com.censoredsoftware.demigods.player;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.helper.ConfigFile;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DItemStack implements ConfigurationSerializable
{
	private UUID id;
	private int typeId;
	private byte byteId;
	private int amount;
	private short durability;
	private Map<String, Object> enchantments; // Format: Map<ENCHANTMENT_ID, LEVEL>
	private String name;
	private List<String> lore;
	private String author;
	private String title;
	private List<String> pages;
	private int type;

	public DItemStack()
	{}

	public DItemStack(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		typeId = conf.getInt("typeId");
		byteId = (byte) conf.getInt("byteId");
		amount = conf.getInt("amount");
		durability = (short) conf.getInt("durability");
		if(conf.getConfigurationSection("enchantments") != null) enchantments = conf.getConfigurationSection("enchantments").getValues(false);
		if(conf.getString("name") != null) name = conf.getString("name");
		if(conf.getString("lore") != null) lore = conf.getStringList("lore");
		if(conf.getString("author") != null) author = conf.getString("author");
		if(conf.getString("title") != null) title = conf.getString("title");
		if(conf.getStringList("pages") != null) pages = conf.getStringList("pages");
		type = conf.getInt("type");
	}

	@Override
	public Map<String, Object> serialize()
	{
		return new HashMap<String, Object>()
		{
			{
				put("typeId", typeId);
				put("byteId", (int) byteId);
				put("amount", amount);
				put("durability", (int) durability);
				if(enchantments != null) put("enchantmetns", enchantments);
				if(lore != null) put("lore", lore);
				if(author != null) put("author", author);
				if(title != null) put("title", title);
				if(pages != null) put("pages", pages);
				put("type", type);
			}
		};
	}

	public void generateId()
	{
		id = UUID.randomUUID();
	}

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
			enchantments = Maps.newHashMap();

			for(Map.Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet())
			{
				enchantments.put(String.valueOf(ench.getKey().getId()), ench.getValue());
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

	public UUID getId()
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
			for(Map.Entry<String, Object> ench : this.enchantments.entrySet())
			{
				item.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(ench.getKey())), Integer.parseInt(ench.getValue().toString()));
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

	public static class File extends ConfigFile
	{
		private static String SAVE_PATH;
		private static final String SAVE_FILE = "itemStacks.yml";

		public File()
		{
			super(Demigods.plugin);
			SAVE_PATH = Demigods.plugin.getDataFolder() + "/data/";
		}

		@Override
		public ConcurrentHashMap<UUID, DItemStack> loadFromFile()
		{
			final FileConfiguration data = getData(SAVE_PATH, SAVE_FILE);
			return new ConcurrentHashMap<UUID, DItemStack>()
			{
				{
					for(String stringId : data.getKeys(false))
						put(UUID.fromString(stringId), new DItemStack(UUID.fromString(stringId), data.getConfigurationSection(stringId)));
				}
			};
		}

		@Override
		public boolean saveToFile()
		{
			FileConfiguration saveFile = getData(SAVE_PATH, SAVE_FILE);
			Map<UUID, DItemStack> currentFile = loadFromFile();

			for(UUID id : DataManager.itemStacks.keySet())
				if(!currentFile.keySet().contains(id) || !currentFile.get(id).equals(DataManager.itemStacks.get(id))) saveFile.createSection(id.toString(), Util.load(id).serialize());

			for(UUID id : currentFile.keySet())
				if(!DataManager.itemStacks.keySet().contains(id)) saveFile.set(id.toString(), null);

			return saveFile(SAVE_PATH, SAVE_FILE, saveFile);
		}
	}

	public static class Util
	{
		public static void save(DItemStack itemStack)
		{
			DataManager.itemStacks.put(itemStack.getId(), itemStack);
		}

		public static void delete(UUID id)
		{
			DataManager.itemStacks.remove(id);
		}

		public static DItemStack load(UUID id)
		{
			return DataManager.itemStacks.get(id);
		}

		public static DItemStack create(ItemStack item)
		{
			DItemStack trackedItem = new DItemStack();
			trackedItem.generateId();
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
	}

	/**
	 * The type enum.
	 */
	public static enum ItemType
	{
		STANDARD(0), WRITTEN_BOOK(1);

		private final int id;

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
