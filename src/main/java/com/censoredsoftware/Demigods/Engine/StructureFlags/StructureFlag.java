package com.censoredsoftware.Demigods.Engine.StructureFlags;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Flag;

public enum StructureFlag implements Demigods.ListedStructureFlag
{
	NO_GRIEFING(new NoGrief()), NO_PVP(new NoPvp()), PRAYER_LOCATION(null), TRIBUTE_LOCATION(null);

	private Flag flag;

	private StructureFlag(Flag flag)
	{
		this.flag = flag;
	}

	public Flag getFlag()
	{
		return this.flag;
	}
}
