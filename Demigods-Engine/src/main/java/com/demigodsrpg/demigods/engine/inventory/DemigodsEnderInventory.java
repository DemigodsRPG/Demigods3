package com.demigodsrpg.demigods.engine.inventory;

import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.item.DemigodsItemStack;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DemigodsEnderInventory extends DataAccess<UUID, DemigodsEnderInventory>
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

	public void generateId()
	{
		id = UUID.randomUUID();
	}

	public void setItems(org.bukkit.inventory.Inventory inventory)
	{
		if(this.items == null) this.items = new String[26];
		for(int i = 0; i < 26; i++)
		{
			if(inventory.getItem(i) == null) this.items[i] = DemigodsItemStack.create(new ItemStack(Material.AIR)).getId().toString();
			else this.items[i] = DemigodsItemStack.create(inventory.getItem(i)).getId().toString();
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
					ItemStack itemStack = DemigodsItemStack.get(UUID.fromString(this.items[i])).getBukkitItem();
					if(itemStack != null) inventory.setItem(i, DemigodsItemStack.get(UUID.fromString(this.items[i])).getBukkitItem());
				}
			}
		}

		// Delete
		remove();
	}

	private static final DataAccess<UUID, DemigodsEnderInventory> DATA_ACCESS = new DemigodsEnderInventory();

	public static DemigodsEnderInventory get(UUID id)
	{
		return DATA_ACCESS.getDirect(id);
	}

	public static DemigodsEnderInventory create(DemigodsCharacter character)
	{
		Inventory inventory = character.getBukkitOfflinePlayer().getPlayer().getEnderChest();
		DemigodsEnderInventory enderInventory = new DemigodsEnderInventory();
		enderInventory.generateId();
		enderInventory.setItems(inventory);
		enderInventory.save();
		return enderInventory;
	}

	public static DemigodsEnderInventory createEmpty()
	{
		DemigodsEnderInventory enderInventory = new DemigodsEnderInventory();
		enderInventory.generateId();
		enderInventory.save();
		return enderInventory;
	}
}
