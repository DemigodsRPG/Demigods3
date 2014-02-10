package com.censoredsoftware.demigods.engine.entity.player.attribute;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsCharacter;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public abstract class Death implements ConfigurationSerializable
{
	private UUID id;
	private long deathTime;
	private UUID killed, attacking;

	public Death(UUID killed)
	{
		deathTime = System.currentTimeMillis();
		id = UUID.randomUUID();
		this.killed = killed;
		save();
	}

	public Death(UUID killed, UUID attacking)
	{
		deathTime = System.currentTimeMillis();
		id = UUID.randomUUID();
		this.killed = killed;
		this.attacking = attacking;
		save();
	}

	public Death(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		deathTime = conf.getLong("deathTime");
		killed = UUID.fromString(conf.getString("killed"));
		if(conf.isString("attacking")) attacking = UUID.fromString(conf.getString("attacking"));
	}

	protected abstract void save();

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deathTime", deathTime);
		map.put("killed", killed.toString());
		if(attacking != null) map.put("attacking", attacking.toString());
		return map;
	}

	public UUID getId()
	{
		return id;
	}

	public long getDeathTime()
	{
		return deathTime;
	}

	public UUID getKilled()
	{
		return killed;
	}

	public UUID getAttacking()
	{
		return attacking;
	}

	public static Set<Death> getRecentDeaths(int seconds)
	{
		final long time = System.currentTimeMillis() - (seconds * 1000);
		return Sets.newHashSet(Iterables.filter(Iterables.concat(Collections2.transform(Demigods.getOnlineCharacters(), new Function<DemigodsCharacter, Collection<Death>>()
		{
			@Override
			public Collection<Death> apply(DemigodsCharacter character)
			{
				try
				{
					return character.getDeaths();
				}
				catch(java.lang.Exception ignored)
				{}
				return null;
			}
		})), new Predicate<Death>()
		{
			@Override
			public boolean apply(Death death)
			{
				return death.getDeathTime() >= time;
			}
		}));
	}

	public static Collection<DDeath> getRecentDeaths(DCharacter character, int seconds)
	{
		final long time = System.currentTimeMillis() - (seconds * 1000);
		return Collections2.filter(character.getDeaths(), new Predicate<DDeath>()
		{
			@Override
			public boolean apply(DDeath death)
			{
				return death.getDeathTime() >= time;
			}
		});
	}
}
