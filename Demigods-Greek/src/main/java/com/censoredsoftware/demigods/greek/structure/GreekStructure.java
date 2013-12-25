package com.censoredsoftware.demigods.greek.structure;

import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.structure.Structure;
import com.censoredsoftware.demigods.engine.structure.StructureData;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;

public enum GreekStructure implements Structure
{
	/**
	 * General
	 */
	// Altar
	ALTAR(Altar.name, Altar.AltarDesign.values(), Altar.getDesign, Altar.createNew, Altar.sanctify, Altar.corrupt, Altar.birth, Altar.kill, Altar.flags, Altar.listener, Altar.radius, Altar.sanctity, Altar.sanctityRegen),

	// Obelisk
	OBELISK(Obelisk.name, Obelisk.ObeliskDesign.values(), Obelisk.getDesign, Obelisk.createNew, Obelisk.sanctify, Obelisk.corrupt, Obelisk.birth, Obelisk.kill, Obelisk.flags, Obelisk.listener, Obelisk.radius, Obelisk.sanctity, Obelisk.sanctityRegen),

	// Shrine
	SHRINE(Shrine.name, Shrine.ShrineDesign.values(), Shrine.getDesign, Shrine.createNew, Shrine.sanctify, Shrine.corrupt, Shrine.birth, Shrine.kill, Shrine.flags, Shrine.listener, Shrine.radius, Shrine.sanctity, Shrine.sanctityRegen);

	private String name;
	private Design[] designs;
	private Function<Location, Design> getDesign;
	private Function<Design, StructureData> createNew;
	private InteractFunction<Boolean> sanctify, corrupt, birth, kill;
	private Set<Structure.Flag> flags;
	private Listener listener;
	private int radius;
	private float sanctity, sanctityRegen;

	private GreekStructure(String name, Design[] designs, Function<Location, Design> getDesign, Function<Design, StructureData> createNew, InteractFunction<Boolean> sanctify, InteractFunction<Boolean> corrupt, InteractFunction<Boolean> birth, InteractFunction<Boolean> kill, Set<Structure.Flag> flags, Listener listener, int radius, float sanctity, float sanctityRegen)
	{
		this.name = name;
		this.designs = designs;
		this.getDesign = getDesign;
		this.createNew = createNew;
		this.sanctify = sanctify;
		this.corrupt = corrupt;
		this.birth = birth;
		this.kill = kill;
		this.flags = flags;
		this.listener = listener;
		this.radius = radius;
		this.sanctity = sanctity;
		this.sanctityRegen = sanctityRegen;
	}

	public String getName()
	{
		return name;
	}

	public Design getDesign(final String name)
	{
		try
		{
			return Iterables.find(Sets.newHashSet(designs), new Predicate<Design>()
			{
				@Override
				public boolean apply(Design design)
				{
					return design.getName().equalsIgnoreCase(name);
				}
			});
		}
		catch(NoSuchElementException ignored)
		{}
		return null;
	}

	public Set<Structure.Flag> getFlags()
	{
		return flags;
	}

	public Listener getUniqueListener()
	{
		return listener;
	}

	public int getRadius()
	{
		return radius;
	}

	public boolean sanctify(StructureData data, DCharacter dCharacter)
	{
		return sanctify.apply(data, dCharacter);
	}

	public boolean corrupt(StructureData data, DCharacter dCharacter)
	{
		return corrupt.apply(data, dCharacter);
	}

	public boolean birth(StructureData data, DCharacter dCharacter)
	{
		return birth.apply(data, dCharacter);
	}

	public boolean kill(StructureData data, DCharacter dCharacter)
	{
		return kill.apply(data, dCharacter);
	}

	public float getDefSanctity()
	{
		return sanctity;
	}

	public float getSanctityRegen()
	{
		return sanctityRegen;
	}

	public Collection<StructureData> getAll()
	{
		return Collections2.filter(StructureData.Util.loadAll(), new Predicate<StructureData>()
		{
			@Override
			public boolean apply(StructureData save)
			{
				return save.getTypeName().equals(getName());
			}
		});
	}

	public StructureData createNew(Location reference, boolean generate)
	{
		Design design = getDesign.apply(reference);
		StructureData save = createNew.apply(design);

		// All structures need these
		save.generateId();
		save.setReferenceLocation(reference);
		save.setType(getName());
		save.setDesign(design.getName());
		save.addFlags(getFlags());
		save.setActive(true);
		save.save();

		if(generate) save.generate();
		return save;
	}

    @Override
    public String toString()
    {
        return getName();
    }
}
