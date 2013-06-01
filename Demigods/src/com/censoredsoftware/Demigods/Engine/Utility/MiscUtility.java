package com.censoredsoftware.Demigods.Engine.Utility;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.Demigods;

public class MiscUtility
{
	/**
	 * Generates a random string with a length of <code>length</code>.
	 * 
	 * @param length the length of the generated string.
	 * @return String
	 */
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

	/**
	 * Generates a random integer with a length of <code>length</code>.
	 * 
	 * @param length the length of the generated integer.
	 * @return Integer
	 */
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

	/**
	 * Generates an integer with a value between <code>min</code> and <code>max</code>.
	 * 
	 * @param min the minimum value of the integer.
	 * @param max the maximum value of the integer.
	 * @return Integer
	 */
	public static int generateIntRange(int min, int max)
	{
		return new Random().nextInt(max - min + 1) + min;
	}

	/**
	 * Capitalizes the <code>input</code> and returns it.
	 * 
	 * @param input the string to capitalize.
	 * @return String
	 */
	public static String capitalize(String input)
	{
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

	/**
	 * Returns a boolean whose value is based on the given <code>percent</code>.
	 * 
	 * @param percent the percent chance for true.
	 * @return Boolean
	 */
	public static boolean randomPercentBool(double percent)
	{
		if(percent <= 0.0) return false;
		Random rand = new Random();
		int chance = rand.nextInt(Math.abs((int) Math.ceil(1.0 / (percent / 100.0))) + 1);
		return chance == 1;
	}

	/**
	 * Checks the <code>string</code> for <code>max</code> capital letters.
	 * 
	 * @param string the string to check.
	 * @param max the maximum allowed capital letters.
	 * @return Boolean
	 */
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

	/**
	 * Generates a random location with the center being <code>reference</code>.
	 * Must be at least <code>min</code> blocks from the center and no more than <code>max</code> blocks away.
	 * 
	 * @param reference the location used as the center for reference.
	 * @param min the minimum number of blocks away.
	 * @param max the maximum number of blocks away.
	 * @return the random location generated.
	 */
	public static Location randomLocation(Location reference, int min, int max)
	{
		Location location = reference.clone();

		double randX = MiscUtility.generateIntRange(min, max);
		double randZ = MiscUtility.generateIntRange(min, max);
		location.add(randX, 0, randZ);
		double highestY = location.clone().getWorld().getHighestBlockYAt(location);
		location.setY(highestY);

		Demigods.message.broadcast("Y: " + highestY); // TODO Why is this broadcasting?

		return location;
	}

	/**
	 * Returns a random location within the <code>chunk</code> passed in.
	 * 
	 * @param chunk the chunk that we will obtain the location from.
	 * @return the random location generated.
	 */
	public static Location randomChunkLocation(Chunk chunk)
	{
		Location reference = chunk.getBlock(MiscUtility.generateIntRange(1, 16), 64, MiscUtility.generateIntRange(1, 16)).getLocation();
		double locX = reference.getX();
		double locY = chunk.getWorld().getHighestBlockYAt(reference);
		double locZ = reference.getZ();
		return new Location(chunk.getWorld(), locX, locY, locZ);
	}
}
