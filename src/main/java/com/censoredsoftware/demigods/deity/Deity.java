package com.censoredsoftware.demigods.deity;

import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.deity.god.Hades;
import com.censoredsoftware.demigods.deity.god.Poseidon;
import com.censoredsoftware.demigods.deity.god.Zeus;
import com.censoredsoftware.demigods.deity.titan.Iapetus;
import com.censoredsoftware.demigods.deity.titan.Oceanus;
import com.censoredsoftware.demigods.deity.titan.Prometheus;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum Deity
{
	/**
	 * Template
	 */
	TEMPLATE(TemplateDeity.name, TemplateDeity.alliance, TemplateDeity.permission, TemplateDeity.color, TemplateDeity.claimItems, TemplateDeity.forsakeItems, TemplateDeity.lore, TemplateDeity.flags, TemplateDeity.abilities, TemplateDeity.accuracy, TemplateDeity.favorRegen, TemplateDeity.maxFavor, TemplateDeity.maxHealth),

	/**
	 * Gods
	 */
	// Zeus
	ZEUS(Zeus.name, Zeus.alliance, Zeus.permission, Zeus.color, Zeus.claimItems, Zeus.forsakeItems, Zeus.lore, Zeus.flags, Zeus.abilities, Zeus.accuracy, Zeus.favorRegen, Zeus.maxFavor, Zeus.maxHealth),

	// Poseidon
	POSEIDON(Poseidon.name, Poseidon.alliance, Poseidon.permission, Poseidon.color, Poseidon.claimItems, Poseidon.forsakeItems, Poseidon.lore, Poseidon.flags, Poseidon.abilities, Poseidon.accuracy, Poseidon.favorRegen, Poseidon.maxFavor, Poseidon.maxHealth),

	// Hades
	HADES(Hades.name, Hades.alliance, Hades.permission, Hades.color, Hades.claimItems, Hades.forsakeItems, Hades.lore, Hades.flags, Hades.abilities, Hades.accuracy, Hades.favorRegen, Hades.maxFavor, Hades.maxHealth),

	/**
	 * Titans
	 */
	// Prometheus
	PROMETHEUS(Prometheus.name, Prometheus.alliance, Prometheus.permission, Prometheus.color, Prometheus.claimItems, Prometheus.forsakeItems, Prometheus.lore, Prometheus.flags, Prometheus.abilities, Prometheus.accuracy, Prometheus.favorRegen, Prometheus.maxFavor, Prometheus.maxHealth),

	// Oceanus
	OCEANUS(Oceanus.name, Oceanus.alliance, Oceanus.permission, Oceanus.color, Oceanus.claimItems, Oceanus.forsakeItems, Oceanus.lore, Oceanus.flags, Oceanus.abilities, Oceanus.accuracy, Oceanus.favorRegen, Oceanus.maxFavor, Oceanus.maxHealth),

	// Iapetus
	IAPETUS(Iapetus.name, Iapetus.alliance, Iapetus.permission, Iapetus.color, Iapetus.claimItems, Iapetus.forsakeItems, Iapetus.lore, Iapetus.flags, Iapetus.abilities, Iapetus.accuracy, Iapetus.favorRegen, Iapetus.maxFavor, Iapetus.maxHealth);

	private String name, alliance, permission;
	private ChatColor color;
	private Map<Material, Integer> claimItems, forsakeItems;
	private List<String> lore;
	private Set<Flag> flags;
	private Set<Ability> abilities;
	private int accuracy, favorRegen, maxFavor;
	private double maxHealth;

	private Deity(String name, String alliance, String permission, ChatColor color, Map<Material, Integer> claimItems, Map<Material, Integer> forsakeItems, List<String> lore, Set<Flag> flags, Set<Ability> abilities, int accuracy, int favorRegen, int maxFavor, double maxHealth)
	{
		this.name = name;
		this.alliance = alliance;
		this.permission = permission;
		this.color = color;
		this.claimItems = claimItems;
		this.forsakeItems = forsakeItems;
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

	public String getAlliance()
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

	public List<String> getLore()
	{
		return lore;
	}

	public Set<Flag> getFlags()
	{
		return flags;
	}

	public Set<Ability> getAbilities()
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
		PLAYABLE, NON_PLAYABLE, MAJOR_DEITY, MINOR_DEITY, NEUTRAL, DIFFICULT, ALTERNATE_ASCENSION_LEVELING, NO_SHRINE, NO_OBELISK
	}

	public static class Util
	{
		public static Collection<String> getLoadedPlayableDeityAlliances()
		{
			return Collections2.filter(getLoadedDeityAlliances(), new Predicate<String>()
			{
				@Override
				public boolean apply(String alliance)
				{
					return getLoadedPlayableDeitiesInAlliance(alliance).size() > 0;
				}
			});
		}

		public static Collection<Deity> getLoadedPlayableDeitiesInAlliance(final String alliance)
		{
			return Collections2.filter(getLoadedDeitiesInAlliance(alliance), new Predicate<Deity>()
			{
				@Override
				public boolean apply(Deity d)
				{
					return d.getFlags().contains(Flag.PLAYABLE) && d.getAlliance().equalsIgnoreCase(alliance);
				}
			});
		}

		public static Collection<String> getLoadedMajorPlayableDeityAlliances()
		{
			return Collections2.filter(getLoadedDeityAlliances(), new Predicate<String>()
			{
				@Override
				public boolean apply(String alliance)
				{
					return getLoadedMajorPlayableDeitiesInAlliance(alliance).size() > 0;
				}
			});
		}

		public static Collection<Deity> getLoadedMajorPlayableDeitiesInAlliance(final String alliance)
		{
			return Collections2.filter(getLoadedDeitiesInAlliance(alliance), new Predicate<Deity>()
			{
				@Override
				public boolean apply(Deity d)
				{
					return d.getFlags().contains(Flag.PLAYABLE) && d.getFlags().contains(Flag.MAJOR_DEITY) && d.getAlliance().equalsIgnoreCase(alliance);
				}
			});
		}

		public static Set<String> getLoadedDeityAlliances()
		{
			return Sets.newHashSet(Collections2.transform(Sets.newHashSet(values()), new Function<Deity, String>()
			{
				@Override
				public String apply(Deity d)
				{
					return d.getAlliance();
				}
			}));
		}

		public static Collection<Deity> getLoadedDeitiesInAlliance(final String alliance)
		{
			return Collections2.filter(Collections2.transform(Sets.newHashSet(values()), new Function<Deity, Deity>()
			{
				@Override
				public Deity apply(Deity d)
				{
					return d;
				}
			}), new Predicate<Deity>()
			{
				@Override
				public boolean apply(Deity d)
				{
					return d.getAlliance().equalsIgnoreCase(alliance);
				}
			});
		}

		public static Deity getDeity(String deity)
		{
			return Deity.valueOf(deity.toUpperCase());
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
