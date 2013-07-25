package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Structure;
import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureFlag;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class ZoneUtility
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
		if(Demigods.config.getSettingBoolean("zones.use_dynamic_pvp_zones"))
		{
			if(Demigods.worldguard != null) return !canWorldGuardDynamicPVPAndNotNoPvPStructure(location);
			else return Structure.isInRadiusWithFlag(location, StructureFlag.NO_PVP);
		}
		else return !canWorldGuardFlagPVP(location);
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
		return (!Structure.isInRadiusWithFlag(location, StructureFlag.NO_PVP)) && canWorldGuardDynamicPVP(location);
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
	 * Returns true if doTargeting is allowed for <code>player</code> in <code>location</code>.
	 * 
	 * @param player the player to check.
	 * @param location the location to check.
	 * @return true/false depending on if doTargeting is allowed.
	 */
	public static boolean canTarget(Entity player, Location location)
	{
		return !(player instanceof Player) || DataUtility.hasKeyTemp(((Player) player).getName(), "temp_was_PVP") && Demigods.config.getSettingBoolean("zones.use_dynamic_pvp_zones") || !zoneNoPVP(location);
	}

	/**
	 * Returns true if doTargeting is allowed for <code>player</code>.
	 * 
	 * @param player the player to check.
	 * @return true/false depending on if doTargeting is allowed.
	 */
	public static boolean canTarget(Entity player)
	{
		return canTarget(player, player.getLocation());
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

	/**
	 * Returns true if the <code>player</code> is entering a no-build zone.
	 * 
	 * @param player the player to check.
	 * @param to the location being entered.
	 * @param from the location being left.
	 * @return true/false depending on if the player is entering a no build zone.
	 */
	public static boolean enterZoneNoBuild(Player player, Location to, Location from)
	{
		return !zoneNoBuild(player, from) && zoneNoBuild(player, to);
	}

	/**
	 * Returns true if the <code>player</code> is exiting a no-build zone.
	 * 
	 * @param player the player to check.
	 * @param to the location being entered.
	 * @param from the location being left.
	 * @return true/false depending on if the player is exiting a no build zone.
	 */
	public static boolean exitZoneNoBuild(Player player, Location to, Location from)
	{
		return enterZoneNoBuild(player, from, to);
	}

	private static boolean canWorldGuardBuild(Player player, Location location)
	{
		return Demigods.worldguard.canBuild(player, location);
	}
}
