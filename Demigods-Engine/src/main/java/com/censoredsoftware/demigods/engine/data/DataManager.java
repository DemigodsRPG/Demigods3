package com.censoredsoftware.demigods.engine.data;

import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public interface DataManager
{
	void init();

	void save();

	void flushData() throws AccessDeniedException;

	<V extends DataAccess<K, V>, K> V getFor(Class<V> clazz, K key);

	<V extends DataAccess<?, V>> Collection<V> getAllOf(Class<V> clazz);

	<V extends DataAccess<?, V>> ConcurrentMap<?, V> getMapFor(Class<V> clazz);

	<K, V extends DataAccess<K, V>> void putFor(Class<V> clazz, K key, V value);

	<K, V extends DataAccess<K, V>> void removeFor(Class<V> clazz, K key);
}
