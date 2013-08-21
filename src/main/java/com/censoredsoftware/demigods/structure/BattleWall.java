package com.censoredsoftware.demigods.structure;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.util.Structures;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BattleWall implements Structure
{
	private final static Schematic general = new Schematic("general", "HmmmQuestionMark", 0)
	{
		{
			add(new Selection(0, 0, 0, Material.WEB));
			add(new Selection(0, 1, 0, Material.WEB));
			add(new Selection(0, 2, 0, Material.WEB));
			add(new Selection(0, 3, 0, Material.WEB));
		}
	};

	public static enum ShrineDesign implements Design
	{
		GENERAL("general", general, null);

		private final String name;
		private final Schematic schematic;
		private final Selection clickableBlocks;

		private ShrineDesign(String name, Schematic schematic, Selection clickableBlocks)
		{
			this.name = name;
			this.schematic = schematic;
			this.clickableBlocks = clickableBlocks;
		}

		@Override
		public String getName()
		{
			return name;
		}

		@Override
		public Set<Location> getClickableBlocks(Location reference)
		{
			return Sets.newHashSet();
		}

		@Override
		public Schematic getSchematic()
		{
			return schematic;
		}
	}

	@Override
	public Set<Flag> getFlags()
	{
		return new HashSet<Flag>()
		{
			{
				add(Flag.PROTECTED_BLOCKS);
				add(Flag.BATTLE_WALL);
			}
		};
	}

	@Override
	public String getStructureType()
	{
		return "Shrine";
	}

	@Override
	public Design getDesign(String name)
	{
		return ShrineDesign.GENERAL;
	}

	@Override
	public int getRadius()
	{
		return Demigods.config.getSettingInt("zones.shrine_radius");
	}

	@Override
	public org.bukkit.event.Listener getUniqueListener()
	{
		return null;
	}

	@Override
	public Collection<Save> getAll()
	{
		return Structures.findAll(new Predicate<Save>()
		{
			@Override
			public boolean apply(Save save)
			{
				return save.getType().equals(getStructureType());
			}
		});
	}

	@Override
	public Save createNew(Location reference, boolean generate)
	{
		Save save = new Save();
		save.generateId();
		save.setReferenceLocation(reference);
		save.setType(getStructureType());
		save.setDesign("general");
		save.addFlags(getFlags());
		save.setActive(true);
		save.save();
		if(generate) save.generate(false);
		return save;
	}
}
