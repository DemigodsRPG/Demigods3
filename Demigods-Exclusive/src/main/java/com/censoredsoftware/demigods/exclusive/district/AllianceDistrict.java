package com.censoredsoftware.demigods.exclusive.district;

import javax.swing.plaf.synth.Region;

import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.google.common.collect.ImmutableCollection;

public abstract class AllianceDistrict implements District
{
	public abstract Alliance getAlliance();

	public abstract ImmutableCollection<Deity> getDeity(Region region);

	// TODO
}
