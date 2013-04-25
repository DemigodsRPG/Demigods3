package com.censoredsoftware.Objects.Character;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Modules.DataPersistence.EnumDataModule;
import com.censoredsoftware.Modules.DataPersistence.IntegerDataModule;
import com.censoredsoftware.Modules.DataPersistence.StringDataModule;

public class PlayerCharacterClass
{
	private EnumDataModule playerCharacterData;
	private StringDataModule abilityCharacterData;
	private IntegerDataModule bindingCharacterData;
	private int globalMaxFavor;

	public PlayerCharacterClass(Plugin instance, int globalMaxFavor, PlayerCharacter character, String className, String teamName, int favor, int maxFavor, int devotion, int ascensions, int offense, int defense, int stealth, int support, int passive, boolean active)
	{
		playerCharacterData = new EnumDataModule(instance, "character" + File.separator + character.getName() + File.separator + className);
		abilityCharacterData = new StringDataModule(instance, "character" + File.separator + character.getName() + File.separator + className + ".abilities");
		bindingCharacterData = new IntegerDataModule(instance, "character" + File.separator + character.getName() + File.separator + className + ".bindings");
		this.globalMaxFavor = globalMaxFavor;

		playerCharacterData.saveData(PlayerCharacterClassData.CHAR_NAME, character.getName());
		playerCharacterData.saveData(PlayerCharacterClassData.CLASS_NAME, className);
		playerCharacterData.saveData(PlayerCharacterClassData.TEAM_NAME, teamName);
		playerCharacterData.saveData(PlayerCharacterClassData.FAVOR, favor);
		playerCharacterData.saveData(PlayerCharacterClassData.MAX_FAVOR, maxFavor);
		playerCharacterData.saveData(PlayerCharacterClassData.DEVOTION, devotion);
		playerCharacterData.saveData(PlayerCharacterClassData.ASCENSIONS, ascensions);
		playerCharacterData.saveData(PlayerCharacterClassData.OFFENSE, offense);
		playerCharacterData.saveData(PlayerCharacterClassData.DEFENSE, defense);
		playerCharacterData.saveData(PlayerCharacterClassData.STEALTH, stealth);
		playerCharacterData.saveData(PlayerCharacterClassData.SUPPORT, support);
		playerCharacterData.saveData(PlayerCharacterClassData.PASSIVE, passive);
		playerCharacterData.saveData(PlayerCharacterClassData.CLASS_ACTIVE, active);
	}

	public void setFavor(int amount)
	{
		playerCharacterData.saveData(PlayerCharacterClassData.FAVOR, amount);
	}

	public void giveFavor(int amount)
	{
		if(playerCharacterData.getDataInt(PlayerCharacterClassData.FAVOR) + amount > playerCharacterData.getDataInt(PlayerCharacterClassData.MAX_FAVOR)) playerCharacterData.saveData(PlayerCharacterClassData.FAVOR, playerCharacterData.getDataInt(PlayerCharacterClassData.MAX_FAVOR));
		else playerCharacterData.saveData(PlayerCharacterClassData.FAVOR, playerCharacterData.getDataInt(PlayerCharacterClassData.FAVOR) + amount);
	}

	public void subtractFavor(int amount)
	{
		if(playerCharacterData.getDataInt(PlayerCharacterClassData.FAVOR) - amount < 0) playerCharacterData.saveData(PlayerCharacterClassData.FAVOR, 0);
		else playerCharacterData.saveData(PlayerCharacterClassData.FAVOR, playerCharacterData.getDataInt(PlayerCharacterClassData.FAVOR) - amount);
	}

	public void setMaxFavor(int amount)
	{
		if((amount) > this.globalMaxFavor) playerCharacterData.saveData(PlayerCharacterClassData.MAX_FAVOR, this.globalMaxFavor);
		else playerCharacterData.saveData(PlayerCharacterClassData.MAX_FAVOR, amount);
	}

	public void addMaxFavor(int amount)
	{
		if((playerCharacterData.getDataInt(PlayerCharacterClassData.MAX_FAVOR) + amount) > this.globalMaxFavor) playerCharacterData.saveData(PlayerCharacterClassData.MAX_FAVOR, this.globalMaxFavor);
		else playerCharacterData.saveData(PlayerCharacterClassData.MAX_FAVOR, playerCharacterData.getDataInt(PlayerCharacterClassData.MAX_FAVOR) + amount);
	}

	public ChatColor getFavorColor()
	{
		int favor = playerCharacterData.getDataInt(PlayerCharacterClassData.FAVOR);
		int maxFavor = playerCharacterData.getDataInt(PlayerCharacterClassData.MAX_FAVOR);
		ChatColor color = ChatColor.RESET;

		// Set favor color dynamically
		if(favor < Math.ceil(0.33 * maxFavor)) color = ChatColor.RED;
		else if(favor < Math.ceil(0.66 * maxFavor) && favor > Math.ceil(0.33 * maxFavor)) color = ChatColor.YELLOW;
		if(favor > Math.ceil(0.66 * maxFavor)) color = ChatColor.GREEN;

		return color;
	}

	public int getDevotionGoal()
	{
		return (int) Math.ceil(500 * Math.pow(playerCharacterData.getDataInt(PlayerCharacterClassData.ASCENSIONS) + 1, 2.02));
	}

	public void setDevotion(int amount)
	{
		playerCharacterData.saveData(PlayerCharacterClassData.DEVOTION, amount);
	}

	public void giveDevotion(int amount)
	{
		int devotionBefore = playerCharacterData.getDataInt(PlayerCharacterClassData.DEVOTION);
		int devotionGoal = getDevotionGoal();
		playerCharacterData.saveData(PlayerCharacterClassData.DEVOTION, devotionBefore + amount);
		int devotionAfter = playerCharacterData.getDataInt(PlayerCharacterClassData.DEVOTION);

		if(devotionAfter > devotionBefore && devotionAfter > devotionGoal)
		{
			// Character leveled up!

			// TODO Trigger an event here instead of doing it as part of the object,
			// TODO that way we can grab a lot more stuff from the listener without having to make everything public.

			playerCharacterData.saveData(PlayerCharacterClassData.ASCENSIONS, playerCharacterData.getDataInt(PlayerCharacterClassData.ASCENSIONS) + 1);
			playerCharacterData.saveData(PlayerCharacterClassData.DEVOTION, devotionAfter - devotionGoal);
		}
	}

	public void subtractDevotion(int amount)
	{
		if(playerCharacterData.getDataInt(PlayerCharacterClassData.DEVOTION) - amount < 0) playerCharacterData.saveData(PlayerCharacterClassData.DEVOTION, 0);
		else playerCharacterData.saveData(PlayerCharacterClassData.DEVOTION, playerCharacterData.getDataInt(PlayerCharacterClassData.DEVOTION) - amount);
	}

	public void setAscensions(int amount)
	{
		playerCharacterData.saveData(PlayerCharacterClassData.ASCENSIONS, amount);
	}

	public void giveAscensions(int amount)
	{
		playerCharacterData.saveData(PlayerCharacterClassData.ASCENSIONS, playerCharacterData.getDataInt(PlayerCharacterClassData.ASCENSIONS) + amount);
	}

	public void subtractAscensions(int amount)
	{
		if(playerCharacterData.getDataInt(PlayerCharacterClassData.ASCENSIONS) - amount < 0) playerCharacterData.saveData(PlayerCharacterClassData.ASCENSIONS, 0);
		else playerCharacterData.saveData(PlayerCharacterClassData.ASCENSIONS, playerCharacterData.getDataInt(PlayerCharacterClassData.ASCENSIONS) - amount);
	}

	public void setTeam(String alliance)
	{
		playerCharacterData.saveData(PlayerCharacterClassData.TEAM_NAME, alliance);
	}

	public boolean isClass(String className)
	{
		return playerCharacterData.getDataString(PlayerCharacterClassData.CLASS_NAME).equalsIgnoreCase(className);
	}

	public synchronized void toggleActive(boolean option)
	{
		playerCharacterData.saveData(PlayerCharacterClassData.CLASS_ACTIVE, option);
	}

	public boolean isEnabledAbility(String ability)
	{
		return abilityCharacterData.containsKey(ability) && abilityCharacterData.getDataBool(ability);
	}

	public void toggleAbility(String ability, boolean option)
	{
		abilityCharacterData.saveData(ability, option);
	}

	public boolean isBound(Material material)
	{
		return getBindings() != null && getBindings().contains(material);
	}

	public Material getBind(String ability)
	{
		for(int type : getBindings())
		{
			if(bindingCharacterData.getDataString(type).equalsIgnoreCase(ability)) return Material.getMaterial(type);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getBindings()
	{
		return bindingCharacterData.listKeys();
	}

	public synchronized void setBound(String ability, Material material)
	{
		bindingCharacterData.saveData(material.getId(), ability);

		// TODO None of the below should be in here, it should be in the place where the player/character can be grabbed.

		/**
		 * Player player = (Player) Bukkit.getOfflinePlayer(this.playerName);
		 * if(API.data.getCharData(this.charID, ability + "_bind") == null)
		 * {
		 * if(player.getItemInHand().getType() == Material.AIR)
		 * {
		 * player.sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
		 * }
		 * else
		 * {
		 * if(isBound(material))
		 * {
		 * player.sendMessage(ChatColor.YELLOW + "That item is already bound to a skill.");
		 * return false;
		 * }
		 * else if(material == Material.AIR)
		 * {
		 * player.sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
		 * return false;
		 * }
		 * else
		 * {
		 * if(API.data.hasCharData(this.charID, "bindings"))
		 * {
		 * ArrayList<Material> bindings = getBindings();
		 * if(!bindings.contains(material)) bindings.add(material);
		 * API.data.saveCharData(this.charID, "bindings", bindings);
		 * }
		 * else
		 * {
		 * ArrayList<Material> bindings = new ArrayList<Material>();
		 * bindings.add(material);
		 * API.data.saveCharData(this.charID, "bindings", bindings);
		 * }
		 * 
		 * 
		 * 
		 * API.data.saveCharData(this.charID, ability + "_bind", material);
		 * player.sendMessage(ChatColor.YELLOW + ability + " is now bound to: " + material.name().toUpperCase());
		 * return true;
		 * }
		 * }
		 * }
		 * else
		 * {
		 * removeBind(ability, ((Material) API.data.getCharData(this.charID, ability + "_bind")));
		 * player.sendMessage(ChatColor.YELLOW + ability + "'s bind has been removed.");
		 * }
		 * return false;
		 */
	}

	public void removeBind(Material material)
	{
		if(bindingCharacterData.containsKey(material.getId())) bindingCharacterData.removeData(material.getId());
	}

	public void removeBind(String ability)
	{
		if(getBind(ability) != null) removeBind(getBind(ability));
	}

	public String getClassName()
	{
		return playerCharacterData.getDataString(PlayerCharacterClassData.CLASS_NAME);
	}

	public String getTeam()
	{
		return playerCharacterData.getDataString(PlayerCharacterClassData.TEAM_NAME);
	}

	public boolean isActive()
	{
		return playerCharacterData.getDataBool(PlayerCharacterClassData.CLASS_ACTIVE);
	}

	public int getFavor()
	{
		return playerCharacterData.getDataInt(PlayerCharacterClassData.FAVOR);
	}

	public int getMaxFavor()
	{
		return playerCharacterData.getDataInt(PlayerCharacterClassData.MAX_FAVOR);
	}

	public int getDevotion()
	{
		return playerCharacterData.getDataInt(PlayerCharacterClassData.DEVOTION);
	}

	public int getAscensions()
	{
		return playerCharacterData.getDataInt(PlayerCharacterClassData.ASCENSIONS);
	}

	// TODO Levels (Offense, Defense, Stealth, Support, Passive).

	/**
	 * Enum defining the data being held in this object.
	 */
	public static enum PlayerCharacterClassData
	{
		/**
		 * String
		 */
		CHAR_NAME,

		/**
		 * String
		 */
		CLASS_NAME,

		/**
		 * String
		 */
		TEAM_NAME,

		/**
		 * Integer
		 */
		FAVOR,

		/**
		 * Integer
		 */
		MAX_FAVOR,

		/**
		 * Integer
		 */
		DEVOTION,

		/**
		 * Integer
		 */
		ASCENSIONS,

		/**
		 * Integer
		 */
		OFFENSE,

		/**
		 * Integer
		 */
		DEFENSE,

		/**
		 * Integer
		 */
		STEALTH,

		/**
		 * Integer
		 */
		SUPPORT,

		/**
		 * Integer
		 */
		PASSIVE,

		/**
		 * Boolean
		 */
		CLASS_ACTIVE
	}
}
