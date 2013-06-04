package com.censoredsoftware.Demigods.Engine.Utility;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MiscUtility
{
	/**
	 * Clears the chat for <code>player</code>.
	 * 
	 * @param player the player whose chat to clear.
	 */
	public static void clearChat(Player player)
	{
		for(int x = 0; x < 120; x++)
			player.sendMessage(" ");
	}

	/**
	 * Returns a color (red, yellow, green) based on the <code>value</code> and <code>max</code> passed in.
	 * 
	 * @param value the actual value.
	 * @param max the maximum value possible.
	 * @return ChatColor
	 */
	public static ChatColor getColor(double value, double max)
	{
		ChatColor color = ChatColor.RESET;
		if(value < Math.ceil(0.33 * max)) color = ChatColor.RED;
		else if(value < Math.ceil(0.66 * max) && value > Math.ceil(0.33 * max)) color = ChatColor.YELLOW;
		if(value > Math.ceil(0.66 * max)) color = ChatColor.GREEN;
		return color;
	}

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
}
