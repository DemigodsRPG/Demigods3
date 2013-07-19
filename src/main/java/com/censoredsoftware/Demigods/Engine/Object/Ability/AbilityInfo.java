package com.censoredsoftware.Demigods.Engine.Object.Ability;

import java.util.List;

public class AbilityInfo
{
	private String deity, name, command, permission;
	private int cost, delay, repeat;
	private List<String> details;
	private Devotion.Type type;

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
}
