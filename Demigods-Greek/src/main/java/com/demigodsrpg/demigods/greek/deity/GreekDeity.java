package com.demigodsrpg.demigods.greek.deity;

import com.censoredsoftware.library.serializable.yaml.SimpleYamlFile;
import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.deity.Ability;
import com.demigodsrpg.demigods.engine.deity.Alliance;
import com.demigodsrpg.demigods.engine.deity.Deity;
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
import org.bukkit.permissions.PermissionDefault;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GreekDeity implements Deity, Deity.MoodManager
{
	private String name, permission;
	private PermissionDefault permissionDefault;
	private Alliance alliance;
	private ChatColor color;
	private Map<Material, Integer> claimItems, forsakeItems;
	private String shortDescription;
	private List<String> lore;
	private Set<Flag> flags;
	private List<Ability> abilities;
	private int accuracy, favorRegen, maxFavor, favorBank;
	private double maxHealth;

	// Mood Manager
	private EnumMap<Mood, MoodPack> moodPacks;

	// Config File
	private SimpleYamlFile<Config> configFile;

	public GreekDeity(String name, String permission, PermissionDefault permissionDefault, Alliance alliance, ChatColor color, Map<Material, Integer> claimItems, Map<Material, Integer> forsakeItems, String shortDescription, List<String> lore, Set<Flag> flags, List<Ability> abilities, int accuracy, int favorRegen, int maxFavor, double maxHealth, int favorBank, EnumMap<Deity.Mood, Deity.MoodPack> moodPacks)
	{
		this.name = name;
		this.permission = permission;
		this.permissionDefault = permissionDefault;
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
		this.moodPacks = moodPacks;

		configFile = new Config();

		getConfig().loadDataFromFile();
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

	@Override
	public PermissionDefault getPermissionDefault()
	{
		return permissionDefault;
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

	public Set<Deity.Flag> getFlags()
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

	public SimpleYamlFile getConfig()
	{
		return configFile;
	}

	public void updateMood()
	{
		// TODO
	}

	public void set(Deity.Mood mood, Deity.MoodPack moodPack)
	{
		// TODO
	}

	public Deity.MoodPack get(Deity.Mood mood)
	{
		// TODO
		return null;
	}

	public class Config extends SimpleYamlFile<Config>
	{
		@Override
		public Config valueFromData(ConfigurationSection conf)
		{
			if(conf.isString("name")) name = conf.getString("name");
			if(conf.isString("permission")) permission = conf.getString("permission");
			if(conf.isString("permissionDefault")) permissionDefault = PermissionDefault.valueOf(conf.getString("permissionDefault"));
			if(conf.isString("color")) color = ChatColor.valueOf(conf.getString("color"));
			if(conf.getConfigurationSection("claimItems") != null) claimItems = convertItemsFromSave(conf.getConfigurationSection("claimItems").getValues(false));
			if(conf.getConfigurationSection("forsakeItems") != null) forsakeItems = convertItemsFromSave(conf.getConfigurationSection("forsakeItems").getValues(false));
			if(conf.isString("shortDescription")) shortDescription = conf.getString("shortDescription");
			if(conf.isList("flags")) flags = convertFlagsFromSave(conf.getStringList("flags"));
			if(conf.isInt("accuracy")) accuracy = conf.getInt("accuracy");
			if(conf.isInt("favorRegen")) favorRegen = conf.getInt("favorRegen");
			if(conf.isInt("maxFavor")) maxFavor = conf.getInt("maxFavor");
			if(conf.isDouble("maxHealth")) maxHealth = conf.getDouble("maxHealth");
			return this;
		}

		@Override
		public String getDirectoryPath()
		{
			return DemigodsPlugin.getInst().getDataFolder() + "/config/deity/" + alliance.getName().toLowerCase() + "/"; // Don't change this.
		}

		@Override
		public String getFullFileName()
		{
			return name.toLowerCase() + ".yml";
		}

		@Override
		public void loadDataFromFile()
		{
			getCurrentFileData();
		}

		@Override
		public Map<String, Object> serialize()
		{
			Map<String, Object> map = Maps.newHashMap();
			map.put("name", name);
			map.put("permission", permission);
			if(permissionDefault != null) map.put("permissionDefault", permissionDefault.name());
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
	}

	protected static Map<String, Object> convertItemsToSave(Map<Material, Integer> given)
	{
		Map<String, Object> map = Maps.newHashMap();
		for(Map.Entry<Material, Integer> entry : given.entrySet())
			map.put(entry.getKey().name(), entry.getValue());
		return map;
	}

	protected static Map<Material, Integer> convertItemsFromSave(Map<String, Object> given)
	{
		Map<Material, Integer> map = Maps.newHashMap();
		for(Map.Entry<String, Object> entry : given.entrySet())
			map.put(Material.valueOf(entry.getKey()), Integer.valueOf(entry.getValue().toString()));
		return map;
	}

	protected static List<String> convertFlagsToSave(Set<Deity.Flag> given)
	{
		return Lists.newArrayList(Collections2.transform(given, new Function<Deity.Flag, String>()
		{
			@Override
			public String apply(Deity.Flag flag)
			{
				return flag.name();
			}
		}));
	}

	protected static Set<Deity.Flag> convertFlagsFromSave(List<String> given)
	{
		return Sets.newHashSet(Collections2.transform(given, new Function<String, Deity.Flag>()
		{
			@Override
			public Deity.Flag apply(String flag)
			{
				return Deity.Flag.valueOf(flag);
			}
		}));
	}
}
