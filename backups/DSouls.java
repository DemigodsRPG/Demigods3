package com.legit2.Demigods;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.legit2.Demigods.Utilities.DUtil;

public class DSouls
{
	// Define variables
	public static ArrayList<ItemStack> allSouls = new ArrayList<ItemStack>();
	static ItemStack mortalSoul;
	static ItemStack immortalSoul;
	static ItemStack dragonSoul;
	
	/*
	 *  loadSouls() : Loads all souls.
	 */
	private static void loadSouls()
	{
		// Define soul name
		String mortalSoulName = "Mortal Soul";
		mortalSoul = new ItemStack(Material.GHAST_TEAR, 1);
		// Define lore
		ArrayList<String> mortalSoulLore = new ArrayList<String>();
		mortalSoulLore.add("Brings you back to life.");
		mortalSoulLore.add("Regain half health!");
		// Add meta data
		ItemMeta mortalSoulMeta = mortalSoul.getItemMeta();
		mortalSoulMeta.setDisplayName(mortalSoulName);
		mortalSoulMeta.setLore(mortalSoulLore);
		mortalSoul.setItemMeta(mortalSoulMeta);
		// Add to soul array
		allSouls.add(mortalSoul);
		
		// Define soul name
		String immortalSoulName = "Immortal Soul";
		immortalSoul = new ItemStack(Material.GOLD_NUGGET, 1);
		// Define lore
		ArrayList<String> immortalSoulLore = new ArrayList<String>();
		immortalSoulLore.add("Brings you back to life.");
		immortalSoulLore.add("Regain full health!");
		// Add meta data
		ItemMeta immortalSoulMeta = immortalSoul.getItemMeta();
		immortalSoulMeta.setDisplayName(immortalSoulName);
		immortalSoulMeta.setLore(immortalSoulLore);
		immortalSoul.setItemMeta(immortalSoulMeta);
		// Add to soul array
		allSouls.add(immortalSoul);
	}
	
	/*
	 *  getSoulFromEntity : Returns soul for type of entity passed in.
	 */
	public static ItemStack getSoulFromEntity(Entity entity)
	{
		// Load all souls
		loadSouls();
		
		// Determine soul information based on entity type
		switch(entity.getType())
		{
			case VILLAGER: return mortalSoul; // Soul dropped by VILLAGER
			case PLAYER: // Soul dropped by PLAYER
				if(DUtil.isImmortal(((Player) entity).getName()))
				{
					return immortalSoul; 
				}
				else
				{
					return mortalSoul;
				}
				
			/*
			 *  TODO: Add soul drops for other entities based on a random chance of dropped.
			 *  ----  Possibly introduces ability to spawn rare super-souls for players to use.
			 */
				
			default: break;	// Break if default
		}
		return null;
	}
	
	/*
	 *  returnAllSouls() : Returns an array of all possible souls.
	 */
	public static ArrayList<ItemStack> returnAllSouls()
	{
		// Load all souls
		loadSouls();
		
		// Return data
		return allSouls;
	}
}
