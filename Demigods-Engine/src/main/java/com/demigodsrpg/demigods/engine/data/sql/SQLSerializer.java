package com.demigodsrpg.demigods.engine.data.sql;

import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.demigodsrpg.demigods.engine.data.DataManager;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public abstract class SQLSerializer extends DataManager
{
	@Override
	protected <K extends Comparable, V extends DataAccess<K, V>> V getFor(Class<V> clazz, K key)
	{
		return null; // TODO
	}

	@Override
	protected <K extends Comparable, V extends DataAccess<K, V>> Collection<V> getAllOf(Class<V> clazz)
	{
		return null; // TODO
	}

	@Override
	protected <K extends Comparable, V extends DataAccess<K, V>> ConcurrentMap<K, V> getMapFor(Class<V> clazz)
	{
		return null; // TODO
	}

	@Override
	protected <K extends Comparable, V extends DataAccess<K, V>> void putFor(Class<V> clazz, K key, V value)
	{
		// TODO
	}

	@Override
	protected <K extends Comparable, V extends DataAccess<K, V>> void removeFor(Class<V> clazz, K key)
	{
		// TODO
	}
}
