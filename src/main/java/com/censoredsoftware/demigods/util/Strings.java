package com.censoredsoftware.demigods.util;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

public class Strings
{
	/**
	 * Returns true if the <code>string</code> starts with a vowel.
	 * 
	 * @param string the string to check.
	 */
	public static boolean beginsWithVowel(String string)
	{
		String[] vowels = { "a", "e", "i", "o", "u" };
		return StringUtils.startsWithAny(string, vowels);
	}

	/**
	 * Returns true if the <code>string</code> starts with a consonant.
	 * 
	 * @param string the string to check.
	 */
	public static boolean beginsWithConsonant(String string)
	{
		return !beginsWithVowel(string);
	}

	/**
	 * Returns true if the <code>string</code> contains any of the strings held in the <code>collection</code>.
	 * 
	 * @param string the string to check.
	 * @param collection the collection given.
	 */
	public static boolean containsAnyInCollection(String string, Collection<String> collection)
	{
		for(String check : collection)
			if(StringUtils.containsIgnoreCase(string, check)) return true;
		return false;
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
}
