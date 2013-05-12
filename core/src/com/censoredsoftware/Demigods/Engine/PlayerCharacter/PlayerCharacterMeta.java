package com.censoredsoftware.Demigods.Engine.PlayerCharacter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import redis.clients.johm.Attribute;
import redis.clients.johm.CollectionMap;
import redis.clients.johm.Id;
import redis.clients.johm.Model;

import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.DemigodsData;

@Model
public class PlayerCharacterMeta
{
	@Id
	private Long id;
	@Attribute
	private Integer ascensions;
	@Attribute
	private Integer devotion;
	@Attribute
	private Integer favor;
	@Attribute
	private Integer maxFavor;
	@CollectionMap(key = String.class, value = Boolean.class)
	private Map<String, Boolean> abilityData;
	@CollectionMap(key = Integer.class, value = String.class)
	private Map<Integer, String> bindingData;
	@CollectionMap(key = String.class, value = Boolean.class)
	private Map<String, Boolean> taskData;
	@CollectionMap(key = String.class, value = Integer.class)
	private Map<String, Integer> levelsData;

	public static void save(PlayerCharacterMeta playerCharacterMeta)
	{
		DemigodsData.jOhm.save(playerCharacterMeta);
	}

	public static PlayerCharacterMeta load(long id) // TODO This belongs somewhere else.
	{
		return DemigodsData.jOhm.get(PlayerCharacterMeta.class, id);
	}

	public static Set<PlayerCharacterMeta> loadAll()
	{
		return DemigodsData.jOhm.getAll(PlayerCharacterMeta.class);
	}

	void initializeMaps()
	{
		this.abilityData = new HashMap<String, Boolean>();
		this.bindingData = new HashMap<Integer, String>();
		this.taskData = new HashMap<String, Boolean>();
		this.levelsData = new HashMap<String, Integer>();
	}

	public long getId()
	{
		return this.id;
	}

	public boolean isEnabledAbility(String ability)
	{
		return abilityData.containsKey(ability) && abilityData.get(ability);
	}

	public void toggleAbility(String ability, boolean option)
	{
		abilityData.put(ability, option);
	}

	public boolean isBound(Material material)
	{
		return getBindings() != null && getBindings().contains(material);
	}

	public Material getBind(String ability)
	{
		for(int type : getBindings())
		{
			if(bindingData.get(type).equalsIgnoreCase(ability)) return Material.getMaterial(type);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Set<Integer> getBindings()
	{
		Set<Integer> bindings = new HashSet<Integer>();
		for(int bind : bindingData.keySet())
		{
			bindings.add(bind);
		}
		return bindings;
	}

	public void setBound(String ability, Material material)
	{
		// TODO None of the below should be in here, it should be in the place where the player/character can be grabbed.
		Player player = CharacterAPI.getChar(getId()).getPlayer().getPlayer();
		if(!bindingData.containsKey(ability))
		{
			if(player.getItemInHand().getType() == Material.AIR)
			{
				player.sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
			}
			else
			{
				if(isBound(material))
				{
					player.sendMessage(ChatColor.YELLOW + "That item is already bound to a skill.");
					return;
				}
				else if(material == Material.AIR)
				{
					player.sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
					return;
				}
				else
				{
					bindingData.put(material.getId(), ability);
					player.sendMessage(ChatColor.YELLOW + ability + " is now bound to: " + material.name().toUpperCase());
					return;
				}
			}
		}
		else
		{
			removeBind(ability);
			player.sendMessage(ChatColor.YELLOW + ability + "'s bind has been removed.");
		}
	}

	public void removeBind(Material material)
	{
		if(bindingData.containsKey(material.getId())) bindingData.remove(material.getId());
	}

	public void removeBind(String ability)
	{
		if(getBind(ability) != null) removeBind(getBind(ability));
	}

	public boolean isFinishedTask(String taskName)
	{
		return taskData.containsKey(taskName) && taskData.get(taskName);
	}

	public void finishTask(String taskName, boolean option)
	{
		taskData.put(taskName, option);
	}

	public int getLevel(String level)
	{
		return levelsData.get(level.toUpperCase());
	}

	public void setLevel(String level, int amount)
	{
		levelsData.put(level.toUpperCase(), amount);
	}

	public void addLevel(String level, int amount)
	{
		setLevel(level.toUpperCase(), getLevel(level.toUpperCase()) + amount);
	}

	public void subtractLevel(String level, int amount)
	{
		if(getLevel(level.toUpperCase()) - amount < 0) setLevel(level.toUpperCase(), 0);
		else setLevel(level.toUpperCase(), getLevel(level.toUpperCase()) - amount);
	}

	public Integer getAscensions()
	{
		return this.ascensions;
	}

	public void addAscension()
	{
		this.ascensions += 1;
	}

	public void addAscensions(int amount)
	{
		this.ascensions += amount;
	}

	public void subtractAscensions(int amount)
	{
		this.ascensions -= amount;
	}

	public void setAscensions(int amount)
	{
		this.ascensions = amount;
	}

	public int getDevotionGoal()
	{
		return (int) Math.ceil(500 * Math.pow(getAscensions() + 1, 2.02));
	}

	public Integer getDevotion()
	{
		return this.devotion;
	}

	public void addDevotion(int amount)
	{
		int devotionBefore = this.devotion;
		int devotionGoal = getDevotionGoal();
		this.devotion = devotionBefore + amount;
		int devotionAfter = this.devotion;

		if(devotionAfter > devotionBefore && devotionAfter > devotionGoal)
		{
			// Character leveled up!

			// TODO Trigger an event here instead of doing it as part of the object,
			// TODO that way we can grab a lot more stuff from the listener without having to make everything public.

			this.ascensions = getAscensions() + 1;
			this.devotion = devotionAfter - devotionGoal;
		}
	}

	public void subtractDevotion(int amount)
	{
		this.devotion -= amount;
	}

	public void setDevotion(int amount)
	{
		this.devotion = amount;
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

	public Integer getFavor()
	{
		return this.favor;
	}

	public void setFavor(int amount)
	{
		this.favor = amount;
	}

	public void addFavor(int amount)
	{
		if((getFavor() + amount) > getMaxFavor()) this.favor = getMaxFavor();
		this.favor += amount;
	}

	public void subtractFavor(int amount)
	{
		if((this.favor - amount) < 0) this.favor = 0;
		this.favor -= amount;
	}

	public Integer getMaxFavor()
	{
		return this.maxFavor;
	}

	public void addMaxFavor(int amount)
	{
		setMaxFavor(getMaxFavor() + amount);
	}

	public void setMaxFavor(int amount)
	{
		if(amount < 0) this.maxFavor = 0;
		if(amount > Demigods.config.getSettingInt("caps.favor")) this.maxFavor = Demigods.config.getSettingInt("caps.favor");
		else this.maxFavor = amount;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
