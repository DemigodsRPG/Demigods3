package com.censoredsoftware.Demigods.Engine.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MiscUtility
{
	/**
	 * Clears the chat for <code>player</code> using .sendMessage().
	 * 
	 * @param player the player whose chat to clear.
	 */
	public static void clearChat(Player player)
	{
		for(int x = 0; x < 120; x++)
			player.sendMessage(" ");
	}

	/**
	 * Clears the chat for <code>player</code> using .sendRawMessage().
	 * 
	 * @param player the player whose chat to clear.
	 */
	public static void clearRawChat(Player player)
	{
		for(int x = 0; x < 120; x++)
			player.sendRawMessage(" ");
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

	public static int fibonacci(int n)
	{
		if(n == 0) return 0;
		else if(n == 1) return 1;
		else return fibonacci(n - 1) + fibonacci(n - 2);
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

	public static boolean isAboveGround(Location location)
	{
		return location.getBlockY() == location.getWorld().getHighestBlockYAt(location);
	}

	public static Location getAboveGround(Location location)
	{
		return new Location(location.getWorld(), location.getX(), (double) location.getWorld().getHighestBlockYAt(location), location.getZ());
	}

	public static Location getFloorBelowLocation(Location location)
	{
		if(location.getBlock().getType().isSolid()) return location;
		return getFloorBelowLocation(location.getBlock().getRelative(BlockFace.DOWN).getLocation());
	}

	public static List<Location> getCirclePoints(Location center, final double radius, final int points)
	{
		final World world = center.getWorld();
		final double X = center.getX();
		final double Y = center.getY();
		final double Z = center.getZ();
		return new ArrayList<Location>()
		{
			{
				for(int i = 0; i < points; i++)
				{
					double x = X + radius * Math.cos((2 * Math.PI * i) / points);
					double z = Z + radius * Math.sin((2 * Math.PI * i) / points);
					add(new Location(world, x, Y, z));
				}
			}
		};
	}

	public static float toDegree(double angle)
	{
		return (float) Math.toDegrees(angle);
	}

	public static Vector getVector(Entity entity)
	{
		if(entity instanceof Player) return ((Player) entity).getEyeLocation().toVector();
		return entity.getLocation().toVector();
	}
}
