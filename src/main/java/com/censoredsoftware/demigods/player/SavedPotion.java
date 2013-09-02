package com.censoredsoftware.demigods.player;

import com.censoredsoftware.demigods.data.DataManager;
import com.google.common.collect.Maps;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;

public class SavedPotion implements ConfigurationSerializable
{
	private UUID id;
	private int type;
	private int duration;
	private int amplifier;
	private boolean ambience;

	public SavedPotion(PotionEffect potion)
	{
		id = UUID.randomUUID();
		type = potion.getType().getId();
		duration = potion.getDuration();
		amplifier = potion.getAmplifier();
		ambience = potion.isAmbient();
		DataManager.savedPotions.put(id, this);
	}

	public SavedPotion(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		type = conf.getInt("type");
		duration = conf.getInt("duration");
		amplifier = conf.getInt("amplifier");
		ambience = conf.getBoolean("ambience");
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = Maps.newHashMap();
		map.put("type", type);
		map.put("duration", duration);
		map.put("amplifier", amplifier);
		map.put("ambience", ambience);
		return map;
	}

	public UUID getId()
	{
		return id;
	}

	public PotionEffectType getType()
	{
		return PotionEffectType.getById(type);
	}

	public int getDuration()
	{
		return duration;
	}

	public int getAmplifier()
	{
		return amplifier;
	}

	public boolean isAmbient()
	{
		return ambience;
	}

	/**
	 * Returns a built PotionEffect.
	 */
	public PotionEffect toPotionEffect()
	{
		return new PotionEffect(getType(), getDuration(), getAmplifier(), isAmbient());
	}
}
