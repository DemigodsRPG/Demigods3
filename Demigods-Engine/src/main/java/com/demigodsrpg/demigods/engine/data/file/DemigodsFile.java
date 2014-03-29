package com.demigodsrpg.demigods.engine.data.file;

import com.censoredsoftware.library.serializable.yaml.TieredStringConvertableGenericYamlFile;
import com.demigodsrpg.demigods.engine.data.DataAccess;
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
public abstract class DemigodsFile<K extends Comparable, V extends DataAccess<K, V>> extends TieredStringConvertableGenericYamlFile<K, V>
{
	private final String fileName, fileType, savePath;
	ConcurrentMap<K, V> dataStore = Maps.newConcurrentMap();

	public DemigodsFile(String fileName, String fileType, String savePath)
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
	public String getDirectoryPath()
	{
		return savePath;
	}

	@Override
	public final String getFullFileName()
	{
		return fileName + fileType;
	}

	@Override
	public final void loadDataFromFile()
	{
		dataStore = getCurrentFileData();
	}

	public final boolean containsKey(K key)
	{
		if(key == null) return false;
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
