package com.censoredsoftware.demigods.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import redis.clients.johm.*;

import com.censoredsoftware.core.region.Region;
import com.censoredsoftware.core.util.Randoms;
import com.google.common.collect.Sets;

@Model
public class DLocation
{
	@Id
	private Long id;
	@Attribute
	@Indexed
	String world;
	@Attribute
	Double X;
	@Attribute
	Double Y;
	@Attribute
	Double Z;
	@Attribute
	Float pitch;
	@Attribute
	Float yaw;
	@Indexed
	@Attribute
	String region;

	void setWorld(String world)
	{
		this.world = world;
	}

	void setX(Double X)
	{
		this.X = X;
	}

	void setY(Double Y)
	{
		this.Y = Y;
	}

	void setZ(Double Z)
	{
		this.Z = Z;
	}

	void setYaw(Float yaw)
	{
		this.yaw = yaw;
	}

	void setPitch(Float pitch)
	{
		this.pitch = pitch;
	}

	void setRegion(Region region)
	{
		this.region = region.toString();
	}

	public Location toLocation() throws NullPointerException
	{
		return new Location(Bukkit.getServer().getWorld(this.world), this.X, this.Y, this.Z, this.yaw, this.pitch);
	}

	public Long getId()
	{
		return this.id;
	}

	public Double getX()
	{
		return this.X;
	}

	public Double getY()
	{
		return this.Y;
	}

	public Double getZ()
	{
		return this.Z;
	}

	public String getWorld()
	{
		return this.world;
	}

	public Region getRegion()
	{
		return Region.Util.getRegion(toLocation());
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

	public static class Util
	{
		public static DLocation create(String world, double X, double Y, double Z, float yaw, float pitch)
		{
			DLocation trackedLocation = new DLocation();
			trackedLocation.setWorld(world);
			trackedLocation.setX(X);
			trackedLocation.setY(Y);
			trackedLocation.setZ(Z);
			trackedLocation.setYaw(yaw);
			trackedLocation.setPitch(pitch);
			trackedLocation.setRegion(Region.Util.getRegion((int) X, (int) Z, world));
			JOhm.save(trackedLocation);
			return trackedLocation;
		}

		public static DLocation create(Location location)
		{
			return create(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		}

		public static Set<DLocation> loadAll()
		{
			return JOhm.getAll(DLocation.class);
		}

		public static DLocation get(Location location)
		{
			for(DLocation tracked : loadAll())
				if(location.getX() == tracked.getX() && location.getY() == tracked.getY() && location.getBlockZ() == tracked.getZ() && location.getWorld().getName().equals(tracked.getWorld())) return tracked;
			return create(location);
		}

		/**
		 * Randoms a random location with the center being <code>reference</code>.
		 * Must be at least <code>min</code> blocks from the center and no more than <code>max</code> blocks away.
		 * 
		 * @param reference the location used as the center for reference.
		 * @param min the minimum number of blocks away.
		 * @param max the maximum number of blocks away.
		 * @return the random location generated.
		 */
		public static Location randomLocation(Location reference, int min, int max)
		{
			Location location = reference.clone();
			double randX = Randoms.generateIntRange(min, max);
			double randZ = Randoms.generateIntRange(min, max);
			location.add(randX, 0, randZ);
			double highestY = location.clone().getWorld().getHighestBlockYAt(location);
			location.setY(highestY);
			return location;
		}

		/**
		 * Returns a random location within the <code>chunk</code> passed in.
		 * 
		 * @param chunk the chunk that we will obtain the location from.
		 * @return the random location generated.
		 */
		public static Location randomChunkLocation(Chunk chunk)
		{
			Location reference = chunk.getBlock(Randoms.generateIntRange(1, 16), 64, Randoms.generateIntRange(1, 16)).getLocation();
			double locX = reference.getX();
			double locY = chunk.getWorld().getHighestBlockYAt(reference);
			double locZ = reference.getZ();
			return new Location(chunk.getWorld(), locX, locY, locZ);
		}

		/**
		 * Returns a set of blocks in a radius of <code>radius</code> at the provided <code>location</code>.
		 * 
		 * @param location the center location to getDesign the blocks from.
		 * @param radius the radius around the center block from which to getDesign the blocks.
		 * @return Set<Block>
		 */
		public static Set<Block> getBlocks(Location location, int radius)
		{
			// Define variables
			Set<Block> blocks = Sets.newHashSet();
			blocks.add(location.getBlock());

			for(int x = 0; x <= radius; x++)
				blocks.add(location.add(x, 0, x).getBlock());

			return blocks;
		}

		public static Location getFloorBelowLocation(Location location)
		{
			if(location.getBlock().getType().isSolid()) return location;
			return getFloorBelowLocation(location.getBlock().getRelative(BlockFace.DOWN).getLocation());
		}

		public static List<Location> getCirclePoints(Location center, final double radius, final int points)
		{
			final World world = center.getWorld();
			final double X = center.getX();
			final double Y = center.getY();
			final double Z = center.getZ();
			return new ArrayList<Location>()
			{
				{
					for(int i = 0; i < points; i++)
					{
						double x = X + radius * Math.cos((2 * Math.PI * i) / points);
						double z = Z + radius * Math.sin((2 * Math.PI * i) / points);
						add(new Location(world, x, Y, z));
					}
				}
			};
		}

		public static float toDegree(double angle)
		{
			return (float) Math.toDegrees(angle);
		}
	}
}
