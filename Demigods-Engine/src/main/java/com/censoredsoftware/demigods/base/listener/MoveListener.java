package com.censoredsoftware.demigods.base.listener;

import com.censoredsoftware.censoredlib.util.Vehicles;
import com.censoredsoftware.demigods.engine.battle.Battle;
import com.censoredsoftware.demigods.engine.battle.Participant;
import com.censoredsoftware.demigods.engine.data.Data;
import com.censoredsoftware.demigods.engine.data.serializable.StructureSave;
import com.censoredsoftware.demigods.engine.language.English;
import com.censoredsoftware.demigods.engine.mythos.StructureType;
import com.censoredsoftware.demigods.engine.util.Configs;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class MoveListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBattleMove(PlayerMoveEvent event)
	{
		onBattleMoveEvent(event.getPlayer(), event.getTo(), event.getFrom());
		onFlagMoveEvent(event.getPlayer(), event.getTo(), event.getFrom());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBattleVehicleMove(VehicleMoveEvent event)
	{
		onBattleMoveEvent(event.getVehicle(), event.getTo(), event.getFrom());
		onFlagMoveEvent(event.getVehicle(), event.getTo(), event.getFrom());
	}

	private static void onBattleMoveEvent(Entity entity, Location to, Location from)
	{
		if(!Configs.getSettingBoolean("battles.enabled") || !Battle.Util.canParticipate(entity) || entity.isInsideVehicle()) return;
		Participant participant = Battle.Util.defineParticipant(entity);
		if(Battle.Util.isInBattle(participant))
		{
			Battle battle = Battle.Util.getBattle(participant);
			boolean toBool = CLocationManager.distanceFlat(to, battle.getStartLocation()) > battle.getRadius();
			boolean fromBool = CLocationManager.distanceFlat(from, battle.getStartLocation()) > battle.getRadius();
			if(toBool && !fromBool) Data.saveTemp((participant.getEntity().getPassenger() == null ? participant.getId().toString() : participant.getRelatedCharacter().getId().toString()), "battle_safe_location", from);
			if(toBool)
			{
				if(Data.hasKeyTemp(participant.getRelatedCharacter().getId().toString(), "battle_safe_location"))
				{
					entity.teleport((Location) Data.getValueTemp(participant.getId().toString(), "battle_safe_location"));
					Data.removeTemp(participant.getId().toString(), "battle_safe_location");
				}
				else Vehicles.teleport(entity, Battle.Util.randomRespawnPoint(battle));
			}
		}
	}

	private static void onFlagMoveEvent(Entity entity, final Location to, final Location from)
	{
		// Handle invisible wall
		if(StructureType.Util.isInRadiusWithFlag(to, StructureType.Flag.RESTRICTED_AREA))
		{
			// Immediately return when possible
			if(!(entity instanceof Player) && !(entity instanceof Vehicle)) return;

			StructureSave save = StructureType.Util.closestInRadiusWithFlag(to, StructureType.Flag.RESTRICTED_AREA);
			if(save == null) return;

			boolean collision = Iterables.any(save.getLocations(), new Predicate<Location>()
			{
				@Override
				public boolean apply(Location location)
				{
					return CLocationManager.distanceFlat(to, location) < 1;
				}
			});

			if(collision)
			{
				// Define the player for messaging later
				Player player = null;

				// Handle teleportation
				if(entity instanceof Player && !save.getType().isAllowed(save, (Player) entity))
				{
					// Set the player and location
					player = (Player) entity;
					Location location = entity.getLocation();

					// Turn them the opposite direction
					if(location.getYaw() > 0) location.setYaw(location.getYaw() - 180);
					else location.setYaw(location.getYaw() + 180);

					entity.teleport(location);
				}
				else if(entity instanceof Vehicle && entity.getPassenger() != null && entity.getPassenger() instanceof Player && !save.getType().isAllowed(save, (Player) entity.getPassenger()))
				{
					// Set the player
					player = (Player) entity.getPassenger();

					// Eject the rider
					entity.eject();
				}

				// Let 'em know what's up
				if(player != null) player.sendMessage(English.NOT_ALLOWED_PAST_INIVISBLE_WALL.getLine());
			}

			// FIXME: Handle entrances from above.
			// FIXME: Handle bugs with vehicles. (e.g. If a player looks backwards and rides into a wall they are teleported to the opposite side. There are likely others.)
		}
	}
}
