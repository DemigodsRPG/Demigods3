package com.censoredsoftware.demigods.engine.item;

import com.censoredsoftware.demigods.engine.data.DataManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class DItemStack implements ConfigurationSerializable
{
	private UUID id;
	private ItemStack item;

	public DItemStack()
	{}

	public DItemStack(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		if(conf.getValues(true) != null) item = ItemStack.deserialize(conf.getValues(true));
	}

	@Override
	public Map<String, Object> serialize()
	{
		return item.serialize();
	}

	public void generateId()
	{
		id = UUID.randomUUID();
	}

	public UUID getId()
	{
		return id;
	}

	public void setItem(ItemStack item)
	{
		this.item = item;
	}

	/**
	 * Returns the DItemStack as an actual, usable ItemStack.
	 * 
	 * @return ItemStack
	 */
	public ItemStack toItemStack()
	{
		return item;
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
			trackedItem.setItem(item);
			save(trackedItem);
			return trackedItem;
		}
	}
}
