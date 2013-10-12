package com.censoredsoftware.demigods.deity;

import org.bukkit.permissions.PermissionDefault;

public enum ListedAlliance implements Alliance
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
}
