package com.censoredsoftware.Demigods.Engine.Object.General;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Object.Structure.Altar;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Shrine;
import com.google.common.base.Objects;

@Model
public class DemigodsBlock
{
	@Id
	private Long id;
	@Reference
	@Indexed
	DemigodsLocation location;
	@Attribute
	@Indexed
	private String type;
	@Attribute
	private int material;
	@Attribute
	private int materialByte;
	@Attribute
	private int previousMaterial;
	@Attribute
	private int previousMaterialByte;

	void setLocation(DemigodsLocation location)
	{
		this.location = location;
	}

	void setType(String type)
	{
		this.type = type;
	}

	void setMaterial(Material material)
	{
		this.material = material.getId();
	}

	void setMaterialByte(byte data)
	{
		this.materialByte = (int) data;
	}

	void setPreviousMaterial(Material material)
	{
		this.previousMaterial = material.getId();
	}

	void setPreviousMaterialByte(byte data)
	{
		this.previousMaterialByte = (int) data;
	}

	public static void save(DemigodsBlock block)
	{
		JOhm.save(block);
	}

	public static void delete(long id)
	{
		JOhm.delete(DemigodsBlock.class, id);
	}

	public static DemigodsBlock load(Long id)
	{
		return JOhm.get(DemigodsBlock.class, id);
	}

	public static Set<DemigodsBlock> loadAll()
	{
		return JOhm.getAll(DemigodsBlock.class);
	}

	public void remove()
	{
		getLocation().getBlock().setTypeIdAndData(this.previousMaterial, (byte) this.previousMaterialByte, true);
		JOhm.delete(DemigodsBlock.class, getId());
	}

	public Long getId()
	{
		return this.id;
	}

	public Material getMaterial()
	{
		return Material.getMaterial(this.material);
	}

	public byte getMaterialByte()
	{
		return (byte) this.materialByte;
	}

	public Location getLocation()
	{
		return this.location.toLocation();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if(this == obj) return true;
		if(obj == null) return false;
		if(obj instanceof DemigodsBlockLocation)
		{
			final DemigodsBlockLocation other = (DemigodsBlockLocation) obj;
			return Objects.equal(this.location.world, other.world) && Objects.equal(this.location.X, other.X) && Objects.equal(this.location.Y, other.Y) && Objects.equal(this.location.Z, other.Z) && Objects.equal(this.location.yaw, other.yaw) && Objects.equal(this.location.pitch, other.pitch);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(location.world, location.X, location.Y, location.Z, location.yaw, location.pitch);
	}

	@Override
	public String toString()
	{
		return Objects.toStringHelper(this).add("id", id).add("location", location).add("type", type).add("material", material).add("materialByte", materialByte).add("previousMaterial", previousMaterial).add("previousMaterialByte", previousMaterialByte).toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

	/**
	 * Grab the DemigodsBlock from the data with id <code>id</code>.
	 * 
	 * @param id The ID of the block.
	 * @return DemigodsBlock.
	 */
	public static DemigodsBlock getBlock(Long id)
	{
		return DemigodsBlock.load(id);
	}

	public static Set<DemigodsBlock> getAllBlocks()
	{
		return DemigodsBlock.loadAll();
	}

	/**
	 * Returns true if the block at the passed in <code>location</code> is protected.
	 * 
	 * @param location the location to check.
	 * @return true/false depending on if the block is protected or not.
	 */
	public static boolean isProtected(Location location)
	{
		return Altar.isAltar(location) || Shrine.isShrine(location);
	}

	/**
	 * Regenerates all structures to ensure that they are in perfect condition.
	 */
	public static void regenerateStructures()
	{
		// Regenerate Altars
		for(Altar altar : Altar.getAllAltars())
		{
			Altar.generate(altar, altar.getLocation());
		}

		// Regenerate Shrines
		for(Shrine shrine : Shrine.getAllShrines())
		{
			Shrine.generate(shrine, shrine.getLocation());
		}
	}
}
