package com.censoredsoftware.Demigods.Engine.Block;

import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedBlock;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedLocation;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedModelFactory;
import com.google.common.base.Objects;

@Model
public class Altar
{
	@Id
	private Long id;
	@Reference
	@Indexed
	private TrackedLocation center;
	@Attribute
	@Indexed
	private boolean active;
	@CollectionList(of = TrackedBlock.class)
	@Indexed
	private List<TrackedBlock> blocks;

	void setCenter(TrackedLocation center)
	{
		this.center = center;
	}

	/**
	 * Sets the active status of this Altar to <code>option</code>.
	 * 
	 * @param option the option to set.
	 */
	public void setActive(boolean option)
	{
		this.active = option;
	}

	public static void save(Altar altar)
	{
		DemigodsData.jOhm.save(altar);
	}

	public void delete()
	{
		DemigodsData.jOhm.delete(Altar.class, getId());
	}

	public static Altar load(Long id)
	{
		return DemigodsData.jOhm.get(Altar.class, id);
	}

	public static Set<Altar> loadAll()
	{
		Set<Altar> altars = DemigodsData.jOhm.getAll(Altar.class);
		return altars;
	}

	/**
	 * Removes the Altar completely.
	 */
	public void remove()
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

	/**
	 * Returns the location of the Altar.
	 * 
	 * @return Location
	 */
	public Location getLocation()
	{
		return this.center.toLocation();
	}

	/**
	 * Returns true if the Altar is marked as active.
	 * 
	 * @return boolean
	 */
	public boolean isActive()
	{
		return active;
	}

	public List<TrackedBlock> getBlocks()
	{
		return this.blocks;
	}

	/**
	 * Returns true if the <code>location</code> matches a location within the Altar.
	 * 
	 * @param location the location to check.
	 * @return boolean
	 */
	public boolean locationMatches(Location location)
	{
		for(TrackedBlock block : this.blocks)
		{
			if(block.getLocation().equals(location)) return true;
		}
		return false;
	}

	/**
	 * Generates a full Altar structure.
	 */
	static void generateNewBlocks(Altar altar, Location location)
	{
		// Remove the emerald block
		location.getBlock().setTypeId(0);

		// Split the location so we can build off of it
		double locX = location.getX();
		double locY = location.getY();
		double locZ = location.getZ();
		World locWorld = location.getWorld();

		// Create the enchantment table
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY + 2, locZ), "altar", Material.ENCHANTMENT_TABLE));

		// Create magical table stand
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY + 1, locZ), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 2, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 2, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 2, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 2, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 2, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 2, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 2, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 2, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY + 6, locZ), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 1, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 1, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 1, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 1, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 1, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 1, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 3, locY, locZ + 3), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 3, locY, locZ - 3), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 3, locY, locZ - 3), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 3, locY, locZ + 3), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 2, locY + 3, locZ + 2), "altar", Material.getMaterial(44), (byte) 13));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 2, locY + 3, locZ - 2), "altar", Material.getMaterial(44), (byte) 13));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 2, locY + 3, locZ - 2), "altar", Material.getMaterial(44), (byte) 13));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 2, locY + 3, locZ + 2), "altar", Material.getMaterial(44), (byte) 13));

		// Left beam
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 1, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY + 4, locZ - 2), "altar", Material.getMaterial(98), (byte) 3));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 1, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 1, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 1, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));

		// Right beam
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 1, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY + 4, locZ + 2), "altar", Material.getMaterial(98), (byte) 3));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 1, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 1, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 1, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));

		// Top beam
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 2, locY + 4, locZ + 1), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 2, locY + 4, locZ), "altar", Material.getMaterial(98), (byte) 3));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 2, locY + 4, locZ - 1), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 2, locY + 5, locZ + 1), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 2, locY + 5, locZ), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + 2, locY + 5, locZ - 1), "altar", Material.getMaterial(126), (byte) 1));

		// Bottom beam
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 2, locY + 4, locZ + 1), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 2, locY + 4, locZ), "altar", Material.getMaterial(98), (byte) 3));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 2, locY + 4, locZ - 1), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 2, locY + 5, locZ + 1), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 2, locY + 5, locZ), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - 2, locY + 5, locZ - 1), "altar", Material.getMaterial(126), (byte) 1));

		// Set locations to use for building
		Location topLeft = new Location(locWorld, locX + 2, locY + 1, locZ - 2);
		Location topRight = new Location(locWorld, locX + 2, locY + 1, locZ + 2);
		Location botLeft = new Location(locWorld, locX - 2, locY + 1, locZ - 2);
		Location botRight = new Location(locWorld, locX - 2, locY + 1, locZ + 2);

		// Top left of platform
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topLeft, "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topLeft.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topLeft.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topLeft.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));

		// Top right of platform
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topRight, "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topRight.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topRight.subtract(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topRight.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));

		// Bottom left of platform
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botLeft, "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botLeft.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botLeft.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botLeft.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));

		// Bottom right of platform
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botRight, "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botRight.subtract(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botRight.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botRight.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));

		// Create central structure of platform
		for(int i = 1; i < 3; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY + 1, locZ + i), "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 3; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX, locY + 1, locZ - i), "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 3; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX - i, locY + 1, locZ), "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 3; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(new Location(locWorld, locX + i, locY + 1, locZ), "altar", Material.getMaterial(44), (byte) 5));

		// Build steps on all sides.
		Location leftSteps = new Location(locWorld, locX + 2, locY, locZ - 4);
		Location rightSteps = new Location(locWorld, locX + 2, locY, locZ + 4);
		Location topSteps = new Location(locWorld, locX + 4, locY, locZ - 2);
		Location botSteps = new Location(locWorld, locX - 4, locY, locZ - 2);

		// Create left steps
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(leftSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(leftSteps.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(leftSteps.add(0, 0, 1), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(leftSteps.add(1, 0, 0), "altar", Material.getMaterial(98)));

		// Create right steps
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(rightSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(rightSteps.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(rightSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(rightSteps.add(1, 0, 0), "altar", Material.getMaterial(98)));

		// Create top steps
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topSteps.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topSteps.subtract(1, 0, 0), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)));

		// Create bottom steps
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botSteps.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botSteps.add(1, 0, 0), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)));

		// Create left step towers
		for(int i = 0; i < 3; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(leftSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(leftSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(leftSteps.subtract(4, 0, 0), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(leftSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(leftSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));

		// Create right step towers
		for(int i = 0; i < 3; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(rightSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(rightSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(rightSteps.subtract(4, 0, 0), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(rightSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(rightSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));

		// Create top step towers
		for(int i = 0; i < 3; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topSteps.add(0, 0, 4), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(topSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));

		// Create bottom step towers
		for(int i = 0; i < 3; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botSteps.add(0, 0, 4), "altar", Material.getMaterial(98)));
		altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
			altar.getBlocks().add(TrackedModelFactory.createTrackedBlock(botSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));
	}

	@Override
	public boolean equals(final Object obj)
	{
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;
		final Altar other = (Altar) obj;
		return Objects.equal(this.id, other.id) && Objects.equal(this.center, other.center) && Objects.equal(this.active, other.active) && Objects.equal(this.blocks, other.blocks);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(id, center, active, blocks);
	}

	@Override
	public String toString()
	{
		return Objects.toStringHelper(this).add("id", id).add("center", center).add("active", active).add("blocks", blocks).toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
