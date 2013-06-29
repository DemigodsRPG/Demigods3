package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Altar;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Shrine;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class ZoneUtility
{
	public static int SHRINE_RADIUS = Demigods.config.getSettingInt("zones.shrine_radius");
	public static int ALTAR_RADIUS = Demigods.config.getSettingInt("zones.altar_radius");

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
			if(Demigods.worldguard != null) return !canWorldGuardDynamicPVPAndNotAltar(location);
			else return zoneAltar(location) != null;
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

	private static boolean canWorldGuardDynamicPVPAndNotAltar(Location location)
	{
		return (zoneAltar(location) == null) && canWorldGuardDynamicPVP(location) || canWorldGuardDynamicPVP(location) && zoneAltar(location) == null;
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

	/**
	 * Returns the shrine at <code>location</code>.
	 * 
	 * @param location the location to check.
	 * @return the Shrine object.
	 */
	public static Shrine zoneShrine(Location location)
	{
		if(Shrine.getAllShrines() == null) return null;

		for(Shrine shrine : Shrine.getAllShrines())
		{
			if(location.getWorld() != shrine.getLocation().getWorld()) continue;
			if(location.distance(shrine.getLocation()) <= SHRINE_RADIUS) return shrine;
		}
		return null;
	}

	/**
	 * Returns the character ID of the Shrine owner at <code>location</code>.
	 * 
	 * @param location the location to check.
	 * @return the character ID of the owner.
	 */
	public static long zoneShrineOwner(Location location)
	{
		if(Shrine.getAllShrines() == null) return -1;

		for(Shrine shrine : Shrine.getAllShrines())
		{
			if(shrine.getLocation().equals(location)) return shrine.getCharacter().getId();
		}
		return -1;
	}

	/**
	 * Returns true if <code>from</code> is entering Altar <code>to</code>.
	 * 
	 * @param to location coming from.
	 * @param from location heading to.
	 * @return true/false depending on the direction of movement.
	 */
	public static boolean enterZoneShrine(Location to, Location from)
	{
		return (zoneShrine(from) == null) && zoneShrine(to) != null;
	}

	/**
	 * Returns true if <code>to</code> is leaving Shrine <code>from</code>.
	 * 
	 * @param to location coming from.
	 * @param from location heading to.
	 * @return true/false depending on the direction of movement.
	 */
	public static boolean exitZoneShrine(Location to, Location from)
	{
		return enterZoneShrine(from, to);
	}

	/**
	 * Returns true if <code>location</code> is within an Altar zone.
	 * 
	 * @param location the location to check.
	 * @return true/false depending on the position of <code>location</code>.
	 */
	public static Altar zoneAltar(Location location)
	{
		if(Altar.getAllAltars() == null) return null;

		for(Altar altar : Altar.getAllAltars())
		{
			if(location.getWorld() != altar.getLocation().getWorld()) continue;
			if(location.distance(altar.getLocation()) <= ALTAR_RADIUS) return altar;
		}
		return null;
	}

	/**
	 * Returns true if <code>from</code> is entering Altar <code>to</code>.
	 * 
	 * @param to location coming from.
	 * @param from location heading to.
	 * @return true/false depending on the direction of movement.
	 */
	public static boolean enterZoneAltar(Location to, Location from)
	{
		return (zoneAltar(from) == null) && zoneAltar(to) != null;
	}

	/**
	 * Returns true if <code>to</code> is leaving Altar <code>from</code>.
	 * 
	 * @param to location coming from.
	 * @param from location heading to.
	 * @return true/false depending on the direction of movement.
	 */
	public static boolean exitZoneAltar(Location to, Location from)
	{
		return enterZoneAltar(from, to);
	}
}
