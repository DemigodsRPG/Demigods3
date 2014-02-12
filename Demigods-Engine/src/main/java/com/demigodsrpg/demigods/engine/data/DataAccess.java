package com.demigodsrpg.demigods.engine.data;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unchecked")
public abstract class DataAccess<K, V extends DataAccess<K, V>> implements ConfigurationSerializable
{
	@SuppressWarnings("RedundantCast")
	private final Class<V> clazz = (Class<V>) ((V) this).getClass();

	public V getDirect(K key)
	{
		return DataManager.DATA_MANAGER.getFor(clazz, key);
	}

	public Collection<V> getAll()
	{
		return DataManager.DATA_MANAGER.getAllOf(clazz);
	}

	public Collection<V> getAllWith(Predicate<V> predicate)
	{
		return Collections2.filter(getAll(), predicate);
	}

	public ConcurrentMap<K, V> getMap()
	{
		return DataManager.DATA_MANAGER.getMapFor(clazz);
	}

	public void put(K key, V value)
	{
		DataManager.DATA_MANAGER.putFor(clazz, key, value);
	}

	public void save()
	{
		put(getId(), (V) this);
	}

	public void remove(K key)
	{
		DataManager.DATA_MANAGER.removeFor(clazz, key);
	}

	public void remove()
	{
		remove(getId());
	}

	protected abstract K getId();
}
