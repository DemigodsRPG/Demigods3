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
	@Reference
	private PlayerCharacter owner;
	@Indexed
	@CollectionSet(of = String.class)
	private Set<String> flags;

	public StructureSave()
	{
		// Initialize data
		this.flags = Sets.newHashSet();
	}

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
		for(Structure.Flag flag : flags)
		{
			this.flags.add(flag.name());
		}
	}

	public void addFlag(Structure.Flag flag)
	{
		this.flags.add(flag.name());
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
				for(String name : flags)
				{
					add(Structure.Flag.valueOf(name));
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
}
