package com.censoredsoftware.demigods.base;

import com.censoredsoftware.demigods.base.structure.InvisibleWall;
import com.censoredsoftware.demigods.engine.mythos.Structure;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

// Structures
public enum DemigodsStructure
{
	INVISIBLE_WALL(new InvisibleWall());

	private final Structure structure;

	private DemigodsStructure(Structure structure)
	{
		this.structure = structure;
	}

	public Structure getStructure()
	{
		return structure;
	}

	public static ImmutableSet<Structure> structures()
	{
		return ImmutableSet.copyOf(Collections2.transform(Sets.newHashSet(values()), new Function<DemigodsStructure, Structure>()
		{
			@Override
			public Structure apply(DemigodsStructure dStructure)
			{
				return dStructure.getStructure();
			}
		}));
	}
}