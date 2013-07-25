package com.censoredsoftware.Demigods.Engine.Object.Structure;

import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsLocation;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;

@Model
public class StructureSave
{
	@Id
	private Long Id;
	@Indexed
	@Attribute
	private String structureType;
	@Indexed
	@Attribute
	private int structureDesign;
	@Indexed
	@Attribute
	private Boolean active;
	@Reference
	private DemigodsLocation reference;
	@Reference
	private PlayerCharacter owner;

	public void setStructureType(String type)
	{
		this.structureType = type;
	}

	public void setStructureDesign(int design)
	{
		this.structureDesign = design;
	}

	public void setReferenceLocation(Location reference)
	{
		this.reference = DemigodsLocation.create(reference);
	}

	public void setOwner(PlayerCharacter character)
	{
		this.owner = character;
		save();
	}

	public void setActive(Boolean bool)
	{
		this.active = bool;
		save();
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
		JOhm.delete(StructureSave.class, this.Id);
	}

	public static StructureSave load(Long Id)
	{
		return JOhm.get(StructureSave.class, Id);
	}

	public static Set<StructureSave> loadAll()
	{
		return JOhm.getAll(StructureSave.class);
	}

	public static List<StructureSave> findAll(String label, Object value)
	{
		return JOhm.find(StructureSave.class, label, value);
	}

	public Location getReferenceLocation()
	{
		return this.reference.toLocation();
	}

	public Location getClickableBlock()
	{
		return getStructureInfo().getClickableBlock(this.reference.toLocation());
	}

	public Set<Location> getLocations()
	{
		return getStructureInfo().getSchematics().get(this.structureDesign).getLocations(this.reference.toLocation());
	}

	public Structure getStructureInfo()
	{
		for(Structure structure : Demigods.getLoadedStructures())
			if(structure.getStructureType().equalsIgnoreCase(this.structureType)) return structure;
		return null;
	}

	public PlayerCharacter getOwner()
	{
		return this.owner;
	}

	public Boolean getActive()
	{
		return this.active;
	}

	public long getId()
	{
		return this.Id;
	}

	public void generate()
	{
		getStructureInfo().getSchematics().get(this.structureDesign).generate(this.reference.toLocation());
	}
}
