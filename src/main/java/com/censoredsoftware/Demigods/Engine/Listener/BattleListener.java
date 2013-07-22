package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Battle.Battle;
import com.censoredsoftware.Demigods.Engine.Object.Battle.BattleParticipant;
import com.censoredsoftware.Demigods.Engine.Utility.BattleUtility;

public class BattleListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onDamageBy(EntityDamageByEntityEvent event)
	{
		if(!BattleUtility.canParticipate(event.getEntity()) || !BattleUtility.canParticipate(event.getDamager())) return;

		// Define participants
		BattleParticipant damageeParticipant = BattleUtility.defineParticipant(event.getEntity());
		BattleParticipant damagerParticipant = BattleUtility.defineParticipant(event.getDamager());

		// Calculate midpoint location
		Location midpoint = damagerParticipant.getCurrentLocation().toVector().getMidpoint(event.getEntity().getLocation().toVector()).toLocation(damagerParticipant.getCurrentLocation().getWorld());

		if(!BattleUtility.existsNear(midpoint) && !BattleUtility.existsInRadius(midpoint))
		{
			// Create new battle
			Battle battle = Battle.create(damagerParticipant, damageeParticipant);

			// Teleport if needed
			BattleUtility.teleportIfNeeded(damageeParticipant, battle);
			BattleUtility.teleportIfNeeded(damagerParticipant, battle);

			// Battle death
			if(event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth()) event.setCancelled(BattleUtility.battleDeath(damagerParticipant, damageeParticipant, battle));

			// Debug
			Demigods.message.broadcast(ChatColor.YELLOW + "Battle started involving " + damagerParticipant.getRelatedCharacter().getName() + " and " + damageeParticipant.getRelatedCharacter().getName() + "!");
		}
		else
		{
			// Add to existing battle
			Battle battle = BattleUtility.getInRadius(midpoint);

			// Teleport if needed
			BattleUtility.teleportIfNeeded(damageeParticipant, battle);
			BattleUtility.teleportIfNeeded(damagerParticipant, battle);

			// Add participants from this event
			battle.getMeta().addParticipant(damageeParticipant);
			battle.getMeta().addParticipant(damagerParticipant);

			// Battle death
			if(event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth()) event.setCancelled(BattleUtility.battleDeath(damagerParticipant, damageeParticipant, battle));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageEvent event)
	{
		if(event instanceof EntityDamageByEntityEvent || !BattleUtility.canParticipate(event.getEntity())) return;

		BattleParticipant participant = BattleUtility.defineParticipant(event.getEntity());

		// Battle death
		if(BattleUtility.isInBattle(participant) && event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth()) event.setCancelled(BattleUtility.battleDeath(participant, BattleUtility.getBattle(participant)));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBattleMove(PlayerMoveEvent event)
	{
		if(!BattleUtility.canParticipate(event.getPlayer())) return;
		BattleParticipant participant = BattleUtility.defineParticipant(event.getPlayer());
		event.setCancelled(onBattleMove(event.getTo(), event.getFrom(), participant));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBattleMove(PlayerTeleportEvent event)
	{
		if(!BattleUtility.canParticipate(event.getPlayer())) return;
		BattleParticipant participant = BattleUtility.defineParticipant(event.getPlayer());
		event.setCancelled(onBattleMove(event.getTo(), event.getFrom(), participant));
	}

	private static boolean onBattleMove(Location toLocation, Location fromLocation, BattleParticipant participant)
	{
		boolean to = BattleUtility.existsInRadius(toLocation);
		boolean from = BattleUtility.existsInRadius(fromLocation);
		boolean enter = to == true && from == false;
		boolean exit = to == false && from == true;
		if(enter) BattleUtility.getInRadius(toLocation).getMeta().addParticipant(participant);
		if(exit && BattleUtility.isInBattle(participant)) return true;
		return false;
	}
}
