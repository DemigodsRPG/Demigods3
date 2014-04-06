package com.demigodsrpg.demigods.base.structure;

import com.censoredsoftware.library.schematic.Schematic;
import com.censoredsoftware.library.schematic.Selection;
import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructure;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.Set;

public class RestrictedArea implements DemigodsStructureType, DemigodsStructureType.Design
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
	public Schematic getSchematic(DemigodsStructure data)
	{
		Schematic theWall = new Schematic(getName(), "Generated", getRadius());
		Location required = data.getBukkitLocation();
		Location optional = data.getSecondaryBukkitLocation();
		theWall.add(new Selection(0, 0, 0, optional.getBlockX() - data.getBukkitLocation().getBlockX(), optional.getBlockY() - required.getBlockY(), optional.getBlockZ() - required.getBlockZ()));
		return theWall;
	}

	@Override
	public int getRequiredGenerationCoords()
	{
		return 2;
	}

	@Override
	public Design getDesign(String name)
	{
		return this;
	}

	@Override
	public Collection<Design> getDesigns()
	{
		return null;
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
	public boolean sanctify(DemigodsStructure data, DemigodsCharacter character)
	{
		return false;
	}

	@Override
	public boolean corrupt(DemigodsStructure data, DemigodsCharacter character)
	{
		return false;
	}

	@Override
	public boolean birth(DemigodsStructure data, DemigodsCharacter character)
	{
		return false;
	}

	@Override
	public boolean kill(DemigodsStructure data, DemigodsCharacter character)
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
	public boolean isAllowed(final DemigodsStructure data, Player player)
	{
		Predicate<Player> permissionPredicate = new Predicate<Player>()
		{
			@Override
			public boolean apply(Player player)
			{
				DemigodsCharacter character = DemigodsCharacter.of(player);
				return data.getPermission() != null && player.hasPermission(data.getPermission()) || character != null && data.getRequiredAscensions() != null && character.getMeta().getAscensions() >= data.getRequiredAscensions();
			}
		};
		return permissionPredicate.apply(player);
	}

	@Override
	public DemigodsStructure createNew(boolean unused, String string, Location... reference)
	{
		if(reference.length < 2) return null;
		DemigodsStructure save = new DemigodsStructure();
		save.generateId();
		save.setActive(true);
		save.setDesign(getName());
		save.addFlags(getFlags());
		save.setPermission("demigods.structure.restrictedarea.immunity");
		save.setType(getName());
		save.setLocation(reference[0]);
		save.setSecondaryLocation(reference[1]);
		save.save();
		save.generate();
		return null;
	}

	public static class Util
	{
		private Util()
		{
		}

		/**
		 * Makes all invisible walls/areas visible to the <code>player</code> as glass with
		 * a fake block update.
		 *
		 * @param player the player to make the blocks visible to
		 */
		public static void toggleDebugRestrictedAreas(final Player player, boolean option)
		{
			if(!DemigodsPlugin.getInst().isEnabled()) return;
			if(option)
			{
				// Create a sync delayed task to avoid asynchronous block update issues
				Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new Runnable()
				{
					@Override
					public void run()
					{
						// Loop through all invisible wall structures
						for(DemigodsStructure save : DemigodsStructureType.Util.getStructuresWithFlag(Flag.RESTRICTED_AREA))
						{
							for(Location location : save.getType().getDesign("Restricted Area").getSchematic(save).getLocations(save.getBukkitLocation()))
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
				Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new Runnable()
				{
					@Override
					public void run()
					{
						// Loop through all invisible wall structures
						for(DemigodsStructure save : DemigodsStructureType.Util.getStructuresWithFlag(Flag.RESTRICTED_AREA))
						{
							for(Location location : save.getType().getDesign("Restricted Area").getSchematic(save).getLocations(save.getBukkitLocation()))
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
