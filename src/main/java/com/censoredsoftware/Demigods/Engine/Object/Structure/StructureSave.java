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
import com.google.common.collect.Sets;

@Model
public class StructureSave
{
	@Id
	private Long id;
	@Indexed
	@CollectionSet(of = String.class)
	private Set<String> flags;
	@Indexed
	@Attribute
	private String type;
	@Indexed
	@Attribute
	private String design;
	@Indexed
	@Attribute
	private Boolean active;
	@Reference
	private DemigodsLocation reference;
	@Indexed
	@Reference
	private PlayerCharacter owner;

	public void setType(String type)
	{
		this.type = type;
	}

	public void setDesign(String name)
	{
		this.design = name;
	}

	public void setReferenceLocation(Location reference)
	{
		this.reference = DemigodsLocation.create(reference);
	}

	public void setOwner(PlayerCharacter character)
	{
		this.owner = character;
	}

	public void setActive(Boolean bool)
	{
		this.active = bool;
	}

	public void addFlags(Set<Structure.Flag> flags)
	{
		if(this.flags == null) this.flags = Sets.newHashSet();
		for(Structure.Flag flag : flags)
		{
			this.flags.add(flag.name());
		}
	}

	public void addFlag(Structure.Flag flag)
	{
		if(this.flags == null) this.flags = Sets.newHashSet();
		this.flags.add(flag.name());
	}

	public Location getReferenceLocation()
	{
		return this.reference.toLocation();
	}

	public Location getClickableBlock()
	{
		return getStructure().getClickableBlock(this.reference.toLocation());
	}

	public Set<Location> getLocations()
	{
		return getStructure().get(this.design).getLocations(this.reference.toLocation());
	}

	public Structure getStructure()
	{
		for(Structure structure : Demigods.getLoadedStructures())
		{
			if(structure.getStructureType().equalsIgnoreCase(this.type)) return structure;
		}
		return null;
	}

	public Boolean hasOwner()
	{
		return this.owner != null;
	}

	public PlayerCharacter getOwner()
	{
		return this.owner;
	}

	public Boolean getActive()
	{
		return this.active;
	}

	public Boolean hasFlag(Structure.Flag flag)
	{
		return this.flags != null && this.flags.contains(flag.name());
	}

	public Set<Structure.Flag> getFlags()
	{
		return new HashSet<Structure.Flag>()
		{
			{
				for(String flag : getRawFlags())
				{
					add(Structure.Flag.valueOf(flag));
				}
			}
		};
	}

	public Set<String> getRawFlags()
	{
		return this.flags;
	}

	public long getId()
	{
		return this.id;
	}

	public void generate()
	{
		getStructure().get(this.design).generate(this.reference.toLocation());
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
		JOhm.delete(DemigodsLocation.class, reference.getId());
		JOhm.delete(StructureSave.class, this.id);
	}

	public static StructureSave load(Long id)
	{
		return JOhm.get(StructureSave.class, id);
	}

	public static Set<StructureSave> loadAll()
	{
		return JOhm.getAll(StructureSave.class);
	}

	public static List<StructureSave> findAll(String label, Object value)
	{
		return JOhm.find(StructureSave.class, label, value);
	}
}
