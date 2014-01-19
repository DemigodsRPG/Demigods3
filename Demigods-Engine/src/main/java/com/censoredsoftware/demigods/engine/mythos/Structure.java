package com.censoredsoftware.demigods.engine.mythos;

import com.censoredsoftware.censoredlib.data.location.CLocation;
import com.censoredsoftware.censoredlib.data.location.Region;
import com.censoredsoftware.censoredlib.schematic.Schematic;
import com.censoredsoftware.demigods.engine.data.serializable.DCharacter;
import com.censoredsoftware.demigods.engine.data.serializable.StructureSave;
import com.censoredsoftware.shaded.org.jgrapht.graph.DefaultWeightedEdge;
import com.censoredsoftware.shaded.org.jgrapht.graph.SimpleWeightedGraph;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.annotation.Nullable;
import java.util.*;

public interface Structure
{
	String getName();

	Design getDesign(final String name);

	Collection<Design> getDesigns();

	Collection<Flag> getFlags();

	Listener getUniqueListener();

	boolean sanctify(StructureSave data, DCharacter character);

	boolean corrupt(StructureSave data, DCharacter character);

	boolean birth(StructureSave data, DCharacter character);

	boolean kill(StructureSave data, DCharacter character);

	float getDefSanctity();

	float getSanctityRegen();

	int getRadius();

	int getRequiredGenerationCoords();

	boolean isAllowed(@Nullable StructureSave data, Player sender);

	StructureSave createNew(boolean generate, @Nullable String design, Location... reference);

	public interface Design
	{
		String getName();

		Set<Location> getClickableBlocks(Location reference);

		Schematic getSchematic(@Nullable StructureSave data);
	}

	public interface InteractFunction<T>
	{
		T apply(@Nullable StructureSave data, @Nullable DCharacter character);
	}

	public enum Flag
	{
		DELETE_WITH_OWNER, DESTRUCT_ON_BREAK, PROTECTED_BLOCKS, NO_GRIEFING, NO_PVP, PRAYER_LOCATION, OBELISK_LOCATION, TRIBUTE_LOCATION, RESTRICTED_AREA, NO_OVERLAP, STRUCTURE_WAND_GENERABLE;
	}

	public static class Util
	{
		public static StructureSave getStructureRegional(final Location location)
		{
			try
			{
				return Iterables.find(getStructuresInRegionalArea(location), new Predicate<StructureSave>()
				{
					@Override
					public boolean apply(StructureSave save)
					{
						return save.getLocations().contains(location);
					}
				});
			}
			catch(NoSuchElementException ignored)
			{}
			return null;
		}

		public static StructureSave getStructureGlobal(final Location location)
		{
			try
			{
				return Iterables.find(StructureSave.Util.loadAll(), new Predicate<StructureSave>()
				{
					@Override
					public boolean apply(StructureSave save)
					{
						return save.getLocations().contains(location);
					}
				});
			}
			catch(NoSuchElementException ignored)
			{}
			return null;
		}

		public static Set<StructureSave> getStructuresInRegionalArea(Location location)
		{
			final Region center = Region.Util.getRegion(location);
			Set<StructureSave> set = new HashSet<>();
			for(Region region : center.getSurroundingRegions())
				set.addAll(getStructuresInSingleRegion(region));
			return set;
		}

		public static Collection<StructureSave> getStructuresInSingleRegion(final Region region)
		{
			return StructureSave.Util.findAll(new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave save)
				{
					return save.getRegion().equals(region.toString());
				}
			});
		}

		public static boolean partOfStructureWithType(final Location location, final String type)
		{
			return Iterables.any(getStructuresInRegionalArea(location), new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave save)
				{
					return save.getTypeName().equals(type) && save.getLocations().contains(location);
				}
			});
		}

		public static boolean partOfStructureWithAllFlags(final Location location, final Flag... flags)
		{
			return Iterables.any(getStructuresInRegionalArea(location), new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave save)
				{
					return save.getRawFlags() != null && save.getLocations().contains(location) && save.getRawFlags().containsAll(Collections2.transform(Sets.newHashSet(flags), new Function<Flag, String>()
					{
						@Override
						public String apply(Flag flag)
						{
							return flag.name();
						}
					}));
				}
			});
		}

		public static boolean partOfStructureWithFlag(final Location location, final Flag... flags)
		{
			return Iterables.any(getStructuresInRegionalArea(location), new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave save)
				{
					if(save.getRawFlags() == null || !save.getLocations().contains(location)) return false;
					for(Flag flag : flags)
						if(save.getRawFlags().contains(flag.name())) return true;
					return false;
				}
			});
		}

		public static boolean partOfStructureWithFlag(final Location location, final Flag flag)
		{
			return Iterables.any(getStructuresInRegionalArea(location), new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave save)
				{
					return save.getRawFlags() != null && save.getRawFlags().contains(flag.name()) && save.getLocations().contains(location);
				}
			});
		}

		public static boolean isClickableBlockWithFlag(final Location location, final Flag flag)
		{
			return Iterables.any(getStructuresInRegionalArea(location), new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave save)
				{
					return save.getRawFlags() != null && save.getRawFlags().contains(flag.name()) && save.getClickableBlocks().contains(location);
				}
			});
		}

		public static boolean isInRadiusWithFlag(Location location, Flag flag)
		{
			return !getInRadiusWithFlag(location, flag).isEmpty();
		}

		public static Collection<StructureSave> getInRadiusWithFlag(final Location location, final Flag... flags)
		{
			return Collections2.filter(getStructuresInRegionalArea(location), new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave save)
				{
					if(save.getRawFlags() == null || !save.getReferenceLocation().getWorld().equals(location.getWorld()) || save.getReferenceLocation().distance(location) > save.getType().getRadius()) return false;
					for(Flag flag : flags)
						if(save.getRawFlags().contains(flag.name())) return true;
					return false;
				}
			});
		}

		public static Collection<StructureSave> getInRadiusWithFlag(final Location location, final Flag flag)
		{
			return Collections2.filter(getStructuresInRegionalArea(location), new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave save)
				{
					return save.getRawFlags() != null && save.getRawFlags().contains(flag.name()) && save.getReferenceLocation().getWorld().equals(location.getWorld()) && save.getReferenceLocation().distance(location) <= save.getType().getRadius();
				}
			});
		}

		public static StructureSave closestInRadiusWithFlag(final Location location, final Flag flag)
		{
			StructureSave found = null;
			double nearestDistance = Double.MAX_VALUE;
			for(StructureSave save : getStructuresInRegionalArea(location))
			{
				if(save.getRawFlags() != null && save.getRawFlags().contains(flag.name()))
				{
					double distance = save.getReferenceLocation().distance(location);
					if(distance <= save.getType().getRadius() && distance < nearestDistance)
					{
						found = save;
						nearestDistance = distance;
					}
				}
			}
			return found;
		}

		public static Set<StructureSave> getInRadiusWithFlag(final Location location, final Flag flag, final int radius)
		{
			return Sets.filter(getStructuresInRegionalArea(location), new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave save)
				{
					return save.getRawFlags() != null && save.getRawFlags().contains(flag.name()) && save.getReferenceLocation().getWorld().equals(location.getWorld()) && save.getReferenceLocation().distance(location) <= radius;
				}
			});
		}

		public static void regenerateStructures()
		{
			for(StructureSave save : StructureSave.Util.loadAll())
				save.generate();
		}

		public static Collection<StructureSave> getStructuresWithFlag(final Flag flag)
		{
			return StructureSave.Util.findAll(new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave save)
				{
					return save.getRawFlags() != null && save.getRawFlags().contains(flag.name());
				}
			});
		}

		public static Collection<StructureSave> getStructuresWithType(final String type)
		{
			return StructureSave.Util.findAll(new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave save)
				{
					return type.equals(save.getTypeName());
				}
			});
		}

		public static boolean noOverlapStructureNearby(Location location)
		{
			return Iterables.any(getStructuresInRegionalArea(location), new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave save)
				{
					return save.getRawFlags().contains(Flag.NO_OVERLAP.name());
				}
			});
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
				if(location.getBlock().getType().isSolid()) return false;
				location.add(0, 1, 1);
				location.subtract(1, 0, 0);
			}

			return true;
		}

		public static SimpleWeightedGraph<UUID, DefaultWeightedEdge> getGraphOfStructuresWithType(final Structure type)
		{
			return getGraphOfStructuresWithPredicate(new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave structureSave)
				{
					return structureSave.getType().equals(type);
				}
			}, type.getRadius() * 5); // FIXME Fine tune this distance.
		}

		public static SimpleWeightedGraph<UUID, DefaultWeightedEdge> getGraphOfStructuresWithFlag(final Flag flag, int distance)
		{
			return getGraphOfStructuresWithPredicate(new Predicate<StructureSave>()
			{
				@Override
				public boolean apply(StructureSave structureSave)
				{
					return structureSave.getType().getFlags().contains(flag);
				}
			}, distance);
		}

		public static SimpleWeightedGraph<UUID, DefaultWeightedEdge> getGraphOfStructuresWithPredicate(Predicate<StructureSave> predicate, final int distance)
		{
			SimpleWeightedGraph<UUID, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

			for(final StructureSave save : StructureSave.Util.findAll(predicate))
			{
				graph.addVertex(save.getId());

				for(StructureSave found : StructureSave.Util.findAll(new Predicate<StructureSave>()
				{
					@Override
					public boolean apply(StructureSave given)
					{
						return !given.equals(save) && CLocation.Util.distanceFlat(given.getReferenceLocation(), save.getReferenceLocation()) <= distance;
					}
				}))
				{
					graph.addVertex(found.getId());
					graph.addEdge(save.getId(), found.getId());
					graph.setEdgeWeight(graph.getEdge(save.getId(), found.getId()), CLocation.Util.distanceFlat(found.getReferenceLocation(), save.getReferenceLocation()));
				}
			}

			return graph;
		}

		/**
		 * Updates favor for all structures.
		 */
		public static void updateSanctity()
		{
			for(StructureSave data : getStructuresWithFlag(Flag.DESTRUCT_ON_BREAK))
				data.corrupt(-1F * data.getType().getSanctityRegen());
		}
	}
}
