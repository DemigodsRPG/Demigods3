package com.censoredsoftware.demigods.engine.data;

import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public interface DataManager
{
	void init();

	void save();

	void flushData() throws AccessDeniedException;

	<T, K> T getFor(Class<T> clazz, K key);

	<T> Collection<T> getAllOf(Class<T> clazz);

	<T> ConcurrentMap<?, T> getMapFor(Class<T> clazz);

	<T, K> void saveFor(Class<T> clazz, K key, T value);

	<T, K> void removeFor(Class<T> clazz, K key);
}
