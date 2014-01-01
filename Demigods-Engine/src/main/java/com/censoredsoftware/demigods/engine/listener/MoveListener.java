package com.censoredsoftware.demigods.engine.listener;

import com.censoredsoftware.censoredlib.util.Vehicles;
import com.censoredsoftware.demigods.engine.data.*;
import com.censoredsoftware.demigods.engine.mythos.Structure;
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
			if(toBool && !fromBool) DataManager.saveTemp((participant.getEntity().getPassenger() == null ? participant.getId().toString() : participant.getRelatedCharacter().getId().toString()), "battle_safe_location", from);
			if(toBool)
			{
				if(DataManager.hasKeyTemp(participant.getRelatedCharacter().getId().toString(), "battle_safe_location"))
				{
					entity.teleport((Location) DataManager.getValueTemp(participant.getId().toString(), "battle_safe_location"));
					DataManager.removeTemp(participant.getId().toString(), "battle_safe_location");
				}
				else Vehicles.teleport(entity, Battle.Util.randomRespawnPoint(battle));
			}
		}
	}

	private static void onFlagMoveEvent(Entity entity, final Location to, final Location from)
	{
		if(Structure.Util.isInRadiusWithFlag(to, Structure.Flag.INVISIBLE_WALL))
		{
			StructureData data = Structure.Util.closestInRadiusWithFlag(to, Structure.Flag.INVISIBLE_WALL);
			if(data == null) return;
			boolean toBool = Iterables.any(data.getLocations(), new Predicate<Location>()
			{
				@Override
				public boolean apply(Location location)
				{
					return CLocationManager.distanceFlat(to, location) < 1;
				}
			});
			boolean fromBool = Iterables.any(data.getLocations(), new Predicate<Location>()
			{
				@Override
				public boolean apply(Location location)
				{
					return CLocationManager.distanceFlat(from, location) < 1;
				}
			});
			if(toBool && !fromBool && entity instanceof Player && !data.getType().isAllowed((Player) entity)) DataManager.saveTemp(DPlayer.Util.getPlayer((Player) entity).getMojangAccount(), "invisible_wall_location", from);
			if(toBool)
			{
				if(entity instanceof Player && data.getType().isAllowed((Player) entity)) return;
				if(entity instanceof Vehicle) entity.eject();
				if(!(entity instanceof Player)) return;
				if(DataManager.hasKeyTemp(DPlayer.Util.getPlayer((Player) entity).getMojangAccount(), "invisible_wall_location"))
				{
					entity.teleport((Location) DataManager.getValueTemp(DPlayer.Util.getPlayer((Player) entity).getMojangAccount(), "invisible_wall_location"));
					DataManager.removeTemp(DPlayer.Util.getPlayer((Player) entity).getMojangAccount(), "invisible_wall_location");
				}
			}
		}
	}
}
