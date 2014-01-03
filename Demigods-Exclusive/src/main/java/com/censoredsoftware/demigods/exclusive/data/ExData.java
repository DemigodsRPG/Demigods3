package com.censoredsoftware.demigods.exclusive.data;

import com.censoredsoftware.demigods.engine.data.Data;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.concurrent.ConcurrentMap;

public class ExData
{
	public static final Data.DemigodsFile<String, ExDPlayer> EXCLUSIVE_PLAYER = new Data.DemigodsFile<String, ExDPlayer>("exclusiveplayers.yml")
	{
		@Override
		public ExDPlayer create(String mojangAccount, ConfigurationSection conf)
		{
			return new ExDPlayer(mojangAccount, conf);
		}

		@Override
		public String convertFromString(String stringId)
		{
			return stringId;
		}
	};

	private static ConcurrentMap<String, DWorld> worlds = Maps.newConcurrentMap();

	public static void addWorld(World world)
	{
		DWorld dWorld = new DWorld(world.getName(), world.getWorldFolder().getPath() + "\\");
		dWorld.loadFromFile();
		worlds.put(world.getName(), dWorld);
	}

	public static void addWorld(DWorld world)
	{
		worlds.put(world.getName(), world);
	}

	public static void removeWorld(String name)
	{
		worlds.get(name).saveToFile();
		worlds.remove(name);
	}

	public static DWorld getWorld(String name)
	{
		return worlds.get(name);
	}

	public static Data.DemigodsFile[] values()
	{
		return new Data.DemigodsFile[] { EXCLUSIVE_PLAYER };
	}

	public static void init()
	{
		for(Data.DemigodsFile data : values())
			data.loadToData();

		for(World world : Bukkit.getWorlds())
			addWorld(world);
	}

	private ExData()
	{}

	public static void save()
	{
		for(Data.DemigodsFile data : values())
			data.saveToFile();

		for(World world : Bukkit.getWorlds())
			getWorld(world.getName()).saveToFile();
	}

	public static void flushData()
	{
		// Clear the data
		for(Data.DemigodsFile data : values())
			data.clear();

		// TODO Does not flush world data... should it?

		save();
	}
}