package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.DemigodsData;

/**
 * Stores a saveable version of a PlayerInventory.
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

	void setHelmet(TrackedItemStack helmet)
	{
		this.helmet = helmet;
	}

	void setChestplate(TrackedItemStack chestplate)
	{
		this.chestplate = chestplate;
	}

	void setLeggings(TrackedItemStack leggings)
	{
		this.leggings = leggings;
	}

	void setBoots(TrackedItemStack boots)
	{
		this.boots = boots;
	}

	void setItems(Map<Integer, TrackedItemStack> items)
	{
		this.items = items;
	}

	TrackedPlayerInventory processInventory(Inventory inventory)
	{
		int slot = 1;
		for(ItemStack item : inventory.getContents())
		{
			if(item != null)
			{
				this.items.put(slot, TrackedModelFactory.createTrackedItemStack(item));
			}

			slot++;
		}
		return this;
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

	public Long getId()
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
		PlayerInventory inventory = player.getInventory();

		// Clear their current inventory
		player.getInventory().clear();

		// Set the armor contents
		inventory.setHelmet(this.helmet.toItemStack());
		inventory.setChestplate(this.chestplate.toItemStack());
		inventory.setLeggings(this.leggings.toItemStack());
		inventory.setBoots(this.boots.toItemStack());

		// Set the main contents
		for(Map.Entry<Integer, TrackedItemStack> item : this.items.entrySet())
		{
			inventory.setItem(item.getKey(), item.getValue().toItemStack());
		}
	}
}
