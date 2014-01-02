package com.censoredsoftware.demigods.exclusive;

import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Mythos;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;

public class Exclusive
{
	private static final Exclusive INST;

	public static final ExclusiveMythos PLUGIN;

	private static BiMap<Alliance, String> WORLDS;

	final ImmutableSet<Listener> LISTENERS;

	static
	{
		PLUGIN = (ExclusiveMythos) Bukkit.getServer().getPluginManager().getPlugin("Demigods-Exclusive");
		INST = new Exclusive();
	}

	private Exclusive()
	{
		WORLDS = loadWorlds();
		LISTENERS = loadListeners();
	}

	public static Exclusive inst()
	{
		return INST;
	}

	public static boolean init()
	{
		Bukkit.getServer().getServicesManager().register(Mythos.class, PLUGIN, PLUGIN, ServicePriority.Highest); // not really sure how Bukkit handles these, presuming the same way as EventPriority
		return true;
	}

	BiMap<Alliance, String> loadWorlds()
	{
		return HashBiMap.create();
	}

	ImmutableSet<Listener> loadListeners()
	{
		return ImmutableSet.of();
	}
}