package com.censoredsoftware.demigods.deity;

import org.bukkit.permissions.PermissionDefault;

public enum ListedAlliance implements Alliance
{
	/**
	 * Test Alliances
	 */
	TEST("Test", "A short description of the Tests.", "demigods.alliance.test", PermissionDefault.FALSE, false),

	/**
	 * Main Alliances
	 */
	GOD("God", "A short description of the Gods.", "demigods.alliance.god", PermissionDefault.TRUE, true), TITAN("Titan", "A short description of the Titans.", "demigods.alliance.titan", PermissionDefault.TRUE, true),

	/**
	 * Special Alliances
	 */
	FATE("Fate", "A short description of the Fates.", "demigods.alliance.fate", PermissionDefault.OP, true), DONOR("Donor", "A short description of the Donors.", "demigods.alliance.donor", PermissionDefault.TRUE, true);

	private String name, shortDescription, permission;
	private PermissionDefault permissionDefault;
	private boolean playable;

	private ListedAlliance(String name, String shortDescription, String permission, PermissionDefault permissionDefault, boolean playable)
	{
		this.name = name;
		this.shortDescription = shortDescription;
		this.permission = permission;
		this.permissionDefault = permissionDefault;
		this.playable = playable;
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

	public boolean isPlayable()
	{
		return playable;
	}
}
