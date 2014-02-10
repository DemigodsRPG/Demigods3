package com.censoredsoftware.demigods.engine.inventory;

import com.censoredsoftware.demigods.engine.data.DataAccess;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class DemigodsItemStack extends DataAccess<UUID, DemigodsItemStack>
{
	private UUID id;
	private ItemStack item;

	public DemigodsItemStack()
	{}

	public DemigodsItemStack(UUID id, ConfigurationSection conf)
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

	public static DemigodsItemStack create(ItemStack item)
	{
		DemigodsItemStack trackedItem = new DemigodsItemStack();
		trackedItem.generateId();
		trackedItem.setItem(item);
		trackedItem.save();
		return trackedItem;
	}
}
