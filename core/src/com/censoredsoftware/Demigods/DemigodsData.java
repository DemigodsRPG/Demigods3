package com.censoredsoftware.Demigods;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.censoredsoftware.Demigods.API.BlockAPI;
import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.API.LocationAPI;
import com.censoredsoftware.Demigods.Block.Altar;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterAbilities;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterBindings;
import com.censoredsoftware.Demigods.Tracked.TrackedBlock;
import com.censoredsoftware.Demigods.Tracked.TrackedLocation;
import com.censoredsoftware.Modules.Data.IntegerDataModule;
import com.censoredsoftware.Modules.Data.ObjectDataModule;
import com.censoredsoftware.Modules.Data.StringDataModule;
import com.censoredsoftware.Modules.Data.TieredPlayerDataModule;
import com.censoredsoftware.Modules.Persistence.Event.LoadStubYAMLEvent;
import com.censoredsoftware.Modules.Persistence.YAMLPersistenceModule;

public class DemigodsData
{
	// Non-Persistent Public StringDataModules
	public static StringDataModule deityPaths;
	public static StringDataModule deityLoaders;
	public static StringDataModule deityTeams;
	public static StringDataModule deityColors;
	public static StringDataModule deityCommands;
	public static StringDataModule deityClaimItems;

	// Persistent Plugin Data
	public static IntegerDataModule locationData;
	public static YAMLPersistenceModule locationYAML;

	// Player Data
	public static TieredPlayerDataModule playerData;
	public static YAMLPersistenceModule playerYAML;
	public static TieredPlayerDataModule tempPlayerData;

	// Character Data
	public static IntegerDataModule characterData;
	public static IntegerDataModule characterAbilityData;
	public static IntegerDataModule characterBindingData;
	public static YAMLPersistenceModule characterYAML;
	public static IntegerDataModule tempTributeData;

	// Block Data
	public static IntegerDataModule trackedBlockData;
	public static YAMLPersistenceModule trackedBlockYAML;
	public static IntegerDataModule altarData;
	public static YAMLPersistenceModule altarYAML;
	public static IntegerDataModule shrineData;

	// Battle Data
	public static IntegerDataModule battleData;

	static ObjectDataModule timedData; // TODO Timed data...

	protected DemigodsData(DemigodsPlugin instance)
	{
		instance.getServer().getPluginManager().registerEvents(new DataListener(), instance);

		pluginDataPersistent(instance);
		pluginDataNonPersistent(instance);
		playerData(instance);
		characterData(instance);
		timedData(instance);
		battleData(instance);
		blockData(instance);

		load(instance, true);
	}

	static void pluginDataPersistent(DemigodsPlugin instance)
	{
		locationData = new IntegerDataModule(instance, "location_data");
	}

	static void pluginDataNonPersistent(DemigodsPlugin instance)
	{
		// Deity Related
		deityPaths = new StringDataModule();
		deityLoaders = new StringDataModule();
		deityTeams = new StringDataModule();
		deityColors = new StringDataModule();
		deityCommands = new StringDataModule();
		deityClaimItems = new StringDataModule();
	}

	static void timedData(DemigodsPlugin instance)
	{
		timedData = new ObjectDataModule(); // TODO Make a timed data module.
	}

	static void playerData(DemigodsPlugin instance)
	{
		playerData = new TieredPlayerDataModule(instance, "player_data");
		tempPlayerData = new TieredPlayerDataModule();
	}

	static void characterData(DemigodsPlugin instance)
	{
		characterData = new IntegerDataModule(instance, "character_data");
		characterAbilityData = new IntegerDataModule();
		characterBindingData = new IntegerDataModule();
		tempTributeData = new IntegerDataModule();
	}

	static void blockData(DemigodsPlugin instance)
	{
		trackedBlockData = new IntegerDataModule(instance, "tracked_block_data");
		altarData = new IntegerDataModule(instance, "altar_data");
		shrineData = new IntegerDataModule(instance, "shrine_data");
	}

	static void battleData(DemigodsPlugin instance)
	{
		battleData = new IntegerDataModule(instance, "battle_data");
	}

	static void load(DemigodsPlugin instance, boolean yaml)
	{
		if(yaml)
		{
			// Core
			locationYAML = new YAMLPersistenceModule(true, instance, "core", "location_data");
			playerYAML = new YAMLPersistenceModule(true, instance, "core", "player_data");
			trackedBlockYAML = new YAMLPersistenceModule(true, instance, "core", "tracked_block_data");

			// Character
			characterYAML = new YAMLPersistenceModule(true, instance, "character", "character_data");
			for(PlayerCharacter character : CharacterAPI.getAllChars())
			{
				new YAMLPersistenceModule(true, instance, "character" + File.separator + character.getID(), "ability_data");
				new YAMLPersistenceModule(true, instance, "character" + File.separator + character.getID(), "binding_data");
			}

			// Block
			altarYAML = new YAMLPersistenceModule(true, instance, "block", "altar_data");
		}
	}

	static void saveAll(boolean yaml)
	{
		if(yaml)
		{
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Demigods.demigods, new Runnable()
			{
				@Override
				public void run()
				{
					long countdown = System.currentTimeMillis();

					locationYAML.save(LocationAPI.getAllLocations());
					playerYAML.save(playerData);
					trackedBlockYAML.save(BlockAPI.getBlocks());
					saveCharacters(true);
					altarYAML.save(BlockAPI.getAllAltars());

					double seconds = (System.currentTimeMillis() - countdown) / 1000.0;

					Demigods.message.info("Data saved: Took " + seconds + " seconds.");
				}
			}, 10);
		}
	}

	static void saveCharacters(boolean yaml)
	{
		if(yaml)
		{
			characterYAML.save(CharacterAPI.getAllChars());
			for(PlayerCharacter character : CharacterAPI.getAllChars())
			{
				new YAMLPersistenceModule(false, Demigods.demigods, "character" + File.separator + character.getID(), "ability_data").save(character.getAbilities());
				new YAMLPersistenceModule(false, Demigods.demigods, "character" + File.separator + character.getID(), "binding_data").save(character.getBindings());
			}

		}
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
		int chance = rand.nextInt(Math.abs((int) Math.ceil(1 / (percent / 100))) + 1);
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
}

class DataListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLocationLoad(LoadStubYAMLEvent event)
	{
		if(!event.getPluginName().equals(Demigods.demigods.getName())) return;

		if(event.getPath().equals("core") && event.getDataName().equals("location_data"))
		{
			new TrackedLocation(event.getData());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onTrackedBlockLoad(LoadStubYAMLEvent event)
	{
		if(!event.getPluginName().equals(Demigods.demigods.getName())) return;

		if(event.getPath().equals("core") && event.getDataName().equals("tracked_block_data"))
		{
			new TrackedBlock(event.getData());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCharacterLoad(LoadStubYAMLEvent event)
	{
		if(!event.getPluginName().equals(Demigods.demigods.getName())) return;

		if(event.getPath().equals("character") && event.getDataName().equals("character_data"))
		{
			DemigodsFactory.playerCharacterFactory.create(event.getData());
		}
		else if(event.getPath().startsWith("character") && event.getDataName().equals("ability_data"))
		{
			new PlayerCharacterAbilities(event.getData());
		}
		else if(event.getPath().startsWith("character") && event.getDataName().equals("binding_data"))
		{
			new PlayerCharacterBindings(event.getData());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAltarLoad(LoadStubYAMLEvent event)
	{
		if(!event.getPluginName().equals(Demigods.demigods.getName())) return;

		if(event.getPath().equals("block") && event.getDataName().equals("altar_data"))
		{
			new Altar(event.getData());
		}
	}
}