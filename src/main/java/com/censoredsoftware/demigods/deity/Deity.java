package com.censoredsoftware.demigods.deity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.deity.fate.Atropos;
import com.censoredsoftware.demigods.deity.fate.Clotho;
import com.censoredsoftware.demigods.deity.fate.Lachesis;
import com.censoredsoftware.demigods.deity.god.Hades;
import com.censoredsoftware.demigods.deity.god.Poseidon;
import com.censoredsoftware.demigods.deity.god.Zeus;
import com.censoredsoftware.demigods.deity.titan.Iapetus;
import com.censoredsoftware.demigods.deity.titan.Oceanus;
import com.censoredsoftware.demigods.deity.titan.Perses;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;

public enum Deity
{
	/**
	 * Template
	 */
	TEMPLATE(TemplateDeity.name, TemplateDeity.permission, TemplateDeity.alliance, TemplateDeity.color, TemplateDeity.claimItems, TemplateDeity.forsakeItems, TemplateDeity.shortDescription, TemplateDeity.lore, TemplateDeity.flags, TemplateDeity.abilities, TemplateDeity.accuracy, TemplateDeity.favorRegen, TemplateDeity.maxFavor, TemplateDeity.maxHealth),

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
	ATROPOS(Atropos.name, Atropos.permission, Atropos.alliance, Atropos.color, Atropos.claimItems, Atropos.forsakeItems, Atropos.shortDescription, Atropos.lore, Atropos.flags, Atropos.abilities, Atropos.accuracy, Atropos.favorRegen, Atropos.maxFavor, Atropos.maxHealth);

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

	private Deity(String name, String permission, Alliance alliance, ChatColor color, Map<Material, Integer> claimItems, Map<Material, Integer> forsakeItems, String shortDescription, List<String> lore, Set<Flag> flags, List<Ability> abilities, int accuracy, int favorRegen, int maxFavor, double maxHealth)
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

	public enum Flag
	{
		PLAYABLE, NON_PLAYABLE, MAJOR_DEITY, MINOR_DEITY, NEUTRAL, DIFFICULT, ALTERNATE_ASCENSION_LEVELING, NO_SHRINE, NO_OBELISK, NO_BATTLE
	}

	public static class Util
	{
		public static Deity getDeity(String deity)
		{
			try
			{
				return Deity.valueOf(deity.toUpperCase());
			}
			catch(Exception ignored)
			{}
			return null;
		}

		public static boolean canUseDeity(DCharacter character, String deity)
		{
			if(character == null) return false;
			if(!character.getOfflinePlayer().isOnline()) return canUseDeitySilent(character, deity);
			Player player = character.getOfflinePlayer().getPlayer();
			if(!character.isDeity(deity))
			{
				player.sendMessage(ChatColor.RED + "You haven't claimed " + deity + "! You can't do that!");
				return false;
			}
			return true;
		}

		public static boolean canUseDeitySilent(DCharacter character, String deity)
		{
			return character != null && character.isDeity(deity);
		}

		public static boolean canUseDeitySilent(Player player, String deityName)
		{
			String currentDeityName = DPlayer.Util.getPlayer(player).getCurrentDeityName();
			return currentDeityName != null && currentDeityName.equalsIgnoreCase(deityName);
		}
	}
}
