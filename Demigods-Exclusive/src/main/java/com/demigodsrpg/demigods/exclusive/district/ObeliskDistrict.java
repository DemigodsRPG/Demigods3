package com.demigodsrpg.demigods.exclusive.district;

import com.demigodsrpg.demigods.engine.location.DemigodsRegion;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructure;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ObeliskDistrict implements District, ConfigurationSerializable
{
	private UUID mainObelisk;
	private int maxObelisks;
	private List<String> flags;
	private List<String> memberObelisks;

	public ObeliskDistrict(DemigodsStructure mainObelisk, int maxObelisks, ImmutableCollection<Flag> flags)
	{
		this.mainObelisk = mainObelisk.getId();
		this.maxObelisks = maxObelisks;
		// TODO
	}

	public ObeliskDistrict(DemigodsStructure mainObelisk, int maxObelisks, Flag... flags)
	{

	}

	@Override
	public World getWorld()
	{
		return null;
	}

	@Override
	public Range<DemigodsRegion.X> rangeX()
	{
		return null;
	}

	@Override
	public Range<DemigodsRegion.Z> rangeZ()
	{
		return null;
	}

	@Override
	public ImmutableSet<DemigodsRegion> getRegions()
	{
		return null;
	}

	@Override
	public Flag getFlags()
	{
		return null;
	}

	@Override
	public Map<String, Object> serialize()
	{
		return null;
	}
}
