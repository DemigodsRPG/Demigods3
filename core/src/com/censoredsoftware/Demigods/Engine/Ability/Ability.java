package com.censoredsoftware.Demigods.Engine.Ability;

import org.bukkit.event.Listener;

public abstract class Ability
{
	private AbilityInfo info;
	private Listener listener;

	public Ability(AbilityInfo info, Listener listener)
	{
		this.info = info;
		this.listener = listener;
	}

	public AbilityInfo getInfo()
	{
		return info;
	}

	public Listener getListener()
	{
		return listener;
	}

	public enum Type
	{
		OFFENSE, DEFENSE, STEALTH, SUPPORT, PASSIVE, ULTIMATE
	}
}
