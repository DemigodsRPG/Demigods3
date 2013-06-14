package com.censoredsoftware.Demigods.Engine.Object.Structure;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsBlock;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsBlockLocation;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsLocation;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsModelFactory;
import com.google.common.base.Objects;

@Model
public class Altar
{
	@Id
	private Long id;
	@Attribute
	@Indexed
	private boolean active;
	@Reference
	@Indexed
	private DemigodsLocation center;
	@CollectionSet(of = DemigodsBlock.class)
	@Indexed
	private Set<DemigodsBlock> blocks;

	void setCenter(Location location)
	{
		this.center = DemigodsModelFactory.createDemigodsLocation(location);
	}

	public void setActive(boolean option)
	{
		this.active = option;
	}

	public static void save(Altar altar)
	{
		JOhm.save(altar);
	}

	public void delete()
	{
		JOhm.delete(Altar.class, getId());
	}

	public static Altar load(Long id)
	{
		return JOhm.get(Altar.class, id);
	}

	public static Set<Altar> loadAll()
	{
		return JOhm.getAll(Altar.class);
	}

	public synchronized void remove()
	{
		for(DemigodsBlock block : this.blocks)
		{
			if(block != null) block.remove();
		}
		delete();
	}

	public Long getId()
	{
		return this.id;
	}

	public Location getLocation()
	{
		return this.center.toLocation();
	}

	public boolean isActive()
	{
		return active;
	}

	public Set<DemigodsBlock> getBlocks()
	{
		if(this.blocks == null) return new HashSet<DemigodsBlock>();
		else return this.blocks;
	}

	/**
	 * Generates a full Altar structure.
	 */
	public static synchronized void generate(Altar altar, Location location)
	{
		// Clear old blocks if they exist
		if(altar.getBlocks() != null && !altar.getBlocks().isEmpty())
		{
			for(DemigodsBlock block : altar.getBlocks())
			{
				if(block != null) block.remove();
			}
		}

		// Remove the emerald block
		location.getBlock().setTypeId(0);

		// Split the location so we can build off of it
		double locX = location.getX();
		double locY = location.getY();
		double locZ = location.getZ();
		World locWorld = location.getWorld();

		// Create the set of blocks
		Set<DemigodsBlock> blocks = new HashSet<DemigodsBlock>();

		// Create the enchantment table
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX, locY + 2, locZ), "altar", Material.ENCHANTMENT_TABLE));

		// Create magical table stand
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX, locY + 1, locZ), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 2, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 2, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 2, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 2, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 2, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 2, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 2, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 2, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX, locY + 6, locZ), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 1, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 1, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 1, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 1, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 1, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 1, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 3, locY, locZ + 3), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 3, locY, locZ - 3), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 3, locY, locZ - 3), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 3, locY, locZ + 3), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 2, locY + 3, locZ + 2), "altar", Material.getMaterial(44), (byte) 13));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 2, locY + 3, locZ - 2), "altar", Material.getMaterial(44), (byte) 13));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 2, locY + 3, locZ - 2), "altar", Material.getMaterial(44), (byte) 13));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 2, locY + 3, locZ + 2), "altar", Material.getMaterial(44), (byte) 13));

		// Left beam
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 1, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX, locY + 4, locZ - 2), "altar", Material.getMaterial(98), (byte) 3));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 1, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 1, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 1, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));

		// Right beam
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 1, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX, locY + 4, locZ + 2), "altar", Material.getMaterial(98), (byte) 3));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 1, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 1, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 1, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));

		// Top beam
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 2, locY + 4, locZ + 1), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 2, locY + 4, locZ), "altar", Material.getMaterial(98), (byte) 3));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 2, locY + 4, locZ - 1), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 2, locY + 5, locZ + 1), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 2, locY + 5, locZ), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + 2, locY + 5, locZ - 1), "altar", Material.getMaterial(126), (byte) 1));

		// Bottom beam
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 2, locY + 4, locZ + 1), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 2, locY + 4, locZ), "altar", Material.getMaterial(98), (byte) 3));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 2, locY + 4, locZ - 1), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 2, locY + 5, locZ + 1), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 2, locY + 5, locZ), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - 2, locY + 5, locZ - 1), "altar", Material.getMaterial(126), (byte) 1));

		// Set locations to use for building
		Location topLeft = new Location(locWorld, locX + 2, locY + 1, locZ - 2);
		Location topRight = new Location(locWorld, locX + 2, locY + 1, locZ + 2);
		Location botLeft = new Location(locWorld, locX - 2, locY + 1, locZ - 2);
		Location botRight = new Location(locWorld, locX - 2, locY + 1, locZ + 2);

		// Top left of platform
		blocks.add(DemigodsModelFactory.createDemigodsBlock(topLeft, "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(topLeft.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(topLeft.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(topLeft.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));

		// Top right of platform
		blocks.add(DemigodsModelFactory.createDemigodsBlock(topRight, "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(topRight.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(topRight.subtract(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(topRight.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));

		// Bottom left of platform
		blocks.add(DemigodsModelFactory.createDemigodsBlock(botLeft, "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(botLeft.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(botLeft.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(botLeft.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));

		// Bottom right of platform
		blocks.add(DemigodsModelFactory.createDemigodsBlock(botRight, "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(botRight.subtract(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(botRight.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(botRight.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));

		// Create central structure of platform
		for(int i = 1; i < 3; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX, locY + 1, locZ + i), "altar", Material.getMaterial(44), (byte) 5));
		}
		for(int i = 1; i < 3; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX, locY + 1, locZ - i), "altar", Material.getMaterial(44), (byte) 5));
		}
		for(int i = 1; i < 3; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX - i, locY + 1, locZ), "altar", Material.getMaterial(44), (byte) 5));
		}
		for(int i = 1; i < 3; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(new Location(locWorld, locX + i, locY + 1, locZ), "altar", Material.getMaterial(44), (byte) 5));
		}

		// Build steps on all sides.
		Location leftSteps = new Location(locWorld, locX + 2, locY, locZ - 4);
		Location rightSteps = new Location(locWorld, locX + 2, locY, locZ + 4);
		Location topSteps = new Location(locWorld, locX + 4, locY, locZ - 2);
		Location botSteps = new Location(locWorld, locX - 4, locY, locZ - 2);

		// Create left steps
		blocks.add(DemigodsModelFactory.createDemigodsBlock(leftSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(leftSteps.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		}
		blocks.add(DemigodsModelFactory.createDemigodsBlock(leftSteps.add(0, 0, 1), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(leftSteps.add(1, 0, 0), "altar", Material.getMaterial(98)));
		}

		// Create right steps
		blocks.add(DemigodsModelFactory.createDemigodsBlock(rightSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(rightSteps.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		}
		blocks.add(DemigodsModelFactory.createDemigodsBlock(rightSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(rightSteps.add(1, 0, 0), "altar", Material.getMaterial(98)));
		}

		// Create top steps
		blocks.add(DemigodsModelFactory.createDemigodsBlock(topSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(topSteps.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		}
		blocks.add(DemigodsModelFactory.createDemigodsBlock(topSteps.subtract(1, 0, 0), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(topSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)));
		}

		// Create bottom steps
		blocks.add(DemigodsModelFactory.createDemigodsBlock(botSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(botSteps.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		}
		blocks.add(DemigodsModelFactory.createDemigodsBlock(botSteps.add(1, 0, 0), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(botSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)));
		}

		// Create left step towers
		for(int i = 0; i < 3; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(leftSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		}
		blocks.add(DemigodsModelFactory.createDemigodsBlock(leftSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(leftSteps.subtract(4, 0, 0), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(leftSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(leftSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));
		}

		// Create right step towers
		for(int i = 0; i < 3; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(rightSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		}
		blocks.add(DemigodsModelFactory.createDemigodsBlock(rightSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(rightSteps.subtract(4, 0, 0), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(rightSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(rightSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));
		}

		// Create top step towers
		for(int i = 0; i < 3; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(topSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		}
		blocks.add(DemigodsModelFactory.createDemigodsBlock(topSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(topSteps.add(0, 0, 4), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(topSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(topSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));
		}

		// Create bottom step towers
		for(int i = 0; i < 3; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(botSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		}
		blocks.add(DemigodsModelFactory.createDemigodsBlock(botSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(botSteps.add(0, 0, 4), "altar", Material.getMaterial(98)));
		blocks.add(DemigodsModelFactory.createDemigodsBlock(botSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
		{
			blocks.add(DemigodsModelFactory.createDemigodsBlock(botSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));
		}

		// Add the blocks to the set
		altar.getBlocks().addAll(blocks);
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

	/**
	 * Returns true/false depending on if there is an Altar within <code>blocks</code> of <code>location</code>.
	 * 
	 * @param location the location used as the center to check from.
	 * @param blocks the radius of blocks to check with.
	 * 
	 * @return true if something
	 */
	public static boolean altarNearby(Location location, int blocks)
	{
		final Set<Altar> altars = getAllAltars();
		if(altars == null) return false;

		for(Altar altar : altars)
		{
			Location altarLocation = altar.getLocation();
			if(!altarLocation.getWorld().equals(location.getWorld())) continue;
			if(altarLocation.distance(location) <= blocks) return true;
		}
		return false;
	}

	/**
	 * Returns the Altar at the <code>location</code>.
	 * 
	 * @param location the location to check.
	 * @return the Altar at <code>location</code>.
	 */
	public static Altar getAltar(Location location)
	{
		for(Altar altar : getAllAltars())
		{
			Location altarLocation = altar.getLocation();
			if(!altarLocation.getChunk().isLoaded() || !altarLocation.getWorld().equals(location.getWorld()) || altarLocation.distance(location) > 7) continue;
			if(altar.getBlocks().contains(new DemigodsBlockLocation(location))) return altar;
		}
		return null;
	}

	/**
	 * Returns true if the block at the passed in <code>location</code> is an Altar.
	 * 
	 * @param location the location to check.
	 * @return true/false depwending on if the block is an Altar or not.
	 */
	public static boolean isAltar(Location location)
	{
		return getAltar(location) != null;
	}

	/**
	 * Returns all Altars as an ArrayList.
	 * 
	 * @return the ArrayList of Altars.
	 */
	public static Set<Altar> getAllAltars()
	{
		return Altar.loadAll();
	}
}
