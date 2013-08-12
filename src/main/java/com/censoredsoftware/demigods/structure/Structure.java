package com.censoredsoftware.demigods.structure;

import java.util.*;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.Listener;

import redis.clients.johm.*;

import com.censoredsoftware.core.region.Region;
import com.censoredsoftware.core.util.Randoms;
import com.censoredsoftware.demigods.Elements;
import com.censoredsoftware.demigods.exception.BlockDataException;
import com.censoredsoftware.demigods.location.DLocation;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.util.Structures;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.*;

public interface Structure
{
	public String getStructureType();

	public Design getDesign(String name);

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
		@Id
		private Long id;
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
		@Indexed
		@Attribute
		private String design;
		@Indexed
		@Attribute
		private Boolean active;
		@Indexed
		@Reference
		private DCharacter owner;

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
			save();
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
			return getStructure().getDesign(this.design).getClickableBlocks(this.reference.toLocation());
		}

		public Set<Location> getLocations()
		{
			return getStructure().getDesign(this.design).getSchematic().getLocations(this.reference.toLocation());
		}

		public Structure getStructure()
		{
			for(Elements.ListedStructure structure : Elements.Structures.values())
				if(structure.getStructure().getStructureType().equalsIgnoreCase(this.type)) return structure.getStructure();
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
			return getStructure().getDesign(this.design).getSchematic().generate(this.reference.toLocation(), check);
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
		private int X, Y, Z, XX, YY, ZZ;
		private int eX, eY, eZ, eXX, eYY, eZZ;
		private boolean cuboid;
		private boolean exclude;
		private boolean excludeSelection;
		private List<BlockData> blockData;

		/**
		 * Constructor for a Selection (non-cuboid), useful for getting 1 location back.
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 */
		public Selection(int X, int Y, int Z)
		{
			this.X = this.XX = X;
			this.Y = this.YY = Y;
			this.Z = this.ZZ = Z;
			this.cuboid = false;
			this.exclude = false;
			this.excludeSelection = false;
			this.blockData = Lists.newArrayList(new BlockData(Material.AIR));
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
		public Selection(int X, int Y, int Z, int XX, int YY, int ZZ)
		{
			this.X = X;
			this.Y = Y;
			this.Z = Z;
			this.XX = XX;
			this.YY = YY;
			this.ZZ = ZZ;
			this.cuboid = true;
			this.exclude = false;
			this.excludeSelection = false;
			this.blockData = Lists.newArrayList(new BlockData(Material.AIR));
		}

		/**
		 * Constructor for a Selection (non-cuboid).
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 * @param material The BlockData objects of this schematic.
		 */
		public Selection(int X, int Y, int Z, Material material)
		{
			this.X = this.XX = X;
			this.Y = this.YY = Y;
			this.Z = this.ZZ = Z;
			this.cuboid = false;
			this.exclude = false;
			this.excludeSelection = false;
			this.blockData = Lists.newArrayList(new BlockData(material));
		}

		/**
		 * Constructor for a Selection (cuboid).
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 * @param XX The second relative X coordinate of the schematic from the reference location, creating a cuboid.
		 * @param YY The second relative Y coordinate of the schematic from the reference location, creating a cuboid.
		 * @param ZZ The second relative Z coordinate of the schematic from the reference location, creating a cuboid.
		 * @param material The BlockData objects of this schematic.
		 */
		public Selection(int X, int Y, int Z, int XX, int YY, int ZZ, Material material)
		{
			this.X = X;
			this.Y = Y;
			this.Z = Z;
			this.XX = XX;
			this.YY = YY;
			this.ZZ = ZZ;
			this.cuboid = true;
			this.exclude = false;
			this.excludeSelection = false;
			this.blockData = Lists.newArrayList(new BlockData(material));
		}

		/**
		 * Constructor for a Selection (non-cuboid).
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 * @param material The BlockData objects of this schematic.
		 */
		public Selection(int X, int Y, int Z, Material material, byte data)
		{
			this.X = this.XX = X;
			this.Y = this.YY = Y;
			this.Z = this.ZZ = Z;
			this.cuboid = false;
			this.exclude = false;
			this.excludeSelection = false;
			this.blockData = Lists.newArrayList(new BlockData(material, data));
		}

		/**
		 * Constructor for a Selection (cuboid).
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 * @param XX The second relative X coordinate of the schematic from the reference location, creating a cuboid.
		 * @param YY The second relative Y coordinate of the schematic from the reference location, creating a cuboid.
		 * @param ZZ The second relative Z coordinate of the schematic from the reference location, creating a cuboid.
		 * @param material The BlockData objects of this schematic.
		 */
		public Selection(int X, int Y, int Z, int XX, int YY, int ZZ, Material material, byte data)
		{
			this.X = X;
			this.Y = Y;
			this.Z = Z;
			this.XX = XX;
			this.YY = YY;
			this.ZZ = ZZ;
			this.cuboid = true;
			this.exclude = false;
			this.excludeSelection = false;
			this.blockData = Lists.newArrayList(new BlockData(material, data));
		}

		/**
		 * Constructor for a Selection (non-cuboid).
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 * @param blockData The BlockData objects of this schematic.
		 */
		public Selection(int X, int Y, int Z, BlockData blockData)
		{
			this.X = this.XX = X;
			this.Y = this.YY = Y;
			this.Z = this.ZZ = Z;
			this.cuboid = false;
			this.exclude = false;
			this.excludeSelection = false;
			this.blockData = Lists.newArrayList(blockData);
		}

		/**
		 * Constructor for a Selection (cuboid).
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 * @param XX The second relative X coordinate of the schematic from the reference location, creating a cuboid.
		 * @param YY The second relative Y coordinate of the schematic from the reference location, creating a cuboid.
		 * @param ZZ The second relative Z coordinate of the schematic from the reference location, creating a cuboid.
		 * @param blockData The BlockData objects of this schematic.
		 */
		public Selection(int X, int Y, int Z, int XX, int YY, int ZZ, BlockData blockData)
		{
			this.X = X;
			this.Y = Y;
			this.Z = Z;
			this.XX = XX;
			this.YY = YY;
			this.ZZ = ZZ;
			this.cuboid = true;
			this.exclude = false;
			this.excludeSelection = false;
			this.blockData = Lists.newArrayList(blockData);
		}

		/**
		 * Constructor for a Selection (non-cuboid).
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 * @param blockData The BlockData objects of this schematic.
		 */
		public Selection(int X, int Y, int Z, List<BlockData> blockData)
		{
			this.X = this.XX = X;
			this.Y = this.YY = Y;
			this.Z = this.ZZ = Z;
			this.cuboid = false;
			this.exclude = false;
			this.excludeSelection = false;
			this.blockData = blockData;
		}

		/**
		 * Constructor for a Selection (cuboid).
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 * @param XX The second relative X coordinate of the schematic from the reference location, creating a cuboid.
		 * @param YY The second relative Y coordinate of the schematic from the reference location, creating a cuboid.
		 * @param ZZ The second relative Z coordinate of the schematic from the reference location, creating a cuboid.
		 * @param blockData The BlockData objects of this schematic.
		 */
		public Selection(int X, int Y, int Z, int XX, int YY, int ZZ, List<BlockData> blockData)
		{
			this.X = X;
			this.Y = Y;
			this.Z = Z;
			this.XX = XX;
			this.YY = YY;
			this.ZZ = ZZ;
			this.cuboid = true;
			this.exclude = false;
			this.excludeSelection = false;
			this.blockData = blockData;
		}

		/**
		 * Constructor for a Selection (non-cuboid).
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 * @param material The BlockData objects of this schematic.
		 */
		public Selection(int X, int Y, int Z, Preset material)
		{
			this.X = this.XX = X;
			this.Y = this.YY = Y;
			this.Z = this.ZZ = Z;
			this.cuboid = false;
			this.exclude = false;
			this.excludeSelection = false;
			this.blockData = material.getData();
		}

		/**
		 * Constructor for a Selection (cuboid).
		 * 
		 * @param X The relative X coordinate of the schematic from the reference location.
		 * @param Y The relative Y coordinate of the schematic from the reference location.
		 * @param Z The relative Z coordinate of the schematic from the reference location.
		 * @param XX The second relative X coordinate of the schematic from the reference location, creating a cuboid.
		 * @param YY The second relative Y coordinate of the schematic from the reference location, creating a cuboid.
		 * @param ZZ The second relative Z coordinate of the schematic from the reference location, creating a cuboid.
		 * @param material The BlockData objects of this schematic.
		 */
		public Selection(int X, int Y, int Z, int XX, int YY, int ZZ, Preset material)
		{
			this.X = X;
			this.Y = Y;
			this.Z = Z;
			this.XX = XX;
			this.YY = YY;
			this.ZZ = ZZ;
			this.cuboid = true;
			this.exclude = false;
			this.excludeSelection = false;
			this.blockData = material.getData();
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
			this.eX = this.eXX = X;
			this.eY = this.eYY = Y;
			this.eZ = this.eZZ = Z;
			this.exclude = true;
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
			this.eX = X;
			this.eY = Y;
			this.eZ = Z;
			this.eXX = XX;
			this.eYY = YY;
			this.eZZ = ZZ;
			this.exclude = true;
			this.excludeSelection = true;
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
			if(cuboid)
			{
				if(exclude)
				{
					if(excludeSelection) return Sets.difference(rangeLoop(reference, X, XX, Y, YY, Z, ZZ), rangeLoop(reference, eX, eXX, eY, eYY, eZ, eZZ));
					return Sets.difference(rangeLoop(reference, X, XX, Y, YY, Z, ZZ), Sets.newHashSet(getLocation(reference, eX, eY, eZ)));
				}
				return rangeLoop(reference, X, XX, Y, YY, Z, ZZ);
			}
			return Sets.newHashSet(getLocation(reference, X, Y, Z));
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
		 * Get a relative location, based on the <code>X</code>, <code>Y</code>, <code>Z</code> coordinates relative to the object's central location.
		 * 
		 * @param X Relative X coordinate.
		 * @param Y Relative Y coordinate.
		 * @param Z Relative Z coordinate.
		 * @return New relative location.
		 */
		public Location getLocation(Location reference, int X, int Y, int Z)
		{
			return reference.clone().add(X, Y, Z);
		}

		/**
		 * Get a cuboid selection as a HashSet.
		 * 
		 * @param reference The reference location.
		 * @param X The relative X coordinate.
		 * @param XX The second relative X coordinate.
		 * @param Y The relative Y coordinate.
		 * @param YY The second relative Y coordinate.
		 * @param Z The relative Z coordinate.
		 * @param ZZ The second relative Z coordinate.
		 * @return The HashSet collection of a cuboid selection.
		 */
		public Set<Location> rangeLoop(final Location reference, final int X, final int XX, final int Y, final int YY, final int Z, final int ZZ)
		{
			return new HashSet<Location>()
			{
				{
					for(int x : Ranges.closed(X < XX ? X : XX, X < XX ? XX : X).asSet(DiscreteDomains.integers()))
						for(int y : Ranges.closed(Y < YY ? Y : YY, Y < YY ? YY : Y).asSet(DiscreteDomains.integers()))
							for(int z : Ranges.closed(Z < ZZ ? Z : ZZ, Z < ZZ ? ZZ : Z).asSet(DiscreteDomains.integers()))
								add(getLocation(reference, x, y, z));
				}
			};
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

		public static enum Preset
		{
			STONE_BRICK(new ArrayList<BlockData>(3)
			{
				{
					add(new BlockData(Material.SMOOTH_BRICK, 80));
					add(new BlockData(Material.SMOOTH_BRICK, (byte) 1, 10));
					add(new BlockData(Material.SMOOTH_BRICK, (byte) 2, 10));
				}
			}), SANDY_GRASS(new ArrayList<BlockData>(2)
			{
				{
					add(new BlockData(Material.SAND, 65));
					add(new BlockData(Material.GRASS, 35));
				}
			}), PRETTY_FLOWERS_AND_GRASS(new ArrayList<BlockData>(4)
			{
				{
					add(new BlockData(Material.AIR, 50));
					add(new BlockData(Material.LONG_GRASS, (byte) 1, 35, true));
					add(new BlockData(Material.YELLOW_FLOWER, 9, true));
					add(new BlockData(Material.RED_ROSE, 6, true));
				}
			}), VINE_1(new ArrayList<BlockData>(2)
			{
				{
					add(new BlockData(Material.VINE, (byte) 1, 40));
					add(new BlockData(Material.AIR, 60));
				}
			}), VINE_4(new ArrayList<BlockData>(2)
			{
				{
					add(new BlockData(Material.VINE, (byte) 4, 40));
					add(new BlockData(Material.AIR, 60));
				}
			});

			private List<BlockData> data;

			private Preset(List<BlockData> data)
			{
				this.data = data;
			}

			public List<BlockData> getData()
			{
				return data;
			}
		}
	}
}
