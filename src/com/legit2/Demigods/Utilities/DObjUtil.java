package com.legit2.Demigods.Utilities;

import java.util.Random;

public class DObjUtil
{
	/*
	 *  capitalize() : Capitalizes (String)string and returns it.
	 */
	public static String capitalize(String input)
	{
		String output = input.substring(0, 1).toUpperCase() + input.substring(1);
		return output;
	}
	
	/*
	 *  toInteger() : Returns an object as an integer.
	 */
	public static int toInteger(Object object)
	{
		return Integer.parseInt(object.toString());
	}
	
	/*
	 *  toLong() : Returns an object as a long.
	 */
	public static long toLong(Object object)
	{
		return Long.parseLong(object.toString());
	}
	
	/*
	 *  toBoolean() : Returns an object as a boolean.
	 */
	public static boolean toBoolean(Object object)
	{
		if(object instanceof Boolean)
		{
			return (Boolean) object;
		}
		else if(object instanceof Integer)
		{
			if((Integer) object == 1) return true;
			else if((Integer) object == 0) return false;
		}
		return Boolean.parseBoolean(object.toString());
	}
	
	/*
	 *  generateString : Generates a random string of characters with a length of (int)length.
	 */
	public static String generateString(int length)
	{
		// Set allowed characters - Create new string to fill - Generate the string - Return string
		char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i < length; i++) {
	        char c = chars[random.nextInt(chars.length)];
	        sb.append(c);
	    }
	    String output = sb.toString();
	    return new String(output);
	}
	
	/*
	 *  generateInt : Generates a random integer  with a length of (int)length.
	 */
	public static Integer generateInt(int length)
	{
		// Set allowed characters - Create new string to fill - Generate the string - Return string
		char[] chars = "0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i < length; i++) {
	        char c = chars[random.nextInt(chars.length)];
	        sb.append(c);
	    }
	    int output = toInteger(sb.toString());
	    return new Integer(output);
	}
}
