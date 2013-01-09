package com.legit2.Demigods;

public class DObjUtils
{
	/*
	 *  capitalize() : Capitalizes (String)string and returns it.
	 */
	public static String capitalize(String input)
	{
		String output = input.substring(0, 1).toUpperCase() + input.substring(1);
		return output;
	}
}
