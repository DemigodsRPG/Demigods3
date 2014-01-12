package com.censoredsoftware.demigods.exclusive.item;

import com.censoredsoftware.censoredlib.helper.ConfigFile2;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public enum LevelDrops
{
	_1(new Config(1)), _2(new Config(2)), _3(new Config(3)), _4(new Config(4)), _5(new Config(5));

	final ImmutableList<ItemStack> drops;

	private LevelDrops(Config config)
	{
		this.drops = config.getItems();
	}

	public ImmutableList<ItemStack> getList()
	{
		return drops;
	}

	public static ItemStack create(Material material, int amount)
	{
		return new ItemStack(material, amount);
	}

	private static class Config extends ConfigFile2
	{
		private static final String SAVE_PATH = DemigodsPlugin.plugin().getDataFolder() + "/config/level_items/";

		private final String saveFile;
		private ImmutableList<ItemStack> list;

		Config(int level)
		{
			saveFile = String.valueOf(level);
		}

		ImmutableList<ItemStack> getItems()
		{
			return list;
		}

		@Override
		public Config unserialize(ConfigurationSection conf)
		{
			ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
			for(Map.Entry<String, Object> entry : conf.getValues(false).entrySet())
			{
				if(!isMaterial(entry.getKey()) || !isInteger(entry.getValue().toString())) continue;
				builder.add(new ItemStack(Material.valueOf(entry.getKey()), Integer.parseInt(entry.getValue().toString())));
			}
			list = builder.build();
			return this;
		}

		@Override
		public String getSavePath()
		{
			return SAVE_PATH;
		}

		@Override
		public String getSaveFile()
		{
			return saveFile;
		}

		@Override
		public Map<String, Object> serialize()
		{
			return Maps.newHashMap();
		}

		private static boolean isMaterial(String str)
		{
			try
			{
				Material test = Material.valueOf(str);
				return test != null;
			}
			catch(Exception ignored)
			{
				// ignored
			}
			return false;
		}

		private static boolean isInteger(String str)
		{
			try
			{
				Integer.parseInt(str);
				return true;
			}
			catch(Exception ignored)
			{
				// ignored
			}
			return false;
		}
	}
}