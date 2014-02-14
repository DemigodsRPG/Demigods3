package com.demigodsrpg.demigods.engine.data.file;

import com.censoredsoftware.censoredlib.helper.ConfigFile;
import com.demigodsrpg.demigods.engine.data.WorldDataAccess;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * Abstract class extending ConfigFile for easy yaml file creation inside of Demigods.
 * 
 * @param <K> The id type.
 * @param <V> The data type.
 */
public abstract class DemigodsWorldFile<K, V extends WorldDataAccess<K, V>> extends ConfigFile<K, V>
{
	private final String fileName, fileType, savePath;
	ConcurrentMap<K, V> dataStore = Maps.newConcurrentMap();

	public DemigodsWorldFile(String fileName, String fileType, String savePath)
	{
		this.fileName = fileName;
		this.fileType = fileType;
		this.savePath = savePath;
	}

	@Override
	public final ConcurrentMap<K, V> getLoadedData()
	{
		return dataStore;
	}

	@Override
	public final Map<String, Object> serialize(K id)
	{
		return getLoadedData().get(id).serialize();
	}

	@Override
	public String getSavePath()
	{
		return savePath;
	}

	public final String getFileName()
	{
		return fileName;
	}

	@Override
	public final String getSaveFile()
	{
		return fileName + fileType;
	}

	@Override
	public final void loadToData()
	{
		dataStore = loadFromFile();
	}

	public final boolean containsKey(K key)
	{
		return dataStore.containsKey(key);
	}

	public final V get(K key)
	{
		return dataStore.get(key);
	}

	public final void put(K key, V value)
	{
		dataStore.put(key, value);
	}

	public final void remove(K key)
	{
		dataStore.remove(key);
	}

	public final Set<K> keySet()
	{
		return dataStore.keySet();
	}

	public final Set<Map.Entry<K, V>> entrySet()
	{
		return dataStore.entrySet();
	}

	public final Collection<V> values()
	{
		return dataStore.values();
	}

	public final void clear()
	{
		dataStore.clear();
	}
}
