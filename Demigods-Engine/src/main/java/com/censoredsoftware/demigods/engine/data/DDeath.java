package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.censoredlib.data.player.Death;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class DDeath extends Death
{

	public DDeath(DCharacter killed)
	{
		super(killed.getId());
	}

	public DDeath(DCharacter killed, DCharacter attacking)
	{
		super(killed.getId(), attacking.getId());
	}

	public DDeath(UUID id, ConfigurationSection conf)
	{
		super(id, conf);
	}

	@Override
	protected void save()
	{
		Util.save(this);
	}

	public static class Util
	{
		public static DDeath load(UUID id)
		{
			return Data.DEATH.get(id);
		}

		public static void save(DDeath death)
		{
			Data.DEATH.put(death.getId(), death);
		}

		public static void delete(UUID id)
		{
			Data.DEATH.remove(id);
		}

		public static Set<DDeath> getRecentDeaths(int seconds)
		{
			final long time = System.currentTimeMillis() - (seconds * 1000);
			return Sets.newHashSet(Iterables.filter(Iterables.concat(Collections2.transform(DCharacter.Util.getOnlineCharacters(), new Function<DCharacter, Collection<DDeath>>()
			{
				@Override
				public Collection<DDeath> apply(DCharacter character)
				{
					try
					{
						return character.getDeaths();
					}
					catch(java.lang.Exception ignored)
					{}
					return null;
				}
			})), new Predicate<DDeath>()
			{
				@Override
				public boolean apply(DDeath death)
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
}
