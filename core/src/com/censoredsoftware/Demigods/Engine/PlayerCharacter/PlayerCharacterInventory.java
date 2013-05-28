package com.censoredsoftware.Demigods.Engine.PlayerCharacter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import redis.clients.johm.CollectionMap;
import redis.clients.johm.Id;
import redis.clients.johm.Model;
import redis.clients.johm.Reference;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedItemStack;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedModelFactory;

/**
 * Creates a saved version of a PlayerInventory.
 */
@Model
public class PlayerCharacterInventory
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
	private Map<Integer, TrackedItemStack> items;

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
			slot++;
			if(item == null) continue;
			TrackedItemStack trackedItem = TrackedModelFactory.createTrackedItemStack(item);
			this.items.put(slot, trackedItem);
		}
	}

	public Long getId()
	{
		return this.id;
	}

	public static void save(PlayerCharacterInventory inventory)
	{
		try
		{
			DemigodsData.jOhm.save(inventory);
			Demigods.message.broadcast("Saving inventory.");
		}
		catch(Exception e)
		{
			Demigods.message.broadcast("Could not save inventory.");
		}
	}

	public static PlayerCharacterInventory load(long id)
	{
		return DemigodsData.jOhm.get(PlayerCharacterInventory.class, id);
	}

	public static Set<PlayerCharacterInventory> loadAll()
	{
		return DemigodsData.jOhm.getAll(PlayerCharacterInventory.class);
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
		if(this.helmet != null) inventory.setHelmet(this.helmet.toItemStack());
		if(this.chestplate != null) inventory.setChestplate(this.chestplate.toItemStack());
		if(this.leggings != null) inventory.setLeggings(this.leggings.toItemStack());
		if(this.boots != null) inventory.setBoots(this.boots.toItemStack());

		for(Map.Entry<Integer, TrackedItemStack> item : this.items.entrySet())
		{
			inventory.setItem(item.getKey(), item.getValue().toItemStack());
		}

		Demigods.message.broadcast("Setting inventory to player.");

		// Delete
		DemigodsData.jOhm.delete(PlayerCharacterInventory.class, id);
	}
}
