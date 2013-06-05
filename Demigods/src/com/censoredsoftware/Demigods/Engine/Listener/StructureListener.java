package com.censoredsoftware.Demigods.Engine.Listener;

import java.util.Set;

import org.bukkit.event.Listener;

import com.censoredsoftware.Demigods.Engine.Structure.Structure;
import com.censoredsoftware.Demigods.Engine.Structure.StructureInfo;
import com.google.common.collect.Sets;

public class StructureListener implements Listener
{
	private static Set<Structure> protectedBlocks = Sets.newHashSet();
	private static Set<Structure> noPvPZone = Sets.newHashSet();
	private static Set<Structure> noGriefingZone = Sets.newHashSet();
	private static Set<Structure> tributeLocation = Sets.newHashSet();
	private static Set<Structure> prayerLocation = Sets.newHashSet();

	public StructureListener(Set<Structure> structures)
	{
		for(Structure structure : structures)
		{
			for(StructureInfo.Flag flag : structure.getInfo().getFlags())
			{
				sortStructuresByFlag(structure, flag);
			}
		}
	}

	private void sortStructuresByFlag(Structure structure, StructureInfo.Flag flag)
	{
		switch(flag)
		{
			case PROTECTED_BLOCKS:
			{
				protectedBlocks.add(structure);
				return;
			}
			case NO_PVP_ZONE:
			{
				noPvPZone.add(structure);
				return;
			}
			case NO_GRIEFING_ZONE:
			{
				noGriefingZone.add(structure);
				return;
			}
			case TRIBUTE_LOCATION:
			{
				tributeLocation.add(structure);
				return;
			}
			case PRAYER_LOCATION:
			{
				prayerLocation.add(structure);
			}
		}
	}
}
