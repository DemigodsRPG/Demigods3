package com.legit2.Demigods.Libraries;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.legit2.Demigods.Utilities.DConfigUtil;
import com.legit2.Demigods.Utilities.DDataUtil;
import com.legit2.Demigods.Utilities.DDeityUtil;
import com.legit2.Demigods.Utilities.DObjUtil;
import com.legit2.Demigods.Utilities.DPlayerUtil;

public class DCharacter implements Serializable
{
	private static final long serialVersionUID = 8201132625259394712L;

	static OfflinePlayer player;
	static String charName, charDeity, charAlliance;
	static int playerID, charID, charHP, charFavor, charMaxFavor, charDevotion, charAscensions;
	static float charExp;
	static Location charLoc;
	static boolean charActive, charImmortal;
	
	public DCharacter(Player player, int charID, String charName, String charDeity)
	{
		// Define variables
		DCharacter.player = player;
		DCharacter.charID = charID;
		playerID = DPlayerUtil.getPlayerID(player);
		charName = DObjUtil.capitalize(charName);
		charDeity = DObjUtil.capitalize(charDeity);
		charAlliance = DDeityUtil.getDeityAlliance(charDeity);
		charHP = player.getHealth();
		charExp = player.getExp();
		charLoc = player.getLocation();
		charFavor = DConfigUtil.getSettingInt("default_favor");
		charMaxFavor = DConfigUtil.getSettingInt("default_max_favor");
		charDevotion = DConfigUtil.getSettingInt("default_devotion");
		charAscensions = DConfigUtil.getSettingInt("default_ascensions");
		charActive = true;
		charImmortal = true;
	}
	
	public void save()
	{
		DDataUtil.saveCharData(charID, "char_object", this);
	}
	
	/* ----------------------------------------
	 * Favor-specific Methods
	 * ---------------------------------------- 
	 */
	public void setFavor(int amount)
	{
		charFavor = amount;
		save();
	}
	
	public void giveFavor(int amount)
	{
		if((getFavor() + amount) > getMaxFavor())
		{
			charFavor = getMaxFavor();
		}
		else charFavor = getFavor() + amount;
		save();
	}
	
	public void subtractFavor(int amount)
	{
		if(getFavor() - amount < 0)
		{
			charFavor = 0;
		}
		else charFavor = getFavor() - amount;
		save();
	}
	
	public void setMaxFavor(int amount)
	{
		charMaxFavor = amount;
		save();
	}
	
	public void addMaxFavor(int amount)
	{
		if((getMaxFavor() + amount) > DConfigUtil.getSettingInt("global_max_favor"))
		{
			charMaxFavor = DConfigUtil.getSettingInt("global_max_favor");
		}
		else charMaxFavor = getMaxFavor() + amount;
		save();
	}

	/* ----------------------------------------
	 * Devotion-specific Methods
	 * ---------------------------------------- 
	 */
	public void setDevotion(int amount)
	{
		charFavor = amount;
		save();
	}
	
	public void giveDevotion(int amount)
	{
		charDevotion = getDevotion() + amount;
		save();
	}
	
	public void subtractDevotion(int amount)
	{
		if(getDevotion() - amount < 0)
		{
			charDevotion = 0;
		}
		else charDevotion = getDevotion() - amount;
		save();
	}
	
	/* ----------------------------------------
	 * Ascension-specific Methods
	 * ---------------------------------------- 
	 */
	public void setAscensions(int amount)
	{
		charAscensions = amount;
		save();
	}
	
	public void giveAscensions(int amount)
	{
		charAscensions = getAscensions() + amount;
		save();
	}
	
	public void subtractAscensions(int amount)
	{
		if(getAscensions() - amount < 0)
		{
			charAscensions = 0;
		}
		else charAscensions = getAscensions() - amount;
		save();
	}
	
	/* ----------------------------------------
	 * Miscellaneous Methods
	 * ----------------------------------------
	 */
	public void setHP(int amount)
	{
		charHP = amount;
		save();
	}
	
	public void setExp(float amount)
	{
		charExp = amount;
		save();
	}
	
	public void setLocation(Location location)
	{
		charLoc = location;
		save();
	}
	
	public void setAlliance(String alliance)
	{
		charAlliance = alliance.toLowerCase();
		save();
	}
	
	public boolean hasDeity(String deity)
	{
		if(charDeity.toLowerCase().equalsIgnoreCase(deity)) return true;
		else return false;
	}
	
	public void toggleActive(boolean option)
	{
		charActive = option;
		save();
	}
	
	public void toggleImmortal(boolean option)
	{
		charImmortal = option;
		save();
	}
	
	public boolean isEnabledAbility(String ability)
	{
		if(DDataUtil.hasCharData(charID, "boolean_" + ability.toLowerCase()))
		{
			return DObjUtil.toBoolean(DDataUtil.getCharData(charID, "boolean_" + ability.toLowerCase()));
		}
		return false;
	}
	
	public void toggleAbility(String ability, boolean option)
	{
		DDataUtil.saveCharData(charID,  "boolean_" + ability.toLowerCase(), option);
		save();
	}
	
	public boolean isCooledDown(String ability, long ability_time, boolean sendMsg)
	{
		if(ability_time > System.currentTimeMillis())
		{
			if(sendMsg) ((Player) player).sendMessage(ChatColor.RED + ability + " has not cooled down!");
			return false;
		}
		else return true;
	}
	
	public static boolean isBound(Material material)
	{
		if(getBindings() != null && getBindings().contains(material)) return true;
		else return false;
	}
	
	public Material getBind(String ability)
	{
		if(DDataUtil.getCharData(charID, ability + "_bind") != null)
		{
			Material material = (Material) DDataUtil.getCharData(charID, ability + "_bind");
			return material;
		}
		else return null;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Material> getBindings()
	{		
		if(DDataUtil.hasCharData(charID, "bindings"))
		{
			return (ArrayList<Material>) DDataUtil.getCharData(charID, "bindings");
		}
		else return new ArrayList<Material>();
	}
	
	public boolean setBound(String ability, Material material)
	{			
		if(DDataUtil.getCharData(charID, ability + "_bind") == null)
		{
			if(((Player) player).getItemInHand().getType() == Material.AIR)
			{
				((Player) player).sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
			}
			else
			{
				if(isBound(material))
				{
					((Player) player).sendMessage(ChatColor.YELLOW + "That item is already bound to a skill.");
					return false;
				}
				else if(material == Material.AIR)
				{
					((Player) player).sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
					return false;
				}
				else
				{			
					if(DDataUtil.hasCharData(charID, "bindings"))
					{
						ArrayList<Material> bindings = getBindings();
						if(!bindings.contains(material)) bindings.add(material);
						DDataUtil.saveCharData(charID, "bindings", bindings);
					}
					else
					{
						ArrayList<Material> bindings = new ArrayList<Material>();
						bindings.add(material);
						DDataUtil.saveCharData(charID, "bindings", bindings);
					}

					save();
					
					DDataUtil.saveCharData(charID, ability + "_bind", material);
					((Player) player).sendMessage(ChatColor.YELLOW + ability + " is now bound to: " + material.name().toUpperCase());
					return true;
				}
			}
		}
		else
		{
			removeBind(ability, ((Material) DDataUtil.getCharData(charID, ability + "_bind")));
			((Player) player).sendMessage(ChatColor.YELLOW + ability + "'s bind has been removed.");
		}
		return false;
	}

	public boolean removeBind(String ability, Material material)
	{
		ArrayList<Material> bindings = null;

		if(DDataUtil.hasCharData(charID, "bindings"))
		{
			bindings = getBindings();
			
			if(bindings != null && bindings.contains(material)) bindings.remove(material);
		}
		
		DDataUtil.saveCharData(charID, "bindings", bindings);
		DDataUtil.removeCharData(charID, ability + "_bind");
		
		save();
		return true;
	}

	public int getID()
	{
		return charID;
	}
	
	public Player getOwner() 
	{
		return Bukkit.getPlayer(player.getName());
	}
	
	public int getOwnerID() 
	{ 
		return playerID; 
	}
	
	public String getName() 
	{ 
		return charName; 
	}
	
	public String getDeity()
	{ 
		return charDeity; 
	}
	
	public String getAlliance() 
	{ 
		return charAlliance; 
	}
	public boolean isImmortal() 
	{ 
		return charImmortal; 
	}
	
	public boolean isActive() 
	{	
		return charActive; 
	}
	
	public Location getLastLocation() 
	{ 
		return charLoc; 
	}
	
	public int getHP()
	{
		return charHP;
	}
	
	public float getExp()
	{
		return charExp;
	}
	
	public int getFavor() 
	{
		return charFavor; 
	}
	
	public int getMaxFavor() 
	{ 
		return charMaxFavor;	
	}
	
	public int getDevotion() 
	{ 
		return charDevotion;	
	}
	
	public int getAscensions() 
	{ 
		return charAscensions;	
	}
}