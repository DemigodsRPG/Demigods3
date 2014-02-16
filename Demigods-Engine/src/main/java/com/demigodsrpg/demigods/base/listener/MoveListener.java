package com.demigodsrpg.demigods.base.listener;

import com.censoredsoftware.library.util.Vehicles;
import com.demigodsrpg.demigods.engine.battle.Battle;
import com.demigodsrpg.demigods.engine.battle.Participant;
import com.demigodsrpg.demigods.engine.data.TempDataManager;
import com.demigodsrpg.demigods.engine.language.English;
import com.demigodsrpg.demigods.engine.location.DemigodsLocation;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructure;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.demigodsrpg.demigods.engine.util.Configs;
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
		if(!Configs.getSettingBoolean("battles.enabled") || !Battle.canParticipate(entity) || entity.isInsideVehicle()) return;
		Participant participant = Battle.defineParticipant(entity);
		if(Battle.isInBattle(participant))
		{
			Battle battle = Battle.getBattle(participant);
			boolean toBool = DemigodsLocation.distanceFlat(to, battle.getStartLocation().getBukkitLocation()) > battle.getRadius();
			boolean fromBool = DemigodsLocation.distanceFlat(from, battle.getStartLocation().getBukkitLocation()) > battle.getRadius();
			if(toBool && !fromBool) TempDataManager.saveTemp((participant.getEntity().getPassenger() == null ? participant.getId().toString() : participant.getRelatedCharacter().getId().toString()), "battle_safe_location", from);
			if(toBool)
			{
				if(TempDataManager.hasKeyTemp(participant.getRelatedCharacter().getId().toString(), "battle_safe_location"))
				{
					entity.teleport((Location) TempDataManager.getValueTemp(participant.getId().toString(), "battle_safe_location"));
					TempDataManager.removeTemp(participant.getId().toString(), "battle_safe_location");
				}
				else Vehicles.teleport(entity, Battle.randomRespawnPoint(battle));
			}
		}
	}

	private static void onFlagMoveEvent(Entity entity, final Location to, final Location from)
	{
		// Handle invisible wall
		if(DemigodsStructureType.Util.isInRadiusWithFlag(to, DemigodsStructureType.Flag.RESTRICTED_AREA))
		{
			// Immediately return when possible
			if(!(entity instanceof Player) && !(entity instanceof Vehicle)) return;

			DemigodsStructure save = DemigodsStructureType.Util.closestInRadiusWithFlag(to, DemigodsStructureType.Flag.RESTRICTED_AREA);
			if(save == null) return;

			boolean collision = Iterables.any(save.getBukkitLocations(), new Predicate<Location>()
			{
				@Override
				public boolean apply(Location location)
				{
					return DemigodsLocation.distanceFlat(to, location) < 1;
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
