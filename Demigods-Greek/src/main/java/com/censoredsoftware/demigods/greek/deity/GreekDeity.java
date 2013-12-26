package com.censoredsoftware.demigods.greek.deity;

import com.censoredsoftware.censoredlib.helper.ConfigFile2;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.ability.Ability;
import com.censoredsoftware.demigods.engine.deity.Alliance;
import com.censoredsoftware.demigods.engine.deity.Deity;
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
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.MaterialData;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum GreekDeity implements Deity, Deity.MoodManager
{
	/**
	 * Gods
	 */
	// Zeus
	ZEUS(Zeus.name, Zeus.permission, Zeus.alliance, Zeus.color, Zeus.claimItems, Zeus.forsakeItems, Zeus.shortDescription, Zeus.lore, Zeus.flags, Zeus.abilities, Zeus.accuracy, Zeus.favorRegen, Zeus.maxFavor, Zeus.maxHealth, Zeus.favorBank),

	// Poseidon
	POSEIDON(Poseidon.name, Poseidon.permission, Poseidon.alliance, Poseidon.color, Poseidon.claimItems, Poseidon.forsakeItems, Poseidon.shortDescription, Poseidon.lore, Poseidon.flags, Poseidon.abilities, Poseidon.accuracy, Poseidon.favorRegen, Poseidon.maxFavor, Poseidon.maxHealth, Poseidon.favorBank),

	// Hades
	HADES(Hades.name, Hades.permission, Hades.alliance, Hades.color, Hades.claimItems, Hades.forsakeItems, Hades.shortDescription, Hades.lore, Hades.flags, Hades.abilities, Hades.accuracy, Hades.favorRegen, Hades.maxFavor, Hades.maxHealth, Hades.favorBank),

	/**
	 * Titans
	 */
	// Perses
	PERSES(Perses.name, Perses.permission, Perses.alliance, Perses.color, Perses.claimItems, Perses.forsakeItems, Perses.shortDescription, Perses.lore, Perses.flags, Perses.abilities, Perses.accuracy, Perses.favorRegen, Perses.maxFavor, Perses.maxHealth, Perses.favorBank),

	// Oceanus
	OCEANUS(Oceanus.name, Oceanus.permission, Oceanus.alliance, Oceanus.color, Oceanus.claimItems, Oceanus.forsakeItems, Oceanus.shortDescription, Oceanus.lore, Oceanus.flags, Oceanus.abilities, Oceanus.accuracy, Oceanus.favorRegen, Oceanus.maxFavor, Oceanus.maxHealth, Oceanus.favorBank),

	// Iapetus
	IAPETUS(Iapetus.name, Iapetus.permission, Iapetus.alliance, Iapetus.color, Iapetus.claimItems, Iapetus.forsakeItems, Iapetus.shortDescription, Iapetus.lore, Iapetus.flags, Iapetus.abilities, Iapetus.accuracy, Iapetus.favorRegen, Iapetus.maxFavor, Iapetus.maxHealth, Iapetus.favorBank),

	/**
	 * Fates (Admin Deities)
	 */
	// Clotho
	CLOTHO(Clotho.name, Clotho.permission, Clotho.alliance, Clotho.color, Clotho.claimItems, Clotho.forsakeItems, Clotho.shortDescription, Clotho.lore, Clotho.flags, Clotho.abilities, Clotho.accuracy, Clotho.favorRegen, Clotho.maxFavor, Clotho.maxHealth, Clotho.favorBank),

	// Lachesis
	LACHESIS(Lachesis.name, Lachesis.permission, Lachesis.alliance, Lachesis.color, Lachesis.claimItems, Lachesis.forsakeItems, Lachesis.shortDescription, Lachesis.lore, Lachesis.flags, Lachesis.abilities, Lachesis.accuracy, Lachesis.favorRegen, Lachesis.maxFavor, Lachesis.maxHealth, Lachesis.favorBank),

	// Atropos
	ATROPOS(Atropos.name, Atropos.permission, Atropos.alliance, Atropos.color, Atropos.claimItems, Atropos.forsakeItems, Atropos.shortDescription, Atropos.lore, Atropos.flags, Atropos.abilities, Atropos.accuracy, Atropos.favorRegen, Atropos.maxFavor, Atropos.maxHealth, Atropos.favorBank),

	/**
	 * Donors
	 */
	// OmegaX17
	OMEGAX17(OmegaX17.name, OmegaX17.permission, OmegaX17.alliance, OmegaX17.color, OmegaX17.claimItems, OmegaX17.forsakeItems, OmegaX17.shortDescription, OmegaX17.lore, OmegaX17.flags, OmegaX17.abilities, OmegaX17.accuracy, OmegaX17.favorRegen, OmegaX17.maxFavor, OmegaX17.maxHealth, OmegaX17.favorBank),

	// DrD1sco
	DRD1SCO(DrD1sco.name, DrD1sco.permission, DrD1sco.alliance, DrD1sco.color, DrD1sco.claimItems, DrD1sco.forsakeItems, DrD1sco.shortDescription, DrD1sco.lore, DrD1sco.flags, DrD1sco.abilities, DrD1sco.accuracy, DrD1sco.favorRegen, DrD1sco.maxFavor, DrD1sco.maxHealth, DrD1sco.favorBank);

	private String name, permission;
	private Alliance alliance;
	private ChatColor color;
	private Map<Material, Integer> claimItems, forsakeItems;
	private String shortDescription;
	private List<String> lore;
	private Set<Flag> flags;
	private List<Ability> abilities;
	private int accuracy, favorRegen, maxFavor, favorBank;
	private double maxHealth;
	private ConfigFile2 configFile;

	// Mood Manager
	private EnumMap<Mood, MoodPack> moodPacks;

	private GreekDeity(String name, String permission, Alliance alliance, ChatColor color, Map<Material, Integer> claimItems, Map<Material, Integer> forsakeItems, String shortDescription, List<String> lore, Set<Flag> flags, List<Ability> abilities, int accuracy, int favorRegen, int maxFavor, double maxHealth, int favorBank)
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
		this.favorBank = favorBank;

		configFile = new File();

		getConfig().loadFromFile();
	}

	@Override
	public String toString()
	{
		return name;
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

	public MaterialData getMaterialData()
	{
		// TODO
		return null;
	}

	public Sound getSound()
	{
		// TODO
		return null;
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

	public int getFavorBank()
	{
		return favorBank;
	}

	public ConfigFile2 getConfig()
	{
		return configFile;
	}

    public void updateMood()
    {
		// TODO
	}

	public void set(Mood mood, MoodPack moodPack)
	{
		// TODO
	}

	public MoodPack get(Mood mood)
	{
		// TODO
		return null;
	}

	public class File extends ConfigFile2
	{
		@Override
		public void unserialize(ConfigurationSection conf)
		{
			if(conf.isString("name")) name = conf.getString("name");
			if(conf.isString("permission")) permission = conf.getString("permission");
			if(conf.isString("color")) color = ChatColor.valueOf(conf.getString("color"));
			if(conf.getConfigurationSection("claimItems") != null) claimItems = convertItemsFromSave(conf.getConfigurationSection("claimItems").getValues(false));
			if(conf.getConfigurationSection("forsakeItems") != null) forsakeItems = convertItemsFromSave(conf.getConfigurationSection("forsakeItems").getValues(false));
			if(conf.isString("shortDescription")) shortDescription = conf.getString("shortDescription");
			if(conf.isList("flags")) flags = convertFlagsFromSave(conf.getStringList("flags"));
			if(conf.isInt("accuracy")) accuracy = conf.getInt("accuracy");
			if(conf.isInt("favorRegen")) favorRegen = conf.getInt("favorRegen");
			if(conf.isInt("maxFavor")) maxFavor = conf.getInt("maxFavor");
			if(conf.isDouble("maxHealth")) maxHealth = conf.getDouble("maxHealth");
		}

		@Override
		public String getSavePath()
		{
			return Demigods.PLUGIN.getDataFolder() + "/config/deity/" + alliance.getName().toLowerCase() + "/"; // Don't change this.
		}

		@Override
		public String getSaveFile()
		{
			return name.toLowerCase() + ".yml";
		}

		@Override
		public Map<String, Object> serialize()
		{
			Map<String, Object> map = Maps.newHashMap();
			map.put("name", name);
			map.put("permission", permission);
			// Cannot save/load alliance, currently. Mythos would be screwed up if we did.
			map.put("color", color.name());
			map.put("claimItems", convertItemsToSave(claimItems));
			map.put("forsakeItems", convertItemsToSave(forsakeItems));
			map.put("shortDescription", shortDescription);
			// Don't save lore... it's already dynamic to the other values.
			map.put("flags", convertFlagsToSave(flags));
			// TODO Ability configuration...
			map.put("accuracy", accuracy);
			map.put("favorRegen", favorRegen);
			map.put("maxFavor", maxFavor);
			map.put("maxHealth", maxHealth);
			return map;
		}

		private Map<String, Object> convertItemsToSave(Map<Material, Integer> given)
		{
			Map<String, Object> map = Maps.newHashMap();
			for(Map.Entry<Material, Integer> entry : given.entrySet())
				map.put(entry.getKey().name(), entry.getValue());
			return map;
		}

		private Map<Material, Integer> convertItemsFromSave(Map<String, Object> given)
		{
			Map<Material, Integer> map = Maps.newHashMap();
			for(Map.Entry<String, Object> entry : given.entrySet())
				map.put(Material.valueOf(entry.getKey()), Integer.valueOf(entry.getValue().toString()));
			return map;
		}

		private List<String> convertFlagsToSave(Set<Flag> given)
		{
			return Lists.newArrayList(Collections2.transform(given, new Function<Flag, String>()
			{
				@Override
				public String apply(Flag flag)
				{
					return flag.name();
				}
			}));
		}

		private Set<Flag> convertFlagsFromSave(List<String> given)
		{
			return Sets.newHashSet(Collections2.transform(given, new Function<String, Flag>()
			{
				@Override
				public Flag apply(String flag)
				{
					return Flag.valueOf(flag);
				}
			}));
		}
	}
}
