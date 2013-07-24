package com.censoredsoftware.Demigods.Engine.Object.Language;

// TODO Replace with YAML.

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
	 * Get a specific line of <code>text</code>.
	 * 
	 * @param text The line we want.
	 * @return The String value of the Text.
	 */
	public String getText(Enum text);
}
