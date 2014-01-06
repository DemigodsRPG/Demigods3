package com.censoredsoftware.demigods.base.structure;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.censoredsoftware.censoredlib.schematic.Schematic;
import com.censoredsoftware.censoredlib.schematic.Selection;
import com.censoredsoftware.demigods.engine.data.serializable.DCharacter;
import com.censoredsoftware.demigods.engine.data.serializable.DPlayer;
import com.censoredsoftware.demigods.engine.data.serializable.StructureData;
import com.censoredsoftware.demigods.engine.mythos.Structure;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

public class InvisibleWall implements Structure, Structure.Design
{
	@Override
	public String getName()
	{
		return "Invisible Wall";
	}

	@Override
	public Set<Location> getClickableBlocks(Location reference)
	{
		return Sets.newHashSet();
	}

	@Override
	public Schematic getSchematic(StructureData data)
	{
		Schematic theWall = new Schematic("Invisible Wall", "Generated", 16);
		Location required = data.getReferenceLocation();
		Location optional = data.getOptionalLocation();
		theWall.add(new Selection(0, 0, 0, optional.getBlockX() - data.getReferenceLocation().getBlockX(), optional.getBlockY() - required.getBlockY(), optional.getBlockZ() - required.getBlockZ()));
		return theWall;
	}

	@Override
	public int getRequiredGenerationPoints()
	{
		return 2;
	}

	@Override
	public Design getDesign(String name)
	{
		return this;
	}

	@Override
	public Set<Flag> getFlags()
	{
		return Sets.newHashSet(Flag.INVISIBLE_WALL, Flag.STRUCTURE_WAND_GENERABLE);
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
	public boolean isAllowed(final StructureData data, Player player)
	{
		Predicate<Player> permissionPredicate = new Predicate<Player>()
		{
			@Override
			public boolean apply(Player player)
			{
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
				return data.getPermission() != null && player.hasPermission(data.getPermission()) || character != null && data.getRequiredAscensions() != null && character.getMeta().getAscensions() >= data.getRequiredAscensions();
			}
		};
		return permissionPredicate.apply(player);
	}

	@Override
	public StructureData createNew(boolean unused, Location... reference)
	{
		if(reference.length < 2) return null;
		StructureData save = new StructureData();
		save.generateId();
		save.setActive(true);
		save.setDesign("Invisible Wall");
		save.addFlags(getFlags());
		save.setPermission("demigods.structure.invisiblewall.immunity");
		save.setType(getName());
		save.setReferenceLocation(reference[0]);
		save.setOptionalLocation(reference[1]);
		save.save();
		save.generate();
		return null;
	}
}
