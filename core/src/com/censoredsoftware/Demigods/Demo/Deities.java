package com.censoredsoftware.Demigods.Demo;

import com.censoredsoftware.Demigods.Demo.Deity.God.Zeus.Zeus;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;

public enum Deities
{
	ZEUS(new Zeus());

	private Deity deity;

	private Deities(Deity deity)
	{
		this.deity = deity;
	}

	public Deity getDeity()
	{
		return deity;
	}
}
