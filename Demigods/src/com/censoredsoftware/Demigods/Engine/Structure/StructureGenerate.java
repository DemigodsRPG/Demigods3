package com.censoredsoftware.Demigods.Engine.Structure;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public interface StructureGenerate
{
	public Set<GenerateBlockArea> getGenerateBlockAreas();

	public static class GenerateBlockArea
	{
		private World world;
		private Location reference;
		private int X, Y, Z, J, K, L;
		private boolean cuboid;
		private Material material;
		private byte data;

		public GenerateBlockArea(Location reference, int X, int Y, int Z, Material material, byte data)
		{
			this.world = reference.getWorld();
			this.reference = reference;
			this.X = this.J = X;
			this.Y = this.K = Y;
			this.Z = this.L = Z;
			this.cuboid = false;
		}

		public GenerateBlockArea(Location reference, int X, int Y, int Z, int J, int K, int L, Material material, byte data)
		{
			this.world = reference.getWorld();
			this.reference = reference;
			this.X = X;
			this.Y = Y;
			this.Z = Z;
			this.J = J;
			this.K = K;
			this.L = L;
			this.cuboid = true;
		}

		public Location getLocation(int X, int Y, int Z)
		{
			return new Location(world, reference.getBlockX() + X, reference.getBlockY() + Y, reference.getBlockZ() + Z);
		}

		public Set<Location> getBlockLocations()
		{
			if(cuboid)
			{
				final int X = this.X < this.J ? this.X : this.J, J = this.X > this.J ? this.X : this.J;
				final int Y = this.Y < this.K ? this.Y : this.K, K = this.Y > this.K ? this.Y : this.K;
				final int Z = this.Z < this.L ? this.Z : this.L, L = this.Z > this.L ? this.Z : this.L;
				return new HashSet<Location>()
				{
					{
						for(int i = X; i < J; i++)
						{
							for(int o = Y; o < K; o++)
							{
								for(int p = Z; p < L; p++)
								{
									add(getLocation(i, o, p));
								}
							}
						}
					}
				};
			}
			else
			{
				final int X = this.X, Y = this.Y, Z = this.Z;
				return new HashSet<Location>()
				{
					{
						add(getLocation(X, Y, Z));
					}
				};
			}
		}
	}
}
