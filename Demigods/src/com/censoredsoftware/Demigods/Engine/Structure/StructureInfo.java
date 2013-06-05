package com.censoredsoftware.Demigods.Engine.Structure;

import java.util.Set;

public class StructureInfo
{
	private Set<Flag> flags;

	public StructureInfo(Set<Flag> flags)
	{
		this.flags = flags;
	}

	public Set<Flag> getFlags()
	{
		return flags;
	}

	public static enum Flag
	{
		PROTECTED_BLOCKS, NO_PVP_ZONE, NO_GRIEFING_ZONE, TRIBUTE_LOCATION, PRAYER_LOCATION;
	}
}
