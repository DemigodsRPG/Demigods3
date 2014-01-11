package com.censoredsoftware.demigods.engine.template;

import com.censoredsoftware.censoredlib.helper.ConfigFile2;
import com.censoredsoftware.censoredlib.language.Symbol;
import com.censoredsoftware.censoredlib.util.Strings;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.google.common.base.Function;
import com.google.common.collect.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.MaterialData;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TemplateDeity implements Deity
{
	public static String name = "Template", shortDescription = ChatColor.GRAY + "The deity of testing.";
	public static Alliance alliance = new Alliance()
	{
		@Override
		public String getName()
		{
			return "Test";
		}

		@Override
		public String getShortDescription()
		{
			return "Test alliance.";
		}

		@Override
		public String getPermission()
		{
			return "demigods.alliance.test";
		}

		@Override
		public PermissionDefault getPermissionDefault()
		{
			return PermissionDefault.FALSE;
		}

		@Override
		public boolean isPlayable()
		{
			return false;
		}
	};
	public static String permission = alliance.getPermission() + "." + name.toLowerCase();
	public static PermissionDefault permissionDefault = PermissionDefault.FALSE;
	public static int accuracy = 15, favorRegen = 5, maxFavor = 20000, favorBank = 10000;
	public static double maxHealth = 40.0;
	public static ChatColor color = ChatColor.GRAY;
	public static Map<Material, Integer> claimItems = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 1));
	public static Map<Material, Integer> forsakeItems = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 1));
	public static List<String> lore = new ArrayList<String>(9 + claimItems.size())
	{
		{
			add(" ");
			add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + color + name);
			add(ChatColor.RESET + "-----------------------------------------------------");
			add(" ");
			add(ChatColor.YELLOW + " Claim Items:");
			add(" ");
			for(Map.Entry<Material, Integer> entry : claimItems.entrySet())
				add(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.WHITE + entry.getValue() + " " + Strings.beautify(entry.getKey().name()).toLowerCase() + (entry.getValue() > 1 ? "s" : ""));
			add(" ");
			add(ChatColor.YELLOW + " Abilities:");
			add(" ");
		}
	};
	public static Set<Deity.Flag> flags = Sets.newHashSet(Deity.Flag.NON_PLAYABLE);
	public static List<Ability> abilities = Lists.newArrayList((Ability) new TemplateAbility(name));

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Alliance getAlliance()
	{
		return alliance;
	}

	@Override
	public String getPermission()
	{
		return permission;
	}

	@Override
	public PermissionDefault getPermissionDefault()
	{
		return permissionDefault;
	}

	@Override
	public ChatColor getColor()
	{
		return color;
	}

	@Override
	public MaterialData getMaterialData()
	{
		return null; // TODO
	}

	@Override
	public Sound getSound()
	{
		return null; // TODO
	}

	@Override
	public Map<Material, Integer> getClaimItems()
	{
		return claimItems;
	}

	@Override
	public Map<Material, Integer> getForsakeItems()
	{
		return forsakeItems;
	}

	@Override
	public String getShortDescription()
	{
		return shortDescription;
	}

	@Override
	public List<String> getLore()
	{
		return lore;
	}

	@Override
	public Set<Flag> getFlags()
	{
		return flags;
	}

	@Override
	public List<Ability> getAbilities()
	{
		return abilities;
	}

	@Override
	public int getAccuracy()
	{
		return accuracy;
	}

	@Override
	public int getFavorRegen()
	{
		return favorRegen;
	}

	@Override
	public int getMaxFavor()
	{
		return maxFavor;
	}

	@Override
	public double getMaxHealth()
	{
		return maxHealth;
	}

	@Override
	public int getFavorBank()
	{
		return favorBank;
	}

	@Override
	public void updateMood()
	{
		// TODO
	}

	@Override
	public ConfigFile2 getConfig()
	{
		return new ConfigFile2()
		{
			@Override
			public ConfigFile2 unserialize(ConfigurationSection conf)
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
				return this;
			}

			@Override
			public String getSavePath()
			{
				return DemigodsPlugin.plugin().getDataFolder() + "/config/deity/" + alliance.getName().toLowerCase() + "/"; // Don't change this.
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

			protected Map<String, Object> convertItemsToSave(Map<Material, Integer> given)
			{
				Map<String, Object> map = Maps.newHashMap();
				for(Map.Entry<Material, Integer> entry : given.entrySet())
					map.put(entry.getKey().name(), entry.getValue());
				return map;
			}

			protected Map<Material, Integer> convertItemsFromSave(Map<String, Object> given)
			{
				Map<Material, Integer> map = Maps.newHashMap();
				for(Map.Entry<String, Object> entry : given.entrySet())
					map.put(Material.valueOf(entry.getKey()), Integer.valueOf(entry.getValue().toString()));
				return map;
			}

			protected List<String> convertFlagsToSave(Set<Deity.Flag> given)
			{
				return Lists.newArrayList(Collections2.transform(given, new Function<Flag, String>()
				{
					@Override
					public String apply(Deity.Flag flag)
					{
						return flag.name();
					}
				}));
			}

			protected Set<Deity.Flag> convertFlagsFromSave(List<String> given)
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
		};
	}
}
