package com.censoredsoftware.demigods.engine.data;

import java.util.Set;

public interface DataSource
{
	void init();

	void save();

	void flushData();

	<T> T get(Class<T> clazz, String key);

	<T> Set<T> getAll(Class<T> clazz);

	<T> void save(Class<T> clazz, String key, T value);
}
