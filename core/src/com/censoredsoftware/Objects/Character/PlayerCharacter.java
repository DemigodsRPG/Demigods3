package com.censoredsoftware.Objects.Character;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Demigods.DemigodsFactory;
import com.censoredsoftware.Modules.DataPersistence.EnumDataModule;
import com.censoredsoftware.Objects.Special.SpecialLocation;

public class PlayerCharacter
{
	private EnumDataModule playerCharacterData;
	private PlayerCharacterInventory playerCharacterInventory;
	private SpecialLocation specialLocation;
	private PlayerCharacterClass playerCharacterClass;

	public PlayerCharacter(Plugin instance, OfflinePlayer player, int charID, String charName)
	{
		playerCharacterData = new EnumDataModule(instance, "character" + File.separator + charName);

		playerCharacterData.saveData(PlayerCharacterData.PLAYER_NAME, player.getName());
		playerCharacterData.saveData(PlayerCharacterData.CHAR_NAME, charName);
		playerCharacterData.saveData(PlayerCharacterData.CHAR_ID, charID);
		playerCharacterData.saveData(PlayerCharacterData.CHAR_LEVEL, 0);
		playerCharacterData.saveData(PlayerCharacterData.CHAR_FOOD_LEVEL, 20);
		playerCharacterData.saveData(PlayerCharacterData.CHAR_HEALTH, 20);
		playerCharacterData.saveData(PlayerCharacterData.CHAR_EXP, 0);
		playerCharacterData.saveData(PlayerCharacterData.CHAR_ACTIVE, true);
		this.playerCharacterInventory = null;
		try
		{
			this.specialLocation = DemigodsFactory.specialLocationFactory.create(player.getPlayer().getLocation());
		}
		catch(Exception ignored)
		{}
	}

	public ChatColor getHealthColor()
	{
		int hp = playerCharacterData.getDataInt(PlayerCharacterData.CHAR_HEALTH);
		int maxHP = Bukkit.getPlayer(playerCharacterData.getDataString(PlayerCharacterData.PLAYER_NAME)).getMaxHealth();
		ChatColor color = ChatColor.RESET;

		// Set favor color dynamically
		if(hp < Math.ceil(0.33 * maxHP)) color = ChatColor.RED;
		else if(hp < Math.ceil(0.66 * maxHP) && hp > Math.ceil(0.33 * maxHP)) color = ChatColor.YELLOW;
		if(hp > Math.ceil(0.66 * maxHP)) color = ChatColor.GREEN;

		return color;
	}

	public void setHealth(int amount)
	{
		playerCharacterData.saveData(PlayerCharacterData.CHAR_HEALTH, amount);
	}

	public void saveInventory()
	{
		this.playerCharacterInventory = DemigodsFactory.playerCharacterInventoryFactory.create(getOwner().getPlayer().getInventory());
	}

	public PlayerCharacterInventory getInventory()
	{
		if(this.playerCharacterInventory != null) return this.playerCharacterInventory;
		else return null;
	}

	public synchronized void setFoodLevel(int amount)
	{
		playerCharacterData.saveData(PlayerCharacterData.CHAR_FOOD_LEVEL, amount);

	}

	public synchronized void setExp(float amount)
	{
		playerCharacterData.saveData(PlayerCharacterData.CHAR_EXP, amount);

	}

	public synchronized void setLevel(int amount)
	{
		playerCharacterData.saveData(PlayerCharacterData.CHAR_LEVEL, amount);

	}

	public synchronized void setLocation(Location location)
	{
		this.specialLocation = DemigodsFactory.specialLocationFactory.create(location);
	}

	public synchronized void toggleActive(boolean option)
	{
		playerCharacterData.saveData(PlayerCharacterData.CHAR_ACTIVE, option);
	}

	public int getID()
	{
		return playerCharacterData.getDataInt(PlayerCharacterData.CHAR_ID);
	}

	public OfflinePlayer getOwner()
	{
		return Bukkit.getOfflinePlayer(playerCharacterData.getDataString(PlayerCharacterData.PLAYER_NAME));
	}

	public String getName()
	{
		return playerCharacterData.getDataString(PlayerCharacterData.CHAR_NAME);
	}

	public boolean isActive()
	{
		return playerCharacterData.getDataBool(PlayerCharacterData.CHAR_ACTIVE);
	}

	public Location getLocation()
	{
		return this.specialLocation.toLocation();
	}

	public int getHealth()
	{
		return playerCharacterData.getDataInt(PlayerCharacterData.CHAR_HEALTH);
	}

	public int getFoodLevel()
	{
		return playerCharacterData.getDataInt(PlayerCharacterData.CHAR_FOOD_LEVEL);
	}

	public int getLevel()
	{
		return playerCharacterData.getDataInt(PlayerCharacterData.CHAR_LEVEL);
	}

	public float getExp()
	{
		return playerCharacterData.getDataFloat(PlayerCharacterData.CHAR_EXP);
	}

	public boolean hasCharacterClass()
	{
		return this.playerCharacterClass != null;
	}

	public PlayerCharacterClass getCharacterClass()
	{
		return this.playerCharacterClass;
	}

	public void setCharacterClass(PlayerCharacterClass characterClass)
	{
		this.playerCharacterClass = characterClass;
	}

	/**
	 * Enum defining the data being held in this object.
	 */
	public static enum PlayerCharacterData
	{
		/**
		 * String: Name of the player owning this character.
		 */
		PLAYER_NAME,

		/**
		 * String: Name of this character.
		 */
		CHAR_NAME,

		/**
		 * Integer: Unique ID for this character.
		 */
		CHAR_ID,

		/**
		 * Integer: The level of this character.
		 */
		CHAR_LEVEL,

		/**
		 * Integer: The food level of this character.
		 */
		CHAR_FOOD_LEVEL,

		/**
		 * Integer: The health of this character.
		 */
		CHAR_HEALTH,

		/**
		 * Float: The experience points of this character.
		 */
		CHAR_EXP,

		/**
		 * Boolean: True if this character is active.
		 */
		CHAR_ACTIVE
	}
}
