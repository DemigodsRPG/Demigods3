package com.censoredsoftware.Demigods;

import java.util.Random;

import com.censoredsoftware.Modules.DataPersistence.IntegerDataModule;
import com.censoredsoftware.Modules.DataPersistence.ObjectDataModule;
import com.censoredsoftware.Modules.DataPersistence.StringDataModule;
import com.censoredsoftware.Modules.DataPersistence.TieredPlayerDataModule;

public class DemigodsData
{
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
	public static IntegerDataModule warpData;
	public static IntegerDataModule inviteData;
	public static IntegerDataModule tempTributeData;

	// Block Data
	public static IntegerDataModule altarData;
	public static IntegerDataModule shrineData;

	// Battle Data
	public static IntegerDataModule battleData;

	static ObjectDataModule timedData; // TODO Timed data...

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
		// TODO
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

	static void timedData(DemigodsPlugin instance)
	{
		timedData = new ObjectDataModule(instance, "timed_data");
	}

	static void playerData(DemigodsPlugin instance)
	{
		playerData = new TieredPlayerDataModule(instance, "player_data");
		tempPlayerData = new TieredPlayerDataModule(instance, "temp_player_data");
	}

	static void characterData(DemigodsPlugin instance)
	{
		characterData = new IntegerDataModule(instance, "character_data");
		warpData = new IntegerDataModule(instance, "warp_data");
		inviteData = new IntegerDataModule(instance, "invite_data");
		tempTributeData = new IntegerDataModule(instance, "temp_tribute_data");
	}

	static void blockData(DemigodsPlugin instance)
	{
		altarData = new IntegerDataModule(instance, "altar_data");
		shrineData = new IntegerDataModule(instance, "shrine_data");
	}

	static void battleData(DemigodsPlugin instance)
	{
		battleData = new IntegerDataModule(instance, "battle_data");
	}

	// TODO Find a place for these:

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

	public static int generateIntRange(int min, int max)
	{
		return new Random().nextInt(max - min + 1) + min;
	}

	public static String capitalize(String input)
	{
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

	public static boolean randomPercentBool(double percent)
	{
		Random rand = new Random();
		int chance = rand.nextInt((int) Math.ceil(1 / (percent / 100))) + 1;
		if(chance == 1) return true;
		return false;
	}

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
}
