package com.censoredsoftware.demigods.exclusive.structure;

import com.censoredsoftware.censoredlib.schematic.Schematic;
import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.StructureData;
import com.censoredsoftware.demigods.engine.mythos.Structure;
import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.Set;

public class InvisibleWall extends StructureData implements Structure, Structure.Design
{
	private final String permission;

	public InvisibleWall(final String permission)
	{
		this.permission = permission;
	}

	@Override
	public String getName()
	{
		return "Invisible Wall";
	}

	@Override
	public Set<Location> getClickableBlocks(Location reference)
	{
		return null; // TODO
	}

	@Override
	public Schematic getSchematic()
	{
		return null; // TODO
	}

	@Override
	public Design getDesign(String unused)
	{
		return this;
	}

	@Override
	public Set<Flag> getFlags()
	{
		return Sets.newHashSet(Flag.INVISIBLE_WALL);
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
		return 16;
	}

	@Override
	public boolean isAllowed(CommandSender sender)
	{
		return sender.hasPermission(permission);
	}

	@Override
	public InvisibleWall createNew(Location reference, boolean generate)
	{
		return null;
	}
}
