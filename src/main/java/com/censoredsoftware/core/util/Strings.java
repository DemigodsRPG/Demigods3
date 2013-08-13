package com.censoredsoftware.core.util;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

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
}
