package com.censoredsoftware.Objects.Character;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Demigods.DemigodsFactory;
import com.censoredsoftware.Modules.DataPersistence.IntegerDataModule;
import com.censoredsoftware.Objects.Special.SpecialItemStack;

public class PlayerCharacterInventory
{
	private String owner, name;
	private SpecialItemStack helmet = DemigodsFactory.specialItemStackFactory.create(new ItemStack(Material.AIR));
	private SpecialItemStack chestplate = DemigodsFactory.specialItemStackFactory.create(new ItemStack(Material.AIR));
	private SpecialItemStack leggings = DemigodsFactory.specialItemStackFactory.create(new ItemStack(Material.AIR));
	private SpecialItemStack boots = DemigodsFactory.specialItemStackFactory.create(new ItemStack(Material.AIR));
	private IntegerDataModule items;

	int size;

	public PlayerCharacterInventory(Plugin instance, Inventory inventory, String name)
	{
		items = new IntegerDataModule(instance);
		if(name != null) this.name = name;

		if(inventory != null)
		{
			this.owner = ((OfflinePlayer) inventory.getHolder()).getName();
			this.size = inventory.getSize();

			if(getOwner().isOnline())
			{
				Player player = getOwner().getPlayer();
				if(player.getInventory().getHelmet() != null) this.helmet = DemigodsFactory.specialItemStackFactory.create(player.getInventory().getHelmet().clone());
				if(player.getInventory().getChestplate() != null) this.chestplate = DemigodsFactory.specialItemStackFactory.create(player.getInventory().getChestplate().clone());
				if(player.getInventory().getLeggings() != null) this.leggings = DemigodsFactory.specialItemStackFactory.create(player.getInventory().getLeggings().clone());
				if(player.getInventory().getBoots() != null) this.boots = DemigodsFactory.specialItemStackFactory.create(player.getInventory().getBoots().clone());
			}

			for(int i = 0; i < this.size; i++)
			{
				ItemStack item = inventory.getItem(i);
				if(item != null)
				{
					items.saveData(i, DemigodsFactory.specialItemStackFactory.create(item));
				}
			}
		}
	}

	/*
	 * toInventory() : Converts back into a standard inventory.
	 */
	public Inventory toInventory(Plugin instance)
	{
		Inventory inv = instance.getServer().createInventory((InventoryHolder) getOwner(), this.size);

		for(int slot : items.listKeys())
		{
			try
			{
				ItemStack item = ((SpecialItemStack) items.getDataObject(slot)).toItemStack();
				inv.setItem(slot, item);
			}
			catch(Exception ignored)
			{}
		}

		return inv;
	}

	/*
	 * setToPlayer() : Sets the inventory to a player.
	 */
	public synchronized void setToPlayer(OfflinePlayer entity)
	{
		Player player;

		if(!entity.isOnline()) return;
		else
		{
			player = entity.getPlayer();
		}

		if(this.getHelmet() != null) player.getInventory().setHelmet(this.getHelmet());
		if(this.getChestplate() != null) player.getInventory().setChestplate(this.getChestplate());
		if(this.getLeggings() != null) player.getInventory().setLeggings(this.getLeggings());
		if(this.getBoots() != null) player.getInventory().setBoots(this.getBoots());

		for(Entry<Integer, ItemStack> slot : this.getItems().entrySet())
		{
			player.getInventory().setItem(slot.getKey(), slot.getValue());
		}
	}

	/*
	 * getOwner() : Returns the Player who owns this inventory.
	 */
	public OfflinePlayer getOwner()
	{
		return Bukkit.getOfflinePlayer(this.owner);
	}

	/*
	 * getItems() : Returns the items.
	 */
	public HashMap<Integer, ItemStack> getItems()
	{
		HashMap<Integer, ItemStack> temp = new HashMap<Integer, ItemStack>();

		for(int slot : items.listKeys())
		{
			try
			{
				ItemStack item = ((SpecialItemStack) items.getDataObject(slot)).toItemStack();
				temp.put(slot, item);
			}
			catch(Exception ignored)
			{}
		}

		return temp;
	}

	/*
	 * getHelmet() : Returns the helmet.
	 */
	public ItemStack getHelmet()
	{
		if(this.helmet != null) return this.helmet.toItemStack();
		else return null;
	}

	/*
	 * getChestplate() : Returns the chestplate.
	 */
	public ItemStack getChestplate()
	{
		if(this.chestplate != null) return this.chestplate.toItemStack();
		else return null;
	}

	/*
	 * getLeggings() : Returns the leggings.
	 */
	public ItemStack getLeggings()
	{
		if(this.leggings != null) return this.leggings.toItemStack();
		else return null;
	}

	/*
	 * getBoots() : Returns the boots.
	 */
	public ItemStack getBoots()
	{
		if(this.boots != null) return this.boots.toItemStack();
		else return null;
	}

	public boolean hasName()
	{
		return this.name != null;
	}

	public String getName()
	{
		return this.name;
	}

	public synchronized void setName(String name)
	{
		this.name = name;
	}
}
