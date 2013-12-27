package com.censoredsoftware.demigods.exclusive.district;

import com.censoredsoftware.demigods.engine.deity.Alliance;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.google.common.collect.ImmutableCollection;

import javax.swing.plaf.synth.Region;

public abstract class AllianceDistrict implements District
{
	public abstract Alliance getAlliance();

	public abstract ImmutableCollection<Deity> getDeity(Region region);

	// TODO
}
