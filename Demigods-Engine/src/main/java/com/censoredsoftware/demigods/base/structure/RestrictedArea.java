package com.censoredsoftware.demigods.base.structure;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.censoredsoftware.censoredlib.schematic.Schematic;
import com.censoredsoftware.censoredlib.schematic.Selection;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.serializable.DCharacter;
import com.censoredsoftware.demigods.engine.data.serializable.DPlayer;
import com.censoredsoftware.demigods.engine.data.serializable.StructureSave;
import com.censoredsoftware.demigods.engine.mythos.Structure;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

public class RestrictedArea implements Structure, Structure.Design
{
	@Override
	public String getName()
	{
		return "Restricted Area";
	}

	@Override
	public Set<Location> getClickableBlocks(Location reference)
	{
		return Sets.newHashSet();
	}

	@Override
	public Schematic getSchematic(StructureSave data)
	{
		Schematic theWall = new Schematic(getName(), "Generated", getRadius());
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
		return Sets.newHashSet(Flag.RESTRICTED_AREA, Flag.STRUCTURE_WAND_GENERABLE);
	}

	@Override
	public Listener getUniqueListener()
	{
		return null;
	}

	@Override
	public boolean sanctify(StructureSave data, DCharacter character)
	{
		return false;
	}

	@Override
	public boolean corrupt(StructureSave data, DCharacter character)
	{
		return false;
	}

	@Override
	public boolean birth(StructureSave data, DCharacter character)
	{
		return false;
	}

	@Override
	public boolean kill(StructureSave data, DCharacter character)
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
	public boolean isAllowed(final StructureSave data, Player player)
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
	public StructureSave createNew(boolean unused, Location... reference)
	{
		if(reference.length < 2) return null;
		StructureSave save = new StructureSave();
		save.generateId();
		save.setActive(true);
		save.setDesign(getName());
		save.addFlags(getFlags());
		save.setPermission("demigods.structure.restrictedarea.immunity");
		save.setType(getName());
		save.setReferenceLocation(reference[0]);
		save.setOptionalLocation(reference[1]);
		save.save();
		save.generate();
		return null;
	}

	public static class Util
	{
		private Util()
		{}

		/**
		 * Makes all invisible walls/areas visible to the <code>player</code> as glass with
		 * a fake block update.
		 * 
		 * @param player the player to make the blocks visible to
		 */
		public static void toggleDebugRestrictedAreas(final Player player, boolean option)
		{
			if(option)
			{
				// Create a sync delayed task to avoid asynchronous block update issues
				Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.plugin(), new Runnable()
				{
					@Override
					public void run()
					{
						// Loop through all invisible wall structures
						for(StructureSave save : Structure.Util.getStructuresWithFlag(Flag.RESTRICTED_AREA))
						{
							for(Location location : save.getType().getDesign("Restricted Area").getSchematic(save).getLocations(save.getReferenceLocation()))
							{
								player.sendBlockChange(location, Material.GLASS, (byte) 0);
							}
						}
					}
				});
			}
			else
			{
				// Create a sync delayed task to avoid asynchronous block update issues
				Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.plugin(), new Runnable()
				{
					@Override
					public void run()
					{
						// Loop through all invisible wall structures
						for(StructureSave save : Structure.Util.getStructuresWithFlag(Flag.RESTRICTED_AREA))
						{
							for(Location location : save.getType().getDesign("Restricted Area").getSchematic(save).getLocations(save.getReferenceLocation()))
							{
								player.sendBlockChange(location, location.getBlock().getType(), location.getBlock().getData());
							}
						}
					}
				});
			}
		}
	}
}
