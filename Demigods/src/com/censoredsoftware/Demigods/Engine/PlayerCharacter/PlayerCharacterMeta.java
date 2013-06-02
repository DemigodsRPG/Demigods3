package com.censoredsoftware.Demigods.Engine.PlayerCharacter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.Demigods;

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
	@CollectionMap(key = String.class, value = Boolean.class)
	private Map<Ability.Type, PlayerCharacterSpecialty> specialtyData;

	private void save()
	{
		PlayerCharacterMeta.save(this);
	}

	public static void save(PlayerCharacterMeta playerCharacterMeta)
	{
		JOhm.save(playerCharacterMeta);
	}

	public static PlayerCharacterMeta load(long id)
	{
		return JOhm.get(PlayerCharacterMeta.class, id);
	}

	public static Set<PlayerCharacterMeta> loadAll()
	{
		return JOhm.getAll(PlayerCharacterMeta.class);
	}

	void initializeMaps()
	{
		this.abilityData = new HashMap<String, Boolean>();
		this.bindingData = new HashMap<Integer, String>();
		this.taskData = new HashMap<String, Boolean>();
		this.specialtyData = new HashMap<Ability.Type, PlayerCharacterSpecialty>();
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
		return getBindings() != null && getBindings().contains(material.getId());
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
		Player player = PlayerCharacter.getChar(getId()).getOfflinePlayer().getPlayer();
		if(!bindingData.containsValue(ability))
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
				}
				else if(material == Material.AIR)
				{
					player.sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
				}
				else
				{
					bindingData.put(material.getId(), ability);
					player.sendMessage(ChatColor.YELLOW + ability + " is now bound to: " + material.name().toUpperCase());
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
		save();
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

	public void addSpecialty(PlayerCharacterSpecialty specialty)
	{
		if(!this.specialtyData.containsKey(specialty.getType())) this.specialtyData.put(specialty.getType(), specialty);
	}

	public Integer getLevel(Ability.Type type)
	{
		if(this.specialtyData.containsKey(type)) return this.specialtyData.get(type).getLevel();
		else return 1;
	}

	public Integer getAscensions()
	{
		return this.ascensions;
	}

	public void addAscension()
	{
		this.ascensions += 1;
		save();
	}

	public void addAscensions(int amount)
	{
		this.ascensions += amount;
		save();
	}

	public void subtractAscensions(int amount)
	{
		this.ascensions -= amount;
		save();
	}

	public void setAscensions(int amount)
	{
		this.ascensions = amount;
		save();
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
		this.devotion += amount;
		int devotionAfter = this.devotion;

		if(devotionAfter > devotionBefore && devotionAfter > devotionGoal)
		{
			// Character leveled up!

			// TODO Trigger an event here instead of doing it as part of the object,
			// TODO that way we can grab a lot more stuff from the listener without having to make everything public.

			this.ascensions += 1;
			this.devotion = devotionAfter - devotionGoal;
		}

		save();
	}

	public void subtractDevotion(int amount)
	{
		this.devotion -= amount;
		save();
	}

	public void setDevotion(int amount)
	{
		this.devotion = amount;
		save();
	}

	public ChatColor getFavorColor()
	{
		int favor = this.favor;
		int maxFavor = this.maxFavor;
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
		save();
	}

	public void addFavor(int amount)
	{
		if((this.favor + amount) > this.maxFavor)
		{
			this.favor = this.maxFavor;
		}
		else
		{
			this.favor += amount;
		}
		save();
	}

	public void subtractFavor(int amount)
	{
		if((this.favor - amount) < 0)
		{
			this.favor = 0;
		}
		else
		{
			this.favor -= amount;
		}
		save();
	}

	public Integer getMaxFavor()
	{
		return this.maxFavor;
	}

	public void addMaxFavor(int amount)
	{
		if((this.maxFavor + amount) > Demigods.config.getSettingInt("caps.favor"))
		{
			this.maxFavor = Demigods.config.getSettingInt("caps.favor");
		}
		else
		{
			this.maxFavor += amount;
		}
		save();
	}

	public void setMaxFavor(int amount)
	{
		if(amount < 0) this.maxFavor = 0;
		if(amount > Demigods.config.getSettingInt("caps.favor")) this.maxFavor = Demigods.config.getSettingInt("caps.favor");
		else this.maxFavor = amount;
		save();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
