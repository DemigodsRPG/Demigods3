package com.censoredsoftware.demigods.engine.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class DemigodsPlayerInventory implements ConfigurationSerializable
{
	private UUID id;
	private UUID helmet;
	private UUID chestplate;
	private UUID leggings;
	private UUID boots;
	private String[] items;

	public DemigodsPlayerInventory()
	{}

	public DemigodsPlayerInventory(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		if(conf.getString("helmet") != null) helmet = UUID.fromString(conf.getString("helmet"));
		if(conf.getString("chestplate") != null) chestplate = UUID.fromString(conf.getString("chestplate"));
		if(conf.getString("leggings") != null) leggings = UUID.fromString(conf.getString("leggings"));
		if(conf.getString("boots") != null) boots = UUID.fromString(conf.getString("boots"));
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
		if(helmet != null) map.put("helmet", helmet.toString());
		if(chestplate != null) map.put("chestplate", chestplate.toString());
		if(leggings != null) map.put("leggings", leggings.toString());
		if(boots != null) map.put("boots", boots.toString());
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

	public void setHelmet(ItemStack helmet)
	{
		this.helmet = create(helmet).getId();
	}

	public void setChestplate(ItemStack chestplate)
	{
		this.chestplate = create(chestplate).getId();
	}

	public void setLeggings(ItemStack leggings)
	{
		this.leggings = create(leggings).getId();
	}

	public void setBoots(ItemStack boots)
	{
		this.boots = create(boots).getId();
	}

	public void setItems(org.bukkit.inventory.Inventory inventory)
	{
		if(this.items == null) this.items = new String[36];
		for(int i = 0; i < 35; i++)
		{
			if(inventory.getItem(i) == null) this.items[i] = create(new ItemStack(Material.AIR)).getId().toString();
			else this.items[i] = create(inventory.getItem(i)).getId().toString();
		}
	}

	public UUID getId()
	{
		return this.id;
	}

	public ItemStack getHelmet()
	{
		if(this.helmet == null) return null;
		DemigodsItemStack item = load(this.helmet);
		if(item != null) return item.toItemStack();
		return null;
	}

	public ItemStack getChestplate()
	{
		if(this.chestplate == null) return null;
		DemigodsItemStack item = load(this.chestplate);
		if(item != null) return item.toItemStack();
		return null;
	}

	public ItemStack getLeggings()
	{
		if(this.leggings == null) return null;
		DemigodsItemStack item = load(this.leggings);
		if(item != null) return item.toItemStack();
		return null;
	}

	public ItemStack getBoots()
	{
		if(this.boots == null) return null;
		DemigodsItemStack item = load(this.boots);
		if(item != null) return item.toItemStack();
		return null;
	}

	/**
	 * Applies this inventory to the given <code>player</code>.
	 * 
	 * @param player the player for whom apply the inventory.
	 */
	public void setToPlayer(Player player)
	{
		// Define the inventory
		PlayerInventory inventory = player.getInventory();

		// Clear it all first
		inventory.clear();
		inventory.setHelmet(new ItemStack(Material.AIR));
		inventory.setChestplate(new ItemStack(Material.AIR));
		inventory.setLeggings(new ItemStack(Material.AIR));
		inventory.setBoots(new ItemStack(Material.AIR));

		// Set the armor contents
		if(getHelmet() != null) inventory.setHelmet(getHelmet());
		if(getChestplate() != null) inventory.setChestplate(getChestplate());
		if(getLeggings() != null) inventory.setLeggings(getLeggings());
		if(getBoots() != null) inventory.setBoots(getBoots());

		if(this.items != null)
		{
			// Set items
			for(int i = 0; i < 35; i++)
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
