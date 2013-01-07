package com.legit2.Demigods;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DSouls
{
	/*
	 *  getSoul : Returns soul for type of entity passed in.
	 */
	@SuppressWarnings("null")
	public static ItemStack getSoul(Entity entity)
	{
		// Define Mortal Soul
		String soulName = null;
		ItemStack soulItem = null;
		ArrayList<String> soulLore = new ArrayList<String>();
		ItemMeta soulMeta = soulItem.getItemMeta();

		// Determine soul information based on entity type
		switch(entity.getType())
		{
			// Soul dropped by VILLAGER
			case VILLAGER: 
				soulName = "Mortal Soul";
				soulItem = new ItemStack(Material.GOLD_NUGGET, 1);
				soulLore.add("Brings you back to life.");
				soulLore.add("Regain half health!");
				break;
				
			// Soul dropped by PLAYER
			case PLAYER: 
				soulName = "Immortal Soul";
				soulItem = new ItemStack(Material.GLOWSTONE_DUST, 1);
				soulLore.add("Brings you back to life.");
				soulLore.add("Regain full health!");
				break;
				
			// Break if default
			default: break;
		}

		// Set the information for the soul
		soulMeta.setDisplayName(soulName);
		soulMeta.setLore(soulLore);
		soulItem.setItemMeta(soulMeta);

		return soulItem;
	}
}
