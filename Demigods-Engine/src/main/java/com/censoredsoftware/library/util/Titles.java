package com.censoredsoftware.library.util;

import com.censoredsoftware.library.language.Symbol;

public class Titles
{
	/**
	 * Returns a formatted title ready for the chat.
	 * 
	 * @param title the title to format
	 * @return the formatted title
	 */
	public static String chatTitle(String title)
	{
		int total = 86;
		String chatTitle = " " + Symbol.RIGHTWARD_ARROW + " " + title + " ";
		for(int i = 0; i < (total - chatTitle.length()); i++)
			chatTitle += "-";
		return chatTitle;
	}
}
