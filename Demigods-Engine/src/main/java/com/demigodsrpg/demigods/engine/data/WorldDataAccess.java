package com.demigodsrpg.demigods.engine.data;

import com.censoredsoftware.library.serializable.DataSerializable;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unchecked")
public abstract class WorldDataAccess<K, V extends WorldDataAccess<K, V>> implements DataSerializable
{
	@SuppressWarnings("RedundantCast")
	private final Class<V> clazz = (Class<V>) ((V) this).getClass();

	public V getDirect(DemigodsWorld world, K key)
	{
		return DataManager.WORLD_DATA_MANAGER.getFor(clazz, world, key);
	}

	public Collection<V> getAll(DemigodsWorld world)
	{
		return DataManager.WORLD_DATA_MANAGER.getAllOf(clazz, world);
	}

	public Set<V> getAll()
	{
		Set<V> valueSet = Sets.newHashSet();
		for(DemigodsWorld world : WorldDataManager.getWorlds())
			valueSet.addAll(DataManager.WORLD_DATA_MANAGER.getAllOf(clazz, world));
		return valueSet;
	}

	public Collection<V> getAllWith(DemigodsWorld world, Predicate<V> predicate)
	{
		return Collections2.filter(getAll(world), predicate);
	}

	public Set<V> getAllWith(Predicate<V> predicate)
	{
		return Sets.filter(getAll(), predicate);
	}

	public ConcurrentMap<K, V> getMap(DemigodsWorld world)
	{
		return DataManager.WORLD_DATA_MANAGER.getMapFor(clazz, world);
	}

	/**
	 * @deprecated Only use this is you have to. Returns an immutable view of all of the maps.
	 */
	public ImmutableMap<K, V> getMapsReadOnly()
	{
		ImmutableMap.Builder builder = ImmutableMap.builder();
		for(DemigodsWorld world : WorldDataManager.getWorlds())
			builder.putAll(DataManager.WORLD_DATA_MANAGER.getMapFor(clazz, world));
		return builder.build();
	}

	public void put(DemigodsWorld world, K key, V value)
	{
		DataManager.WORLD_DATA_MANAGER.putFor(clazz, world, key, value);
	}

	public void save()
	{
		put(getWorld(), getId(), (V) this);
	}

	public void remove(DemigodsWorld world, K key)
	{
		DataManager.WORLD_DATA_MANAGER.removeFor(clazz, world, key);
	}

	public void remove()
	{
		remove(getWorld(), getId());
	}

	protected abstract K getId();

	protected abstract DemigodsWorld getWorld();
}
