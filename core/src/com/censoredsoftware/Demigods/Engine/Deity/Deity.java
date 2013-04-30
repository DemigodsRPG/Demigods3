package com.censoredsoftware.Demigods.Engine.Deity;

import java.util.List;

import com.censoredsoftware.Demigods.Engine.Ability.Ability;

public abstract class Deity
{
	private DeityInfo info;
	private List<Ability> abilities;

	public Deity(DeityInfo info, List<Ability> abilities)
	{
		this.info = info;
		this.abilities = abilities;
	}

	public DeityInfo getInfo()
	{
		return info;
	}

	public List<Ability> getAbilities()
	{
		return abilities;
	}

	public enum Type
	{
		DEMO, TIER1, TIER2, TIER3
	}
}
