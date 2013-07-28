package com.censoredsoftware.why.engine.language;

// TODO Replace with YAML.

import java.util.ArrayList;

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
	 * Gets a specific line of <code>text</code>.
	 * 
	 * @param text The line we want.
	 * @return The String value of the Text.
	 */
	public String getText(Enum text);

	/**
	 * Gets an ArrayList for the given <code>text</code>.
	 * 
	 * @param text The block of text we want.
	 * @return The ArrayList for the block of text.
	 */
	public ArrayList<String> getTextBlock(Enum text);
}
