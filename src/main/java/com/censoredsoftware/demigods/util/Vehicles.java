package com.censoredsoftware.demigods.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTeleportEvent;

public class Vehicles
{
	public static void teleport(final Entity entity, final Location to)
	{
		EntityTeleportEvent event = new EntityTeleportEvent(entity, entity.getLocation(), to);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) return;
		if(entity.isInsideVehicle())
		{
			Entity vehicle = entity.getVehicle();
			vehicle.eject();
			vehicle.teleport(to);
			entity.teleport(to);
			vehicle.setPassenger(entity);
		}
		else entity.teleport(to);
	}
}
