package com.censoredsoftware.demigods.engine.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import redis.clients.johm.JOhm;

import com.censoredsoftware.core.region.Region;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.element.Structure.Structure;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class Structures
{
	public static Structure.Save getStructureSave(Location location, boolean filter)
	{
		for(Structure.Save save : filterForRegion(location, loadAll(), filter))
		{
			if(save.getLocations().contains(location)) return save;
		}
		return null;
	}

	public static Set<Structure.Save> getStructuresInRegionalArea(Location location)
	{
		final Region center = Region.Util.getRegion(location);
		return new HashSet<Structure.Save>()
		{
			{
				for(Region region : center.getSurroundingRegions())
					addAll(getStructuresInSingleRegion(region));
			}
		};
	}

	public static Set<Structure.Save> getStructuresInSingleRegion(Region region)
	{
		return Sets.newHashSet((List) JOhm.find(Structure.Save.class, "region", region.toString()));
	}

	public static boolean partOfStructureWithType(Location location, String type, boolean filter)
	{
		for(Structure.Save save : filterForRegion(location, findAll("type", type), filter))
		{
			if(save.getLocations().contains(location)) return true;
		}
		return false;
	}

	public static boolean partOfStructureWithFlag(Location location, Structure.Flag flag, boolean filter)
	{
		for(Structure.Save save : filterForRegion(location, findAll("flags", flag.name()), filter))
		{
			if(save.getLocations().contains(location)) return true;
		}
		return false;
	}

	public static boolean isReferenceBlockWithFlag(Location location, Structure.Flag flag, boolean filter)
	{
		for(Structure.Save save : filterForRegion(location, findAll("flags", flag.name()), filter))
		{
			if(save.getLocations().contains(location)) return true;
		}
		return false;
	}

	public static boolean isClickableBlockWithFlag(Location location, Structure.Flag flag, boolean filter)
	{
		for(Structure.Save save : filterForRegion(location, findAll("flags", flag.name()), filter))
		{
			if(save.getClickableBlocks().contains(location)) return true;
		}
		return false;
	}

	public static boolean isInRadiusWithFlag(Location location, Structure.Flag flag, boolean filter)
	{
		return getInRadiusWithFlag(location, flag, filter) != null;
	}

	public static Structure.Save getInRadiusWithFlag(Location location, Structure.Flag flag, boolean filter)
	{
		for(Structure.Save save : filterForRegion(location, findAll("flags", flag.name()), filter))
		{
			if(save.getReferenceLocation().distance(location) <= save.getStructure().getRadius()) return save;
		}
		return null;
	}

	public static boolean isTrespassingInNoGriefingZone(Player player)
	{
		Location location = player.getLocation();
		if(Zones.zoneNoBuild(player, player.getLocation())) return true;
		if(isInRadiusWithFlag(location, Structure.Flag.NO_GRIEFING, true))
		{
			Structure.Save save = getInRadiusWithFlag(location, Structure.Flag.NO_GRIEFING, true);
			return !(save.getOwner() != null && save.getOwner().getId().equals(DPlayer.Util.getPlayer(player).getCurrent().getId()));
		}
		return false;
	}

	public static void regenerateStructures()
	{
		for(Structure.Save save : loadAll())
		{
			save.generate(false);
		}
	}

	public static Set<Structure.Save> filterForRegion(Location location, Set<Structure.Save> structures, boolean filter)
	{
		if(filter) return Sets.intersection(getStructuresInRegionalArea(location), structures);
		return structures;
	}

	public static Set<Structure> getStructuresWithFlag(final Structure.Flag flag)
	{
		return new HashSet<Structure>()
		{
			{
				for(Structure structure : Demigods.getLoadedStructures())
				{
					if(structure.getFlags().contains(flag)) add(structure);
				}
			}
		};
	}

	public static Set<Structure.Save> getStructuresSavesWithFlag(final Structure.Flag flag)
	{
		return new HashSet<Structure.Save>()
		{
			{
				for(Structure.Save save : findAll("flags", flag.name()))
				{
					add(save);
				}
			}
		};
	}

	/**
	 * Strictly checks the <code>reference</code> location to validate if the area is safe
	 * for automated generation.
	 * 
	 * @param reference the location to be checked
	 * @param area how big of an area (in blocks) to validate
	 * @return Boolean
	 */
	public static boolean canGenerateStrict(Location reference, int area)
	{
		Location location = reference.clone();
		location.subtract(0, 1, 0);
		location.add((area / 3), 0, (area / 2));

		// Check ground
		for(int i = 0; i < area; i++)
		{
			if(!location.getBlock().getType().isSolid()) return false;
			location.subtract(1, 0, 0);
		}

		// Check ground adjacent
		for(int i = 0; i < area; i++)
		{
			if(!location.getBlock().getType().isSolid()) return false;
			location.subtract(0, 0, 1);
		}

		// Check ground adjacent again
		for(int i = 0; i < area; i++)
		{
			if(!location.getBlock().getType().isSolid()) return false;
			location.add(1, 0, 0);
		}

		location.add(0, 1, 0);

		// Check air diagonally
		for(int i = 0; i < area + 1; i++)
		{
			if(!location.getBlock().getType().isTransparent()) return false;
			location.add(0, 1, 1);
			location.subtract(1, 0, 0);
		}

		return true;
	}

	public static Structure.Save load(Long id)
	{
		return JOhm.get(Structure.Save.class, id);
	}

	public static Set<Structure.Save> loadAll()
	{
		return JOhm.getAll(Structure.Save.class);
	}

	public static Set<Structure.Save> findAll(String label, Object value)
	{
		return Sets.newHashSet((List) JOhm.find(Structure.Save.class, label, value));
	}
}
