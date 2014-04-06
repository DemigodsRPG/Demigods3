package com.censoredsoftware.library.util;

import java.util.Random;

public class Randoms
{
	private static final Random random = new Random();

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
		return random.nextInt(max - min + 1) + min;
	}

	/**
	 * Generates a double with a value between <code>min</code> and <code>max</code>.
	 *
	 * @param min the minimum value of the integer.
	 * @param max the maximum value of the integer.
	 * @return Integer
	 */
	public static double generateDoubleRange(double min, double max)
	{
		return (max - min) * random.nextDouble() + min;
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
		double roll = generateDoubleRange(0.0, 100.0);
		return roll <= percent;
	}
}
