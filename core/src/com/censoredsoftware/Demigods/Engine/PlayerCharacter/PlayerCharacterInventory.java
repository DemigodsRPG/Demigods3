package com.censoredsoftware.Demigods.Engine.PlayerCharacter;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Tracked.TrackedItemStack;

/**
 * Stores a saveable inventory for a specific character.
 */
@Model
public class PlayerCharacterInventory
{
	@Id
	private long id;
	@Attribute
	private long charId;
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

	/**
	 * Creates a new PlayerCharacterInventory.
	 * 
	 * @param character the character for whom to create the inventory.
	 */
	public PlayerCharacterInventory(PlayerCharacter character)
	{
		// Ensure that the player is online before continuing
		if(!character.getOwner().isOnline()) return;

		// Save variables and define the inventory
		this.charId = character.getId();
		PlayerInventory inventory = character.getOwner().getPlayer().getInventory();

		// Save armor
		this.helmet = new TrackedItemStack(inventory.getHelmet());
		this.chestplate = new TrackedItemStack(inventory.getChestplate());
		this.leggings = new TrackedItemStack(inventory.getLeggings());
		this.boots = new TrackedItemStack(inventory.getBoots());

		// Define the new items map and the slot counter and save the contents
		this.items = new HashMap<Integer, TrackedItemStack>();
		int slot = 1;

		for(ItemStack item : inventory.getContents())
		{
			this.items.put(slot, new TrackedItemStack(item));
			slot++;
		}
	}

	/**
	 * Applies this inventory to the given <code>player</code>.
	 * 
	 * @param player the player for whom apply the inventory.
	 */
	public void applyTo(Player player)
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
