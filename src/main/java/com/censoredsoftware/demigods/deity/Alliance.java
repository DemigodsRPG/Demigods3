package com.censoredsoftware.demigods.deity;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.Collection;

public interface Alliance
{
	@Override
	public String toString();

	public String getName();

	public String getShortDescription();

	public String getPermission();

	public PermissionDefault getPermissionDefault();

	public static class Util
	{
		public static Collection<ListedDeity> getLoadedPlayableDeitiesInAlliance(final ListedAlliance alliance)
		{
			return Collections2.filter(getLoadedDeitiesInAlliance(alliance), new Predicate<ListedDeity>()
			{
				@Override
				public boolean apply(ListedDeity d)
				{
					return d.getFlags().contains(ListedDeity.Flag.PLAYABLE) && d.getAlliance().getName().equalsIgnoreCase(alliance.getName());
				}
			});
		}

		public static Collection<ListedDeity> getLoadedMajorPlayableDeitiesInAllianceWithPerms(final ListedAlliance alliance, final Player player)
		{
			return Collections2.filter(getLoadedDeitiesInAlliance(alliance), new Predicate<ListedDeity>()
			{
				@Override
				public boolean apply(ListedDeity d)
				{
					return player.hasPermission(d.getPermission()) && d.getFlags().contains(ListedDeity.Flag.PLAYABLE) && d.getFlags().contains(ListedDeity.Flag.MAJOR_DEITY) && d.getAlliance().getName().equalsIgnoreCase(alliance.getName());
				}
			});
		}

		public static Collection<ListedDeity> getLoadedMajorPlayableDeitiesInAlliance(final ListedAlliance alliance)
		{
			return Collections2.filter(getLoadedDeitiesInAlliance(alliance), new Predicate<ListedDeity>()
			{
				@Override
				public boolean apply(ListedDeity d)
				{
					return d.getFlags().contains(ListedDeity.Flag.PLAYABLE) && d.getFlags().contains(ListedDeity.Flag.MAJOR_DEITY) && d.getAlliance().getName().equalsIgnoreCase(alliance.getName());
				}
			});
		}

		public static Collection<ListedDeity> getLoadedDeitiesInAlliance(final ListedAlliance alliance)
		{
			return Collections2.filter(Collections2.transform(Sets.newHashSet(ListedDeity.values()), new Function<ListedDeity, ListedDeity>()
			{
				@Override
				public ListedDeity apply(ListedDeity d)
				{
					return d;
				}
			}), new Predicate<ListedDeity>()
			{
				@Override
				public boolean apply(ListedDeity d)
				{
					return d.getAlliance().getName().equalsIgnoreCase(alliance.getName());
				}
			});
		}
	}
}
