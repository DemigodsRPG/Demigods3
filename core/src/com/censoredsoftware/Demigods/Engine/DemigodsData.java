package com.censoredsoftware.Demigods.Engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
		jedisPool.getResource().disconnect();
	}

	public static void save()
	{
		jedisPool.getResource().bgsave();
	}

	public static boolean hasKeyTemp(String key, String key_)
	{
		return tempData.containsKey(key) && tempData.get(key).containsKey(key_);
	}

	public static boolean hasValueTemp(String key, Object value)
	{
		return tempData.containsKey(key) && tempData.get(key).containsValue(value);
	}

	public static Object getValueTemp(String key, String key_)
	{
		return tempData.get(key).get(key_);
	}

	public static void setTemp(String key, String key_, Object value)
	{
		if(!tempData.containsKey(key)) tempData.put(key, new HashMap<String, Object>());
		tempData.get(key).put(key_, value);
	}

	public static void removeTemp(String key, String key_)
	{
		if(!tempData.containsKey(key)) tempData.put(key, new HashMap<String, Object>());
		if(tempData.get(key).containsKey(key_)) tempData.get(key).remove(key_);
	}

	// TODO Find a place for these:

	public static String generateString(int length)
	{
		// Set allowed characters - Create new string to fill - Generate the string - Return string
		char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for(int i = 0; i < length; i++)
		{
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return sb.toString();
	}

	public static int generateInt(int length)
	{
		// Set allowed characters - Create new string to fill - Generate the string - Return string
		char[] chars = "0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for(int i = 0; i < length; i++)
		{
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return Integer.parseInt(sb.toString());
	}

	public static int generateIntRange(int min, int max)
	{
		return new Random().nextInt(max - min + 1) + min;
	}

	public static String capitalize(String input)
	{
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

	public static boolean randomPercentBool(double percent)
	{
		if(percent <= 0.0) return false;
		Random rand = new Random();
		int chance = rand.nextInt(Math.abs((int) Math.ceil(1.0 / (percent / 100.0))) + 1);
		if(chance == 1) return true;
		return false;
	}

	public static boolean hasCapitalLetters(String string, int max)
	{
		// Define variables
		String allCaps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int count = 0;
		char[] characters = string.toCharArray();
		for(char character : characters)
		{
			if(allCaps.contains("" + character))
			{
				count++;
			}

			if(count > max) return true;
		}
		return false;
	}

	/**
	 * Check to see if an input string is an integer.
	 * 
	 * @param string The input string.
	 * @return True if the string is an integer.
	 */
	public static boolean isInt(String string)
	{
		try
		{
			Integer.parseInt(string);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * Check to see if an input string is a double.
	 * 
	 * @param string The input string.
	 * @return True if the string is a double.
	 */
	public static boolean isDouble(String string)
	{
		try
		{
			Double.parseDouble(string);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * Check to see if an input string is a long.
	 * 
	 * @param string The input string.
	 * @return True if the string is a long.
	 */
	public static boolean isLong(String string)
	{
		try
		{
			Long.parseLong(string);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * Check to see if an input string is a float.
	 * 
	 * @param string The input string.
	 * @return True if the string is a float.
	 */
	public static boolean isFloat(String string)
	{
		try
		{
			Float.parseFloat(string);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * Check to see if an input string is a boolean.
	 * 
	 * @param string The input string.
	 * @return True if the string is a boolean.
	 */
	public static boolean isBoolean(String string)
	{
		try
		{
			Boolean.parseBoolean(string);
			return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false");
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
