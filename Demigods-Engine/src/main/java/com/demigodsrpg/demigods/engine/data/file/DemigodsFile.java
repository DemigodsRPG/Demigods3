package com.demigodsrpg.demigods.engine.data.file;

import com.censoredsoftware.censoredlib.helper.ConfigFile;
import com.google.common.collect.Maps;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * Abstract class extending ConfigFile for easy yaml file creation inside of Demigods.
 * 
 * @param <ID> The id type.
 * @param <DATA> The data type.
 */
public abstract class DemigodsFile<ID, DATA extends ConfigurationSerializable> extends ConfigFile<ID, DATA>
{
	private final String saveFile, savePath;
	ConcurrentMap<ID, DATA> dataStore = Maps.newConcurrentMap();

	protected DemigodsFile(String saveFile, String savePath)
	{
		this.saveFile = saveFile;
		this.savePath = savePath;
	}

	@Override
	public final ConcurrentMap<ID, DATA> getLoadedData()
	{
		return dataStore;
	}

	@Override
	public final Map<String, Object> serialize(ID id)
	{
		return getLoadedData().get(id).serialize();
	}

	@Override
	public String getSavePath()
	{
		return savePath;
	}

	@Override
	public final String getSaveFile()
	{
		return saveFile;
	}

	@Override
	public final void loadToData()
	{
		dataStore = loadFromFile();
	}

	public final boolean containsKey(ID key)
	{
		return dataStore.containsKey(key);
	}

	public final DATA get(ID key)
	{
		return dataStore.get(key);
	}

	public final void put(ID key, DATA value)
	{
		dataStore.put(key, value);
	}

	public final void remove(ID key)
	{
		dataStore.remove(key);
	}

	public final Set<ID> keySet()
	{
		return dataStore.keySet();
	}

	public final Set<Map.Entry<ID, DATA>> entrySet()
	{
		return dataStore.entrySet();
	}

	public final Collection<DATA> values()
	{
		return dataStore.values();
	}

	public final void clear()
	{
		dataStore.clear();
	}
}
