package com.censoredsoftware.plugin.data;

import com.censoredsoftware.library.serializable.yaml.TieredGenericYamlFile;
import com.censoredsoftware.library.util.Threads;
import com.demigodsrpg.demigods.engine.data.file.FileDataManager;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class Cache
{
	private ConcurrentMap<UUID, LegacyTimedData> cache = Maps.newConcurrentMap();
	private String fileName;
	private CacheFile file;
	private Integer task;
	private UUID timed;

	private Cache()
	{}

	public static Cache load(Plugin plugin, String fileName)
	{
		Cache cache = new Cache();
		cache.fileName = fileName;
		cache.start(plugin);
		return cache;
	}

	private void start(Plugin plugin)
	{
		file = (new CacheFile());
		file.loadDataFromFile();
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new BukkitRunnable()
		{
			@Override
			public void run()
			{
				// Save data
				file.saveDataToFile();
			}
		}, 20, 300 * 20);

		timed = Threads.newTimedTask(new Threads.CensoredRunnable()
		{
			@Override
			public void runIt()
			{
				// Timed
				updateCachedData();
			}
		}, 0, 50);
	}

	public void unload()
	{
		Bukkit.getScheduler().cancelTask(task);
		Threads.getTimedTask(timed).cancel();
		file.saveDataToFile();
	}

	public LegacyTimedData get(UUID id)
	{
		return cache.get(id);
	}

	public Set<LegacyTimedData> getAll()
	{
		return Sets.newHashSet(cache.values());
	}

	public LegacyTimedData find(String key, String subKey)
	{
		if(findByKey(key) == null) return null;

		for(LegacyTimedData data : findByKey(key))
			if(data.getSubKey().equals(subKey)) return data;

		return null;
	}

	public Set<LegacyTimedData> findByKey(final String key)
	{
		return Sets.newHashSet(Collections2.filter(getAll(), new Predicate<LegacyTimedData>()
		{
			@Override
			public boolean apply(LegacyTimedData serverData)
			{
				return serverData.getKey().equals(key);
			}
		}));
	}

	public void delete(LegacyTimedData data)
	{
		cache.remove(data.getId());
	}

	public void remove(String key, String subKey)
	{
		if(find(key, subKey) != null) delete(find(key, subKey));
	}

	public void saveTimed(String key, String subKey, Object data, Integer seconds)
	{
		// Remove the data if it exists already
		remove(key, subKey);

		// Create and save the timed data
		LegacyTimedData cache = new LegacyTimedData();
		cache.generateId();
		cache.setKey(key);
		cache.setSubKey(subKey);
		cache.setData(data.toString());
		cache.setSeconds(seconds);
		this.cache.put(cache.getId(), cache);
	}

	public void saveTimedDay(String key, String subKey, Object data)
	{
		// Remove the data if it exists already
		remove(key, subKey);

		// Create and save the timed data
		LegacyTimedData cache = new LegacyTimedData();
		cache.generateId();
		cache.setKey(key);
		cache.setSubKey(subKey);
		cache.setData(data.toString());
		cache.setHours(24);
		this.cache.put(cache.getId(), cache);
	}

	public void saveTimedWeek(String key, String subKey, Object data)
	{
		// Remove the data if it exists already
		remove(key, subKey);

		// Create and save the timed data
		LegacyTimedData cache = new LegacyTimedData();
		cache.generateId();
		cache.setKey(key);
		cache.setSubKey(subKey);
		cache.setData(data.toString());
		cache.setHours(168);
		this.cache.put(cache.getId(), cache);
	}

	public void removeTimed(String key, String subKey)
	{
		remove(key, subKey);
	}

	public boolean hasTimed(String key, String subKey)
	{
		return find(key, subKey) != null;
	}

	public Object getTimedValue(String key, String subKey)
	{
		return find(key, subKey).getData();
	}

	/**
	 * Updates all timed data.
	 */
	public void updateCachedData()
	{
		for(LegacyTimedData data : Collections2.filter(getAll(), new Predicate<LegacyTimedData>()
		{
			@Override
			public boolean apply(LegacyTimedData data)
			{
				return data.getExpiration() <= System.currentTimeMillis();
			}
		}))
			delete(data);
	}

	class CacheFile extends TieredGenericYamlFile<UUID, LegacyTimedData>
	{
		@Override
		public LegacyTimedData valueFromData(UUID uuid, ConfigurationSection conf)
		{
			return new LegacyTimedData(uuid, conf);
		}

		@Override
		public ConcurrentMap<UUID, LegacyTimedData> getLoadedData()
		{
			return cache;
		}

		@Override
		public String getDirectoryPath()
		{
			return FileDataManager.SAVE_PATH;
		}

		@Override
		public String getFullFileName()
		{
			return fileName;
		}

		@Override
		public Map<String, Object> serialize(UUID uuid)
		{
			return getLoadedData().get(uuid).serialize();
		}

		@Override
		public UUID keyFromString(String stringId)
		{
			return UUID.fromString(stringId);
		}

		@Override
		public void loadDataFromFile()
		{
			cache = getCurrentFileData();
		}
	}
}
