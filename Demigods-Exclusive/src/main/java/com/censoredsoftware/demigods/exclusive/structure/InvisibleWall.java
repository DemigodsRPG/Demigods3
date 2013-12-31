package com.censoredsoftware.demigods.exclusive.structure;

import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.StructureData;
import com.censoredsoftware.demigods.engine.mythos.Structure;
import com.google.common.base.Predicate;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.Set;

// TODO

public class InvisibleWall implements Structure
{
	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public Design getDesign(String name)
	{
		return null;
	}

	@Override
	public Set<Flag> getFlags()
	{
		return null;
	}

	@Override
	public Listener getUniqueListener()
	{
		return null;
	}

	@Override
	public boolean sanctify(StructureData data, DCharacter character)
	{
		return false;
	}

	@Override
	public boolean corrupt(StructureData data, DCharacter character)
	{
		return false;
	}

	@Override
	public boolean birth(StructureData data, DCharacter character)
	{
		return false;
	}

	@Override
	public boolean kill(StructureData data, DCharacter character)
	{
		return false;
	}

	@Override
	public float getDefSanctity()
	{
		return 0;
	}

	@Override
	public float getSanctityRegen()
	{
		return 0;
	}

	@Override
	public int getRadius()
	{
		return 0;
	}

	@Override
	public Predicate<CommandSender> isAllowed()
	{
		return null;
	}

	@Override
	public Collection<StructureData> getAll()
	{
		return null;
	}

	@Override
	public StructureData createNew(Location reference, boolean generate)
	{
		return null;
	}
}
