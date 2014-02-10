package com.censoredsoftware.demigods.engine.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class DemigodsEnderInventory implements ConfigurationSerializable
{
	private UUID id;
	private String[] items;

	public DemigodsEnderInventory()
	{}

	public DemigodsEnderInventory(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		if(conf.getStringList("items") != null)
		{
			List<String> stringItems = conf.getStringList("items");
			items = new String[stringItems.size()];
			for(int i = 0; i < stringItems.size(); i++)
				items[i] = stringItems.get(i);
		}
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = Maps.newHashMap();
		if(items != null) map.put("items", Lists.newArrayList(items));
		return map;
	}

	protected abstract DemigodsItemStack create(ItemStack itemStack);

	protected abstract DemigodsItemStack load(UUID itemStack);

	protected abstract void delete();

	public void generateId()
	{
		id = UUID.randomUUID();
	}

	public void setItems(org.bukkit.inventory.Inventory inventory)
	{
		if(this.items == null) this.items = new String[26];
		for(int i = 0; i < 26; i++)
		{
			if(inventory.getItem(i) == null) this.items[i] = create(new ItemStack(Material.AIR)).getId().toString();
			else this.items[i] = create(inventory.getItem(i)).getId().toString();
		}
	}

	public UUID getId()
	{
		return this.id;
	}

	/**
	 * Applies this inventory to the given <code>player</code>.
	 * 
	 * @param player the player for whom apply the inventory.
	 */
	public void setToPlayer(Player player)
	{
		// Define the inventory
		Inventory inventory = player.getEnderChest();

		// Clear it all first
		inventory.clear();

		if(this.items != null)
		{
			// Set items
			for(int i = 0; i < 26; i++)
			{
				if(this.items[i] != null)
				{
					ItemStack itemStack = load(UUID.fromString(this.items[i])).toItemStack();
					if(itemStack != null) inventory.setItem(i, load(UUID.fromString(this.items[i])).toItemStack());
				}
			}
		}

		// Delete
		delete();
	}
}
