package com.demigodsrpg.demigods.base;

import com.demigodsrpg.demigods.base.structure.RestrictedArea;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

// Structures
public enum DemigodsBaseStructure
{
	INVISIBLE_WALL(new RestrictedArea());

	private final DemigodsStructureType structureType;

	private DemigodsBaseStructure(DemigodsStructureType structureType)
	{
		this.structureType = structureType;
	}

	public DemigodsStructureType getStructureType()
	{
		return structureType;
	}

	public static ImmutableSet<DemigodsStructureType> structures()
	{
		return ImmutableSet.copyOf(Collections2.transform(Sets.newHashSet(values()), new Function<DemigodsBaseStructure, DemigodsStructureType>()
		{
			@Override
			public DemigodsStructureType apply(DemigodsBaseStructure dStructure)
			{
				return dStructure.getStructureType();
			}
		}));
	}
}
