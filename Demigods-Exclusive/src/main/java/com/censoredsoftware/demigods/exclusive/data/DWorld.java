package com.censoredsoftware.demigods.exclusive.data;

import com.censoredsoftware.censoredlib.helper.ConfigFile2;
import net.minecraft.util.com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Map;

public class DWorld extends ConfigFile2
{
	private final String worldName, worldFolder;

	DWorld(String worldName, String worldFolder)
	{
		this.worldName = worldName;
		this.worldFolder = worldFolder;
	}

	public String getName()
	{
		return worldName;
	}

	@Override
	public DWorld unserialize(ConfigurationSection conf)
	{
		// TODO set values here
		return this;
	}

	@Override
	public String getSavePath()
	{
		return worldFolder;
	}

	@Override
	public String getSaveFile()
	{
		return "demigods.dat";
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = Maps.newHashMap();
		// TODO Other variables
		return map;
	}

	public World getWorld()
	{
		return Bukkit.getWorld(worldName);
	}

	public static class Util
	{
		private Util()
		{}

		public static void save(DWorld dWorld)
		{
			ExData.addWorld(dWorld.getWorld());
		}

		public static boolean delete(DWorld dWorld)
		{
			ExData.removeWorld(dWorld.getName());
			return new File(dWorld.getSavePath() + dWorld.getSaveFile()).delete();
		}
	}
}