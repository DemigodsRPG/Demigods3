package com.legit2.Demigods;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DSouls
{
	// Define variables
	public static ArrayList<ItemStack> allSouls = new ArrayList<ItemStack>();
	static ItemStack mortalSoul;
	static ItemStack immortalSoul;
	
	private static void loadSouls()
	{
		// Define Mortal Soul
		mortalSoul = new ItemStack(Material.GOLD_NUGGET, 1);
		String mortalSoulName = "Mortal Soul";
		ArrayList<String> mortalSoulLore = new ArrayList<String>();
		mortalSoulLore.add("Brings you back to life.");
		mortalSoulLore.add("Regain half health!");
		ItemMeta mortalSoulMeta = mortalSoul.getItemMeta();
		mortalSoulMeta.setDisplayName(mortalSoulName);
		mortalSoulMeta.setLore(mortalSoulLore);
		mortalSoul.setItemMeta(mortalSoulMeta);
		allSouls.add(mortalSoul);
		
		// Define Immortal Soul
		immortalSoul = new ItemStack(Material.GLOWSTONE_DUST, 1);
		String immortalSoulName = "Immortal Soul";
		ArrayList<String> immortalSoulLore = new ArrayList<String>();
		immortalSoulLore.add("Brings you back to life.");
		immortalSoulLore.add("Regain full health!");
		ItemMeta immortalSoulMeta = immortalSoul.getItemMeta();
		immortalSoulMeta.setDisplayName(immortalSoulName);
		immortalSoulMeta.setLore(immortalSoulLore);
		immortalSoul.setItemMeta(immortalSoulMeta);
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
			case PLAYER: return immortalSoul; // Soul dropped by PLAYER
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
