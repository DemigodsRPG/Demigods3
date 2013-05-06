package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.Set;

import javax.persistence.Id;

import org.bukkit.Location;
import org.bukkit.Material;

import redis.clients.johm.Attribute;
import redis.clients.johm.Indexed;
import redis.clients.johm.Model;
import redis.clients.johm.Reference;

import com.censoredsoftware.Demigods.Engine.DemigodsData;

@Model
public class TrackedBlock
{
	@Id
	private long id;
	@Reference
	@Indexed
	private TrackedLocation location;
	@Attribute
	@Indexed
	private String type;
	@Attribute
	private int material;
	@Attribute
	private byte materialByte;
	@Attribute
	private int previousMaterial;
	@Attribute
	private byte previousMaterialByte;

	public TrackedBlock()
	{}

	public TrackedBlock(Location location, String type, Material material)
	{
		this.location = TrackedModelFactory.createTrackedLocation(location);
		this.type = type;
		this.material = material.getId();
		this.materialByte = (byte) 0;
		this.previousMaterial = location.getBlock().getTypeId();
		this.previousMaterialByte = location.getBlock().getData();

		// Create the actual block
		location.getBlock().setType(material);

		save();
	}

	public TrackedBlock(Location location, String type, Material material, byte matByte)
	{
		this.location = TrackedModelFactory.createTrackedLocation(location);
		this.type = type;
		this.material = material.getId();
		this.materialByte = matByte;
		this.previousMaterial = location.getBlock().getTypeId();
		this.previousMaterialByte = location.getBlock().getData();

		// Create the actual block
		location.getBlock().setType(material);
		location.getBlock().setData(matByte);

		save();
	}

	public void save()
	{
		DemigodsData.jOhm.save(this);
	}

	public static TrackedBlock load(long id) // TODO This belongs somewhere else.
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
		getLocation().getBlock().setTypeIdAndData(this.previousMaterial, this.previousMaterialByte, true);
	}

	/*
	 * getID() : Returns the ID of the block.
	 */
	public long getId()
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
		return this.materialByte;
	}

	public Location getLocation()
	{
		return this.location.toLocation();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
