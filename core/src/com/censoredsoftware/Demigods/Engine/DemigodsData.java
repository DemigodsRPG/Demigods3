package com.censoredsoftware.Demigods.Engine;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.johm.JOhm;

public class DemigodsData
{
	// The Redis DB
	private static JedisPool jedisPool;

	// Persistence
	public static JOhm jOhm;

	// Temp Data
	private static Map<String, HashMap<String, Object>> tempData;
	private static Map<Long, HashMap<String, Object>> timedData;

	protected DemigodsData(DemigodsPlugin instance)
	{
		// Create Data Instances
		jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);
		tempData = new HashMap<String, HashMap<String, Object>>();

		// Create Persistence
		jOhm = new JOhm();
		jOhm.setPool(jedisPool);
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

	public static boolean hasKeyTemp(String key, String key_)
	{
		return tempData.containsKey(key) && tempData.get(key).containsKey(key_);
	}

	public static Object getValueTemp(String key, String key_)
	{
		return tempData.get(key).get(key_);
	}

	public static void saveTemp(String key, String key_, Object value)
	{
		if(!tempData.containsKey(key)) tempData.put(key, new HashMap<String, Object>());
		tempData.get(key).put(key_, value);
	}

	public static void removeTemp(String key, String key_)
	{
		if(!tempData.containsKey(key)) tempData.put(key, new HashMap<String, Object>());
		if(tempData.get(key).containsKey(key_)) tempData.get(key).remove(key_);
	}

	public static void saveTimed(String key, Object value, Integer seconds)
	{
		// Convert to time
		long time = System.currentTimeMillis() + (seconds * 1000);
		// TODO
	}

	public static Object getTimedValue()
	{
		// TODO
		return null;
	}
}
