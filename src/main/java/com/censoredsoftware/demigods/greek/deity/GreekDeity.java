package com.censoredsoftware.demigods.greek.deity;

import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.deity.Alliance;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.greek.deity.donor.DrD1sco;
import com.censoredsoftware.demigods.greek.deity.donor.OmegaX17;
import com.censoredsoftware.demigods.greek.deity.fate.Atropos;
import com.censoredsoftware.demigods.greek.deity.fate.Clotho;
import com.censoredsoftware.demigods.greek.deity.fate.Lachesis;
import com.censoredsoftware.demigods.greek.deity.god.Hades;
import com.censoredsoftware.demigods.greek.deity.god.Poseidon;
import com.censoredsoftware.demigods.greek.deity.god.Zeus;
import com.censoredsoftware.demigods.greek.deity.titan.Iapetus;
import com.censoredsoftware.demigods.greek.deity.titan.Oceanus;
import com.censoredsoftware.demigods.greek.deity.titan.Perses;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;
import java.util.Set;

public enum GreekDeity implements Deity
{
	/**
	 * Gods
	 */
	// Zeus
	ZEUS(Zeus.name, Zeus.permission, Zeus.alliance, Zeus.color, Zeus.claimItems, Zeus.forsakeItems, Zeus.shortDescription, Zeus.lore, Zeus.flags, Zeus.abilities, Zeus.accuracy, Zeus.favorRegen, Zeus.maxFavor, Zeus.maxHealth),

	// Poseidon
	POSEIDON(Poseidon.name, Poseidon.permission, Poseidon.alliance, Poseidon.color, Poseidon.claimItems, Poseidon.forsakeItems, Poseidon.shortDescription, Poseidon.lore, Poseidon.flags, Poseidon.abilities, Poseidon.accuracy, Poseidon.favorRegen, Poseidon.maxFavor, Poseidon.maxHealth),

	// Hades
	HADES(Hades.name, Hades.permission, Hades.alliance, Hades.color, Hades.claimItems, Hades.forsakeItems, Hades.shortDescription, Hades.lore, Hades.flags, Hades.abilities, Hades.accuracy, Hades.favorRegen, Hades.maxFavor, Hades.maxHealth),

	/**
	 * Titans
	 */
	// Perses
	PERSES(Perses.name, Perses.permission, Perses.alliance, Perses.color, Perses.claimItems, Perses.forsakeItems, Perses.shortDescription, Perses.lore, Perses.flags, Perses.abilities, Perses.accuracy, Perses.favorRegen, Perses.maxFavor, Perses.maxHealth),

	// Oceanus
	OCEANUS(Oceanus.name, Oceanus.permission, Oceanus.alliance, Oceanus.color, Oceanus.claimItems, Oceanus.forsakeItems, Oceanus.shortDescription, Oceanus.lore, Oceanus.flags, Oceanus.abilities, Oceanus.accuracy, Oceanus.favorRegen, Oceanus.maxFavor, Oceanus.maxHealth),

	// Iapetus
	IAPETUS(Iapetus.name, Iapetus.permission, Iapetus.alliance, Iapetus.color, Iapetus.claimItems, Iapetus.forsakeItems, Iapetus.shortDescription, Iapetus.lore, Iapetus.flags, Iapetus.abilities, Iapetus.accuracy, Iapetus.favorRegen, Iapetus.maxFavor, Iapetus.maxHealth),

	/**
	 * Fates (Admin Deities)
	 */
	// Clotho
	CLOTHO(Clotho.name, Clotho.permission, Clotho.alliance, Clotho.color, Clotho.claimItems, Clotho.forsakeItems, Clotho.shortDescription, Clotho.lore, Clotho.flags, Clotho.abilities, Clotho.accuracy, Clotho.favorRegen, Clotho.maxFavor, Clotho.maxHealth),

	// Lachesis
	LACHESIS(Lachesis.name, Lachesis.permission, Lachesis.alliance, Lachesis.color, Lachesis.claimItems, Lachesis.forsakeItems, Lachesis.shortDescription, Lachesis.lore, Lachesis.flags, Lachesis.abilities, Lachesis.accuracy, Lachesis.favorRegen, Lachesis.maxFavor, Lachesis.maxHealth),

	// Atropos
	ATROPOS(Atropos.name, Atropos.permission, Atropos.alliance, Atropos.color, Atropos.claimItems, Atropos.forsakeItems, Atropos.shortDescription, Atropos.lore, Atropos.flags, Atropos.abilities, Atropos.accuracy, Atropos.favorRegen, Atropos.maxFavor, Atropos.maxHealth),

	/**
	 * Donors
	 */
	// OmegaX17
	OMEGAX17(OmegaX17.name, OmegaX17.permission, OmegaX17.alliance, OmegaX17.color, OmegaX17.claimItems, OmegaX17.forsakeItems, OmegaX17.shortDescription, OmegaX17.lore, OmegaX17.flags, OmegaX17.abilities, OmegaX17.accuracy, OmegaX17.favorRegen, OmegaX17.maxFavor, OmegaX17.maxHealth),

	// DrD1sco
	DRD1SCO(DrD1sco.name, DrD1sco.permission, DrD1sco.alliance, DrD1sco.color, DrD1sco.claimItems, DrD1sco.forsakeItems, DrD1sco.shortDescription, DrD1sco.lore, DrD1sco.flags, DrD1sco.abilities, DrD1sco.accuracy, DrD1sco.favorRegen, DrD1sco.maxFavor, DrD1sco.maxHealth);

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

	private GreekDeity(String name, String permission, Alliance alliance, ChatColor color, Map<Material, Integer> claimItems, Map<Material, Integer> forsakeItems, String shortDescription, List<String> lore, Set<Flag> flags, List<Ability> abilities, int accuracy, int favorRegen, int maxFavor, double maxHealth)
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
