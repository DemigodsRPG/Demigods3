package com.censoredsoftware.Demigods.Engine.Utility;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.johm.JOhm;

import com.censoredsoftware.Demigods.DemigodsBukkit;
import com.censoredsoftware.Demigods.Engine.Object.TimedData;
import com.google.common.collect.Maps;

public class DataUtility
{
	// The Redis DB
	private static JedisPool jedisPool;

	// Persistence
	public static JOhm jOhm;

	// Temp Data
	private static Map<String, HashMap<String, Object>> tempData;

	public DataUtility(DemigodsBukkit instance)
	{
		// Create Data Instances
		jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);
		tempData = Maps.newHashMap();

		// Create Persistence
		jOhm = new JOhm();
		JOhm.setPool(jedisPool);
	}

	public static void disconnect()
	{
		Jedis jedis = jedisPool.getResource();
		jedis.disconnect();
		jedisPool.returnBrokenResource(jedis);
		jedisPool.destroy();
	}

	public static void save()
	{
		Jedis jedis = jedisPool.getResource();
		jedis.bgsave();
		jedisPool.returnResource(jedis);
	}

	public static void flushData()
	{
		Jedis jedis = jedisPool.getResource();
		jedis.flushDB();
		jedisPool.returnResource(jedis);
	}

	public static boolean hasKeyTemp(String key, String subKey)
	{
		return tempData.containsKey(key) && tempData.get(key).containsKey(subKey);
	}

	public static Object getValueTemp(String key, String subKey)
	{
		return tempData.get(key).get(subKey);
	}

	public static void saveTemp(String key, String subKey, Object value)
	{
		if(!tempData.containsKey(key)) tempData.put(key, new HashMap<String, Object>());
		tempData.get(key).put(subKey, value);
	}

	public static void removeTemp(String key, String subKey)
	{
		if(!tempData.containsKey(key)) tempData.put(key, new HashMap<String, Object>());
		if(tempData.get(key).containsKey(subKey)) tempData.get(key).remove(subKey);
	}

	public static void saveTimed(String key, String subKey, Object data, Integer seconds)
	{
		// Remove the data if it exists already
		TimedData.remove(key, subKey);

		// Create and save the timed data
		TimedData timedData = new TimedData();
		timedData.setKey(key);
		timedData.setSubKey(subKey);
		timedData.setData(data.toString());
		timedData.setSeconds(seconds);
		TimedData.save(timedData);
	}

	public static void removeTimed(String key, String subKey)
	{
		TimedData.remove(key, subKey);
	}

	public static boolean hasTimed(String key, String subKey)
	{
		return TimedData.find(key, subKey) != null;
	}

	public static Object getTimedValue(String key, String subKey)
	{
		return TimedData.find(key, subKey).getData();
	}
}
