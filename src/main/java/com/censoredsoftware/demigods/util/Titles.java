package com.censoredsoftware.demigods.util;

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
		String chatTitle = " " + Unicodes.rightwardArrow() + " " + title + " ";
		for(int i = 0; i < (total - chatTitle.length()); i++)
			chatTitle += "-";
		return chatTitle;
	}

	/**
	 * Returns a formatted title ready for the chat.
	 * 
	 * @param title the title to format
	 * @return the formatted title
	 */
	public static String chatTitleDash(String title)
	{
		int total = 43;
		String chatTitle = " " + Unicodes.rightwardArrow() + " " + title + " ";
		for(int i = 0; i < (total - chatTitle.length()); i++)
			chatTitle += "―";
		return chatTitle;
	}
}
