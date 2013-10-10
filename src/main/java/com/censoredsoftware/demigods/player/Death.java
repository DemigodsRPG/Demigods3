package com.censoredsoftware.demigods.player;

import com.censoredsoftware.demigods.data.DataManager;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class Death implements ConfigurationSerializable
{
	private UUID id;
	private long deathTime;
	private UUID killed, attacking;

	public Death(DCharacter killed)
	{
		deathTime = System.currentTimeMillis();
		id = UUID.randomUUID();
		this.killed = killed.getId();
		Util.save(this);
	}

	public Death(DCharacter killed, DCharacter attacking)
	{
		deathTime = System.currentTimeMillis();
		id = UUID.randomUUID();
		this.killed = killed.getId();
		this.attacking = attacking.getId();
		Util.save(this);
	}

	public Death(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		deathTime = conf.getLong("deathTime");
		killed = UUID.fromString(conf.getString("killed"));
		if(conf.isString("attacking")) attacking = UUID.fromString(conf.getString("attacking"));
	}

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

	public static class Util
	{
		public static Death load(UUID id)
		{
			return DataManager.deaths.get(id);
		}

		public static void save(Death death)
		{
			DataManager.deaths.put(death.id, death);
		}

		public static void delete(UUID id)
		{
			DataManager.deaths.remove(id);
		}

		public static Set<Death> getRecentDeaths(int seconds)
		{
			final long time = System.currentTimeMillis() - (seconds * 1000);
			return Sets.newHashSet(Iterables.filter(Iterables.concat(Collections2.transform(DCharacter.Util.getOnlineCharacters(), new Function<DCharacter, Collection<Death>>()
			{
				@Override
				public Collection<Death> apply(DCharacter character)
				{
					try
					{
						return character.getDeaths();
					}
					catch(Exception ignored)
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

		public static Collection<Death> getRecentDeaths(DCharacter character, int seconds)
		{
			final long time = System.currentTimeMillis() - (seconds * 1000);
			return Collections2.filter(character.getDeaths(), new Predicate<Death>()
			{
				@Override
				public boolean apply(Death death)
				{
					return death.getDeathTime() >= time;
				}
			});
		}
	}
}
