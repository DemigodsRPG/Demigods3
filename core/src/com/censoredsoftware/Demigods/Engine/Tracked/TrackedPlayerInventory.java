package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.DemigodsData;

/**
 * Creates a saved version of a PlayerInventory.
 */
@Model
public class TrackedPlayerInventory
{
	@Id
	private Long id;
	@Reference
	private TrackedItemStack helmet;
	@Reference
	private TrackedItemStack chestplate;
	@Reference
	private TrackedItemStack leggings;
	@Reference
	private TrackedItemStack boots;
	@CollectionMap(key = Integer.class, value = TrackedItemStack.class)
	@Indexed
	private Map<Integer, TrackedItemStack> items;
	@Attribute
	private Integer size;

	void setSize(Integer size)
	{
		this.size = size;
	}

	void setHelmet(ItemStack helmet)
	{
		this.helmet = TrackedModelFactory.createTrackedItemStack(helmet);
	}

	void setChestplate(ItemStack chestplate)
	{
		this.chestplate = TrackedModelFactory.createTrackedItemStack(chestplate);
	}

	void setLeggings(ItemStack leggings)
	{
		this.leggings = TrackedModelFactory.createTrackedItemStack(leggings);
	}

	void setBoots(ItemStack boots)
	{
		this.boots = TrackedModelFactory.createTrackedItemStack(boots);
	}

	void setItems(Inventory inventory)
	{
		if(this.items == null) this.items = new HashMap<Integer, TrackedItemStack>();

		int slot = 1;
		for(ItemStack item : inventory.getContents())
		{
			if(item == null) continue;

			TrackedItemStack trackedItem = TrackedModelFactory.createTrackedItemStack(item);

			this.items.put(slot, trackedItem);

			Demigods.message.broadcast("ID: " + trackedItem.getId());

			slot++;
		}
	}

	public Long getId()
	{
		return this.id;
	}

	public static void save(TrackedPlayerInventory inventory)
	{
		DemigodsData.jOhm.save(inventory);
	}

	public static TrackedPlayerInventory load(long id) // TODO This belongs somewhere else.
	{
		return DemigodsData.jOhm.get(TrackedPlayerInventory.class, id);
	}

	public static Set<TrackedPlayerInventory> loadAll()
	{
		return DemigodsData.jOhm.getAll(TrackedPlayerInventory.class);
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
		inventory.setBoots(new ItemStack(Material.AIR));
		inventory.setChestplate(new ItemStack(Material.AIR));
		inventory.setLeggings(new ItemStack(Material.AIR));
		inventory.setBoots(new ItemStack(Material.AIR));

		// Set the armor contents
		if(this.helmet != null) inventory.setHelmet(this.helmet.toItemStack());
		if(this.chestplate != null) inventory.setChestplate(this.chestplate.toItemStack());
		if(this.leggings != null) inventory.setLeggings(this.leggings.toItemStack());
		if(this.boots != null) inventory.setBoots(this.boots.toItemStack());

		for(Map.Entry<Integer, TrackedItemStack> item : this.items.entrySet())
		{
			inventory.setItem(item.getKey(), item.getValue().toItemStack());
		}

		// We're done with this instance now, delete it
		DemigodsData.jOhm.delete(TrackedPlayerInventory.class, this.id);
	}
}
