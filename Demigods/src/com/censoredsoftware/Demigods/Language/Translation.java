package com.censoredsoftware.Demigods.Language;

import java.util.Set;

public interface Translation
{
	/**
	 * The version of this language text.
	 * 
	 * @return Integer version.
	 */
	public int version();

	/**
	 * The translator to credit.
	 * 
	 * @return Name of translator.
	 */
	public String translator();

	/**
	 * The set of Episodes supported by this text.
	 * 
	 * @return Set of Episodes.
	 */
	public Set<Episode> episodes();

	/**
	 * Get a specific line of <code>text</code>.
	 * 
	 * @param text The line we want.
	 * @return The String value of the Text.
	 */
	public String getText(Episode episode, Text text);

	public static enum Episode
	{
		ENGINE, DEMO;
	}

	public static enum Text
	{
		TEXT;
	}
}
