package com.censoredsoftware.demigods.engine.entity.player.attribute;

import com.google.common.collect.Maps;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;

public abstract class DemigodsPotionEffect implements ConfigurationSerializable
{
	private UUID id;
	private String type;
	private int duration;
	private int amplifier;
	private boolean ambience;

	public DemigodsPotionEffect(PotionEffect potion)
	{
		id = UUID.randomUUID();
		type = potion.getType().getName();
		duration = potion.getDuration();
		amplifier = potion.getAmplifier();
		ambience = potion.isAmbient();
		save();
	}

	public DemigodsPotionEffect(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		type = conf.getString("type");
		duration = conf.getInt("duration");
		amplifier = conf.getInt("amplifier");
		ambience = conf.getBoolean("ambience");
	}

	protected abstract void save();

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
		return PotionEffectType.getByName(type);
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
