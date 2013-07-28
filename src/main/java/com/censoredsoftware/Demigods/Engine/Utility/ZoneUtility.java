package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Structure;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class ZoneUtility
{
	// TODO Optimize this stuff.

	/**
	 * Returns true if <code>location</code> is within a no-PVP zone.
	 * 
	 * @param location the location to check.
	 * @return true/false depending on if it's a no-PVP zone or not.
	 */
	public static boolean zoneNoPVP(Location location)
	{
		if(Demigods.config.getSettingBoolean("zones.allow_skills_anywhere")) return false;
		if(Demigods.config.getSettingBoolean("zones.use_dynamic_pvp_zones"))
		{
			if(Demigods.worldguard != null) return !canWorldGuardDynamicPVPAndNotNoPvPStructure(location);
			return Structure.Util.isInRadiusWithFlag(location, Structure.Flag.NO_PVP, true);
		}
		return !canWorldGuardFlagPVP(location);
	}

	/**
	 * Returns true if <code>to</code> is outside of a no-PVP zone.
	 * 
	 * @param to the location moving towards.
	 * @param from the location leaving from.
	 * @return true/false if leaving a no-PVP zone.
	 */
	public static boolean enterZoneNoPVP(Location to, Location from)
	{
		return !zoneNoPVP(from) && zoneNoPVP(to);
	}

	/**
	 * Returns true if <code>to</code> is outside of a no-PVP zone.
	 * 
	 * @param to the location moving towards.
	 * @param from the location leaving from.
	 * @return true/false if leaving a no-PVP zone.
	 */
	public static boolean exitZoneNoPVP(Location to, Location from)
	{
		return enterZoneNoPVP(from, to);
	}

	private static boolean canWorldGuardDynamicPVPAndNotNoPvPStructure(Location location)
	{
		return (!Structure.Util.isInRadiusWithFlag(location, Structure.Flag.NO_PVP, true)) && canWorldGuardDynamicPVP(location);
	}

	private static boolean canWorldGuardDynamicPVP(Location location)
	{
		ApplicableRegionSet set = Demigods.worldguard.getRegionManager(location.getWorld()).getApplicableRegions(location);
		for(ProtectedRegion region : set)
		{
			if(region.getId().toLowerCase().contains("nopvp")) return false;
		}
		return true;
	}

	private static boolean canWorldGuardFlagPVP(Location location)
	{
		ApplicableRegionSet set = Demigods.worldguard.getRegionManager(location.getWorld()).getApplicableRegions(location);
		return !set.allows(DefaultFlag.PVP);
	}

	/**
	 * Returns true if <code>location</code> is within a no-build zone
	 * for <code>player</code>.
	 * 
	 * @param player the player to check.
	 * @param location the location to check.
	 * @return true/false depending on the position of the <code>player</code>.
	 */
	public static boolean zoneNoBuild(Player player, Location location)
	{
		return !canWorldGuardBuild(player, location);
	}

	private static boolean canWorldGuardBuild(Player player, Location location)
	{
		return Demigods.worldguard.canBuild(player, location);
	}
}
