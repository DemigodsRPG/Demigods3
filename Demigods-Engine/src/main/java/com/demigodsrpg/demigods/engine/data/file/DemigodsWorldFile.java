package com.demigodsrpg.demigods.engine.data.file;

import com.censoredsoftware.library.serializable.yaml.TieredGenericYamlFile;
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
public abstract class DemigodsWorldFile<K extends Comparable, V extends WorldDataAccess<K, V>> extends TieredGenericYamlFile<K, V>
{
	// Special variables that will be accessed pretty often.
	private final String fileName, fileType, savePath;

	// This is where all the magic happens: All data is accessed from this exact map.
	ConcurrentMap<K, V> dataStore = Maps.newConcurrentMap();

	/**
	 * Create a DemigodsWorldFile from the file name, file extension, and file directory path.
	 *
	 * @param fileName The name of the file.
	 * @param fileType The extension of the file.
	 * @param savePath The file directory path.
	 */
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
