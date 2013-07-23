package com.censoredsoftware.Demigods.Engine.Object.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Ability.Devotion;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsItemStack;
import com.google.common.collect.Sets;

@Model
public class PlayerCharacterMeta
{
	@Id
	private Long id;
	@Attribute
	private Integer ascensions;
	@Attribute
	private Integer favor;
	@Attribute
	private Integer maxFavor;
	@CollectionMap(key = Integer.class, value = String.class)
	private Map<String, Long> bindingData;
	@CollectionMap(key = String.class, value = Boolean.class)
	private Map<String, Boolean> abilityData;
	@CollectionMap(key = String.class, value = Boolean.class)
	private Map<String, Boolean> taskData;
	@CollectionMap(key = String.class, value = Boolean.class)
	private Map<String, Devotion> devotionData;

	void initializeMaps()
	{
		this.abilityData = new HashMap<String, Boolean>();
		this.bindingData = new HashMap<String, Long>();
		this.taskData = new HashMap<String, Boolean>();
		this.devotionData = new HashMap<String, Devotion>();
	}

	public static PlayerCharacterMeta create()
	{
		PlayerCharacterMeta charMeta = new PlayerCharacterMeta();
		charMeta.initializeMaps();
		charMeta.setAscensions(Demigods.config.getSettingInt("character.defaults.ascensions"));
		charMeta.setFavor(Demigods.config.getSettingInt("character.defaults.favor"));
		charMeta.setMaxFavor(Demigods.config.getSettingInt("character.defaults.max_favor"));
		charMeta.addDevotion(Devotion.create(Devotion.Type.OFFENSE));
		charMeta.addDevotion(Devotion.create(Devotion.Type.DEFENSE));
		charMeta.addDevotion(Devotion.create(Devotion.Type.PASSIVE));
		charMeta.addDevotion(Devotion.create(Devotion.Type.STEALTH));
		charMeta.addDevotion(Devotion.create(Devotion.Type.SUPPORT));
		charMeta.addDevotion(Devotion.create(Devotion.Type.ULTIMATE));
		PlayerCharacterMeta.save(charMeta);
		return charMeta;
	}

	public long getId()
	{
		return this.id;
	}

	public void addDevotion(Devotion devotion)
	{
		if(!this.devotionData.containsKey(devotion.getType().toString())) this.devotionData.put(devotion.getType().toString(), devotion);
		save(this);
	}

	public Devotion getDevotion(Devotion.Type type)
	{
		if(this.devotionData.containsKey(type.toString()))
		{
			return this.devotionData.get(type.toString());
		}
		else
		{
			addDevotion(Devotion.create(type));
			return this.devotionData.get(type.toString());
		}
	}

	public boolean isEnabledAbility(String ability)
	{
		return abilityData.containsKey(ability) && abilityData.get(ability);
	}

	public void toggleAbility(String ability, boolean option)
	{
		abilityData.put(ability, option);
	}

	public boolean isBound(String ability)
	{
		return getBind(ability) != null;
	}

	public boolean isBound(ItemStack item)
	{
		return getBind(item) != null;
	}

	public DemigodsItemStack getBind(String ability)
	{
		return this.bindingData.containsKey(ability) ? DemigodsItemStack.load(this.bindingData.get(ability)) : null;
	}

	public DemigodsItemStack getBind(ItemStack item)
	{
		for(Long bindId : this.bindingData.values())
		{
			DemigodsItemStack bind = DemigodsItemStack.load(bindId);
			if(bind.toItemStack().equals(item))
			{
				return bind;
			}
		}
		return null;
	}

	public Set<DemigodsItemStack> getBindings()
	{
		Set<DemigodsItemStack> bindings = Sets.newHashSet();
		for(Long id : this.bindingData.values())
		{
			bindings.add(DemigodsItemStack.load(id));
		}
		return bindings;
	}

	public void removeBind(String ability)
	{
		this.bindingData.remove(ability);
	}

	public void setBound(String ability, ItemStack item)
	{
		this.bindingData.put(ability, DemigodsItemStack.create(item).getId());
	}

	public void removeBind(ItemStack item)
	{
		if(isBound(item))
		{
			DemigodsItemStack bind = getBind(item);
			this.bindingData.values().remove(bind);
		}
	}

	public boolean isFinishedTask(String taskName)
	{
		return taskData.containsKey(taskName) && taskData.get(taskName);
	}

	public void finishTask(String taskName, boolean option)
	{
		taskData.put(taskName, option);
	}

	public Integer getAscensions()
	{
		return this.ascensions;
	}

	public void addAscension()
	{
		this.ascensions += 1;
		save(this);
	}

	public void addAscensions(int amount)
	{
		this.ascensions += amount;
		save(this);
	}

	public void subtractAscensions(int amount)
	{
		this.ascensions -= amount;
		save(this);
	}

	public void setAscensions(int amount)
	{
		this.ascensions = amount;
		save(this);
	}

	public Integer getFavor()
	{
		return this.favor;
	}

	public void setFavor(int amount)
	{
		this.favor = amount;
		save(this);
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
		save(this);
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
		save(this);
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
		save(this);
	}

	public void setMaxFavor(int amount)
	{
		if(amount < 0) this.maxFavor = 0;
		if(amount > Demigods.config.getSettingInt("caps.favor")) this.maxFavor = Demigods.config.getSettingInt("caps.favor");
		else this.maxFavor = amount;
		save(this);
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

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
