package com.demigodsrpg.demigods.engine.data;

import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.data.file.FileDataManager;
import com.demigodsrpg.demigods.engine.location.DemigodsLocation;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructure;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("ALL")
public class WorldDataManager implements Listener
{
	static final ConcurrentMap<String, DemigodsWorld> WORLDS = Maps.newConcurrentMap();

	WorldDataManager()
	{
		Bukkit.getPluginManager().registerEvents(this, DemigodsPlugin.getInst());
	}

	// -- WORLD DATA -- //

	void addWorld(World world)
	{
		DemigodsWorld demigodsWorld = new DemigodsWorld(world.getName(), FileDataManager.SAVE_PATH + "worlds/" + world.getName() + "/");
		demigodsWorld.loadData();
		WORLDS.put(world.getName(), demigodsWorld);
	}

	void removeWorld(String name)
	{
		WORLDS.remove(name);
	}

	public static DemigodsWorld getWorld(String name)
	{
		if(name == null || !WORLDS.containsKey(name)) return null;
		return WORLDS.get(name);
	}

	public static Collection<DemigodsWorld> getWorlds()
	{
		return WORLDS.values();
	}

	// Prevent accidental double init.
	private static boolean didInit = false;

	public void init()
	{
		// Check if init has happened already...
		if(didInit) throw new RuntimeException("Data tried to initialize more than once.");

		// Load worlds
		for(World world : Bukkit.getWorlds())
			addWorld(world);

		// Let the plugin know that this has finished.
		didInit = true;
	}

	protected void save()
	{
		for(DemigodsWorld world : WORLDS.values())
			world.saveData();
	}

	protected void flushData() throws AccessDeniedException
	{
		WORLDS.clear();
	}

	protected <V extends WorldDataAccess<K, V>, K> V getFor(Class<V> clazz, DemigodsWorld world, K key)
	{
		if(getMapFor(clazz, world).containsKey(key)) return (V) (WorldDataAccess) getMapFor(clazz, world).get(key);
		return null;
	}

	protected <K, V extends WorldDataAccess<K, V>> Collection<V> getAllOf(Class<V> clazz, DemigodsWorld world)
	{
		return getMapFor(clazz, world).values();
	}

	protected <K, V extends WorldDataAccess<K, V>> ConcurrentMap<K, V> getMapFor(Class<V> clazz, DemigodsWorld world)
	{
		if(clazz == DemigodsLocation.class) return (ConcurrentMap) world.locations().getLoadedData();
		else if(clazz == DemigodsStructure.class) return (ConcurrentMap) world.structures().getLoadedData();
		return null;
	}

	protected <K, V extends WorldDataAccess<K, V>> void putFor(Class<V> clazz, DemigodsWorld world, K key, V value)
	{
		getMapFor(clazz, world).put(key, value);
	}

	protected <K, V extends WorldDataAccess<K, V>> void removeFor(Class<V> clazz, DemigodsWorld world, K key)
	{
		getMapFor(clazz, world).remove(key);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldLoad(WorldLoadEvent event)
	{
		addWorld(event.getWorld());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldUnload(WorldUnloadEvent event)
	{
		removeWorld(event.getWorld().getName());
	}
}
