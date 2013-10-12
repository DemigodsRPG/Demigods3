package com.censoredsoftware.demigods.rome.deity;

import com.censoredsoftware.demigods.deity.Alliance;
import org.bukkit.permissions.PermissionDefault;

public enum RomanAlliance implements Alliance
{
	GOD("God", "The Gods.", "demigods.rome.alliance.god", PermissionDefault.TRUE, true);

	private String name, description, permission;
	private PermissionDefault permissionDefault;
	private boolean playable;

	private RomanAlliance(String name, String description, String permission, PermissionDefault permissionDefault, boolean playable)
	{
		this.name = name;
		this.description = description;
		this.permission = permission;
		this.permissionDefault = permissionDefault;
		this.playable = playable;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getShortDescription()
	{
		return description;
	}

	@Override
	public String getPermission()
	{
		return permission;
	}

	@Override
	public PermissionDefault getPermissionDefault()
	{
		return permissionDefault;
	}

	@Override
	public boolean isPlayable()
	{
		return playable;
	}
}
