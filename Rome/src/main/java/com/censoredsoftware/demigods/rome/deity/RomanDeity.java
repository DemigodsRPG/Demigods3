package com.censoredsoftware.demigods.rome.deity;

import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.deity.Alliance;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.rome.deity.god.Jupiter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;
import java.util.Set;

public enum RomanDeity implements Deity
{
	/**
	 * Gods
	 */
	// Jupiter
	JUPITER(Jupiter.name, Jupiter.permission, Jupiter.alliance, Jupiter.color, Jupiter.claimItems, Jupiter.forsakeItems, Jupiter.shortDescription, Jupiter.lore, Jupiter.flags, Jupiter.abilities, Jupiter.accuracy, Jupiter.favorRegen, Jupiter.maxFavor, Jupiter.maxHealth);

	private String name, permission;
	private Alliance alliance;
	private ChatColor color;
	private Map<Material, Integer> claimItems, forsakeItems;
	private String shortDescription;
	private List<String> lore;
	private Set<Flag> flags;
	private List<Ability> abilities;
	private int accuracy, favorRegen, maxFavor;
	private double maxHealth;

	private RomanDeity(String name, String permission, Alliance alliance, ChatColor color, Map<Material, Integer> claimItems, Map<Material, Integer> forsakeItems, String shortDescription, List<String> lore, Set<Flag> flags, List<Ability> abilities, int accuracy, int favorRegen, int maxFavor, double maxHealth)
	{
		this.name = name;
		this.permission = permission;
		this.alliance = alliance;
		this.color = color;
		this.claimItems = claimItems;
		this.forsakeItems = forsakeItems;
		this.shortDescription = shortDescription;
		this.lore = lore;
		this.flags = flags;
		this.abilities = abilities;
		this.accuracy = accuracy;
		this.favorRegen = favorRegen;
		this.maxFavor = maxFavor;
		this.maxHealth = maxHealth;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public String getName()
	{
		return name;
	}

	public Alliance getAlliance()
	{
		return alliance;
	}

	public String getPermission()
	{
		return permission;
	}

	public ChatColor getColor()
	{
		return color;
	}

	public Map<Material, Integer> getClaimItems()
	{
		return claimItems;
	}

	public Map<Material, Integer> getForsakeItems()
	{
		return forsakeItems;
	}

	public String getShortDescription()
	{
		return shortDescription;
	}

	public List<String> getLore()
	{
		return lore;
	}

	public Set<Flag> getFlags()
	{
		return flags;
	}

	public List<Ability> getAbilities()
	{
		return abilities;
	}

	public int getAccuracy()
	{
		return accuracy;
	}

	public int getFavorRegen()
	{
		return favorRegen;
	}

	public int getMaxFavor()
	{
		return maxFavor;
	}

	public double getMaxHealth()
	{
		return maxHealth;
	}
}
