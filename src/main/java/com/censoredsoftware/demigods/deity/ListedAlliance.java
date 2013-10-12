package com.censoredsoftware.demigods.deity;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.Collection;

public enum ListedAlliance
{
	/**
	 * Test Alliances
	 */
	TEST("Test", "A short description of the Tests.", "demigods.alliance.test", PermissionDefault.FALSE),

	/**
	 * Main Alliances
	 */
	GOD("God", "A short description of the Gods.", "demigods.alliance.god", PermissionDefault.TRUE), TITAN("Titan", "A short description of the Titans.", "demigods.alliance.titan", PermissionDefault.TRUE),

	/**
	 * Special Alliances
	 */
	FATE("Fate", "A short description of the Fates.", "demigods.alliance.fate", PermissionDefault.OP), DONOR("Donor", "A short description of the Donors.", "demigods.alliance.donor", PermissionDefault.TRUE);

	private String name, shortDescription, permission;
	private PermissionDefault permissionDefault;

	private ListedAlliance(String name, String shortDescription, String permission, PermissionDefault permissionDefault)
	{
		this.name = name;
		this.shortDescription = shortDescription;
		this.permission = permission;
		this.permissionDefault = permissionDefault;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public String getName()
	{
		return name;
	}

	public String getShortDescription()
	{
		return shortDescription;
	}

	public String getPermission()
	{
		return permission;
	}

	public PermissionDefault getPermissionDefault()
	{
		return permissionDefault;
	}

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
