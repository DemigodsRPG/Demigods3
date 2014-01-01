package com.censoredsoftware.demigods.exclusive.structure;

import com.censoredsoftware.censoredlib.helper.ConfigFile2;
import com.censoredsoftware.censoredlib.schematic.Schematic;
import com.censoredsoftware.censoredlib.schematic.Selection;
import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.StructureData;
import com.censoredsoftware.demigods.engine.mythos.Structure;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class InvisibleWall extends ConfigFile2 implements Structure
{
	private final String permission;
	private final Schematic wall;

	private static ConcurrentMap<String, Design> invisibleWalls = Maps.newConcurrentMap();

	public InvisibleWall(final String permission, Location reference1, Location reference2)
	{
		this.permission = permission;
		wall = new Schematic("Invisible Wall", "Generated", 16);
		wall.add(new Selection(reference1.getBlockX(), reference1.getBlockY(), reference1.getBlockZ(), reference2.getBlockX(), reference2.getBlockY(), reference2.getBlockZ()));
	}

	@Override
	public String getName()
	{
		return "Invisible Wall";
	}

	@Override
	public Design getDesign(String name)
	{
		if(invisibleWalls.containsKey(name)) return invisibleWalls.get(name);
		return null;
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
	public StructureData createNew(boolean unused, Location... reference)
	{
		if(reference.length < 2) return null;
		StructureData save = new StructureData();
		save.setActive(true);
		save.setDesign("Invisible Wall");
		save.setType(getName());
		save.setReferenceLocation(reference[0]);
		save.setOptionalLocation(reference[1]);
		return null;
	}

	@Override
	public void unserialize(ConfigurationSection conf)
	{
		// TODO
	}

	@Override
	public String getSavePath()
	{
		return ""; // TODO
	}

	@Override
	public String getSaveFile()
	{
		return ""; // TODO
	}

	@Override
	public Map<String, Object> serialize()
	{
		return Maps.newHashMap(); // TODO
	}
}