package com.demigodsrpg.demigods.engine.entity.player.attribute;

import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.google.common.collect.Maps;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;

public class DemigodsPotionEffect extends DataAccess<UUID, DemigodsPotionEffect>
{
	private UUID id;
	private String type;
	private int duration;
	private int amplifier;
	private boolean ambience;

	private DemigodsPotionEffect()
	{}

	public DemigodsPotionEffect(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		type = conf.getString("type");
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

	private static final DataAccess<UUID, DemigodsPotionEffect> DATA_ACCESS = new DemigodsPotionEffect();

	public static DemigodsPotionEffect get(UUID id)
	{
		return DATA_ACCESS.getDirect(id);
	}

	public static DemigodsPotionEffect of(PotionEffect potion)
	{
		DemigodsPotionEffect effect = new DemigodsPotionEffect();
		effect.id = UUID.randomUUID();
		effect.type = potion.getType().getName();
		effect.duration = potion.getDuration();
		effect.amplifier = potion.getAmplifier();
		effect.ambience = potion.isAmbient();
		effect.save();
		return effect;
	}

	/**
	 * Returns a built PotionEffect.
	 */
	public PotionEffect getBukkitPotionEffect()
	{
		return new PotionEffect(getType(), getDuration(), getAmplifier(), isAmbient());
	}
}
