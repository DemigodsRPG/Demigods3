package com.censoredsoftware.demigods.engine.element.Structure;

import java.util.*;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.event.Listener;

import redis.clients.johm.*;

import com.censoredsoftware.core.region.Region;
import com.censoredsoftware.core.util.Randoms;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.exception.BlockDataException;
import com.censoredsoftware.demigods.engine.location.DLocation;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.util.Structures;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.*;

public interface Structure
{
	public String getStructureType();

	public Set<Structure.Flag> getFlags();

	public Set<Save> getAll();

	public Listener getUniqueListener();

	public int getRadius();

	public Save createNew(Location reference, boolean generate);

	public enum Flag
	{
		DELETE_WITH_OWNER, PROTECTED_BLOCKS, NO_GRIEFING, NO_PVP, PRAYER_LOCATION, TRIBUTE_LOCATION, SLOW_GEN
	}

	@Model
	public static class Save
	{
		/**
		 * JOhm
		 */
		@Id
		private Long id;

		/**
		 * Required
		 */
		@Indexed
		@Attribute
		private String type;
		@Reference
		private DLocation reference;
		@Indexed
		@CollectionSet(of = String.class)
		private Set<String> flags;
		@Indexed
		@Attribute
		private String region;

		/**
		 * Optional
		 */
		@Indexed
		@Attribute
		private String design;
		@Indexed
		@Attribute
		private Boolean active;
		@Indexed
		@Reference
		private DCharacter owner;
		@Indexed
		@Reference
		private MassiveStructure.Save parent;

		public void setType(String type)
		{
			this.type = type;
		}

		public void setDesign(String name)
		{
			this.design = name;
			save();
		}

		public void setReferenceLocation(Location reference)
		{
			this.reference = DLocation.Util.create(reference);
			setRegion(this.reference.getRegion());
		}

		public void setOwner(DCharacter character)
		{
			this.owner = character;
		}

		public void setActive(Boolean bool)
		{
			this.active = bool;
		}

		public Location getReferenceLocation()
		{
			return this.reference.toLocation();
		}

		public Set<Location> getClickableBlocks()
		{
			if(getStructure() instanceof StandaloneStructure) return ((StandaloneStructure) getStructure()).getDesign(this.design).getClickableBlocks(this.reference.toLocation());
			return null;
		}

		public Set<Location> getLocations()
		{
			if(getStructure() instanceof StandaloneStructure) return ((StandaloneStructure) getStructure()).getDesign(this.design).getSchematic().getLocations(this.reference.toLocation());
			if(getStructure() instanceof MassiveStructurePart) return ((MassiveStructurePart) getStructure()).getDesign(this.design).getSchematic().getLocations(this.reference.toLocation());
			return null;
		}

		public Structure getStructure()
		{
			for(Structure structure : Demigods.getLoadedStructures())
			{
				if(structure.getStructureType().equalsIgnoreCase(this.type)) return structure;
			}
			return null;
		}

		public Boolean hasOwner()
		{
			return this.owner != null;
		}

		public DCharacter getOwner()
		{
			return this.owner;
		}

		public Boolean getActive()
		{
			return this.active;
		}

		public void setRegion(Region region)
		{
			this.region = region.toString();
			save();
		}

		public void addFlags(Set<Structure.Flag> flags)
		{
			save();
			for(Structure.Flag flag : flags)
			{
				this.flags.add(flag.name());
			}
		}

		public void addFlag(Structure.Flag flag)
		{
			this.flags.add(flag.name());
			save();
		}

		public Region getRegion()
		{
			return this.reference.getRegion();
		}

		public Boolean hasFlag(Structure.Flag flag)
		{
			return this.flags != null && this.flags.contains(flag.name());
		}

		public Set<Structure.Flag> getFlags()
		{
			return new HashSet<Structure.Flag>()
			{
				{
					for(String flag : getRawFlags())
					{
						add(Structure.Flag.valueOf(flag));
					}
				}
			};
		}

		public Set<String> getRawFlags()
		{
			return this.flags;
		}

		public long getId()
		{
			return this.id;
		}

		public boolean generate(boolean check)
		{
			if(getStructure() instanceof StandaloneStructure) return ((StandaloneStructure) getStructure()).getDesign(this.design).getSchematic().generate(this.reference.toLocation(), check);
			if(getStructure() instanceof MassiveStructurePart) return ((MassiveStructurePart) getStructure()).getDesign(this.design).getSchematic().generate(this.reference.toLocation(), check);
			return ((MassiveStructure) getStructure()).generate(this.reference.toLocation());
		}

		public void save()
		{
			JOhm.save(this);
		}

		public void remove()
		{
			for(Location location : getLocations())
			{
				location.getBlock().setTypeId(Material.AIR.getId());
			}
			JOhm.delete(DLocation.class, reference.getId());
			JOhm.delete(Save.class, this.id);
		}

		@Override
		public String toString()
		{
			return Objects.toStringHelper(this).add("id", this.id).toString();
		}

		@Override
		public int hashCode()
		{
			return Objects.hashCode(id);
		}

		@Override
		public boolean equals(Object other)
		{
			return other != null && other instanceof Save && ((Save) other).getId() == getId();
		}
	}

	public static class Schematic extends ArrayList<Selection>
	{
		private final String name;
		private final String designer;
		private int radius;

		public Schematic(String name, String designer, int groundRadius)
		{
			this.name = name;
			this.designer = designer;
			this.radius = groundRadius;
		}

		public Set<Location> getLocations(Location reference)
		{
			Set<Location> locations = Sets.newHashSet();
			for(Selection cuboid : this)
				locations.addAll(cuboid.getBlockLocations(reference));
			return locations;
		}

		public int getGroundRadius()
		{
			return this.radius;
		}

		public boolean generate(final Location reference, boolean check)
		{
			if(check && !Structures.canGenerateStrict(reference, getGroundRadius())) return false;
			for(Selection cuboid : this)
				cuboid.generate(reference);
			for(Item drop : reference.getWorld().getEntitiesByClass(Item.class))
				if(reference.distance(drop.getLocation()) <= (getGroundRadius() * 3)) drop.remove();
			return true;
		}

		@Override
		public String toString()
		{
			return this.name;
		}
	}

	public interface Design
	{
		public String getName();

		public Set<Location> getClickableBlocks(Location reference);

		public Schematic getSchematic();
	}

	public static class Selection
	{
		private Set<RelativeBlockLocation> include = Sets.newHashSet();
		private Set<RelativeBlockLocation> exclude = Sets.newHashSet();
		private List<BlockData> blockData = Lists.newArrayList();

		/**
		 * Set Selection (non-cuboid), useful for getting 1 location back.
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 */
		public Selection include(int X, int Y, int Z)
		{
			include.add(new RelativeBlockLocation(X, Y, Z));
			return this;
		}

		/**
		 * Constructor for a Selection (cuboid), useful for getting only locations back.
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 * @param XX The second relative X coordinate of the schematic from the reference location, creating a cuboid.
		 * @param YY The second relative Y coordinate of the schematic from the reference location, creating a cuboid.
		 * @param ZZ The second relative Z coordinate of the schematic from the reference location, creating a cuboid.
		 */
		public Selection include(int X, int Y, int Z, int XX, int YY, int ZZ)
		{
			include.addAll(rangeLoop(X, XX, Y, YY, Z, ZZ));
			return this;
		}

		/**
		 * Excluding for a Selection (non-cuboid).
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 * @return This schematic.
		 */
		public Selection exclude(int X, int Y, int Z)
		{
			exclude.add(new RelativeBlockLocation(X, Y, Z));
			return this;
		}

		/**
		 * Excluding for a Selection (cuboid).
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 * @param XX The second relative X coordinate of the schematic from the reference location, creating a cuboid.
		 * @param YY The second relative Y coordinate of the schematic from the reference location, creating a cuboid.
		 * @param ZZ The second relative Z coordinate of the schematic from the reference location, creating a cuboid.
		 * @return This schematic.
		 */
		public Selection exclude(int X, int Y, int Z, int XX, int YY, int ZZ)
		{
			exclude.addAll(rangeLoop(X, XX, Y, YY, Z, ZZ));
			return this;
		}

		/**
		 * Set Blockdata for a Selection (cuboid).
		 * 
		 * @param data The data being set.
		 * @return This schematic.
		 */
		public Selection setBlockData(BlockData data)
		{
			this.blockData = Lists.newArrayList(data);
			return this;
		}

		/**
		 * Set Blockdata for a Selection (cuboid).
		 * 
		 * @param data The data being set.
		 * @return This schematic.
		 */
		public Selection setBlockData(List<BlockData> data)
		{
			this.blockData = data;
			return this;
		}

		/**
		 * Set Blockdata for a Selection (cuboid).
		 * 
		 * @param data The data being set.
		 * @return This schematic.
		 */
		public Selection setLayer(BlockData data)
		{
			this.blockData = Lists.newArrayList(data);
			return this;
		}

		/**
		 * Get the material of the object (a random material is chosen based on the configured odds).
		 * 
		 * TODO This method needs work, I'm not sure this is the more efficient way to do what we want.
		 * 
		 * @return A material.
		 */
		public BlockData getStructureBlockData()
		{
			if(blockData.isEmpty()) return new BlockData(Material.AIR);
			final int roll = Randoms.generateIntRange(1, 100);
			Collection<BlockData> check = Collections2.filter(blockData, new Predicate<BlockData>()
			{
				@Override
				public boolean apply(@Nullable BlockData blockData)
				{
					return blockData.getOdds() >= roll;
				}
			});
			if(check.isEmpty()) return getStructureBlockData();
			return Lists.newArrayList(check).get(Randoms.generateIntRange(0, check.size() - 1));
		}

		/**
		 * Get the block locations in this object.
		 * 
		 * @param reference The reference location.
		 * @return A set of locations.
		 */
		public Set<Location> getBlockLocations(final Location reference)
		{
			return new HashSet<Location>()
			{
				{
					for(RelativeBlockLocation rbl : Sets.difference(include, exclude))
						add(rbl.getFrom(reference));
				}
			};
		}

		/**
		 * Generate this schematic.
		 * 
		 * @param reference The reference Location.
		 */
		public void generate(Location reference)
		{
			for(Location location : getBlockLocations(reference))
			{
				BlockData data = getStructureBlockData();
				location.getBlock().setTypeIdAndData(data.getMaterial().getId(), data.getData(), data.getPhysics());
			}
		}

		/**
		 * Generate this schematic.
		 * 
		 * @param reference The reference Location.
		 */
		public void slowGenerate(Location reference)
		{
			// TODO Figure out how to order every falling block so that they fit perfectly.

			for(Location location : getBlockLocations(reference))
			{
				BlockData data = getStructureBlockData();
				FallingBlock block = reference.getWorld().spawnFallingBlock(reference, data.getMaterial().getId(), data.getData());
			}
		}

		/**
		 * Get a cuboid selection as a HashSet.
		 * 
		 * @param X The relative X coordinate.
		 * @param XX The second relative X coordinate.
		 * @param Y The relative Y coordinate.
		 * @param YY The second relative Y coordinate.
		 * @param Z The relative Z coordinate.
		 * @param ZZ The second relative Z coordinate.
		 * @return The HashSet collection of a cuboid selection.
		 */
		public Set<RelativeBlockLocation> rangeLoop(final int X, final int XX, final int Y, final int YY, final int Z, final int ZZ)
		{
			return new HashSet<RelativeBlockLocation>()
			{
				{
					for(int x : Ranges.closed(X < XX ? X : XX, X < XX ? XX : X).asSet(DiscreteDomains.integers()))
						for(int y : Ranges.closed(Y < YY ? Y : YY, Y < YY ? YY : Y).asSet(DiscreteDomains.integers()))
							for(int z : Ranges.closed(Z < ZZ ? Z : ZZ, Z < ZZ ? ZZ : Z).asSet(DiscreteDomains.integers()))
								add(new RelativeBlockLocation(x, y, z));
				}
			};
		}

		public static class RelativeBlockLocation
		{
			int X;
			int Y;
			int Z;

			public RelativeBlockLocation(int X, int Y, int Z)
			{
				this.X = X;
				this.Y = Y;
				this.Z = Z;
			}

			public Location getFrom(Location location)
			{
				return location.clone().add(X, Y, Z);
			}
		}

		public static class BlockData
		{
			private Material material;
			private byte data;
			private int odds;
			private boolean physics;

			/**
			 * Constructor for BlockData with only Material given.
			 * 
			 * @param material Material of the block.
			 */
			public BlockData(Material material)
			{
				this.material = material;
				this.data = 0;
				this.odds = 100;
				this.physics = false;
			}

			/**
			 * Constructor for BlockData with only Material given.
			 * 
			 * @param material Material of the block.
			 */
			public BlockData(Material material, boolean physics)
			{
				this.material = material;
				this.data = 0;
				this.odds = 100;
				this.physics = physics;
			}

			/**
			 * Constructor for BlockData with only Material given and odds given.
			 * 
			 * @param material Material of the block.
			 * @param odds The odds of this object being generated.
			 */
			public BlockData(Material material, int odds)
			{
				if(odds == 0 || odds > 100) throw new BlockDataException();
				this.material = material;
				this.data = 100;
				this.odds = odds;
				this.physics = false;
			}

			/**
			 * Constructor for BlockData with only Material given and odds given.
			 * 
			 * @param material Material of the block.
			 * @param odds The odds of this object being generated.
			 */
			public BlockData(Material material, int odds, boolean physics)
			{
				if(odds == 0 || odds > 100) throw new BlockDataException();
				this.material = material;
				this.data = 100;
				this.odds = odds;
				this.physics = physics;
			}

			/**
			 * Constructor for BlockData with only Material and byte data given.
			 * 
			 * @param material Material of the block.
			 * @param data Byte data of the block.
			 */
			public BlockData(Material material, byte data)
			{
				this.material = material;
				this.data = data;
				this.odds = 100;
				this.physics = false;
			}

			/**
			 * Constructor for BlockData with only Material and byte data given.
			 * 
			 * @param material Material of the block.
			 * @param data Byte data of the block.
			 */
			public BlockData(Material material, byte data, boolean physics)
			{
				this.material = material;
				this.data = data;
				this.odds = 100;
				this.physics = physics;
			}

			/**
			 * Constructor for BlockData with Material, byte data, and odds given.
			 * 
			 * @param material Material of the block.
			 * @param data Byte data of the block.
			 * @param odds The odds of this object being generated.
			 */
			public BlockData(Material material, byte data, int odds)
			{
				if(odds == 0 || odds > 100) throw new BlockDataException();
				this.material = material;
				this.data = data;
				this.odds = odds;
				this.physics = false;
			}

			/**
			 * Constructor for BlockData with Material, byte data, and odds given.
			 * 
			 * @param material Material of the block.
			 * @param data Byte data of the block.
			 * @param odds The odds of this object being generated.
			 */
			public BlockData(Material material, byte data, int odds, boolean physics)
			{
				if(odds == 0 || odds > 100) throw new BlockDataException();
				this.material = material;
				this.data = data;
				this.odds = odds;
				this.physics = physics;
			}

			/**
			 * Get the Material of this object.
			 * 
			 * @return A Material.
			 */
			public Material getMaterial()
			{
				return this.material;
			}

			/**
			 * Get the byte data of this object.
			 * 
			 * @return Byte data.
			 */
			public byte getData()
			{
				return this.data;
			}

			/**
			 * Get the odds of this object generating.
			 * 
			 * @return Odds (as an integer, out of 5).
			 */
			public int getOdds()
			{
				return this.odds;
			}

			/**
			 * Get the physics boolean.
			 * 
			 * @return If physics should apply on generation.
			 */
			public boolean getPhysics()
			{
				return this.physics;
			}
		}

		public static class BuildingBlock // TODO: Rename these to make more sense. // Shouldn't this be in the episode data? - HQM
		{
			public final static List<BlockData> enchantTable = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.ENCHANTMENT_TABLE));
				}
			};
			public final static List<BlockData> stoneBrick = new ArrayList<BlockData>(3)
			{
				{
					add(new BlockData(Material.SMOOTH_BRICK, 80));
					add(new BlockData(Material.SMOOTH_BRICK, (byte) 1, 10));
					add(new BlockData(Material.SMOOTH_BRICK, (byte) 2, 10));
				}
			};
			public final static List<BlockData> quartz = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.QUARTZ_BLOCK));
				}
			};
			public final static List<BlockData> pillarQuartz = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.QUARTZ_BLOCK, (byte) 2));
				}
			};
			public final static List<BlockData> stoneBrickSlabBottom = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.getMaterial(44), (byte) 5));
				}
			};
			public final static List<BlockData> stoneBrickSlabTop = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.getMaterial(44), (byte) 13));
				}
			};
			public final static List<BlockData> quartzSlabBottom = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.getMaterial(44), (byte) 7));
				}
			};
			public final static List<BlockData> quartzSlabTop = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.getMaterial(44), (byte) 15));
				}
			};
			public final static List<BlockData> stoneBrickSpecial = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.getMaterial(98), (byte) 3));
				}
			};
			public final static List<BlockData> quartzSpecial = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.QUARTZ_BLOCK, (byte) 1));
				}
			};
			public final static List<BlockData> spruceWood = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.getMaterial(5), (byte) 1));
				}
			};
			public final static List<BlockData> spruceSlab = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.getMaterial(126), (byte) 1));
				}
			};
			public final static List<BlockData> birchWood = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.getMaterial(5), (byte) 2));
				}
			};
			public final static List<BlockData> birchSlab = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.getMaterial(126), (byte) 2));
				}
			};
			public final static List<BlockData> sandStairNorth = new ArrayList<BlockData>()
			{
				{
					add(new BlockData(Material.getMaterial(128), (byte) 6));
				}
			};
			public final static List<BlockData> sandStairSouth = new ArrayList<BlockData>()
			{
				{
					add(new BlockData(Material.getMaterial(128), (byte) 7));
				}
			};
			public final static List<BlockData> sandStairEast = new ArrayList<BlockData>()
			{
				{
					add(new BlockData(Material.getMaterial(128), (byte) 5));
				}
			};
			public final static List<BlockData> sandStairWest = new ArrayList<BlockData>()
			{
				{
					add(new BlockData(Material.getMaterial(128), (byte) 4));
				}
			};
			public final static List<BlockData> smoothSandStone = new ArrayList<BlockData>()
			{
				{
					add(new BlockData(Material.SANDSTONE, (byte) 2));
				}
			};
			public final static List<BlockData> sandStone = new ArrayList<BlockData>()
			{
				{
					add(new BlockData(Material.SANDSTONE));
				}
			};
			public final static List<BlockData> sandyGrass = new ArrayList<BlockData>()
			{
				{
					add(new BlockData(Material.SAND, 65));
					add(new BlockData(Material.GRASS, 35));
				}
			};
			public final static List<BlockData> grass = new ArrayList<BlockData>()
			{
				{
					add(new BlockData(Material.GRASS));
				}
			};
			public final static List<BlockData> prettyFlowersAndGrass = new ArrayList<BlockData>()
			{
				{
					add(new BlockData(Material.AIR, 50));
					add(new BlockData(Material.LONG_GRASS, (byte) 1, 35, true));
					add(new BlockData(Material.YELLOW_FLOWER, 9, true));
					add(new BlockData(Material.RED_ROSE, 6, true));
				}
			};
			public final static List<BlockData> water = new ArrayList<BlockData>()
			{
				{
					add(new BlockData(Material.WATER));
				}
			};
			public final static List<BlockData> torch = new ArrayList<BlockData>()
			{
				{
					add(new BlockData(Material.TORCH));
				}
			};
			public final static List<BlockData> fence = new ArrayList<BlockData>()
			{
				{
					add(new BlockData(Material.FENCE));
				}
			};
			public final static List<BlockData> air = new ArrayList<BlockData>()
			{
				{
					add(new BlockData(Material.AIR));
				}
			};
			public final static List<BlockData> specialStoneBrick = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.SMOOTH_BRICK, (byte) 3));
				}
			};
			public final static List<BlockData> specialSandstone = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.SANDSTONE, (byte) 1));
				}
			};
			public final static List<BlockData> sandstone = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.SANDSTONE));
				}
			};
			public final static List<BlockData> redstoneBlock = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.REDSTONE_BLOCK));
				}
			};
			public final static List<BlockData> redstoneLamp = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.REDSTONE_LAMP_ON));
				}
			};
			public final static List<BlockData> vine1 = new ArrayList<BlockData>(2)
			{
				{
					add(new BlockData(Material.VINE, (byte) 1, 40));
					add(new BlockData(Material.AIR, 60));
				}
			};
			public final static List<BlockData> vine4 = new ArrayList<BlockData>(2)
			{
				{
					add(new BlockData(Material.VINE, (byte) 4, 40));
					add(new BlockData(Material.AIR, 60));
				}
			};
			public final static List<BlockData> goldClickBlock = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.GOLD_BLOCK));
				}
			};
			public final static List<BlockData> enderChest = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.ENDER_CHEST));
				}
			};
			public final static List<BlockData> stoneBrickStairs = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.SMOOTH_STAIRS));
				}
			};
			public final static List<BlockData> stoneBrickStairs1 = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.SMOOTH_STAIRS, (byte) 1));
				}
			};
			public final static List<BlockData> stoneBrickStairs2 = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.SMOOTH_STAIRS, (byte) 2));
				}
			};
			public final static List<BlockData> stoneBrickStairs3 = new ArrayList<BlockData>(1)
			{
				{
					add(new BlockData(Material.SMOOTH_STAIRS, (byte) 3));
				}
			};
		}
	}
}
