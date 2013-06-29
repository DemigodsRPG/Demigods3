package com.censoredsoftware.Demigods.Engine.Object.PlayerCharacter;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsItemStack;

/**
 * Creates a saved version of a PlayerInventory.
 */
@Model
public class PlayerCharacterInventory
{
	@Id
	private Long id;
	@Attribute
	@Indexed
	private long owner;
	@Reference
	private DemigodsItemStack helmet;
	@Reference
	private DemigodsItemStack chestplate;
	@Reference
	private DemigodsItemStack leggings;
	@Reference
	private DemigodsItemStack boots;
	@Array(of = DemigodsItemStack.class, length = 36)
	private DemigodsItemStack[] items;

	void setOwner(Long id)
	{
		this.owner = id;
	}

	void setHelmet(ItemStack helmet)
	{
		this.helmet = DemigodsItemStack.create(helmet);
	}

	void setChestplate(ItemStack chestplate)
	{
		this.chestplate = DemigodsItemStack.create(chestplate);
	}

	void setLeggings(ItemStack leggings)
	{
		this.leggings = DemigodsItemStack.create(leggings);
	}

	void setBoots(ItemStack boots)
	{
		this.boots = DemigodsItemStack.create(boots);
	}

	void setItems(Inventory inventory)
	{
		if(this.items == null) this.items = new DemigodsItemStack[36];
		for(int i = 0; i < 35; i++)
		{
			if(inventory.getItem(i) == null)
			{
				this.items[i] = DemigodsItemStack.create(new ItemStack(Material.AIR));
			}
			else
			{
				this.items[i] = DemigodsItemStack.create(inventory.getItem(i));
			}
		}
	}

	public static PlayerCharacterInventory create(PlayerCharacter character)
	{
		PlayerInventory inventory = character.getOfflinePlayer().getPlayer().getInventory();
		PlayerCharacterInventory charInventory = new PlayerCharacterInventory();
		charInventory.setOwner(character.getId());
		if(inventory.getHelmet() != null) charInventory.setHelmet(inventory.getHelmet());
		if(inventory.getChestplate() != null) charInventory.setChestplate(inventory.getChestplate());
		if(inventory.getLeggings() != null) charInventory.setLeggings(inventory.getLeggings());
		if(inventory.getBoots() != null) charInventory.setBoots(inventory.getBoots());
		charInventory.setItems(inventory);
		PlayerCharacterInventory.save(charInventory);
		return charInventory;
	}

	public static PlayerCharacterInventory createEmpty()
	{
		PlayerCharacterInventory charInventory = new PlayerCharacterInventory();
		charInventory.setHelmet(new ItemStack(Material.AIR));
		charInventory.setChestplate(new ItemStack(Material.AIR));
		charInventory.setLeggings(new ItemStack(Material.AIR));
		charInventory.setBoots(new ItemStack(Material.AIR));
		PlayerCharacterInventory.save(charInventory);
		return charInventory;
	}

	public Long getId()
	{
		return this.id;
	}

	public PlayerCharacter getOwner()
	{
		return PlayerCharacter.load(this.owner);
	}

	public static void save(PlayerCharacterInventory inventory)
	{
		try
		{
			JOhm.save(inventory);
		}
		catch(Exception e)
		{
			Demigods.message.severe("Could not save inventory: " + inventory.getId());
		}
	}

	public static PlayerCharacterInventory load(long id)
	{
		return JOhm.get(PlayerCharacterInventory.class, id);
	}

	public static Set<PlayerCharacterInventory> loadAll()
	{
		return JOhm.getAll(PlayerCharacterInventory.class);
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

		if(this.items != null)
		{
			// Set items
			for(int i = 0; i < 35; i++)
			{
				if(this.items[i] != null) inventory.setItem(i, this.items[i].toItemStack());
			}
		}

		// Delete
		JOhm.delete(PlayerCharacterInventory.class, id);
	}
}
