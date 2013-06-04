package com.censoredsoftware.Demigods.Engine.Utility;

import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.google.common.collect.Sets;

public class MiscUtility
{
	/**
	 * Clears the chat for <code>player</code>.
	 * 
	 * @param player the player whose chat to clear.
	 */
	public static void clearChat(Player player)
	{
		for(int x = 0; x < 120; x++)
			player.sendMessage(" ");
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
		Random random = new Random();
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
		Random random = new Random();
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
		return new Random().nextInt(max - min + 1) + min;
	}

	/**
	 * Capitalizes the <code>input</code> and returns it.
	 * 
	 * @param input the string to capitalize.
	 * @return String
	 */
	public static String capitalize(String input)
	{
		return input.substring(0, 1).toUpperCase() + input.substring(1);
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
		Random rand = new Random();
		int chance = rand.nextInt(Math.abs((int) Math.ceil(1.0 / (percent / 100.0))) + 1);
		return chance == 1;
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
	 * Check to see if an input string is an integer.
	 * 
	 * @param string The input string.
	 * @return True if the string is an integer.
	 */
	public static boolean isInt(String string)
	{
		try
		{
			Integer.parseInt(string);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * Check to see if an input string is a double.
	 * 
	 * @param string The input string.
	 * @return True if the string is a double.
	 */
	public static boolean isDouble(String string)
	{
		try
		{
			Double.parseDouble(string);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * Check to see if an input string is a long.
	 * 
	 * @param string The input string.
	 * @return True if the string is a long.
	 */
	public static boolean isLong(String string)
	{
		try
		{
			Long.parseLong(string);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * Check to see if an input string is a float.
	 * 
	 * @param string The input string.
	 * @return True if the string is a float.
	 */
	public static boolean isFloat(String string)
	{
		try
		{
			Float.parseFloat(string);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * Check to see if an input string is a boolean.
	 * 
	 * @param string The input string.
	 * @return True if the string is a boolean.
	 */
	public static boolean isBoolean(String string)
	{
		try
		{
			Boolean.parseBoolean(string);
			return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false");
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * Generates a random location with the center being <code>reference</code>.
	 * Must be at least <code>min</code> blocks from the center and no more than <code>max</code> blocks away.
	 * 
	 * @param reference the location used as the center for reference.
	 * @param min the minimum number of blocks away.
	 * @param max the maximum number of blocks away.
	 * @return the random location generated.
	 */
	public static Location randomLocation(Location reference, int min, int max)
	{
		Location location = reference.clone();
		double randX = MiscUtility.generateIntRange(min, max);
		double randZ = MiscUtility.generateIntRange(min, max);
		location.add(randX, 0, randZ);
		double highestY = location.clone().getWorld().getHighestBlockYAt(location);
		location.setY(highestY);
		return location;
	}

	/**
	 * Returns a random location within the <code>chunk</code> passed in.
	 * 
	 * @param chunk the chunk that we will obtain the location from.
	 * @return the random location generated.
	 */
	public static Location randomChunkLocation(Chunk chunk)
	{
		Location reference = chunk.getBlock(MiscUtility.generateIntRange(1, 16), 64, MiscUtility.generateIntRange(1, 16)).getLocation();
		double locX = reference.getX();
		double locY = chunk.getWorld().getHighestBlockYAt(reference);
		double locZ = reference.getZ();
		return new Location(chunk.getWorld(), locX, locY, locZ);
	}

	/**
	 * Strictly checks the <code>reference</code> location to validate if the area is safe
	 * for automated generation.
	 * 
	 * @param reference the location to be checked
	 * @param area how big of an area (in blocks) to validate
	 * @return Boolean
	 */
	public static boolean canGenerateStrict(Location reference, int area)
	{
		Location location = reference.clone();
		location.subtract(0, 1, 0);
		location.add((area / 3), 0, (area / 2));

		// Check ground
		for(int i = 0; i < area; i++)
		{
			if(!location.getBlock().getType().isSolid()) return false;
			location.subtract(1, 0, 0);
		}

		// Check ground adjacent
		for(int i = 0; i < area; i++)
		{
			if(!location.getBlock().getType().isSolid()) return false;
			location.subtract(0, 0, 1);
		}

		// Check ground adjacent again
		for(int i = 0; i < area; i++)
		{
			if(!location.getBlock().getType().isSolid()) return false;
			location.add(1, 0, 0);
		}

		location.add(0, 1, 0);

		// Check air diagonally
		for(int i = 0; i < area + 1; i++)
		{
			if(!location.getBlock().getType().isTransparent()) return false;
			location.add(0, 1, 1);
			location.subtract(1, 0, 0);
		}

		return true;
	}

	/**
	 * Generates a mound at the given <code>location</code> with a top radius of <code>radius</code>based on the predominant material found. Used in situations
	 * such as generating structures on hills.
	 * 
	 * @param reference the center location to generate at.
	 * @param radius the radii that the top of the mound will be.
	 */
	public static void generateMound(Location reference, int radius)
	{
		Location location = reference.clone();

		// Get the blocks in the area
		// TODO
	}

	/**
	 * Returns a set of blocks in a radius of <code>radius</code> at the provided <code>location</code>.
	 * 
	 * @param location the center location to get the blocks from.
	 * @param radius the radius around the center block from which to get the blocks.
	 * @return Set<Block>
	 */
	public static Set<Block> getBlocks(Location location, int radius)
	{
		Set<Block> blocks = Sets.newHashSet();

		blocks.add(location.getBlock());

		for(int i = 0; i <= radius; i++)
		{
			blocks.add(location.getBlock().getRelative(i, 0, i));
			blocks.add(location.getBlock().getRelative(-1 * i, 0, i));
			blocks.add(location.getBlock().getRelative(i, 0, -1 * i));
			blocks.add(location.getBlock().getRelative(-1 * i, 0, -1 * i));
		}

		return blocks;
	}
}
