package com.censoredsoftware.Demigods.Engine.Object.Ability;

import java.util.List;

import org.bukkit.Material;

public class AbilityInfo
{
	private String deity, name, command, permission;
	private int cost, delay, repeat;
	private List<String> details;
	private Devotion.Type type;
	private Material weapon;

	public AbilityInfo(String deity, String name, String command, String permission, int cost, int delay, int repeat, List<String> details, Devotion.Type type)
	{
		this.deity = deity;
		this.name = name;
		this.command = command;
		this.permission = permission;
		this.cost = cost;
		this.delay = delay;
		this.repeat = repeat;
		this.details = details;
		this.type = type;
	}

	public AbilityInfo(String deity, String name, String command, String permission, int cost, int delay, int repeat, List<String> details, Devotion.Type type, Material weapon)
	{
		this.deity = deity;
		this.name = name;
		this.command = command;
		this.permission = permission;
		this.cost = cost;
		this.delay = delay;
		this.repeat = repeat;
		this.details = details;
		this.type = type;
		this.weapon = weapon;
	}

	public String getDeity()
	{
		return deity;
	}

	public String getName()
	{
		return name;
	}

	public String getCommand()
	{
		return command;
	}

	public String getPermission()
	{
		return permission;
	}

	public int getCost()
	{
		return cost;
	}

	public int getDelay()
	{
		return delay;
	}

	public int getRepeat()
	{
		return repeat;
	}

	public List<String> getDetails()
	{
		return details;
	}

	public Devotion.Type getType()
	{
		return type;
	}

	public Material getWeapon()
	{
		return weapon;
	}
}
