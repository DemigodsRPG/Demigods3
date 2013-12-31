package com.censoredsoftware.demigods.engine.base;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashMap;

// Permissions
public enum DemigodsPermission
{
	BASIC(new Permission("demigods.basic", "The very basic permissions for Demigods.", PermissionDefault.TRUE, new HashMap<String, Boolean>()
	{
		{
			put("demigods.basic.create", true);
			put("demigods.basic.forsake", true);
		}
	})), ADMIN(new Permission("demigods.admin", "The admin permissions for Demigods.", PermissionDefault.OP)), PVP_AREA_COOLDOWN(new Permission("demigods.bypass.pvpareacooldown", "Bypass the wait for leaving/entering PVP zones.", PermissionDefault.FALSE));

	private Permission permission;

	private DemigodsPermission(Permission permission)
	{
		this.permission = permission;
	}

	public Permission getPermission()
	{
		return permission;
	}
}