package com.censoredsoftware.demigods.data;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.battle.Battle;
import com.censoredsoftware.demigods.item.DItemStack;
import com.censoredsoftware.demigods.language.Translation;
import com.censoredsoftware.demigods.location.DLocation;
import com.censoredsoftware.demigods.player.*;
import com.censoredsoftware.demigods.structure.Structure;
import com.google.common.collect.Maps;

public class DataManager
{
	// Files
	private static DPlayer.File playersYAML;
	private static DItemStack.File itemStacksYAML;
	private static DLocation.File locationsYAML;
	private static Ability.Devotion.File devotionYAML;
	private static DCharacter.Meta.File characterMetasYAML;
	private static DCharacter.Inventory.File inventoriesYAML;
	private static DCharacter.SavedPotion.File savedPotionsYAML;
	private static DCharacter.File charactersYAML;
	private static Notification.File notificationsYAML;
	private static Pet.File petsYAML;
	private static Structure.Save.File structuresYAML;
	private static Battle.File battlesYAML;
	private static Death.File deathsYAML;
	private static TimedData.File timedDataYAML;

	// Data
	public static ConcurrentMap<String, DPlayer> players;
	public static ConcurrentMap<UUID, DItemStack> itemStacks;
	public static ConcurrentMap<UUID, DLocation> locations;
	public static ConcurrentMap<UUID, Ability.Devotion> devotion;
	public static ConcurrentMap<UUID, DCharacter> characters;
	public static ConcurrentMap<UUID, Notification> notifications;
	public static ConcurrentMap<UUID, Pet> pets;
	public static ConcurrentMap<UUID, DCharacter.Meta> characterMetas;
	public static ConcurrentMap<UUID, DCharacter.Inventory> inventories;
	public static ConcurrentMap<UUID, DCharacter.SavedPotion> savedPotions;
	public static ConcurrentMap<UUID, Structure.Save> structures;
	public static ConcurrentMap<UUID, Battle> battles;
	public static ConcurrentMap<UUID, Death> deaths;
	public static ConcurrentMap<UUID, TimedData> timedData;

	private static ConcurrentMap<String, HashMap<String, Object>> tempData;

	public DataManager()
	{
		itemStacksYAML = new DItemStack.File();
		locationsYAML = new DLocation.File();
		devotionYAML = new Ability.Devotion.File();
		playersYAML = new DPlayer.File();
		charactersYAML = new DCharacter.File();
		characterMetasYAML = new DCharacter.Meta.File();
		inventoriesYAML = new DCharacter.Inventory.File();
		savedPotionsYAML = new DCharacter.SavedPotion.File();
		notificationsYAML = new Notification.File();
		petsYAML = new Pet.File();
		structuresYAML = new Structure.Save.File();
		battlesYAML = new Battle.File();
		deathsYAML = new Death.File();
		timedDataYAML = new TimedData.File();

		load();

		tempData = Maps.newConcurrentMap();
	}

	public static void load()
	{
		itemStacks = itemStacksYAML.loadFromFile();
		locations = locationsYAML.loadFromFile();
		devotion = devotionYAML.loadFromFile();
		players = playersYAML.loadFromFile();
		characters = charactersYAML.loadFromFile();
		characterMetas = characterMetasYAML.loadFromFile();
		inventories = inventoriesYAML.loadFromFile();
		notifications = notificationsYAML.loadFromFile();
		savedPotions = savedPotionsYAML.loadFromFile();
		pets = petsYAML.loadFromFile();
		structures = structuresYAML.loadFromFile();
		battles = battlesYAML.loadFromFile();
		deaths = deathsYAML.loadFromFile();
		timedData = timedDataYAML.loadFromFile();
	}

	public static void save()
	{
		itemStacksYAML.saveToFile();
		locationsYAML.saveToFile();
		devotionYAML.saveToFile();
		playersYAML.saveToFile();
		charactersYAML.saveToFile();
		characterMetasYAML.saveToFile();
		inventoriesYAML.saveToFile();
		notificationsYAML.saveToFile();
		petsYAML.saveToFile();
		structuresYAML.saveToFile();
		battlesYAML.saveToFile();
		deathsYAML.saveToFile();
		timedDataYAML.saveToFile();
	}

	public static void flushData()
	{
		// Kick everyone
		for(Player player : Bukkit.getOnlinePlayers())
			player.kickPlayer(ChatColor.GREEN + Demigods.language.getText(Translation.Text.DATA_RESET_KICK));

		// Clear the data
		itemStacks.clear();
		locations.clear();
		players.clear();
		characters.clear();
		characterMetas.clear();
		inventories.clear();
		devotion.clear();
		notifications.clear();
		pets.clear();
		structures.clear();
		battles.clear();
		deaths.clear();
		timedData.clear();

		tempData.clear();

		save();

		// Reload the plugin
		Bukkit.getServer().getPluginManager().disablePlugin(Demigods.plugin);
		Bukkit.getServer().getPluginManager().enablePlugin(Demigods.plugin);
	}

	public static boolean hasKeyTemp(String key, String subKey)
	{
		return tempData.containsKey(key) && tempData.get(key).containsKey(subKey);
	}

	public static Object getValueTemp(String key, String subKey)
	{
		if(tempData.containsKey(key)) return tempData.get(key).get(subKey);
		else return null;
	}

	public static void saveTemp(String key, String subKey, Object value)
	{
		if(!tempData.containsKey(key)) tempData.put(key, new HashMap<String, Object>());
		tempData.get(key).put(subKey, value);
	}

	public static void removeTemp(String key, String subKey)
	{
		if(tempData.containsKey(key) && tempData.get(key).containsKey(subKey)) tempData.get(key).remove(subKey);
	}

	public static void saveTimed(String key, String subKey, Object data, Integer seconds)
	{
		// Remove the data if it exists already
		TimedData.Util.remove(key, subKey);

		// Create and save the timed data
		TimedData timedData = new TimedData();
		timedData.generateId();
		timedData.setKey(key);
		timedData.setSubKey(subKey);
		timedData.setData(data.toString());
		timedData.setSeconds(seconds);
		DataManager.timedData.put(timedData.getId(), timedData);
	}

	public static void removeTimed(String key, String subKey)
	{
		TimedData.Util.remove(key, subKey);
	}

	public static boolean hasTimed(String key, String subKey)
	{
		return TimedData.Util.find(key, subKey) != null;
	}

	public static Object getTimedValue(String key, String subKey)
	{
		return TimedData.Util.find(key, subKey).getData();
	}
}
