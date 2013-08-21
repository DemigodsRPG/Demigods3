package com.censoredsoftware.demigods.util;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.structure.Structure;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Zones
{
	/**
	 * Returns true if <code>location</code> is within a no-PVP zone.
	 * 
	 * @param location the location to check.
	 * @return true/false depending on if it's a no-PVP zone or not.
	 */
	public static boolean zoneNoPVP(Location location)
	{
		if(Demigods.config.getSettingBoolean("zones.allow_skills_anywhere")) return false;
		if(Demigods.worldguard != null) return Structures.isInRadiusWithFlag(location, Structure.Flag.NO_PVP) || Iterators.any(Demigods.worldguard.getRegionManager(location.getWorld()).getApplicableRegions(location).iterator(), new Predicate<ProtectedRegion>()
		{
			@Override
			public boolean apply(ProtectedRegion region)
			{
				return region.getId().toLowerCase().contains("nopvp");
			}
		});
		return Structures.isInRadiusWithFlag(location, Structure.Flag.NO_PVP);
	}

	/**
	 * Returns true if <code>location</code> is within a no-build zone
	 * for <code>player</code>.
	 * 
	 * @param player the player to check.
	 * @param location the location to check.
	 * @return true/false depending on the position of the <code>player</code>.
	 */
	public static boolean zoneNoBuild(Player player, Location location) // TODO BROKEN
	{
		if(Demigods.worldguard != null && !Demigods.worldguard.canBuild(player, location)) return true;
		Structure.Save save = Structures.getInRadiusWithFlag(location, Structure.Flag.NO_GRIEFING);
		if(save != null && save.getOwner() != null) return !save.getOwner().equals(DPlayer.Util.getPlayer(player).getCurrent().getId());
		return false;
	}
}
