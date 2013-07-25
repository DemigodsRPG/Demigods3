package com.censoredsoftware.Demigods.Engine.Object.Structure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsLocation;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.StructureFlags.StructureFlag;
import com.google.common.collect.Sets;

@Model
public class StructureSave
{
	@Id
	private Long id;
	@Indexed
	@Attribute
	private String structureType;
	@Indexed
	@Attribute
	private String structureDesign;
	@Indexed
	@Attribute
	private Boolean active;
	@Reference
	private DemigodsLocation reference;
	@Reference
	private PlayerCharacter owner;

	// Settings
	@Indexed
	@Attribute
	private Boolean protectedBlocks;
	@Indexed
	@Attribute
	private Boolean hasOwner;
	@Indexed
	@Attribute
	private Boolean deleteOnOwnerDelete;
	@CollectionSet(of = String.class)
	@Indexed
	private Set<String> flags;

	public void setFlag(StructureFlag flag)
	{
		if(this.flags == null || this.flags.isEmpty()) this.flags = Sets.newHashSet();
		this.flags.add(flag.name());
		save();
	}

	public void setStructureType(String type)
	{
		this.structureType = type;
	}

	public void setStructureDesign(String name)
	{
		this.structureDesign = name;
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

	public void setSettings(boolean protectedBlocks, boolean hasOwner, boolean deleteOnOwnerDelete)
	{
		this.protectedBlocks = protectedBlocks;
		this.hasOwner = hasOwner;
		this.deleteOnOwnerDelete = deleteOnOwnerDelete;
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
			location.getBlock().setTypeId(Material.AIR.getId());
		JOhm.delete(DemigodsLocation.class, reference.getId());
		JOhm.delete(StructureSave.class, this.id);
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
		return getStructureInfo().get(this.structureDesign).getLocations(this.reference.toLocation());
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

	public boolean getSettingProtectedBlocks()
	{
		return this.protectedBlocks;
	}

	public boolean getSettingHasOwner()
	{
		return this.hasOwner;
	}

	public boolean getSettingDeleteOnOwnerDelete()
	{
		return this.deleteOnOwnerDelete;
	}

	public Boolean getActive()
	{
		return this.active;
	}

	public Set<StructureFlag> getFlags()
	{
		return new HashSet<StructureFlag>()
		{
			{
				for(String name : flags)
				{
					add(StructureFlag.valueOf(name));
				}
			}
		};
	}

	public long getId()
	{
		return this.id;
	}

	public void generate()
	{
		getStructureInfo().get(this.structureDesign).generate(this.reference.toLocation());
	}
}
