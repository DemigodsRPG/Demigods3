package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.demigods.engine.Demigods;
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

	public V get(K key)
	{
		return Demigods.getDataManager().getFor(clazz, key);
	}

	public Collection<V> getAll()
	{
		return Demigods.getDataManager().getAllOf(clazz);
	}

	public Collection<V> getAllWith(Predicate<V> predicate)
	{
		return Collections2.filter(getAll(), predicate);
	}

	public ConcurrentMap<K, V> getMap()
	{
		return (ConcurrentMap<K, V>) Demigods.getDataManager().getMapFor(clazz);
	}

	public void put(K key, V value)
	{
		Demigods.getDataManager().putFor(clazz, key, value);
	}

	public void save()
	{
		put(getId(), (V) this);
	}

	public void remove(K key)
	{
		Demigods.getDataManager().removeFor(clazz, key);
	}

	public void remove()
	{
		remove(getId());
	}

	protected abstract K getId();
}
