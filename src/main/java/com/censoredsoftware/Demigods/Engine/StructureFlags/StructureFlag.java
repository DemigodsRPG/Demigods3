package com.censoredsoftware.Demigods.Engine.StructureFlags;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Flag;

public enum StructureFlag implements Demigods.ListedStructureFlag
{
	NO_GRIEFING(1, new NoGrief()), NO_PVP(2, new NoPvp()), PRAYER_LOCATION(3, null), TRIBUTE_LOCATION(4, null);

	private Integer id;
	private Flag flag;

	private StructureFlag(Integer id, Flag flag)
	{
		this.id = id;
		this.flag = flag;
	}

	public int getId()
	{
		return this.id;
	}

	public Flag getFlag()
	{
		return this.flag;
	}
}
