package com.censoredsoftware.Demigods.Demo.Data.Item;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.Demigods.API.ItemAPI;

public enum Baetylus
{
	/**
	 * A large shard of the Baetylus.
	 */
	LARGE_SHARD(new Shard(20, Sound.ANVIL_BREAK, ItemAPI.create(Material.GHAST_TEAR, "Large Baetylus Shard", new ArrayList<String>()
	{
		{
			add("Regain full health instead of dying.");
		}
	}, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.THORNS, 3);
		}
	}))),

	/**
	 * A shard of the Baetylus.
	 */
	NORMAL_SHARD(new Shard(10, Sound.ANVIL_BREAK, ItemAPI.create(Material.GHAST_TEAR, "Baetylus Shard", new ArrayList<String>()
	{
		{
			add("Regain half health instead of dying.");
		}
	}, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.THORNS, 2);
		}
	}))),

	/**
	 * A small shard of the Baetylus.
	 */
	SMALL_SHARD(new Shard(5, Sound.ANVIL_BREAK, ItemAPI.create(Material.GHAST_TEAR, "Small Baetylus Shard", new ArrayList<String>()
	{
		{
			add("Regain quarter health instead of dying.");
		}
	}, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.THORNS, 1);
		}
	})));

	private Shard value;

	private Baetylus(Shard value)
	{
		this.value = value;
	}

	public Shard getShard()
	{
		return this.value;
	}

	public static class Shard
	{
		private int health;
		private Sound sound;
		private ItemStack item;

		public Shard(int health, Sound sound, ItemStack item)
		{
			this.health = health;
			this.sound = sound;
			this.item = item;
		}

		public int getHealth()
		{
			return health;
		}

		public Sound getSound()
		{
			return sound;
		}

		public ItemStack getItem()
		{
			return item;
		}
	}
}
