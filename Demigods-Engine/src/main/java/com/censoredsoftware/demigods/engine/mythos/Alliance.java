package com.censoredsoftware.demigods.engine.mythos;

import com.censoredsoftware.demigods.engine.Demigods;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.Collection;

public interface Alliance
{
	@Override
	String toString();

	String getName();

	String getShortDescription();

	String getPermission();

	PermissionDefault getPermissionDefault();

	boolean isPlayable();

	public static class Util
	{
		public static Alliance valueOf(final String name)
		{
			try
			{
				return Iterables.find(Demigods.mythos().getAlliances(), new Predicate<Alliance>()
				{
					@Override
					public boolean apply(Alliance alliance)
					{
						return alliance.getName().equalsIgnoreCase(name);
					}
				});
			}
			catch(Exception ignored)
			{
				// ignored
			}
			return null;
		}

		public static Collection<Deity> getLoadedPlayableDeitiesInAlliance(final Alliance alliance)
		{
			return Collections2.filter(getLoadedDeitiesInAlliance(alliance), new Predicate<Deity>()
			{
				@Override
				public boolean apply(Deity d)
				{
					return d.getFlags().contains(Deity.Flag.PLAYABLE) && d.getAlliance().equals(alliance);
				}
			});
		}

		public static Collection<Deity> getLoadedMajorPlayableDeitiesInAllianceWithPerms(final Alliance alliance, final Player player)
		{
			return Collections2.filter(getLoadedDeitiesInAlliance(alliance), new Predicate<Deity>()
			{
				@Override
				public boolean apply(Deity d)
				{
					return player.hasPermission(d.getPermission()) && d.getFlags().contains(Deity.Flag.PLAYABLE) && d.getFlags().contains(Deity.Flag.MAJOR_DEITY) && d.getAlliance().equals(alliance);
				}
			});
		}

		public static Collection<Deity> getLoadedMajorPlayableDeitiesInAlliance(final Alliance alliance)
		{
			return Collections2.filter(getLoadedDeitiesInAlliance(alliance), new Predicate<Deity>()
			{
				@Override
				public boolean apply(Deity d)
				{
					return d.getFlags().contains(Deity.Flag.PLAYABLE) && d.getFlags().contains(Deity.Flag.MAJOR_DEITY) && d.getAlliance().equals(alliance);
				}
			});
		}

		public static Collection<Deity> getLoadedDeitiesInAlliance(final Alliance alliance)
		{
			return Collections2.filter(Demigods.mythos().getDeities(), new Predicate<Deity>()
			{
				@Override
				public boolean apply(Deity d)
				{
					return d.getAlliance().equals(alliance);
				}
			});
		}
	}
}
