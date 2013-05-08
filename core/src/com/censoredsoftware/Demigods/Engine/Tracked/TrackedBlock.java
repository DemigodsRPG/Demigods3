package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.google.common.base.Objects;

@Model
public class TrackedBlock
{
	@Id
	private Long id;
	@Reference
	@Indexed
	TrackedLocation location;
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

	void setLocation(TrackedLocation location)
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

	public static void save(TrackedBlock block)
	{
		DemigodsData.jOhm.save(block);
	}

	public static TrackedBlock load(Long id)
	{
		return DemigodsData.jOhm.get(TrackedBlock.class, id);
	}

	public static Set<TrackedBlock> loadAll()
	{
		return DemigodsData.jOhm.getAll(TrackedBlock.class);
	}

	/*
	 * remove() : Removes the block.
	 */
	public void remove()
	{
		getLocation().getBlock().setTypeIdAndData((byte) this.previousMaterial, (byte) this.previousMaterialByte, true);
		DemigodsData.jOhm.delete(TrackedBlock.class, getId());
	}

	/*
	 * getID() : Returns the ID of the block.
	 */
	public Long getId()
	{
		return this.id;
	}

	/*
	 * getMaterial() : Returns the material of the block.
	 */
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
		if(obj instanceof ComparableLocation)
		{
			final ComparableLocation other = (ComparableLocation) obj;
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
}
