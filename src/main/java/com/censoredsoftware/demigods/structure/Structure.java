package com.censoredsoftware.demigods.structure;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.Elements;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.exception.BlockDataException;
import com.censoredsoftware.demigods.helper.ConfigFile;
import com.censoredsoftware.demigods.location.DLocation;
import com.censoredsoftware.demigods.location.Region;
import com.censoredsoftware.demigods.util.Randoms;
import com.censoredsoftware.demigods.util.Structures;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Item;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public interface Structure
{
	public String getStructureType();

	public Design getDesign(String name);

	public Set<Structure.Flag> getFlags();

	public Collection<Save> getAll();

	public Listener getUniqueListener();

	public int getRadius();

	public Save createNew(Location reference, boolean generate);

	public enum Flag
	{
		DELETE_WITH_OWNER, PROTECTED_BLOCKS, NO_GRIEFING, NO_PVP, PRAYER_LOCATION, TRIBUTE_LOCATION, BATTLE_WALL
	}

	public static class Save implements ConfigurationSerializable
	{
		private UUID id;
		private String type;
		private UUID referenceLocation;
		private List<String> flags;
		private String region;
		private String design;
		private Boolean active;
		private UUID owner;
		private List<String> members;

		public Save()
		{}

		public Save(UUID id, ConfigurationSection conf)
		{
			this.id = id;
			type = conf.getString("type");
			referenceLocation = UUID.fromString(conf.getString("referenceLocation"));
			flags = conf.getStringList("flags");
			region = conf.getString("region");
			design = conf.getString("design");
			if(conf.getString("active") != null) active = conf.getBoolean("active");
			if(conf.getString("owner") != null) owner = UUID.fromString(conf.getString("owner"));
			if(conf.isList("members")) members = conf.getStringList("members");
		}

		@Override
		public Map<String, Object> serialize()
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", type);
			map.put("referenceLocation", referenceLocation.toString());
			map.put("flags", flags);
			map.put("region", region);
			map.put("design", design);
			if(active != null) map.put("active", active);
			if(owner != null) map.put("owner", owner.toString());
			if(members != null) map.put("members", members);
			return map;
		}

		public void generateId()
		{
			id = UUID.randomUUID();
		}

		public void setType(String type)
		{
			this.type = type;
		}

		public void setDesign(String name)
		{
			this.design = name;
		}

		public void setReferenceLocation(Location reference)
		{
			DLocation dLocation = DLocation.Util.create(reference);
			this.referenceLocation = dLocation.getId();
			setRegion(dLocation.getRegion());
		}

		public void setOwner(UUID id)
		{
			this.owner = id;
		}

		void setMembers(List<String> members)
		{
			this.members = members;
		}

		public void addMember(UUID id)
		{
			members.add(id.toString());
		}

		public void removeMember(UUID id)
		{
			members.remove(id.toString());
		}

		public void setActive(Boolean bool)
		{
			this.active = bool;
		}

		public Location getReferenceLocation()
		{
			return DLocation.Util.load(this.referenceLocation).toLocation();
		}

		public Set<Location> getClickableBlocks()
		{
			return getStructure().getDesign(this.design).getClickableBlocks(getReferenceLocation());
		}

		public Set<Location> getLocations()
		{
			return getStructure().getDesign(this.design).getSchematic().getLocations(getReferenceLocation());
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

		public UUID getOwner()
		{
			return this.owner;
		}

		public Boolean hasMembers()
		{
			return this.members != null && !members.isEmpty();
		}

		public Collection<UUID> getMembers()
		{
			return Collections2.transform(members, new Function<String, UUID>()
			{
				@Override
				public UUID apply(String s)
				{
					return UUID.fromString(s);
				}
			});
		}

		public String getType()
		{
			return type;
		}

		public Boolean getActive()
		{
			return this.active;
		}

		public void setRegion(Region region)
		{
			this.region = region.toString();
		}

		public String getRegion()
		{
			return region;
		}

		public void addFlags(Set<Structure.Flag> flags)
		{
			for(Structure.Flag flag : flags)
				getRawFlags().add(flag.name());
		}

		public List<String> getRawFlags()
		{
			if(this.flags == null) flags = Lists.newArrayList();
			return this.flags;
		}

		public UUID getId()
		{
			return this.id;
		}

		public boolean generate(boolean check)
		{
			return getStructure().getDesign(this.design).getSchematic().generate(getReferenceLocation(), check);
		}

		public void save()
		{
			DataManager.structures.put(getId(), this);
		}

		public void remove()
		{
			for(Location location : getLocations())
				location.getBlock().setTypeId(Material.AIR.getId());
			DLocation.Util.delete(referenceLocation);
			Structures.remove(id);
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

		public static class File extends ConfigFile
		{
			private static String SAVE_PATH;
			private static final String SAVE_FILE = "structures.yml";

			public File()
			{
				super(Demigods.plugin);
				SAVE_PATH = Demigods.plugin.getDataFolder() + "/data/";
			}

			@Override
			public ConcurrentHashMap<UUID, Save> loadFromFile()
			{
				final FileConfiguration data = getData(SAVE_PATH, SAVE_FILE);
				ConcurrentHashMap<UUID, Save> map = new ConcurrentHashMap<UUID, Save>();
				for(String stringId : data.getKeys(false))
					map.put(UUID.fromString(stringId), new Save(UUID.fromString(stringId), data.getConfigurationSection(stringId)));
				return map;
			}

			@Override
			public boolean saveToFile()
			{
				FileConfiguration saveFile = getData(SAVE_PATH, SAVE_FILE);
				Map<UUID, Save> currentFile = loadFromFile();

				for(UUID id : DataManager.structures.keySet())
					if(!currentFile.keySet().contains(id) || !currentFile.get(id).equals(DataManager.structures.get(id))) saveFile.createSection(id.toString(), Structures.load(id).serialize());

				for(UUID id : currentFile.keySet())
					if(!DataManager.structures.keySet().contains(id)) saveFile.set(id.toString(), null);

				return saveFile(SAVE_PATH, SAVE_FILE, saveFile);
			}
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
				public boolean apply(BlockData blockData)
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
			Set<Location> set = new HashSet<Location>();
			for(int x : Ranges.closed(X < XX ? X : XX, X < XX ? XX : X).asSet(DiscreteDomains.integers()))
				for(int y : Ranges.closed(Y < YY ? Y : YY, Y < YY ? YY : Y).asSet(DiscreteDomains.integers()))
					for(int z : Ranges.closed(Z < ZZ ? Z : ZZ, Z < ZZ ? ZZ : Z).asSet(DiscreteDomains.integers()))
						set.add(getLocation(reference, x, y, z));
			return set;
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
