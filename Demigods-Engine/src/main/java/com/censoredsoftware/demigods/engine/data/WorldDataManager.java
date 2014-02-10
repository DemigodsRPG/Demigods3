package com.censoredsoftware.demigods.engine.data;

import com.google.common.collect.Maps;
import org.bukkit.World;

import java.util.concurrent.ConcurrentMap;

public class WorldDataManager
{
	// -- WORLD DATA -- //

	// World Data
	private static final ConcurrentMap<String, WorldData> WORLDS = Maps.newConcurrentMap();

	public static void addWorld(World world)
	{
		WorldData dWorld = new WorldData(world.getName(), world.getWorldFolder().getPath());
		WORLDS.put(world.getName(), dWorld);
	}

	public static void addWorld(WorldData world)
	{
		WORLDS.put(world.getName(), world);
	}

	public static void removeWorld(String name)
	{
		WORLDS.get(name).save();
		WORLDS.remove(name);
	}

	public static WorldData getWorld(String name)
	{
		return WORLDS.get(name);
	}
}
