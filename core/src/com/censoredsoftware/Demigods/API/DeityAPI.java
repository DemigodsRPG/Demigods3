package com.censoredsoftware.Demigods.API;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.DemigodsPlugin;

// TODO Not sure if this file will remain an API or not.  It's probably too powerful to be considered an API.
// TODO If need be, parts of it will be moved into the CommandListener as a subclass (for invoking deity commands).
// TODO Other parts of it can be moved to the Demigods.java file as a subclass.

public class DeityAPI
{
	/*
	 * getDeityPath() : Returns the string of the (String)deity's classpath.
	 */
	public static String getDeityPath(String deity)
	{
		return DemigodsData.deityPaths.getDataString(deity);
	}

	/*
	 * getClassLoader() : Returns the ClassLoader for the deity.
	 */
	public static ClassLoader getClassLoader(String deityPath)
	{
		if(DemigodsData.deityLoaders.containsKey(deityPath)) return (ClassLoader) DemigodsData.deityLoaders.getDataObject(deityPath);
		else return DemigodsPlugin.class.getClassLoader();
	}

	/*
	 * invokeDeityMethod() : Invokes a method (with no paramaters) from inside a deity class.
	 */
	@SuppressWarnings("rawtypes")
	public static Object invokeDeityMethod(String deityClass, ClassLoader loader, String method) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		// No Paramaters
		Class noparams[] = {};

		// Creates a new instance of the deity class
		Object obj = Class.forName(deityClass, true, loader).newInstance();

		// Load everything else for the Deity (Listener, etc.)
		Method toInvoke = Class.forName(deityClass, true, loader).getMethod(method, noparams);

		return toInvoke.invoke(obj, (Object[]) null);
	}

	/*
	 * invokeDeityMethodWithString() : Invokes a method, with a String, from inside a deity class.
	 */
	public static Object invokeDeityMethodWithString(String deityClass, ClassLoader loader, String method, String paramater) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		// Creates a new instance of the deity class
		Object obj = Class.forName(deityClass, true, loader).newInstance();

		// Load everything else for the Deity (Listener, etc.)
		Method toInvoke = Class.forName(deityClass, true, loader).getMethod(method, String.class);

		return toInvoke.invoke(obj, paramater);
	}

	/*
	 * invokeDeityMethodWithStringArray() : Invokes a method, with an ArrayList, from inside a deity class.
	 */
	public static Object invokeDeityMethodWithStringArray(String deityClass, ClassLoader loader, String method, Player player, String[] paramater) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		// Creates a new instance of the deity class
		Object obj = Class.forName(deityClass, true, loader).newInstance();

		// Load everything else for the Deity (Listener, etc.)
		Method toInvoke = Class.forName(deityClass, true, loader).getMethod(method, Player.class, String[].class);

		return toInvoke.invoke(obj, player, paramater);
	}

	/*
	 * invokeDeityMethodWithPlayer() : Invokes a method, with a Player, from inside a deity class.
	 */
	public static Object invokeDeityMethodWithPlayer(String deityClass, ClassLoader loader, String method, Player paramater) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		// Creates a new instance of the deity class
		Object obj = Class.forName(deityClass, true, loader).newInstance();

		// Load everything else for the Deity (Listener, etc.)
		Method toInvoke = Class.forName(deityClass, true, loader).getMethod(method, Player.class);

		return toInvoke.invoke(obj, paramater);
	}

	/*
	 * invokeDeityCommand : Invokes a deity command.
	 */
	@SuppressWarnings("unchecked")
	public static boolean invokeDeityCommand(Player player, String[] args) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		String deity = null;
		String command = args[0];

		for(String knownDeity : getLoadedDeityNames())
		{
			try
			{
				if(((ArrayList<String>) DemigodsData.deityCommands.getDataObject(knownDeity)).contains(command.toLowerCase()))
				{
					deity = knownDeity;
					break;
				}
			}
			catch(Exception ignored)
			{}
		}
		if(deity == null) return false;

		String deityClass = getDeityPath(deity);
		ClassLoader loader = getClassLoader(deity);

		invokeDeityMethodWithStringArray(deityClass, loader, command + "Command", player, args);
		return true;
	}

	/*
	 * getLoadedDeityNames() : Returns a ArrayList<String> of all the loaded deities' names.
	 */
	public static ArrayList<String> getLoadedDeityNames()
	{
		ArrayList<String> toReturn = new ArrayList<String>();

		for(String deity : DemigodsData.deityTeams.listKeys())
		{
			toReturn.add(deity);
		}

		return toReturn;
	}

	/*
	 * getLoadedDeityAlliances() : Returns a ArrayList<String> of all the loaded deities' alliances.
	 */
	public static ArrayList<String> getLoadedDeityAlliances()
	{
		ArrayList<String> toReturn = new ArrayList<String>();
		for(String knownDeity : getLoadedDeityNames())
		{
			String alliance = getDeityAlliance(knownDeity);
			if(toReturn.contains(alliance)) continue;
			toReturn.add(alliance);
		}
		return toReturn;
	}

	/*
	 * getDeityAlliance() : Returns a String of a loaded (String)deity's alliance.
	 */
	public static String getDeityAlliance(String deity)
	{
		return DemigodsData.deityTeams.getDataString(deity);
	}

	/*
	 * getDeityColor() : Returns a ChatColor of a loaded (String)deity.
	 */
	public static ChatColor getDeityColor(String deity)
	{
		return (ChatColor) DemigodsData.deityColors.getDataObject(deity);
	}

	/*
	 * getDeityClaimItems() : Returns an ArrayList<Material> of a loaded (String)deity's claim items.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Material> getDeityClaimItems(String deity)
	{
		return (ArrayList<Material>) DemigodsData.deityClaimItems.getDataObject(deity);
	}

	/*
	 * getAllDeitiesInAlliance() : Returns a ArrayList<String> of all the loaded deities' names.
	 */
	public static ArrayList<String> getAllDeitiesInAlliance(String alliance)
	{
		ArrayList<String> toReturn = new ArrayList<String>();
		for(String deity : getLoadedDeityNames())
		{
			if(!(getDeityAlliance(deity)).equalsIgnoreCase(alliance)) continue;
			toReturn.add(deity);
		}
		return toReturn;
	}

	/*
	 * getAllDeityCommands() : Returns a ArrayList<String> of all the loaded deities' commands.
	 */
	public static ArrayList<String> getAllDeityCommands()
	{
		ArrayList<String> toReturn = new ArrayList<String>();
		for(String knownDeity : getLoadedDeityNames())
		{
			toReturn.add(DemigodsData.deityCommands.getDataString(knownDeity));
		}
		return toReturn;
	}
}
