package com.censoredsoftware.demigods.exclusive.district;

import com.censoredsoftware.censoredlib.data.location.Region;
import com.censoredsoftware.demigods.engine.data.StructureData;
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

	public ObeliskDistrict(StructureData mainObelisk, int maxObelisks, ImmutableCollection<Flag> flags)
	{
		this.mainObelisk = mainObelisk.getId();
		this.maxObelisks = maxObelisks;
		// TODO
	}

	public ObeliskDistrict(StructureData mainObelisk, int maxObelisks, Flag... flags)
	{

	}

	@Override
	public World getWorld()
	{
		return null;
	}

	@Override
	public Range<Region.X> rangeX()
	{
		return null;
	}

	@Override
	public Range<Region.Z> rangeZ()
	{
		return null;
	}

	@Override
	public ImmutableSet<Region> getRegions()
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
