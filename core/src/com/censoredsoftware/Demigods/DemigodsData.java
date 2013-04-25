package com.censoredsoftware.Demigods;

import java.util.Random;

import com.censoredsoftware.Modules.DataPersistence.IntegerDataModule;
import com.censoredsoftware.Modules.DataPersistence.ObjectDataModule;
import com.censoredsoftware.Modules.DataPersistence.StringDataModule;
import com.censoredsoftware.Modules.DataPersistence.TieredPlayerDataModule;

// TODO A lot of this file will become obsolete as I decentralize the data.

public class DemigodsData
{
	// private static final HashMap<Object, HashMap<String, HashMap<String, Object>>> timedData = new HashMap<Object, HashMap<String, HashMap<String, Object>>>();
	// private static final HashMap<String, HashMap<String, Object>> playerData = new HashMap<String, HashMap<String, Object>>();
	// private static final HashMap<Integer, HashMap<String, Object>> charData = new HashMap<Integer, HashMap<String, Object>>();
	// private static final HashMap<Integer, HashMap<String, Object>> battleData = new HashMap<Integer, HashMap<String, Object>>();
	// private static final HashMap<String, HashMap<Integer, Object>> blockData = new HashMap<String, HashMap<Integer, Object>>();

	// Non-Persistent Public StringDataModules
	public static StringDataModule deityPaths;
	public static StringDataModule deityLoaders;
	public static StringDataModule deityTeams;
	public static StringDataModule deityColors;
	public static StringDataModule deityCommands;
	public static StringDataModule deityClaimItems;

	// Player Data
	public static TieredPlayerDataModule playerData;
	public static TieredPlayerDataModule tempPlayerData;

	// Character Data
	public static IntegerDataModule characterData;

	// TODO These.
	static ObjectDataModule timedData;
	static IntegerDataModule battleData;
	static StringDataModule blockData;

	// TODO These.

	DemigodsData(DemigodsPlugin instance)
	{
		pluginDataPersistent(instance);
		pluginDataNonPersistent(instance);
		playerData(instance);
		characterData(instance);
		timedData(instance);
		battleData(instance);
		blockData(instance);
	}

	static void pluginDataPersistent(DemigodsPlugin instance)
	{

	}

	static void pluginDataNonPersistent(DemigodsPlugin instance)
	{
		// Deity Related
		deityPaths = new StringDataModule(instance, "deity_paths");
		deityLoaders = new StringDataModule(instance, "deity_loaders");
		deityTeams = new StringDataModule(instance, "deity_teams");
		deityColors = new StringDataModule(instance, "deity_colors");
		deityCommands = new StringDataModule(instance, "deity_commands");
		deityClaimItems = new StringDataModule(instance, "deity_claim_items");
	}

	static void playerData(DemigodsPlugin instance)
	{
		playerData = new TieredPlayerDataModule(instance, "player_data");
		tempPlayerData = new TieredPlayerDataModule(instance, "temp_player_data");
	}

	static void timedData(DemigodsPlugin instance)
	{
		timedData = new ObjectDataModule(instance, "timed_data");
	}

	static void characterData(DemigodsPlugin instance)
	{
		characterData = new IntegerDataModule(instance, "character_data");
	}

	static void battleData(DemigodsPlugin instance)
	{
		battleData = new IntegerDataModule(instance, "battle_data");
	}

	static void blockData(DemigodsPlugin instance)
	{
		blockData = new StringDataModule(instance, "block_data");
	}

	// Internal Utility Methods

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
}
