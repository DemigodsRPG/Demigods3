package com.censoredsoftware.Demigods.PlayerCharacter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.censoredsoftware.Demigods.API.LocationAPI;
import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.Tracked.TrackedLocation;
import com.censoredsoftware.Modules.Data.DataStubModule;

public class PlayerCharacter implements DataStubModule
{
	// Define HashMaps
	private Map<String, Object> characterData;
	private PlayerCharacterInventory playerCharacterInventory; // TODO Track these in a different file somehow.
	private int globalMaxFavor;

	public PlayerCharacter(int globalMaxFavor, Map map)
	{
		this.globalMaxFavor = globalMaxFavor;
		setMap(map);
		save(this);
	}

	public PlayerCharacter(int globalMaxFavor, OfflinePlayer player, int charID, String charName, boolean charActive, String className, String teamName, int favor, int maxFavor, int devotion, int ascensions, int offense, int defense, int stealth, int support, int passive, boolean classActive)
	{
		this.globalMaxFavor = globalMaxFavor;
		characterData = new HashMap<String, Object>();

		// Vanilla Data
		saveData("PLAYER_NAME", player.getName());
		saveData("CHAR_NAME", charName);
		saveData("CHAR_ID", charID);
		saveData("CHAR_LEVEL", 0);
		saveData("CHAR_FOOD_LEVEL", 20);
		saveData("CHAR_HEALTH", 20);
		saveData("CHAR_EXP", 0);
		saveData("CHAR_ACTIVE", charActive);

		// Demigods Data
		saveData("CLASS_NAME", className);
		saveData("TEAM_NAME", teamName);
		saveData("FAVOR", favor);
		saveData("MAX_FAVOR", maxFavor);
		saveData("DEVOTION", devotion);
		saveData("ASCENSIONS", ascensions);
		saveData("OFFENSE", offense);
		saveData("DEFENSE", defense);
		saveData("STEALTH", stealth);
		saveData("SUPPORT", support);
		saveData("PASSIVE", passive);
		saveData("CLASS_ACTIVE", classActive);

		// Location Data
		saveData("WARPS", new ArrayList<Integer>());
		saveData("INVITES", new ArrayList<Integer>());

		this.playerCharacterInventory = null;
		new PlayerCharacterAbilities(charID);
		new PlayerCharacterBindings(charID);
		try
		{
			saveData("LOCATION", new TrackedLocation(DemigodsData.generateInt(5), player.getPlayer().getLocation(), null).getID());
		}
		catch(Exception ignored)
		{}

		save(this);
	}

	public static void save(PlayerCharacter character) // TODO This belongs somewhere else.
	{
		DemigodsData.characterData.saveData(character.getID(), character);
	}

	/**
	 * Checks if the characterData Map contains <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return True if characterData contains the key.
	 */
	public boolean containsKey(String key)
	{
		return characterData.get(key) != null && characterData.containsKey(key);
	}

	/**
	 * Retrieve the Object data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Object data.
	 */
	public Object getData(String key)
	{
		if(containsKey(key)) return characterData.get(key);
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Save the Object <code>data</code> for int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @param data The Object being saved.
	 */
	public void saveData(String key, Object data)
	{
		characterData.put(key, data);
	}

	/**
	 * Remove the data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 */
	public void removeData(String key)
	{
		if(!containsKey(key)) return;
		characterData.remove(key);
	}

	public ChatColor getHealthColor()
	{
		int hp = getHealth();
		int maxHP = Bukkit.getPlayer(getOwner().getName()).getMaxHealth();
		ChatColor color = ChatColor.RESET;

		// Set favor color dynamically
		if(hp < Math.ceil(0.33 * maxHP)) color = ChatColor.RED;
		else if(hp < Math.ceil(0.66 * maxHP) && hp > Math.ceil(0.33 * maxHP)) color = ChatColor.YELLOW;
		if(hp > Math.ceil(0.66 * maxHP)) color = ChatColor.GREEN;

		return color;
	}

	public void setHealth(int amount)
	{
		saveData("CHAR_HEALTH", amount);
	}

	public void saveInventory()
	{
		this.playerCharacterInventory = new PlayerCharacterInventory(getOwner().getPlayer().getInventory());
	}

	public PlayerCharacterInventory getInventory()
	{
		if(this.playerCharacterInventory != null) return this.playerCharacterInventory;
		else return null;
	}

	public PlayerCharacterAbilities getAbilities()
	{
		if(DemigodsData.characterAbilityData.containsKey(getID())) return (PlayerCharacterAbilities) DemigodsData.characterAbilityData.getDataObject(getID());
		else return new PlayerCharacterAbilities(getID());
	}

	public PlayerCharacterBindings getBindings()
	{
		if(DemigodsData.characterBindingData.containsKey(getID())) return (PlayerCharacterBindings) DemigodsData.characterBindingData.getDataObject(getID());
		else return new PlayerCharacterBindings(getID());
	}

	public void setFoodLevel(int amount)
	{
		saveData("CHAR_FOOD_LEVEL", amount);
	}

	public void setExp(float amount)
	{
		saveData("CHAR_EXP", amount);
	}

	public void setLevel(int amount)
	{
		saveData("CHAR_LEVEL", amount);
	}

	public void setLocation(Location location)
	{
		saveData("LOCATION", new TrackedLocation(DemigodsData.generateInt(5), location, null).getID());
	}

	public void toggleActive(boolean option)
	{
		saveData("CHAR_ACTIVE", option);
	}

	public OfflinePlayer getOwner()
	{
		return Bukkit.getOfflinePlayer(getData("PLAYER_NAME").toString());
	}

	public String getName()
	{
		return getData("CHAR_NAME").toString();
	}

	public boolean isActive()
	{
		return Boolean.parseBoolean(getData("CHAR_ACTIVE").toString());
	}

	public TrackedLocation getLocation()
	{
		return LocationAPI.getLocation(Integer.parseInt(getData("LOCATION").toString()));
	}

	public int getHealth()
	{
		return Integer.parseInt(getData("CHAR_HEALTH").toString());
	}

	public int getFoodLevel()
	{
		return Integer.parseInt(getData("CHAR_FOOD_LEVEL").toString());
	}

	public float getExp()
	{
		return Float.parseFloat(getData("CHAR_EXP").toString());
	}

	public void setFavor(int amount)
	{
		saveData("FAVOR", amount);
	}

	public void giveFavor(int amount)
	{
		if(getFavor() + amount > getMaxFavor()) saveData("FAVOR", getMaxFavor());
		else saveData("FAVOR", getFavor() + amount);
	}

	public void subtractFavor(int amount)
	{
		if(getFavor() - amount < 0) saveData("FAVOR", 0);
		else saveData("FAVOR", getFavor() - amount);
	}

	public void setMaxFavor(int amount)
	{
		if((amount) > this.globalMaxFavor) saveData("MAX_FAVOR", this.globalMaxFavor);
		else saveData("MAX_FAVOR", amount);
	}

	public void addMaxFavor(int amount)
	{
		if((getMaxFavor() + amount) > this.globalMaxFavor) saveData("MAX_FAVOR", this.globalMaxFavor);
		else saveData("MAX_FAVOR", getMaxFavor() + amount);
	}

	public ChatColor getFavorColor()
	{
		int favor = getFavor();
		int maxFavor = getMaxFavor();
		ChatColor color = ChatColor.RESET;

		// Set favor color dynamically
		if(favor < Math.ceil(0.33 * maxFavor)) color = ChatColor.RED;
		else if(favor < Math.ceil(0.66 * maxFavor) && favor > Math.ceil(0.33 * maxFavor)) color = ChatColor.YELLOW;
		if(favor > Math.ceil(0.66 * maxFavor)) color = ChatColor.GREEN;

		return color;
	}

	public int getDevotionGoal()
	{
		return (int) Math.ceil(500 * Math.pow(getAscensions() + 1, 2.02));
	}

	public void setDevotion(int amount)
	{
		saveData("DEVOTION", amount);
	}

	public void giveDevotion(int amount)
	{
		int devotionBefore = getDevotion();
		int devotionGoal = getDevotionGoal();
		saveData("DEVOTION", devotionBefore + amount);
		int devotionAfter = getDevotion();

		if(devotionAfter > devotionBefore && devotionAfter > devotionGoal)
		{
			// Character leveled up!

			// TODO Trigger an event here instead of doing it as part of the object,
			// TODO that way we can grab a lot more stuff from the listener without having to make everything public.

			saveData("ASCENSIONS", getAscensions() + 1);
			saveData("DEVOTION", devotionAfter - devotionGoal);
		}
	}

	public void subtractDevotion(int amount)
	{
		if(getDevotion() - amount < 0) saveData("DEVOTION", 0);
		else saveData("DEVOTION", getDevotion() - amount);
	}

	public void setAscensions(int amount)
	{
		saveData("ASCENSIONS", amount);
	}

	public void giveAscensions(int amount)
	{
		saveData("ASCENSIONS", getAscensions() + amount);
	}

	public void subtractAscensions(int amount)
	{
		if(getAscensions() - amount < 0) saveData("ASCENSIONS", 0);
		else saveData("ASCENSIONS", getAscensions() - amount);
	}

	public void setTeam(String alliance)
	{
		saveData("TEAM_NAME", alliance);
	}

	public boolean isDeity(String className)
	{
		return isDeity().equalsIgnoreCase(className);
	}

	public void toggleImmortal(boolean option)
	{
		saveData("CLASS_ACTIVE", option);
	}

	public String isDeity()
	{
		return getData("CLASS_NAME").toString();
	}

	public String getTeam()
	{
		return getData("TEAM_NAME").toString();
	}

	public boolean isImmortal()
	{
		return Boolean.parseBoolean(getData("CLASS_ACTIVE").toString());
	}

	public int getFavor()
	{
		return Integer.parseInt(getData("FAVOR").toString());
	}

	public int getMaxFavor()
	{
		return Integer.parseInt(getData("MAX_FAVOR").toString());
	}

	public int getDevotion()
	{
		return Integer.parseInt(getData("DEVOTION").toString());
	}

	public int getAscensions()
	{
		return Integer.parseInt(getData("ASCENSIONS").toString());
	}

	public int getPower(Enum ignored) // TODO Levels (Offense, Defense, Stealth, Support, Passive).
	{
		return 10;
	}

	public void addWarp(int id)
	{
		List<Integer> warps = (List<Integer>) getData("WARPS");
		warps.add(id);
		saveData("WARPS", warps);
	}

	public void removeWarp(int id)
	{
		List<Integer> warps = (List<Integer>) getData("WARPS");
		warps.remove(id);
		saveData("WARPS", warps);
	}

	public List<TrackedLocation> getWarps()
	{
		List<TrackedLocation> warps = new ArrayList<TrackedLocation>();
		for(Integer warp : (List<Integer>) getData("WARPS"))
		{
			warps.add(LocationAPI.getLocation(warp));
		}
		return warps;
	}

	public void addInvite(int id)
	{
		List<Integer> invites = (List<Integer>) getData("WARPS");
		invites.add(id);
		saveData("INVITE", invites);
	}

	public void removeInvite(int id)
	{
		List<Integer> invites = (List<Integer>) getData("INVITES");
		invites.remove(id);
		saveData("INVITES", invites);
	}

	public List<TrackedLocation> getInvites()
	{
		List<TrackedLocation> invites = new ArrayList<TrackedLocation>();
		for(Integer invite : (List<Integer>) getData("INVITES"))
		{
			invites.add(LocationAPI.getLocation(invite));
		}
		return invites;
	}

	@Override
	public int getID()
	{
		return Integer.parseInt(getData("CHAR_ID").toString());
	}

	@Override
	public Map<String, Object> getMap()
	{
		return this.characterData;
	}

	@Override
	public void setMap(Map map)
	{
		this.characterData = map;
	}
}
