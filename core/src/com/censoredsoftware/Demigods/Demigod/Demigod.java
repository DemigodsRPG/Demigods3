package com.censoredsoftware.Demigods.Demigod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import com.censoredsoftware.Modules.PlayerCharacter.PlayerCharacter;

public class Demigod extends PlayerCharacter
{
	private int globalMaxFavor;
	private Map<String, Object> abilityData;
	private Map<Integer, Object> bindingData;

	public Demigod(int globalMaxFavor, OfflinePlayer player, int charID, String charName, boolean charActive, String className, String teamName, int favor, int maxFavor, int devotion, int ascensions, int offense, int defense, int stealth, int support, int passive, boolean classActive)
	{
		// Create PlayerCharacter
		super(player, charID, charName, charActive);

		// Define HashMaps
		abilityData = new HashMap<String, Object>();
		bindingData = new HashMap<Integer, Object>();

		// Define Important Caps
		this.globalMaxFavor = globalMaxFavor;

		// Save Class Data
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

		save(this);
	}

	/**
	 * Checks if the abilityData Map contains <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return True if abilityData contains the key.
	 */
	boolean containsAbilityKey(String key)
	{
		return abilityData.get(key) != null && abilityData.containsKey(key);
	}

	/**
	 * Retrieve the Object data from String <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Object data.
	 */
	Object getAbilityData(String key)
	{
		if(containsAbilityKey(key)) return abilityData.get(key);
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Save the Object <code>data</code> for String <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @param data The Object being saved.
	 */
	void saveAbilityData(String key, Object data)
	{
		abilityData.put(key, data);
	}

	/**
	 * Remove the data from String <code>key</code>.
	 * 
	 * @param key The key in the save.
	 */
	void removeAbilityData(String key)
	{
		if(!containsAbilityKey(key)) return;
		abilityData.remove(key);
	}

	/**
	 * Checks if the bindingData Map contains <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return True if bindingData contains the key.
	 */
	boolean containsBindingKey(int key)
	{
		return bindingData.get(key) != null && bindingData.containsKey(key);
	}

	/**
	 * Retrieve the Object data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Object data.
	 */
	Object getBindingData(int key)
	{
		if(containsBindingKey(key)) return bindingData.get(key);
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Save the Object <code>data</code> for int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @param data The Object being saved.
	 */
	void saveBindingData(int key, Object data)
	{
		bindingData.put(key, data);
	}

	/**
	 * Remove the data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 */
	void removeBindingData(int key)
	{
		if(!containsBindingKey(key)) return;
		bindingData.remove(key);
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

	public boolean isClass(String className)
	{
		return isDeity().equalsIgnoreCase(className);
	}

	public void toggleActive(boolean option)
	{
		saveData("CLASS_ACTIVE", option);
	}

	public boolean isEnabledAbility(String ability)
	{
		return containsAbilityKey(ability) && Boolean.parseBoolean(getAbilityData(ability).toString());
	}

	public void toggleAbility(String ability, boolean option)
	{
		saveAbilityData(ability, option);
	}

	public boolean isBound(Material material)
	{
		return getBindings() != null && getBindings().contains(material);
	}

	public Material getBind(String ability)
	{
		for(int type : getBindings())
		{
			if(getBindingData(type).toString().equalsIgnoreCase(ability)) return Material.getMaterial(type);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getBindings()
	{
		List<Integer> bindings = new ArrayList<Integer>();
		for(int bind : bindingData.keySet())
		{
			bindings.add(bind);
		}
		return bindings;
	}

	public synchronized void setBound(String ability, Material material)
	{
		saveBindingData(material.getId(), ability);

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
		if(containsBindingKey(material.getId())) removeBindingData(material.getId());
	}

	public void removeBind(String ability)
	{
		if(getBind(ability) != null) removeBind(getBind(ability));
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

	/**
	 * Grab the Map in it's entirely.
	 */
	protected Map<String, Object> getAbilityMap()
	{
		return this.abilityData;
	}

	protected void setAbilityMap(Map map)
	{
		try
		{
			this.abilityData = map;
		}
		catch(Exception ignored)
		{}
	}

	/**
	 * Grab the Map in it's entirely.
	 */
	protected Map<Integer, Object> getBindingMap()
	{
		return this.bindingData;
	}

	protected void setBindingMap(Map map)
	{
		try
		{
			this.bindingData = map;
		}
		catch(Exception ignored)
		{}
	}
}
