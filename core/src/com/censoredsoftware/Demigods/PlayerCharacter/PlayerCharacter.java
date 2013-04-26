package com.censoredsoftware.Demigods.PlayerCharacter;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.censoredsoftware.Demigods.Tracked.TrackedLocation;

public class PlayerCharacter
{
	// Define HashMaps
	private Map<String, Object> characterData;

	private PlayerCharacterInventory playerCharacterInventory;
	private TrackedLocation specialLocation;

	public PlayerCharacter(OfflinePlayer player, int charID, String charName, boolean charActive)
	{
		characterData = new HashMap<String, Object>();

		saveData("PLAYER_NAME", player.getName());
		saveData("CHAR_NAME", charName);
		saveData("CHAR_ID", charID);
		saveData("CHAR_LEVEL", 0);
		saveData("CHAR_FOOD_LEVEL", 20);
		saveData("CHAR_HEALTH", 20);
		saveData("CHAR_EXP", 0);
		saveData("CHAR_ACTIVE", charActive);
		this.playerCharacterInventory = null;
		try
		{
			this.specialLocation = new TrackedLocation(player.getPlayer().getLocation(), null);
		}
		catch(Exception ignored)
		{}
	}

	/**
	 * Checks if the characterData Map contains <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return True if characterData contains the key.
	 */
	boolean containsKey(String key)
	{
		return characterData.get(key) != null && characterData.containsKey(key);
	}

	/**
	 * Retrieve the Object data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Object data.
	 */
	Object getData(String key)
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
	void saveData(String key, Object data)
	{
		characterData.put(key, data);
	}

	/**
	 * Remove the data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 */
	void removeData(String key)
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
		this.specialLocation = new TrackedLocation(location, null);
	}

	public void toggleActive(boolean option)
	{
		saveData("CHAR_ACTIVE", option);
	}

	public int getID()
	{
		return Integer.parseInt(getData("CHAR_ID").toString());
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
		return this.specialLocation;
	}

	public int getHealth()
	{
		return Integer.parseInt(getData("CHAR_HEALTH").toString());
	}

	public int getFoodLevel()
	{
		return Integer.parseInt(getData("CHAR_FOOD_LEVEL").toString());
	}

	public int getLevel()
	{
		return Integer.parseInt(getData("CHAR_LEVEL").toString());
	}

	public float getExp()
	{
		return Float.parseFloat(getData("CHAR_EXP").toString());
	}

	/**
	 * Grab the Map in it's entirely.
	 */
	protected Map<String, Object> grabMap()
	{
		return this.characterData;
	}

	protected void overrideMap(Map map)
	{
		try
		{
			this.characterData = map;
		}
		catch(Exception ignored)
		{}
	}
}
