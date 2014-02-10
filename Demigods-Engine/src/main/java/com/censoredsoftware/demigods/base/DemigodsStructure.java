package com.censoredsoftware.demigods.base;

import com.censoredsoftware.demigods.base.structure.RestrictedArea;
import com.censoredsoftware.demigods.engine.mythos.StructureType;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

// Structures
public enum DemigodsStructure
{
	INVISIBLE_WALL(new RestrictedArea());

	private final StructureType structureType;

	private DemigodsStructure(StructureType structureType)
	{
		this.structureType = structureType;
	}

	public StructureType getStructureType()
	{
		return structureType;
	}

	public static ImmutableSet<StructureType> structures()
	{
		return ImmutableSet.copyOf(Collections2.transform(Sets.newHashSet(values()), new Function<DemigodsStructure, StructureType>()
		{
			@Override
			public StructureType apply(DemigodsStructure dStructure)
			{
				return dStructure.getStructureType();
			}
		}));
	}
}
