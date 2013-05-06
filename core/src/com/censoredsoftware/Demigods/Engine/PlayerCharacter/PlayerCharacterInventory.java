package com.censoredsoftware.Demigods.Engine.PlayerCharacter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Id;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import redis.clients.johm.Model;

import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedItemStack;

// TODO Figure out how this file is going to save.

@Model
public class PlayerCharacterInventory
{
	@Id
	private long id;
	private int size;
	private TrackedItemStack helmet = new TrackedItemStack(new ItemStack(Material.AIR), null);
	private TrackedItemStack chestPlate = new TrackedItemStack(new ItemStack(Material.AIR), null);
	private TrackedItemStack leggings = new TrackedItemStack(new ItemStack(Material.AIR), null);
	private TrackedItemStack boots = new TrackedItemStack(new ItemStack(Material.AIR), null);
	private HashMap<Integer, TrackedItemStack> items;

	public PlayerCharacterInventory(Inventory inventory)
	{
		items = new HashMap<Integer, TrackedItemStack>();

		if(inventory != null)
		{
			this.size = inventory.getSize();

			if(getOwner().isOnline())
			{
				Player player = getOwner().getPlayer();
				if(player.getInventory().getHelmet() != null) this.helmet = new TrackedItemStack(player.getInventory().getHelmet().clone(), null);
				if(player.getInventory().getChestplate() != null) this.chestPlate = new TrackedItemStack(player.getInventory().getChestplate().clone(), null);
				if(player.getInventory().getLeggings() != null) this.leggings = new TrackedItemStack(player.getInventory().getLeggings().clone(), null);
				if(player.getInventory().getBoots() != null) this.boots = new TrackedItemStack(player.getInventory().getBoots().clone(), null);
			}

			for(int i = 0; i < this.size; i++)
			{
				ItemStack item = inventory.getItem(i);
				if(item != null)
				{
					items.saveData(i, new TrackedItemStack(item, null));
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
				ItemStack item = ((TrackedItemStack) items.getDataObject(slot)).toItemStack();
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
		return CharacterAPI.getChar(charID).getOwner();
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
				ItemStack item = ((TrackedItemStack) items.getDataObject(slot)).toItemStack();
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
	public TrackedItemStack getHelmet()
	{
		if(this.helmet != null) return this.helmet;
		else return null;
	}

	/*
	 * getChestPlate() : Returns the chestPlate.
	 */
	public TrackedItemStack getChestPlate()
	{
		if(this.chestPlate != null) return this.chestPlate;
		else return null;
	}

	/*
	 * getLeggings() : Returns the leggings.
	 */
	public TrackedItemStack getLeggings()
	{
		if(this.leggings != null) return this.leggings;
		else return null;
	}

	/*
	 * getBoots() : Returns the boots.
	 */
	public TrackedItemStack getBoots()
	{
		if(this.boots != null) return this.boots;
		else return null;
	}

	public boolean containsKey(String key)
	{
		return false; // TODO
	}

	public Object getData(String key)
	{
		return null; // TODO
	}

	public void saveData(String key, Object data)
	{
		// TODO
	}

	public void removeData(String key)
	{
		// TODO
	}

	public int getID()
	{
		return charID;
	}

	public Map getMap() // TODO Make this into an actual DataStubModule instead of it holding a generic data module.
	{
		return items.getMap();
	}

	public void setMap(Map map) // TODO Make this into an actual DataStubModule instead of it holding a generic data module.
	{
		items.setMap(map);
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
