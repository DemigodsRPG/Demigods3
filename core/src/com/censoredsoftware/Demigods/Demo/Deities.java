package com.censoredsoftware.Demigods.Demo;

import com.censoredsoftware.Demigods.Demo.Deity.God.Zeus;
import com.censoredsoftware.Demigods.Demo.Deity.Titan.Prometheus;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Demigods;

public enum Deities implements Demigods.ListedDeity
{
	ZEUS(new Zeus()), PROMETHEUS(new Prometheus());

	private Deity deity;

	private Deities(Deity deity)
	{
		this.deity = deity;
	}

	@Override
	public Deity getDeity()
	{
		return deity;
	}
}
