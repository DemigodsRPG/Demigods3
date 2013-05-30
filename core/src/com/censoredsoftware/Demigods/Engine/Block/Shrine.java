package com.censoredsoftware.Demigods.Engine.Block;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.API.DeityAPI;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedBlock;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedLocation;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedModelFactory;
import com.google.common.base.Objects;

@Model
public class Shrine
{
	@Id
	private Long id;
	@Attribute
	@Indexed
	private boolean active;
	@Attribute
	@Indexed
	private long owner;
	@Attribute
	@Indexed
	private String deity;
	@Reference
	@Indexed
	private TrackedLocation center;
	@CollectionSet(of = TrackedBlock.class)
	@Indexed
	private Set<TrackedBlock> blocks;

	public static void save(Shrine shrine)
	{
		DemigodsData.jOhm.save(shrine);
	}

	public void delete()
	{
		DemigodsData.jOhm.delete(Shrine.class, getId());
	}

	public static Shrine load(Long id)
	{
		return DemigodsData.jOhm.get(Shrine.class, id);
	}

	public static Set<Shrine> loadAll()
	{
		return DemigodsData.jOhm.getAll(Shrine.class);
	}

	void setCenter(Location location)
	{
		this.center = TrackedModelFactory.createTrackedLocation(location);
	}

	void setOwner(PlayerCharacter character)
	{
		this.owner = character.getId();
	}

	void setDeity(Deity deity)
	{
		this.deity = deity.getInfo().getName();
	}

	public void setActive(boolean option)
	{
		this.active = option;
	}

	public synchronized void remove()
	{
		for(TrackedBlock block : this.blocks)
		{
			block.remove();
		}
		delete();
	}

	public Long getId()
	{
		return this.id;
	}

	public PlayerCharacter getOwner()
	{
		return CharacterAPI.getChar(this.owner);
	}

	public Deity getDeity()
	{
		return DeityAPI.getDeity(this.deity);
	}

	public Location getLocation()
	{
		return this.center.toLocation();
	}

	public boolean isActive()
	{
		return this.active;
	}

	public Set<TrackedBlock> getBlocks()
	{
		if(this.blocks == null) return new HashSet<TrackedBlock>();
		else return this.blocks;
	}

	public static synchronized void generate(Shrine shrine, Location location)
	{
		// Clear old blocks if they exist
		if(shrine.getBlocks() != null && !shrine.getBlocks().isEmpty())
		{
			for(TrackedBlock block : shrine.getBlocks())
			{
				if(block != null) TrackedBlock.delete(block.getId());
			}
		}

		// Create the set of blocks
		Set<TrackedBlock> blocks = new HashSet<TrackedBlock>();

		// Split the location so we can build off of it
		double locX = location.getX();
		double locY = location.getY();
		double locZ = location.getZ();
		World locWorld = location.getWorld();

		// Create the main block
		blocks.add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY + 1, locZ), "shrine", Material.GOLD_BLOCK));

		// Create the ender chest and the block below
		blocks.add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY, locZ), "shrine", Material.ENDER_CHEST));
		blocks.add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY - 0.5, locZ), "shrine", Material.SMOOTH_BRICK));

		// Create the rest
		blocks.add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 1, locY, locZ), "shrine", Material.SMOOTH_STAIRS, (byte) 1));
		blocks.add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 1, locY, locZ), "shrine", Material.SMOOTH_STAIRS, (byte) 0));
		blocks.add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY, locZ + 1), "shrine", Material.SMOOTH_STAIRS, (byte) 3));
		blocks.add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY, locZ - 1), "shrine", Material.SMOOTH_STAIRS, (byte) 2));

		// Add the blocks to the set
		shrine.getBlocks().addAll(blocks);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;
		final Shrine other = (Shrine) obj;
		return Objects.equal(this.id, other.id) && Objects.equal(this.center, other.center) && Objects.equal(this.active, other.active) && Objects.equal(this.blocks, other.blocks);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.id, this.center, this.active, this.blocks);
	}

	@Override
	public String toString()
	{
		return Objects.toStringHelper(this).add("id", this.id).add("center", this.center).add("active", this.active).add("blocks", this.blocks).toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
