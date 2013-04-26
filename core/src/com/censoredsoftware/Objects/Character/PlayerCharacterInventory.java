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

import com.censoredsoftware.Modules.DataPersistence.IntegerDataModule;
import com.censoredsoftware.Objects.Special.SpecialItemStack;

public class PlayerCharacterInventory
{
	private String owner;
	private int size;
	private SpecialItemStack helmet = new SpecialItemStack(new ItemStack(Material.AIR), null);
	private SpecialItemStack chestPlate = new SpecialItemStack(new ItemStack(Material.AIR), null);
	private SpecialItemStack leggings = new SpecialItemStack(new ItemStack(Material.AIR), null);
	private SpecialItemStack boots = new SpecialItemStack(new ItemStack(Material.AIR), null);
	private IntegerDataModule items;

	public PlayerCharacterInventory(String owner, int size, SpecialItemStack helmet, SpecialItemStack chestPlate, SpecialItemStack leggings, SpecialItemStack boots, IntegerDataModule items)
	{
		this.owner = owner;
		this.size = size;
		this.helmet = helmet;
		this.chestPlate = chestPlate;
		this.leggings = leggings;
		this.boots = boots;
		this.items = items;
	}

	public PlayerCharacterInventory(Inventory inventory)
	{
		items = new IntegerDataModule();

		if(inventory != null)
		{
			this.owner = ((OfflinePlayer) inventory.getHolder()).getName();
			this.size = inventory.getSize();

			if(getOwner().isOnline())
			{
				Player player = getOwner().getPlayer();
				if(player.getInventory().getHelmet() != null) this.helmet = new SpecialItemStack(player.getInventory().getHelmet().clone(), null);
				if(player.getInventory().getChestplate() != null) this.chestPlate = new SpecialItemStack(player.getInventory().getChestplate().clone(), null);
				if(player.getInventory().getLeggings() != null) this.leggings = new SpecialItemStack(player.getInventory().getLeggings().clone(), null);
				if(player.getInventory().getBoots() != null) this.boots = new SpecialItemStack(player.getInventory().getBoots().clone(), null);
			}

			for(int i = 0; i < this.size; i++)
			{
				ItemStack item = inventory.getItem(i);
				if(item != null)
				{
					items.saveData(i, new SpecialItemStack(item, null));
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

		if(this.getHelmet() != null) player.getInventory().setHelmet(this.getHelmet().toItemStack());
		if(this.getChestPlate() != null) player.getInventory().setChestplate(this.getChestPlate().toItemStack());
		if(this.getLeggings() != null) player.getInventory().setLeggings(this.getLeggings().toItemStack());
		if(this.getBoots() != null) player.getInventory().setBoots(this.getBoots().toItemStack());

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
	public SpecialItemStack getHelmet()
	{
		if(this.helmet != null) return this.helmet;
		else return null;
	}

	/*
	 * getChestPlate() : Returns the chestPlate.
	 */
	public SpecialItemStack getChestPlate()
	{
		if(this.chestPlate != null) return this.chestPlate;
		else return null;
	}

	/*
	 * getLeggings() : Returns the leggings.
	 */
	public SpecialItemStack getLeggings()
	{
		if(this.leggings != null) return this.leggings;
		else return null;
	}

	/*
	 * getBoots() : Returns the boots.
	 */
	public SpecialItemStack getBoots()
	{
		if(this.boots != null) return this.boots;
		else return null;
	}

	public IntegerDataModule grabItems()
	{
		return this.items;
	}
}
