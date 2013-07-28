package com.censoredsoftware.demigods.engine.battle;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.censoredsoftware.demigods.engine.util.Messages;

public class BattleListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onDamageBy(EntityDamageByEntityEvent event)
	{
		Entity damager = event.getDamager();
		if(damager instanceof Projectile) damager = ((Projectile) damager).getShooter();
		if(!Battle.Util.canParticipate(event.getEntity()) || !Battle.Util.canParticipate(damager)) return;

		// Define participants
		Battle.Participant damageeParticipant = Battle.Util.defineParticipant(event.getEntity());
		Battle.Participant damagerParticipant = Battle.Util.defineParticipant(damager);

		// Calculate midpoint location
		Location midpoint = damagerParticipant.getCurrentLocation().toVector().getMidpoint(event.getEntity().getLocation().toVector()).toLocation(damagerParticipant.getCurrentLocation().getWorld());

		if(!Battle.Util.existsNear(midpoint) && !Battle.Util.existsInRadius(midpoint))
		{
			// Create new battle
			Battle battle = Battle.Util.create(damagerParticipant, damageeParticipant);

			// Teleport if needed
			// Battle.teleportIfNeeded(damageeParticipant, battle);
			// Battle.teleportIfNeeded(damagerParticipant, battle);

			// Battle death
			if(event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth()) event.setCancelled(Battle.Util.battleDeath(damagerParticipant, damageeParticipant, battle));

			// Debug
			Messages.broadcast(ChatColor.YELLOW + "Battle started involving " + damagerParticipant.getRelatedCharacter().getName() + " and " + damageeParticipant.getRelatedCharacter().getName() + "!");
		}
		else
		{
			// Add to existing battle
			Battle battle = Battle.Util.getNear(midpoint) != null ? Battle.Util.getNear(midpoint) : Battle.Util.getInRadius(midpoint);

			// Teleport if needed
			// Battle.teleportIfNeeded(damageeParticipant, battle);
			// Battle.teleportIfNeeded(damagerParticipant, battle);

			// Add participants from this event
			battle.getMeta().addParticipant(damageeParticipant);
			battle.getMeta().addParticipant(damagerParticipant);

			// Battle death
			if(event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth()) event.setCancelled(Battle.Util.battleDeath(damagerParticipant, damageeParticipant, battle));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageEvent event)
	{
		if(event instanceof EntityDamageByEntityEvent || !Battle.Util.canParticipate(event.getEntity())) return;

		Battle.Participant participant = Battle.Util.defineParticipant(event.getEntity());

		// Battle death
		if(Battle.Util.isInBattle(participant) && event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth()) event.setCancelled(Battle.Util.battleDeath(participant, Battle.Util.getBattle(participant)));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	// TODO Doesn't work.
	public void onBattleMove(PlayerMoveEvent event)
	{
		if(!Battle.Util.canParticipate(event.getPlayer())) return;
		Battle.Participant participant = Battle.Util.defineParticipant(event.getPlayer());
		if(onBattleMove(event.getTo(), event.getFrom(), participant)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	// TODO Doesn't work.
	public void onBattleMove(PlayerTeleportEvent event)
	{
		if(!Battle.Util.canParticipate(event.getPlayer())) return;
		Battle.Participant participant = Battle.Util.defineParticipant(event.getPlayer());
		if(onBattleMove(event.getTo(), event.getFrom(), participant)) event.setCancelled(true);
	}

	private static boolean onBattleMove(Location toLocation, Location fromLocation, Battle.Participant participant)
	{
		boolean to = Battle.Util.existsInRadius(toLocation);
		boolean from = Battle.Util.existsInRadius(fromLocation);
		boolean enter = to && !from;
		boolean exit = !to && from;
		if(enter) Battle.Util.getInRadius(toLocation).getMeta().addParticipant(participant);
		return exit && Battle.Util.isInBattle(participant);
	}
}
